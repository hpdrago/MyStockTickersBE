package com.stocktracker.servicelayer.stockinformationprovider;

import com.stocktracker.common.exceptions.StockNotFoundException;

import java.math.BigDecimal;

/**
 * Defines the methods required for a stock quote provider.
 */
public interface StockQuoteServiceProvider
{
    /**
     * Identifies the provider of the service
     * @return
     */
    String getProviderName();

    /**
     * Get the stock price.
     * @param tickerSymbol
     * @return
     */
    BigDecimal getStockPrice( final String tickerSymbol )
        throws StockNotFoundException;
}

