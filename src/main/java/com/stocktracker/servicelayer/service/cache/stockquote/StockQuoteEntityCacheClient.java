package com.stocktracker.servicelayer.service.cache.stockquote;

import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import com.stocktracker.servicelayer.service.StockQuoteEntityService;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDBEntityClient;
import com.stocktracker.servicelayer.service.cache.stockpricequote.StockQuoteEntityCacheRequestKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This interface is used by any class that need information from the {@code StockQuoteEntityCache}.
 */
@Service
public class StockQuoteEntityCacheClient extends AsyncCacheDBEntityClient<String,
                                                                          String,
                                                                          StockQuoteEntity,
                                                                          StockQuoteEntityCacheEntry,
                                                                          StockQuoteEntityCacheDataReceiver,
                                                                          StockQuoteEntityCacheRequest,
                                                                          StockQuoteEntityCacheResponse,
    StockQuoteEntityCacheRequestKey,
                                                                          StockQuoteEntityServiceExecutor,
                                                                          StockQuoteEntityCache,
                                                                          StockQuoteEntityService>
{
    @Autowired
    private StockQuoteEntityCache stockQuoteEntityCache;
    @Autowired
    private StockQuoteEntityService stockQuoteEntityService;

    @Override
    protected StockQuoteEntityCache getCache()
    {
        return stockQuoteEntityCache;
    }

    @Override
    protected StockQuoteEntity createCachedDataObject()
    {
        return this.context.getBean( StockQuoteEntity.class );
    }

    @Override
    protected StockQuoteEntityService getEntityService()
    {
        return this.stockQuoteEntityService;
    }

    @Override
    protected StockQuoteEntityCacheRequestKey createRequestKey( final String cacheKey, final String asyncKey )
    {
        return new StockQuoteEntityCacheRequestKey( cacheKey, asyncKey );
    }
}
