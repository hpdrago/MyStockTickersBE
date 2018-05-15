package com.stocktracker.servicelayer.service.cache.stockquote;

import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDataReceiver;
import com.stocktracker.servicelayer.service.stocks.TickerSymbolContainer;

/*
* *
 * This interface defines the methods for classes that will get Stock Quote information from the StockQuoteEntityCache
 */
public interface StockQuoteEntityContainer extends TickerSymbolContainer,
                                                   AsyncCacheDataReceiver<String,StockQuoteEntity>
{
    /**
     * This method is called to set the {@code StockQuoteEntity}.  Classes implementing this interface can then
     * extract any or all of the Stock Quote (IEXTrading Quote) properties as needed.
     * @param stockQuoteEntity
     */
    //void setStockQuoteEntity( final StockQuoteEntity stockQuoteEntity );

    /**
     * This method is called to set the state of the {@code StockQuoteEntity} received from the stock quote cache.
     * @param stockQuoteEntityCacheState
     */
    //void setStockQuoteCacheEntryState( final AsyncCacheEntryState stockQuoteEntityCacheState );

    /**
     * This method is called to set the error encoutered while retrieving the Quote from IEXTrading.
     * @param stockQuoteCacheError
     */
    //void setStockQuoteCacheError( final String stockQuoteCacheError );

}
