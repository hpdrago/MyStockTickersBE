package com.stocktracker.servicelayer.service.cache.stockcompany;

import com.stocktracker.repositorylayer.entity.StockCompanyEntity;
import com.stocktracker.servicelayer.service.StockCompanyEntityService;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDBEntityClient;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.zankowski.iextrading4j.api.stocks.Company;

/**
 * This class the client interface to the {@code StockCompanyEntityCache}.  It defines the necessary methods and data
 * types to correctly extend the {@code AsyncCacheDBEntityClient} which contains all of the required processing logic.
 */
@Service
public class StockCompanyEntityCacheClient extends AsyncCacheDBEntityClient<String,
                                                                            StockCompanyEntity,
                                                                            String,
                                                                            Company,
                                                                            StockCompanyEntityCacheEntry,
                                                                            StockCompanyEntityCacheDataReceiver,
                                                                            StockCompanyEntityContainer,
                                                                            StockCompanyEntityCacheRequestKey,
                                                                            StockCompanyEntityCacheRequest,
                                                                            StockCompanyEntityCacheResponse,
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
    public StockCompanyEntityCacheDataReceiver createDataReceiver()
    {
        return this.context.getBean( StockCompanyEntityCacheDataReceiver.class );
    }

    @Override
    protected StockCompanyEntityCacheRequestKey createRequestKey( final String cacheKey, final String asyncKey )
    {
        return new StockCompanyEntityCacheRequestKey( cacheKey, asyncKey );
    }

    @Override
    protected String getASyncKey( final StockCompanyEntityContainer container )
    {
        return container.getASyncKey();
    }

    @Override
    protected String getCacheKey( final StockCompanyEntityContainer container )
    {
        return container.getCacheKey();
    }

    @Override
    protected void setCacheState( final StockCompanyEntityContainer container, final AsyncCacheEntryState cacheState )
    {
        container.setCacheState( cacheState );
    }

    @Override
    protected void setCacheError( final StockCompanyEntityContainer container, final String cacheError )
    {
        container.setCacheError( cacheError );
    }

    @Override
    protected void setCachedData( final StockCompanyEntityContainer container, final StockCompanyEntity cachedData )
    {
        if ( cachedData != null )
        {
            container.setCachedData( cachedData );
        }
    }

    @Override
    protected void setCacheKey( final StockCompanyEntityContainer container, final String cacheKey )
    {
        container.setCacheKey( cacheKey );
    }
}
