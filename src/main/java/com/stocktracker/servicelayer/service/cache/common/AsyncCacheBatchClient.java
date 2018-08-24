package com.stocktracker.servicelayer.service.cache.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
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
 * @param <CK> Key type for cached data.
 * @param <ASK> The key to the async data.
 * @param  <CD> Cached data type.
 * @param <CE> Cache Entry Type.
 * @param <RQ> Cache request type.
 * @param <RS> Cache response type.
 * @param  <X> Cache Executor Type
 * @param  <C> Cache type.
 * @param <DR> Data Receiver.  The type that will receive the cached data
 */
public abstract class AsyncCacheBatchClient<CK,
                                            CD extends AsyncCacheData,
                                           ASK,
                                           ASD,
                                            CE extends AsyncCacheEntry<CK,CD,ASK,ASD>,
                                            DR extends AsyncCacheDataReceiver<CK,CD,ASK>,
                                           CDC,
                                            RK extends AsyncBatchCacheRequestKey<CK,ASK>,
                                            RQ extends AsyncBatchCacheRequest<CK,CD,ASK>,
                                            RS extends AsyncBatchCacheResponse<CK,ASK,ASD>,
                                             X extends AsyncCacheBatchServiceExecutor<CK,CD,ASK,ASD,RQ,RS>,
                                             C extends AsyncBatchCache<CK,CD,ASK,ASD,CE,RK,RQ,RS,X>>
    extends AsyncCacheClient<CK,CD,ASK,ASD,CE,DR,X,C>
{

    /**
     * Set the stock price quotes for the list of dto containers.
     * @param containers
     */
    public void updateContainers( final List<? extends CDC> containers )
    {
        final String methodName = "getCachedData";
        Objects.requireNonNull( containers, "containers argument cannot be null" );
        logMethodBegin( methodName, containers.size() + " containers" );
        /*
         * Create the stock price quote data receivers
         */
        final List<DR> receivers = this.createReceivers( containers );
        /*
         * Search the cache for existing cached data.
         */
        final Map<CK,DR> receiverMap = this.searchCache( receivers );
        /*
         * Make asynchronous requests for the receivers where there was no cached data found or the cached data was
         * not current.
         */
        this.makeASyncRequests( receivers );
        /*
         * Update the containers with the cache information.
         */
        this.updateContainers( containers, receiverMap );
        logMethodEnd( methodName );
    }

    /**
     * Converts the list of containers into a list of receivers that can receive the cache information from the cache
     * and then update the receivers.
     * @param containers
     * @return
     */
    private List<DR> createReceivers( final List<? extends CDC> containers )
    {
        return containers.stream()
                         .map( (CDC container) ->
                               {
                                   final DR receiver = this.createDataReceiver();
                                   receiver.setAsyncKey( this.getASyncKey( container) );
                                   receiver.setCacheKey( this.getCacheKey( container ) );
                                   return receiver;
                               })
                         .collect( Collectors.toList() );
    }

    /**
     * Update the containers with the information from the cache.
     * @param containers
     * @param receiverMap
     */
    protected void updateContainers( final List<? extends CDC> containers,
                                     final Map<CK, DR> receiverMap )
    {
        final String methodName = "updateContainers";
        containers.forEach( container ->
                            {
                                final DR receiver = receiverMap.get( this.getCacheKey( container ));
                                if ( receiver == null )
                                {
                                    logError( methodName, "Cache failed to provide results for container: " +
                                                          container );
                                }
                                else
                                {
                                    this.setCacheKey( container, receiver.getCacheKey() );
                                    this.setCachedData( container, receiver.getCachedData() );
                                    this.setCacheError( container, receiver.getCacheError() );
                                    this.setCacheState( container, receiver.getCacheState() );
                                }
                            } );
    }

    /**
     * For each receiver, searches the cache for existing entries.
     * @param receivers
     * @return
     */
    protected Map<CK,DR> searchCache( final List<DR> receivers )
    {
        //final String methodName = "searchCache";
        //logMethodBegin( methodName, receivers );
        final Map<CK,DR> receiverMap = new HashMap<>();
        /*
         * Need to be be aware that there could be multiple receivers with the same cache key so we need to insure
         * that we just collection the unique keys here.
         */
        receivers.forEach( (DR receiver) ->
                           {
                               if ( !receiverMap.containsKey( receiver.getCacheKey() ))
                               {
                                    receiverMap.put( receiver.getCacheKey(), receiver );
                               }
                           });

        /*
         * Search the cache for each receiver.
         */
        receivers.forEach( this::searchCache );
        return receiverMap;
    }

    /**
     * Sets the cached data (if present) and the cache data state on the {@code receiver}.
     * @param receiver
     */
    protected void searchCache( final DR receiver )
    {
        Objects.requireNonNull( receiver, "receiver argument cannot be null" );
        final String methodName = "searchCache";
        //logMethodBegin( methodName );
        CE cacheEntry = this.getCache()
                            .getCacheEntry( receiver.getCacheKey() );
        if ( cacheEntry == null || cacheEntry.isStale() || cacheEntry.getCacheState() == null )
        {
            logDebug( methodName, "No cache entry found for " + receiver.getCacheKey() );
            receiver.setCacheState( STALE );
        }
        else
        {
            logDebug( methodName, "Cache entry for {0} found with cache state: {1}",
                      receiver.getCacheKey(), cacheEntry.getCacheState() );
            try
            {
                updateReceiverWithCacheInformation( cacheEntry, receiver );
            }
            catch( Exception e )
            {
                receiver.setCacheError( e.getMessage() );
                receiver.setCacheState( FAILURE );
                logError( methodName, " error updating receiver with cache key: " + receiver.getCacheKey(), e );
            }
        }
        //logMethodEnd( methodName );
    }

    /**
     * Updates the {@code receivers} with the any current information in the cache and performs an asynchronous fetch
     * for information that is not in the cache or for information that is stale.
     * @param receivers
     */
    protected void makeASyncRequests( final List<? extends DR> receivers )
    {
        final String methodName = "asynchronousGetCachedData";
        Objects.requireNonNull( receivers, "receivers argument cannot be null" );
        logMethodBegin( methodName, receivers.size() );
        /*
         * Update the receivers with the data if found and the cache state -- no fetch of data is made in this call
         * whatever can be retrieved from memory is done so now.
         */
        for ( final DR receiver: receivers )
        {
            this.searchCache( receiver );
        }
        /*
         * Need to go through the updated receivers to find any that need to be fetched (are stale)
         * and send the list of keys to the cache to perform the work.
         */
        final List<RK> requestKeys = receivers.stream()
                                              .filter( dr -> dr.getCacheState() == null ||
                                                             dr.getCacheState().isStale() )
                                              .map( dr -> this.createRequestKey( dr.getCacheKey(),
                                                                                 dr.getASyncKey() ))
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
     * Updates the receiver with the cache entry state and other information including the cached data if current.
     * @param cacheEntry
     * @param receiver
     */
    protected void updateReceiverWithCacheInformation( final CE cacheEntry, final DR receiver )
    {
        Objects.requireNonNull( receiver, "receiver argument cannot be null" );
        Objects.requireNonNull( cacheEntry, "cacheEntry argument cannot be null" );
        Objects.requireNonNull( cacheEntry.getCacheState(), "cacheEntry.cachedState cannot be null" );
        final String methodName = "updateReceiverWithCacheInformation";
        logMethodBegin( methodName, cacheEntry, receiver );
        switch ( cacheEntry.getCacheState() )
        {
            case CURRENT:
                receiver.setCacheState( CURRENT );
                receiver.setCachedData( cacheEntry.getCachedData() );
                break;

            case STALE:
                receiver.setCacheState( STALE );
                receiver.setCachedData( cacheEntry.getCachedData() );
                break;

            case FAILURE:
                receiver.setCacheState( FAILURE );
                receiver.setCachedData( null );
                receiver.setCacheError( receiver.getCacheError() );
                break;

            case NOT_FOUND:
                receiver.setCacheState( NOT_FOUND );
                receiver.setCachedData( null );
                break;
        }
        logMethodEnd( methodName, receiver );
    }

    /**
     * Subclasses must override this method to create a new async data receiver.
     * @return
     */
    public abstract DR createDataReceiver();

    /**
     * Subclasses must create a new instance of the request key that contains the cache key and the async key.
     * @param cacheKey
     * @param asyncKey
     * @return
     */
    protected abstract RK createRequestKey( final CK cacheKey, final ASK asyncKey );

    /**
     * Extracts the async key from the container.
     * @param container
     * @return
     */
    protected abstract ASK getASyncKey( final CDC container );

    /**
     * Extracts the cache key from the container.
     * @param container
     * @return
     */
    protected abstract CK getCacheKey( final CDC container );

    /**
     * Sets the cache state on the container.
     * @param container
     * @param cacheState
     */
    protected abstract void setCacheState( final CDC container, final AsyncCacheEntryState cacheState );

    /**
     * Sets the cache error on the container.
     * @param container
     * @param cacheError
     */
    protected abstract void setCacheError( final CDC container, final String cacheError );

    /**
     * Sets the cache data on the container.
     * @param container
     * @param cachedData
     */
    protected abstract void setCachedData( final CDC container, final CD cachedData );

    /**
     * Sets the cache key on the container.
     * @param container
     * @param cacheKey
     */
    protected abstract void setCacheKey( final CDC container, final CK cacheKey );

}