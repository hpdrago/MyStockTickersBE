package com.stocktracker.servicelayer.service.cache.stockquote;

import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import com.stocktracker.servicelayer.service.StockQuoteEntityService;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDBEntityClient;
import com.stocktracker.servicelayer.service.cache.stockpricequote.StockQuoteEntityCacheRequestKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.zankowski.iextrading4j.api.stocks.Quote;

/**
 * This is the client class that interacts with the {@code StockQuoteEntityCache} to get current IEXTrading Quote
 * updates.
 */
@Service
public class StockQuoteEntityCacheClient extends AsyncCacheDBEntityClient<String,
                                                                          StockQuoteEntity,
                                                                          String,
                                                                          Quote,
                                                                          StockQuoteEntityCacheEntry,
                                                                          StockQuoteEntityCacheDataReceiver,
                                                                          StockQuoteEntityCacheRequestKey,
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

    @Override
    protected StockQuoteEntityCacheRequestKey createRequestKey( final String cacheKey, final String asyncKey )
    {
        return new StockQuoteEntityCacheRequestKey( cacheKey, asyncKey );
    }
}
