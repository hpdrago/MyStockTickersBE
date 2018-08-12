package com.stocktracker.servicelayer.service.cache.linkedaccount;

import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.servicelayer.service.cache.common.AsyncBatchCacheRequestKey;

import java.util.UUID;

/**
 * The cache key and the third party key are the same for the stock company.
 */
public class LinkedAccountEntityCacheRequestKey extends AsyncBatchCacheRequestKey<UUID,LinkedAccountEntity>
{
    public LinkedAccountEntityCacheRequestKey( final UUID linkedAccountUUID, final LinkedAccountEntity linkedAccountEntity )
    {
        super( linkedAccountUUID, linkedAccountEntity);
    }
}
