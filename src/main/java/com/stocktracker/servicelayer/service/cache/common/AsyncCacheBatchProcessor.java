package com.stocktracker.servicelayer.service.cache.common;

import com.stocktracker.servicelayer.service.BaseService;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This class provides the ability to process a batch of containers (<C>) to obtain the async cache data for each container
 * and then update the containers with the DTO information for the cache data.
 * @param <CK>  - Key type.
 * @param <CD>  - Cache data type.
 * @param <ASK> - async key type.  Used to query the information form the async source.
 * @param <ASD> - async data type.
 * @param <CE> - Cache entry type.
 * @param <DR> - Data receiver type.
 * @param <RQ> - Asynchronous request data type.
 * @param <RS> - Asynchronous response data type.
 * @param <RK> - The request key that contains the cache key and the async key.
 * @param <CDC> - Container type.
 * @param <X>  - Async executor type.
 * @param <C>  - Cache type.
 * @param <CL> - Batch client type.
 */
public abstract class AsyncCacheBatchProcessor< CK extends Serializable,
                                                CD extends AsyncCacheData,
                                               ASK,
                                               ASD,
                                                CE extends AsyncCacheEntry<CK,CD,ASK,ASD>,
                                                DR extends AsyncCacheDataReceiver<CK,CD,ASK>,
                                                RK extends AsyncBatchCacheRequestKey<CK,ASK>,
                                                RQ extends AsyncBatchCacheRequest<CK,CD,ASK>,
                                                RS extends AsyncBatchCacheResponse<CK,ASK,ASD>,
                                               CDC extends AsyncCachedDataContainer<CK,CD>,
                                                 X extends AsyncCacheBatchServiceExecutor<CK,CD,ASK,ASD,RQ,RS>,
                                                 C extends AsyncBatchCache<CK,CD,ASK,ASD,CE,RK,RQ,RS,X>,
                                                CL extends AsyncCacheBatchClient<CK,CD,ASK,ASD,CE,DR,RK,RQ,RS,X,C>>
    extends BaseService
{
    /**
     * Populates the {@code dataReceivers} with the cached data.
     * @param dataReceivers
     */
    public void getCachedData( final List<? extends DR> dataReceivers )
    {
        /*
         * Make the batch request, the cachedDataContainers contain the request key and will be populated with the
         * cached information.
         */
        this.processBatch( dataReceivers );
    }

    /**
     * Gets the cache information and state for all of the DTOs in {@code containers}.  The information is requested
     * as a batch and not individually.
     * @param receivers
     */
    protected void processBatch( final List<? extends DR> receivers )
    {
        final String methodName = "processBatch";
        logMethodBegin( methodName, receivers.size() );
        /*
         * Make the batch data request and update the receivers with the status of the stock quote.
         */
        this.getAsyncCacheClient()
            .asynchronousGetCachedData( receivers );
        logMethodEnd( methodName );
    }

    /**
     * Subclasses must return the cache key for the {@code container}.
     * @param container
     * @return
     */
    protected abstract CK getCacheKey( final CDC container );

    /**
     * Subclasses must specify the async cache client as it is used to process the batch.
     * @return
     */
    public abstract CL getAsyncCacheClient();
}
