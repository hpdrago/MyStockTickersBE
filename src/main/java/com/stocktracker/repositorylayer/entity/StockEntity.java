package com.stocktracker.repositorylayer.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Created by mike on 9/11/2016.
 */
@Entity
@Table( name = "stock", schema = "stocktracker", catalog = "" )
public class StockEntity
{
    private String tickerSymbol;
    private String companyName;
    //private String exchange;
    private Integer createdBy;
    private Character userEntered;
    private BigDecimal lastPrice;
    private Timestamp lastPriceUpdate;
    private Timestamp lastPriceChange;
    private String stockExchange;
    private Timestamp createDate;
    private Timestamp updateDate;
    private String quoteUrl;
    private String sector;
    private String industry;

    /*
    @Basic
    @Column( name = "exchange", nullable = true, length = 10 )
    public String getExchange()
    {
        return exchange;
    }

    public void setExchange( final String exchange )
    {
        this.exchange = exchange;
    }
    */
    private CustomerEntity customerByCreatedBy;

    /**
     * Create a new stock entity instance
     * @return
     */
    public static StockEntity newInstance()
    {
        StockEntity stockEntity = new StockEntity();
        return stockEntity;
    }

    public void setUserEntered( final String userEntered )
    {
        if ( userEntered == null )
        this.userEntered = userEntered == null ? 'N' : userEntered.equals( "Y" ) ? 'Y' : 'N';
    }

    @Id
    @Column( name = "ticker_symbol", nullable = false, length = 5 )
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
    @Column( name = "created_by" )
    public Integer getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy( Integer createdBy )
    {
        this.createdBy = createdBy;
    }

    @Basic
    @Column( name = "user_entered", nullable = true, length = 1 )
    public Character getUserEntered()
    {
        return userEntered;
    }

    public void setUserEntered( final Character downloaded )
    {
        this.userEntered = downloaded;
    }

    @Basic
    @Column( name = "last_price", nullable = true, precision = 2 )
    public BigDecimal getLastPrice()
    {
        return lastPrice;
    }

    public void setLastPrice( final BigDecimal lastPrice )
    {
        this.lastPrice = lastPrice;
    }

    /**
     * The last time the lastPrice was updated in the stock table
     */
    @Basic
    @Column( name = "last_price_update", nullable = true )
    public Timestamp getLastPriceUpdate()
    {
        return lastPriceUpdate;
    }

    public void setLastPriceUpdate( final Timestamp createDate )
    {
        this.lastPriceUpdate = createDate;
    }

    /**
     * The last time the stock price changed
     */
    @Basic
    @Column( name = "last_price_change", nullable = true )
    public Timestamp getLastPriceChange()
    {
        return lastPriceChange;
    }

    public void setLastPriceChange( Timestamp lastPriceChange )
    {
        this.lastPriceChange = lastPriceChange;
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
        final StockEntity that = (StockEntity) o;
        return Objects.equals( tickerSymbol, that.tickerSymbol ) &&
            Objects.equals( companyName, that.companyName );// &&
            //Objects.equals( exchange, that.exchange );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( tickerSymbol, companyName ); //, exchange );
    }

    @Basic
    @Column( name = "stock_exchange", nullable = true, length = 10 )
    public String getStockExchange()
    {
        return stockExchange;
    }

    public void setStockExchange( final String stockExchange )
    {
        this.stockExchange = stockExchange;
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
    @Column( name = "quote_url", nullable = true, length = 120 )
    public String getQuoteUrl()
    {
        return quoteUrl;
    }

    public void setQuoteUrl( final String quoteUrl )
    {
        this.quoteUrl = quoteUrl;
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
    @Column( name = "update_date", nullable = true )
    public Timestamp getUpdateDate()
    {
        return updateDate;
    }

    public void setUpdateDate( final Timestamp updateDate )
    {
        this.updateDate = updateDate;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockEntity{" );
        sb.append( "tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", companyName='" ).append( companyName ).append( '\'' );
        sb.append( ", createdBy=" ).append( createdBy );
        sb.append( ", userEntered=" ).append( userEntered );
        sb.append( ", lastPrice=" ).append( lastPrice );
        sb.append( ", lastPriceUpdate=" ).append( lastPriceUpdate );
        sb.append( ", lastPriceChange=" ).append( lastPriceChange );
        sb.append( ", stockExchange='" ).append( stockExchange ).append( '\'' );
        sb.append( ", createDate=" ).append( createDate );
        sb.append( ", updateDate=" ).append( updateDate );
        sb.append( ", quoteUrl='" ).append( quoteUrl ).append( '\'' );
        sb.append( ", sector='" ).append( sector ).append( '\'' );
        sb.append( ", industry='" ).append( industry ).append( '\'' );
        sb.append( ", customerByCreatedBy=" ).append( customerByCreatedBy );
        sb.append( '}' );
        return sb.toString();
    }
}
