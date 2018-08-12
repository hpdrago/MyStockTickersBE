package com.stocktracker.servicelayer.service.cache.common;

import com.stocktracker.servicelayer.service.BaseService;
import io.reactivex.processors.AsyncProcessor;

import javax.validation.constraints.NotNull;

/**
 * Base class for all Cache Service Executors.
 *
 * @param <CK> - The key used to get the cache entry.
 * @param <TPK> - The third party key used to fetch the third party data.
 * @param <TPD> - The type of information to obtain from the third party.
 */
public abstract class BaseAsyncCacheServiceExecutor<CK,CD,TPK,TPD> extends BaseService
    implements AsyncCacheServiceExecutor<CK,CD,TPK,TPD>
{
    /**
     * Asynchronous fetching of the information for {@code searchKey}.
     * The subject is notified once the fetch has completed.
     *
     * Subclasses should override this method and declare it as @Async so that a call to this method results
     * in a new thread being created.
     *
     * @param cacheKey
     */
    public void asynchronousFetch( @NotNull final CK cacheKey,
                                   @NotNull final TPK thirdPartyKey,
                                   @NotNull final AsyncProcessor<CD> subject )
    {
        final String methodName = "asynchronousFetch";
        logMethodBegin( methodName, cacheKey );
        final TPD thirdPartyData;
        try
        {
            thirdPartyData = this.getThirdPartyData( cacheKey, thirdPartyKey );
            logTrace( methodName, "fetchResult: {0}", thirdPartyData );
            CD cacheData = this.convertThirdPartyData( thirdPartyKey, thirdPartyData );
            logTrace( methodName, "onNext();onComplete();" );
            subject.onNext( cacheData );
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
