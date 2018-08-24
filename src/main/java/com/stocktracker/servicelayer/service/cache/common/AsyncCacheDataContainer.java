package com.stocktracker.servicelayer.service.cache.common;

import java.sql.Timestamp;
import java.util.Date;

/**
 * This interface defines the methods for DTO's receiving AsyncCache information.
 * @param <CD> Type of cached data.
 */
public interface AsyncCacheDataContainer<CK,CD,ASK>
{
    /**
     * Get the key to the cache.
     * @return
     */
    ASK getASyncKey();

    /**
     * Get the key to the cache.
     * @return
     */
    CK getCacheKey();

    /**
     * Set the cache key.
     * @param cacheKey
     */
    void setCacheKey( CK cacheKey );

    /**
     * This method is called to set the cached {@code cachedData}.
     * @param cachedData
     */
    void setCachedData( final CD cachedData );

    /**
     * Get the cached data.
     * @return
     */
    CD getCachedData();

    /**
     * This method is called to set the state of the {@code StockQuoteDTO} received from the stock quote cache.
     * @param stockQuoteEntityCacheState
     */
    void setCacheState( final AsyncCacheEntryState stockQuoteEntityCacheState );

    /**
     * Get the cache entry state.
     * @return
     */
    AsyncCacheEntryState getCacheState();

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
