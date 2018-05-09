package com.stocktracker.servicelayer.service.cache.stockpricequote;

import com.stocktracker.servicelayer.service.cache.common.InformationCacheEntryState;
import com.stocktracker.servicelayer.service.stocks.StockPriceContainer;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * This interface contains the methods for the {@code StockPriceQuoteEntity} class to enable other class to implement this
 * interface and enable the easy copy of stock quote information from the quote to a DTO containing quote information.
 */
public interface StockPriceQuoteContainer extends StockPriceContainer
{
    InformationCacheEntryState getStockPriceCacheState();

    void setStockPriceCacheState( InformationCacheEntryState informationCacheEntryState );

    String getTickerSymbol();

    void setTickerSymbol( String tickerSymbol );

    BigDecimal getLastPrice();

    void setLastPrice( BigDecimal lastPrice );

    Timestamp getLastPriceChange();

    void setLastPriceChange( Timestamp lastPriceChange );

    void setOpenPrice( BigDecimal openPrice );

    BigDecimal getOpenPrice();

    String getCompanyName();

    void setCompanyName( String companyName );

    String getSector();

    void setSector( String sector );

    String getIndustry();

    void setIndustry( String industry );

    Timestamp getExpirationTime();

    void setExpirationTime( Timestamp expirationTime );

    void setStockPriceQuote( InformationCacheEntryState cacheState, StockPriceQuote information );
}
