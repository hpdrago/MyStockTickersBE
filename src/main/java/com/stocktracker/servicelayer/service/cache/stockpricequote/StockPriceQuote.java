package com.stocktracker.servicelayer.service.cache.stockpricequote;

import com.stocktracker.servicelayer.service.cache.common.AsyncCacheData;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.beans.Transient;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

/**
 * This class contains the information for a Stock Price Quote.
 * Information is gathered about the stock company from IEXTrading and some information from a Quote from IEXTrading
 * along with the most recent (last price) stock price.
 *
 * Created by mike on 12/10/2016.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class StockPriceQuote implements AsyncCacheData
{
    private String tickerSymbol;
    private BigDecimal lastPrice;
    private Timestamp lastPriceChange;
    private Timestamp expirationTime;
    private String cacheError;
    private AsyncCacheEntryState cacheState;

    public String getTickerSymbol()
    {
        return tickerSymbol;
    }
    public void setTickerSymbol( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    public BigDecimal getLastPrice()
    {
        return lastPrice;
    }
    public void setLastPrice( final BigDecimal lastPrice )
    {
        this.lastPrice = lastPrice;// .divide( new BigDecimal( 1 ), 2,  BigDecimal.ROUND_HALF_UP  );
        if ( lastPrice != null )
        {
            lastPrice.setScale( 2, RoundingMode.HALF_UP );
        }
    }

    public Timestamp getExpirationTime()
    {
        return expirationTime;
    }
    public void setExpirationTime( final Timestamp expirationTime )
    {
        this.expirationTime = expirationTime;
    }

    public Timestamp getLastPriceChange()
    {
        return lastPriceChange;
    }

    public void setLastPriceChange( final Timestamp lastPriceChange )
    {
        this.lastPriceChange = lastPriceChange;
    }

    public String getCacheError()
    {
        return cacheError;
    }

    public void setCacheError( final String cacheError )
    {
        this.cacheError = cacheError;
    }

    public AsyncCacheEntryState getCacheState()
    {
        return cacheState;
    }

    public void setCacheState( final AsyncCacheEntryState cacheState )
    {
        this.cacheState = cacheState;
    }

    /**
     * Get the expiration time.
     * @return
     */
    @Override
    @Transient
    public Timestamp getExpiration()
    {
        return this.expirationTime;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockPriceQuote{" );
        sb.append( "tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", lastPrice=" ).append( lastPrice );
        sb.append( ", lastPriceChange=" ).append( lastPriceChange );
        sb.append( ", expirationTime=" ).append( expirationTime );
        sb.append( ", cacheState='" ).append( cacheState ).append( '\'' );
        sb.append( ", cacheError='" ).append( cacheError ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }
}
