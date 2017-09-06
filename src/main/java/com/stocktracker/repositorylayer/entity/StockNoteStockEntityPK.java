package com.stocktracker.repositorylayer.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class StockNoteStockEntityPK implements Serializable
{
    private Integer customerId;
    private Integer stockNoteId;
    private String tickerSymbol;

    @Column( name = "customer_id" )
    @Id
    public Integer getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( final Integer customerId )
    {
        this.customerId = customerId;
    }

    @Column( name = "stock_note_id", nullable = false )
    @Basic
    @Id
    public Integer getStockNoteId()
    {
        return stockNoteId;
    }

    public void setStockNoteId( final Integer stockNoteId )
    {
        this.stockNoteId = stockNoteId;
    }

    @Column( name = "ticker_symbol", nullable = false, length = 5 )
    @Basic
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

        final StockNoteStockEntityPK that = (StockNoteStockEntityPK) o;

        if ( customerId != null
             ? !customerId.equals( that.customerId )
             : that.customerId != null )
        {
            return false;
        }
        if ( stockNoteId != null
             ? !stockNoteId.equals( that.stockNoteId )
             : that.stockNoteId != null )
        {
            return false;
        }
        if ( tickerSymbol != null
             ? !tickerSymbol.equals( that.tickerSymbol )
             : that.tickerSymbol != null )
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = customerId != null
                     ? customerId.hashCode()
                     : 0;
        result = 31 * result + (stockNoteId != null
                                ? stockNoteId.hashCode()
                                : 0);
        result = 31 * result + (tickerSymbol != null
                                ? tickerSymbol.hashCode()
                                : 0);
        return result;
    }
}
