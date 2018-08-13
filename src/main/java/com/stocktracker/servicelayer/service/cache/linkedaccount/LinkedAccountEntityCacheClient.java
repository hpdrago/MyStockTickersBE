package com.stocktracker.servicelayer.service.cache.linkedaccount;

import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.servicelayer.service.LinkedAccountEntityService;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheClient;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDBEntityClient;
import com.stocktracker.servicelayer.tradeit.apiresults.GetAccountOverviewAPIResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * This interface is used by any class that need information from the {@code LinkedAccountEntityCache}.
 */
@Service
public class LinkedAccountEntityCacheClient extends AsyncCacheClient<UUID,
                                                                     LinkedAccountEntity,
                                                                     LinkedAccountEntityCacheAsyncKey,
                                                                     LinkedAccountEntityCacheEntry,
                                                                     LinkedAccountEntityCacheDataReceiver,
                                                                     LinkedAccountEntityServiceExecutor,
                                                                     LinkedAccountEntityCache>
{
    @Autowired
    private LinkedAccountEntityCache linkedAccountEntityCache;

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
}
