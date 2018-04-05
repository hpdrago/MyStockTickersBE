package com.stocktracker.servicelayer.stockinformationprovider;

import com.stocktracker.common.MyLogger;
import com.stocktracker.servicelayer.service.StockCompanyEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * This cache holds stocks that will expire after the set amount of time
 */
@Service
public class StockPriceCache implements MyLogger, HandleStockQuoteResult
{
    /**
     * IEXTrading quotes are delayed 15 minutes.
     */
    public static final long EXPIRATION_TIME = TimeUnit.MINUTES.toMillis( 15 );
    private Map<String, StockPriceCacheEntry> cacheEntryMap = Collections.synchronizedMap( new HashMap<>( ) );
    private StockPriceServiceExecutor stockPriceServiceExecutor;
    private StockCompanyEntityService stockCompanyEntityService;

    public StockPriceCache()
    {
    }

    /**
     * Updates the stock price cache for the ticker symbol.
     * @param tickerSymbol
     * @param stockPrice
     */
    public void updateStockPrice( @NotNull final String tickerSymbol, @NotNull final BigDecimal stockPrice )
    {
        final String methodName = "updateStockPrice";
        logMethodBegin( methodName, tickerSymbol, stockPrice );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Objects.requireNonNull( stockPriceServiceExecutor,  "stockPrice cannot be null" );
        final StockPriceCacheEntry stockPriceCacheEntry = this.cacheEntryMap.get( tickerSymbol );
        if ( stockPriceCacheEntry == null )
        {
            this.synchronousPriceFetch( stockPriceCacheEntry );
        }
        else
        {
            stockPriceCacheEntry.setStockPrice( stockPrice );
        }
    }

    /**
     * Get the stock quote information for the {@code tickerSymbol}
     * @param tickerSymbol
     * @param fetchMode
     * @return
     */
    public StockPriceCacheEntry getStockPrice( @NotNull String tickerSymbol,
                                               @NotNull final StockPriceFetchMode fetchMode )
    {
        final String methodName = "getStockPrice";
        logMethodBegin( methodName, tickerSymbol, fetchMode );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Objects.requireNonNull( fetchMode, "fetchMode cannot be null" );
        StockPriceCacheEntry stockPriceCacheEntry = this.cacheEntryMap.get( tickerSymbol );
        /*
         * If null, the stock is not in the cache
         */
        if ( stockPriceCacheEntry == null )
        {
            stockPriceCacheEntry = StockPriceCacheEntry.newInstance( tickerSymbol );
            stockPriceCacheEntry.setFetchState( StockPriceFetchState.FETCHING );
            stockPriceCacheEntry.setCacheState( StockPriceCacheState.STALE );
            this.cacheEntryMap
                .put( stockPriceCacheEntry.getTickerSymbol(), stockPriceCacheEntry );
            if ( fetchMode == StockPriceFetchMode.SYNCHRONOUS )
            {
                this.synchronousPriceFetch( stockPriceCacheEntry );
            }
            else
            {
                stockPriceCacheEntry = StockPriceCacheEntry.newInstance( tickerSymbol );
                this.asynchronousPriceFetch( stockPriceCacheEntry );
            }
        }
        else
        {
            if ( System.currentTimeMillis() - stockPriceCacheEntry.getStockPriceRefreshTime() > EXPIRATION_TIME )
            {
                /*
                 * Set the cached value to stale
                 */
                stockPriceCacheEntry.setCacheState( StockPriceCacheState.STALE );
                switch ( stockPriceCacheEntry.getFetchState() )
                {
                    case FETCHING:
                        break;
                    case NOT_FETCHING:
                        if ( fetchMode == StockPriceFetchMode.SYNCHRONOUS )
                        {
                            logDebug( methodName, "{0} is stale", tickerSymbol );
                            this.synchronousPriceFetch( stockPriceCacheEntry );
                        }
                        else
                        {
                            this.asynchronousPriceFetch( stockPriceCacheEntry );
                        }
                        break;
                }
            }
        }
        logMethodEnd( methodName, stockPriceCacheEntry );
        return stockPriceCacheEntry;
    }

