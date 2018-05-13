package com.stocktracker.servicelayer.service.cache.common;

/**
 * This enum identifies what to do with the asynchronous data that was retrieved.
 */
public enum AsyncCacheStrategy
{
    /**
     * Fetch data will kept in the cache.
     */
    KEEP,

    /**
     * Cached entries will be removed after they have been retrieved and all observers have been sent the fetched data.
     * This is used for database entity caches where the cache entry only exists as a placeholder waiting for the
     * subsequent retrieval request.
     */
    REMOVE;

    public boolean isKeep() { return this == KEEP; }
    public boolean isRemove() { return this == REMOVE; }
}
