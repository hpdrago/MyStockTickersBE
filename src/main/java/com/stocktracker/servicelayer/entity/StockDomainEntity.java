package com.stocktracker.servicelayer.entity;

import java.util.Objects;

/**
 * Created by mike on 9/11/2016.
 */
public class StockDomainEntity
{
    private String tickerSymbol;
    private String companyName;
    private String exchange;

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
        return Objects.equals( tickerSymbol, that.tickerSymbol ) &&
            Objects.equals( companyName, that.companyName ) &&
            Objects.equals( exchange, that.exchange );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( tickerSymbol, companyName, exchange );
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
        sb.append( '}' );
        return sb.toString();
    }
}
