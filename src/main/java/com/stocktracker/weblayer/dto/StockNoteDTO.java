package com.stocktracker.weblayer.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.stocktracker.common.JSONMoneySerializer;
import com.stocktracker.servicelayer.service.stocks.StockPriceContainer;
import com.stocktracker.servicelayer.stockinformationprovider.StockPriceQuoteDTO;
import com.stocktracker.weblayer.dto.common.NotesSourceIdContainer;
import com.stocktracker.weblayer.dto.common.UuidDTO;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by mike on 5/7/2017.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class StockNoteDTO extends StockPriceQuoteDTO implements StockPriceContainer,
                                                                UuidDTO,
                                                                CustomerIdContainer,
                                                                NotesSourceIdContainer
{
    private String id;
    private String customerId;
    private String notes;
    private String notesDate;
    private String notesSourceName;
    private String notesSourceId;
    private Byte notesRating;
    private Boolean publicInd;
    private Byte bullOrBear;
    private Byte actionTaken;
    private Integer actionTakenShares;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal actionTakenPrice;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal stockPriceWhenCreated;
    private String createDate;
    private String updateDate;
    private Integer version;

    public String getId()
    {
        return id;
    }

    public void setId( String id )
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

    public String getNotesDate()
    {
        return notesDate;
    }

    public void setNotesDate( String notesDate )
    {
        this.notesDate = notesDate;
    }

    public String getNotesSourceName()
    {
        return notesSourceName;
    }

    public void setNotesSourceName( String notesSourceName )
    {
        this.notesSourceName = notesSourceName;
    }

    public String getNotesSourceId()
    {
        return notesSourceId;
    }

    public void setNotesSourceId( String notesSourceId )
    {
        this.notesSourceId = notesSourceId;
    }

    public String getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate( String createDate )
    {
        this.createDate = createDate;
    }

    public String getUpdateDate()
    {
        return updateDate;
    }

    public void setUpdateDate( String updateDate )
    {
        this.updateDate = updateDate;
    }

    public Byte getNotesRating()
    {
        return notesRating;
    }

    public void setNotesRating( Byte notesRating )
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

    public void setCustomerId( String customerId )
    {
        this.customerId = customerId;
    }

    public String getCustomerId()
    {
        return this.customerId;
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

    public Byte getActionTaken()
    {
        return actionTaken;
    }

    public void setActionTaken( final Byte actionTaken )
    {
        this.actionTaken = actionTaken;
    }

    public Integer getActionTakenShares()
    {
        return actionTakenShares;
    }

    public void setActionTakenShares( final Integer actionTakenShares )
    {
        this.actionTakenShares = actionTakenShares;
    }

    public BigDecimal getActionTakenPrice()
    {
        return actionTakenPrice;
    }

    public void setActionTakenPrice( final BigDecimal actionTakenPrice )
    {
        this.actionTakenPrice = actionTakenPrice;
    }

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
        final StringBuilder sb = new StringBuilder( "StockQuoteDTO{" );
        sb.append( "id=" ).append( id );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( ", notes='" ).append( notes ).append( '\'' );
        sb.append( ", notesDate='" ).append( notesDate ).append( '\'' );
        sb.append( ", notesSourceName='" ).append( notesSourceName ).append( '\'' );
        sb.append( ", notesSourceId=" ).append( notesSourceId );
        sb.append( ", notesRating=" ).append( notesRating );
        sb.append( ", publicInd=" ).append( publicInd );
        sb.append( ", bullOrBear=" ).append( bullOrBear );
        sb.append( ", actionTaken=" ).append( actionTaken );
        sb.append( ", actionTakenShares=" ).append( actionTakenShares );
        sb.append( ", actionTakenPrice=" ).append( actionTakenPrice );
        sb.append( ", stockPriceWhenCreated=" ).append( stockPriceWhenCreated );
        sb.append( ", createDate='" ).append( createDate ).append( '\'' );
        sb.append( ", updateDate='" ).append( updateDate ).append( '\'' );
        sb.append( ", version=" ).append( version );
        sb.append( '}' );
        return sb.toString();
    }
}
