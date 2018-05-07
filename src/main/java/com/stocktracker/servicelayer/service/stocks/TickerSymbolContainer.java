package com.stocktracker.servicelayer.service.stocks;

/**
 * Defines methods for those classes that contain a ticker symbol.
 */
public interface TickerSymbolContainer
{
    String getTickerSymbol();
    void setTickerSymbol( final String tickerSymbol );
}
