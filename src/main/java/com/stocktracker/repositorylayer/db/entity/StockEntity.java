package com.stocktracker.repositorylayer.db.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
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
    private String exchange;
    private int createdBy;
    private char userEntered;
    private BigDecimal lastPrice;

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
    @Column( name = "exchange", nullable = true, length = 10 )
    public String getExchange()
    {
        return exchange;
    }

    public void setExchange( final String exchange )
    {
        this.exchange = exchange;
    }

    @Basic
    @Column( name = "created_by" )
    public int getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy( int createdBy )
    {
        this.createdBy = createdBy;
    }

    @Basic
    @Column( name = "user_entered", nullable = true, length = 1 )
    public char getUserEntered()
    {
        return userEntered;
    }

    public void setUserEntered( final char downloaded )
    {
        this.userEntered = downloaded;
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
            Objects.equals( companyName, that.companyName ) &&
            Objects.equals( exchange, that.exchange );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( tickerSymbol, companyName, exchange );
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

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockEntity" );
        sb.append( "@" );
        sb.append( hashCode() );
        sb.append( "{" );
        sb.append( "tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", companyName='" ).append( companyName ).append( '\'' );
        sb.append( ", exchange='" ).append( exchange ).append( '\'' );
        sb.append( ", createdBy=" ).append( createdBy );
        sb.append( ", downloaded=" ).append( userEntered );
        sb.append( ", lastPrice=" ).append( lastPrice );
        sb.append( '}' );
        return sb.toString();
    }
}
