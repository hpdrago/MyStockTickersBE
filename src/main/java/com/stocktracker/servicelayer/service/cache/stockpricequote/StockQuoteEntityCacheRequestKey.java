package com.stocktracker.servicelayer.service.cache.stockpricequote;

import com.stocktracker.servicelayer.service.cache.common.AsyncBatchCacheRequestKey;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * The cache key and the async key are the same for the stock quote.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class StockQuoteEntityCacheRequestKey extends AsyncBatchCacheRequestKey<String,String>
{
    public StockQuoteEntityCacheRequestKey()
    {
    }

    public StockQuoteEntityCacheRequestKey( final String cacheKey, final String asyncKey )
    {
        super( cacheKey, asyncKey );
    }
}
