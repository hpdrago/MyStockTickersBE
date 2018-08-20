package com.stocktracker.servicelayer.service.cache.stockpricequote;

import com.stocktracker.servicelayer.service.cache.common.AsyncCacheBatchProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Stock price quote batch processors.  Extends the AsyncCacheBatchProcessor which in turn uses the
 * StockPriceQuoteCacheClient to process a batch of DTOs to obtain the stock quote information from the Stock Quote Cache.
 */
@Service
public class StockPriceQuoteCacheBatchProcessor extends AsyncCacheBatchProcessor<String,
                                                                                 StockPriceQuote,
                                                                                 String,
                                                                                 BigDecimal,
                                                                                 StockPriceQuoteCacheEntry,
                                                                                 StockPriceQuoteCacheDataReceiver,
                                                                                 StockPriceQuoteCacheRequestKey,
                                                                                 StockPriceQuoteCacheRequest,
                                                                                 StockPriceQuoteCacheResponse,
                                                                                 StockPriceQuote,
                                                                                 StockPriceQuoteServiceExecutor,
                                                                                 StockPriceQuoteCache,
                                                                                 StockPriceQuoteCacheClient>
{
    @Autowired
    private StockPriceQuoteCacheClient stockPriceQuoteCacheClient;

    @Override
    protected String getCacheKey( final StockPriceQuote container )
    {
        return container.getCacheKey();
    }

    @Override
    public StockPriceQuoteCacheClient getAsyncCacheClient()
    {
        return this.stockPriceQuoteCacheClient;
    }

}
