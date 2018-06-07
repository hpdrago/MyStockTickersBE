package com.stocktracker.servicelayer.service.cache.common;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.servicelayer.service.BaseService;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState.CURRENT;
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
                                       CE extends AsyncCacheEntry<T>,
                                        X extends AsyncCacheServiceExecutor<K,T>,
                                        C extends AsyncCache<K,T,CE,X>,
                                       DR extends AsyncCacheDataReceiver<K,T>>
    extends BaseService
    implements MyLogger
{
    /**
     * Set the cached data on multiple receivers.  See {@code setCachedData} for details.
     * @param receivers
     */
    public void setCachedData( final List<DR> receivers )
    {
        final String methodName = "setCachedData";
        logMethodBegin( methodName, receivers.size() );
        for ( final DR receiver: receivers )
        {
            this.setCachedData( receiver );
        }
        logMethodEnd( methodName );
    }

    /**
     * Working with the cache, and the entity service, checks the cache for the data first and then attempts to
     * retrieve the data from the database.  If it is not found or if the data is not current, an asynchronous fetch
     * will be performed.  If the data is found and is current, the {@code receiver} is updated with the entity
     * information. This method is call {@code setCachedData} because the receiver is updated (set) with the cached
     * values if found or set with cache state information that indicates the data is being retrieved asyncrhonously.
     * @param receiver The object that will "receive" the cached data and cache state values.
     */
    public void setCachedData( final DR receiver )
    {
        final String methodName = "setCachedData";
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
        receiver.setDataExpiration( cacheEntry.getExpirationTime() );
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

        T cachedData = this.createCachedDataObject();
        final CE cacheEntry = this.getCache()
                                  .getCacheEntry( searchKey );
        if ( cacheEntry != null )
        {
            logDebug( methodName, "cacheEntry: {0}", cacheEntry );
            if ( cacheEntry.getFetchState().isFetching() )
            {
                logDebug( methodName, "Is fetching.  Blocking and waiting" );
                final T finalCachedData = cachedData;
                final CE finalCacheEntry = cacheEntry;
                cacheEntry.getAsyncProcessor()
                          .blockingSubscribe( fetchedData ->
                                              {
                                                  logDebug( methodName, "Received: " + fetchedData );
                                                  if ( fetchedData != null )
                                                  {
                                                      BeanUtils.copyProperties( fetchedData, finalCachedData );
                                                      receiver.setCachedData( fetchedData );
                                                      receiver.setCacheDataState( CURRENT );
                                                      finalCacheEntry.setFetchState( NOT_FETCHING );
                                                      finalCacheEntry.setCacheState( CURRENT );
                                                      finalCacheEntry.setCachedData( fetchedData );
                                                  }
                                                  else
                                                  {
                                                      receiver.setCachedData( null );
                                                      receiver.setCacheDataState( NOT_FOUND );
                                                      finalCacheEntry.setFetchState( NOT_FETCHING );
                                                      receiver.setCacheError( "Could not find entry for " + searchKey );
                                                  }
                                              });
            }
            else
            {
                /*
                 * It's in the cache.
                 */
                logDebug( methodName, "It's in the cache and not fetching, checking currency" );
                if ( cacheEntry.getCachedData() != null )
                {
                    BeanUtils.copyProperties( cacheEntry.getCachedData(), cachedData );
                }
                if ( cacheEntry.isStale() )
                {
                    logDebug( methodName, "It's stale" );
                    cachedData = this.getCache()
                                     .synchronousGet( searchKey )
                                     .getCachedData();
                    receiver.setCachedData( cachedData );
                    receiver.setCacheDataState( CURRENT );
                }
                else
                {
                    logDebug( methodName, "It's current" );
                    receiver.setCachedData( cachedData );
                    receiver.setCacheDataState( CURRENT );
                }
            }
        }
        else
        {
            logDebug( methodName, searchKey + " is not in the cache, fetching now" );
            cachedData = this.getCache()
                             .synchronousGet( searchKey )
                             .getCachedData();
            receiver.setCachedData( cachedData );
            receiver.setCacheDataState( CURRENT );
        }
        logMethodEnd( methodName, receiver );
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
}
