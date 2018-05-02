package com.stocktracker.servicelayer.stockinformationprovider;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.stocktracker.common.JSONMoneySerializer;
import com.stocktracker.common.JSONTimestampDateTimeSerializer;
import com.stocktracker.servicelayer.service.stocks.StockCompanyContainer;
import com.stocktracker.servicelayer.service.stocks.StockOpenPriceContainer;
import com.stocktracker.servicelayer.service.stocks.StockPriceContainer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * This class is used to return stock update information from the stock quote service
 *
 * Created by mike on 12/10/2016.
 */
public class StockPriceQuoteDTO implements StockPriceContainer,
                                           StockOpenPriceContainer,
                                           StockCompanyContainer
{
    private String tickerSymbol;
    private String companyName;
    private String sector;
    private String industry;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal lastPrice;
    @JsonSerialize( using = JSONTimestampDateTimeSerializer.class )
    private Timestamp lastPriceChange;
    private StockPriceCacheState stockPriceCacheState;
    @JsonSerialize( using = JSONTimestampDateTimeSerializer.class )
    private Timestamp expirationTime;
    private BigDecimal openPrice;

    public StockPriceCacheState getStockPriceCacheState()
    {
        return stockPriceCacheState;
    }

    public void setStockPriceCacheState( final StockPriceCacheState stockPriceCacheState )
    {
        this.stockPriceCacheState = stockPriceCacheState;
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
        final StringBuilder sb = new StringBuilder( "StockPriceQuote{" );
        sb.append( "tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", companyName'=" ).append( companyName ).append( '\'' );
        sb.append( ", lastPrice=" ).append( lastPrice );
        sb.append( ", lastPriceChange=" ).append( lastPriceChange );
        sb.append( ", openPrice=" ).append( lastPrice );
        sb.append( ", stockPriceCacheState=" ).append( stockPriceCacheState );
        sb.append( ", expirationTime=" ).append( expirationTime );
        sb.append( ", industry'=" ).append( industry ).append( '\'' );
        sb.append( ", sector'=" ).append( sector ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }
}
