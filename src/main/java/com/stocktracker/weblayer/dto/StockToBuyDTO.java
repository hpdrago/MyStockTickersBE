package com.stocktracker.weblayer.dto;

import com.stocktracker.repositorylayer.entity.StockTagEntity;
import com.stocktracker.servicelayer.service.stocks.StockPriceWhenCreatedContainer;
import com.stocktracker.weblayer.dto.common.CustomerIdContainer;
import com.stocktracker.weblayer.dto.common.DatabaseEntityDTO;
import com.stocktracker.weblayer.dto.common.NotesSourceIdContainer;
import com.stocktracker.weblayer.dto.common.StockPriceQuoteDTOContainer;
import com.stocktracker.weblayer.dto.common.StockQuoteDTOContainer;
import com.stocktracker.weblayer.dto.common.TagsContainer;
import com.stocktracker.weblayer.dto.common.UuidDTO;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.beans.Transient;
import java.math.BigDecimal;
import java.util.List;

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class StockToBuyDTO extends DatabaseEntityDTO<String>
                           implements NotesSourceIdContainer,
                                      UuidDTO,
                                      CustomerIdContainer,
                                      StockPriceWhenCreatedContainer,
                                      TagsContainer,
                                      StockPriceQuoteDTOContainer,
                                      StockQuoteDTOContainer

{
    /*
     * Entity (DB columns)
     */
    private String tickerSymbol;
    private String customerId;
    private String comments;
    private String notesSourceId;
    private String notesSourceName;
    private BigDecimal buySharesUpToPrice;
    private BigDecimal stockPriceWhenCreated;
    private Boolean completed;
    private String buyAfterDate;
    private List<String> tags;
    private BigDecimal avgAnalystPriceTarget;
    private StockPriceQuoteDTO stockPriceQuoteDTO;
    private StockQuoteDTO stockQuoteDTO;

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
    public StockQuoteDTO getStockQuote()
    {
        return stockQuoteDTO;
    }

    @Override
    public void setStockQuote( final StockQuoteDTO stockQuoteDTO )
    {
        this.stockQuoteDTO = stockQuoteDTO;
    }

    public String getCustomerId()
    {
        return customerId;
    }

    @Override
    public StockTagEntity.StockTagReferenceType getStockTagReferenceType()
    {
        return StockTagEntity.StockTagReferenceType.STOCK_TO_BUY;
    }

    @Override
    public String getEntityId()
    {
        return this.getId();
    }

    public void setCustomerId( String customerId )
    {
        this.customerId = customerId;
    }

    public String getComments()
    {
        return comments;
    }

    public void setComments( String comments )
    {
        this.comments = comments;
    }

    public BigDecimal getBuySharesUpToPrice()
    {
        return buySharesUpToPrice;
    }

    public void setBuySharesUpToPrice( BigDecimal buySharesUpToPrice )
    {
        this.buySharesUpToPrice = buySharesUpToPrice;
    }

    public BigDecimal getStockPriceWhenCreated()
    {
        return stockPriceWhenCreated;
    }

    public void setStockPriceWhenCreated( BigDecimal stockPriceWhenCreated )
    {
        this.stockPriceWhenCreated = stockPriceWhenCreated;
    }

    public List<String> getTags()
    {
        return tags;
    }

    public void setTags( final List<String> tags )
    {
        this.tags = tags;
    }

    public void setTagsArray( final List<String> stockTags )
    {
        this.tags = stockTags;
    }

    public Boolean isCompleted()
    {
        return completed == null ? false : completed;
    }

    public void setCompleted( final Boolean completed )
    {
        this.completed = completed;
    }

    public Boolean getCompleted()
    {
        return completed;
    }

    public String getBuyAfterDate()
    {
        return buyAfterDate;
    }

    public void setBuyAfterDate( final String buyAfterDate )
    {
        this.buyAfterDate = buyAfterDate;
    }

    public String getNotesSourceId()
    {
        return notesSourceId;
    }

    public void setNotesSourceId( final String notesSourceId )
    {
        this.notesSourceId = notesSourceId;
    }

    public String getNotesSourceName()
    {
        return notesSourceName;
    }

    @Override
    public void setNotesSourceName( final String notesSourceName )
    {
        this.notesSourceName = notesSourceName;
    }

    public BigDecimal getAvgAnalystPriceTarget()
    {
        return avgAnalystPriceTarget;
    }

    public void setAvgAnalystPriceTarget( final BigDecimal avgAnalystPriceTarget )
    {
        this.avgAnalystPriceTarget = avgAnalystPriceTarget;
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

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockToBuyQuoteDTO{" );
        sb.append( "id=" ).append( super.getId() );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", comments='" ).append( comments ).append( '\'' );
        sb.append( ", notesSourceId=" ).append( notesSourceId );
        sb.append( ", notesSourceName=" ).append( notesSourceName );
        sb.append( ", buySharesUpToPrice=" ).append( buySharesUpToPrice );
        sb.append( ", stockPriceWhenCreated=" ).append( stockPriceWhenCreated );
        sb.append( ", avgAnalystPriceTarget=" ).append( avgAnalystPriceTarget );
        sb.append( ", completed='" ).append( completed ).append( '\'' );
        sb.append( ", buyAfterDate='" ).append( buyAfterDate ).append( '\'' );
        sb.append( ", tags=" ).append( tags );
        sb.append( ", stockPriceQuoteDTO=" ).append( stockPriceQuoteDTO );
        sb.append( ", stockQuoteDTO=" ).append( stockQuoteDTO );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
