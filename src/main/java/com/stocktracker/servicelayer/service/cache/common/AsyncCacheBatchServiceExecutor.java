package com.stocktracker.servicelayer.service.cache.common;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Extends the {@code AsyncCacheServiceExecutor} to add batch updates for multiple keys.
 *
 * @param <K> - The key type to the cache -- this is key used to query the information from the third party.
 * @param <T> - The type of information to obtain from the third party.
 * @param <RQ> - The Async Request Type.
 * @param <RS> - The Async Response Type.
 */
public interface AsyncCacheBatchServiceExecutor<K,
                                                T,
                                                RQ extends AsyncBatchCacheRequest<K,T>,
                                                RS extends AsyncBatchCacheResponse<K,T>>
    extends AsyncCacheServiceExecutor<K,T>
{
    /**
     * Asynchronous fetching of the information for the keys contained in {@code requests}.
     * NOTE: subclasses should use the @Async annotation on the method so that it is executed asynchronously.
     * @param requests Contains a list of {@code AsyncBatchCacheRequest} instances each of which contains the request
     *                 key and the RxJava async processor instance.
     */
    void asynchronousFetch( @NotNull final Map<K,RQ> requests );

    /**
     * Fetches the information for all of the keys in {@code searchKeys} as a batch.
     * @param requests Contains the information to make the batch request.
     * @return List of results.
     */
    List<RS> synchronousFetch( @NotNull final Map<K,RQ> requests );
}
