package com.stocktracker.servicelayer.service.stockinformationprovider;

import com.stocktracker.AppConfig;
import com.stocktracker.common.MyLogger;
import com.stocktracker.servicelayer.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This cache holds stocks that will expire after the set amount of time
 */
@Service
public class StockCache implements MyLogger
{
    private static final long EXPIRATION_TIME = 5 * 60 * 1000; // 5 min
    private Map<String, CachedStockEntry> cacheEntryMap = new HashMap<>();
    private YahooStockService yahooStockService;
    private IEXTradingStockService iexTradingStockService;
    private ThreadPoolTaskExecutor taskExecutor;
    private StockService stockService;

    public StockCache()
    {
        ApplicationContext context = new AnnotationConfigApplicationContext( AppConfig.class );
        taskExecutor = (ThreadPoolTaskExecutor) context.getBean( "taskExecutor" );
    }

    /**
     * Get the stock quote information for the {@code tickerSymbol}
     *
     * @param tickerSymbol The stock symbol
     * @param fetchMode Identified whether this is a synchronous or asynchronous call
     * @return
     */
    public CachedStockQuote getStock( @NotNull String tickerSymbol, final StockQuoteFetchMode fetchMode )
    {
        final String methodName = "getStock";
        logMethodBegin( methodName, tickerSymbol, fetchMode );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Objects.requireNonNull( fetchMode, "fetchMode cannot be null" );
        CachedStockEntry cachedStockEntry = this.cacheEntryMap.get( tickerSymbol );
        if ( cachedStockEntry == null )
        {
            if ( fetchMode == StockQuoteFetchMode.SYNCHRONOUS )
            {
                cachedStockEntry = synchronousQuoteFetch( tickerSymbol );
            }
            else
            {
                cachedStockEntry = this.asynchronousQuoteFetch( tickerSymbol );
            }
        }
        else
        {
            if ( System.currentTimeMillis() - cachedStockEntry.lastQuoteRefreshTime > EXPIRATION_TIME )
            {
                if ( fetchMode == StockQuoteFetchMode.SYNCHRONOUS )
                {
                    logDebug( methodName, "{0} is stale", tickerSymbol );
                    cachedStockEntry = synchronousQuoteFetch( tickerSymbol );
                }
                else
                {
                    /*
                     * Set the cached value to stale and return that value to the caller but also
                     * submit a task to refresh the stock quote
                     */
                    cachedStockEntry.setStockQuoteState( StockQuoteState.STALE );
                    this.asynchronousQuoteFetch( tickerSymbol );
                }
            }
            else
            {
                cachedStockEntry.setStockQuoteState( StockQuoteState.CURRENT );
            }
        }
        logMethodEnd( methodName, cachedStockEntry );
        return cachedStockEntry;
    }

    /**
     * Creates a CachedStockEntry and submits a task to refresh the stock quote.
     * @param tickerSymbol
     * @return The created CachedStockEntry containing only the ticker symbol and state.
     */
    private CachedStockEntry asynchronousQuoteFetch( final @NotNull String tickerSymbol )
    {
        CachedStockEntry cachedStockEntry = new CachedStockEntry( tickerSymbol, StockQuoteState.NOT_CACHED );
        GetStockQuoteTask stockQuoteTask = new GetStockQuoteTask();
        stockQuoteTask.setTickerSymbol( tickerSymbol );
        stockQuoteTask.setStockCache( this );
        stockQuoteTask.setServiceProvider( this.iexTradingStockService );
        this.taskExecutor.execute( stockQuoteTask );
        return cachedStockEntry;
    }

    /**
     * Synchronous fetch from stock quote provider
     * @param tickerSymbol
     * @return
     */
    private CachedStockEntry synchronousQuoteFetch( final @NotNull String tickerSymbol )
    {
        final String methodName = "synchronousQuoteFetch";
        logMethodBegin( methodName, tickerSymbol );
        final CachedStockEntry cachedStockEntry;
        cachedStockEntry = this.getStockFromProvider( this.iexTradingStockService, tickerSymbol );
        cachedStockEntry.setStockQuoteState( StockQuoteState.CURRENT );
        logMethodEnd( methodName, cachedStockEntry );
        return cachedStockEntry;
    }

