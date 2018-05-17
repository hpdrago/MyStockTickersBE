package com.stocktracker.servicelayer.service.cache.stockcompany;

import com.stocktracker.AppConfig;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.repositorylayer.entity.StockCompanyEntity;
import com.stocktracker.servicelayer.service.StockCompanyEntityService;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDBEntityServiceExecutor;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDataNotFoundException;
import com.stocktracker.servicelayer.service.cache.stockpricequote.IEXTradingStockService;
import io.reactivex.processors.AsyncProcessor;
import io.reactivex.processors.BehaviorProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import pl.zankowski.iextrading4j.api.stocks.Company;

/**
 * This class makes the calls to the IEXTrading API to get the Stock Company: https://iextrading.com/developer/docs/#company
 */
@Service
// Proxy target class to get past implementation of the interface and getting a runtime proxy error.
@EnableAsync(proxyTargetClass = true)
public class StockCompanyEntityServiceExecutor extends AsyncCacheDBEntityServiceExecutor<String,
                                                                                         StockCompanyEntity,
                                                                                         StockCompanyEntityService,
                                                                                         Company>
{
    /**
     * Service for the stock company entities.
     */
    private StockCompanyEntityService stockCompanyEntityService;

    /**
     * Service to get the Company from IEXTrading
     */
    private IEXTradingStockService iexTradingStockService;

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
            throw new AsyncCacheDataNotFoundException( tickerSymbol, e2 );
        }
    }

    /**
     * This method, when call is called on a new thread launched and managed by the Spring container.
     * In the new thread, the stock company will be retrieved and the caller will be notified through the {@code observable}
     * @param tickerSymbol
     * @param subject Behaviour subject to use to notify the caller that the request has been completed.
     */
    @Async( AppConfig.STOCK_QUOTE_THREAD_POOL )
    @Override
    public void asynchronousFetch( final String tickerSymbol, final AsyncProcessor<StockCompanyEntity> subject )
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

    @Autowired
    public void setStockCompanyEntityService( final StockCompanyEntityService stockCompanyEntityService )
    {
        this.stockCompanyEntityService = stockCompanyEntityService;
    }

    @Autowired
    public void setIexTradingStockService( final IEXTradingStockService iexTradingStockService )
    {
        this.iexTradingStockService = iexTradingStockService;
    }
}
