package com.stocktracker.weblayer.dto;

import java.util.Objects;

/**
 * Created by mike on 9/11/2016.
 */
public class StockDTO
{
    private String tickerSymbol;
    private String companyName;
    private String exchange;
    private int createdBy;
    private char userEntered;

    public static StockDTO newInstance(  )
    {
        StockDTO fragment = new StockDTO();
        return fragment;
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

    public char getUserEntered()
    {
        return userEntered;
    }

    public void setUserEntered( char userEntered )
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
        sb.append( "@" );
        sb.append( hashCode() );
        sb.append( "{" );
        sb.append( "tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", companyName='" ).append( companyName ).append( '\'' );
        sb.append( ", exchange='" ).append( exchange ).append( '\'' );
        sb.append( ", createdBy='" ).append( createdBy ).append( '\'' );
        sb.append( ", userEntered='" ).append( userEntered ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }
}
