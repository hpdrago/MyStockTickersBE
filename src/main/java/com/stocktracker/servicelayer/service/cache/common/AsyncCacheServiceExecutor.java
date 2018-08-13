package com.stocktracker.servicelayer.service.cache.common;

import io.reactivex.processors.AsyncProcessor;

import javax.validation.constraints.NotNull;

/**
 * This interface defines the methods for the information executor that provides two methods to obtain the requested
 * information, one for synchronous fetching, and one for asynchronous fetching of the information.
 *
 * @param <CK>  - The cache key.
 * @param <CD>  - The cache data type.
 * @param <ASK> - The key used to query the information form the async source data source.
 */
public interface AsyncCacheServiceExecutor<CK,CD,ASK>
{
    /**
     * Fetches the information for {@code asyncKey} immediately.
     * @param cacheKey The key to the cache.
     * @param asyncKey The key to the asynchronous data.
     * @return Information of type ASD wrapped in an Optional.
     */
    CD getASyncData( @NotNull final CK cacheKey,
                     @NotNull final ASK asyncKey )
        throws AsyncCacheDataNotFoundException, AsyncCacheDataRequestException;

    /**
     * Asynchronous fetching of the information for {@code asyncKey}.
     * @param cacheKey
     * @param asyncKey
     * @return Observable of result.
     */
    void asynchronousFetch( @NotNull final CK cacheKey,
                            @NotNull final ASK asyncKey,
                            @NotNull final AsyncProcessor<CD> observable )
        throws AsyncCacheDataRequestException;
}
