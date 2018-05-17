package com.stocktracker.weblayer.dto.common;

import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState;

import java.sql.Timestamp;
import java.util.Date;

/**
 * This interface defines the methods for DTO's receiving AsyncCache information.
 * @param <D>
 */
public interface AsyncCacheDTOContainer<D>
{
    /**
     * This method is called to set the cached {@code dto}.
     * @param dto
     */
    void setCachedDTO( final D dto );

    /**
     * This method is called to set the state of the {@code StockQuoteDTO} received from the stock quote cache.
     * @param stockQuoteEntityCacheState
     */
    void setCacheState( final AsyncCacheEntryState stockQuoteEntityCacheState );

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
