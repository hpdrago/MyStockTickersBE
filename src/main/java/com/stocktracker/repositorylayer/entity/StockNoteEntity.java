package com.stocktracker.repositorylayer.entity;

import com.stocktracker.repositorylayer.common.CustomerUuidContainer;
import com.stocktracker.repositorylayer.common.NotesSourceUuidContainer;
import com.stocktracker.servicelayer.service.StockNoteSourceEntityService;
import com.stocktracker.servicelayer.service.stocks.StockPriceWhenCreatedContainer;
import com.stocktracker.servicelayer.service.stocks.TickerSymbolContainer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by mike on 5/7/2017.
 */
@Entity
@Table( name = "stock_note", schema = "stocktracker", catalog = "" )
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class StockNoteEntity extends UUIDEntity
                             implements NotesSourceUuidContainer,
                                        CustomerUuidContainer,
                                        TickerSymbolContainer,
                                        StockPriceWhenCreatedContainer
{
    private String tickerSymbol;
    private String notes;
    private Timestamp notesDate;
    private UUID customerUuid;
    private Byte notesRating;
    private String publicInd;
    private Byte bullOrBear;
    private Byte actionTaken;
    private Integer actionTakenShares;
    private BigDecimal actionTakenPrice;
    private UUID notesSourceUuid;
    //private StockNoteSourceEntity stockNoteSourceByNotesSourceUuid;
    private BigDecimal stockPriceWhenCreated;

    @Basic
    @Column( name = "notes_source_uuid", nullable = false )
    public UUID getNotesSourceUuid()
    {
        return notesSourceUuid;
    }

    public void setNotesSourceUuid( final UUID notesSourceUuid )
    {
        this.notesSourceUuid = notesSourceUuid;
    }

    @Basic
    @Column( name = "customer_uuid", nullable = false )
    public UUID getCustomerUuid()
    {
        return customerUuid;
    }

    public void setCustomerUuid( final UUID customerId )
    {
        this.customerUuid = customerId;
    }

    @Basic
    @Column( name = "notes", nullable = false, length = 4000 )
    public String getNotes()
    {
        return notes;
    }

    public void setNotes( final String notes )
    {
        this.notes = notes;
    }

    @Basic
    @Column( name = "notes_date", nullable = false )
    public Timestamp getNotesDate()
    {
        return notesDate;
    }

    public void setNotesDate( final Timestamp notesDate )
    {
        this.notesDate = notesDate;
    }

    @Basic
    @Column( name = "notes_rating", nullable = true )
    public Byte getNotesRating()
    {
        return notesRating;
    }

    public void setNotesRating( final Byte noteRating )
    {
        this.notesRating = noteRating;
    }

    @Basic
    @Column( name = "public_ind", nullable = true, length = 1 )
    public String getPublicInd()
    {
        return publicInd;
    }

    public void setPublicInd( final String publicInd )
    {
        this.publicInd = publicInd;
    }

    @Basic
    @Column( name = "bull_or_bear", nullable = true, length = 1 )
    public Byte getBullOrBear()
    {
        return bullOrBear;
    }

    public void setBullOrBear( final Byte bullOrBear )
    {
        this.bullOrBear = bullOrBear;
    }

    /*
    @ManyToOne
    @JoinColumn( name = "notes_source_uuid", referencedColumnName = "uuid" )
    public StockNoteSourceEntity getStockNoteSourceByNotesSourceUuid()
    {
        return stockNoteSourceByNotesSourceUuid;
    }

    public void setStockNoteSourceByNotesSourceUuid( final StockNoteSourceEntity stockNoteSourceByNotesSourceId )
    {
        this.stockNoteSourceByNotesSourceUuid = stockNoteSourceByNotesSourceId;
    }

    @Transient
    @Override
    public Optional<StockNoteSourceEntity> getNotesSourceEntity()
    {
        return Optional.ofNullable( this.stockNoteSourceByNotesSourceUuid );
    }

    @Transient
    @Override
    public void setNotesSourceEntity( final StockNoteSourceEntity stockNoteSourceEntity )
    {
        this.stockNoteSourceByNotesSourceUuid = stockNoteSourceEntity;
    }

    @Transient
    @Override
    public Optional<UUID> getStockNoteSourceUuid()
    {
        return Optional.ofNullable( this.stockNoteSourceByNotesSourceUuid == null
                                    ? null
                                    : this.stockNoteSourceByNotesSourceUuid.getUuid() );
    }
    */

    @Basic
    @Column( name = "action_taken", nullable = false )
    public Byte getActionTaken()
    {
        return actionTaken;
    }

    public void setActionTaken( final Byte actionTaken )
    {
        this.actionTaken = actionTaken;
    }

    @Basic
    @Column( name = "action_taken_shares", nullable = false )
    public Integer getActionTakenShares()
    {
        return actionTakenShares;
    }

    public void setActionTakenShares( final Integer actionTakenShares )
    {
        this.actionTakenShares = actionTakenShares;
    }

    @Basic
    @Column( name = "ticker_symbol", nullable = false )
    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public void setTickerSymbol( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    @Basic
    @Column( name = "action_taken_price", nullable = false )
    public BigDecimal getActionTakenPrice()
    {
        return actionTakenPrice;
    }

    public void setActionTakenPrice( final BigDecimal actionTakenPrice )
    {
        this.actionTakenPrice = actionTakenPrice;
    }

    @Basic
    @Column( name = "stock_price_when_created", nullable = false )
    public BigDecimal getStockPriceWhenCreated()
    {
        return stockPriceWhenCreated;
    }

    public void setStockPriceWhenCreated( final BigDecimal stockPriceWhenCreated )
    {
        this.stockPriceWhenCreated = stockPriceWhenCreated;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockNoteEntity{" );
        sb.append( "uuid=" ).append( getUuidString() );
        sb.append( ", tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", notes='" ).append( notes ).append( '\'' );
        sb.append( ", notesDate=" ).append( notesDate );
        sb.append( ", customerId=" ).append( customerUuid );
        sb.append( ", notesRating=" ).append( notesRating );
        sb.append( ", publicInd='" ).append( publicInd ).append( '\'' );
        sb.append( ", bullOrBear=" ).append( bullOrBear );
        sb.append( ", actionTaken=" ).append( actionTaken );
        sb.append( ", actionTakenShares=" ).append( actionTakenShares );
        sb.append( ", actionTakenPrice=" ).append( actionTakenPrice );
        sb.append( ", notesSourceUuid=" ).append( notesSourceUuid );
        sb.append( ", stockPriceWhenCreated=" ).append( stockPriceWhenCreated );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
