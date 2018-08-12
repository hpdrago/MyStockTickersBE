package com.stocktracker.servicelayer.service.cache.stockquote;

import com.stocktracker.servicelayer.service.cache.common.AsyncBatchCacheRequestKey;

/**
 * The cache key and the third party key are the same for the stock quote.
 */
public class StockQuoteRequestKey extends AsyncBatchCacheRequestKey<String,String>
{
    public StockQuoteRequestKey( final String cacheKey, final String thirdPartyKey )
    {
        super( cacheKey, thirdPartyKey );
    }
}
