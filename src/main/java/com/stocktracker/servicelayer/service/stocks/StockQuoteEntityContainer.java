package com.stocktracker.servicelayer.service.stocks;

import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import com.stocktracker.servicelayer.service.cache.common.InformationCacheEntryState;

/**
 * This interface defines the methods necessary for a class to receive StockQuoteEntity information from the
 * StockQuoteEntityCache.
 */
public interface StockQuoteEntityContainer
{
    /**
     * This method is called to set the {@code StockQuoteEntity}.  Classes implementing this interface can then
     * extract any or all of the Stock Quote (IEXTrading Quote) properties as needed.  The {@code stockQuoteEntityCacheState}
     * will need to be checked to see the state of {@code stockQuoteEntity} as the entity may be being fetch in the
     * background from IEXTrading and thus the values may not be set.
     * @param stockQuoteEntityCacheState State of the {@code stockQuoteEntity}
     * @param stockQuoteEntity
     */
    void setStockQuoteEntity( final InformationCacheEntryState stockQuoteEntityCacheState,
                              final StockQuoteEntity stockQuoteEntity );
}
