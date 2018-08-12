package com.stocktracker.servicelayer.service.cache.linkedaccount;

import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDataReceiver;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * This class implements the {@code AsyncCacheDataReceiver} interface in order to interact with the
 * {@code LinkedAccountAsyncCache}.  This class will contain the results of requesting a stock quote from the cache.
 * It will then be used to by the {@code LinkedAccountEntityService} to set the quote values on the target target class
 * that is requesting stock quote information.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class LinkedAccountEntityCacheDataReceiver implements AsyncCacheDataReceiver<UUID,
                                                                                    LinkedAccountEntity,
                                                                                    LinkedAccountEntity>
{
    private UUID cacheKey;
    private LinkedAccountEntity linkedAccountEntity;
    private AsyncCacheEntryState cacheState;
    private String error;
    private Timestamp dataExpiration;

    @Override
    public void setCacheKey( final UUID key )
    {
        this.cacheKey = key;
    }

    /**
     * Key used to retrieve the {@code LinkedAccountEntity} from the database.
     * @return
     */
    @Override
    public UUID getCacheKey()
    {
        return this.cacheKey;
    }

    @Override
    public LinkedAccountEntity getThirdPartyKey()
    {
        return this.linkedAccountEntity;
    }

    /**
     * Set the LinkedAccountEntity data.
     * @param linkedAccountEntity
     */
    @Override
    public void setCachedData( final LinkedAccountEntity linkedAccountEntity )
    {
        this.linkedAccountEntity = linkedAccountEntity;
    }

    @Override
    public LinkedAccountEntity getCachedData()
    {
        return this.linkedAccountEntity;
    }

    /**
     * Set the cache data state.
     * @param cacheState
     */
    @Override
    public void setCacheState( final AsyncCacheEntryState cacheState )
    {
        this.cacheState = cacheState;
    }

    @Override
    public AsyncCacheEntryState getCacheState()
    {
        return this.cacheState;
    }

    /**
     * Set the error value from the asynchronous fetch process.
     * @param error
     */
    @Override
    public void setCacheError( final String error )
    {
        this.error = error;
    }

    @Override
    public String getCacheError()
    {
        return this.error;
    }

    @Override
    public void setExpirationTime( final Timestamp dataExpiration )
    {
        this.dataExpiration = dataExpiration;
    }

    @Override
    public Timestamp getExpirationTime()
    {
        return this.dataExpiration;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "LinkedAccountEntityCacheDataReceiver{" );
        sb.append( "uuid='" ).append( cacheKey ).append( '\'' );
        sb.append( ", linkedAccountEntity=" ).append( linkedAccountEntity );
        sb.append( ", cacheState=" ).append( cacheState );
        sb.append( ", error='" ).append( error ).append( '\'' );
        sb.append( ", dataExpiration=" ).append( dataExpiration );
        sb.append( '}' );
        return sb.toString();
    }
}
