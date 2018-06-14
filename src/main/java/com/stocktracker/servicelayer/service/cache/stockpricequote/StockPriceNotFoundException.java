package com.stocktracker.servicelayer.service.cache.stockpricequote;

import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDataNotFoundException;

public class StockPriceNotFoundException extends AsyncCacheDataNotFoundException
{
    public StockPriceNotFoundException( final String tickerSymbol )
    {
        super( tickerSymbol + " was not found" );
    }

    public StockPriceNotFoundException( final String tickerSymbol, final Exception e )
    {
        super( tickerSymbol + " was not found", e );
    }
}
