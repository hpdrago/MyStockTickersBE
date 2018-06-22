package com.stocktracker.servicelayer.service.cache.common;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.servicelayer.service.BaseService;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState.CURRENT;
import static com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState.FAILURE;
import static com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState.NOT_FOUND;
import static com.stocktracker.servicelayer.service.cache.common.AsyncCacheFetchState.NOT_FETCHING;

/**
 * Abstract class that implements the common pattern of obtaining a value from a AsyncCache.
 * @param  <T> Cached data type.
 * @param  <K> Key type for cached data.
 * @param <CE> Cache Entry Type.
 * @param  <X> Cache Executor Type
 * @param  <C> Cache type.
 * @param <DR> Data Receiver.  The type that will receive the cached data
 */
public abstract class AsyncCacheClient< K extends Serializable,
                                        T extends AsyncCacheData,
                                       CE extends AsyncCacheEntry<K,T>,
                                       DR extends AsyncCacheDataReceiver<K,T>,
                                        X extends AsyncCacheServiceExecutor<K,T>,
                                        C extends AsyncCache<K,T,CE,X>>
    extends BaseService
    implements MyLogger
{
    /**
     * Set the cached data on multiple receivers.  See {@code getCachedData} for details.
     * @param receivers
     */
    public void getCachedData( final List<DR> receivers )
        throws VersionedEntityNotFoundException
    {
        final String methodName = "getCachedData";
        logMethodBegin( methodName, receivers.size() );
        for ( final DR receiver: receivers )
        {
            this.getCachedData( receiver );
        }
        logMethodEnd( methodName );
    }

    /**
     * Working with the cache, and the entity service, checks the cache for the data first and then attempts to
     * retrieve the data from the database.  If it is not found or if the data is not current, an asynchronous fetch
     * will be performed.  If the data is found and is current, the {@code receiver} is updated with the entity
     * information. This method is call {@code getCachedData} because the receiver is updated (set) with the cached
     * values if found or set with cache state information that indicates the data is being retrieved asyncrhonously.
     * @param receiver The object that will "receive" the cached data and cache state values.
     */
    public void getCachedData( final DR receiver )
    {
        final String methodName = "getCachedData";
        logMethodBegin( methodName, receiver );
        Objects.requireNonNull( receiver, "receiver argument cannon be null" );
        Objects.requireNonNull( receiver.getCacheKey(), "receiver's entity key cannot be null" );
        final K key = receiver.getCacheKey();
        logDebug( methodName, "Making asynchronous fetch for: {0}", key );
        final CE cacheEntry = this.getCache()
                                  .asynchronousGet( receiver.getCacheKey() );
        logDebug( methodName, "cacheEntry: {0}", cacheEntry );
        receiver.setCacheDataState( cacheEntry.getCacheState() );
        receiver.setCachedData( cacheEntry.getCachedData() );
        if ( cacheEntry.getCacheState().isFailure() )
        {
            receiver.setCacheError( cacheEntry.getFetchThrowable().getMessage() );
        }
        receiver.setExpirationTime( cacheEntry.getExpirationTime() );
        logMethodEnd( methodName, receiver );
    }

    /**
     * This method is called when no data exists and we need to cache to fetch the data.
     * @param receiver
     */
    protected void asynchronousFetch( final DR receiver )
    {
        final String methodName = "asynchronousFetch";
        logMethodBegin( methodName, receiver );
        final CE cacheEntry = this.getCache()
                                  .asynchronousGet( receiver.getCacheKey() );
        receiver.setCacheDataState( cacheEntry.getCacheState() );
        receiver.setCachedData( cacheEntry.getCachedData() );
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

    /**
     * Get the cached data for a search key.  It is assumed that an attempt has been made to determine if the cached
     * entry exists first and if the data is stale, the data will be retrieved.
     * @param searchKey
     * @return
     */
    public void getCachedData( final K searchKey, final DR receiver )
    {
        final String methodName = "getCachedData";
        logMethodBegin( methodName, searchKey );
        receiver.setCacheKey( searchKey );
        final CE cacheEntry = this.getCache()
                                  .getCacheEntry( searchKey );
        if ( cacheEntry != null )
        {
            logDebug( methodName, "cacheEntry: {0}", cacheEntry );
            if ( cacheEntry.getFetchState().isFetching() )
            {
                this.handleInCacheIsFetching( searchKey, receiver, cacheEntry );
            }
            else
            {
                this.handleInCacheNotFetching( searchKey, receiver, cacheEntry );
            }
        }
        else
        {
            this.handleNotInCache( searchKey, receiver );
        }
        logMethodEnd( methodName, receiver );
    }

    /**
     * Handles the case when the information is not found in the cache.  An synchronous call to obtain the information
     * will be made
     * @param searchKey
     * @param receiver
     */
    protected void handleNotInCache( final K searchKey, final DR receiver )
    {
        final String methodName = "handleNotInCache";
        logDebug( methodName, searchKey + " is not in the cache, fetching now" );
        final CE cacheEntry = this.getCache()
                                  .synchronousGet( searchKey );
        receiver.setCacheKey( searchKey );
        receiver.setCachedData( cacheEntry.getCachedData() );
        receiver.setCacheDataState( cacheEntry.getCacheState() );
        if ( cacheEntry.getFetchThrowable() != null )
        {
            receiver.setCacheError( cacheEntry.getFetchThrowable().getMessage() );
        }
        receiver.setExpirationTime( cacheEntry.getExpirationTime() );
    }

    /**
     * Handles the case when the entry is in the cache and it is not begin fetch so it should be current.
     * @param searchKey
     * @param receiver
     * @param cacheEntry
     */
    protected void handleInCacheNotFetching( final K searchKey, final DR receiver, final CE cacheEntry )
    {
        final String methodName = "handleInCacheNotFetching";
        /*
         * It's not fetching
         */
        logDebug( methodName, "It's in the cache and not fetching, checking currency" );
        receiver.setCacheKey( searchKey );
        if ( cacheEntry.getCachedData() != null )
        {
            T cachedData = this.createCachedDataObject();
            BeanUtils.copyProperties( cacheEntry.getCachedData(), cachedData );
        }
        if ( cacheEntry.isStale() )
        {
            logDebug( methodName, "It's stale" );
            T cachedData = this.getCache()
                               .synchronousGet( searchKey )
                               .getCachedData();
            receiver.setCachedData( cachedData );
            if ( cachedData == null )
            {
                receiver.setCacheDataState( NOT_FOUND );
            }
            else
            {
                receiver.setCacheDataState( CURRENT );
            }
        }
        else
        {
            logDebug( methodName, "It's current" );
            receiver.setCachedData( cacheEntry.getCachedData() );
            receiver.setCacheDataState( CURRENT );
        }
    }

    /**
     * Handles the condition when the information is already being fetched.  This method will subcribe to be notified
     * when the fetch has completed.  This is a blocking call.
     * @param searchKey
     * @param receiver
     * @param cacheEntry
     */
    protected void handleInCacheIsFetching( final K searchKey, final DR receiver, final CE cacheEntry )
    {
        final String methodName = "handleIsFetching";
        logMethodBegin( methodName, searchKey, cacheEntry );
        Objects.requireNonNull( searchKey, "searchKey argument cannot be null" );
        Objects.requireNonNull( cacheEntry, "cacheEntry argument cannot be null" );
        logDebug( methodName, "Is fetching.  Blocking and waiting" );
        final CE finalCacheEntry = cacheEntry;
        cacheEntry.getAsyncProcessor()
                  .doOnError( (Throwable e) ->
                            {
                                if ( e instanceof AsyncCacheDataNotFoundException )
                                {
                                    this.handleDataNotFound( searchKey, receiver, finalCacheEntry );
                                }
                                else
                                {
                                    this.handleFailure( searchKey, receiver, finalCacheEntry, e );
                                }
                            })
                  .blockingSubscribe( fetchedData ->
                                      {
                                          logDebug( methodName, "Received: " + fetchedData );
                                          if ( fetchedData != null )
                                          {
                                              this.handleDataFound( searchKey, receiver, finalCacheEntry, fetchedData );
                                          }
                                          else
                                          {
                                              this.handleDataNotFound( searchKey, receiver, finalCacheEntry );
                                          }
                                      });
        logMethodEnd( methodName, searchKey );
    }

    /**
     * This method is called when the data is successfully retrieved.
     * @param searchKey
     * @param receiver
     * @param cacheEntry
     * @param fetchedData
     */
    private void handleDataFound(  final K searchKey, final DR receiver, final CE cacheEntry, final T fetchedData )
    {
        /*
        final T cachedData = cacheEntry.getCachedData() == null
                                  ? this.createCachedDataObject()
                                  : cacheEntry.getCachedData();
        BeanUtils.copyProperties( fetchedData, cachedData );
                                  */
        receiver.setCacheKey( searchKey );
        receiver.setCachedData( fetchedData );
        receiver.setCacheDataState( CURRENT );
        receiver.setCacheError( null );
        cacheEntry.setCacheState( CURRENT );
        cacheEntry.setCachedData( searchKey, fetchedData );
        cacheEntry.setFetchState( NOT_FETCHING );
        cacheEntry.setFetchThrowable( null );
    }

    /**
     * This method is called when there is a failure retrieving the data.
     * @param searchKey
     * @param receiver
     * @param cacheEntry
     * @param throwable
     */
    private void handleFailure( final K searchKey, final DR receiver, final CE cacheEntry, final Throwable throwable )
    {
        receiver.setCacheKey( searchKey );
        receiver.setCachedData( null );
        receiver.setCacheDataState( FAILURE );
        receiver.setCacheError( throwable.getMessage() );
        cacheEntry.setFetchState( NOT_FETCHING );
        cacheEntry.setCacheState( FAILURE );
        cacheEntry.setFetchThrowable( throwable );
        cacheEntry.setCachedData( searchKey, null );
    }

    /**
     * This method is called when the data is not found.
     * @param searchKey
     * @param receiver
     * @param cacheEntry
     */
    private void handleDataNotFound( final K searchKey, final DR receiver, final CE cacheEntry )
    {
        receiver.setCacheKey( searchKey );
        receiver.setCachedData( null );
        receiver.setCacheDataState( NOT_FOUND );
        receiver.setCacheError( "Could not find entry for " + searchKey );
        cacheEntry.setFetchState( NOT_FETCHING );
        cacheEntry.setCacheState( NOT_FOUND );
        cacheEntry.setCachedData( searchKey, null );
        cacheEntry.setFetchThrowable( null );
    }

    /**
     * Get the cache instance.
     * @return
     */
    protected abstract C getCache();

    /**
     * Create an instance of the data type that will be cached.
     * @return
     */
    protected abstract T createCachedDataObject();

    /**
     * Update the receiver.
     * @param receiver
     */
    protected abstract void updateDataReceiver( final DR receiver )
        throws VersionedEntityNotFoundException;
}
