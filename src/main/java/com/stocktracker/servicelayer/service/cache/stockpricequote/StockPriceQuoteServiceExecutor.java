package com.stocktracker.servicelayer.service.cache.stockpricequote;

import com.stocktracker.AppConfig;
import com.stocktracker.servicelayer.service.IEXTradingStockService;
import com.stocktracker.servicelayer.service.StockCompanyEntityService;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDataNotFoundException;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheServiceExecutor;
import com.stocktracker.servicelayer.service.cache.common.BaseAsyncCacheBatchServiceExecutor;
import io.reactivex.processors.AsyncProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.stocktracker.servicelayer.service.cache.stockpricequote.StockPriceFetchResult.DISCONTINUED;
import static com.stocktracker.servicelayer.service.cache.stockpricequote.StockPriceFetchResult.NOT_FOUND;
import static com.stocktracker.servicelayer.service.cache.stockpricequote.StockPriceFetchResult.SUCCESS;

/**
 * This class makes the calls to the IEXTrading API to get the Stock Price: https://iextrading.com/developer/docs/#price
 * and to Yahoo as well.
 */
@Service
// Proxy target class to get past implementation of the interface and getting a runtime proxy error.
@EnableAsync(proxyTargetClass = true)
public class StockPriceQuoteServiceExecutor extends BaseAsyncCacheBatchServiceExecutor<String,
                                                                                       StockPriceQuote,
                                                                                       String,
                                                                                       GetStockPriceResult,
                                                                                       StockQuoteEntityCacheRequestKey,
                                                                                       StockPriceQuoteCacheRequest,
                                                                                       StockPriceQuoteCacheResponse>
    implements AsyncCacheServiceExecutor<String,StockPriceQuote,String>
{
    @Autowired
    private StockPriceServiceExecutor stockPriceServiceExecutor;

    @Autowired
    private StockPriceQuoteCache stockPriceQuoteCache;

    @Autowired
    private StockCompanyEntityService stockCompanyEntityService;

    @Autowired
    private IEXTradingStockService iexTradingStockService;

    /**
     * Get stock price quotes for a list of ticker symbols.
     * @param requestKeys
     * @return
     */
    @Override
    protected List<GetStockPriceResult> batchFetch( final List<StockQuoteEntityCacheRequestKey> requestKeys )
    {
        final String methodName = "batchFetch";
        logMethodBegin( methodName, requestKeys );

        /*
         * Extract the ticker symbols from the request keys.
         */
        final List<String> tickerSymbols = requestKeys.stream()
                                                      .map( requestKey -> requestKey.getASyncKey() )
                                                      .collect(Collectors.toList() );

        /*
         * Make the batch stock price request
         */
        final Map<String,BigDecimal> stockPriceResults = this.iexTradingStockService
                                                             .getStockPrices( tickerSymbols );
        /*
         * Check to see if we got different results.
         */
        if ( stockPriceResults.size() != tickerSymbols.size() )
        {
            logWarn( methodName, "Did not receive all stock prices requested: {0} received: {1}",
                     tickerSymbols.size(), stockPriceResults.size() );
        }
        final List<StockPriceQuote> stockPriceQuotes = new ArrayList<>( tickerSymbols.size() );
        stockPriceResults.forEach( ( String tickerSymbol, BigDecimal stockPrice ) ->
                                   {
                                       final StockPriceQuote stockPriceQuote = this.context.getBean( StockPriceQuote.class );
                                       stockPriceQuote.setTickerSymbol( tickerSymbol );
                                       stockPriceQuote.setCacheState( AsyncCacheEntryState.CURRENT );
                                       stockPriceQuote.setLastPrice( stockPrice );
                                       stockPriceQuote.setCacheError( null );
                                       stockPriceQuotes.add( stockPriceQuote );
                                   });
        logMethodEnd( stockPriceQuotes.size() + " stock quotes" );
        return stockPriceQuotes;
    }

    /**
     * Fetches the StockQuote synchronously.
     * @param cacheKey The ticker symbol to search for.
     * @param asyncKey Not used since the ticker symbol contained in cacheKey are the same.
     * @return
     */

    @Override
    public StockPriceQuote getASyncData( final String cacheKey, final String asyncKey )
        throws AsyncCacheDataNotFoundException
    {
        final String methodName = "getASyncData";
        logMethodBegin( methodName, cacheKey );
        final GetStockPriceResult stockPriceResult = stockPriceServiceExecutor.synchronousGetStockPrice( cacheKey );
        final StockPriceQuote stockPriceQuote = convertASyncData( cacheKey, asyncKey, stockPriceResult );
        logMethodEnd( methodName, stockPriceQuote );
        return stockPriceQuote;
    }


    /**
     * Process the stock price quote result returned from the stock price service executor.
     * @param cacheKey
     * @param asyncKey
     * @param stockPriceResult
     * @return
     */
    @Override
    protected StockPriceQuote convertASyncData( final String cacheKey, final String asyncKey,
                                                final GetStockPriceResult stockPriceResult )
        throws AsyncCacheDataNotFoundException
    {
        final String methodName = "convertASyncData";
        logMethodBegin( methodName, cacheKey, asyncKey, stockPriceResult );
        final StockPriceQuote stockPriceQuote = this.context.getBean( StockPriceQuote.class );
        final StockPriceQuoteCacheEntry stockPriceQuoteCacheEntry = this.stockPriceQuoteCache
                                                                        .getCacheEntry( stockPriceResult.getTickerSymbol() );
        stockPriceQuote.setTickerSymbol( stockPriceResult.getTickerSymbol() );
        switch ( stockPriceResult.getStockPriceFetchResult() )
        {
            case DISCONTINUED:
                this.stockCompanyEntityService
                    .markStockAsDiscontinued( stockPriceResult.getTickerSymbol() );
                stockPriceQuoteCacheEntry.setStockTableEntryValidated( true );
                throw new StockPriceNotFoundException( stockPriceResult.getTickerSymbol() );

            case NOT_FOUND:
                throw new StockPriceNotFoundException( stockPriceResult.getTickerSymbol() );

            case SUCCESS:
                stockPriceQuote.setCacheState( AsyncCacheEntryState.CURRENT );
                stockPriceQuote.setLastPrice( stockPriceResult.getStockPrice() );
                stockPriceQuote.setCacheError( null );
                break;

            case EXCEPTION:
                stockPriceQuoteCacheEntry.setStockTableEntryValidated( true );
                final StockPriceNotFoundException exception = new StockPriceNotFoundException( stockPriceResult.getTickerSymbol(),
                                                                                               stockPriceResult.getException() );
                throw exception;

        }
        return stockPriceQuote;
    }

    /**
     * This method, when called, starts on a new thread launched and managed by the Spring container.
     * In the new thread, the stock quote will be retrieved and the caller will be notified through the {@code observable}
     * @param tickerSymbol
     * @param asyncKey The cache key and async key are both ticker symbols.
     * @param subject Behaviour subject to use to notify the caller that the request has been completed.
     */
    @Async( AppConfig.STOCK_QUOTE_THREAD_POOL )
    @Override
    public void asynchronousFetch( final String tickerSymbol,
                                   final String asyncKey,
                                   final AsyncProcessor<StockPriceQuote> subject )
    {
        final String methodName = "asynchronousFetch";
        logMethodBegin( methodName, tickerSymbol );
        super.asynchronousFetch( tickerSymbol, asyncKey, subject );
        logMethodEnd( methodName );
    }

    @Override
    protected StockQuoteEntityCacheRequestKey createRequestKey( final String cacheKey, final String asyncKey )
    {
        final StockQuoteEntityCacheRequestKey requestKey = this.context.getBean( StockQuoteEntityCacheRequestKey.class );
        requestKey.setASyncKey( asyncKey );
        requestKey.setCacheKey( cacheKey );
        return null;
    }

    @Override
    protected StockPriceQuoteCacheResponse newResponse()
    {
        return this.context.getBean( StockPriceQuoteCacheResponse.class );
    }
}
