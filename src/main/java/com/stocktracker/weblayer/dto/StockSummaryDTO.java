package com.stocktracker.weblayer.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class StockSummaryDTO
{
    private Integer id;
    private Integer customerId;
    private String tickerSymbol;
    private String comments;
    private Integer analystBuyCount;
    private Integer analystSellCount;
    private Integer analystHoldCount;
    private Timestamp nextCatalystDate;
    private String nextCatalystDesc;
    private BigDecimal avgAnalystPriceTarget;
    private BigDecimal lowAnalystPriceTarget;
    private BigDecimal highAnalystPriceTarget;
    private BigDecimal buySharesBelow;

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

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockSummaryDTO{" );
        sb.append( "id=" ).append( id );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", comments='" ).append( comments ).append( '\'' );
        sb.append( ", analystBuyCount=" ).append( analystBuyCount );
        sb.append( ", analystSellCount=" ).append( analystSellCount );
        sb.append( ", analystHoldCount=" ).append( analystHoldCount );
        sb.append( ", nextCatalystDate=" ).append( nextCatalystDate );
        sb.append( ", nextCatalystDesc='" ).append( nextCatalystDesc ).append( '\'' );
        sb.append( ", avgAnalystPriceTarget=" ).append( avgAnalystPriceTarget );
        sb.append( ", lowAnalystPriceTarget=" ).append( lowAnalystPriceTarget );
        sb.append( ", highAnalystPriceTarget=" ).append( highAnalystPriceTarget );
        sb.append( ", buySharesBelow=" ).append( buySharesBelow );
        sb.append( '}' );
        return sb.toString();
    }
}
