package com.stocktracker.weblayer.dto;

import com.stocktracker.repositorylayer.entity.StockTagEntity;
import com.stocktracker.servicelayer.service.stocks.StockPriceWhenCreatedContainer;
import com.stocktracker.weblayer.dto.common.CustomerIdContainer;
import com.stocktracker.weblayer.dto.common.NotesSourceIdContainer;
import com.stocktracker.weblayer.dto.common.TagsContainer;
import com.stocktracker.weblayer.dto.common.UuidDTO;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class StockToBuyDTO extends StockQuoteDTO
                           implements NotesSourceIdContainer,
                                      UuidDTO,
                                      CustomerIdContainer,
                                      StockPriceWhenCreatedContainer,
                                      TagsContainer

{
    /*
     * Entity (DB columns)
     */
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
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockToBuyQuoteDTO{" );
        sb.append( "id=" ).append( super.getId() );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", tickerSymbol='" ).append( super.getTickerSymbol() ).append( '\'' );
        sb.append( ", comments='" ).append( comments ).append( '\'' );
        sb.append( ", notesSourceId=" ).append( notesSourceId );
        sb.append( ", notesSourceName=" ).append( notesSourceName );
        sb.append( ", buySharesUpToPrice=" ).append( buySharesUpToPrice );
        sb.append( ", stockPriceWhenCreated=" ).append( stockPriceWhenCreated );
        sb.append( ", avgAnalystPriceTarget=" ).append( avgAnalystPriceTarget );
        sb.append( ", completed='" ).append( completed ).append( '\'' );
        sb.append( ", buyAfterDate='" ).append( buyAfterDate ).append( '\'' );
        sb.append( ", tags=" ).append( tags );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
