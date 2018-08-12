package com.stocktracker.servicelayer.service.cache.stockpricequote;

import com.stocktracker.servicelayer.service.cache.common.AsyncBatchCacheRequest;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This class defines the request type for batch requests of stock price quotes.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class StockPriceQuoteCacheRequest extends AsyncBatchCacheRequest<String,String,StockPriceQuote>
{
}
