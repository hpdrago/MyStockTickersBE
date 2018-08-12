package com.stocktracker.servicelayer.service.cache.stockcompany;

import com.stocktracker.repositorylayer.entity.StockCompanyEntity;
import com.stocktracker.servicelayer.service.StockCompanyEntityService;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDBEntityClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class the client interface to the {@code StockCompanyEntityCache}.  It defines the necessary methods and data
 * types to correctly extend the {@code AsyncCacheDBEntityClient} which contains all of the required processing logic.
 */
@Service
public class StockCompanyEntityCacheClient extends AsyncCacheDBEntityClient<String,
                                                                            String,
                                                                            StockCompanyEntity,
                                                                            StockCompanyEntityCacheEntry,
                                                                            StockCompanyEntityCacheDataReceiver,
                                                                            StockCompanyEntityCacheRequest,
                                                                            StockCompanyEntityCacheResponse,
                                                                            StockCompanyEntityCacheRequestKey,
                                                                            StockCompanyEntityServiceExecutor,
                                                                            StockCompanyEntityCache,
                                                                            StockCompanyEntityService>
{
    @Autowired
    private StockCompanyEntityCache stockCompanyEntityCache;

    @Autowired
    private StockCompanyEntityService stockCompanyEntityService;

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

    @Override
    protected StockCompanyEntityCacheRequestKey createRequestKey( final String cacheKey, final String thirdPartyKey )
    {
        return new StockCompanyEntityCacheRequestKey( cacheKey, thirdPartyKey );
    }
}
