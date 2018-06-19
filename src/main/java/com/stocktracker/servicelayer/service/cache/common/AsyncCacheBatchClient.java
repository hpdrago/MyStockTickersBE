package com.stocktracker.servicelayer.service.cache.common;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState.CURRENT;
import static com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState.FAILURE;
import static com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState.NOT_FOUND;
import static com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState.STALE;

/**
 * This class extends {@code ASyncCacheDBEntityClient} to leverage the batch capability of IEXTrading so that a single
 * request containing multiple ticker symbols can be sent to IEXTrading to reduce the amount of calls made to IEXTrading
 * as that service does have volume restrictions.  We also get performance improvements by batching the requests.
 *
 * @param  <K> Key type for cached data.
 * @param  <T> Cached data type.
 * @param <CE> Cache Entry Type.
 * @param <RQ> Cache request type.
 * @param <RS> Cache response type.
 * @param  <X> Cache Executor Type
 * @param  <C> Cache type.
 * @param <DR> Data Receiver.  The type that will receive the cached data
 */
public abstract class AsyncCacheBatchClient< K extends Serializable,
                                             T extends AsyncCacheData,
                                            CE extends AsyncCacheEntry<T>,
                                            DR extends AsyncCacheDataReceiver<K,T>,
                                            RQ extends AsyncBatchCacheRequest<K,T>,
                                            RS extends AsyncBatchCacheResponse<K,T>,
                                             X extends AsyncCacheBatchServiceExecutor<K,T,RQ,RS>,
                                             C extends AsyncBatchCache<K,T,CE,RQ,RS,X>>
    extends AsyncCacheClient<K,T,CE,DR,X,C>
{
    /**
     * Updates the {@code receivers} with the any current information in the cache and performs an asynchronous fetch
     * for information that is not in the cache or for information that is stale.
     * @param receivers
     */
    @Override
    public void getCachedData( final List<DR> receivers )
    {
        final String methodName = "getCachedData";
        Objects.requireNonNull( receivers, "receivers argument cannot be null" );
        logMethodBegin( methodName, receivers.size() );
        /*
         * Update the receivers with the data if found and the cache state -- no fetch of data is made in this call
         * whatever can be retrieved from memory is done so now.
         */
        for ( final DR receiver: receivers )
        {
            this.updateDataReceiver( receiver );
        }
        /*
         * Need to go through the updated receivers to find any that need to be fetched (are stale)
         * and send the list of keys to the cache to perform the work.
         */
        final List<K> requestKeys = receivers.stream()
                                             .filter( dr -> dr.getCachedDataState().isStale() )
                                             .map( dr -> dr.getCacheKey() )
                                             .collect( Collectors.toList() );
        /*
         * Request batch updates for the request keys.
         */
        if ( requestKeys.isEmpty() )
        {
            logDebug( methodName, "All entities are CURRENT, no asynchronous fetch required." );
        }
        else
        {
            /*
             * Make the asynchronous call to the cache to retrieve missing or stale information.
             */
            this.getCache()
                .asynchronousGet( requestKeys );
        }
        logMethodEnd( methodName );
    }

    /**
     * Sets the cached data (if present) and the cache data state on the {@code receiver}.
     * @param receiver
     */
    protected void updateDataReceiver( final DR receiver )
    {
        Objects.requireNonNull( receiver, "receiver argument cannot be null" );
        final String methodName = "updateDataReceiver";
        logMethodBegin( methodName );
        CE cacheEntry = this.getCache()
                            .getCacheEntry( receiver.getCacheKey() );
        if ( cacheEntry == null || cacheEntry.isStale() || cacheEntry.getCacheState() == null )
        {
            receiver.setCacheDataState( STALE );
        }
        else
        {
            logDebug( methodName, "Cache entry for {0} found with cache state: {1}",
                      receiver.getCacheKey(), receiver.getCachedDataState() );
            try
            {
                updateReceiverWithCacheInformation( cacheEntry, receiver );
            }
            catch( Exception e )
            {
                receiver.setCacheError( e.getMessage() );
                receiver.setCacheDataState( FAILURE );
                logError( methodName, " error updating receiver with cache key: " + receiver.getCacheKey(), e );
            }
        }
        logMethodEnd( methodName );
    }

    /**
     * Updates the receiver with the cache entry state and other information including the cached data if current.
     * @param cacheEntry
     * @param receiver
     */
    protected void updateReceiverWithCacheInformation( final CE cacheEntry, final DR receiver )
    {
        Objects.requireNonNull( receiver, "receiver argument cannot be null" );
        Objects.requireNonNull( receiver.getCachedDataState(), "receiver.cachedState cannot be null" );
        Objects.requireNonNull( cacheEntry, "cacheEntry argument cannot be null" );
        final String methodName = "updateReceiverWithCacheInformation";
        logMethodBegin( methodName, cacheEntry, receiver );
        switch ( cacheEntry.getCacheState() )
        {
            case CURRENT:
                receiver.setCacheDataState( CURRENT );
                receiver.setCachedData( cacheEntry.getCachedData() );
                break;

            case STALE:
                receiver.setCacheDataState( STALE );
                receiver.setCachedData( cacheEntry.getCachedData() );
                break;

            case FAILURE:
                receiver.setCacheDataState( FAILURE );
                receiver.setCachedData( null );
                receiver.setCacheError( receiver.getCacheError() );
                break;

            case NOT_FOUND:
                receiver.setCacheDataState( NOT_FOUND );
                receiver.setCachedData( null );
                break;
        }
        logMethodEnd( methodName, receiver );
    }
}