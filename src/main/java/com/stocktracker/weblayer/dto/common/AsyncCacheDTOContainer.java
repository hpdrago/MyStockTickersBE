package com.stocktracker.weblayer.dto.common;

import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState;

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

    String getCacheError();
}
