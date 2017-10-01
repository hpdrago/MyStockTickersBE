package com.stocktracker.repositorylayer.entity;

import com.stocktracker.common.MyLogger;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

/**
 * Created by mike on 5/7/2017.
 */
@Entity
@Table( name = "stock_note", schema = "stocktracker", catalog = "" )
public class StockNoteEntity implements MyLogger
{
    private Integer id;
    private String notes;
    private Timestamp notesDate;
    private Timestamp dateCreated;
    private Timestamp dateModified;
    private Integer customerId;
    private Integer notesRating;
    private String publicInd;
    private Byte bullOrBear;
    private List<StockNoteStockEntity> stockNoteStocks;
    private StockNoteSourceEntity stockNoteSourceByNotesSourceId;

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
    @Column( name = "notes_date", nullable = true )
    public Timestamp getNotesDate()
    {
        return notesDate;
    }

    public void setNotesDate( final Timestamp notesDate )
    {
        this.notesDate = notesDate;
    }

    @Basic
    @Column( name = "date_created", nullable = false, insertable = false, updatable = false )
    public Timestamp getDateCreated()
    {
        return dateCreated;
    }

    public void setDateCreated( final Timestamp dateCreated )
    {
        this.dateCreated = dateCreated;
    }

    @Basic
    @Column( name = "date_modified", nullable = true, insertable = false )
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
    public Integer getNotesRating()
    {
        return notesRating;
    }

    public void setNotesRating( final Integer noteRating )
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

    @OneToMany( mappedBy = "stockNoteEntity",
                cascade = CascadeType.ALL,
                orphanRemoval = true )
    public List<StockNoteStockEntity> getStockNoteStocks()
    {
        return stockNoteStocks;
    }

    public void setStockNoteStocks( final List<StockNoteStockEntity> stockNoteStocks )
    {
        this.stockNoteStocks = stockNoteStocks;
    }

    public void addStockNoteStock( final StockNoteStockEntity stockNoteStockEntity )
    {
        this.stockNoteStocks.add( stockNoteStockEntity );
        stockNoteStockEntity.setStockNoteEntity( this );
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
        sb.append( ", notes='" ).append( notes ).append( '\'' );
        sb.append( ", notesDate=" ).append( notesDate );
        sb.append( ", dateCreated=" ).append( dateCreated );
        sb.append( ", dateModified=" ).append( dateModified );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", notesRating=" ).append( notesRating );
        sb.append( ", publicInd='" ).append( publicInd ).append( '\'' );
        sb.append( ", bullOrBear=" ).append( bullOrBear );
        sb.append( ", stockNoteSource=" ).append( stockNoteSourceByNotesSourceId );
        sb.append( ", stockNoteStocks=" ).append( stockNoteStocks );
        sb.append( '}' );
        return sb.toString();
    }
}
