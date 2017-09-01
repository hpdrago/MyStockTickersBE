package com.stocktracker.weblayer.dto;

import com.stocktracker.common.BullOrBear;
import com.stocktracker.servicelayer.entity.StockNoteDE;
import org.springframework.beans.BeanUtils;

import java.security.Timestamp;
import java.util.Objects;

/**
 * Created by mike on 5/7/2017.
 */
public class StockNoteDTO
{
    private Integer id;
    private Integer customerId;
    private String tickerSymbol;
    private String notes;
    private Double stockPrice;
    private Timestamp noteDate;
    private String source;
    private Integer sourceId;
    private Integer noteRating;
    private Boolean publicInd;
    private BullOrBear bullOrBear;
    private Timestamp dateCreated;
    private Timestamp dateModified;

    /**
     * Create a new instance from a StockNoteDE instance
     * @param stockNoteDE
     * @return
     */
    public static StockNoteDTO newInstance( final StockNoteDE stockNoteDE )
    {
        Objects.requireNonNull( stockNoteDE );
        StockNoteDTO stockNoteDTO = new StockNoteDTO();
        BeanUtils.copyProperties( stockNoteDE, stockNoteDTO );
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

    public Timestamp getNoteDate()
    {
        return noteDate;
    }

    public void setNoteDate( Timestamp noteDate )
    {
        this.noteDate = noteDate;
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

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockNoteDTO{" );
        sb.append( "id=" ).append( id );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", notes='" ).append( notes ).append( '\'' );
        sb.append( ", noteDate='" ).append( noteDate ).append( '\'' );
        sb.append( ", stockPrice='" ).append( stockPrice ).append( '\'' );
        sb.append( ", source='" ).append( source ).append( '\'' );
        sb.append( ", sourceId=" ).append( sourceId );
        sb.append( ", noteRating=" ).append( noteRating );
        sb.append( ", publicInd=" ).append( publicInd );
        sb.append( ", bullOrBear=" ).append( bullOrBear );
        sb.append( ", dateCreated=" ).append( dateCreated );
        sb.append( ", dateModified=" ).append( dateModified );
        sb.append( '}' );
        return sb.toString();
    }

    public Double getStockPrice()
    {
        return stockPrice;
    }

    public void setStockPrice( Double stockPrice )
    {
        this.stockPrice = stockPrice;
    }
}
