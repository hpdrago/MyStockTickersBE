package com.stocktracker.weblayer.dto.common;

import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDTOContainer;
import com.stocktracker.servicelayer.service.stocks.TickerSymbolContainer;
import com.stocktracker.weblayer.dto.StockPriceQuoteDTO;
import com.stocktracker.weblayer.dto.StockQuoteDTO;

/**
 * Defines the necessary methods to contain a stock price and work with the StockPriceQuoteCache
 */
public interface StockPriceQuoteDTOContainer extends TickerSymbolContainer,
                                                     AsyncCacheDTOContainer<String,StockQuoteDTO>

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
