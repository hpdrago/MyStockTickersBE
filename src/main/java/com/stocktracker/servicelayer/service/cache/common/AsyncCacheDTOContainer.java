package com.stocktracker.servicelayer.service.cache.common;

import java.io.Serializable;

/**
 * Interface that defines the methods for DTO containers.
 * DTOs are converted from the cached data into DTOs and sent back to the caller.
 * @param <K>
 * @param <D>
 */
public interface AsyncCacheDTOContainer<K extends Serializable,D>
{
    /**
     * Get the cache key.
     * @return
     */
    K getCacheKey();

    /**
     * Set the cache key.
     * @param tickerSymbol
     */
    void setCacheKey( String tickerSymbol );
}
