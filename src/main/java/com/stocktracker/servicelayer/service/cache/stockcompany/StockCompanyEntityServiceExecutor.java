package com.stocktracker.servicelayer.service.cache.stockcompany;

import com.stocktracker.AppConfig;
import com.stocktracker.repositorylayer.entity.StockCompanyEntity;
import com.stocktracker.servicelayer.service.IEXTradingStockService;
import com.stocktracker.servicelayer.service.StockCompanyEntityService;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDBEntityServiceExecutor;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDataNotFoundException;
import io.reactivex.processors.AsyncProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import pl.zankowski.iextrading4j.api.stocks.Company;

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
                                                                                         String,
                                                                                         Company,
                                                                                         StockCompanyEntityService,
                                                                                         StockCompanyEntityCacheRequestKey,
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
     * @param tickerSymbol The ticker symbol is both the cache key and the async key.
     * @param asyncProcessor Behaviour subject to use to notify the caller that the request has been completed.
     */
    @Async( AppConfig.STOCK_COMPANY_THREAD_POOL )
    @Override
    public void asynchronousFetch( final String tickerSymbol,
                                   final AsyncProcessor<Company> asyncProcessor )
    {
        final String methodName = "asynchronousFetch";
        logMethodBegin( methodName, tickerSymbol );
        /*
         * The super class calls this.getASyncData and takes care of the subject notification.
         */
        super.asynchronousFetch( tickerSymbol, asyncProcessor );
        logMethodEnd( methodName );
    }

    /**
     * This method, when called, is run a new thread and makes a call to the super class to make perform the asynchronous
     * fetch logic which, in part, ends up calling the {@code getASyncData} method below to perform the batch
     * stock company fetch.
     * @param asyncBatchCacheRequests
     */
    @Async( AppConfig.STOCK_COMPANY_THREAD_POOL )
    @Override
    public void asynchronousFetch( List<StockCompanyEntityCacheRequest> asyncBatchCacheRequests )
    {
        final String methodName = "asynchronousFetch";
        logMethodBegin( methodName, asyncBatchCacheRequests.size() );
        super.asynchronousFetch( asyncBatchCacheRequests );
        logMethodEnd( methodName );
    }

    @Override
    protected StockCompanyEntityCacheRequestKey createRequestKey( final String cacheKey, final String asyncKey )
    {
        return new StockCompanyEntityCacheRequestKey( cacheKey, asyncKey );
    }

    /**
     * Retrieves the stock companys for the ticker symbols.
     * @param requestKeys
     */
     @Override
    protected Map<String,Company> batchFetch( final List<StockCompanyEntityCacheRequestKey> requestKeys )
    {
        final String methodName = "getASyncData";
        logMethodBegin( methodName, requestKeys );
        /*
         * Extract the tickers symbols.
         */
        final List<String> tickerSymbols = requestKeys.stream()
                                                      .map( stockCompanyEntityCacheRequestKey ->
                                                                stockCompanyEntityCacheRequestKey.getASyncKey() )
                                                      .collect(Collectors.toList());
        /*
         * Make the call to IEXTrading to get the companies.
         */
        final List<Company> companies = this.iexTradingStockService
                                            .getCompanies( tickerSymbols );
        /*
         * Convert the list of companies to a map of companies keyed by ticker symbol
         */
        final Map<String,Company> companyMap = new HashMap<>(companies.size());
        companies.forEach( company -> companyMap.put( company.getSymbol(), company ));
        logMethodEnd( methodName, "Received " + companies.size() + " stock companies" );
        return companyMap;
    }

    /**
     * Get the stock company.
     * @param tickerSymbol
     * @return
     * @throws AsyncCacheDataNotFoundException
     */
    @Override
    public Company getASyncData( final String tickerSymbol )
    {
        final String methodName = "getASyncData";
        logMethodBegin( methodName, tickerSymbol );
        final Company company = this.iexTradingStockService
                                    .getCompany( tickerSymbol );
        logMethodEnd( methodName, company );
        return company;
    }

    @Override
    protected StockCompanyEntityCacheResponse newResponse()
    {
        return this.context.getBean( StockCompanyEntityCacheResponse.class );
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
}
