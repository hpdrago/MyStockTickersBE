package com.stocktracker.servicelayer.service.cache.common;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * This interface defines the methods for DTO's receiving AsyncCache information.
 * @param <T> Type of cached data.
 */
public interface AsyncCachedDataContainer<K extends Serializable,T>
{
    /**
     * Get the key to the cache.
     * @return
     */
    K getCacheKey();

    /**
     * Set the cache key.
     * @param cacheKey
     */
    void setCacheKey( K cacheKey );

    /**
     * This method is called to set the cached {@code cachedData}.
     * @param cachedData
     */
    void setCachedData( final T cachedData );

    /**
     * Get the cached data.
     * @return
     */
    T getCachedData();

    /**
     * This method is called to set the state of the {@code StockQuoteDTO} received from the stock quote cache.
     * @param stockQuoteEntityCacheState
     */
    void setCachedDataState( final AsyncCacheEntryState stockQuoteEntityCacheState );

    /**
     * Get the cache entry state.
     * @return
     */
    AsyncCacheEntryState getCacheDataState();

    /**
     * This method is called to set the error encoutered while retrieving the Quote from IEXTrading.
     * @param stockQuoteCacheError
     */
    void setCacheError( final String stockQuoteCacheError );

    /**
     * Set the cache error;
     * @return
     */
    String getCacheError();

    /**
     * Set the expiration time.
     * @param expirationTime
     */
    void setExpirationTime( final Timestamp expirationTime );

    /**
     * Get the expiration time.
     * @return
     */
    Date getExpirationTime();
}
