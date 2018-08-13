package com.stocktracker.servicelayer.service.cache.linkedaccount;

import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.servicelayer.service.cache.common.AsyncBatchCache;
import com.stocktracker.servicelayer.service.cache.common.AsyncCache;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheStrategy;
import com.stocktracker.servicelayer.tradeit.apiresults.GetAccountOverviewAPIResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.stocktracker.servicelayer.service.cache.common.AsyncCacheStrategy.REMOVE;

/**
 * This is the cache for IEXTrading Stock Quotes.  See https://iextrading.com/developer/docs/#quote for information
 * contained in the quote.  It is information that only needs to be obtained a couple times a day, well mostly, that's
 * they way we plan to use it initially.
 */
@Service
public class LinkedAccountEntityCache extends AsyncCache<UUID,
                                                         LinkedAccountEntity,
                                                         LinkedAccountEntityCacheAsyncKey,
                                                         LinkedAccountEntityCacheEntry,
                                                         LinkedAccountEntityServiceExecutor>
{
    @Autowired
    private LinkedAccountEntityServiceExecutor stockQuoteEntityServiceExecutor;

    /**
     * Creates the cache entry.
     * @return
     */
    @Override
    protected LinkedAccountEntityCacheEntry createCacheEntry()
    {
        return new LinkedAccountEntityCacheEntry();
    }

    /**
     * Creates the executor.
     * @return
     */
    @Override
    protected LinkedAccountEntityServiceExecutor getExecutor()
    {
        return this.stockQuoteEntityServiceExecutor;
    }

    /**
     * Identifies the strategy when retrieving cache entry from the cache.  Since the stock quote information is cached in the stock_quote table,
     * we don't need to cache it in the cache and thus we can remove the cache entry after we have the asynchronous work completed.
     * @return
     */
    @Override
    protected AsyncCacheStrategy getCacheStrategy()
    {
        return REMOVE;
    }
}
