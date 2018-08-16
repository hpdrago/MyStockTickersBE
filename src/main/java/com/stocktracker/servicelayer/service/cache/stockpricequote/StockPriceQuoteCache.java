package com.stocktracker.servicelayer.service.cache.stockpricequote;

import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.servicelayer.service.cache.common.AsyncBatchCache;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDataRequestException;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.stocktracker.servicelayer.service.cache.common.AsyncCacheStrategy.KEEP;

/**
 * This is the cache for Stock Price Quotes {@see StockPriceQuote} for columns values.  Stock price quote is basically
 * needed to enable us to only fetch stock prices when the values have become stale and need to be refetched.  A stock
 * quote basically contains the ticker symbol, last price, open price, and the company name.  We might at other similar
 * information later.  This cache should not be confused with the {@code StockQuoteEntityCache} which contains {@code Quote}
 * information from IEXTrading which is information that is not changed that often during the day.
 */
@Service
public class StockPriceQuoteCache extends AsyncBatchCache<String,
                                                          StockPriceQuote,
                                                          String,
                                                          BigDecimal,
                                                          StockPriceQuoteCacheEntry,
                                                          StockPriceQuoteCacheRequestKey,
                                                          StockPriceQuoteCacheRequest,
                                                          StockPriceQuoteCacheResponse,
                                                          StockPriceQuoteServiceExecutor>
{
    public static final long EXPIRATION_TIME = TimeUnit.MINUTES.toMillis( 15 );

    private StockPriceQuoteServiceExecutor stockPriceQuoteServiceExecutor;

    /**
     * Updates the stock price cache for the ticker symbol.
     * @param tickerSymbol
     * @param stockPrice
     * @throws StockNotFoundException
     */
    public void updateStockPrice( @NotNull final String tickerSymbol, @NotNull final BigDecimal stockPrice )
        throws StockNotFoundException
    {
        final String methodName = "updateStockPrice";
        logMethodBegin( methodName, tickerSymbol, stockPrice );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Objects.requireNonNull( stockPriceQuoteServiceExecutor,  "stockPriceQuoteServiceExecutor cannot be null" );
        final StockPriceQuoteCacheEntry stockPriceQuoteCacheEntry = this.getCacheEntry( tickerSymbol );
        if ( stockPriceQuoteCacheEntry != null )
        {
            stockPriceQuoteCacheEntry.getCachedData().setLastPrice( stockPrice );
        }
    }

    /**
     * Convert the stock price into a {@code StockPriceQuote} instance.
     * @param cacheKey
     * @param asyncKey
     * @param stockPrice
     * @return
     * @throws AsyncCacheDataRequestException
     */
    @Override
    protected StockPriceQuote convertAsyncData( final String cacheKey,
                                                final String asyncKey,
                                                final BigDecimal stockPrice )
        throws AsyncCacheDataRequestException
    {
        final String methodName = "convertAsyncData";
        logMethodBegin( methodName, cacheKey, asyncKey, stockPrice );
        final StockPriceQuote stockPriceQuote = this.context.getBean( StockPriceQuote.class );
        stockPriceQuote.setTickerSymbol( cacheKey );
        stockPriceQuote.setLastPrice( stockPrice );
        return stockPriceQuote;
    }

    /**
     * Creates the cache entry.
     * @return
     */
    @Override
    protected StockPriceQuoteCacheEntry createCacheEntry()
    {
        return new StockPriceQuoteCacheEntry();
    }

    /**
     * Creates the executor.
     * @return
     */
    @Override
    protected StockPriceQuoteServiceExecutor getExecutor()
    {
        return this.stockPriceQuoteServiceExecutor;
    }

    @Override
    protected AsyncCacheStrategy getCacheStrategy()
    {
        return KEEP;
    }

    @Autowired
    public void setStockPriceQuoteServiceExecutor( final StockPriceQuoteServiceExecutor stockPriceQuoteServiceExecutor )
    {
        this.stockPriceQuoteServiceExecutor = stockPriceQuoteServiceExecutor;
    }

    @Override
    protected StockPriceQuoteCacheRequest createBatchRequestType()
    {
        return this.context.getBean( StockPriceQuoteCacheRequest.class );
    }
}
