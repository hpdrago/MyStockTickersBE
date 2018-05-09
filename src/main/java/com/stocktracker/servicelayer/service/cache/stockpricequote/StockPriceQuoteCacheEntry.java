package com.stocktracker.servicelayer.service.cache.stockpricequote;

import com.stocktracker.servicelayer.service.cache.common.InformationCacheEntry;

import java.util.concurrent.TimeUnit;

/**
 * This class defines the Stock Price Quote cache's entry types.
 */
public class StockPriceQuoteCacheEntry extends InformationCacheEntry<StockPriceQuote>
{
    /**
     * Identifies if the stock company table's existence has already been checked.
     */
    private boolean stockTableEntryValidated;
    /**
     * Identifies if the stock is no longer used.
     */
    private boolean discontinued;

    /**
     * Stock quotes are valid for 15 minutes.
     * @return
     */
    @Override
    protected long getCurrentDurationTime()
    {
        return TimeUnit.MINUTES.convert( 15, TimeUnit.MILLISECONDS );
    }
}
