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
    private Byte dateOrTimePeriod;
    private Byte timePeriod;
    private Short timePeriodYear;
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
    @Column( name = "customer_id", nullable = false )
    public Integer getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( final Integer customerId )
    {
        this.customerId = customerId;
    }

    @Basic
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
    @Column( name = "catalyst_date", nullable = true )
    public Timestamp getCatalystDate()
    {
        return catalystDate;
    }

    public void setCatalystDate( final Timestamp catalystDate )
    {
        this.catalystDate = catalystDate;
    }

    @Basic
    @Column( name = "catalyst_desc", nullable = false, length = 1000 )
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


    @Basic
    @Column( name = "date_or_time_period", nullable = false )
    public Byte getDateOrTimePeriod()
    {
        return dateOrTimePeriod;
    }

    public void setDateOrTimePeriod( final Byte dateOrTimePeriod )
    {
        this.dateOrTimePeriod = dateOrTimePeriod;
    }

    @Basic
    @Column( name = "time_period", nullable = true, length = 10 )
    public Byte getTimePeriod()
    {
        return timePeriod;
    }

    public void setTimePeriod( final Byte timePeriod )
    {
        this.timePeriod = timePeriod;
    }

    @Basic
    @Column( name = "time_period_year", nullable = true )
    public Short getTimePeriodYear()
    {
        return timePeriodYear;
    }

    public void setTimePeriodYear( final Short timePeriodYear )
    {
        this.timePeriodYear = timePeriodYear;
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
        final StringBuilder sb = new StringBuilder( "StockCatalystEventEntity{" );
        sb.append( "id=" ).append( id );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", catalystDate=" ).append( catalystDate );
        sb.append( ", catalystDesc='" ).append( catalystDesc ).append( '\'' );
        sb.append( ", createDate=" ).append( createDate );
        sb.append( ", updateDate=" ).append( updateDate );
        sb.append( ", dateOrTimePeriod=" ).append( dateOrTimePeriod );
        sb.append( ", timePeriod='" ).append( timePeriod ).append( '\'' );
        sb.append( ", timePeriodYear=" ).append( timePeriodYear );
        sb.append( '}' );
        return sb.toString();
    }
}
