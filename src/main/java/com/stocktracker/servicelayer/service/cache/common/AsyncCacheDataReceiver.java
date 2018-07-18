package com.stocktracker.servicelayer.service.cache.common;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * This interface is implemented by classes, probably DTO's, that receive cached data from an AsynCache.
 * There are four pieces of information that a receiver "receives"
 * 1. Cached data state - This includes STALE, CURRENT, ERROR which identifies the state of the cached data.
 * 2. Cached Data - The data that is being cached.
 * 3. Error - A string value of any errors that occurred during the fetching of the data.
 * --4. Exception - Any exceptions that occurred during the fetching of the cached data.
 *
 * <K> The cache key type.
 * <T> The cached data type.
 */
public interface AsyncCacheDataReceiver<K extends Serializable,T>
{
    /**
     * Set the cache key.
     * @param cacheKey
     */
    void setCacheKey( final K cacheKey );

    /**
     * Get the cache key value.
     * @return
     */
    K getCacheKey();

    /**
     * Set the cached data on the receiver.
     * @param cachedData When null, there's just a state change.
     */
    void setCachedData( final T cachedData );

    /**
     * Get the cached data that was set by the cache.
     * @return
     */
    T getCachedData();

    /**
     * Set the state of the cached data (STALE, CURRENT, ERROR).
     * @param cacheState
     */
    void setCacheState( final AsyncCacheEntryState cacheState );

    /**
     * Get the state of the cached data.
     * @return
     */
    AsyncCacheEntryState getCacheState();

    /**
     * Set the the error message if an exception occured while fetching.
     * @param error
     */
    void setCacheError( final String error );

    /**
     * Get the error message for exceptions encoutered in the cache.
     * @return
     */
    String getCacheError();

    /**
     * Sets the date and time when the data will expire.
     * @param dataExpiration
     */
    void setExpirationTime( final Timestamp dataExpiration );

    /**
     * Get the expiration date.
     * @return
     */
    Timestamp getExpirationTime();
}
