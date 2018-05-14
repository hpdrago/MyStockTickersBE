package com.stocktracker.servicelayer.service.cache.stockcompany;

import com.stocktracker.repositorylayer.entity.StockCompanyEntity;
import com.stocktracker.servicelayer.service.StockCompanyEntityService;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDBEntityClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * This interface is used by any class that need information from the {@code StockCompanyEntityCache}.
 */
@Service
public class StockCompanyEntityCacheClient extends AsyncCacheDBEntityClient<String,
                                                                            StockCompanyEntity,
                                                                            StockCompanyEntityCacheEntry,
                                                                            StockCompanyEntityServiceExecutor,
                                                                            StockCompanyEntityCache,
                                                                            StockCompanyEntityCacheDataReceiver,
                                                                            StockCompanyEntityService>
{
    private StockCompanyEntityCache stockCompanyEntityCache;
    private StockCompanyEntityService stockCompanyEntityService;

    /**
     * Stock company information is very static so update it if it hasn't been updated before or if the data is older
     * than a year.
     * @param entity
     * @return
     */
    @Override
    protected boolean isCurrent( final StockCompanyEntity entity )
    {
        return entity.getUpdateDate() != null &&
               entity.getUpdateDate().getTime() > System.currentTimeMillis() - TimeUnit.DAYS.convert( 365, TimeUnit.MILLISECONDS );
    }

    /**
     * Get the error message
     * @return
     */
    @Override
    protected StockCompanyEntityCache getCache()
    {
        return this.stockCompanyEntityCache;
    }

    @Override
    protected StockCompanyEntity createCachedDataObject()
    {
        return this.context.getBean( StockCompanyEntity.class );
    }

    @Override
    protected StockCompanyEntityService getEntityService()
    {
        return this.stockCompanyEntityService;
    }

    @Autowired
    public void setStockCompanyEntityCache( final StockCompanyEntityCache stockCompanyEntityCache )
    {
        this.stockCompanyEntityCache = stockCompanyEntityCache;
    }

    @Autowired
    public void setStockCompanyEntityService( final StockCompanyEntityService stockCompanyEntityService )
    {
        this.stockCompanyEntityService = stockCompanyEntityService;
    }

}
