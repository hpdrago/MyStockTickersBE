package com.stocktracker.repositorylayer.entity;

import com.stocktracker.common.MyLogger;
import com.stocktracker.servicelayer.service.StockNoteSourceService;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by mike on 5/7/2017.
 */
@Entity
@Table( name = "stock_note", schema = "stocktracker", catalog = "" )
public class StockNoteEntity implements MyLogger, StockNoteSourceService.StockNoteSourceEntityContainer
{
    private Integer id;
    private String tickerSymbol;
    private String notes;
    private Timestamp notesDate;
    private Timestamp dateCreated;
    private Timestamp dateModified;
    private Integer customerId;
    private Byte notesRating;
    private String publicInd;
    private Byte bullOrBear;
    private Byte actionTaken;
    private Integer actionTakenShares;
    private BigDecimal actionTakenPrice;
    private StockNoteSourceEntity stockNoteSourceByNotesSourceId;
    private BigDecimal stockPriceWhenCreated;
    private Timestamp createDate;
    private Timestamp updateDate;
    private Integer version;

    public static StockNoteEntity newInstance()
    {
        return new StockNoteEntity();
    }

    @Id
    @Column( name = "id", nullable = false )
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId()
    {
        return id;
    }

    public void setId( final Integer id )
    {
        this.id = id;
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
    @Column( name = "create_date", nullable = false, insertable = false, updatable = false )
    public Timestamp getDateCreated()
    {
        return dateCreated;
    }

    public void setDateCreated( final Timestamp dateCreated )
    {
        this.dateCreated = dateCreated;
    }

    @Basic
    @Column( name = "update_date", nullable = true, insertable = false )
    public Timestamp getDateModified()
    {
        return dateModified;
    }

    public void setDateModified( final Timestamp dateModified )
    {
        this.dateModified = dateModified;
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

    @ManyToOne
    @JoinColumn( name = "notes_source_id", referencedColumnName = "id" )
    public StockNoteSourceEntity getStockNoteSourceByNotesSourceId()
    {
        return stockNoteSourceByNotesSourceId;
    }

    public void setStockNoteSourceByNotesSourceId( final StockNoteSourceEntity stockNoteSourceByNotesSourceId )
    {
        this.stockNoteSourceByNotesSourceId = stockNoteSourceByNotesSourceId;
    }

    @Transient
    @Override
    public Optional<StockNoteSourceEntity> getNotesSourceEntity()
    {
        return Optional.ofNullable( this.stockNoteSourceByNotesSourceId );
    }

    @Transient
    @Override
    public void setNotesSourceEntity( final StockNoteSourceEntity stockNoteSourceEntity )
    {
        this.stockNoteSourceByNotesSourceId = stockNoteSourceEntity;
    }

    @Transient
    @Override
    public Optional<Integer> getStockNoteSourceId()
    {
        return Optional.ofNullable( this.stockNoteSourceByNotesSourceId == null
                                    ? null
                                    : this.stockNoteSourceByNotesSourceId.getId() );
    }

    @Basic
    @Column( name = "create_date", nullable = false )
    public Timestamp getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate( final Timestamp createDate )
    {
        this.createDate = createDate;
    }

    @Basic
    @Column( name = "update_date", insertable = false, updatable = false )
    public Timestamp getUpdateDate()
    {
        return updateDate;
    }

    public void setUpdateDate( final Timestamp updateDate )
    {
        this.updateDate = updateDate;
    }

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

    @Basic
    @Column( name = "version" )
    public Integer getVersion()
    {
        return version;
    }

    public void setVersion( final Integer version )
    {
        this.version = version;
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
        final StockNoteEntity that = (StockNoteEntity) o;
        return Objects.equals( id, that.id );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( id );
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockNoteEntity{" );
        sb.append( "id=" ).append( id );
        sb.append( ", tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", notes='" ).append( notes ).append( '\'' );
        sb.append( ", notesDate=" ).append( notesDate );
        sb.append( ", dateCreated=" ).append( dateCreated );
        sb.append( ", dateModified=" ).append( dateModified );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", notesRating=" ).append( notesRating );
        sb.append( ", publicInd='" ).append( publicInd ).append( '\'' );
        sb.append( ", bullOrBear=" ).append( bullOrBear );
        sb.append( ", actionTaken=" ).append( actionTaken );
        sb.append( ", actionTakenShares=" ).append( actionTakenShares );
        sb.append( ", actionTakenPrice=" ).append( actionTakenPrice );
        sb.append( ", stockNoteSourceByNotesSourceId=" ).append( stockNoteSourceByNotesSourceId );
        sb.append( ", stockPriceWhenCreated=" ).append( stockPriceWhenCreated );
        sb.append( ", version=" ).append( version );
        sb.append( ", createDate=" ).append( createDate );
        sb.append( ", updateDate=" ).append( updateDate );
        sb.append( '}' );
        return sb.toString();
    }
}
