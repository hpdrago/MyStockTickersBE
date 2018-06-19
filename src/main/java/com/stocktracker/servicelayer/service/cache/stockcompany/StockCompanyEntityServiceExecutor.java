package com.stocktracker.servicelayer.service.cache.stockcompany;

import com.stocktracker.AppConfig;
import com.stocktracker.common.exceptions.DuplicateEntityException;
import com.stocktracker.common.exceptions.StockCompanyNotFoundException;
import com.stocktracker.repositorylayer.entity.StockCompanyEntity;
import com.stocktracker.servicelayer.service.IEXTradingStockService;
import com.stocktracker.servicelayer.service.StockCompanyEntityService;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDBEntityServiceExecutor;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDataNotFoundException;
import io.reactivex.processors.AsyncProcessor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import pl.zankowski.iextrading4j.api.stocks.Company;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class makes the calls to the IEXTrading API to get the Stock Company: https://iextrading.com/developer/docs/#company
 */
@Service
// Proxy target class to get past implementation of the interface and getting a runtime proxy error.
@EnableAsync(proxyTargetClass = true)
public class StockCompanyEntityServiceExecutor extends AsyncCacheDBEntityServiceExecutor<String,
                                                                                         StockCompanyEntity,
                                                                                         Company,
                                                                                         StockCompanyEntityService,
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
         * The super class calls this.getExternalData and takes care of the subject notification.
         */
        super.asynchronousFetch( tickerSymbol, asyncProcessor );
        logMethodEnd( methodName );
    }

    /**
     * This method, when called, is run a new thread and makes a call to the super class to make perform the asynchronous
     * fetch logic which, in part, ends up calling the {@code getExternalData} method below to perform the batch
     * stock company fetch.
     * @param asyncBatchCacheRequests
     */
    @Async( AppConfig.STOCK_COMPANY_THREAD_POOL )
    @Override
    public void asynchronousFetch( final Map<String, StockCompanyEntityCacheRequest> asyncBatchCacheRequests )
    {
        final String methodName = "asynchronousFetch";
        logMethodBegin( methodName, asyncBatchCacheRequests.size() );
        super.asynchronousFetch( asyncBatchCacheRequests );
        logMethodEnd( methodName );
    }

//    /**
//     * Fetches the stock companies from IEXTrading. One for each ticker symbol.
//     * @param tickerSymbols
//     * @return
//     */
//    @Override
//    protected List<StockCompanyEntity> getExternalData( final List<String> tickerSymbols )
//    {
//        final String methodName = "getExternalData";
//        logMethodBegin( methodName, tickerSymbols );
//        List<StockCompanyEntity> stockCompanyEntities = new ArrayList<>();
//        final List<Company> companies = this.iexTradingStockService
//                                            .getCompanies( tickerSymbols );
//        companies.forEach( company ->
//                           {
//                               logDebug( methodName, "for company: {0}", company );
//                               StockCompanyEntity stockCompanyEntity = this.context.getBean( StockCompanyEntity.class );
//                               this.copyExternalDataToEntity( company, stockCompanyEntity );
//                               try
//                               {
//                                   stockCompanyEntity = this.stockCompanyEntityService
//                                                            .saveEntity( stockCompanyEntity );
//                               }
//                               catch( DuplicateEntityException e )
//                               {
//                                   // ignore
//                               }
//                               stockCompanyEntities.add( stockCompanyEntity );
//                           } );
//        logMethodEnd( methodName, stockCompanyEntities.size() + " companies" );
//        return stockCompanyEntities;
//    }

    @Override
    protected String getCacheKeyFromThirdPartyData( final Company company )
    {
        return company.getSymbol();
    }

    /**
     * Retrieves the stock companys for the ticker symbols.
     * @param tickerSymbols
     * @return
     */
    @Override
    protected List<Company> getThirdPartyData( final List<String> tickerSymbols )
    {
        final String methodName = "getThirdPartyData";
        logMethodBegin( methodName, tickerSymbols );
        final List<Company> companies = this.iexTradingStockService
                                            .getCompanies( tickerSymbols );
        logMethodEnd( methodName, "Received " + companies.size() + " stock companies" );
        return companies;
    }

    /**
     * Get the stock company.
     * @param tickerSymbol
     * @return
     * @throws AsyncCacheDataNotFoundException
     */
    @Override
    protected Company getThirdPartyData( final String tickerSymbol )
        throws AsyncCacheDataNotFoundException
    {
        final String methodName = "getThirdPartyData";
        logMethodBegin( methodName, tickerSymbol );
        final Company company = this.iexTradingStockService
                                    .getCompany( tickerSymbol );
        logMethodEnd( methodName, company );
        return company;
    }

    /**
     * Copy company information from the IEXTrading data to the company entity.
     * @param company
     * @param companyEntity
     */
    protected void copyExternalDataToEntity( final Company company, final StockCompanyEntity companyEntity )
    {
        companyEntity.setTickerSymbol( company.getSymbol() );
        BeanUtils.copyProperties( company, companyEntity );
    }

    @Override
    protected String getCacheKey( final StockCompanyEntity stockCompanyEntity )
    {
        return stockCompanyEntity.getTickerSymbol();
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

    @Override
    protected StockCompanyNotFoundException createException( final String key, final Exception cause )
    {
        return new StockCompanyNotFoundException( key, cause );
    }

}
