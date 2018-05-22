package com.stocktracker.servicelayer.service.cache.stockpricequote;

import com.stocktracker.servicelayer.service.cache.common.AsyncCacheClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Client interface with the Stock Price Quote Cache.
 */
@Service
public class StockPriceQuoteCacheClient extends AsyncCacheClient<String,
                                                                 StockPriceQuote,
                                                                 StockPriceQuoteCacheEntry,
                                                                 StockPriceQuoteServiceExecutor,
                                                                 StockPriceQuoteCache,
                                                                 StockPriceQuoteDataReceiver>
{
    private StockPriceQuoteCache stockPriceQuoteCache;

    @Override
    protected StockPriceQuoteCache getCache()
    {
        return this.stockPriceQuoteCache;
    }

    @Override
    protected StockPriceQuote createCachedDataObject()
    {
        return context.getBean( StockPriceQuote.class );
    }

    @Autowired
    public void setStockPriceQuoteCache( final StockPriceQuoteCache stockPriceQuoteCache )
    {
        this.stockPriceQuoteCache = stockPriceQuoteCache;
    }

}
