package com.stocktracker.weblayer.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.stocktracker.common.JSONMoneySerializer;
import com.stocktracker.weblayer.dto.common.CustomerIdContainer;
import com.stocktracker.weblayer.dto.common.DatabaseEntityDTO;
import com.stocktracker.weblayer.dto.common.NotesSourceIdContainer;
import com.stocktracker.weblayer.dto.common.StockPriceQuoteDTOContainer;
import com.stocktracker.weblayer.dto.common.StockQuoteDTOContainer;
import com.stocktracker.weblayer.dto.common.UuidDTO;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by mike on 5/7/2017.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class StockNoteDTO extends DatabaseEntityDTO<String>
                          implements CustomerIdContainer,
                                     NotesSourceIdContainer,
                                     UuidDTO,
                                     StockQuoteDTOContainer,
                                     StockPriceQuoteDTOContainer
{
    private String tickerSymbol;
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
    private StockPriceQuoteDTO stockPriceQuoteDTO;
    private StockQuoteDTO stockQuoteDTO;

    @Override
    public String getCacheKey()
    {
        return this.tickerSymbol;
    }

    @Override
    public void setCacheKey( final String tickerSymbol )
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
    public StockPriceQuoteDTO getStockPriceQuote()
    {
        return stockPriceQuoteDTO;
    }

    @Override
    public void setStockPriceQuote( final StockPriceQuoteDTO stockPriceQuoteDTO )
    {
        this.stockPriceQuoteDTO = stockPriceQuoteDTO;
    }

    @Override
    public StockQuoteDTO getStockQuoteDTO()
    {
        return stockQuoteDTO;
    }

    @Override
    public void setStockQuoteDTO( final StockQuoteDTO stockQuoteDTO )
    {
        this.stockQuoteDTO = stockQuoteDTO;
    }

    @Override
    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    @Override
    public void setTickerSymbol( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockQuoteDTO{" );
        sb.append( "id=" ).append( super.getId() );
        sb.append( ", tickerSymbol=" ).append( tickerSymbol );
        sb.append( ", customerId=" ).append( customerId );
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
        sb.append( ", stockPriceQuoteDTO=" ).append( stockPriceQuoteDTO );
        sb.append( ", stockQuoteDTO=" ).append( stockQuoteDTO );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
