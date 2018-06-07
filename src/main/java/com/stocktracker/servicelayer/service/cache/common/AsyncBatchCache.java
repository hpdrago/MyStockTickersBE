package com.stocktracker.servicelayer.service.cache.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An extension of {@code AsyncCache} that has the ability to make batch requests to third parties.
 *
 * @param <T> - The type of cached data to obtain from the third party.
 * @param <K> - The key type to the cache -- this is key used to query the information from the third party.
 * @param <E> - The cache entry type which is a subclass of AsyncCacheEntry<T>.
 * @param <RQ>- Batch request type.
 * @param <RS>- Batch response type.
 * @param <X> - The interface definition of the class that will be performing synchronous and asynchronous work to
 *              get the information of type T.
 */
public abstract class AsyncBatchCache<K extends Serializable,
                                      T,
                                      E extends AsyncCacheEntry<T>,
                                      RQ extends AsyncBatchCacheRequest<K,T>,
                                      RS extends AsyncBatchCacheResponse<K,T>,
                                      X extends AsyncCacheBatchServiceExecutor<K,T,RQ,RS>>
    extends AsyncCache<K,T,E,X>
{
    /**
     * Performs a batch request to the external entity for each of the request keys.
     * @param requestKeys
     */
    public void asynchronousGet( final List<K> requestKeys )
    {
        final Map<K,RQ> requestMap = new HashMap<>();
        /*
         * Convert the request keys into a map of {@code AsyncBatchCacheRequest}s keyed by the cache key.
         * A maps is used so that the async process can easily map results to requests.
         */
        requestKeys.stream()
                   .map( cacheKey ->
                         {
                             final RQ asyncBatchCacheRequest = this.createBatchRequestType();
                             E cacheEntry = this.getCacheEntry( cacheKey );
                             if ( cacheEntry == null )
                             {
                                 cacheEntry = this.createCacheEntry();
                             }
                             asyncBatchCacheRequest.setCacheKey( cacheKey );
                             asyncBatchCacheRequest.setAsyncProcessor( cacheEntry.getAsyncProcessor() );
                             return asyncBatchCacheRequest;
                         })
                   .forEach( asyncBatchCacheRequest -> requestMap.put( asyncBatchCacheRequest.getCacheKey(), asyncBatchCacheRequest  ) );
        /*
         * Make the batch request
         */
        this.getExecutor()
            .asynchronousFetch( requestMap );
    }

    /**
     * Subclasses must override this method that creates a new instance of their request type.
     * @return
     */
    protected abstract RQ createBatchRequestType();
}
