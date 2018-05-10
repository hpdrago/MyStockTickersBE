package com.stocktracker.servicelayer.service.cache.stockquote;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import com.stocktracker.servicelayer.service.cache.common.AsyncCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

/**
 * This is the cache for IEXTrading Stock Quotes.  See https://iextrading.com/developer/docs/#quote for information
 * contained in the quote.  It is information that only needs to be obtained a couple times a day, well mostly, that's
 * they way we plan to use it initially.
 */
@Service
public class StockQuoteEntityCache extends AsyncCache<StockQuoteEntity,
                                                      String,
                                                      StockQuoteEntityCacheEntry,
                                                      StockQuoteEntityServiceExecutor>
                                   implements MyLogger
{
    private StockQuoteEntityServiceExecutor stockQuoteEntityServiceExecutor;

    /**
     * Gets the Stock Quote from the cache if present, otherwise the Stock Quote is fetch asynchronously.
     * {@code stockQuoteEntityContainer} will be upated with the Stock Quote information and the cache entry status.
     * @param tickerSymbol
     * @param stockQuoteEntityContainer
     */
    public void asynchronousGet( @NotNull final String tickerSymbol, @NotNull StockQuoteEntityContainer stockQuoteEntityContainer )
    {
        final String methodName = "asynchronousGet";
        logMethodBegin( methodName, tickerSymbol );
        final StockQuoteEntityCacheEntry cacheEntry = this.asynchronousGet( tickerSymbol );
        stockQuoteEntityContainer.setStockQuoteEntity( cacheEntry.getCacheState(), cacheEntry.getInformation() );
        logMethodEnd( methodName, cacheEntry );
    }

    /**
     * Creates a new entity.
     * @return
     */
    /*@Override
    protected StockQuoteEntity createInformationObject()
    {
        return new StockQuoteEntity();
    }*/

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

    @Autowired
    public void setStockQuoteEntityServiceExecutor( final StockQuoteEntityServiceExecutor stockQuoteEntityServiceExecutor )
    {
        this.stockQuoteEntityServiceExecutor = stockQuoteEntityServiceExecutor;
    }

}
