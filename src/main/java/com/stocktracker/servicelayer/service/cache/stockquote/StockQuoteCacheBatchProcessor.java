package com.stocktracker.servicelayer.service.cache.stockquote;

import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheBatchProcessor;
import com.stocktracker.weblayer.dto.StockQuoteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Stock quote batch processors.  Extends the AsyncCacheBatchProcessor which in turn uses the StockQuoteCacheClient
 * to process a batch of DTOs to obtain the stock quote information from the Stock Quote Cache.
 */
@Service
public class StockQuoteCacheBatchProcessor extends AsyncCacheBatchProcessor<String,
                                                                            StockQuoteEntity,
                                                                            StockQuoteEntityCacheEntry,
                                                                            StockQuoteEntityCacheDataReceiver,
                                                                            StockQuoteEntityCacheRequest,
                                                                            StockQuoteEntityCacheResponse,
                                                                            StockQuoteEntityContainer,
                                                                            StockQuoteDTO,
                                                                            StockQuoteEntityServiceExecutor,
                                                                            StockQuoteEntityCache,
                                                                            StockQuoteEntityCacheClient>
{
    @Autowired
    private StockQuoteEntityCacheClient stockQuoteEntityCacheClient;

    @Override
    protected void setDTOContainer( final StockQuoteEntityContainer cachedDataContainer, final StockQuoteDTO dtoContainer )
    {
        /*
         * The cached data will be null if the data is not found, needs to be fetched, or there was an error.
         */
        if ( cachedDataContainer.getCachedData() != null )
        {
            dtoContainer.setCachedData( cachedDataContainer.getCachedData() );
        }
        dtoContainer.setCacheState( cachedDataContainer.getCacheState() );
        dtoContainer.setCacheError( cachedDataContainer.getCacheError() );
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
