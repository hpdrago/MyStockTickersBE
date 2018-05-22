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
}
