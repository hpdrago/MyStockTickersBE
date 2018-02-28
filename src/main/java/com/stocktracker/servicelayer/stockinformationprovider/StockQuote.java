package com.stocktracker.servicelayer.stockinformationprovider;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * This interface defines the properties of a class that contains stock quote properties.
 */
public interface StockQuote
{
    String getTickerSymbol();
    String getCompanyName();
    String getStockExchange();
    BigDecimal getOpenPrice();
    BigDecimal getLastPrice();
    Timestamp getLastPriceChange();
    StockQuoteState getStockQuoteState();
    Timestamp getExpiration();
}
