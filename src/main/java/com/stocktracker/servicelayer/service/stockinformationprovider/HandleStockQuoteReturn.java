package com.stocktracker.servicelayer.service.stockinformationprovider;

/**
 * This interface defines the method that will be called by the {@code StockQuoteServiceExecutor} after the
 * stock quote has been retrieved from the stock quote service.
 */
public interface HandleStockQuoteReturn
{
    void handleStockQuoteReturn( final StockTickerQuote stockTickerQuote );
}
