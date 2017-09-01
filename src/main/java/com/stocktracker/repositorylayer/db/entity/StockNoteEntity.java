package com.stocktracker.repositorylayer.db.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Created by mike on 5/7/2017.
 */
@Entity
@Table( name = "stock_note", schema = "stocktracker", catalog = "" )
public class StockNoteEntity
{
    private Integer id;
    private String tickerSymbol;
    private String notes;
    private Integer notesSourceId;
    private Timestamp notesDate;
    private BigDecimal stockPrice;
    private Timestamp dateCreated;
    private Timestamp dateModified;
    private Integer customerId;
    private Integer noteRating;
    private String publicInd;
    private Byte bullOrBear;

    @Id
    @Column( name = "id", nullable = false )
    public Integer getId()
    {
        return id;
    }

    public void setId( final Integer id )
    {
        this.id = id;
    }

    @Basic
    @Column( name = "notes_source_id", nullable = true )
    public Integer getNotesSourceId()
    {
        return notesSourceId;
    }

    public void setNotesSourceId( final Integer notesSourceId )
    {
        this.notesSourceId = notesSourceId;
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
    @Column( name = "notes_date" )
    public Timestamp getNotesDate()
    {
        return notesDate;
    }

    public void setNotesDate( final Timestamp notesDate )
    {
        this.notesDate = notesDate;
    }

    @Basic
    @Column( name = "stock_price" )
    public BigDecimal getStockPrice()
    {
        return stockPrice;
    }

    public void setStockPrice( final BigDecimal stockPrice )
    {
        this.stockPrice = stockPrice;
    }

    @Basic
    @Column( name = "date_created", nullable = false )
    public Timestamp getDateCreated()
    {
        return dateCreated;
    }

    public void setDateCreated( final Timestamp dateCreated )
    {
        this.dateCreated = dateCreated;
    }

    @Basic
    @Column( name = "date_modified", nullable = true )
    public Timestamp getDateModified()
    {
        return dateModified;
    }

    public void setDateModified( final Timestamp dateModified )
    {
        this.dateModified = dateModified;
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
    @Column( name = "note_rating", nullable = true )
    public Integer getNoteRating()
    {
        return noteRating;
    }

    public void setNoteRating( final Integer noteRating )
    {
        this.noteRating = noteRating;
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
    @Column( name = "bull_or_bear", nullable = true )
    public Byte getBullOrBear()
    {
        return bullOrBear;
    }

    public void setBullOrBear( final Byte bullOrBear )
    {
        this.bullOrBear = bullOrBear;
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
        sb.append( ", notes='" ).append( notesDate ).append( '\'' );
        sb.append( ", stockPrice='" ).append( stockPrice ).append( '\'' );
        sb.append( ", notesSourceId=" ).append( notesSourceId );
        sb.append( ", dateCreated=" ).append( dateCreated );
        sb.append( ", dateModified=" ).append( dateModified );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", noteRating=" ).append( noteRating );
        sb.append( ", publicInd='" ).append( publicInd ).append( '\'' );
        sb.append( ", bullOrBear='" ).append( bullOrBear ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }

    {
        this.stockPrice = stockPrice;
    }
}
