package com.stocktracker.servicelayer.service.cache.common;

import com.stocktracker.servicelayer.service.BaseService;
import io.reactivex.processors.BehaviorProcessor;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * Base class for all Cache Service Executors.
 *
 * @param <T> - The type of information to obtain from the third party.
 * @param <K> - The key type to the cache -- this is key used to query the information from the third party.
 */
public abstract class AsyncCacheBaseCacheServiceExecutor<K,T> extends BaseService
    implements AsyncCacheServiceExecutor<K,T>
{
    /**
     * Asynchronous fetching of the information for {@code searchKey}.
     * The subject is notified once the fetch has completed.
     *
     * Subclasses should override this method and declare it as @Async so that a call to this method results
     * in a new thread being created.
     *
     * @param searchKey
     */
    public void asynchronousFetch( @NotNull final K searchKey, @NotNull final BehaviorProcessor<Optional<T>> subject )
    {
        final String methodName = "asynchronousFetch";
        logMethodBegin( methodName, searchKey );
        final Optional<T> fetchResult = this.synchronousFetch( searchKey );
        subject.onNext( fetchResult );
        subject.onComplete();
        logMethodEnd( methodName, searchKey );
    }
}
