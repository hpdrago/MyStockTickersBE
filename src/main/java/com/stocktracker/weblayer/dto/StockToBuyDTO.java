package com.stocktracker.weblayer.dto;

import com.stocktracker.servicelayer.service.StockNoteSourceService;
import com.stocktracker.servicelayer.service.StockQuoteService;
import com.stocktracker.servicelayer.service.stockinformationprovider.StockTickerQuote;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class StockToBuyDTO extends StockTickerQuote implements StockQuoteService.StockQuoteContainer,
                                                               StockNoteSourceService.StockNoteSourceDTOContainer
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

    public static StockToBuyDTO newInstance()
    {
        return new StockToBuyDTO();
    }

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

        final StockToBuyDTO that = (StockToBuyDTO) o;

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
        final StringBuilder sb = new StringBuilder( "StockToBuyDTO{" );
        sb.append( "id=" ).append( id );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( ", comments='" ).append( comments ).append( '\'' );
        sb.append( ", notesSourceId=" ).append( notesSourceId );
        sb.append( ", buySharesUpToPrice=" ).append( buySharesUpToPrice );
        sb.append( ", stockPriceWhenCreated=" ).append( stockPriceWhenCreated );
        sb.append( ", completed='" ).append( completed ).append( '\'' );
        sb.append( ", buyAfterDate='" ).append( buyAfterDate ).append( '\'' );
        sb.append( ", createDate='" ).append( createDate ).append( '\'' );
        sb.append( ", tags=" ).append( Arrays.toString( tags ) );
        sb.append( '}' );
        return sb.toString();
    }
}
