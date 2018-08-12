package com.stocktracker.servicelayer.service.cache.common;

import io.reactivex.processors.AsyncProcessor;

import javax.validation.constraints.NotNull;

/**
 * This interface defines the methods for the information executor that provides two methods to obtain the requested
 * information, one for synchronous fetching, and one for asynchronous fetching of the information.
 *
 * @param <CK>  - The cache key.
 * @param <CD>  - The cache data type.
 * @param <TPK> - The key used to query the information from the third party.
 * @param <TPD> - The type of information to obtain from the third party.
 */
public interface AsyncCacheServiceExecutor<CK,CD,TPK,TPD>
{
    /**
     * Fetches the information for {@code searchKey} immediately.
     * @param cacheKey The search key.
     * @param thirdPartyKey
     * @return Information of type TPD wrapped in an Optional.
     */
    TPD getThirdPartyData( @NotNull final CK cacheKey,
                           @NotNull final TPK thirdPartyKey )
        throws AsyncCacheDataNotFoundException;

    /**
     * Converts the third party data into the cached data instance.
     * @param thirdPartyKey
     * @param thirdPartyData
     * @return Cached data.
     */
    CD convertThirdPartyData( @NotNull final TPK thirdPartyKey,
                              @NotNull final TPD thirdPartyData );

    /**
     * Asynchronous fetching of the information for {@code thirdPartyKey}.
     * @param cacheKey
     * @param thirdPartyKey
     * @return Observable of result.
     */
    void asynchronousFetch( @NotNull final CK cacheKey,
                            @NotNull final TPK thirdPartyKey,
                            @NotNull final AsyncProcessor<CD> observable );
}
