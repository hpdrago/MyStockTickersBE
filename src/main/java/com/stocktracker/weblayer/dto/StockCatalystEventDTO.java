package com.stocktracker.weblayer.dto;

import com.stocktracker.weblayer.dto.common.CustomerIdContainer;
import com.stocktracker.weblayer.dto.common.DatabaseEntityDTO;
import com.stocktracker.weblayer.dto.common.StockPriceQuoteDTOContainer;
import com.stocktracker.weblayer.dto.common.StockQuoteDTOContainer;
import com.stocktracker.weblayer.dto.common.UuidDTO;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.beans.Transient;

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class StockCatalystEventDTO extends DatabaseEntityDTO<String>
                                   implements UuidDTO,
                                              CustomerIdContainer,
                                              StockQuoteDTOContainer,
                                              StockPriceQuoteDTOContainer
{
    private String tickerSymbol;
    private String customerId;
    private String catalystDate;
    private String catalystDesc;
    private Byte dateOrTimePeriod;
    private Byte timePeriod;
    private Short timePeriodYear;
    private StockQuoteDTO stockQuoteDTO;
    private StockPriceQuoteDTO stockPriceQuoteDTO;
    private boolean stockPriceQuoteRequested;
    private boolean stockQuoteRequested;


    @Override
    public void setStockQuoteDTO( final StockQuoteDTO stockQuoteDTO )
    {
        this.stockQuoteDTO = stockQuoteDTO;
    }

    @Override
    public StockQuoteDTO getStockQuoteDTO()
    {
        return this.stockQuoteDTO;
    }

    @Override
    public String getCacheKey()
    {
        return this.tickerSymbol;
    }

    @Override
    public void setCacheKey( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    public StockPriceQuoteDTO getStockPriceQuote()
    {
        return stockPriceQuoteDTO;
    }

    @Transient
    public void setQuoteRequested( final boolean requested )
    {
        this.stockQuoteRequested = requested;
    }

    @Transient
    public boolean isQuoteRequested()
    {
        return this.stockQuoteRequested;
    }

    @Override
    public void setStockPriceQuote( final StockPriceQuoteDTO stockPriceQuoteDTO )
    {
        this.stockPriceQuoteDTO = stockPriceQuoteDTO;
    }

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
    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    @Override
    public void setTickerSymbol( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockCatalystEventDTOEntity{" );
        sb.append( "tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", id='" ).append( super.getId() ).append( '\'' );
        sb.append( ", customerId='" ).append( customerId ).append( '\'' );
        sb.append( ", catalystDate='" ).append( catalystDate ).append( '\'' );
        sb.append( ", catalystDesc='" ).append( catalystDesc ).append( '\'' );
        sb.append( ", dateOrTimePeriod=" ).append( dateOrTimePeriod );
        sb.append( ", timePeriod=" ).append( timePeriod );
        sb.append( ", timePeriodYear=" ).append( timePeriodYear );
        sb.append( ", stockQuoteDTO='" ).append( stockQuoteDTO ).append( '\'' );
        sb.append( ", stockPriceQuoteDTO='" ).append( stockPriceQuoteDTO ).append( '\'' );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
