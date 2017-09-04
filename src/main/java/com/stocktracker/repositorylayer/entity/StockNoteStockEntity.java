package com.stocktracker.repositorylayer.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table( name = "stock_note_stock", schema = "stocktracker", catalog = "" )
public class StockNoteStockEntity
{
    private Integer customerId;
    private Integer stockNoteId;
    private String tickerSymbol;
    private BigDecimal stockPrice;

    @Id
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
    @Column( name = "stock_note_id", nullable = false )
    public Integer getStockNoteId()
    {
        return stockNoteId;
    }

    public void setStockNoteId( final Integer stockNoteId )
    {
        this.stockNoteId = stockNoteId;
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
    @Column( name = "stock_price", nullable = false, precision = 2 )
    public BigDecimal getStockPrice()
    {
        return stockPrice;
    }

    public void setStockPrice( final BigDecimal stockPrice )
    {
        this.stockPrice = stockPrice;
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
        final StockNoteStockEntity that = (StockNoteStockEntity) o;
        return Objects.equals( customerId, that.customerId ) &&
               Objects.equals( stockNoteId, that.stockNoteId ) &&
               Objects.equals( tickerSymbol, that.tickerSymbol ) &&
               Objects.equals( stockPrice, that.stockPrice );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( customerId, stockNoteId, tickerSymbol, stockPrice );
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockNoteStockEntity{" );
        sb.append( "customerId=" ).append( customerId );
        sb.append( ", stockNoteId=" ).append( stockNoteId );
        sb.append( ", tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", stockPrice=" ).append( stockPrice );
        sb.append( '}' );
        return sb.toString();
    }
}
