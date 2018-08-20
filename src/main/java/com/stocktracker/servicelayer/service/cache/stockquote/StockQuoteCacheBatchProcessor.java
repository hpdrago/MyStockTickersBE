package com.stocktracker.servicelayer.service.cache.stockquote;

import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheBatchProcessor;
import com.stocktracker.servicelayer.service.cache.stockpricequote.StockQuoteEntityCacheRequestKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.zankowski.iextrading4j.api.stocks.Quote;

/**
 * Stock quote batch processors.  Extends the AsyncCacheBatchProcessor which in turn uses the StockQuoteCacheClient
 * to process a batch of DTOs to obtain the stock quote information from the Stock Quote Cache.
 */
@Service
public class StockQuoteCacheBatchProcessor extends AsyncCacheBatchProcessor<String,
                                                                            StockQuoteEntity,
                                                                            String,
                                                                            Quote,
                                                                            StockQuoteEntityCacheEntry,
                                                                            StockQuoteEntityCacheDataReceiver,
                                                                            StockQuoteEntityCacheRequestKey,
                                                                            StockQuoteEntityCacheRequest,
                                                                            StockQuoteEntityCacheResponse,
                                                                            StockQuoteEntityContainer,
                                                                            StockQuoteEntityServiceExecutor,
                                                                            StockQuoteEntityCache,
                                                                            StockQuoteEntityCacheClient>
{
    @Autowired
    private StockQuoteEntityCacheClient stockQuoteEntityCacheClient;

    @Override
    protected String getCacheKey( final StockQuoteEntityContainer container )
    {
        return container.getCacheKey();
    }

    @Override
    public StockQuoteEntityCacheClient getAsyncCacheClient()
    {
        return this.stockQuoteEntityCacheClient;
    }
}
