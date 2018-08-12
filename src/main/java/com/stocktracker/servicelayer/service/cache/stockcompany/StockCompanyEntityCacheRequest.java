package com.stocktracker.servicelayer.service.cache.stockcompany;

import com.stocktracker.repositorylayer.entity.StockCompanyEntity;
import com.stocktracker.servicelayer.service.cache.common.AsyncBatchCacheRequest;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This class defines the request type for batch requests of stock companies.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class StockCompanyEntityCacheRequest extends AsyncBatchCacheRequest<String,String,StockCompanyEntity>
{
}
