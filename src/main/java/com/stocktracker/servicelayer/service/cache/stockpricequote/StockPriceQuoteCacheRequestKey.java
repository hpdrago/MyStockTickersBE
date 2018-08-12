package com.stocktracker.servicelayer.service.cache.stockpricequote;

import com.stocktracker.servicelayer.service.cache.common.AsyncBatchCacheRequestKey;

/**
 * The cache key and the third party key are the same for the stock price quote.
 */
public class StockPriceQuoteCacheRequestKey extends AsyncBatchCacheRequestKey<String,String>
{
    public StockPriceQuoteCacheRequestKey( final String cacheKey, final String thirdPartyKey )
    {
        super( cacheKey, thirdPartyKey );
    }
}
