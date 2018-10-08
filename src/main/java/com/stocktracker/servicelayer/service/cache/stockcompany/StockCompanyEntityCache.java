package com.stocktracker.servicelayer.service.cache.stockcompany;

import com.stocktracker.repositorylayer.entity.StockCompanyEntity;
import com.stocktracker.servicelayer.service.cache.common.AsyncBatchCache;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.zankowski.iextrading4j.api.stocks.Company;

import static com.stocktracker.servicelayer.service.cache.common.AsyncCacheStrategy.KEEP;

/**
 * This is the cache for IEXTrading Stock Company. See https://iextrading.com/developer/docs/#company for information
 * contained in the company.
 */
@Service
public class StockCompanyEntityCache extends AsyncBatchCache<String,
                                                             StockCompanyEntity,
                                                             String,
                                                             Company,
                                                             StockCompanyEntityCacheEntry,
                                                             StockCompanyEntityCacheRequestKey,
                                                             StockCompanyEntityCacheRequest,
                                                             StockCompanyEntityCacheResponse,
                                                             StockCompanyEntityServiceExecutor>
{
    @Autowired
    private StockCompanyEntityServiceExecutor stockCompanyEntityServiceExecutor;

    @Override
    protected StockCompanyEntity convertAsyncData( final String tickerSymbol, final String notUsed, final Company asyncData )
    {
        final StockCompanyEntity stockCompanyEntity = this.context.getBean( StockCompanyEntity.class );
        stockCompanyEntity.copyCompany( asyncData );
        return stockCompanyEntity;
    }

    /**
     * Creates the cache entry.
     * @return
     */
    @Override
    protected StockCompanyEntityCacheEntry createCacheEntry()
    {
        return new StockCompanyEntityCacheEntry();
    }

    /**
     * Creates the executor.
     * @return
     */
    @Override
    protected StockCompanyEntityServiceExecutor getExecutor()
    {
        return this.stockCompanyEntityServiceExecutor;
    }

    @Override
    protected AsyncCacheStrategy getCacheStrategy()
    {
        return KEEP;
    }

    @Override
    protected StockCompanyEntityCacheRequest createBatchRequestType()
    {
        return this.context.getBean( StockCompanyEntityCacheRequest.class );
    }
}