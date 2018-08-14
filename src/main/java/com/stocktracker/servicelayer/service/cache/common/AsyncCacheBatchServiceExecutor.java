package com.stocktracker.servicelayer.service.cache.common;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Extends the {@code AsyncCacheServiceExecutor} to add batch updates for multiple keys.
 *
 * @param <CK> - The key type to the cache
 * @param <CD> - The cached data type.
 * @param <ASK> - The async key type.
 * @param <ASD> - The async data type.
 * @param <RQ> - The Async Request Type.
 * @param <RS> - The Async Response Type.
 */
public interface AsyncCacheBatchServiceExecutor<CK,
                                                CD,
                                                ASK,
                                                ASD,
                                                RQ extends AsyncBatchCacheRequest<CK,CD,ASK>,
                                                RS extends AsyncBatchCacheResponse<CK,ASK,ASD>>
    extends AsyncCacheServiceExecutor<ASK,ASD>
{
    /**
     * Asynchronous fetching of the information for the keys contained in {@code requests}.
     * NOTE: subclasses should use the @Async annotation on the method so that it is executed asynchronously.
     * @param requests Contains a list of {@code AsyncBatchCacheRequest} instances each of which contains the request
     *                 key and the RxJava async processor instance.
     */
    void asynchronousFetch( @NotNull final List<RQ> requests );

    /**
     * Fetches the information for all of the keys in {@code searchKeys} as a batch.
     * @param requests Contains the information to make the batch request.
     * @return List of results.
     */
    List<RS> synchronousFetch( @NotNull final List<RQ> requests );
}
