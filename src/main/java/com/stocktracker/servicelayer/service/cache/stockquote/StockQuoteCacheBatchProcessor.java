package com.stocktracker.servicelayer.service.cache.stockquote;

import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheBatchProcessor;
import com.stocktracker.servicelayer.service.cache.stockpricequote.StockQuoteEntityCacheRequestKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Stock quote batch processors.  Extends the AsyncCacheBatchProcessor which in turn uses the StockQuoteCacheClient
 * to process a batch of DTOs to obtain the stock quote information from the Stock Quote Cache.
 */
@Service
public class StockQuoteCacheBatchProcessor extends AsyncCacheBatchProcessor<String,
                                                                            String,
                                                                            StockQuoteEntity,
                                                                            StockQuoteEntityCacheEntry,
                                                                            StockQuoteEntityCacheDataReceiver,
                                                                            StockQuoteEntityCacheRequest,
                                                                            StockQuoteEntityCacheResponse,
                                                                            StockQuoteEntityCacheRequestKey,
                                                                            StockQuoteEntityContainer,
                                                                            StockQuoteEntityServiceExecutor,
                                                                            StockQuoteEntityCache,
                                                                            StockQuoteEntityCacheClient>
{
    @Autowired
    private StockQuoteEntityCacheClient stockQuoteEntityCacheClient;

    @Override
    protected void setDataReceiver( final StockQuoteEntityContainer cachedDataContainer,
                                    final StockQuoteEntityCacheDataReceiver dataReceiver )
    {
        /*
         * The cached data will be null if the data is not found, needs to be fetched, or there was an error.
         */
        if ( cachedDataContainer.getCachedData() != null )
        {
            dataReceiver.setCachedData( cachedDataContainer.getCachedData() );
        }
        dataReceiver.setCacheState( cachedDataContainer.getCacheState() );
        dataReceiver.setCacheError( cachedDataContainer.getCacheError() );
    }

    @Override
    protected String getCacheKey( final StockQuoteEntityContainer container )
    {
        return container.getCacheKey();
    }

    @Override
    public StockQuoteEntityCacheDataReceiver newReceiver()
    {
        return this.context.getBean( StockQuoteEntityCacheDataReceiver.class );
    }

    @Override
    protected StockQuoteEntityContainer newContainer()
    {
        return this.context.getBean( StockQuoteEntityContainer.class );
    }

    @Override
    public StockQuoteEntityCacheClient getAsyncCacheClient()
    {
        return this.stockQuoteEntityCacheClient;
    }
}
