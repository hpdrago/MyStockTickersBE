package com.stocktracker.servicelayer.service.cache.stockpricequote;

import com.stocktracker.servicelayer.service.cache.common.AsyncCacheBatchClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Client interface with the Stock Price Quote Cache.
 */
@Service
public class StockPriceQuoteCacheClient extends AsyncCacheBatchClient<String,
                                                                      String,
                                                                      StockPriceQuote,
                                                                      StockPriceQuoteCacheEntry,
                                                                      StockPriceQuoteCacheDataReceiver,
                                                                      StockPriceQuoteCacheRequest,
                                                                      StockPriceQuoteCacheResponse,
                                                                      StockPriceQuoteCacheRequestKey,
                                                                      StockPriceQuoteServiceExecutor,
                                                                      StockPriceQuoteCache>
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

    @Override
    protected StockPriceQuoteCacheRequestKey createRequestKey( final String cacheKey, final String thirdPartyKey )
    {
        return new StockPriceQuoteCacheRequestKey( cacheKey, thirdPartyKey );
    }
}
