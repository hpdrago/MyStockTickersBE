package com.stocktracker.servicelayer.service.stockinformationprovider;

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
     * Get the ticker quote from the provider.
     * @param tickerSymbol
     * @return
     */
    StockTickerQuote getStockTickerQuote( final String tickerSymbol );
}

