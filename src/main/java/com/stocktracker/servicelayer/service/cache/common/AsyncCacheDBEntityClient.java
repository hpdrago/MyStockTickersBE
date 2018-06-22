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
 * @param <RQ> Cache request type.
 * @param <RS> Cache response type.
 * @param  <X> Cache Executor Type
 * @param  <C> Cache type.
 * @param <DR> Data Receiver.  The type that will receive the cached data
 * @param  <S> The entity service.
 */
public abstract class AsyncCacheDBEntityClient< K extends Serializable,
                                                T extends AsyncCacheDBEntity<K>,
                                               CE extends AsyncCacheEntry<K,T>,
                                               DR extends AsyncCacheDataReceiver<K,T>,
                                               RQ extends AsyncBatchCacheRequest<K,T>,
                                               RS extends AsyncBatchCacheResponse<K,T>,
                                                X extends AsyncCacheBatchServiceExecutor<K,T,RQ,RS>,
                                                C extends AsyncBatchCache<K,T,CE,RQ,RS,X>,
                                                S extends VersionedEntityService<K,T,?,?,?>>
    extends AsyncCacheBatchClient<K,T,CE,DR,RQ,RS,X,C>
{
    /**
     * Working with the cache, and the entity service, attempts to retrieve the data from the database.  If it is not
     * found or if the data is not current, an asynchronous fetch will be performed.  If the data is found and is current,
     * the {@code receiver} is updated with the entity information.
     * @param receiver
     */
    @Override
    public void getCachedData( final DR receiver )
    {
        final String methodName = "getCachedData";
        logMethodBegin( methodName, receiver );
        Objects.requireNonNull( receiver, "receiver argument cannon be null" );
        Objects.requireNonNull( receiver.getCacheKey(), "receiver's entity key cannot be null" );
        this.updateDataReceiver( receiver );
        /*
         * Check to see if the entity is STALE and needs to be refreshed.
         */
        if ( receiver.getCachedDataState().isStale() )
        {
            logDebug( methodName, "Making asynchronous fetch for {0}", receiver.getCacheKey() );
            this.getCache()
                .asynchronousGet( receiver.getCacheKey() );
        }
        logMethodEnd( methodName, receiver );
    }

    /**
     * The data is not in the cache, see if the data is in the database, if not go get it asynchronously.
     * @param searchKey
     * @param receiver
     */
    @Override
    protected void handleNotInCache( final K searchKey, final DR receiver )
    {
        final String methodName = "handleNotInCache";
        logMethodBegin( methodName, searchKey );
        try
        {
            this.getDBEntity( receiver, null );
        }
        catch( VersionedEntityNotFoundException e )
        {
            //receiver.setCacheDataState( NOT_FOUND );
            //receiver.setCacheError( searchKey + " was not found" );
            logDebug( methodName, "{0} was not in the map, asynchronously fetching now", searchKey );
            this.asynchronousFetch( receiver );
        }
        logMethodEnd( methodName, searchKey );
    }

    /**
     * Gets the entity from the database and evaluates its currency.
     * @param receiver
     * @param cacheEntry
     * @throws VersionedEntityNotFoundException
     */
    private void getDBEntity( final DR receiver, final CE cacheEntry )
        throws VersionedEntityNotFoundException
    {
        final String methodName = "getDBEntity";
        logMethodBegin( methodName, receiver.getCacheKey() );
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
                logDebug( methodName, "Entity {0} is CURRENT", receiver.getCacheKey() );
                receiver.setCacheDataState( CURRENT );
            }
            else
            {
                logDebug( methodName, "Entity {0} is STALE", receiver.getCacheKey() );
                receiver.setCacheDataState( STALE );
            }
            receiver.setCachedData( entity );
            /*
             * Need to create the cache entry for stale and current entities because the client is going to request the updated
             * values.
             */
            if ( cacheEntry == null )
            {
                this.getCache()
                    .createCacheEntry( receiver.getCacheKey(), receiver.getCachedData(), receiver.getCachedDataState() );
            }
        }
        catch( VersionedEntityNotFoundException e )
        {
            /*
             * if the database entity is not found, then we need to retrieve the data.
             */
            receiver.setCacheDataState( STALE );
            this.getCache()
                .createCacheEntry( receiver.getCacheKey(), receiver.getCachedData(), receiver.getCachedDataState() );
            /*
             * Throw the exception back to the caller and let them decide what to do.
             * When requesting a single entity, there will be an async request made right away.
             * In batch mode, this exception is ignored as the request for data will be made in a batch.
             */
            throw e;
        }
        logMethodEnd( methodName, receiver.getCacheKey() );
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
