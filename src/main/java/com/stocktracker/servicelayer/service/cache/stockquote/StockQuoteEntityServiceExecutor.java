package com.stocktracker.servicelayer.service.cache.stockquote;

import com.stocktracker.AppConfig;
import com.stocktracker.common.exceptions.StockQuoteNotFoundException;
import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import com.stocktracker.servicelayer.service.IEXTradingStockService;
import com.stocktracker.servicelayer.service.StockQuoteEntityService;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDBEntityServiceExecutor;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDataNotFoundException;
import com.stocktracker.servicelayer.service.cache.stockpricequote.StockQuoteEntityCacheRequestKey;
import io.reactivex.processors.AsyncProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import pl.zankowski.iextrading4j.api.stocks.Quote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class makes the calls to the IEXTrading API to get the Stock Quote: https://iextrading.com/developer/docs/#quote.
 * Quotes are stored to the stock_quote table.
 */
@Service
// Proxy target class to get past implementation of the interface and getting a runtime proxy error.
@EnableAsync(proxyTargetClass = true)
public class StockQuoteEntityServiceExecutor extends AsyncCacheDBEntityServiceExecutor<String,
                                                                                       StockQuoteEntity,
                                                                                       String,
                                                                                       Quote,
                                                                                       StockQuoteEntityService,
                                                                                       StockQuoteEntityCacheRequestKey,
                                                                                       StockQuoteEntityCacheRequest,
                                                                                       StockQuoteEntityCacheResponse>
{
    /**
     * Service for the stock quote entities.
     */
    @Autowired
    private StockQuoteEntityService stockQuoteEntityService;

    /**
     * IEXTrading service.
     */
    @Autowired
    private IEXTradingStockService iexTradingStockService;

    /**
     * Get the quotes for the ticker symbols.
     * @param requestKeys
     * @return
     */
    @Override
    protected Map<String, Quote> batchFetch( final List<StockQuoteEntityCacheRequestKey> requestKeys )
    {
        final String methodName = "batchFetch";
        logMethodBegin( methodName, requestKeys );
        final List<String> tickerSymbols = requestKeys.stream()
                                                      .map( requestKey -> requestKey.getASyncKey() )
                                                      .collect( Collectors.toList() );
        /*
         * Get the quotes
         */
        final List<Quote> quoteList = this.iexTradingStockService
                                          .getQuotes( tickerSymbols );
        /*
         * Convert to a map
         */
        final Map<String,Quote> quoteMap = quoteList.stream()
                                                    .collect(Collectors.toMap( Quote::getSymbol, quote-> quote ));
        logMethodEnd( methodName, "Received " + quoteList.size() + " quotes" );
        return quoteMap;
    }

    /**
     * Get the Quote from IEXTrading.
     * @param tickerSymbol
     * @return
     * @throws AsyncCacheDataNotFoundException
     */
    @Override
    public Quote getASyncData( final String tickerSymbol )
        throws AsyncCacheDataNotFoundException
    {
        final String methodName = "getASyncData";
        logMethodBegin( methodName, tickerSymbol );
        final Quote quote = this.iexTradingStockService
                                .getQuote( tickerSymbol );
        if ( quote == null )
        {
            throw new StockQuoteNotFoundException( tickerSymbol );
        }
        logMethodEnd( methodName, quote );
        return quote;
    }

    /**
     * This method, when call is called on a new thread launched and managed by the Spring container.
     * In the new thread, the stock quote will be retrieved and the caller will be notified through the {@code observable}
     * @param cacheKey
     * @param subject Behaviour subject to use to notify the caller that the request has been completed.
     */
    @Async( AppConfig.STOCK_QUOTE_THREAD_POOL )
    @Override
    public void asynchronousFetch( final String cacheKey,
                                   final AsyncProcessor<Quote> subject )
    {
        final String methodName = "asynchronousFetch";
        final String tickerSymbol = cacheKey;
        logMethodBegin( methodName, tickerSymbol );
        /*
         * The super class calls this.getASyncData and takes care of the subject notification.
         */
        super.asynchronousFetch( tickerSymbol, subject );
        logMethodEnd( methodName );
    }

    /**
     * This method, when called, is run a new thread and makes a call to the super class to make perform the asynchronous
     * fetch logic which, in part, ends up calling the {@code getASyncData} method below to perform the batch
     * stock company fetch.
     * @param asyncBatchCacheRequests
     */
    @Async( AppConfig.STOCK_QUOTE_THREAD_POOL )
    @Override
    public void asynchronousFetch( final List<StockQuoteEntityCacheRequest> asyncBatchCacheRequests )
    {
        final String methodName = "asynchronousFetch";
        logMethodBegin( methodName, asyncBatchCacheRequests );
        super.asynchronousFetch( asyncBatchCacheRequests );
        logMethodEnd( methodName );
    }

    /**
     * Creates a new entity.
     * @return
     */
    @Override
    protected StockQuoteEntity createEntity()
    {
        return this.context.getBean( StockQuoteEntity.class );
    }

    @Override
    protected StockQuoteEntityCacheRequestKey createRequestKey( final String cacheKey, final String asyncKey )
    {
        return new StockQuoteEntityCacheRequestKey( cacheKey, asyncKey );
    }

    @Override
    protected StockQuoteEntityCacheResponse newResponse()
    {
        return this.context.getBean( StockQuoteEntityCacheResponse.class );
    }

    @Override
    protected StockQuoteEntityService getEntityService()
    {
        return this.stockQuoteEntityService;
    }

}
