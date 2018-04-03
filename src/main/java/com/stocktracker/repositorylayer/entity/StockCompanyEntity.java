package com.stocktracker.repositorylayer.entity;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.Objects;

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
@Entity
@Table( name = "stock_company", schema = "stocktracker", catalog = "" )
public class StockCompanyEntity implements VersionedEntity<String>
{
    public static final int TICKER_SYMBOL_LEN = 6;
    private String tickerSymbol;
    private String companyName;
    private String companyURL;
    private String sector;
    private String industry;
    private String discontinuedInd;
    private Timestamp createDate;
    private Timestamp updateDate;
    private Integer version;

    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Id
    @Column( name = "ticker_symbol", nullable = false, length = 25 )
    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public void setTickerSymbol( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    @Basic
    @Column( name = "company_name", nullable = true, length = 70 )
    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName( final String companyName )
    {
        this.companyName = companyName;
    }

    @Basic
    @Column( name = "company_url", nullable = true, length = 120 )
    public String getCompanyURL()
    {
        return companyURL;
    }

    public void setCompanyURL( final String quoteUrl )
    {
        this.companyURL = quoteUrl;
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

    @Basic
    @Column( name = "create_date", nullable = false )
    public Timestamp getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate( final Timestamp createDate )
    {
        this.createDate = createDate;
    }

    @Basic
    @Column( name = "update_date", nullable = true )
    public Timestamp getUpdateDate()
    {
        return updateDate;
    }

    public void setUpdateDate( final Timestamp updateDate )
    {
        this.updateDate = updateDate;
    }

    @Transient
    @Override
    public String getId()
    {
        return this.tickerSymbol;
    }

    @Basic
    @Column( name = "version", nullable = false )
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
        final StockCompanyEntity that = (StockCompanyEntity) o;
        return Objects.equals( tickerSymbol, that.tickerSymbol );
    }

    @Override
    public int hashCode()
    {
        return Objects
            .hash( tickerSymbol, companyName, companyURL, sector, industry, discontinuedInd, createDate, updateDate, version );
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockCompanyEntity{" );
        sb.append( "tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", companyName='" ).append( companyName ).append( '\'' );
        sb.append( ", quoteUrl='" ).append( companyURL ).append( '\'' );
        sb.append( ", sector='" ).append( sector ).append( '\'' );
        sb.append( ", industry='" ).append( industry ).append( '\'' );
        sb.append( ", discontinuedInd='" ).append( discontinuedInd ).append( '\'' );
        sb.append( ", createDate=" ).append( createDate );
        sb.append( ", updateDate=" ).append( updateDate );
        sb.append( ", version=" ).append( version );
        sb.append( '}' );
        return sb.toString();
    }
}
