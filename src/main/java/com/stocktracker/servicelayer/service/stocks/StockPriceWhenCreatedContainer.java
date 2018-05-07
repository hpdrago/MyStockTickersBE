package com.stocktracker.servicelayer.service.stocks;

import java.math.BigDecimal;

/**
 * This interface defines the methods for those class that have a stock price when created field.  This stock price
 * is common for a number of classes so that the amount the price has changed since the entity was created can be displayed.
 */
public interface StockPriceWhenCreatedContainer
{
    void setStockPriceWhenCreated( final BigDecimal stockPriceWhenCreated );
    BigDecimal getStockPriceWhenCreated();
}
