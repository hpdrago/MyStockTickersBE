package com.stocktracker.servicelayer.service.cache.common;

/**
 * This interface is implemented by all classes that contain a async cache entry state.
 */
public interface AsyncCacheEntryStateContainer
{
    /**
     * Set the async cache entry state.
     * @param asyncCacheEntryState
     */
    void setAsyncCacheEntryState( final AsyncCacheEntryState asyncCacheEntryState );

    /**
     * Get the async cache entry state.
     * @return
     */
    AsyncCacheEntryState getAsyncCacheEntryState();
}
