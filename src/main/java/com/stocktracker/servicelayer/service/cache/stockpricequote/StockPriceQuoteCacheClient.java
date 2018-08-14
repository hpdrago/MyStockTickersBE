package com.stocktracker.servicelayer.service.cache.stockpricequote;

import com.stocktracker.servicelayer.service.cache.common.AsyncCacheBatchClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Client interface with the Stock Price Quote Cache.
 */
@Service
public class StockPriceQuoteCacheClient extends AsyncCacheBatchClient<String,
                                                                      StockPriceQuote,
                                                                      String,
                                                                      BigDecimal,
                                                                      StockPriceQuoteCacheEntry,
                                                                      StockPriceQuoteCacheDataReceiver,
                                                                      StockPriceQuoteCacheRequestKey,
                                                                      StockPriceQuoteCacheRequest,
                                                                      StockPriceQuoteCacheResponse,
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
    protected StockPriceQuoteCacheRequestKey createRequestKey( final String cacheKey, final String asyncKey )
    {
        return new StockPriceQuoteCacheRequestKey( cacheKey, asyncKey );
    }
}
