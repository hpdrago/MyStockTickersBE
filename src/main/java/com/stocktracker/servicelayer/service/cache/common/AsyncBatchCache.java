package com.stocktracker.servicelayer.service.cache.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * An extension of {@code AsyncCache} that has the ability to make batch requests to third parties.
 *
 * @param <CK> - The key type to the cache
 * @param <CD> - The cached data type.
 * @param <ASK> - The async key used to fetch the data form the async source.
 * @param <ASD> - The type of cached data to obtain form the async source.
 * @param <CE> - The cache entry type which is a subclass of AsyncCacheEntry<ASD>.
 * @param <RQ> - Batch request type.
 * @param <RS> - Batch response type.
 * @param <RK> - Request key type.  Contains the CK, ASK key values.
 * @param <X>  - The interface definition of the class that will be performing synchronous and asynchronous work to
 *              get the information of type ASD.
 */
public abstract class AsyncBatchCache< CK extends Serializable,
                                       CD,
                                       ASK,
                                       ASD,
                                       CE extends AsyncCacheEntry<CK,CD,ASK,ASD>,
                                       RK extends AsyncBatchCacheRequestKey<CK,ASK>,
                                       RQ extends AsyncBatchCacheRequest<CK,CD,ASK>,
                                       RS extends AsyncBatchCacheResponse<CK,ASK,ASD>,
                                       X extends AsyncCacheBatchServiceExecutor<CK,CD,ASK,ASD,RQ,RS>>
    extends AsyncCache<CK,CD,ASK,ASD,CE,X>
{
    /**
     * Performs a batch request to the external entity for each of the request keys.
     * @param requestKeys
     */
    public void asynchronousGet( final List<RK> requestKeys )
    {
        final String methodName = "asynchronousGet";
        logMethodBegin( methodName, requestKeys );
        final List<RQ> requestList = new ArrayList<>( requestKeys.size());
        /*
         * Convert the request keys into a map of {@code AsyncBatchCacheRequest}s keyed by the cache key.
         * A maps is used so that the async process can easily map results to requests.
         */
        requestKeys.stream()
                   .map( requestKey ->
                         {
                             final RQ asyncBatchCacheRequest = this.createBatchRequestType();
                             CE cacheEntry = this.getCacheEntry( requestKey.getCacheKey() );
                             if ( cacheEntry == null )
                             {
                                 cacheEntry = this.createCacheEntry();
                                 cacheEntry.setCacheKey( requestKey.getCacheKey() );
                                 this.addCacheEntry( requestKey.getCacheKey(), cacheEntry );
                             }
                             asyncBatchCacheRequest.setCacheKey( requestKey.getCacheKey() );
                             asyncBatchCacheRequest.setAsyncProcessor( cacheEntry.getCachedDataSyncProcessor() );
                             return asyncBatchCacheRequest;
                         })
                   .forEach( asyncBatchCacheRequest -> requestList.add( asyncBatchCacheRequest ));
        /*
         * Make the batch request
         */
        this.getExecutor()
            .asynchronousFetch( requestList );
        logMethodEnd( methodName, requestList );
    }

    /**
     * Subclasses must override this method that creates a new instance of their request type.
     * @return
     */
    protected abstract RQ createBatchRequestType();
}
