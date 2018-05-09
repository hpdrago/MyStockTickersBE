package com.stocktracker.servicelayer.service.cache.stockpricequote;

import com.stocktracker.AppConfig;
import com.stocktracker.servicelayer.service.cache.common.InformationCacheBaseCacheServiceExecutor;
import com.stocktracker.servicelayer.service.cache.common.InformationCacheEntryState;
import com.stocktracker.servicelayer.service.cache.common.InformationCacheFetchState;
import com.stocktracker.servicelayer.service.cache.common.InformationCacheServiceExecutor;
import io.reactivex.processors.BehaviorProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * This class makes the calls to the IEXTrading API to get the Stock Price: https://iextrading.com/developer/docs/#price
 * and to Yahoo as well.
 */
@Service
// Proxy target class to get past implementation of the interface and getting a runtime proxy error.
@EnableAsync(proxyTargetClass = true)
public class StockPriceQuoteServiceExecutor extends InformationCacheBaseCacheServiceExecutor<String,StockPriceQuote>
                                            implements InformationCacheServiceExecutor<String,StockPriceQuote>
{
    private StockPriceServiceExecutor stockPriceServiceExecutor;

    /**
     * Fetches the StockQuote synchronously.
     * @param tickerSymbol The ticker symbol to search for.
     * @return
     */
    @Override
    public Optional<StockPriceQuote> synchronousFetch( final String tickerSymbol )
    {
        final String methodName = "synchronousFetch";
        logMethodBegin( methodName, tickerSymbol );
        GetStockPriceResult getStockPriceResult = stockPriceServiceExecutor.synchronousGetStockPrice( tickerSymbol );
        this.handleGetStockPriceResult( getStockPriceResult );
        return Optional.ofNullable( stockQuoteEntity );
    }

    /**
     * This method is called when a stock quote has been retrieved from the stock quote service.
     * The stock quote is added to the cache and its state is set to CURRENT.
     * @param getStockPriceResult
     */
    private void handleGetStockPriceResult( final GetStockPriceResult getStockPriceResult )
    {
        Objects.requireNonNull( getStockPriceResult, "getStockPriceResult cannot be null" );
        final String methodName = "handleGetStockPriceResult";
        final String tickerSymbol = getStockPriceResult.getTickerSymbol();
        logMethodBegin( methodName, getStockPriceResult );
        StockPriceQuoteCacheEntry stockPriceQuoteCacheEntry = this..get( getStockPriceResult.getTickerSymbol() );
        stockPriceQuoteCacheEntry.setFetchState( InformationCacheFetchState.NOT_FETCHING );
        /*
         * Set the results in the cache entry and notify any observers waiting for the stock price update.
         */
        switch ( getStockPriceResult.getStockPriceFetchResult() )
        {
            case SUCCESS:
                /*
                 * Create the stock_company table entry if needed and if we haven't already validated this stock.
                 */
                if ( !stockPriceQuoteCacheEntry.isStockTableEntryValidated() )
                {
                    this.stockCompanyEntityService.checkStockTableEntry( tickerSymbol );
                    stockPriceQuoteCacheEntry.setStockTableEntryValidated( true );
                }
                stockPriceQuoteCacheEntry.setStockPrice( getStockPriceResult.getStockPrice() );
                stockPriceQuoteCacheEntry.setCacheState( InformationCacheEntryState.CURRENT );
                break;

            case NOT_FOUND:
                stockPriceQuoteCacheEntry.setCacheState( InformationCacheEntryState.NOT_FOUND );
                stockPriceQuoteCacheEntry.setStockPrice( new BigDecimal( 0 ));
                break;

            case EXCEPTION:
                stockPriceQuoteCacheEntry.setCacheState( InformationCacheEntryState.FAILURE );
                stockPriceQuoteCacheEntry.setFetchException( getStockPriceResult.getException() );
                logError( methodName, getStockPriceResult.getException() );

                /*
                 * Mark the stock_company table entry as discontinued if the stock was not found.
                 */
            case DISCONTINUED:
                this.stockCompanyEntityService.markStockAsDiscontinued( tickerSymbol );
                stockPriceQuoteCacheEntry.setDiscontinued( true );
                break;
        }
        stockPriceQuoteCacheEntry.notifySubscribers();
        logMethodEnd( methodName );
    }

    /**
     * This method, when called, starts on a new thread launched and managed by the Spring container.
     * In the new thread, the stock quote will be retrieved and the caller will be notified through the {@code observable}
     * @param tickerSymbol
     * @param subject Behaviour subject to use to notify the caller that the request has been completed.
     */
    @Async( AppConfig.STOCK_QUOTE_THREAD_POOL )
    @Override
    public void asynchronousFetch( final String tickerSymbol, final BehaviorProcessor<Optional<StockPriceQuote>> subject )
    {
        final String methodName = "asynchronousFetch";
        logMethodBegin( methodName, tickerSymbol );
        super.asynchronousFetch( tickerSymbol, subject );
        logMethodEnd( methodName );
    }

    @Autowired
    public void setStockPriceServiceExecutor( final StockPriceServiceExecutor stockPriceServiceExecutor )
    {
        this.stockPriceServiceExecutor = stockPriceServiceExecutor;
    }
}
