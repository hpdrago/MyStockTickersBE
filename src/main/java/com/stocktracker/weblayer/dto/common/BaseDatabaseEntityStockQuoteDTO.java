package com.stocktracker.weblayer.dto.common;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.stocktracker.common.JSONMoneySerializer;
import com.stocktracker.common.JSONTimestampDateTimeSerializer;
import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState;
import com.stocktracker.servicelayer.service.cache.stockpricequote.StockQuoteContainer;
import com.stocktracker.servicelayer.service.cache.stockquote.StockQuoteEntityCacheClient;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

/**
 * This is the base class for DTO's that are derived from database entities and contain stock quote information.
 * This class contains the common properties and methods for the combination of these two data containers.
 *
 * This base DTO, basically is a duplicate of StockPriceQuoteDTO, but they are used for different purposes and the
 * content can now be changed in either context without affecting the other.
 */
public abstract class BaseDatabaseEntityStockQuoteDTO<K extends Serializable>
    extends BaseDatabaseEntityDTO<K>
    implements StockQuoteContainer,
               StockQuoteEntityCacheClient
{
    private String tickerSymbol;
    private String companyName;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal lastPrice;
    @JsonSerialize( using = JSONTimestampDateTimeSerializer.class )
    private Timestamp lastPriceChange;
    @JsonSerialize( using = JSONTimestampDateTimeSerializer.class )
    private Timestamp expirationTime;
    private BigDecimal openPrice;
    private AsyncCacheEntryState stockPriceQuoteCacheState;
    private AsyncCacheEntryState stockQuoteCacheState;

    public void setStockQuoteEntity( final StockQuoteEntity stockQuoteEntity )
    {
        BeanUtils.copyProperties( stockQuoteEntity, this );
    }

    @Override
    public AsyncCacheEntryState getStockQuoteCacheState()
    {
        return stockQuoteCacheState;
    }

    @Override
    public void setStockQuoteCacheState( final AsyncCacheEntryState stockQuoteCacheState )
    {
        this.stockQuoteCacheState = stockQuoteCacheState;
    }

    public AsyncCacheEntryState getStockPriceQuoteCacheState()
    {
        return stockPriceQuoteCacheState;
    }

    public void setStockPriceQuoteCacheState( final AsyncCacheEntryState stockPriceQuoteCacheState )
    {
        this.stockPriceQuoteCacheState = stockPriceQuoteCacheState;
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

    public AsyncCacheEntryState getStockPrideQuoteCacheState()
    {
        return this.stockPriceQuoteCacheState;
    }

    public void setStockPrideQuoteCacheState( final AsyncCacheEntryState stockPrideQuoteCacheState )
    {
        this.stockPriceQuoteCacheState = stockPrideQuoteCacheState;
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
        sb.append( ", stockPriceCacheState=" ).append( stockPriceQuoteCacheState );
        sb.append( ", expirationTime=" ).append( expirationTime );
        sb.append( ", stockPriceQuoteCacheState='" ).append( stockPriceQuoteCacheState ).append( '\'' );
        sb.append( ", super=" ).append( super.toString() ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }

}
