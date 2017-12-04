package com.stocktracker.servicelayer.stockinformationprovider;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * This interface defines the methods to extract the values from a cached stock
 */
public interface StockQuote
{
    String getTickerSymbol();
    String getCompanyName();
    String getStockExchange();
    BigDecimal getLastPrice();
    Timestamp getLastPriceChange();
    StockQuoteState getStockQuoteState();
}
