package com.stocktracker.weblayer.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.stocktracker.common.JSONMoneySerializer;
import com.stocktracker.common.JSONTimestampDateTimeSerializer;
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
import java.sql.Timestamp;

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class StockAnalystConsensusDTO extends DatabaseEntityDTO<String>
                                      implements NotesSourceIdContainer,
                                                 UuidDTO,
                                                 CustomerIdContainer,
                                                 StockPriceQuoteDTOContainer,
                                                 StockQuoteDTOContainer
{
    private String tickerSymbol;
    private String customerId;
    private String comments;
    private Integer analystStrongBuyCount;
    private Integer analystBuyCount;
    private Integer analystHoldCount;
    private Integer analystUnderPerformCount;
    private Integer analystSellCount;
    @JsonSerialize( using = JSONTimestampDateTimeSerializer.class )
    private Timestamp analystSentimentDate;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal avgAnalystPriceTarget;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal lowAnalystPriceTarget;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal highAnalystPriceTarget;
    @JsonSerialize( using = JSONTimestampDateTimeSerializer.class )
    private Timestamp analystPriceDate;
    private String notesSourceId;
    private String notesSourceName;

    private BigDecimal stockPriceWhenCreated;
    private StockPriceQuoteDTO stockPriceQuoteDTO;
    private StockQuoteDTO stockQuoteDTO;

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
    public String getCustomerId()
    {
        return this.customerId;
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

    public Integer getAnalystBuyCount()
    {
        return analystBuyCount;
    }

    public void setAnalystBuyCount( Integer analystBuyCount )
    {
        this.analystBuyCount = analystBuyCount;
    }

    public Integer getAnalystSellCount()
    {
        return analystSellCount;
    }

    public void setAnalystSellCount( Integer analystSellCount )
    {
        this.analystSellCount = analystSellCount;
    }

    public Integer getAnalystHoldCount()
    {
        return analystHoldCount;
    }

    public void setAnalystHoldCount( Integer analystHoldCount )
    {
        this.analystHoldCount = analystHoldCount;
    }

    public BigDecimal getAvgAnalystPriceTarget()
    {
        return avgAnalystPriceTarget;
    }

    public void setAvgAnalystPriceTarget( BigDecimal avgAnalystPriceTarget )
    {
        this.avgAnalystPriceTarget = avgAnalystPriceTarget;
    }

    public BigDecimal getLowAnalystPriceTarget()
    {
        return lowAnalystPriceTarget;
    }

    public void setLowAnalystPriceTarget( BigDecimal lowAnalystPriceTarget )
    {
        this.lowAnalystPriceTarget = lowAnalystPriceTarget;
    }

    public BigDecimal getHighAnalystPriceTarget()
    {
        return highAnalystPriceTarget;
    }

    public void setHighAnalystPriceTarget( BigDecimal highAnalystPriceTarget )
    {
        this.highAnalystPriceTarget = highAnalystPriceTarget;
    }

    public Integer getAnalystStrongBuyCount()
    {
        return analystStrongBuyCount;
    }

    public void setAnalystStrongBuyCount( Integer analystStrongBuyCount )
    {
        this.analystStrongBuyCount = analystStrongBuyCount;
    }

    public Integer getAnalystUnderPerformCount()
    {
        return analystUnderPerformCount;
    }

    public void setAnalystUnderPerformCount( Integer analystUnderPerformCount )
    {
        this.analystUnderPerformCount = analystUnderPerformCount;
    }

    public Timestamp getAnalystSentimentDate()
    {
        return analystSentimentDate;
    }

    public void setAnalystSentimentDate( final Timestamp analystSentimentDate )
    {
        this.analystSentimentDate = analystSentimentDate;
    }

    public Timestamp getAnalystPriceDate()
    {
        return analystPriceDate;
    }

    public void setAnalystPriceDate( final Timestamp analystPriceDate )
    {
        this.analystPriceDate = analystPriceDate;
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

    public void setNotesSourceName( final String notesSourceName )
    {
        this.notesSourceName = notesSourceName;
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
    public void setStockPriceQuote( final StockPriceQuoteDTO stockPriceQuoteDTO )
    {
        this.stockPriceQuoteDTO = stockPriceQuoteDTO;
    }

    @Override
    public StockPriceQuoteDTO getStockPriceQuote()
    {
        return this.stockPriceQuoteDTO;
    }

    @Override
    public void setStockQuoteDTO( final StockQuoteDTO stockQuoteDTO )
    {
        this.stockQuoteDTO = stockQuoteDTO;
    }

    @Override
    public StockQuoteDTO getStockQuoteDTO()
    {
        return this.stockQuoteDTO;
    }

    @Override
    public String getCacheKey()
    {
        return tickerSymbol;
    }

    @Override
    public void setCacheKey( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockAnalystConsensusDTO{" );
        sb.append( "id=" ).append( super.getId() );
        sb.append( ", customerId=" ).append( this.getCustomerId() );
        sb.append( ", tickerSymbol='" ).append( getTickerSymbol() ).append( '\'' );
        sb.append( ", comments='" ).append( comments ).append( '\'' );
        sb.append( ", analystStrongBuyCount=" ).append( analystStrongBuyCount );
        sb.append( ", analystBuyCount=" ).append( analystBuyCount );
        sb.append( ", analystHoldCount=" ).append( analystHoldCount );
        sb.append( ", analystUnderPerformCount=" ).append( analystUnderPerformCount );
        sb.append( ", analystSellCount=" ).append( analystSellCount );
        sb.append( ", analystSentimentDate='" ).append( analystSentimentDate ).append( '\'' );
        sb.append( ", avgAnalystPriceTarget=" ).append( avgAnalystPriceTarget );
        sb.append( ", lowAnalystPriceTarget=" ).append( lowAnalystPriceTarget );
        sb.append( ", highAnalystPriceTarget=" ).append( highAnalystPriceTarget );
        sb.append( ", analystPriceDate=" ).append( analystPriceDate );
        sb.append( ", notesSourceId=" ).append( notesSourceId );
        sb.append( ", notesSourceName='" ).append( notesSourceName ).append( '\'' );
        sb.append( ", stockPriceWhenCreated='" ).append( stockPriceWhenCreated ).append( '\'' );
        sb.append( ", stockPriceQuoteDTO=" ).append( stockPriceQuoteDTO );
        sb.append( ", stockQuoteDTO=" ).append( stockQuoteDTO );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
