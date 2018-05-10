package com.stocktracker.weblayer.dto;

import com.stocktracker.weblayer.dto.common.UuidDTO;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class StockCatalystEventDTO extends StockQuoteDTO
                                   implements UuidDTO,
                                              CustomerIdContainer
{
    private String customerId;
    private String catalystDate;
    private String catalystDesc;
    private Byte dateOrTimePeriod;
    private Byte timePeriod;
    private Short timePeriodYear;

    public String getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( String customerId )
    {
        this.customerId = customerId;
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
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockCatalystEventDTO{" );
        sb.append( "id=" ).append( super.getId() );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", tickerSymbol='" ).append( super.getTickerSymbol() ).append( '\'' );
        sb.append( ", catalystDate='" ).append( catalystDate ).append( '\'' );
        sb.append( ", catalystDesc='" ).append( catalystDesc ).append( '\'' );
        sb.append( ", dateOrTimePeriod=" ).append( dateOrTimePeriod );
        sb.append( ", timePeriod=" ).append( timePeriod );
        sb.append( ", timePeriodYear=" ).append( timePeriodYear );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }

}
