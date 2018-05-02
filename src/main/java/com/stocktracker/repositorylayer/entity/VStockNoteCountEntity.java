package com.stocktracker.repositorylayer.entity;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
@Entity
@Table( name = "v_stock_note_count", schema = "stocktracker", catalog = "" )
public class VStockNoteCountEntity
{
    private String tickerSymbol;
    private Integer customerId;
    private Long noteCount;

    public static VStockNoteCountEntity newInstance()
    {
        return new VStockNoteCountEntity();
    }

    @Basic
    @Id
    @Column( name = "customer_id", nullable = false )
    public Integer getCustomerUuid()
    {
        return customerId;
    }

    public void setCustomerUuid( final Integer customerId )
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

    @Basic
    @Column( name = "note_count", nullable = false )
    public Long getNoteCount()
    {
        return noteCount;
    }

    public void setNoteCount( final Long noteCount )
    {
        this.noteCount = noteCount;
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

        final VStockNoteCountEntity that = (VStockNoteCountEntity) o;

        if ( !tickerSymbol.equals( that.tickerSymbol ) )
        {
            return false;
        }
        return customerId.equals( that.customerId );
    }

    @Override
    public int hashCode()
    {
        int result = tickerSymbol.hashCode();
        result = 31 * result + customerId.hashCode();
        return result;
    }

    @Override
    public String toString()
    {
        return "VStockNoteCountEntity{" +
               "tickerSymbol='" + tickerSymbol + '\'' +
               ", customerId=" + customerId +
               ", noteCount=" + noteCount +
               '}';
    }
}
