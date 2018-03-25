package com.stocktracker.servicelayer.stockinformationprovider;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockQuoteUnavailableException;
import com.stocktracker.servicelayer.service.StockEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * This cache holds stocks that will expire after the set amount of time
 */
@Service
public class StockQuoteCache implements MyLogger, HandleStockQuoteReturn
{
    /**
     * IEXTrading quotes are delayed 15 minutes.
     */
    public static final long EXPIRATION_TIME = TimeUnit.MINUTES.toMillis( 15 );
    private Map<String, StockTickerQuoteCacheEntry> cacheEntryMap = Collections.synchronizedMap( new HashMap<>( ) );
    private StockQuoteServiceExecutor stockQuoteServiceExecutor;
    private StockEntityService stockService;

    public StockQuoteCache()
    {
    }

    public void updateLastPrice( @NotNull final String tickerSymbol, @NotNull final BigDecimal lastPrice )
    {
        final String methodName = "updateLastPrice";
        logMethodBegin( methodName, tickerSymbol, lastPrice );
        StockTickerQuoteCacheEntry stockTickerQuoteCacheEntry = this.cacheEntryMap.get( tickerSymbol );
        /*
         * If we don't have a full quote, go get it now.
         */
        if ( stockTickerQuoteCacheEntry == null )
        {
            try
            {
                this.synchronousQuoteFetch( tickerSymbol );
            }
            catch( Exception e )
            {
                logError( methodName, e );
            }
        }
        else
        {
            stockTickerQuoteCacheEntry.setLastPrice( lastPrice );
        }
    }

    /**
     * Get the stock quote information for the {@code tickerSymbol}
     * @param tickerSymbol
     * @param fetchMode
     * @return
     * @throws StockQuoteUnavailableException
     * @throws StockNotFoundException
     */
    public StockQuote getStockQuote( @NotNull String tickerSymbol, @NotNull final StockQuoteFetchMode fetchMode )
        throws StockQuoteUnavailableException,
               StockNotFoundException
    {
        final String methodName = "getStockQuote";
        logMethodBegin( methodName, tickerSymbol, fetchMode );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Objects.requireNonNull( fetchMode, "fetchMode cannot be null" );
        StockTickerQuoteCacheEntry stockTickerQuoteCacheEntry = this.cacheEntryMap.get( tickerSymbol );
        /*
         * If null, the stock is not in the cache
         */
        if ( stockTickerQuoteCacheEntry == null )
        {
            if ( fetchMode == StockQuoteFetchMode.SYNCHRONOUS )
            {
                stockTickerQuoteCacheEntry = synchronousQuoteFetch( tickerSymbol );
            }
            else
            {
                stockTickerQuoteCacheEntry = StockTickerQuoteCacheEntry.newInstance( tickerSymbol,
                                                                                     StockQuoteState.NOT_CACHED );
                this.asynchronousQuoteFetch( tickerSymbol );
            }
        }
        else
        {
            if ( System.currentTimeMillis() - stockTickerQuoteCacheEntry.getLastQuoteRefreshTime() > EXPIRATION_TIME )
            {
                if ( fetchMode == StockQuoteFetchMode.SYNCHRONOUS )
                {
                    logDebug( methodName, "{0} is stale", tickerSymbol );
                    stockTickerQuoteCacheEntry = synchronousQuoteFetch( tickerSymbol );
                }
                else
                {
                    /*
                     * Set the cached value to stale and return that value to the caller but also
                     * submit a task to refresh the stock quote
                     */
                    stockTickerQuoteCacheEntry.setStockQuoteState( StockQuoteState.STALE );
                    this.asynchronousQuoteFetch( tickerSymbol );
                }
            }
            else
            {
                stockTickerQuoteCacheEntry.setStockQuoteState( StockQuoteState.CURRENT );
            }
        }
        logMethodEnd( methodName, stockTickerQuoteCacheEntry );
        return stockTickerQuoteCacheEntry;
    }

