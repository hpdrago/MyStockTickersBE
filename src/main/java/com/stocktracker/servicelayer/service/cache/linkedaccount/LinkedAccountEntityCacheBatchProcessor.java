package com.stocktracker.servicelayer.service.cache.linkedaccount;

import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheBatchProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Stock quote batch processors.  Extends the AsyncCacheBatchProcessor which in turn uses the StockQuoteCacheClient
 * to process a batch of DTOs to obtain the stock quote information from the Stock Quote Cache.
 */
@Service
public class LinkedAccountEntityCacheBatchProcessor extends AsyncCacheBatchProcessor<UUID,
                                                                                     LinkedAccountEntity,
                                                                                     LinkedAccountEntity,
                                                                                     LinkedAccountEntityCacheEntry,
                                                                                     LinkedAccountEntityCacheDataReceiver,
                                                                                     LinkedAccountEntityCacheRequest,
                                                                                     LinkedAccountEntityCacheResponse,
                                                                                     LinkedAccountEntityCacheRequestKey,
                                                                                     LinkedAccountEntityContainer,
                                                                                     LinkedAccountEntityServiceExecutor,
                                                                                     LinkedAccountEntityCache,
                                                                                     LinkedAccountEntityCacheClient>
{
    @Autowired
    private LinkedAccountEntityCacheClient linkedAccountEntityCacheClient;

    @Override
    protected void setDataReceiver( final LinkedAccountEntityContainer cachedDataContainer,
                                    final LinkedAccountEntityCacheDataReceiver dataReceiver )
    {
        /*
         * The cached data will be null if the data is not found, needs to be fetched, or there was an error.
         */
        if ( cachedDataContainer.getCachedData() != null )
        {
            dataReceiver.setCachedData( cachedDataContainer.getCachedData() );
        }
        dataReceiver.setCacheState( cachedDataContainer.getCacheState() );
        dataReceiver.setCacheError( cachedDataContainer.getCacheError() );
    }

    @Override
    protected UUID getCacheKey( final LinkedAccountEntityContainer container )
    {
        return container.getCacheKey();
    }

    @Override
    public LinkedAccountEntityCacheDataReceiver newReceiver()
    {
        return this.context.getBean( LinkedAccountEntityCacheDataReceiver.class );
    }

    @Override
    protected LinkedAccountEntityContainer newContainer()
    {
        return this.context.getBean( LinkedAccountEntityContainer.class );
    }

    @Override
    public LinkedAccountEntityCacheClient getAsyncCacheClient()
    {
        return this.linkedAccountEntityCacheClient;
    }
}
