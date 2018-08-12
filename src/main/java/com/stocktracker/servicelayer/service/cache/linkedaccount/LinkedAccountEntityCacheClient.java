package com.stocktracker.servicelayer.service.cache.linkedaccount;

import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.servicelayer.service.LinkedAccountEntityService;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDBEntityClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * This interface is used by any class that need information from the {@code LinkedAccountEntityCache}.
 */
@Service
public class LinkedAccountEntityCacheClient extends AsyncCacheDBEntityClient<UUID,
                                                                             LinkedAccountEntity,
                                                                             LinkedAccountEntity,
                                                                             LinkedAccountEntityCacheEntry,
                                                                             LinkedAccountEntityCacheDataReceiver,
                                                                             LinkedAccountEntityCacheRequest,
                                                                             LinkedAccountEntityCacheResponse,
                                                                             LinkedAccountEntityCacheRequestKey,
                                                                             LinkedAccountEntityServiceExecutor,
                                                                             LinkedAccountEntityCache,
                                                                             LinkedAccountEntityService>
{
    @Autowired
    private LinkedAccountEntityCache linkedAccountEntityCache;
    @Autowired
    private LinkedAccountEntityService stockQuoteEntityService;

    @Override
    protected LinkedAccountEntityCache getCache()
    {
        return linkedAccountEntityCache;
    }

    @Override
    protected LinkedAccountEntity createCachedDataObject()
    {
        return this.context.getBean( LinkedAccountEntity.class );
    }

    @Override
    protected LinkedAccountEntityService getEntityService()
    {
        return this.stockQuoteEntityService;
    }

    @Override
    protected LinkedAccountEntityCacheRequestKey createRequestKey( final UUID cacheKey,
                                                                   final LinkedAccountEntity thirdPartyKey )
    {
        return new LinkedAccountEntityCacheRequestKey( cacheKey, thirdPartyKey );
    }
}
