package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yahoofinance.Stock;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * This cache holds stocks that will expire after the set amount of time
 */
@Service
public class StockCache implements MyLogger
{
    private static final long EXPIRATION_TIME = 5 * 60 * 1000; // 5 min
    private Map<String, CachedStockEntry> cacheEntryMap = new HashMap<>();
    private YahooStockService yahooStockService;

    /**
     * Get the stock quote information for the {@code tickerSymbol}
     * @param tickerSymbol
     * @return
     */
    public Optional<CachedStock> getStock( @NotNull String tickerSymbol )
    {
        final String methodName = "getStock";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        CachedStockEntry cachedStockEntry = this.cacheEntryMap.get( tickerSymbol );
        if ( cachedStockEntry == null )
        {
            cachedStockEntry = getStockFromYahoo( tickerSymbol );
        }
        else
        {
            if ( System.currentTimeMillis() - cachedStockEntry.lastUpdateTime > EXPIRATION_TIME )
            {
                logDebug( methodName, "{0} is stale", tickerSymbol );
                cachedStockEntry = getStockFromYahoo( tickerSymbol );
            }
        }
        logMethodEnd( methodName, cachedStockEntry );
        return Optional.ofNullable( cachedStockEntry );
    }

    /**
     * Using the yahooStockService, call to get the latest stock quote for the {@code tickerSymbol}
     * @param tickerSymbol
     * @return
     */
    private CachedStockEntry getStockFromYahoo( final @NotNull String tickerSymbol )
    {
        final String methodName = "getStockFromYahoo";
        logMethodBegin( methodName, tickerSymbol );
        CachedStockEntry cachedStockEntry = null;
        try
        {
            Stock stock = this.yahooStockService.getStock( tickerSymbol );
            if ( stock == null )
            {
                logInfo( methodName, "Yahoo doesn't recognize {0}" );
            }
            else
            {
                cachedStockEntry = new CachedStockEntry( stock );
                this.cacheEntryMap.put( tickerSymbol, cachedStockEntry );
            }
        }
        catch ( IOException e )
        {
            logError( methodName, e );
            cachedStockEntry = this.cacheEntryMap.get( tickerSymbol );
        }
        logMethodEnd( methodName, cachedStockEntry );
        return cachedStockEntry;
    }

    @Autowired
    public void setStockService( final YahooStockService yahooStockService )
    {
        this.yahooStockService = yahooStockService;
    }

    /**
     * This interface defines the methods to extract the values from a cached stock
     */
    public interface CachedStock
    {
        String getTickerSymbol();
        String getCompanyName();
        String getExchange();
        BigDecimal getLastPrice();
        Timestamp getLastPriceChange();
    }

    private class CachedStockEntry implements CachedStock
    {
        private String tickerSymbol;
        private String companyName;
        private String exchange;
        private BigDecimal lastPrice;
        private Timestamp lastPriceChange;
        private long lastUpdateTime;

        public CachedStockEntry( final Stock yahooStock )
        {
            this.lastUpdateTime = System.currentTimeMillis();
            this.tickerSymbol = yahooStock.getSymbol();
            this.companyName = yahooStock.getName();
            this.exchange = yahooStock.getStockExchange();

            if ( yahooStock.getQuote().getLastTradeTime() != null )
            {
                this.lastPriceChange = ( new Timestamp( yahooStock.getQuote().getLastTradeTime().getTimeInMillis() ) );
            }
            this.lastPrice = yahooStock.getQuote().getPrice();
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

        public void setLastPriceChange( Timestamp lastPriceChange )
        {
            this.lastPriceChange = lastPriceChange;
        }

        public long getLastUpdateTime()
        {
            return lastUpdateTime;
        }

        public void setLastUpdateTime( long lastUpdateTime )
        {
            this.lastUpdateTime = lastUpdateTime;
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
            sb.append( ", lastUpdateTime=" ).append( lastUpdateTime );
            sb.append( '}' );
            return sb.toString();
        }
    }
}
