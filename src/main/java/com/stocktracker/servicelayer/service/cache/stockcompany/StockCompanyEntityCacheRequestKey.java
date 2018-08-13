package com.stocktracker.servicelayer.service.cache.stockcompany;

import com.stocktracker.servicelayer.service.cache.common.AsyncBatchCacheRequestKey;

/**
 * The cache key and the async key are the same for the stock company.
 */
public class StockCompanyEntityCacheRequestKey extends AsyncBatchCacheRequestKey<String,String>
{
    public StockCompanyEntityCacheRequestKey( final String cacheKey, final String asyncKey )
    {
        super( cacheKey, asyncKey);
    }
}
