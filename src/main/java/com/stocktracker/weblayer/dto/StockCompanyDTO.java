package com.stocktracker.weblayer.dto;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class StockCompanyDTO implements VersionedDTO<String>
{
    private String tickerSymbol;
    private String companyName;
    private String quoteUrl;
    private String sector;
    private String industry;
    private Integer version;

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

    public String getQuoteUrl()
    {
        return quoteUrl;
    }

    public void setQuoteUrl( final String quoteUrl )
    {
        this.quoteUrl = quoteUrl;
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
    public String getId()
    {
        return this.tickerSymbol;
    }

    @Override
    public Integer getVersion()
    {
        return version;
    }

    @Override
    public void setVersion( final Integer version )
    {
        this.version = version;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockCompanyDTO{" );
        sb.append( "tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", companyName='" ).append( companyName ).append( '\'' );
        sb.append( ", quoteUrl='" ).append( quoteUrl ).append( '\'' );
        sb.append( ", sector='" ).append( sector ).append( '\'' );
        sb.append( ", industry='" ).append( industry ).append( '\'' );
        sb.append( ", version=" ).append( version );
        sb.append( '}' );
        return sb.toString();
    }

}
