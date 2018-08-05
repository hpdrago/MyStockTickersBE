package com.stocktracker.servicelayer.service.cache.common;

/**
 * This enum defines how this class will behave when a stock quote is not found in the cache or found in the cache
 * but is stale (CachedStockEntry.lastQuoteRefreshTime > EXPIRATION_TIME)
 */
public enum AsyncCacheFetchMode
{
    SYNCHRONOUS,
    ASYNCHRONOUS;

    /**
     * Is Synchronous
     * @return
     */
    public boolean isSynchronous()
    {
        return this == SYNCHRONOUS;
    }

    /**
     * Is Asynchronous.
     * @return
     */
    public boolean isAsynchronous()
    {
        return this == ASYNCHRONOUS;
    }
}

