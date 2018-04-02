package com.stocktracker.servicelayer.stockinformationprovider;

import java.util.List;

/**
 * This interface defines the method that will be called by the {@code StockQuoteServiceExecutor} after the
 * stock quote has been retrieved from the stock quote service.
 */
public interface HandleStockQuoteResult
{
    void handleGetStockPriceResult( final GetStockPriceResult getStockPriceResult );
    void handleGetStockPriceResults( final List<GetStockPriceResult> getStockPriceResults );
}
