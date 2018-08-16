package com.stocktracker.servicelayer.service.cache.common;

import com.stocktracker.common.MyLogger;
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
 * @param <CK> Key type for cached data.
 * @param <ASK> Third Party Key type used to fetch the async data.
 * @param <CD> Cached data type.
 * @param <CE> Cache Entry Type.
 * @param <X> Cache Executor Type
 * @param <C> Cache type.
 * @param <DR> Data Receiver.  The type that will receive the cached data
 */
public abstract class AsyncCacheClient<CK extends Serializable,
                                       CD extends AsyncCacheData,
                                      ASK,
                                      ASD,
                                       CE extends AsyncCacheEntry<CK,CD,ASK,ASD>,
                                       DR extends AsyncCacheDataReceiver<CK,CD,ASK>,
                                        X extends AsyncCacheServiceExecutor<ASK,ASD>,
                                        C extends AsyncCache<CK,CD,ASK,ASD,CE,X>>
    extends BaseService
    implements MyLogger
{
    /**
     * Set the cached data on multiple receivers.  See {@code asynchronousGetCachedData} for details.
     * @param receivers
     */
    public void asynchronousGetCachedData( final List<DR> receivers )
        throws AsyncCacheDataRequestException
    {
        final String methodName = "asynchronousGetCachedData";
        logMethodBegin( methodName, receivers.size() );
        for ( final DR receiver: receivers )
        {
            this.asynchronousGetCachedData( receiver );
        }
        logMethodEnd( methodName );
    }

    /**
     * This method is called {@code asynchronousGetCachedData} because the receiver is updated (set) with the cached
     * values if found or set with cache state information that indicates the data is being retrieved asynchronously.
     * @param receiver The object that will "receive" the cached data and cache state values.
     */
    public void asynchronousGetCachedData( final DR receiver )
    {
        final String methodName = "asynchronousGetCachedData";
        logMethodBegin( methodName, receiver );
        Objects.requireNonNull( receiver, "receiver argument cannon be null" );
        Objects.requireNonNull( receiver.getCacheKey(), "receiver's entity key cannot be null" );
        final CK key = receiver.getCacheKey();
        logDebug( methodName, "Making asynchronous fetch for: {0}", key );
        final CE cacheEntry = this.getCache()
                                  .asynchronousGet( receiver.getCacheKey(), receiver.getASyncKey() );
        logDebug( methodName, "cacheEntry: {0}", cacheEntry );
        receiver.setCacheState( cacheEntry.getCacheState() );
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
        throws AsyncCacheDataRequestException
    {
        final String methodName = "asynchronousFetch";
        logMethodBegin( methodName, receiver );
        final CE cacheEntry = this.getCache()
                                  .asynchronousGet( receiver.getCacheKey(), receiver.getASyncKey() );
        receiver.setCacheState( cacheEntry.getCacheState() );
        receiver.setCachedData( cacheEntry.getCachedData() );
        logMethodEnd( methodName, receiver );
    }

    /**
     * Get the cache data and block and wait if it is not available.
     * @param receiver
     * @return
     */
    protected void synchronousFetch( final DR receiver )
    {
        final String methodName = "synchronousFetch";
        logMethodBegin( methodName, receiver );
        /*
         * Now block and wait if needed.
         */
        final CE cacheEntry = this.getCache()
                                  .synchronousGet( receiver.getCacheKey(), receiver.getASyncKey() );
        receiver.setCacheState( cacheEntry.getCacheState() );
        receiver.setCachedData( cacheEntry.getCachedData() );
        logMethodEnd( methodName, receiver );
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
     * Get the cached data for a search key.  It is assumed that an attempt has been made to determine if the cached
     * entry exists first and if the data is stale, the data will be retrieved synchronously.
     * @param receiver
     * @return
     */
    public void synchronousGetCachedData( final DR receiver )
    {
        final String methodName = "asynchronousGetCachedData";
        logMethodBegin( methodName, receiver );
        final CE cacheEntry = this.getCache()
                                  .getCacheEntry( receiver.getCacheKey() );
        if ( cacheEntry != null )
        {
            logDebug( methodName, "cacheEntry: {0}", cacheEntry );
            if ( cacheEntry.getFetchState().isFetching() )
            {
                this.handleInCacheIsFetching( receiver, cacheEntry );
            }
            else
            {
                this.handleInCacheNotFetching( receiver, cacheEntry );
            }
        }
        else
        {
            this.handleNotInCache( receiver );
        }
        logMethodEnd( methodName, receiver );
    }

    /**
     * Handles the case when the information is not found in the cache.  An synchronous call to obtain the information
     * will be made
     * @param receiver
     */
    protected void handleNotInCache( final DR receiver )
    {
        final String methodName = "handleNotInCache";
        logDebug( methodName, receiver.getCacheKey() + " is not in the cache, fetching now" );
        final CE cacheEntry = this.getCache()
                                  .synchronousGet( receiver.getCacheKey(), receiver.getASyncKey() );
        receiver.setCacheKey( receiver.getCacheKey() );
        receiver.setCachedData( cacheEntry.getCachedData() );
        receiver.setCacheState( cacheEntry.getCacheState() );
        if ( cacheEntry.getFetchThrowable() != null )
        {
            receiver.setCacheError( cacheEntry.getFetchThrowable().getMessage() );
        }
        receiver.setExpirationTime( cacheEntry.getExpirationTime() );
    }

    /**
     * Handles the case when the entry is in the cache and it is not begin fetch so it should be current.
     * @param receiver
     * @param cacheEntry
     */
    protected void handleInCacheNotFetching( final DR receiver, final CE cacheEntry )
    {
        final String methodName = "handleInCacheNotFetching";
        /*
         * It's not fetching
         */
        logDebug( methodName, "It's in the cache and not fetching, checking currency" );
        if ( cacheEntry.getCachedData() != null )
        {
            CD cachedData = this.createCachedDataObject();
            BeanUtils.copyProperties( cacheEntry.getCachedData(), cachedData );
        }
        if ( cacheEntry.isStale() )
        {
            logDebug( methodName, "It's stale" );
            CD cachedData = this.getCache()
                                .synchronousGet( receiver.getCacheKey(), receiver.getASyncKey() )
                                .getCachedData();
            receiver.setCachedData( cachedData );
            if ( cachedData == null )
            {
                receiver.setCacheState( NOT_FOUND );
            }
            else
            {
                receiver.setCacheState( CURRENT );
            }
        }
        else
        {
            logDebug( methodName, "It's current" );
            receiver.setCachedData( cacheEntry.getCachedData() );
            receiver.setCacheState( CURRENT );
        }
    }

    /**
     * Handles the condition when the information is already being fetched.  This method will subcribe to be notified
     * when the fetch has completed.  This is a blocking call.
     * @param receiver
     * @param cacheEntry
     */
    protected void handleInCacheIsFetching( final DR receiver, final CE cacheEntry )
    {
        final String methodName = "handleIsFetching";
        logMethodBegin( methodName, receiver, cacheEntry );
        Objects.requireNonNull( receiver, "receiver argument cannot be null" );
        Objects.requireNonNull( cacheEntry, "cacheEntry argument cannot be null" );
        logDebug( methodName, "Is fetching.  Blocking and waiting" );
        final CE finalCacheEntry = cacheEntry;
        cacheEntry.getCachedDataSyncProcessor()
                  .doOnError( (Throwable e) ->
                            {
                                if ( e instanceof AsyncCacheDataNotFoundException )
                                {
                                    this.handleDataNotFound( receiver, finalCacheEntry );
                                }
                                else
                                {
                                    this.handleFailure( receiver, finalCacheEntry, e );
                                }
                            })
                  .blockingSubscribe( fetchedData ->
                                      {
                                          logDebug( methodName, "Received: " + fetchedData );
                                          if ( fetchedData != null )
                                          {
                                              this.handleDataFound( receiver, finalCacheEntry, fetchedData );
                                          }
                                          else
                                          {
                                              this.handleDataNotFound( receiver, finalCacheEntry );
                                          }
                                      });
        logMethodEnd( methodName, cacheEntry );
    }

    /**
     * This method is called when the data is successfully retrieved.
     * @param receiver
     * @param cacheEntry
     * @param fetchedData
     */
    private void handleDataFound( final DR receiver,
                                  final CE cacheEntry,
                                  final CD fetchedData )
    {
        /*
        final CD cachedData = cacheEntry.asynchronousGetCachedData() == null
                                  ? this.createCachedDataObject()
                                  : cacheEntry.asynchronousGetCachedData();
        BeanUtils.copyProperties( fetchedData, cachedData );
                                  */
        receiver.setCachedData( fetchedData );
        receiver.setCacheState( CURRENT );
        receiver.setCacheError( null );
        cacheEntry.setCacheState( CURRENT );
        cacheEntry.setCachedData( receiver.getCacheKey(), receiver.getASyncKey(), fetchedData );
        cacheEntry.setFetchState( NOT_FETCHING );
        cacheEntry.setFetchThrowable( null );
    }

    /**
     * This method is called when there is a failure retrieving the data.
     * @param receiver
     * @param cacheEntry
     * @param throwable
     */
    private void handleFailure( final DR receiver, final CE cacheEntry, final Throwable throwable )
    {
        receiver.setCachedData( null );
        receiver.setCacheState( FAILURE );
        receiver.setCacheError( throwable.getMessage() );
        cacheEntry.setFetchState( NOT_FETCHING );
        cacheEntry.setCacheState( FAILURE );
        cacheEntry.setFetchThrowable( throwable );
        cacheEntry.setCachedData( receiver.getCacheKey(), receiver.getASyncKey(), null );
    }

    /**
     * This method is called when the data is not found.
     * @param cacheKey
     * @param receiver
     * @param cacheEntry
     */
    private void handleDataNotFound( final DR receiver, final CE cacheEntry )
    {
        receiver.setCachedData( null );
        receiver.setCacheState( NOT_FOUND );
        receiver.setCacheError( "Could not find entry for " + receiver.getCacheKey() );
        cacheEntry.setFetchState( NOT_FETCHING );
        cacheEntry.setCacheState( NOT_FOUND );
        cacheEntry.setCachedData( receiver.getCacheKey(), receiver.getASyncKey(), null );
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
    protected abstract CD createCachedDataObject();
}
