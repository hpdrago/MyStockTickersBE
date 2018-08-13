package com.stocktracker.servicelayer.service.cache.linkedaccount;

import com.stocktracker.servicelayer.service.cache.common.AsyncBatchCacheRequestKey;

import java.util.UUID;

/**
 * The cache key and the async key are the same for the stock company.
 */
public class LinkedAccountEntityCacheRequestKey extends AsyncBatchCacheRequestKey<UUID, LinkedAccountEntityCacheAsyncKey>
{
    public LinkedAccountEntityCacheRequestKey( final UUID linkedAccountUUID, final LinkedAccountEntityCacheAsyncKey asyncKey )
    {
        super( linkedAccountUUID, asyncKey);
    }
}
