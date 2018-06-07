package com.stocktracker.servicelayer.service.cache.common;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This is the base service executor for making batch requests to third parties.
 *
 * @param <K> The type of the key.
 * @param <T> The type of the data to be retrieved.
 * @param <RQ> - The Async Request Type.
 * @param <RS> - The Async Response Type.
 */
public abstract class BaseAsyncCacheBatchServiceExecutor<K,
                                                         T,
                                                         RQ extends AsyncBatchCacheRequest<K,T>,
                                                         RS extends AsyncBatchCacheResponse<K,T>>
    extends BaseAsyncCacheServiceExecutor<K,T>
    implements AsyncCacheBatchServiceExecutor<K,T,RQ,RS>
{
    /**
     * Asynchronous fetching of the information for {@code searchKey}.
     * Subclasses must override this method and add the @Async annotation so that this method executes on a separate
     * thread.
     * @param requests Contains a list of {@code AsyncBatchCacheRequest} instances each of which contains the request
     *                 key and the RxJava async processor instance.
     */
    public void asynchronousFetch( @NotNull final Map<K,RQ> requests )
    {
        final String methodName = "asynchronousFetch";
        logMethodBegin( methodName, requests.size() );
        /*
         * Execute the batch request.
         */
        final List<RS> results = this.synchronousFetch( requests );
        /*
         * Need to ensure that we have the same number of results as requests or something went wrong.
         */
        if ( requests.size() != results.size() )
        {
            throw new IllegalArgumentException( String.format( "Request (%d) and results (%d) sizes do not match.",
                                                               requests.size(), results.size() ));
        }

        /*
         * Cycle through the results, find the associated request entry, and make the results notification through
         * the request's AsyncProcessor.
         */
        for ( final AsyncBatchCacheResponse<K,T> result: results )
        {
            final AsyncBatchCacheRequest<K,T> request = requests.get( result.getCacheKey() );
            Objects.requireNonNull( request, "Request not found for cache key: " + result.getCacheKey() );
            if ( result.getException() == null )
            {
                request.getAsyncProcessor()
                       .onNext( result.getData() );
                request.getAsyncProcessor()
                       .onComplete();
            }
            else
            {
                request.getAsyncProcessor()
                       .onError( result.getException() );
            }
        }
        logMethodEnd( methodName );
    }
}
