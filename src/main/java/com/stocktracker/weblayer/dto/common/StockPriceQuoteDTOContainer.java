package com.stocktracker.weblayer.dto.common;

import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState;
import com.stocktracker.servicelayer.service.stocks.TickerSymbolContainer;
import com.stocktracker.weblayer.dto.StockPriceQuoteDTO;

/**
 * Defines the necessary methods to contain a stock price and work with the StockPriceQuoteCache
 */
public interface StockPriceQuoteDTOContainer extends TickerSymbolContainer
{
    /**
     * Set the stock price quote.
     * @param stockPriceQuoteDTO
     */
    void setStockPriceQuote( final StockPriceQuoteDTO stockPriceQuoteDTO );

    /**
     * Get the stock price quote.
     * @return
     */
    StockPriceQuoteDTO getStockPriceQuote();
}