    /**
     * Submits a task to get/refresh the stock price.
     * @param stockPriceCacheEntry
     */
    private void asynchronousPriceFetch( final StockPriceCacheEntry stockPriceCacheEntry )
    {
        Objects.requireNonNull( stockPriceCacheEntry, "stockPriceCacheEntry cannot be null" );
        final String methodName = "asynchronousPriceFetch";
        final String tickerSymbol = stockPriceCacheEntry.getTickerSymbol();
        logMethodBegin( methodName, tickerSymbol );
        stockPriceCacheEntry.setFetchState( StockPriceFetchState.FETCHING );
        this.stockPriceServiceExecutor
            .asynchronousGetStockPrice( tickerSymbol, this );
        logMethodEnd( methodName );
    }

    /**
     * Synchronous fetch from stock quote provider
     * @param stockPriceCacheEntry
     */
    private void synchronousPriceFetch( final StockPriceCacheEntry stockPriceCacheEntry )
    {
        Objects.requireNonNull( stockPriceCacheEntry, "stockPriceCacheEntry cannot be null" );
        final String methodName = "synchronousPrice";
        final String tickerSymbol = stockPriceCacheEntry.getTickerSymbol();
        logMethodBegin( methodName, tickerSymbol );
        stockPriceCacheEntry.setFetchState( StockPriceFetchState.FETCHING );
        GetStockPriceResult stockPriceResult = this.stockPriceServiceExecutor
                                                   .synchronousGetStockPrice( tickerSymbol );
        this.handleGetStockPriceResult( stockPriceResult );
        logMethodEnd( methodName, stockPriceCacheEntry );
    }

    /**
     * This method is called when a stock quote has been retrieved from the stock quote service.
     * The stock quote is added to the cache and its state is set to CURRENT.
     * @param getStockPriceResult
     */
    @Override
    public void handleGetStockPriceResult( final GetStockPriceResult getStockPriceResult )
    {
        Objects.requireNonNull( getStockPriceResult, "getStockPriceResult cannot be null" );
        final String methodName = "handleGetStockPriceResult";
        final String tickerSymbol = getStockPriceResult.getTickerSymbol();
        logMethodBegin( methodName, getStockPriceResult );
        StockPriceCacheEntry stockPriceCacheEntry = this.cacheEntryMap.get( getStockPriceResult.getTickerSymbol() );
        stockPriceCacheEntry.setFetchState( StockPriceFetchState.NOT_FETCHING );
        /*
         * Create the stock_company table entry if needed and if we haven't already validated this stock.
         */
        if ( !stockPriceCacheEntry.isStockTableEntryValidated() )
        {
            this.stockCompanyEntityService.checkStockTableEntry( tickerSymbol );
            stockPriceCacheEntry.setStockTableEntryValidated( true );
        }
        /*
         * Mark the stock_company table entry as discontinued if the stock was not found.
         */
        if ( getStockPriceResult.getStockPriceFetchResult().isDiscontinued() )
        {
            this.stockCompanyEntityService.markStockAsDiscontinued( tickerSymbol );
            stockPriceCacheEntry.setDiscontinued( true );

        }
        /*
         * Set the results in the cache entry and notify any observers waiting for the stock price update.
         */
        switch ( getStockPriceResult.getStockPriceFetchResult() )
        {
            case SUCCESS:
                stockPriceCacheEntry.setStockPrice( getStockPriceResult.getStockPrice() );
                stockPriceCacheEntry.setCacheState( StockPriceCacheState.CURRENT );
                break;

            case NOT_FOUND:
                stockPriceCacheEntry.setCacheState( StockPriceCacheState.NOT_FOUND );
                break;

            case EXCEPTION:
                stockPriceCacheEntry.setCacheState( StockPriceCacheState.FAILURE );
                stockPriceCacheEntry.setFetchException( getStockPriceResult.getException() );
                logError( methodName, getStockPriceResult.getException() );
                break;
        }
        stockPriceCacheEntry.notifySubscribers();
        logMethodEnd( methodName );
    }

    @Override
    public void handleGetStockPriceResults( final List<GetStockPriceResult> getStockPriceResults )
    {

    }

    @Autowired
    public void setStockPriceServiceExecutor( final StockPriceServiceExecutor stockPriceServiceExecutor )
    {
        this.stockPriceServiceExecutor = stockPriceServiceExecutor;
    }

    @Autowired
    public void setStockService( final StockCompanyEntityService stockCompanyEntityService )
    {
        this.stockCompanyEntityService = stockCompanyEntityService;
    }
}
