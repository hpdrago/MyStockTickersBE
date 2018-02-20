package com.stocktracker.weblayer.dto;

import com.stocktracker.servicelayer.service.StockQuoteService;

public class StockCatalystEventDTO implements StockQuoteService.StockCompanyNameContainer,
                                              VersionedDTO<Integer>
{
    private Integer id;
    private Integer customerId;
    private String tickerSymbol;
    private String companyName;
    private String catalystDate;
    private String catalystDesc;
    private Byte dateOrTimePeriod;
    private Byte timePeriod;
    private Short timePeriodYear;
    private Integer version;

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

    public Byte getDateOrTimePeriod()
    {
        return dateOrTimePeriod;
    }

    public void setDateOrTimePeriod( final Byte dateOrTimePeriod )
    {
        this.dateOrTimePeriod = dateOrTimePeriod;
    }

    public Byte getTimePeriod()
    {
        return timePeriod;
    }

    public void setTimePeriod( final Byte timePeriod )
    {
        this.timePeriod = timePeriod;
    }

    public Short getTimePeriodYear()
    {
        return timePeriodYear;
    }

    public void setTimePeriodYear( final Short timePeriodYear )
    {
        this.timePeriodYear = timePeriodYear;
    }


    @Override
    public Integer getVersion()
    {
        return version;
    }

    public void setVersion( final Integer version )
    {
        this.version = version;
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
        final StringBuilder sb = new StringBuilder( "StockCatalystEventDTO{" );
        sb.append( "id=" ).append( id );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", companyName='" ).append( companyName ).append( '\'' );
        sb.append( ", catalystDate='" ).append( catalystDate ).append( '\'' );
        sb.append( ", catalystDesc='" ).append( catalystDesc ).append( '\'' );
        sb.append( ", dateOrTimePeriod=" ).append( dateOrTimePeriod );
        sb.append( ", timePeriod=" ).append( timePeriod );
        sb.append( ", timePeriodYear=" ).append( timePeriodYear );
        sb.append( ", version=" ).append( version );
        sb.append( '}' );
        return sb.toString();
    }

}