    /**
     * Using the yahooStockService, call to get the latest stock quote for the {@code tickerSymbol}
     * @param tickerSymbol
     * @return
     */
    public CachedStockEntry getStockFromProvider( final @NotNull StockQuoteServiceProvider stockQuoteServiceProvider,
                                                  final @NotNull String tickerSymbol )
    {
        final String methodName = "getStockFromProvider";
        logMethodBegin( methodName, tickerSymbol );
        CachedStockEntry cachedStockEntry = null;
        StockTickerQuote stockTickerQuote = stockQuoteServiceProvider.getStockQuote( tickerSymbol );
        if ( stockTickerQuote == null )
        {
            cachedStockEntry = new CachedStockEntry( tickerSymbol, StockQuoteState.NOT_FOUND );
            logInfo( methodName, "{0} doesn't recognize {1}",
                     stockQuoteServiceProvider.getProviderName(), tickerSymbol );
        }
        else
        {
            cachedStockEntry = new CachedStockEntry( stockTickerQuote );
            /*
             * Update the cache
             */
            this.cacheEntryMap.put( tickerSymbol, cachedStockEntry );
            /*
             * Update the database
             */
            this.stockService.persistStockQuote( cachedStockEntry );
        }
        logMethodEnd( methodName, cachedStockEntry );
        return cachedStockEntry;
    }

    /**
     * POJO implementation for interface {@code CachedStock}
     */
    private class CachedStockEntry implements CachedStockQuote
    {
        private String tickerSymbol;
        private String companyName;
        private String exchange;
        private BigDecimal lastPrice;
        private Timestamp lastPriceChange;
        private long lastQuoteRefreshTime;
        private StockQuoteState stockQuoteState;

        public CachedStockEntry( final String tickerSymbol, final StockQuoteState stockQuoteState )
        {
            this.tickerSymbol = tickerSymbol;
            this.stockQuoteState = stockQuoteState;
        }

        public CachedStockEntry( final StockTickerQuote stockTickerQuote )
        {
            this.lastQuoteRefreshTime = System.currentTimeMillis();
            this.tickerSymbol = stockTickerQuote.getTickerSymbol();
            this.companyName = stockTickerQuote.getCompanyName();
            this.lastPriceChange = stockTickerQuote.getLastPriceChange();
            this.lastPrice = stockTickerQuote.getLastPrice();
        }

        public String getTickerSymbol()
        {
            return tickerSymbol;
        }

        public void setTickerSymbol( String tickerSymbol )
        {
            this.tickerSymbol = tickerSymbol;
        }

        public String getCompanyName()
        {
            return companyName;
        }

        public void setCompanyName( String companyName )
        {
            this.companyName = companyName;
        }

        public String getExchange()
        {
            return exchange;
        }

        public void setExchange( String exchange )
        {
            this.exchange = exchange;
        }

        public BigDecimal getLastPrice()
        {
            return lastPrice;
        }

        public void setLastPrice( BigDecimal lastPrice )
        {
            this.lastPrice = lastPrice;
        }

        public Timestamp getLastPriceChange()
        {
            return lastPriceChange;
        }

        public void setStockQuoteState( final StockQuoteState stockQuoteState )
        {
            this.stockQuoteState = stockQuoteState;
        }

        @Override
        public StockQuoteState getStockQuoteState()
        {
            return stockQuoteState;
        }

        public void setLastPriceChange( Timestamp lastPriceChange )
        {
            this.lastPriceChange = lastPriceChange;
        }

        public long getLastQuoteRefreshTime()
        {
            return lastQuoteRefreshTime;
        }

        public void setLastQuoteRefreshTime( long lastQuoteRefreshTime )
        {
            this.lastQuoteRefreshTime = lastQuoteRefreshTime;
        }

        @Override
        public String toString()
        {
            final StringBuilder sb = new StringBuilder( "CachedStockEntry{" );
            sb.append( "tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
            sb.append( ", companyName='" ).append( companyName ).append( '\'' );
            sb.append( ", exchange='" ).append( exchange ).append( '\'' );
            sb.append( ", lastPrice=" ).append( lastPrice );
            sb.append( ", lastPriceChange=" ).append( lastPriceChange );
            sb.append( ", stockQuoteState=" ).append( stockQuoteState );
            sb.append( ", lastQuoteRefreshTime=" ).append( lastQuoteRefreshTime );
            sb.append( '}' );
            return sb.toString();
        }
    }

    @Autowired
    public void setYahooStockService( final YahooStockService yahooStockService )
    {
        this.yahooStockService = yahooStockService;
    }

    @Autowired
    public void setIexTradingStockService( final IEXTradingStockService iexTradingStockService )
    {
        this.iexTradingStockService = iexTradingStockService;
    }

    @Autowired
    public void setStockService( final StockService stockService )
    {
        this.stockService = stockService;
    }
}
