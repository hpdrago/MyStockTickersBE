package com.stocktracker.weblayer.dto;

import com.stocktracker.servicelayer.service.YahooStockService;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Created by mike on 9/11/2016.
 */
public class StockDTO implements YahooStockService.YahooStockContainer
{
    private String tickerSymbol;
    private String companyName;
    private String exchange;
    private BigDecimal lastPrice;
    private Timestamp lastPriceUpdate;
    private Timestamp lastPriceChange;
    private int createdBy;
    private boolean userEntered;

    public static StockDTO newInstance()
    {
        StockDTO stockDTO = new StockDTO();
        return stockDTO;
    }

    private StockDTO()
    {
    }

    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public void setTickerSymbol( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName( final String companyName )
    {
        this.companyName = companyName;
    }

    public String getExchange()
    {
        return exchange;
    }

    public void setExchange( final String exchange )
    {
        this.exchange = exchange;
    }

    public int getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy( int createdBy )
    {
        this.createdBy = createdBy;
    }

    public boolean isUserEntered()
    {
        return userEntered;
    }

    public void setUserEntered( boolean userEntered )
    {
        this.userEntered = userEntered;
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
        final StockDTO that = (StockDTO) o;
        return Objects.equals( tickerSymbol, that.tickerSymbol );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( tickerSymbol, companyName, exchange );
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockDTO" );
        sb.append( "{" );
        sb.append( "tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", companyName='" ).append( companyName ).append( '\'' );
        sb.append( ", lastPrice='" ).append( lastPrice ).append( '\'' );
        sb.append( ", lastPriceUpdate='" ).append( lastPriceUpdate ).append( '\'' );
        sb.append( ", lastPriceChange='" ).append( lastPriceChange ).append( '\'' );
        sb.append( ", exchange='" ).append( exchange ).append( '\'' );
        sb.append( ", createdBy='" ).append( createdBy ).append( '\'' );
        sb.append( ", userEntered='" ).append( userEntered ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }
}
