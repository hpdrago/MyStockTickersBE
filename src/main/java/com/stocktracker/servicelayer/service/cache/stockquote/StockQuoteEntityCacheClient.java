package com.stocktracker.servicelayer.service.cache.stockquote;

import com.stocktracker.repositorylayer.common.VersionedEntity;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState;
import com.stocktracker.servicelayer.service.cache.stockpricequote.StockQuoteContainer;
import com.stocktracker.servicelayer.service.stocks.TickerSymbolContainer;

import java.io.Serializable;

/**
 * This interface is used by any class that need information from the {@code StockQuoteEntityCache}.
 * @param <K>
 */
public interface StockQuoteEntityCacheClient<K extends Serializable> extends StockQuoteContainer,
                                                                             VersionedEntity<K>,
                                                                             TickerSymbolContainer
{
    /**
     * Get the cache state.
     * @return
     */
    AsyncCacheEntryState getStockQuoteCacheState();

    /**
     * Set the the error message if an exception occured while fetching.
     * @param error
     */
    void setError( final String error );

    /**
     * Get the error message
     * @return
     */
    String getError();
}
