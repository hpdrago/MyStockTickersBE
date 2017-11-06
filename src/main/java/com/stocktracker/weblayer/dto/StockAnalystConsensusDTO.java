package com.stocktracker.weblayer.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.stocktracker.common.JSONDateConverter;
import com.stocktracker.common.JSONMoneySerializer;
import com.stocktracker.servicelayer.service.StockService;
import com.stocktracker.servicelayer.service.stockinformationprovider.StockQuoteState;
import com.stocktracker.servicelayer.service.stockinformationprovider.StockTickerQuote;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class StockAnalystConsensusDTO extends StockTickerQuote implements StockService.StockQuoteContainer
{
    /*
     * Entity (DB columns)
     */
    private Integer id;
    private Integer customerId;
    private String comments;
    private Integer analystStrongBuyCount;
    private Integer analystBuyCount;
    private Integer analystHoldCount;
    private Integer analystUnderPerformCount;
    private Integer analystSellCount;
    private String analystSentimentDate;
    private String nextCatalystDate;
    private String nextCatalystDesc;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal avgAnalystPriceTarget;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal lowAnalystPriceTarget;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal highAnalystPriceTarget;
    private String analystPriceDate;

    /*
     * Calculated columns
     */
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal avgUpsidePercent;
    private String companyName;

    public static StockAnalystConsensusDTO newInstance()
    {
        return new StockAnalystConsensusDTO();
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

    public String getNextCatalystDate()
    {
        return nextCatalystDate;
    }

    public void setNextCatalystDate( String nextCatalystDate )
    {
        this.nextCatalystDate = nextCatalystDate;
    }

    public void setNextCatalystDate( Timestamp nextCatalystDate )
    {
        if ( nextCatalystDate == null )
        {
            this.nextCatalystDate = null;
        }
        else
        {
            this.nextCatalystDate = JSONDateConverter.toString( nextCatalystDate );
        }
    }

    public String getNextCatalystDesc()
    {
        return nextCatalystDesc;
    }

    public void setNextCatalystDesc( String nextCatalystDesc )
    {
        this.nextCatalystDesc = nextCatalystDesc;
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

    @Override
    public String getCompanyName()
    {
        return this.companyName;
    }

    @Override
    public void setCompanyName( final String companyName )
    {
        this.companyName = companyName;
    }

    public BigDecimal getAvgUpsidePercent()
    {
        return avgUpsidePercent;
    }

    public void setAvgUpsidePercent( BigDecimal avgUpsidePercent )
    {
        this.avgUpsidePercent = avgUpsidePercent;
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

    public String getAnalystSentimentDate()
    {
        return analystSentimentDate;
    }

    public void setAnalystSentimentDate( final Timestamp analystSentimentDate )
    {
        if ( analystSentimentDate == null )
        {
            this.analystSentimentDate = null;
        }
        else
        {
            this.analystSentimentDate = JSONDateConverter.toString( analystSentimentDate );
        }
    }

    public void setAnalystSentimentDate( final String analystSentimentDate )
    {
        this.analystSentimentDate = analystSentimentDate;
    }

    public String getAnalystPriceDate()
    {
        return analystPriceDate;
    }

    public void setAnalystPriceDate( final String analystPriceDate )
    {
        this.analystPriceDate = analystPriceDate;
    }

    public void setAnalystPriceDate( final Timestamp analystPriceDate )
    {
        if ( analystPriceDate == null )
        {
            this.analystPriceDate = null;
        }
        else
        {
            this.analystPriceDate = JSONDateConverter.toString( analystPriceDate );
        }
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

        final StockAnalystConsensusDTO that = (StockAnalystConsensusDTO) o;

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
        final StringBuilder sb = new StringBuilder( "StockAnalystConsensusDTO{" );
        sb.append( "id=" ).append( id );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( ", comments='" ).append( comments ).append( '\'' );
        sb.append( ", analystStrongBuyCount=" ).append( analystStrongBuyCount );
        sb.append( ", analystBuyCount=" ).append( analystBuyCount );
        sb.append( ", analystHoldCount=" ).append( analystHoldCount );
        sb.append( ", analystUnderPerformCount=" ).append( analystUnderPerformCount );
        sb.append( ", analystSellCount=" ).append( analystSellCount );
        sb.append( ", analystSentimentDate=" ).append( analystSentimentDate );
        sb.append( ", nextCatalystDate=" ).append( nextCatalystDate );
        sb.append( ", nextCatalystDesc='" ).append( nextCatalystDesc ).append( '\'' );
        sb.append( ", avgAnalystPriceTarget=" ).append( avgAnalystPriceTarget );
        sb.append( ", lowAnalystPriceTarget=" ).append( lowAnalystPriceTarget );
        sb.append( ", highAnalystPriceTarget=" ).append( highAnalystPriceTarget );
        sb.append( ", analystPriceDate=" ).append( analystPriceDate );
        sb.append( ", avgUpsidePercent=" ).append( avgUpsidePercent );
        sb.append( ", companyName='" ).append( companyName ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }
}
