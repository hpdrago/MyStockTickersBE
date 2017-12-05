package com.stocktracker.servicelayer.stockinformationprovider;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockQuoteUnavailableException;
import com.stocktracker.servicelayer.service.StockContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This cache holds stocks that will expire after the set amount of time
 */
@Service
public class StockQuoteCache implements MyLogger, HandleStockQuoteReturn
{
    public static final long EXPIRATION_TIME = 5 * 60 * 1000; // 5 min
    private Map<String, StockTickerQuoteCacheEntry> cacheEntryMap = Collections.synchronizedMap( new HashMap<>( ) );
    private StockQuoteServiceExecutor stockQuoteServiceExecutor;
    private StockContainerService stockService;

    public StockQuoteCache()
    {
    }

    /**
     * Get the stock quote information for the {@code tickerSymbol}
     *
     * @param tickerSymbol The stock symbol
     * @param fetchMode Identified whether this is a synchronous or asynchronous call
     * @return
     */
    public StockQuote getStockQuote( @NotNull String tickerSymbol, final StockQuoteFetchMode fetchMode )
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
     */
    @Override
    public void handleStockQuoteReturn( final StockTickerQuote stockTickerQuote )
        throws StockQuoteUnavailableException,
               StockNotFoundException
    {
        final String methodName = "handleStockQuoteReturn";
        logMethodBegin( methodName, stockTickerQuote );
        this.cacheStockQuote( stockTickerQuote );
        this.stockService.checkStockTableEntry( stockTickerQuote.getTickerSymbol() );
        StockTickerQuoteCacheEntry stockTickerQuoteCacheEntry = this.cacheEntryMap.get( stockTickerQuote.getTickerSymbol() );
        stockTickerQuoteCacheEntry.setStockTableEntryValidated( true );
        logMethodEnd( methodName );
    }

    /**
     * Synchronous fetch from stock quote provider
     * @param tickerSymbol
     * @return
     */
    private StockTickerQuoteCacheEntry synchronousQuoteFetch( final @NotNull String tickerSymbol )
        throws StockQuoteUnavailableException,
               StockNotFoundException
    {
        final String methodName = "synchronousQuoteFetch";
        logMethodBegin( methodName, tickerSymbol );
        StockTickerQuote stockTickerQuote = this.stockQuoteServiceExecutor.synchronousGetStockQuote( tickerSymbol );
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
    public void setStockService( final StockContainerService stockService )
    {
        this.stockService = stockService;
    }
}
