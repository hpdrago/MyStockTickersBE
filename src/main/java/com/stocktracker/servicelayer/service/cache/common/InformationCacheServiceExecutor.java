package com.stocktracker.servicelayer.service.cache.common;

import io.reactivex.processors.BehaviorProcessor;
import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * This interface defines the methods for the information executor that provides two methods to obtain the requested
 * information, one for synchronous fetching, and one for asynchronous fetching of the information.
 *
 * @param <T> - The type of information to obtain from the third party.
 * @param <K> - The key type to the cache -- this is key used to query the information from the third party.
 */
public interface InformationCacheServiceExecutor<K,T>
{
    /**
     * Fetches the information for {@code searchKey} immediately.
     * @param searchKey The search key.
     * @return Information of type T wrapped in an Optional.
     */
    Optional<T> synchronousFetch( @NotNull final K searchKey );

    /**
     * Asynchronous fetching of the information for {@code searchKey}.
     * @param searchKey
     * @return Observable of result.
     */
    void asynchronousFetch( @NotNull final K searchKey, @NotNull final BehaviorProcessor<Optional<T>> observable );
}
