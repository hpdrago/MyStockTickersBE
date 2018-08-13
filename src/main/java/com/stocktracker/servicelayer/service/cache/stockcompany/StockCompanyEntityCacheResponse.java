package com.stocktracker.servicelayer.service.cache.stockcompany;

import com.stocktracker.servicelayer.service.cache.common.AsyncBatchCacheResponse;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.zankowski.iextrading4j.api.stocks.Company;

/**
 * This class defines the request type for batch response of stock companies.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class StockCompanyEntityCacheResponse extends AsyncBatchCacheResponse<String,
                                                                             String,
                                                                             Company>
{
}
