package com.stocktracker.servicelayer.service.cache.common;

import io.reactivex.processors.AsyncProcessor;

import javax.validation.constraints.NotNull;

/**
 * This interface defines the methods for the information executor that provides two methods to obtain the requested
 * information, one for synchronous fetching, and one for asynchronous fetching of the information.
 *
 * The service executor is responsible for obtaining the asynchronous data.
 *
 * @param <ASK> - The key used to query the information form the async source data source.
 * @param <ASD> - The async data type.
 */
public interface AsyncCacheServiceExecutor<ASK,ASD>
{
    /**
     * Fetches the information for {@code asyncKey} immediately.
     * @param asyncKey The key to the asynchronous data.
     * @return Information of type ASD wrapped in an Optional.
     */
    ASD getASyncData( @NotNull final ASK asyncKey )
        throws AsyncCacheDataNotFoundException, AsyncCacheDataRequestException;

    /**
     * Asynchronous fetching of the information for {@code asyncKey}.
     * Implements should add the @Async annotation so that that method gets called on a separate thread.
     * @param asyncKey
     * @return Observable of result.
     */
    void asynchronousFetch( @NotNull final ASK asyncKey,
                            @NotNull final AsyncProcessor<ASD> observable );
}
