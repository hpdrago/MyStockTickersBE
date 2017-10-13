package com.stocktracker.repositorylayer.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * STOCK_NOTE_STOCK Table Entity
 */
@Entity
@Table( name = "stock_note_stock", schema = "stocktracker", catalog = "" )
public class StockNoteStockEntity
{
    private Integer id;
    private Integer customerId;
    private String tickerSymbol;
    private BigDecimal stockPrice;

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "stock_note_id", nullable = false, updatable = false, insertable = false )
    private StockNoteEntity stockNoteEntity;

    public static StockNoteStockEntity newInstance()
    {
        return new StockNoteStockEntity();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name = "id", nullable = false )
    public Integer getId()
    {
        return id;
    }

    public void setId( final Integer id )
    {
        if ( this.id != null && id != null )
            throw new IllegalArgumentException( "Cannot change ID value" );
        this.id = id;
    }

    @Basic
    @Column( name = "stock_note_id", nullable = false, updatable = false, insertable = false )
    public Integer getStockNoteId()
    {
        return this.stockNoteEntity == null ? null : this.stockNoteEntity.getId();
    }

    public void setStockNoteId( final Integer stockNoteId )
    {
        // don't set the parent id
    }

    @Basic
    @Column( name = "stock_price", nullable = true, precision = 2 )
    public BigDecimal getStockPrice()
    {
        return stockPrice;
    }

    public void setStockPrice( final BigDecimal stockPrice )
    {
        this.stockPrice = stockPrice;
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

    @ManyToOne
    @JoinColumn( name = "stock_note_id", referencedColumnName = "id", nullable = false )
    public StockNoteEntity getStockNoteEntity()
    {
        return stockNoteEntity;
    }

    public void setStockNoteEntity( final StockNoteEntity stockNoteEntity )
    {
        this.stockNoteEntity = stockNoteEntity;
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
        return Objects.equals( id, that.id ) &&
               Objects.equals( customerId, that.customerId ) &&
               Objects.equals( tickerSymbol, that.tickerSymbol ) &&
               Objects.equals( stockPrice, that.stockPrice );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( id, tickerSymbol, customerId, stockPrice );
    }

    @Override
    public String toString()
    {
        return "StockNoteStockEntity{" +
               "id=" + id +
               ", stockNoteStockId=" + stockNoteEntity.getId() +
               ", customerId=" + customerId +
               ", tickerSymbol='" + tickerSymbol + '\'' +
               ", stockPrice=" + stockPrice +
               '}';
    }
}
