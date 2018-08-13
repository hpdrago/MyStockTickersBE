package com.stocktracker.servicelayer.service.cache.linkedaccount;

import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.servicelayer.service.cache.common.AsyncBatchCacheRequest;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * This class defines the request type for batch requests of stock companies.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class LinkedAccountEntityCacheRequest extends AsyncBatchCacheRequest<UUID,
                                                                            LinkedAccountEntity,
                                                                            LinkedAccountEntityCacheAsyncKey>
{
}
