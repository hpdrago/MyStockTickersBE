package com.stocktracker.weblayer.dto.common;

import com.stocktracker.servicelayer.service.cache.common.AsyncCachedDataContainer;
import com.stocktracker.servicelayer.service.stocks.TickerSymbolContainer;
import com.stocktracker.weblayer.dto.StockQuoteDTO;

/**
 * This interface is implemented by DTO's that contain a cached {@code StockQuoteDTO} instance.
 */
public interface StockQuoteDTOAsyncContainer extends AsyncCachedDataContainer<String,StockQuoteDTO>,
                                                     TickerSymbolContainer
{
}
