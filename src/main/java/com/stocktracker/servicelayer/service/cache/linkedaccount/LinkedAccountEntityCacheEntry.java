package com.stocktracker.servicelayer.service.cache.linkedaccount;

import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntry;
import com.stocktracker.servicelayer.tradeit.apiresults.GetAccountOverviewAPIResult;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * This class defines the Stock Quote Entity cache's entry types.
 */
public class LinkedAccountEntityCacheEntry extends AsyncCacheEntry<UUID,
                                                                   LinkedAccountEntity,
                                                                   GetAccountOverviewAsyncCacheKey,
                                                                   GetAccountOverviewAPIResult>
{
    /**
     * Stock quotes are valid for 6 hours.
     * @return
     */
    @Override
    protected long getCurrentDurationTime()
    {
        return TimeUnit.HOURS.convert( 6, TimeUnit.MILLISECONDS );
    }
}
