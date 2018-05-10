package com.stocktracker.servicelayer.service.cache.stockquote;

import com.stocktracker.repositorylayer.common.VersionedEntity;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState;
import com.stocktracker.servicelayer.service.cache.stockpricequote.StockQuoteContainer;
import com.stocktracker.servicelayer.service.stocks.TickerSymbolContainer;
import yahoofinance.quotes.stock.StockQuote;

import java.io.Serializable;

/**
 * This interface is used by any class that need information from the {@code StockQuoteEntityCache}.
 * @param <K>
 */
public interface StockQuoteEntityCacheClient<K extends Serializable> extends StockQuoteContainer,
                                                                             VersionedEntity<K>,
                                                                             TickerSymbolContainer
{
    AsyncCacheEntryState getStockQuoteCacheState();
    void setStockQuoteCacheState( AsyncCacheEntryState stockQuoteCacheState );

    void setStockQuote( final StockQuote stockQuote );

    /**
     * This method is called to set the {@code StockQuoteEntity}.  Classes implementing this interface can then
     * extract any or all of the Stock Quote (IEXTrading Quote) properties as needed.  The {@code stockQuoteEntityCacheState}
     * will need to be checked to see the state of {@code stockQuoteEntity} as the entity may be being fetch in the
     * background from IEXTrading and thus the values may not be set.
     * @param stockQuoteEntityCacheState State of the {@code stockQuoteEntity}
     * @param stockQuoteEntity
     */
    /*
    void setStockQuoteEntity( final AsyncCacheEntryState stockQuoteEntityCacheState,
                              final StockQuoteEntity stockQuoteEntity );
                              */

    void setError( final String error );
    String getError();
}
