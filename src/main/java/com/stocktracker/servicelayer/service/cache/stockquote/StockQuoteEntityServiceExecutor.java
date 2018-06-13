package com.stocktracker.servicelayer.service.cache.stockquote;

import com.stocktracker.AppConfig;
import com.stocktracker.common.exceptions.DuplicateEntityException;
import com.stocktracker.common.exceptions.StockQuoteNotFoundException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import com.stocktracker.servicelayer.service.StockQuoteEntityService;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDBEntityServiceExecutor;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDataNotFoundException;
import com.stocktracker.servicelayer.service.cache.stockpricequote.IEXTradingStockService;
import io.reactivex.processors.AsyncProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import pl.zankowski.iextrading4j.api.stocks.Quote;

import java.util.ArrayList;
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
                                                                                       StockQuoteEntityService,
                                                                                       Quote,
                                                                                       StockQuoteNotFoundException,
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
     * Get the IEXTrading quote.
     * @param tickerSymbol
     * @return
     * @throws AsyncCacheDataNotFoundException
     */
    @Override
    protected Quote getExternalData( final String tickerSymbol )
        throws AsyncCacheDataNotFoundException
    {
        final String methodName = "getExternalData";
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
     * Creates a new entity.
     * @return
     */
    @Override
    protected StockQuoteEntity createEntity()
    {
        return this.context.getBean( StockQuoteEntity.class );
    }

    /**
     * Copies the properties from {@code quote} to {@code stockQuoteEntity}
     * @param quote
     * @param stockQuoteEntity
     */
    @Override
    protected void copyExternalDataToEntity( final Quote quote, final StockQuoteEntity stockQuoteEntity )
    {
        stockQuoteEntity.copyQuote( quote );
    }

    /**
     * This method, when call is called on a new thread launched and managed by the Spring container.
     * In the new thread, the stock quote will be retrieved and the caller will be notified through the {@code observable}
     * @param tickerSymbol
     * @param subject Behaviour subject to use to notify the caller that the request has been completed.
     */
    @Async( AppConfig.STOCK_QUOTE_THREAD_POOL )
    @Override
    public void asynchronousFetch( final String tickerSymbol, final AsyncProcessor<StockQuoteEntity> subject )
    {
        final String methodName = "asynchronousFetch";
        logMethodBegin( methodName, tickerSymbol );
        /*
         * The super class calls this.synchronousFetch and takes care of the subject notification.
         */
        super.asynchronousFetch( tickerSymbol, subject );
        logMethodEnd( methodName );
    }

    /**
     * This method, when called, is run a new thread and makes a call to the super class to make perform the asynchronous
     * fetch logic which, in part, ends up calling the {@code synchronousFetch} method below to perform the batch
     * stock company fetch.
     * @param asyncBatchCacheRequests
     */
    @Async( AppConfig.STOCK_QUOTE_THREAD_POOL )
    @Override
    public void asynchronousFetch( final Map<String, StockQuoteEntityCacheRequest> asyncBatchCacheRequests )
    {
        final String methodName = "asynchronousFetch";
        logMethodBegin( methodName, asyncBatchCacheRequests.keySet() );
        Map<String, StockQuoteEntityCacheRequest> requestMap = new HashMap<>();
        for ( final StockQuoteEntityCacheRequest entry: asyncBatchCacheRequests.values() )
        {
            requestMap.put( entry.getCacheKey(), entry );
            /*
             * IEXTrading is limited to 100 symbols at a time.
             */
            if ( requestMap.size() == 100 )
            {
                super.asynchronousFetch( requestMap );
                requestMap = new HashMap<>();
            }
        }
        /*
         * If there were more than 100, then this is remaining elements to process, otherwise it contains all of the
         * elements to process.
         */
        if ( requestMap.size() > 0 )
        {
            super.asynchronousFetch( requestMap );
        }
        logMethodEnd( methodName );
    }

    /**
     * Fetch the stock quote batch from IEXTrading.
     * @param requests Contains the information to make the batch request.
     * @return
     */
    @Override
    public List<StockQuoteEntityCacheResponse> synchronousFetch( final Map<String, StockQuoteEntityCacheRequest> requests )
    {
        final String methodName = "synchronousFetch";
        logMethodBegin( methodName, requests.keySet() );
        /*
         * Get all of the ticker symbols.
         */
        final List<String> tickerSymbols = requests.values()
                                                   .stream()
                                                   .map( stockQuoteEntityAsyncBatchRequest -> stockQuoteEntityAsyncBatchRequest.getCacheKey() )
                                                   .collect( Collectors.toList() );
        /*
         * Get the companies for the ticker symbols.
         */
        final List<Quote> quotes = this.iexTradingStockService
                                       .getQuotes( tickerSymbols );
        List<StockQuoteEntityCacheResponse> responses = new ArrayList<>();
        /*
         * Update existing companies and insert new companies.
         */
        quotes.stream()
              .forEach( company ->
                      {
                          StockQuoteEntity stockQuoteEntity = this.context.getBean( StockQuoteEntity.class );
                          this.copyExternalDataToEntity( company, stockQuoteEntity );
                          final StockQuoteEntityCacheResponse stockQuoteEntityCacheResponse = this.context
                              .getBean( StockQuoteEntityCacheResponse.class );
                          stockQuoteEntityCacheResponse.setCacheKey( company.getSymbol() );
                          try
                          {
                              stockQuoteEntity = this.stockQuoteEntityService
                                                     .saveEntity( stockQuoteEntity );
                          }
                          catch( DuplicateEntityException e )
                          {
                              logDebug( methodName, "DuplicateEntityException encountered saving {0}",
                                        stockQuoteEntity );
                              /*
                               * Retrieve and re0save information.
                               */
                              try
                              {
                                  stockQuoteEntity = this.stockQuoteEntityService
                                                         .getEntity( company.getSymbol() );
                                  this.copyExternalDataToEntity( company, stockQuoteEntity );
                                  stockQuoteEntity = this.stockQuoteEntityService
                                                         .saveEntity( stockQuoteEntity );
                              }
                              catch( VersionedEntityNotFoundException e1 )
                              {
                                   // ignore
                              }
                              catch( DuplicateEntityException e1 )
                              {
                                  // ignore
                              }
                              catch( Exception ex )
                              {
                                  stockQuoteEntityCacheResponse.setException( ex );
                                  logError( methodName, ex );
                              }
                          }
                          stockQuoteEntityCacheResponse.setData( stockQuoteEntity );
                          responses.add( stockQuoteEntityCacheResponse );
                      });
        return responses;
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
}
