package com.stocktracker.weblayer.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.stocktracker.common.JSONMoneySerializer;
import com.stocktracker.common.JSONTimestampDateTimeSerializer;
import com.stocktracker.servicelayer.service.cache.common.InformationCacheEntryState;
import com.stocktracker.servicelayer.service.cache.stockpricequote.StockPriceQuoteContainer;
import com.stocktracker.weblayer.dto.common.BaseDatabaseEntityDTO;

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
public abstract class BaseDatabaseEntityStockQuoteDTO extends BaseDatabaseEntityDTO
    implements StockPriceQuoteContainer
{
    private String tickerSymbol;
    private String companyName;
    private String sector;
    private String industry;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal lastPrice;
    @JsonSerialize( using = JSONTimestampDateTimeSerializer.class )
    private Timestamp lastPriceChange;
    private InformationCacheEntryState informationCacheEntryState;
    @JsonSerialize( using = JSONTimestampDateTimeSerializer.class )
    private Timestamp expirationTime;
    private BigDecimal openPrice;

    public InformationCacheEntryState getStockPriceCacheState()
    {
        return informationCacheEntryState;
    }

    public void setStockPriceCacheState( final InformationCacheEntryState informationCacheEntryState )
    {
        this.informationCacheEntryState = informationCacheEntryState;
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

    @Override
    public String getSector()
    {
        return this.sector;
    }

    @Override
    public void setSector( final String sector )
    {
        this.sector = sector;
    }

    @Override
    public String getIndustry()
    {
        return this.industry;
    }

    @Override
    public void setIndustry( final String industry )
    {
        this.industry = industry;
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
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockPriceQuoteEntity{" );
        sb.append( "tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", companyName'=" ).append( companyName ).append( '\'' );
        sb.append( ", lastPrice=" ).append( lastPrice );
        sb.append( ", lastPriceChange=" ).append( lastPriceChange );
        sb.append( ", openPrice=" ).append( lastPrice );
        sb.append( ", informationCacheEntryState=" ).append( informationCacheEntryState );
        sb.append( ", expirationTime=" ).append( expirationTime );
        sb.append( ", industry='" ).append( industry ).append( '\'' );
        sb.append( ", sector='" ).append( sector ).append( '\'' );
        sb.append( ", super=" ).append( sector ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }

}
