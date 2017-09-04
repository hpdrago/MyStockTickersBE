package com.stocktracker.repositorylayer.entity;

import com.stocktracker.repositorylayer.common.BooleanUtils;
import com.stocktracker.servicelayer.entity.StockDE;
import org.springframework.beans.BeanUtils;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Created by mike on 9/11/2016.
 */
@Entity
@Table( name = "stock", schema = "stocktracker" )
public class StockEntity extends BaseDBEntity<StockEntity, StockDE>
{
    private String tickerSymbol;
    private String companyName;
    //private String exchange;
    private Integer createdBy;
    private Character userEntered;
    private BigDecimal lastPrice;
    private Timestamp lastPriceUpdate;
    private Timestamp lastPriceChange;

    /**
     * Create a new stock entity instance from a stock DE
     * @param stockDE
     * @return
     */
    public static StockEntity newInstance( final StockDE stockDE )
    {
        Objects.requireNonNull( stockDE );
        StockEntity stockEntity = new StockEntity();
        BeanUtils.copyProperties( stockDE, stockEntity );
        stockEntity.setUserEntered( BooleanUtils.fromBooleanToChar( stockDE.isUserEntered() ));
        return stockEntity;
    }

    /**
     * Copies properties from {@code stockEntity} to {@code stockDE}
     * @param stockEntity
     * @param stockDE
     */
    public void fromDBEntityToDomainEntity( final StockEntity stockEntity, final StockDE stockDE )
    {
        stockDE.setUserEntered( BooleanUtils.fromCharToBoolean( stockEntity.userEntered ));
        super.fromDBEntityToDomainEntity( stockEntity, stockDE );
    }

    /**
     * Copies properties from {@code stockDE} to {@code stockEntity}
     * @param stockDE
     * @param stockEntity
     */
    @Override
    public void fromDomainEntityToDBEntity( final StockDE stockDE, final StockEntity stockEntity )
    {
        stockEntity.setUserEntered( BooleanUtils.fromBooleanToChar( stockDE.isUserEntered() ));
        super.fromDomainEntityToDBEntity( stockDE, stockEntity );
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
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockEntity" );
        sb.append( "@" );
        sb.append( hashCode() );
        sb.append( "{" );
        sb.append( "tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", companyName='" ).append( companyName ).append( '\'' );
   //     sb.append( ", exchange='" ).append( exchange ).append( '\'' );
        sb.append( ", createdBy=" ).append( createdBy );
        sb.append( ", userEntered=" ).append( userEntered );
        sb.append( ", lastPrice=" ).append( lastPrice );
        sb.append( ", lastPriceUpdate=" ).append( lastPriceUpdate );
        sb.append( ", lastPriceChange=" ).append( lastPriceChange );
        sb.append( '}' );
        return sb.toString();
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

}
