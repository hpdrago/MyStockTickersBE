package com.stocktracker.weblayer.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.stocktracker.common.JSONMoneySerializer;
import com.stocktracker.common.JSONTimestampDateTimeSerializer;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState;
import com.stocktracker.weblayer.dto.common.StockPriceQuoteDTOAsyncContainer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

/**
 * This class is used to return stock update information from the stock quote service.
 *
 * Created by mike on 12/10/2016.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class StockPriceQuoteDTO implements StockPriceQuoteDTOAsyncContainer
{
    private String tickerSymbol;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal lastPrice;
    @JsonSerialize( using = JSONTimestampDateTimeSerializer.class )
    private Timestamp lastPriceChange;
    private AsyncCacheEntryState stockPriceQuoteCacheState;
    @JsonSerialize( using = JSONTimestampDateTimeSerializer.class )
    private Timestamp expirationTime;
    private AsyncCacheEntryState cacheState;
    private String cacheError;

    @Override
    public void setCachedDTO( final StockPriceQuoteDTO dto )
    {
        BeanUtils.copyProperties( dto, this );
    }

    @Override
    public void setCacheState( final AsyncCacheEntryState stockQuoteEntityCacheState )
    {
        this.cacheState = stockQuoteEntityCacheState;
    }

    @Override
    public AsyncCacheEntryState getCacheState()
    {
        return this.cacheState;
    }

    @Override
    public void setCacheError( final String stockQuoteCacheError )
    {
        this.cacheError = stockQuoteCacheError;
    }

    @Override
    public String getCacheError()
    {
        return this.cacheError;
    }

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

    public Timestamp getLastPriceChange()
    {
        return lastPriceChange;
    }

    public void setLastPriceChange( Timestamp lastPriceChange )
    {
        this.lastPriceChange = lastPriceChange;
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
        sb.append( ", lastPrice=" ).append( lastPrice );
        sb.append( ", lastPriceChange=" ).append( lastPriceChange );
        sb.append( ", openPrice=" ).append( lastPrice );
        sb.append( ", stockPriceQuoteCacheState=" ).append( stockPriceQuoteCacheState );
        sb.append( ", expirationTime=" ).append( expirationTime );
        sb.append( ", cacheState=" ).append( cacheState );
        sb.append( ", cacheError=" ).append( cacheError );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
