package com.stocktracker.servicelayer.entity;

import com.stocktracker.common.BullOrBear;
import com.stocktracker.repositorylayer.db.entity.StockNoteEntity;
import com.stocktracker.servicelayer.service.StockNoteService;
import org.springframework.beans.BeanUtils;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * Created by mike on 5/7/2017.
 */
public class StockNoteDE
{
    private Integer id;
    private Integer customerId;
    private String tickerSymbol;
    private String notes;
    private double stockPrice;
    private Timestamp notesDate;
    private Integer notesSourceId;
    private String notesSource;
    private Integer noteRating;
    private Boolean publicInd;
    private BullOrBear bullOrBear;
    private Timestamp dateCreated;
    private Timestamp dateModified;

    public static StockNoteDE newInstance( final StockNoteEntity stockNoteEntity,
                                           final StockNoteService stockNoteService )
    {
        Objects.requireNonNull( stockNoteEntity );
        StockNoteDE stockNoteDE = new StockNoteDE();
        BeanUtils.copyProperties( stockNoteEntity, stockNoteDE );
        stockNoteDE.bullOrBear = BullOrBear.valueOf( stockNoteEntity.getBullOrBear() );
        stockNoteDE.notesSource = stockNoteService.getStockNoteSource( stockNoteEntity.getNotesSourceId() )
                                                  .getNoteSource();
        return stockNoteDE;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId( Integer id )
    {
        this.id = id;
    }

    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public void setTickerSymbol( String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes( String notes )
    {
        this.notes = notes;
    }

    public Integer getNotesSourceId()
    {
        return notesSourceId;
    }

    public void setNotesSourceId( Integer notesSourceId )
    {
        this.notesSourceId = notesSourceId;
    }

    public Timestamp getDateCreated()
    {
        return dateCreated;
    }

    public void setDateCreated( Timestamp dateCreated )
    {
        this.dateCreated = dateCreated;
    }

    public Timestamp getDateModified()
    {
        return dateModified;
    }

    public void setDateModified( Timestamp dateModified )
    {
        this.dateModified = dateModified;
    }

    public String getNotesSource()
    {
        return notesSource;
    }

    public void setNotesSource( String notesSource )
    {
        this.notesSource = notesSource;
    }

    public Integer getNoteRating()
    {
        return noteRating;
    }

    public void setNoteRating( Integer noteRating )
    {
        this.noteRating = noteRating;
    }

    public Boolean getPublicInd()
    {
        return publicInd;
    }

    public void setPublicInd( Boolean publicInd )
    {
        this.publicInd = publicInd;
    }

    public Integer getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( Integer customerId )
    {
        this.customerId = customerId;
    }

    public BullOrBear getBullOrBear()
    {
        return bullOrBear;
    }

    public void setBullOrBear( BullOrBear bullOrBear )
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
        if ( !(o instanceof StockNoteDE) )
        {
            return false;
        }
        final StockNoteDE that = (StockNoteDE) o;
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
        final StringBuilder sb = new StringBuilder( "StockNoteDE{" );
        sb.append( "id=" ).append( id );
        sb.append( ", tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", customerId='" ).append( customerId ).append( '\'' );
        sb.append( ", notes='" ).append( notes ).append( '\'' );
        sb.append( ", notesDate='" ).append( notesDate ).append( '\'' );
        sb.append( ", stockPrice='" ).append( stockPrice ).append( '\'' );
        sb.append( ", notesSourceId=" ).append( notesSourceId );
        sb.append( ", notesSource='" ).append( notesSource ).append( '\'' );
        sb.append( ", noteRating=" ).append( noteRating );
        sb.append( ", publicInd=" ).append( publicInd );
        sb.append( ", bullOrBear=" ).append( bullOrBear );
        sb.append( ", dateCreated=" ).append( dateCreated );
        sb.append( ", dateModified=" ).append( dateModified );
        sb.append( '}' );
        return sb.toString();
    }

    public Timestamp getNotesDate()
    {
        return notesDate;
    }

    public void setNotesDate( Timestamp notesDate )
    {
        this.notesDate = notesDate;
    }
}
