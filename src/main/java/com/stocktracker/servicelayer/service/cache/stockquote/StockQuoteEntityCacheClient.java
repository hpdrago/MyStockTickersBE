package com.stocktracker.servicelayer.service.cache.stockquote;

import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import com.stocktracker.servicelayer.service.StockQuoteEntityService;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDBEntityClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This interface is used by any class that need information from the {@code StockQuoteEntityCache}.
 */
@Service
public class StockQuoteEntityCacheClient extends AsyncCacheDBEntityClient<String,
                                                                          StockQuoteEntity,
                                                                          StockQuoteEntityCacheEntry,
                                                                          StockQuoteEntityCacheDataReceiver,
                                                                          StockQuoteEntityCacheRequest,
                                                                          StockQuoteEntityCacheResponse,
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
}
