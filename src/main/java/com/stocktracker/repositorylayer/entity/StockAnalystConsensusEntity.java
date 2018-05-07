package com.stocktracker.repositorylayer.entity;

import com.stocktracker.repositorylayer.common.CustomerUuidContainer;
import com.stocktracker.servicelayer.service.stocks.TickerSymbolContainer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
@Entity
@Table( name = "stock_analyst_consensus", schema = "stocktracker", catalog = "" )
public class StockAnalystConsensusEntity extends UUIDEntity
                                         implements CustomerUuidContainer,
                                                    TickerSymbolContainer
{
    private UUID customerUuid;
    private String tickerSymbol;
    private String comments;
    private Integer analystStrongBuyCount;
    private Integer analystBuyCount;
    private Integer analystHoldCount;
    private Integer analystUnderPerformCount;
    private Integer analystSellCount;
    private Timestamp analystSentimentDate;
    private BigDecimal avgAnalystPriceTarget;
    private BigDecimal lowAnalystPriceTarget;
    private BigDecimal highAnalystPriceTarget;
    private Timestamp analystPriceDate;
    private BigDecimal stockPriceWhenCreated;
    private StockNoteSourceEntity stockNoteSourceByNoteSourceUuid;
    private Integer noteSourceUuid;

    public static StockAnalystConsensusEntity newInstance()
    {
        return new StockAnalystConsensusEntity();
    }

    @Basic
    @Column( name = "customer_uuid" )
    public UUID getCustomerUuid()
    {
        return customerUuid;
    }

    public void setCustomerUuid( final UUID customerUuid )
    {
        this.customerUuid = customerUuid;
    }

    @Basic
    @Column( name = "ticker_symbol" )
    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public void setTickerSymbol( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    @Basic
    @Column( name = "comments", nullable = true, length = 4000 )
    public String getComments()
    {
        return comments;
    }

    public void setComments( final String comments )
    {
        this.comments = comments;
    }

    @Basic
    @Column( name = "analyst_buy_count", nullable = true )
    public Integer getAnalystBuyCount()
    {
        return analystBuyCount;
    }

    public void setAnalystBuyCount( final Integer analystBuyCount )
    {
        this.analystBuyCount = analystBuyCount;
    }

    @Basic
    @Column( name = "analyst_sell_count", nullable = true )
    public Integer getAnalystSellCount()
    {
        return analystSellCount;
    }

    public void setAnalystSellCount( final Integer analystSellCount )
    {
        this.analystSellCount = analystSellCount;
    }

    @Basic
    @Column( name = "analyst_hold_count", nullable = true )
    public Integer getAnalystHoldCount()
    {
        return analystHoldCount;
    }

    public void setAnalystHoldCount( final Integer analystHoldCount )
    {
        this.analystHoldCount = analystHoldCount;
    }

    @Basic
    @Column( name = "analyst_strong_buy_count", nullable = true )
    public Integer getAnalystStrongBuyCount()
    {
        return analystStrongBuyCount;
    }

    public void setAnalystStrongBuyCount( final Integer analystStrongBuyCount )
    {
        this.analystStrongBuyCount = analystStrongBuyCount;
    }

    @Basic
    @Column( name = "analyst_under_perform_count", nullable = true )
    public Integer getAnalystUnderPerformCount()
    {
        return analystUnderPerformCount;
    }

    public void setAnalystUnderPerformCount( final Integer analystUnderPerformCount )
    {
        this.analystUnderPerformCount = analystUnderPerformCount;
    }

    @Basic
    @Column( name = "avg_analyst_price_target", nullable = true, precision = 2 )
    public BigDecimal getAvgAnalystPriceTarget()
    {
        return avgAnalystPriceTarget;
    }

    public void setAvgAnalystPriceTarget( final BigDecimal avgAnalystPriceTarget )
    {
        this.avgAnalystPriceTarget = avgAnalystPriceTarget;
    }

    @Basic
    @Column( name = "low_analyst_price_target", nullable = true, precision = 2 )
    public BigDecimal getLowAnalystPriceTarget()
    {
        return lowAnalystPriceTarget;
    }

    public void setLowAnalystPriceTarget( final BigDecimal lowAnalystPriceTarget )
    {
        this.lowAnalystPriceTarget = lowAnalystPriceTarget;
    }

    @Basic
    @Column( name = "high_analyst_price_target", nullable = true, precision = 2 )
    public BigDecimal getHighAnalystPriceTarget()
    {
        return highAnalystPriceTarget;
    }

    public void setHighAnalystPriceTarget( final BigDecimal highAnalystPriceTarget )
    {
        this.highAnalystPriceTarget = highAnalystPriceTarget;
    }

    @Basic
    @Column( name = "analyst_sentiment_date", nullable = true, insertable = false, updatable = false )
    public Timestamp getAnalystSentimentDate()
    {
        return analystSentimentDate;
    }

    public void setAnalystSentimentDate( final Timestamp analystSentimentDate )
    {
        this.analystSentimentDate = analystSentimentDate;
    }

    @Basic
    @Column( name = "analyst_price_date", nullable = true, insertable = false, updatable = false )
    public Timestamp getAnalystPriceDate()
    {
        return analystPriceDate;
    }

    public void setAnalystPriceDate( final Timestamp analystPriceDate )
    {
        this.analystPriceDate = analystPriceDate;
    }

    @Basic
    @Column( name = "stock_price_when_created" )
    public BigDecimal getStockPriceWhenCreated()
    {
        return stockPriceWhenCreated;
    }

    public void setStockPriceWhenCreated( final BigDecimal stockPriceWhenCreated )
    {
        this.stockPriceWhenCreated = stockPriceWhenCreated;
    }

    @ManyToOne
    @JoinColumn( name = "notes_source_uuid", referencedColumnName = "uuid" )
    public StockNoteSourceEntity getStockNoteSourceByNoteSourceUuid()
    {
        return stockNoteSourceByNoteSourceUuid;
    }

    public void setStockNoteSourceByNoteSourceUuid( final StockNoteSourceEntity stockNoteSourceByNoteSourceUuid )
    {
        this.stockNoteSourceByNoteSourceUuid = stockNoteSourceByNoteSourceUuid;
    }

    @Basic
    @Column( name = "notes_source_uuid", insertable = false, updatable = false )
    public Integer getNoteSourceUuid()
    {
        return noteSourceUuid;
    }

    public void setNoteSourceUuid( final Integer noteSourceUuid )
    {
        this.noteSourceUuid = noteSourceUuid;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockAnalystConsensusEntity{" );
        sb.append( "uuid=" ).append( getUuidString() );
        sb.append( ", customerUuid=" ).append( customerUuid );
        sb.append( ", tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", comments='" ).append( comments ).append( '\'' );
        sb.append( ", analystStrongBuyCount=" ).append( analystStrongBuyCount );
        sb.append( ", analystBuyCount=" ).append( analystBuyCount );
        sb.append( ", analystHoldCount=" ).append( analystHoldCount );
        sb.append( ", analystUnderPerformCount=" ).append( analystUnderPerformCount );
        sb.append( ", analystSellCount=" ).append( analystSellCount );
        sb.append( ", analystSentimentDate=" ).append( analystSentimentDate );
        sb.append( ", avgAnalystPriceTarget=" ).append( avgAnalystPriceTarget );
        sb.append( ", lowAnalystPriceTarget=" ).append( lowAnalystPriceTarget );
        sb.append( ", highAnalystPriceTarget=" ).append( highAnalystPriceTarget );
        sb.append( ", analystPriceDate=" ).append( analystPriceDate );
        sb.append( ", stockPriceWhenCreated=" ).append( stockPriceWhenCreated );
        sb.append( ", noteSourceUuid=" ).append( noteSourceUuid );
        sb.append( ", stockNoteSourceByNoteSourceId=" ).append( stockNoteSourceByNoteSourceUuid );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
