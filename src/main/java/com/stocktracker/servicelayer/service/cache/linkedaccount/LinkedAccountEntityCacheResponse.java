package com.stocktracker.servicelayer.service.cache.linkedaccount;

import com.stocktracker.servicelayer.service.cache.common.AsyncBatchCacheResponse;
import com.stocktracker.servicelayer.tradeit.apiresults.GetAccountOverviewAPIResult;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * This class defines the request type for batch response of stock companies.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class LinkedAccountEntityCacheResponse extends AsyncBatchCacheResponse<UUID,
    GetAccountOverviewAsyncCacheKey,
                                                                              GetAccountOverviewAPIResult>
{
}
