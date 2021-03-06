package com.stocktracker.servicelayer.service.cache.common;

import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.servicelayer.service.VersionedEntityService;

import java.io.Serializable;
import java.util.Objects;

import static com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState.CURRENT;
import static com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState.STALE;

/**
 * Abstract class that implements the common pattern of obtaining a value from a AsyncCache.
 * @param <CK> Key type for cached data.
 * @param <CD> Cached data type.
 * @param <ASK> The key to the async key.
 * @param <ASD> The async data.
 * @param <CE> Cache Entry type.
 * @param <DR> Data receiver type.
 * @param <RQ> Cache request type.
 * @param <RS> Cache response type.
 * @param  <X> Cache executor type
 * @param  <C> Cache type.
 * @param <DR> Data Receiver.  The type that will receive the cached data
 * @param  <S> The entity service.
 */
public abstract class AsyncCacheDBEntityClient<CK extends Serializable,
                                               CD extends AsyncCacheDBEntity<CK>,
                                              ASK,
                                              ASD,
                                               CE extends AsyncCacheEntry<CK,CD,ASK,ASD>,
                                               DR extends AsyncCacheDataReceiver<CK,CD,ASK>,
                                              CDC,
                                               RK extends AsyncBatchCacheRequestKey<CK,ASK>,
                                               RQ extends AsyncBatchCacheRequest<CK,CD,ASK>,
                                               RS extends AsyncBatchCacheResponse<CK,ASK,ASD>,
                                                X extends AsyncCacheBatchServiceExecutor<CK,CD,ASK,ASD,RQ,RS>,
                                                C extends AsyncBatchCache<CK,CD,ASK,ASD,CE,RK,RQ,RS,X>,
                                                S extends VersionedEntityService<CK,CD,?,?,?>>
    extends AsyncCacheBatchClient<CK,CD,ASK,ASD,CE,DR,CDC,RK,RQ,RS,X,C>
{
    /**
     * Working with the cache, and the entity service, attempts to retrieve the data from the database.  If it is not
     * found or if the data is not current, an asynchronous fetch will be performed.  If the data is found and is current,
     * the {@code receiver} is updated with the entity information.
     * @param receiver
     */
    @Override
    public void asynchronousGetCachedData( final DR receiver )
    {
        final String methodName = "asynchronousGetCachedData";
        logMethodBegin( methodName, receiver );
        Objects.requireNonNull( receiver, "receiver argument cannon be null" );
        Objects.requireNonNull( receiver.getCacheKey(), "receiver's entity key cannot be null" );
        this.searchCache( receiver );
        /*
         * Check to see if the entity is STALE and needs to be refreshed.
         */
        if ( receiver.getCacheState().isStale() )
        {
            logDebug( methodName, "Making asynchronous fetch for {0}", receiver.getCacheKey() );
            this.getCache()
                .asynchronousGet( receiver.getCacheKey(), receiver.getASyncKey() );
        }
        logMethodEnd( methodName, receiver );
    }

    /**
     * The data is not in the cache, see if the data is in the database, if not go get it asynchronously.
     * @param receiver
     * @return new cache entry
     */
    @Override
    protected CE handleNotInCache( final DR receiver )
    {
        final String methodName = "handleNotInCache";
        logMethodBegin( methodName, receiver );
        CE cacheEntry;
        try
        {
            cacheEntry = this.getDBEntity( receiver, null );
            if ( receiver.getCacheState().isStale() )
            {
                logDebug( methodName, "{0} is stale, synchronously fetching now", receiver.getCacheKey() );
                cacheEntry = this.synchronousFetch( receiver );
            }
            else if ( receiver.getCacheState().isNotFound() )
            {
                logDebug( methodName, "{0} was not found, synchronously fetching now", receiver.getCacheKey() );
                cacheEntry = this.synchronousFetch( receiver );
            }
        }
        catch( VersionedEntityNotFoundException e )
        {
            logDebug( methodName, "{0} was not in the database, synchronously fetching now", receiver.getCacheKey() );
            cacheEntry = this.synchronousFetch( receiver );
        }
        logMethodEnd( methodName, receiver.getCacheKey() );
        return cacheEntry;
    }

    /**
     * Gets the entity from the database and evaluates its currency.
     * @param receiver
     * @param cacheEntry
     * @throws VersionedEntityNotFoundException
     */
    private CE getDBEntity( final DR receiver, final CE cacheEntry )
        throws VersionedEntityNotFoundException
    {
        final String methodName = "getDBEntity";
        logMethodBegin( methodName, receiver.getCacheKey() );
        /*
         * Retrieve the entity from the database, it might not exist.
         */
        final CD entity;
        CE myCacheEntry = cacheEntry;
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
                receiver.setCacheState( CURRENT );
            }
            else
            {
                logDebug( methodName, "Entity {0} is STALE", receiver.getCacheKey() );
                receiver.setCacheState( STALE );
            }
            receiver.setCachedData( entity );
            /*
             * Need to create the cache entry for stale and current entities because the client is going to request the updated
             * values.
             */
            if ( cacheEntry == null )
            {
                myCacheEntry = this.getCache()
                                   .createCacheEntry( receiver.getCacheKey(),
                                                      receiver.getCachedData(),
                                                      receiver.getASyncKey(),
                                                      receiver.getCacheState() );
            }
        }
        catch( VersionedEntityNotFoundException e )
        {
            /*
             * if the database entity is not found, then we need to retrieve the data.
             */
            receiver.setCacheState( STALE );
            this.getCache()
                .createCacheEntry( receiver.getCacheKey(),
                                   receiver.getCachedData(),
                                   receiver.getASyncKey(),
                                   receiver.getCacheState() );
            /*
             * Throw the exception back to the caller and let them decide what to do.
             * When requesting a single entity, there will be an async request made right away.
             * In batch mode, this exception is ignored as the request for data will be made in a batch.
             */
            throw e;
        }
        logMethodEnd( methodName, receiver.getCacheKey() );
        return myCacheEntry;
    }

    /**
     * Determines if the entity is current ie. not stale.
     * @param entity
     * @return
     */
    protected boolean isCurrent( final CD entity )
    {
        return entity.getExpiration().getTime() > System.currentTimeMillis();
    }

    /**
     * Subclasses specify their entity service to be used to check the database for existence of requested entities.
     * @return
     */
    protected abstract S getEntityService();
}
