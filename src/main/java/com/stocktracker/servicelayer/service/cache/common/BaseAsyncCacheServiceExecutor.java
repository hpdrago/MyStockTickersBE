package com.stocktracker.servicelayer.service.cache.common;

import com.stocktracker.servicelayer.service.BaseService;
import io.reactivex.processors.AsyncProcessor;

import javax.validation.constraints.NotNull;

/**
 * Base class for all Cache Service Executors.
 *
 * @param <ASK> - The async key used to fetch the async data.
 * @param <ASD> - The async data type.
 *
 */
public abstract class BaseAsyncCacheServiceExecutor<ASK,ASD> extends BaseService
    implements AsyncCacheServiceExecutor<ASK,ASD>
{
    /**
     * Asynchronous fetching of the information for {@code searchKey}.
     * The subject is notified once the fetch has completed.
     *
     * Subclasses should override this method and declare it as @Async so that a call to this method results
     * in a new thread being created.
     *
     * @param asyncKey The async data key.
     * @param subject Async processor to notify the call when the async process has completed.
     * @throws AsyncCacheDataRequestException When an issue is encountered with the asynchronous data source.
     */
    public void asynchronousFetch( @NotNull final ASK asyncKey,
                                   @NotNull final AsyncProcessor<ASD> subject )
    {
        final String methodName = "asynchronousFetch";
        logMethodBegin( methodName, asyncKey );
        final ASD asyncData;
        try
        {
            asyncData = this.getASyncData( asyncKey );
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
        catch( AsyncCacheDataRequestException asyncCacheDataRequestException )
        {
            subject.onError( asyncCacheDataRequestException );
            logTrace( methodName, "onError();" );
        }
        logMethodEnd( methodName, asyncKey );
    }
}
