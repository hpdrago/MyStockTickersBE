package com.stocktracker.servicelayer.service.cache.stockcompany;

import com.stocktracker.repositorylayer.entity.StockCompanyEntity;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntry;
import pl.zankowski.iextrading4j.api.stocks.Company;

import java.util.concurrent.TimeUnit;

/**
 * This class defines the Stock Company Entity cache's entry types.
 */
public class StockCompanyEntityCacheEntry extends AsyncCacheEntry<String,StockCompanyEntity,String,Company>
{
    /**
     * Stock companies are valid for 1 day.
     * @return
     */
    @Override
    protected long getCurrentDurationTime()
    {
        return TimeUnit.DAYS.toMillis( 1 );
    }
}
