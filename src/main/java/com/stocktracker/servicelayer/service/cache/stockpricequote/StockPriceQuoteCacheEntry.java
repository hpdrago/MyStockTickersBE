package com.stocktracker.servicelayer.service.cache.stockpricequote;

import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntry;

/**
 * This class defines the Stock Price Quote cache's entry types.
 */
public class StockPriceQuoteCacheEntry extends AsyncCacheEntry<String,StockPriceQuote,String>
{
    /**
     * Identifies if the stock company table's existence has already been checked.
     */
    private boolean stockTableEntryValidated;
    /**
     * Identifies if the stock is no longer used.
     */
    private boolean discontinued;

    public boolean isStockTableEntryValidated()
    {
        return stockTableEntryValidated;
    }

    public void setStockTableEntryValidated( final boolean stockTableEntryValidated )
    {
        this.stockTableEntryValidated = stockTableEntryValidated;
    }

    public boolean isDiscontinued()
    {
        return discontinued;
    }

    public void setDiscontinued( final boolean discontinued )
    {
        this.discontinued = discontinued;
    }

    /**
     * Stock quotes are valid for 15 minutes.
     * @return
     */
    @Override
    protected long getCurrentDurationTime()
    {
        return StockPriceQuoteCache.EXPIRATION_TIME;
    }
}
