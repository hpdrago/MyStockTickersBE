package com.stocktracker.servicelayer.service.cache.linkedaccount;

import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.servicelayer.service.cache.common.BaseAsyncCacheDataContainer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

/*
* *
 * This interface defines the methods for classes that will get Stock Quote information from the LinkedAccountEntityCache
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class LinkedAccountEntityContainer extends BaseAsyncCacheDataContainer<UUID,
                                                                              LinkedAccountEntity,
                                                                              GetAccountOverviewAsyncCacheKey>
{
    private UUID cacheKey;
    private LinkedAccountEntity linkedAccountEntity;
    private GetAccountOverviewAsyncCacheKey getAccountOverviewAsyncCacheKey;

    @Override
    public GetAccountOverviewAsyncCacheKey getASyncKey()
    {
        return this.getAccountOverviewAsyncCacheKey;
    }

    @Override
    public UUID getCacheKey()
    {
        return this.cacheKey;
    }

    @Override
    public void setCacheKey( final UUID cacheKey )
    {
        this.cacheKey = cacheKey;
    }

    @Override
    public void setCachedData( final LinkedAccountEntity cachedData )
    {
        this.linkedAccountEntity = linkedAccountEntity;
    }

    @Override
    public LinkedAccountEntity getCachedData()
    {
        return this.linkedAccountEntity;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "LinkedAccountEntityContainer{" );
        sb.append( "uuid='" ) .append( cacheKey ) .append( '\'' );
        sb.append( ", linkedAccountEntity=" ) .append( linkedAccountEntity );
        sb.append( ", getAccountOverviewAsyncCacheKey=" ) .append( getAccountOverviewAsyncCacheKey );
        sb.append( ", super=" ) .append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
