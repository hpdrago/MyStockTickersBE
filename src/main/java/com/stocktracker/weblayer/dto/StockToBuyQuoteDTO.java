package com.stocktracker.weblayer.dto;

import com.stocktracker.servicelayer.service.StockNoteSourceEntityService;
import com.stocktracker.servicelayer.service.stocks.StockPriceContainer;
import com.stocktracker.servicelayer.stockinformationprovider.StockPriceQuoteDTO;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class StockToBuyQuoteDTO extends StockPriceQuoteDTO implements StockPriceContainer,
                                                                      StockNoteSourceEntityService.StockNoteSourceDTOContainer,
                                                                      VersionedDTO<Integer>
{
    /*
     * Entity (DB columns)
     */
    private Integer id;
    private Integer customerId;
    private String comments;
    private Integer notesSourceId;
    private String notesSourceName;
    private BigDecimal buySharesUpToPrice;
    private BigDecimal stockPriceWhenCreated;
    private Boolean completed;
    private String buyAfterDate;
    private String createDate;
    private String[] tags;
    private BigDecimal avgAnalystPriceTarget;
    private Integer version;

    public Integer getId()
    {
        return id;
    }

    public void setId( Integer id )
    {
        this.id = id;
    }

    public Integer getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( Integer customerId )
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

    public String getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate( final String createDate )
    {
        this.createDate = createDate;
    }

    public String[] getTags()
    {
        return tags;
    }

    public void setTags( final String[] tags )
    {
        this.tags = tags;
    }

    public void setTagsArray( final List<String> stockTags )
    {
        this.tags = stockTags.toArray(new String[stockTags.size()]);
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

    public Integer getNotesSourceId()
    {
        return notesSourceId;
    }

    public void setNotesSourceId( final Integer notesSourceId )
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
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }

        final StockToBuyQuoteDTO that = (StockToBuyQuoteDTO) o;

        return id.equals( that.id );
    }

    @Override
    public int hashCode()
    {
        return id.hashCode();
    }


    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockToBuyQuoteDTO{" );
        sb.append( "id=" ).append( id );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( ", comments='" ).append( comments ).append( '\'' );
        sb.append( ", notesSourceId=" ).append( notesSourceId );
        sb.append( ", buySharesUpToPrice=" ).append( buySharesUpToPrice );
        sb.append( ", stockPriceWhenCreated=" ).append( stockPriceWhenCreated );
        sb.append( ", avgAnalystPriceTarget=" ).append( avgAnalystPriceTarget );
        sb.append( ", completed='" ).append( completed ).append( '\'' );
        sb.append( ", buyAfterDate='" ).append( buyAfterDate ).append( '\'' );
        sb.append( ", createDate='" ).append( createDate ).append( '\'' );
        sb.append( ", tags=" ).append( Arrays.toString( tags ) );
        sb.append( ", version=" ).append( version );
        sb.append( '}' );
        return sb.toString();
    }
}
