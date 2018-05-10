package com.stocktracker.servicelayer.service.cache.common;

/**
 * This enum identifies the different states of the cache price stock entry.
 */
public enum AsyncCacheFetchState
{
    /**
     * The Stock Price is currently begin fetched.
     */
    FETCHING,
    /**
     * The stock price is not being fetched.
     */
    NOT_FETCHING;

    /**
     * Identifies if the stock price is currently being fetched.
     * @return
     */
    public boolean isFetching() { return this == FETCHING; }
}
