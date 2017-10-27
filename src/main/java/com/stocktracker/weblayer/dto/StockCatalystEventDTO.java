package com.stocktracker.weblayer.dto;

import com.stocktracker.servicelayer.service.StockService;

public class StockCatalystEventDTO implements StockService.StockCompanyNameContainer
{
    private Integer id;
    private Integer customerId;
    private String tickerSymbol;
    private String companyName;
    private String catalystDate;
    private String catalystDesc;

    public static StockCatalystEventDTO newInstance()
    {
        return new StockCatalystEventDTO();
    }

    public Integer getId()
    {
        return id;
    }

    public void setId( Integer id )
    {
        this.id = id;
    }

    public Integer getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( Integer customerId )
    {
        this.customerId = customerId;
    }

    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    @Override
    public void setCompanyName( final String companyName )
    {
        this.companyName = companyName;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setTickerSymbol( String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    public String getCatalystDate()
    {
        return catalystDate;
    }

    public void setCatalystDate( String catalystDate )
    {
        this.catalystDate = catalystDate;
    }

    public String getCatalystDesc()
    {
        return catalystDesc;
    }

    public void setCatalystDesc( String catalystDesc )
    {
        this.catalystDesc = catalystDesc;
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

        final StockCatalystEventDTO that = (StockCatalystEventDTO) o;

        return id.equals( that.id );
    }

    @Override
    public int hashCode()
    {
        return id.hashCode();
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockAnalyticsDTO{" );
        sb.append( "id=" ).append( id );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", companyName='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", catalystDate=" ).append( catalystDate );
        sb.append( ", catalystDesc='" ).append( catalystDesc ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }
}
