package com.stocktracker.repositorylayer.entity;

import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDBEntity;
import com.stocktracker.servicelayer.service.stocks.StockCompanyContainer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
@Entity
@Table( name = "stock_company", schema = "stocktracker", catalog = "" )
public class StockCompanyEntity extends TickerSymbolEntity
                                implements StockCompanyContainer,
                                           AsyncCacheDBEntity<String>
{
    private String companyName;
    private String website;
    private String sector;
    private String industry;
    private String discontinuedInd;

    @Basic
    @Column( name = "company_name", nullable = false, length = 70 )
    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName( final String companyName )
    {
        if ( companyName == null )
        {
            this.companyName = null;
        }
        else if ( companyName.length() > 70 )
        {
            this.companyName = companyName.substring( 0, 70 );
        }
        else
        {
            this.companyName = companyName;
        }
    }

    @Basic
    @Column( name = "website", nullable = true, length = 120 )
    public String getWebsite()
    {
        return website;
    }

    public void setWebsite( final String quoteUrl )
    {
        this.website = quoteUrl;
    }

    @Basic
    @Column( name = "sector", nullable = true, length = 45 )
    public String getSector()
    {
        return sector;
    }

    public void setSector( final String sector )
    {
        this.sector = sector;
    }

    @Basic
    @Column( name = "industry", nullable = true, length = 45 )
    public String getIndustry()
    {
        return industry;
    }

    public void setIndustry( final String industry )
    {
        this.industry = industry;
    }

    @Basic
    @Column( name = "discontinued_ind", nullable = true, length = 1 )
    public String getDiscontinuedInd()
    {
        return discontinuedInd;
    }

    public void setDiscontinuedInd( final String discontinuedInd )
    {
        this.discontinuedInd = discontinuedInd;
    }

    @Transient
    public void setDiscontinuedInd( final boolean discontinuedInd )
    {
        this.discontinuedInd = discontinuedInd ? "Y" : "N";
    }

    @Transient
    public boolean isDiscontinued()
    {
        return this.discontinuedInd == null ? false : this.discontinuedInd.equalsIgnoreCase( "Y" );
    }

    @Transient
    @Override
    public Timestamp getExpiration()
    {
        return new Timestamp( this.getUpdateDate().getTime() + TimeUnit.DAYS.convert( 365, TimeUnit.MILLISECONDS ));
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockCompanyEntity{" );
        sb.append( "tickerSymbol='" ).append( super.getTickerSymbol() ).append( '\'' );
        sb.append( ", companyName='" ).append( companyName ).append( '\'' );
        sb.append( ", quoteUrl='" ).append( website ).append( '\'' );
        sb.append( ", sector='" ).append( sector ).append( '\'' );
        sb.append( ", industry='" ).append( industry ).append( '\'' );
        sb.append( ", discontinuedInd='" ).append( discontinuedInd ).append( '\'' );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
