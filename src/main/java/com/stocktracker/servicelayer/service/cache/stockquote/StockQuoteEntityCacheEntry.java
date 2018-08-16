package com.stocktracker.servicelayer.service.cache.stockquote;

import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntry;
import pl.zankowski.iextrading4j.api.stocks.Quote;

import java.util.concurrent.TimeUnit;

/**
 * This class defines the Stock Quote Entity cache's entry types.
 */
public class StockQuoteEntityCacheEntry extends AsyncCacheEntry<String,StockQuoteEntity,String,Quote>
{
    /**
     * Stock quotes are valid for 6 hours.
     * @return
     */
    @Override
    protected long getCurrentDurationTime()
    {
        return TimeUnit.HOURS.convert( 6, TimeUnit.MILLISECONDS );
    }
}
