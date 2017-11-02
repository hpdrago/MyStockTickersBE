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
     * Gets a stock quote for {@code tickerSymbol}
     * @param tickerSymbol
     * @return
     */
    StockTickerQuote getStockQuote( final String tickerSymbol );
}

