package com.stocktracker.servicelayer.service.cache.stockquote;

import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import com.stocktracker.servicelayer.service.cache.common.AsyncBatchCache;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDataRequestException;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheStrategy;
import com.stocktracker.servicelayer.service.cache.stockpricequote.StockQuoteEntityCacheRequestKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.zankowski.iextrading4j.api.stocks.Quote;

import static com.stocktracker.servicelayer.service.cache.common.AsyncCacheStrategy.KEEP;
import static com.stocktracker.servicelayer.service.cache.common.AsyncCacheStrategy.REMOVE;

/**
 * This is the cache for IEXTrading Stock Quotes.  See https://iextrading.com/developer/docs/#quote for information
 * contained in the quote.  It is information that only needs to be obtained a couple times a day, well mostly, that's
 * they way we plan to use it initially.
 */
@Service
public class StockQuoteEntityCache extends AsyncBatchCache<String,
                                                           StockQuoteEntity,
                                                           String,
                                                           Quote,
                                                           StockQuoteEntityCacheEntry,
                                                           StockQuoteEntityCacheRequestKey,
                                                           StockQuoteEntityCacheRequest,
                                                           StockQuoteEntityCacheResponse,
                                                           StockQuoteEntityServiceExecutor>
{
    @Autowired
    private StockQuoteEntityServiceExecutor stockQuoteEntityServiceExecutor;

    /**
     * Converts the Quote into a StockQuoteEntity
     * @param cacheKey
     * @param asyncKey
     * @param asyncData
     * @return
     * @throws AsyncCacheDataRequestException
     */
    @Override
    protected StockQuoteEntity convertAsyncData( final String cacheKey, final String asyncKey, final Quote asyncData )
        throws AsyncCacheDataRequestException
    {
        final StockQuoteEntity stockQuoteEntity = this.context.getBean( StockQuoteEntity.class );
        stockQuoteEntity.setTickerSymbol( cacheKey );
        stockQuoteEntity.copyQuote( asyncData );
        return stockQuoteEntity;
    }

    /**
     * Creates the cache entry.
     * @return
     */
    @Override
    protected StockQuoteEntityCacheEntry createCacheEntry()
    {
        return new StockQuoteEntityCacheEntry();
    }

    /**
     * Creates the executor.
     * @return
     */
    @Override
    protected StockQuoteEntityServiceExecutor getExecutor()
    {
        return this.stockQuoteEntityServiceExecutor;
    }

    /**
     * Identifies the strategy when retrieving cache entry from the cache.  Since the stock quote information is cached in the stock_quote table,
     * we don't need to cache it in the cache and thus we can remove the cache entry after we have the asynchronous work completed.
     * @return
     */
    @Override
    protected AsyncCacheStrategy getCacheStrategy()
    {
        return KEEP;
    }

    @Override
    protected StockQuoteEntityCacheRequest createBatchRequestType()
    {
        return this.context.getBean( StockQuoteEntityCacheRequest.class );
    }
}
