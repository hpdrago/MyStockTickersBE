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
                                                                  StockQuoteEntityServiceExecutor,
                                                                  StockQuoteEntityCache,
    StockQuoteEntityCacheDataReceiver,
                                                                  StockQuoteEntityService>
{
    private StockQuoteEntityCache stockQuoteEntityCache;

    /**
     * Stock quote information is very static so update it if it hasn't been updated before or if the data is older
     * than a year.
     * @param entity
     * @return
     */
    @Override
    protected boolean isCurrent( final StockQuoteEntity entity )
    {
        return StockQuoteEntity.isCurrent( entity );
    }

    /**
     * Get the error message
     * @return
     */
    @Override
    protected StockQuoteEntityCache getCache()
    {
        return this.stockQuoteEntityCache;
    }

    @Override
    protected StockQuoteEntity createCachedDataObject()
    {
        return this.context.getBean( StockQuoteEntity.class );
    }

    @Override
    protected StockQuoteEntityService getEntityService()
    {
        return null;
    }

    @Autowired
    public void setStockQuoteEntityCache( final StockQuoteEntityCache stockQuoteEntityCache )
    {
        this.stockQuoteEntityCache = stockQuoteEntityCache;
    }

}
