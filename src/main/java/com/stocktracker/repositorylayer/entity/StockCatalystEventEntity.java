package com.stocktracker.repositorylayer.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table( name = "stock_catalyst_event", schema = "stocktracker", catalog = "" )
public class StockCatalystEventEntity
{
    private Integer id;
    private Integer customerId;
    private String tickerSymbol;
    private Timestamp catalystDate;
    private String catalystDesc;
    private Timestamp createDate;
    private Timestamp updateDate;

    public static StockCatalystEventEntity newInstance()
    {
        return new StockCatalystEventEntity();
    }

    @Id
    @Column( name = "id", nullable = false )
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    public Integer getId()
    {
        return id;
    }

    public void setId( final Integer id )
    {
        this.id = id;
    }

    @Basic
    @Column( name = "customer_id" )
    public Integer getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( final Integer customerId )
    {
        this.customerId = customerId;
    }

    @Basic
    @Column( name = "ticker_symbol" )
    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public void setTickerSymbol( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    @Basic
    @Column( name = "catalyst_date", nullable = false )
    public Timestamp getCatalystDate()
    {
        return catalystDate;
    }

    public void setCatalystDate( final Timestamp catalystDate )
    {
        this.catalystDate = catalystDate;
    }

    @Basic
    @Column( name = "catalyst_desc", nullable = false, length = 60 )
    public String getCatalystDesc()
    {
        return catalystDesc;
    }

    public void setCatalystDesc( final String catalystDesc )
    {
        this.catalystDesc = catalystDesc;
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

    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !(o instanceof StockCatalystEventEntity) )
        {
            return false;
        }
        final StockCatalystEventEntity that = (StockCatalystEventEntity) o;
        return Objects.equals( id, that.id );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( id );
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockAnalystConsensusEntity{" );
        sb.append( "id=" ).append( id );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", catalystDate=" ).append( catalystDate );
        sb.append( ", catalystDesc='" ).append( catalystDesc ).append( '\'' );
        sb.append( ", createDate=" ).append( createDate );
        sb.append( ", updateDate=" ).append( updateDate );
        sb.append( '}' );
        return sb.toString();
    }

}