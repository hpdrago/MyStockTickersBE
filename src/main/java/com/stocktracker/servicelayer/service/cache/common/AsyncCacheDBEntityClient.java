package com.stocktracker.servicelayer.service.cache.common;

import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.servicelayer.service.VersionedEntityService;

import java.io.Serializable;
import java.util.Objects;

import static com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState.CURRENT;
import static com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState.STALE;

/**
 * Abstract class that implements the common pattern of obtaining a value from a AsyncCache.
 * @param  <K> Key type for cached data.
 * @param  <T> Cached data type.
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
            this.updateDataReceiver( receiver );
            /*
             * Check to see if the entity is STALE and needs to be refreshed.
             */
            if ( receiver.getCacheDataState().isStale() )
            {
                logDebug( methodName, "Making asynchronous fetch" );
                this.getCache()
                    .asynchronousGet( receiver.getCacheKey() );
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
     * Sets the cached data (if present) and the cache data state on the {@code receiver}.
     * @param receiver
     * @throws VersionedEntityNotFoundException
     */
    protected void updateDataReceiver( final DR receiver )
        throws VersionedEntityNotFoundException
    {
        final String methodName = "updateDataReceiver";
        logMethodBegin( methodName );
        /*
         * Check the cache first for existence.
         */
        final CE cacheEntry = this.getCache()
                                  .getCacheEntry( receiver.getCacheKey() );
        if ( cacheEntry == null || cacheEntry.isStale() )
        {
            /*
             * Retrieve the entity from the database, it might not exist.
             */
            final T entity;
            try
            {
                entity = this.getEntityService()
                                     .getEntity( receiver.getCacheKey() );
                logDebug( methodName, "Loaded entity: " + entity );
                receiver.setCachedData( entity );
                /*
                 * Check to see if the entity needs to be refreshed.
                 */
                if ( this.isCurrent( entity ) )
                {
                    logDebug( methodName, "Entity is CURRENT" );
                    receiver.setCacheDataState( CURRENT );
                }
                else
                {
                    logDebug( methodName, "Entity is STALE" );
                    receiver.setCacheDataState( STALE );
                    receiver.setCachedData( entity );
                }
                /*
                 * Need to create the cache entry for stale entities because the client is going to request the updated
                 * values.
                 */
                if ( cacheEntry == null && receiver.getCacheDataState().isStale() )
                {
                    this.getCache()
                        .createCacheEntry( receiver.getCacheKey(), receiver.getCachedData(), receiver.getCacheDataState() );
                }
            }
            catch( VersionedEntityNotFoundException e )
            {
                /*
                 * if the database entity is not found, then we need to retrieve the data.
                 */
                receiver.setCacheDataState( STALE );
                this.getCache()
                    .createCacheEntry( receiver.getCacheKey(), receiver.getCachedData(), receiver.getCacheDataState() );
                /*
                 * Throw the exception back to the caller and let them decide what to do.
                 * When requesting a single entity, there will be an async request made right away.
                 * In batch mode, this exception is ignored as the request for data will be made in a batch.
                 */
                throw e;
            }
        }
        else
        {
            logDebug( methodName, "Cache entry is present and current for {0}", receiver.getCacheKey() );
            receiver.setCachedData( cacheEntry.getCachedData() );
            receiver.setCacheDataState( CURRENT );
        }
        logMethodEnd( methodName );
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

    /**
     * Subclasses specify their entity service to be used to check the database for existence of requested entities.
     * @return
     */
    protected abstract S getEntityService();
}
