package com.stocktracker.weblayer.dto.common;

import com.stocktracker.servicelayer.service.stocks.TickerSymbolContainer;
import com.stocktracker.weblayer.dto.StockPriceQuoteDTO;

/**
 * This interface is implemented by DTO's that contain a cached {@code StockPriceQuoteDTO} instance.
 */
public interface StockPriceQuoteDTOAsyncContainer extends AsyncCacheDTOContainer<String,StockPriceQuoteDTO>,
                                                          TickerSymbolContainer
{
}
