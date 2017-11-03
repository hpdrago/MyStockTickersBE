package com.stocktracker.weblayer.dto;

import com.stocktracker.servicelayer.service.StockService;
import com.stocktracker.servicelayer.service.stockinformationprovider.StockQuoteState;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by mike on 5/7/2017.
 */
public class StockNoteDTO implements StockService.StockQuoteContainer
{
    private Integer id;
    private Integer customerId;
    private String tickerSymbol;
    private String companyName;
    private String notes;
    private String notesDate;
    private String notesSourceName;
    private Integer notesSourceId;
    private Byte notesRating;
    private Boolean publicInd;
    private Byte bullOrBear;
    private Byte actionTaken;
    private Integer actionTakenShares;
    private BigDecimal actionTakenPrice;
    private BigDecimal stockPriceWhenCreated;
    private BigDecimal lastPrice;
    private String lastPriceChange;
    private StockQuoteState stockQuoteState;
    private BigDecimal percentChange;
    private String createDate;
    private String updateDate;

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

    public Integer getNotesSourceId()
    {
        return notesSourceId;
    }

    public void setNotesSourceId( Integer notesSourceId )
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

    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    @Override
    public void setCompanyName( final String companyName )
    {

    }

    @Override
    public String getCompanyName()
    {
        return null;
    }

    public void setTickerSymbol( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    public BigDecimal getLastPrice()
    {
        return lastPrice;
    }

    @Override
    public void setStockQuoteState( final StockQuoteState stockQuoteState )
    {
        this.stockQuoteState = stockQuoteState;
    }

    @Override
    public StockQuoteState getStockQuoteState()
    {
        return stockQuoteState;
    }

    @Override
    public void setLastPriceChange( final String lastPriceChange )
    {
        this.lastPriceChange = lastPriceChange;
    }

    @Override
    public String getLastPriceChange()
    {
        return this.lastPriceChange;
    }

    public void setLastPrice( final BigDecimal lastPrice )
    {
        this.lastPrice = lastPrice;
    }

    public BigDecimal getPercentChange()
    {
        return percentChange;
    }

    public void setPercentChange( final BigDecimal percentChange )
    {
        this.percentChange = percentChange;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockNoteDTO{" );
        sb.append( "id=" ).append( id );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", companyName='" ).append( companyName ).append( '\'' );
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
        sb.append( ", lastPrice=" ).append( lastPrice );
        sb.append( ", lastPriceChange='" ).append( lastPriceChange ).append( '\'' );
        sb.append( ", stockQuoteState=" ).append( stockQuoteState );
        sb.append( ", percentChange=" ).append( percentChange );
        sb.append( ", createDate='" ).append( createDate ).append( '\'' );
        sb.append( ", updateDate='" ).append( updateDate ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }
}
