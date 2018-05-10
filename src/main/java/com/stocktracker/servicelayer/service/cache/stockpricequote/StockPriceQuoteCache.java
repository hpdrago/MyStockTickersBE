package com.stocktracker.servicelayer.service.cache.stockpricequote;

import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.servicelayer.service.cache.common.AsyncCache;
import com.stocktracker.servicelayer.service.stocks.StockPriceQuoteContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * This is the cache for Stock Price Quotes {@see StockPriceQuote} for columns values.  Stock price quote is basically
 * needed to enable us to only fetch stock prices when the values have become stale and need to be refetched.  A stock
 * quote basically contains the ticker symbol, last price, open price, and the company name.  We might at other similar
 * information later.  This cache should not be confused with the {@code StockQuoteEntityCache} which contains {@code Quote}
 * information from IEXTrading which is information that is not changed that often during the day.
 */
@Service
public class StockPriceQuoteCache extends AsyncCache<StockPriceQuote,
                                                     String,
                                                     StockPriceQuoteCacheEntry,
                                                     StockPriceQuoteServiceExecutor>
{
    public static final long EXPIRATION_TIME = TimeUnit.MINUTES.toMillis( 15 );

    private StockPriceQuoteServiceExecutor stockPriceQuoteServiceExecutor;

    /**
     * Gets the Stock Quote from the cache if present, otherwise the Stock Quote is fetch asynchronously.
     * {@code stockPriceQuoteContainer} will be upated with the Stock Quote information and the cache entry status.
     * @param tickerSymbol
     * @param stockPriceQuoteContainer
     */
    public void asynchronousGet( @NotNull final String tickerSymbol, @NotNull StockPriceQuoteContainer stockPriceQuoteContainer )
    {
        final String methodName = "asynchronousGet";
        logMethodBegin( methodName, tickerSymbol );
        final StockPriceQuoteCacheEntry cacheEntry = this.asynchronousGet( tickerSymbol );
        stockPriceQuoteContainer.setStockPriceQuote( cacheEntry.getCacheState(), cacheEntry.getInformation() );
        logMethodEnd( methodName, cacheEntry );
    }

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
            stockPriceQuoteCacheEntry.getInformation().setLastPrice( stockPrice );
        }
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

    @Autowired
    public void setStockPriceQuoteServiceExecutor( final StockPriceQuoteServiceExecutor stockPriceQuoteServiceExecutor )
    {
        this.stockPriceQuoteServiceExecutor = stockPriceQuoteServiceExecutor;
    }

}
