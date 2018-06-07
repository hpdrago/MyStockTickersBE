package com.stocktracker.servicelayer.service.cache.stockquote;

import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import com.stocktracker.servicelayer.service.StockQuoteEntityService;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDBEntityBatchClient;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDBEntityClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This interface is used by any class that need information from the {@code StockQuoteEntityCache}.
 */
@Service
public class StockQuoteEntityCacheClient extends AsyncCacheDBEntityBatchClient<String,
                                                                               StockQuoteEntity,
                                                                               StockQuoteEntityCacheEntry,
                                                                               StockQuoteEntityCacheRequest,
                                                                               StockQuoteEntityCacheResponse,
                                                                               StockQuoteEntityServiceExecutor,
                                                                               StockQuoteEntityCache,
                                                                               StockQuoteEntityCacheDataReceiver,
                                                                               StockQuoteEntityService>
{
    @Autowired
    private StockQuoteEntityCache stockQuoteEntityCache;
    @Autowired
    private StockQuoteEntityService stockQuoteEntityService;

    /**
     * Returns the {@code StockQuoteEntityCache} instance.
     * @return
     */
    @Override
    protected StockQuoteEntityCache getCache()
    {
        return this.stockQuoteEntityCache;
    }

    /**
     * Create the {@code StockQuoteEntity} object which is the data that is cached.
     * @return
     */
    @Override
    protected StockQuoteEntity createCachedDataObject()
    {
        return this.context.getBean( StockQuoteEntity.class );
    }

    /**
     * Getter for the {@code StockQuoteEntityService}.
     * @return
     */
    @Override
    protected StockQuoteEntityService getEntityService()
    {
        return this.stockQuoteEntityService;
    }
}
