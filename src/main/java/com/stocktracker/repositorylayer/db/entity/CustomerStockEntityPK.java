package com.stocktracker.repositorylayer.db.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by mike on 5/7/2017.
 */
public class CustomerStockEntityPK implements Serializable
{
    private Integer customerId;
    private String tickerSymbol;

    @Column( name = "customer_id", nullable = false )
    @Id
    public Integer getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( final Integer customerId )
    {
        this.customerId = customerId;
    }

    @Column( name = "ticker_symbol", nullable = false, length = 5 )
    @Id
    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public void setTickerSymbol( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
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
        final CustomerStockEntityPK that = (CustomerStockEntityPK) o;
        return Objects.equals( customerId, that.customerId ) &&
               Objects.equals( tickerSymbol, that.tickerSymbol );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( customerId, tickerSymbol );
    }
}
