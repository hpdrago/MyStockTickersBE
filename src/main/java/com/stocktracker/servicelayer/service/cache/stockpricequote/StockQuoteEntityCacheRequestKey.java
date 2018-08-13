package com.stocktracker.servicelayer.service.cache.stockpricequote;

import com.stocktracker.servicelayer.service.cache.common.AsyncBatchCacheRequestKey;

/**
 * The cache key and the async key are the same for the stock quote.
 */
public class StockQuoteEntityCacheRequestKey extends AsyncBatchCacheRequestKey<String,String>
{
    public StockQuoteEntityCacheRequestKey( final String cacheKey, final String asyncKey )
    {
        super( cacheKey, asyncKey );
    }
}
