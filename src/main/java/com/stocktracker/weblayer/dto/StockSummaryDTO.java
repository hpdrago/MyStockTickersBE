package com.stocktracker.weblayer.dto;

import com.stocktracker.servicelayer.service.YahooStockService;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class StockSummaryDTO implements YahooStockService.YahooStockContainer
{
    /*
     * Entity (DB columns)
     */
    private Integer id;
    private Integer customerId;
    private String tickerSymbol;
    private String comments;
    private Integer analystStrongBuyCount;
    private Integer analystBuyCount;
    private Integer analystHoldCount;
    private Integer analystUnderPerformCount;
    private Integer analystSellCount;
    private Timestamp lastAnalystSentimentDate;
    private Timestamp nextCatalystDate;
    private String nextCatalystDesc;
    private BigDecimal avgAnalystPriceTarget;
    private BigDecimal lowAnalystPriceTarget;
    private BigDecimal highAnalystPriceTarget;
    private Timestamp lastAnalystPriceDate;
    private BigDecimal buySharesBelow;

    /*
     * Calculated columns
     */
    private BigDecimal lastPrice;
    private Timestamp lastPriceChange;
    private BigDecimal avgUpsidePercent;
    private String companyName;

    public static StockSummaryDTO newInstance()
    {
        return new StockSummaryDTO();
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

    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public void setTickerSymbol( String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
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

    public Timestamp getNextCatalystDate()
    {
        return nextCatalystDate;
    }

    public void setNextCatalystDate( Timestamp nextCatalystDate )
    {
        this.nextCatalystDate = nextCatalystDate;
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

    public BigDecimal getBuySharesBelow()
    {
        return buySharesBelow;
    }

    public void setBuySharesBelow( BigDecimal buySharesBelow )
    {
        this.buySharesBelow = buySharesBelow;
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

        final StockSummaryDTO that = (StockSummaryDTO) o;

        return id.equals( that.id );
    }

    @Override
    public int hashCode()
    {
        return id.hashCode();
    }

    public BigDecimal getLastPrice()
    {
        return lastPrice;
    }

    @Override
    public Timestamp getLastPriceChange()
    {
        return null;
    }

    public void setLastPrice( BigDecimal lastPrice )
    {
        this.lastPrice = lastPrice;
    }

    @Override
    public void setLastPriceChange( final Timestamp lastPriceChange )
    {
        this.lastPriceChange = lastPriceChange;
    }

    @Override
    public String getCompanyName()
    {
        return null;
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

    public Timestamp getLastAnalystSentimentDate()
    {
        return lastAnalystSentimentDate;
    }

    public void setLastAnalystSentimentDate( final Timestamp lastAnalystSentimentDate )
    {
        this.lastAnalystSentimentDate = lastAnalystSentimentDate;
    }

    public Timestamp getLastAnalystPriceDate()
    {
        return lastAnalystPriceDate;
    }

    public void setLastAnalystPriceDate( final Timestamp lastAnalystPriceDate )
    {
        this.lastAnalystPriceDate = lastAnalystPriceDate;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockSummaryDTO{" );
        sb.append( "id=" ).append( id );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", comments='" ).append( comments ).append( '\'' );
        sb.append( ", analystStrongBuyCount=" ).append( analystStrongBuyCount );
        sb.append( ", analystBuyCount=" ).append( analystBuyCount );
        sb.append( ", analystHoldCount=" ).append( analystHoldCount );
        sb.append( ", analystUnderPerformCount=" ).append( analystUnderPerformCount );
        sb.append( ", analystSellCount=" ).append( analystSellCount );
        sb.append( ", lastAnalystSentimentDate=" ).append( lastAnalystSentimentDate );
        sb.append( ", nextCatalystDate=" ).append( nextCatalystDate );
        sb.append( ", nextCatalystDesc='" ).append( nextCatalystDesc ).append( '\'' );
        sb.append( ", avgAnalystPriceTarget=" ).append( avgAnalystPriceTarget );
        sb.append( ", lowAnalystPriceTarget=" ).append( lowAnalystPriceTarget );
        sb.append( ", highAnalystPriceTarget=" ).append( highAnalystPriceTarget );
        sb.append( ", lastAnalystPriceDate=" ).append( lastAnalystPriceDate );
        sb.append( ", buySharesBelow=" ).append( buySharesBelow );
        sb.append( ", lastPrice=" ).append( lastPrice );
        sb.append( ", lastPriceChange=" ).append( lastPriceChange );
        sb.append( ", avgUpsidePercent=" ).append( avgUpsidePercent );
        sb.append( ", companyName='" ).append( companyName ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }
}
