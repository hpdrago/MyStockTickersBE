package com.stocktracker.repositorylayer.entity;

import com.stocktracker.servicelayer.service.YahooStockService;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * STOCK_NOTE_STOCK Table Entity
 */
@Entity
@Table( name = "stock_note_stock", schema = "stocktracker", catalog = "" )
public class StockNoteStockEntity implements YahooStockService.YahooStockContainer
{
    @EmbeddedId
    private StockNoteStockEntityPK id;
    private Integer customerId;
    private BigDecimal stockPrice;

    @ManyToOne( fetch = FetchType.EAGER )
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "stock_note_id", nullable = false, updatable = false, insertable = false )
    private StockNoteEntity stockNoteEntity;

    /**
     * Creates a new primary key id.
     * @param id
     * @param tickerSymbol
     */
    public void setId( final Integer id, final String tickerSymbol )
    {
        this.id = new StockNoteStockEntityPK( id, tickerSymbol );
    }

    public StockNoteStockEntityPK getId()
    {
        return id;
    }

    public void setStockNoteId( final Integer stockNoteId )
    {
        this.id.setStockNoteId( stockNoteId );
    }

    public void setId( StockNoteStockEntityPK id )
    {
        this.id = id;
    }

    public Integer getStockNoteId()
    {
        return id == null ? null : id.getStockNoteId();
    }

    private void checkPrimaryKey()
    {
        if ( id == null )
        {
            id = new StockNoteStockEntityPK();
        }
    }

    @Basic
    @Column( name = "stock_price", nullable = false, precision = 2 )
    public BigDecimal getStockPrice()
    {
        return stockPrice;
    }

    @Override
    public String getTickerSymbol()
    {
        return id.getTickerSymbol();
    }

    @Override
    public void setLastPrice( final BigDecimal stockPrice )
    {
        this.stockPrice = stockPrice;
    }

    @Override
    public void setLastPriceChange( final Timestamp lastPriceChange )
    {
    }

    public void setStockPrice( final BigDecimal stockPrice )
    {
        this.stockPrice = stockPrice;
    }

    @Override
    public void setCompanyName( final String companyName )
    {
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

    public StockNoteEntity getStockNoteEntity()
    {
        return stockNoteEntity;
    }

    public void setStockNoteEntity( StockNoteEntity stockNoteEntity )
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

        if ( !id.equals( that.id ) )
        {
            return false;
        }
        if ( !customerId.equals( that.customerId ) )
        {
            return false;
        }
        return stockPrice != null
               ? stockPrice.equals( that.stockPrice )
               : that.stockPrice == null;
    }

    @Override
    public int hashCode()
    {
        int result = id.hashCode();
        result = 31 * result + customerId.hashCode();
        result = 31 * result + (stockPrice != null
                                ? stockPrice.hashCode()
                                : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "StockNoteStockEntity{" +
               "id=" + id +
               ", customerId=" + customerId +
               ", stockPrice=" + stockPrice +
               '}';
    }
}
