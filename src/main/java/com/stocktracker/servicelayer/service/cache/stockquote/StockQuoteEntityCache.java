package com.stocktracker.servicelayer.service.cache.stockquote;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import com.stocktracker.servicelayer.service.cache.common.AsyncCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

/**
 * This is the cache for IEXTrading Stock Quotes.  See https://iextrading.com/developer/docs/#quote for information
 * contained in the quote.  It is information that only needs to be obtained a couple times a day, well mostly, that's
 * they way we plan to use it initially.
 */
@Service
public class StockQuoteEntityCache extends AsyncCache<StockQuoteEntity,
                                                      String,
                                                      StockQuoteEntityCacheEntry,
                                                      StockQuoteEntityServiceExecutor>
                                   implements MyLogger
{
    private StockQuoteEntityServiceExecutor stockQuoteEntityServiceExecutor;

    /**
     * Creates the cache entry.
     * @return
     */
    @Override
    protected StockQuoteEntityCacheEntry createCacheEntry()
    {
        return new StockQuoteEntityCacheEntry();
    }

    /**
     * Creates the executor.
     * @return
     */
    @Override
    protected StockQuoteEntityServiceExecutor getExecutor()
    {
        return this.stockQuoteEntityServiceExecutor;
    }

    @Autowired
    public void setStockQuoteEntityServiceExecutor( final StockQuoteEntityServiceExecutor stockQuoteEntityServiceExecutor )
    {
        this.stockQuoteEntityServiceExecutor = stockQuoteEntityServiceExecutor;
    }

}
