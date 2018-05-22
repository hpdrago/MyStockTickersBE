package com.stocktracker.servicelayer.service.cache.common;

import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.servicelayer.service.VersionedEntityService;

import java.io.Serializable;
import java.util.Objects;

import static com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState.CURRENT;
import static com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState.STALE;

/**
 * Abstract class that implements the common pattern of obtaining a value from a AsyncCache.
 * @param  <T> Cached data type.
 * @param  <K> Key type for cached data.
 * @param <CE> Cache Entry Type.
 * @param  <X> Cache Executor Type
 * @param  <C> Cache type.
 * @param <DR> Data Receiver.  The type that will receive the cached data
 * @param  <S> The entity service.
 */
public abstract class AsyncCacheDBEntityClient< K extends Serializable,
                                                T extends AsyncCacheDBEntity<K>,
                                               CE extends AsyncCacheEntry<T>,
                                                X extends AsyncCacheServiceExecutor<K,T>,
                                                C extends AsyncCache<K,T,CE,X>,
                                               DR extends AsyncCacheDataReceiver<K,T>,
                                                S extends VersionedEntityService<K,T,?,?,?>>
    extends AsyncCacheClient<K,T,CE,X,C,DR>
{
    /**
     * Working with the cache, and the entity service, attempts to retrieve the data from the database.  If it is not
     * found or if the data is not current, an asynchronous fetch will be performed.  If the data is found and is current,
     * the {@code receiver} is updated with the entity information.
     * @param receiver
     */
    public void setCachedData( final DR receiver )
    {
        final String methodName = "setCachedData";
        logMethodBegin( methodName, receiver );
        Objects.requireNonNull( receiver, "receiver argument cannon be null" );
        Objects.requireNonNull( receiver.getCacheKey(), "receiver's entity key cannot be null" );
        final K entityKey = receiver.getCacheKey();
        try
        {
            /*
             * Retrieve the entity from the database, it might not exist.
             */
            final T entity = this.getEntityService()
                                 .getEntity( receiver.getCacheKey() );
            logDebug( methodName, "Loaded entity: " + entity );
            receiver.setCachedData( entity );
            /*
             * Check to see if the entity needs to be refreshed.
             */
            if ( this.isCurrent( entity ))
            {
                logDebug( methodName, "Entity is CURRENT" );
                receiver.setCacheDataState( CURRENT );
            }
            else
            {
                logDebug( methodName, "Entity is STALE" );
                receiver.setCacheDataState( STALE );
                receiver.setCachedData( entity );
                /*
                 * The entity is STALE and need to be refreshed.  This is the job for the cache to perform this work
                 * asynchronously.
                 */
                logDebug( methodName, "Making asynchronous fetch" );
                this.getCache()
                    .asynchronousGet( receiver.getCacheKey(), entity );
            }
        }
        catch( VersionedEntityNotFoundException e )
        {
            logDebug( methodName, "{0} was was not found in the database, fetching new data", entityKey );
            this.asynchronousFetch( receiver );
        }
        logMethodEnd( methodName, receiver );
    }

    /**
     * Determines if the entity is current ie. not stale.
     * @param entity
     * @return
     */
    protected boolean isCurrent( final T entity )
    {
        return entity.getExpiration().getTime() > System.currentTimeMillis();
    }

    protected abstract S getEntityService();
}
