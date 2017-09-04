package com.stocktracker.weblayer.dto;

import com.stocktracker.repositorylayer.entity.StockNoteEntity;
import org.springframework.beans.BeanUtils;

import java.security.Timestamp;
import java.util.List;
import java.util.Objects;

/**
 * Created by mike on 5/7/2017.
 */
public class StockNoteDTO
{
    private Integer id;
    private Integer customerId;
    private String notes;
    private Timestamp notesDate;
    private String source;
    private Integer sourceId;
    private Integer notesRating;
    private Boolean publicInd;
    private Byte bullOrBear;
    private Timestamp dateCreated;
    private Timestamp dateModified;
    private List<StockNoteStockDTO> stocks;

    /**
     * Create a new instance from a StockNoteDE instance
     * @param stockNoteEntity
     * @return
     */
    public static StockNoteDTO newInstance( final StockNoteEntity stockNoteEntity )
    {
        Objects.requireNonNull( stockNoteEntity );
        StockNoteDTO stockNoteDTO = new StockNoteDTO();
        BeanUtils.copyProperties( stockNoteEntity, stockNoteDTO );
        return stockNoteDTO;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId( Integer id )
    {
        this.id = id;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes( String notes )
    {
        this.notes = notes;
    }

    public Timestamp getNotesDate()
    {
        return notesDate;
    }

    public void setNotesDate( Timestamp notesDate )
    {
        this.notesDate = notesDate;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource( String source )
    {
        this.source = source;
    }

    public Integer getSourceId()
    {
        return sourceId;
    }

    public void setSourceId( Integer sourceId )
    {
        this.sourceId = sourceId;
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

    public Integer getNotesRating()
    {
        return notesRating;
    }

    public void setNotesRating( Integer notesRating )
    {
        this.notesRating = notesRating;
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

    public Byte getBullOrBear()
    {
        return bullOrBear;
    }

    public void setBullOrBear( Byte bullOrBear )
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
        if ( !(o instanceof StockNoteDTO) )
        {
            return false;
        }
        final StockNoteDTO that = (StockNoteDTO) o;
        return Objects.equals( id, that.id );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( id );
    }

    public List<StockNoteStockDTO> getStocks()
    {
        return stocks;
    }

    public void setStocks( List<StockNoteStockDTO> stocks )
    {
        this.stocks = stocks;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockNoteDTO{" );
        sb.append( "id=" ).append( id );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", notes='" ).append( notes ).append( '\'' );
        sb.append( ", notesDate='" ).append( notesDate ).append( '\'' );
        sb.append( ", source='" ).append( source ).append( '\'' );
        sb.append( ", sourceId=" ).append( sourceId );
        sb.append( ", notesRating=" ).append( notesRating );
        sb.append( ", publicInd=" ).append( publicInd );
        sb.append( ", bullOrBear=" ).append( bullOrBear );
        sb.append( ", dateCreated=" ).append( dateCreated );
        sb.append( ", dateModified=" ).append( dateModified );
        sb.append( ", stocks=" ).append( stocks );
        sb.append( '}' );
        return sb.toString();
    }
}
