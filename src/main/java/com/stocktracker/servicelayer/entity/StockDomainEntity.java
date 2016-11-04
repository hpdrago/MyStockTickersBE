package com.stocktracker.servicelayer.entity;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by mike on 9/11/2016.
 */
public class StockDomainEntity
{
    private String tickerSymbol;
    private String companyName;
    private String exchange;
    private int createdBy;
    private char userEntered;
    private BigDecimal lastPrice;

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

    public char getUserEntered()
    {
        return userEntered;
    }

    public void setUserEntered( final char userEntered )
    {
        this.userEntered = userEntered;
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
        final StockDomainEntity that = (StockDomainEntity) o;
        return Objects.equals( tickerSymbol, that.tickerSymbol );
    }
    @Override
    public int hashCode()
    {
        return Objects.hash( tickerSymbol, companyName, exchange );
    }

    public BigDecimal getLastPrice()
    {
        return lastPrice;
    }

    public void setLastPrice( BigDecimal lastPrice )
    {
        this.lastPrice = lastPrice;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockDomainEntity" );
        sb.append( "@" );
        sb.append( hashCode() );
        sb.append( "{" );
        sb.append( "tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", companyName='" ).append( companyName ).append( '\'' );
        sb.append( ", exchange='" ).append( exchange ).append( '\'' );
        sb.append( ", createdBy='" ).append( createdBy ).append( '\'' );
        sb.append( ", userEntered='" ).append( userEntered ).append( '\'' );
        sb.append( ", lastPrice='" ).append( userEntered ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }
}
