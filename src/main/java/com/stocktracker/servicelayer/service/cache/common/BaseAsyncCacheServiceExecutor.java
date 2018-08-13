package com.stocktracker.servicelayer.service.cache.common;

import com.stocktracker.servicelayer.service.BaseService;
import io.reactivex.processors.AsyncProcessor;

import javax.validation.constraints.NotNull;

/**
 * Base class for all Cache Service Executors.
 *
 * @param <CK> - The key used to get the cache entry.
 * @param <CD> - The cache data type.
 * @param <ASK> - The async key used to fetch the async data.
 */
public abstract class BaseAsyncCacheServiceExecutor<CK,CD,ASK> extends BaseService
    implements AsyncCacheServiceExecutor<CK,CD,ASK>
{
    /**
     * Asynchronous fetching of the information for {@code searchKey}.
     * The subject is notified once the fetch has completed.
     *
     * Subclasses should override this method and declare it as @Async so that a call to this method results
     * in a new thread being created.
     *
     * @param cacheKey
     * @throws AsyncCacheDataRequestException When an issue is encountered with the asynchronous data source.
     */
    public void asynchronousFetch( @NotNull final CK cacheKey,
                                   @NotNull final ASK asyncKey,
                                   @NotNull final AsyncProcessor<CD> subject )
        throws AsyncCacheDataRequestException
    {
        final String methodName = "asynchronousFetch";
        logMethodBegin( methodName, cacheKey );
        final CD asyncData;
        try
        {
            asyncData = this.getASyncData( cacheKey, asyncKey );
            logTrace( methodName, "fetchResult: {0}", asyncData );
            logTrace( methodName, "onNext();onComplete();" );
            subject.onNext( asyncData );
            subject.onComplete();
        }
        catch( AsyncCacheDataNotFoundException asyncCacheDataNotFoundException )
        {
            subject.onError( asyncCacheDataNotFoundException );
            logTrace( methodName, "onError();" );
        }
        logMethodEnd( methodName, cacheKey );
    }
}
