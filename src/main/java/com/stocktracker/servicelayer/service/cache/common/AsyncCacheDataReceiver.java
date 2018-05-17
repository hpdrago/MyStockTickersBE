package com.stocktracker.servicelayer.service.cache.common;

import java.io.Serializable;
import java.util.Date;

/**
 * This interface is implemented by classes, probably DTO's, that receive cached data from an AsynCache.
 * There are four pieces of information that a receiver "receives"
 * 1. Cached data state - This includes STALE, CURRENT, ERROR which identifies the state of the cached data.
 * 2. Cached Data - The data that is being cached.
 * 3. Error - A string value of any errors that occurred during the fetching of the data.
 * --4. Exception - Any exceptions that occurred during the fetching of the cached data.
 *
 * <EK> The entity key.
 *  <T> The entity type.
 */
public interface AsyncCacheDataReceiver<EK extends Serializable,T>
{
    /**
     * Get the entity key value.
     * @return
     */
    EK getEntityKey();

    /**
     * Set the cached data on the receiver.
     * @param cachedData When null, there's just a state change.
     */
    void setCachedData( final T cachedData );

    /**
     * Set the state of the cached data (STALE, CURRENT, ERROR).
     * @param cacheState
     */
    void setCacheDataState( final AsyncCacheEntryState cacheState );

    /**
     * Set the the error message if an exception occured while fetching.
     * @param error
     */
    void setCacheError( final String error );

    /**
     * Sets the date and time when the data will expire.
     * @param dataExpiration
     */
    void setDataExpiration( final Date dataExpiration );
}
