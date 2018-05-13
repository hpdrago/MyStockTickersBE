package com.stocktracker.weblayer.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.stocktracker.common.JSONMoneySerializer;
import com.stocktracker.common.JSONTimestampDateTimeSerializer;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState;
import com.stocktracker.servicelayer.service.cache.stockpricequote.StockPriceQuote;
import com.stocktracker.servicelayer.service.stocks.StockPriceQuoteContainer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * This class is used to return stock update information from the stock quote service.
 *
 * Created by mike on 12/10/2016.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class StockPriceQuoteDTO implements StockPriceQuoteContainer
{
    private String tickerSymbol;
    private String companyName;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal lastPrice;
    @JsonSerialize( using = JSONTimestampDateTimeSerializer.class )
    private Timestamp lastPriceChange;
    private AsyncCacheEntryState stockPriceQuoteCacheState;
    @JsonSerialize( using = JSONTimestampDateTimeSerializer.class )
    private Timestamp expirationTime;
    private BigDecimal openPrice;


    public String getTickerSymbol()
    {
        return tickerSymbol;
    }
    public void setTickerSymbol( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    @Override
    public void setStockPriceQuoteCacheState( final AsyncCacheEntryState stockPriceQuoteCacheState )
    {
        this.stockPriceQuoteCacheState = stockPriceQuoteCacheState;
    }

    @Override
    public AsyncCacheEntryState getStockPriceQuoteCacheState()
    {
        return this.stockPriceQuoteCacheState;
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

    public Timestamp getLastPriceChange()
    {
        return lastPriceChange;
    }

    public void setLastPriceChange( Timestamp lastPriceChange )
    {
        this.lastPriceChange = lastPriceChange;
    }

    public void setOpenPrice( final BigDecimal openPrice )
    {
        this.openPrice = openPrice;
    }

    public BigDecimal getOpenPrice()
    {
        return this.openPrice;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName( final String companyName )
    {
        this.companyName = companyName;
    }

    public Timestamp getExpirationTime()
    {
        return expirationTime;
    }

    public void setExpirationTime( final Timestamp expirationTime )
    {
        this.expirationTime = expirationTime;
    }

    @Override
    public void setStockPriceQuote( final AsyncCacheEntryState cacheState, final StockPriceQuote stockPriceQuote )
    {

    }

    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }
        final StockPriceQuoteDTO that = (StockPriceQuoteDTO) o;
        return Objects.equals( tickerSymbol, that.tickerSymbol );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( tickerSymbol );
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockPriceQuoteEntity{" );
        sb.append( "tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", companyName'=" ).append( companyName ).append( '\'' );
        sb.append( ", lastPrice=" ).append( lastPrice );
        sb.append( ", lastPriceChange=" ).append( lastPriceChange );
        sb.append( ", openPrice=" ).append( lastPrice );
        sb.append( ", stockPriceQuoteCacheState=" ).append( stockPriceQuoteCacheState );
        sb.append( ", expirationTime=" ).append( expirationTime );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
