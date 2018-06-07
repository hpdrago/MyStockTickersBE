package com.stocktracker.servicelayer.service.cache.stockcompany;

import com.stocktracker.AppConfig;
import com.stocktracker.common.exceptions.DuplicateEntityException;
import com.stocktracker.common.exceptions.StockCompanyNotFoundException;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.repositorylayer.entity.StockCompanyEntity;
import com.stocktracker.servicelayer.service.StockCompanyEntityService;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDBEntityServiceExecutor;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDataNotFoundException;
import com.stocktracker.servicelayer.service.cache.stockpricequote.IEXTradingStockService;
import io.reactivex.processors.AsyncProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import pl.zankowski.iextrading4j.api.stocks.Company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class makes the calls to the IEXTrading API to get the Stock Company: https://iextrading.com/developer/docs/#company
 */
@Service
// Proxy target class to get past implementation of the interface and getting a runtime proxy error.
@EnableAsync(proxyTargetClass = true)
public class StockCompanyEntityServiceExecutor extends AsyncCacheDBEntityServiceExecutor<String,
                                                                                         StockCompanyEntity,
                                                                                         StockCompanyEntityService,
                                                                                         Company,
                                                                                         StockCompanyNotFoundException,
    StockCompanyEntityCacheRequest,
    StockCompanyEntityCacheResponse>
{
    /**
     * Service for the stock company entities.
     */
    @Autowired
    private StockCompanyEntityService stockCompanyEntityService;

    /**
     * Service to get the Company from IEXTrading
     */
    @Autowired
    private IEXTradingStockService iexTradingStockService;


    /**
     * This method, when call is called on a new thread launched and managed by the Spring container.
     * In the new thread, the stock company will be retrieved and the caller will be notified through the {@code observable}
     * @param tickerSymbol
     * @param asyncProcessor Behaviour subject to use to notify the caller that the request has been completed.
     */
    @Async( AppConfig.STOCK_COMPANY_THREAD_POOL )
    @Override
    public void asynchronousFetch( final String tickerSymbol, final AsyncProcessor<StockCompanyEntity> asyncProcessor )
    {
        final String methodName = "asynchronousFetch";
        logMethodBegin( methodName, tickerSymbol );
        /*
         * The super class calls this.synchronousFetch and takes care of the subject notification.
         */
        super.asynchronousFetch( tickerSymbol, asyncProcessor );
        logMethodEnd( methodName );
    }

    /**
     * This method, when called, is run a new thread and makes a call to the super class to make perform the asynchronous
     * fetch logic which, in part, ends up calling the {@code synchronousFetch} method below to perform the batch
     * stock company fetch.
     * @param asyncBatchCacheRequests
     */
    @Async( AppConfig.STOCK_COMPANY_THREAD_POOL )
    @Override
    public void asynchronousFetch( final Map<String, StockCompanyEntityCacheRequest> asyncBatchCacheRequests )
    {
        final String methodName = "asynchronousFetch";
        logMethodBegin( methodName, asyncBatchCacheRequests.size() );
        Map<String, StockCompanyEntityCacheRequest> requestMap = new HashMap<>();
        for ( final StockCompanyEntityCacheRequest entry: asyncBatchCacheRequests.values() )
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
     * This method is called to get a batch of IEXTrading Company instances on a single call.
     * @param requests Contains the information to make the batch request.
     * @return
     */
    @Override
    public List<StockCompanyEntityCacheResponse> synchronousFetch( final Map<String, StockCompanyEntityCacheRequest> requests )
    {
        final String methodName = "synchronousFetch";
        logMethodBegin( methodName, requests.size() );
        /*
         * Get all of the ticker symbols.
         */
        final List<String> tickerSymbols = requests.values()
                                                   .stream()
                                                   .map( stockCompanyEntityAsyncBatchRequest -> stockCompanyEntityAsyncBatchRequest.getCacheKey() )
                                                   .collect( Collectors.toList() );
        /*
         * Get the companies for the ticker symbols.
         */
        final List<Company> companies = this.iexTradingStockService
                                            .getCompanies( tickerSymbols );
        List<StockCompanyEntityCacheResponse> responses = new ArrayList<>();
        /*
         * Update existing companies and insert new companies.
         */
        companies.stream()
                 .forEach( company ->
                  {
                      StockCompanyEntity stockCompanyEntity = this.context.getBean( StockCompanyEntity.class );
                      this.copyExternalDataToEntity( company, stockCompanyEntity );
                      final StockCompanyEntityCacheResponse stockCompanyEntityCacheResponse = this.context
                                                                                      .getBean( StockCompanyEntityCacheResponse.class );
                      stockCompanyEntityCacheResponse.setCacheKey( company.getSymbol() );
                      try
                      {
                          stockCompanyEntity = this.stockCompanyEntityService
                                                   .saveEntity( stockCompanyEntity );
                      }
                      catch( DuplicateEntityException e )
                      {
                          // ignore
                      }
                      stockCompanyEntityCacheResponse.setData( stockCompanyEntity );
                      responses.add( stockCompanyEntityCacheResponse );
                  } );
        return responses;
    }

    /**
     * Fetch the stock company information from IEXTrading.
     * @param tickerSymbol
     * @return
     * @throws AsyncCacheDataNotFoundException
     */
    @Override
    protected Company getExternalData( final String tickerSymbol )
        throws AsyncCacheDataNotFoundException
    {
        try
        {
            final Company company = this.iexTradingStockService
                                        .getCompany( tickerSymbol );
            return company;
        }
        catch( StockNotFoundException e2 )
        {
            throw new StockCompanyNotFoundException( tickerSymbol, e2 );
        }
    }

    protected void copyExternalDataToEntity( final Company company, final StockCompanyEntity companyEntity )
    {
        super.copyExternalDataToEntity( company, companyEntity );
        companyEntity.setTickerSymbol( company.getSymbol() );
    }

    /**
     * Creates a new entity instance.
     * @return
     */
    @Override
    protected StockCompanyEntity createEntity()
    {
        return this.context.getBean( StockCompanyEntity.class );
    }

    @Override
    protected StockCompanyEntityService getEntityService()
    {
        return this.stockCompanyEntityService;
    }

    @Override
    protected StockCompanyNotFoundException createException( final String key, final Exception cause )
    {
        return new StockCompanyNotFoundException( key, cause );
    }
}
