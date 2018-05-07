package com.stocktracker.repositorylayer.entity;

import com.stocktracker.servicelayer.service.stocks.TickerSymbolContainer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
@Entity
@Table( name = "stock_catalyst_event", schema = "stocktracker", catalog = "" )
public class StockCatalystEventEntity extends UUIDEntity implements TickerSymbolContainer
{
    private UUID customerUuid;
    private String tickerSymbol;
    private Timestamp catalystDate;
    private String catalystDesc;
    private Byte dateOrTimePeriod;
    private Byte timePeriod;
    private Short timePeriodYear;

    @Basic
    @Column( name = "customer_uuid", nullable = false )
    public UUID getCustomerUuid()
    {
        return customerUuid;
    }

    public void setCustomerUuid( final UUID customerUuid )
    {
        this.customerUuid = customerUuid;
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
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockCatalystEventEntity{" );
        sb.append( "uuid=" ).append( getUuidString() );
        sb.append( ", customerUuid=" ).append( customerUuid );
        sb.append( ", tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", catalystDate=" ).append( catalystDate );
        sb.append( ", catalystDesc='" ).append( catalystDesc ).append( '\'' );
        sb.append( ", dateOrTimePeriod=" ).append( dateOrTimePeriod );
        sb.append( ", timePeriod='" ).append( timePeriod ).append( '\'' );
        sb.append( ", timePeriodYear=" ).append( timePeriodYear );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
