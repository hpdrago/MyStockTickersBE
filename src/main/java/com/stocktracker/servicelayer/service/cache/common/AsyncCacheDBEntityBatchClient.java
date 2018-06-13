package com.stocktracker.servicelayer.service.cache.common;

import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.servicelayer.service.VersionedEntityService;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

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
 * @param  <S> The entity service.
 */
public abstract class AsyncCacheDBEntityBatchClient< K extends Serializable,
                                                     T extends AsyncCacheDBEntity<K>,
                                                    CE extends AsyncCacheEntry<T>,
                                                    RQ extends AsyncBatchCacheRequest<K,T>,
                                                    RS extends AsyncBatchCacheResponse<K,T>,
                                                     X extends AsyncCacheBatchServiceExecutor<K,T,RQ,RS>,
                                                     C extends AsyncBatchCache<K,T,CE,RQ,RS,X>,
                                                    DR extends AsyncCacheDataReceiver<K,T>,
                                                     S extends VersionedEntityService<K,T,?,?,?>>
    extends AsyncCacheDBEntityClient<K,T,CE,X,C,DR,S>
{
    /**
     * This method overrides the default method to leverage
     * See {@code setCachedData} for details.
     * @param receivers
     */
    @Override
    public void setCachedData( final List<DR> receivers )
    {
        final String methodName = "setCachedData";
        logMethodBegin( methodName, receivers.size() );
        /*
         * Update the receivers with the data if found and the cache state -- no fetch of data is made in this call.
         */
        for ( final DR receiver: receivers )
        {
            try
            {
                super.updateDataReceiver( receiver );
            }
            catch( VersionedEntityNotFoundException e )
            {
                // ignore this exception as we retrieve the data in a batch request.
            }
        }
        /*
         * Need to go through the updated receivers to find any that need to be fetched (are stale)
         * and send the list of keys to the cache to perform the work.
         */
        final List<K> requestKeys = receivers.stream()
                                             .filter( dr -> dr.getCacheDataState().isStale() )
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
            this.getCache()
                .asynchronousGet( requestKeys );
        }
        logMethodEnd( methodName );
    }

}
