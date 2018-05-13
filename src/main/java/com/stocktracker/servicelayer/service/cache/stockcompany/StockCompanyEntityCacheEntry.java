package com.stocktracker.servicelayer.service.cache.stockcompany;

import com.stocktracker.repositorylayer.entity.StockCompanyEntity;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntry;

/**
 * This class defines the Stock Company Entity cache's entry types.
 */
public class StockCompanyEntityCacheEntry extends AsyncCacheEntry<StockCompanyEntity>
{
    /**
     * Stock companys are valid for 6 hours.
     * @return
     */
    @Override
    protected long getCurrentDurationTime()
    {
        // never expires.  There are very limited fields in the stock company and they don't change.
        return Long.MAX_VALUE;
    }
}
