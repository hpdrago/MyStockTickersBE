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

import java.util.List;
import java.util.Map;

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
     * @param tickerSymbols
     * @return
     */
    @Override
    protected List<Quote> getASyncData( final List<String> tickerSymbols )
    {
        final String methodName = "getASyncData";
        logMethodBegin( methodName, tickerSymbols );
        final List<Quote> quotes = this.iexTradingStockService
                                       .getQuotes( tickerSymbols );
        logMethodEnd( methodName, "Received " + quotes.size() + " quotes" );
        return quotes;
    }

    /**
     * Get the Quote from IEXTrading.
     * @param tickerSymbol
     * @return
     * @throws AsyncCacheDataNotFoundException
     */
    @Override
    protected Quote getASyncData( final String tickerSymbol )
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
     * @param asyncKey The cache key and the async key are the same -- ticker symbol.
     * @param subject Behaviour subject to use to notify the caller that the request has been completed.
     */
    @Async( AppConfig.STOCK_QUOTE_THREAD_POOL )
    @Override
    public void asynchronousFetch( final String cacheKey,
                                   final String asyncKey,
                                   final AsyncProcessor<StockQuoteEntity> subject )
    {
        final String methodName = "asynchronousFetch";
        final String tickerSymbol = cacheKey;
        logMethodBegin( methodName, tickerSymbol );
        /*
         * The super class calls this.getASyncData and takes care of the subject notification.
         */
        super.asynchronousFetch( tickerSymbol, tickerSymbol, subject );
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
    public void asynchronousFetch( final Map<String, StockQuoteEntityCacheRequest> asyncBatchCacheRequests )
    {
        final String methodName = "asynchronousFetch";
        logMethodBegin( methodName, asyncBatchCacheRequests.keySet() );
        super.asynchronousFetch( asyncBatchCacheRequests );
        logMethodEnd( methodName );
    }

    @Override
    protected void copyASyncData( final Quote asyncData, final StockQuoteEntity entity )
    {
        super.copyASyncData( asyncData, entity );
        entity.copyQuote( asyncData );
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
    protected String getCacheKeyFromASyncData( final Quote quote )
    {
        return quote.getSymbol();
    }

    @Override
    protected String getCacheKey( final StockQuoteEntity stockQuoteEntity )
    {
        return stockQuoteEntity.getTickerSymbol();
    }

    @Override
    protected StockQuoteEntityCacheRequestKey createRequestKey( final String cacheKey, final String asyncKey )
    {
        return null;
    }

    @Override
    protected List<Quote> batchFetch( final List<StockQuoteEntityCacheRequestKey> requestKeys )
    {
        return null;
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

    @Override
    protected StockQuoteNotFoundException createException( final String key, final Exception cause )
    {
        return new StockQuoteNotFoundException( key, cause );
    }

    @Override
    protected StockQuoteEntityCacheResponse createResponse( final StockQuoteEntityCacheRequestKey requestKey,
                                                            final Quote asyncData )
    {
        final StockQuoteEntityCacheResponse response = this.context.getBean( StockQuoteEntityCacheResponse.class  );
        response.setASyncKey( requestKey.getASyncKey() );
    }
}
