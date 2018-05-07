package com.stocktracker.servicelayer.service.stocks;

import com.stocktracker.servicelayer.stockinformationprovider.StockPriceCacheState;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Defines the necessary methods to contain a stock quote and work with the StockQuoteCache
 */
public interface StockPriceContainer extends TickerSymbolContainer
{
    void setLastPrice( final BigDecimal stockPrice );
    BigDecimal getLastPrice();
    void setStockPriceCacheState( final StockPriceCacheState stockPriceCacheState );
    StockPriceCacheState getStockPriceCacheState();
    void setExpirationTime( final Timestamp expirationTime );
    Timestamp getExpirationTime();
}
