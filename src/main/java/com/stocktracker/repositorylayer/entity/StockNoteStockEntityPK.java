package com.stocktracker.repositorylayer.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * This is the Primary Key definition class for hte StockNoteStock table
 */
@Embeddable
public class StockNoteStockEntityPK implements Serializable
{
    @Column( name = "stock_note_id", nullable = false )
    private Integer stockNoteId;

    @Column( name = "ticker_symbol", nullable = false, length = 5 )
    private String tickerSymbol;

    public StockNoteStockEntityPK()
    {
    }

    public StockNoteStockEntityPK( final Integer stockNoteId, final String tickerSymbol )
    {
        Objects.requireNonNull( stockNoteId, "stockNoteId cannot be null" );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        this.stockNoteId = stockNoteId;
        this.tickerSymbol = tickerSymbol;
    }

    public Integer getStockNoteId()
    {
        return stockNoteId;
    }

    public String getTickerSymbol()
    {
        return tickerSymbol;
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

        if ( stockNoteId != null
             ? !stockNoteId.equals( that.stockNoteId )
             : that.stockNoteId != null )
        {
            return false;
        }
        return tickerSymbol != null
               ? tickerSymbol.equals( that.tickerSymbol )
               : that.tickerSymbol == null;
    }

    @Override
    public int hashCode()
    {
        int result = stockNoteId != null
                     ? stockNoteId.hashCode()
                     : 0;
        result = 31 * result + (tickerSymbol != null
                                ? tickerSymbol.hashCode()
                                : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "StockNoteStockEntityPK{" +
               "stockNoteId=" + stockNoteId +
               ", tickerSymbol='" + tickerSymbol + '\'' +
               '}';
    }
}
