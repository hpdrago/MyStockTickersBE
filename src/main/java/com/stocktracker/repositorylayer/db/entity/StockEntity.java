package com.stocktracker.repositorylayer.db.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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

    @Id
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
    @Column( name = "company_name" )
    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName( final String companyName )
    {
        this.companyName = companyName;
    }

    @Basic
    @Column( name = "exchange" )
    public String getExchange()
    {
        return exchange;
    }

    public void setExchange( final String exchange )
    {
        this.exchange = exchange;
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
}
