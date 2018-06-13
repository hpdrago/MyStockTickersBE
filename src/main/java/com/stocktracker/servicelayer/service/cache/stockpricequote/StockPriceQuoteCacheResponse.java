package com.stocktracker.servicelayer.service.cache.stockpricequote;

import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import com.stocktracker.servicelayer.service.cache.common.AsyncBatchCacheResponse;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This class defines the request type for batch response of stock price quotes.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class StockPriceQuoteCacheResponse extends AsyncBatchCacheResponse<String,StockPriceQuote>
{
}