    /**
     * Creates a StockQuoteCacheEntry and submits a task to refresh the stock quote.
     * @param tickerSymbol
     * @throws StockNotFoundException
     * @throws StockQuoteUnavailableException
     */
    private void asynchronousQuoteFetch( final @NotNull String tickerSymbol )
        throws StockNotFoundException,
               StockQuoteUnavailableException
    {
        final String methodName = "asynchronousQuoteFetch";
        logMethodBegin( methodName, tickerSymbol );
        this.stockQuoteServiceExecutor.asynchronousGetStockQuote( tickerSymbol, this );
        logMethodEnd( methodName );
    }

    /**
     * This method is called when a stock quote has been retrieved from the stock quote service.
     * The stock quote is added to the cache and its state is set to CURRENT.
     * @param stockTickerQuote
     * @throws StockQuoteUnavailableException
     * @throws StockNotFoundException
     */
    @Override
    public void handleStockQuoteReturn( final StockTickerQuote stockTickerQuote )
        throws StockQuoteUnavailableException,
               StockNotFoundException
    {
        final String methodName = "handleStockQuoteReturn";
        logMethodBegin( methodName, stockTickerQuote );
        if ( stockTickerQuote != null )
        {
            this.cacheStockQuote( stockTickerQuote );
            this.stockService.checkStockTableEntry( stockTickerQuote.getTickerSymbol() );
            StockTickerQuoteCacheEntry stockTickerQuoteCacheEntry = this.cacheEntryMap.get( stockTickerQuote.getTickerSymbol() );
            stockTickerQuoteCacheEntry.setStockTableEntryValidated( true );
        }
        logMethodEnd( methodName );
    }

    /**
     * Synchronous fetch from stock quote provider
     * @param tickerSymbol
     * @return
     * @throws StockQuoteUnavailableException
     * @throws StockNotFoundException
     */
    private StockTickerQuoteCacheEntry synchronousQuoteFetch( final @NotNull String tickerSymbol )
        throws StockQuoteUnavailableException,
               StockNotFoundException
    {
        final String methodName = "synchronousQuoteFetch";
        logMethodBegin( methodName, tickerSymbol );
        StockTickerQuote stockTickerQuote = this.stockQuoteServiceExecutor
                                                .synchronousGetStockQuote( tickerSymbol );
        if ( stockTickerQuote == null )
        {
            throw new StockNotFoundException( tickerSymbol + " is not a valid ticker symbol" );
        }
        StockTickerQuoteCacheEntry stockTickerQuoteCacheEntry = this.cacheStockQuote( stockTickerQuote );
        if ( !stockTickerQuoteCacheEntry.isStockTableEntryValidated() )
        {
            this.stockService.checkStockTableEntry( stockTickerQuote.getTickerSymbol() );
            stockTickerQuoteCacheEntry.setStockTableEntryValidated( true );
        }
        logMethodEnd( methodName, stockTickerQuote );
        return stockTickerQuoteCacheEntry;
    }

    /**
     * Puts {@code stockTickerQuote} into the cache (map).
     * @param stockTickerQuote
     * @return
     */
    public StockTickerQuoteCacheEntry cacheStockQuote( final @NotNull StockTickerQuote stockTickerQuote )
    {
        Objects.requireNonNull( stockTickerQuote, "stockTickerQuote cannot be null" );
        stockTickerQuote.setStockQuoteState( StockQuoteState.CURRENT );
        StockTickerQuoteCacheEntry stockTickerQuoteCacheEntry = StockTickerQuoteCacheEntry.newInstance( stockTickerQuote );
        this.cacheEntryMap.put( stockTickerQuoteCacheEntry.getTickerSymbol(), stockTickerQuoteCacheEntry );
        return stockTickerQuoteCacheEntry;
    }

    @Autowired
    public void setStockQuoteServiceExecutor( final StockQuoteServiceExecutor stockQuoteServiceExecutor )
    {
        this.stockQuoteServiceExecutor = stockQuoteServiceExecutor;
    }

    @Autowired
    public void setStockService( final StockEntityService stockService )
    {
        this.stockService = stockService;
    }
}
