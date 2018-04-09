package com.stocktracker.weblayer.dto;

import com.stocktracker.servicelayer.service.stocks.StockCompanyContainer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * A base class containing the common stock company properties.
 * Makes it easy to extend a DTO class that contains stock company information.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class BaseStockCompanyDTO implements StockCompanyContainer
{
    private String tickerSymbol;
    private String companyName;
    private String companyUrl;
    private String sector;
    private String industry;

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

    public String getCompanyUrl()
    {
        return companyUrl;
    }

    public void setCompanyUrl( final String companyUrl )
    {
        this.companyUrl = companyUrl;
    }

    public String getSector()
    {
        return sector;
    }

    public void setSector( final String sector )
    {
        this.sector = sector;
    }

    public String getIndustry()
    {
        return industry;
    }

    public void setIndustry( final String industry )
    {
        this.industry = industry;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockCompanyDTO{" );
        sb.append( "tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", companyName='" ).append( companyName ).append( '\'' );
        sb.append( ", companyUrl='" ).append( companyUrl ).append( '\'' );
        sb.append( ", sector='" ).append( sector ).append( '\'' );
        sb.append( ", industry='" ).append( industry ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }

}
