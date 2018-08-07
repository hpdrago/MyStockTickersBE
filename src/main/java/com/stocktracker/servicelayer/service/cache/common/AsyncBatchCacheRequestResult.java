package com.stocktracker.servicelayer.service.cache.common;

/**
 * Result status of a batch request.
 */
public enum AsyncBatchCacheRequestResult
{
    FOUND,
    NOT_FOUND,
    ERROR;

    public boolean isFound() { return this == FOUND; }
    public boolean isNotFound() { return this == NOT_FOUND; }
    public boolean isError() { return this == ERROR; }
}
