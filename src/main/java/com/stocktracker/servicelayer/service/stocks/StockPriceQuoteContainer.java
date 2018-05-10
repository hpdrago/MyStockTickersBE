package com.stocktracker.servicelayer.service.stocks;

import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState;
import com.stocktracker.servicelayer.service.cache.stockpricequote.StockPriceQuote;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Defines the necessary methods to contain a stock price and work with the StockPriceQuoteCache
 */
public interface StockPriceQuoteContainer extends TickerSymbolContainer
{
    void setLastPrice( final BigDecimal stockPrice );
    BigDecimal getLastPrice();
    void setStockPriceQuoteCacheState( final AsyncCacheEntryState asyncCacheEntryState );
    AsyncCacheEntryState getStockPriceQuoteCacheState();
    void setExpirationTime( final Timestamp expirationTime );
    Timestamp getExpirationTime();

    /**
     * This method is called by the {@code StockPriceQuoteCache} to provide the current stock price quote information.
     * @param stockPriceQuoteState Identifies the state of the {@!code stockPriceQuote} whether it's current, stale, error
     * @param stockPriceQuote The current stock price quote information.
     */
    void setStockPriceQuote( AsyncCacheEntryState stockPriceQuoteState, StockPriceQuote stockPriceQuote );
}
