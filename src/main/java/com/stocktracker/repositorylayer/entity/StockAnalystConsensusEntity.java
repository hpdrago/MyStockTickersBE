package com.stocktracker.repositorylayer.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table( name = "stock_analyst_consensus", schema = "stocktracker", catalog = "" )
public class StockAnalystConsensusEntity implements VersionedEntity<Integer>
{
    private Integer id;
    private Integer customerId;
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
    private Timestamp createDate;
    private Timestamp updateDate;
    private BigDecimal stockPriceWhenCreated;
    private StockNoteSourceEntity stockNoteSourceByNoteSourceId;
    private Integer noteSourceId;
    private Integer version;

    public static StockAnalystConsensusEntity newInstance()
    {
        return new StockAnalystConsensusEntity();
    }

    @Id
    @Column( name = "id", nullable = false )
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    public Integer getId()
    {
        return id;
    }

    public void setId( final Integer id )
    {
        this.id = id;
    }

    @Basic
    @Column( name = "customer_id" )
    public Integer getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( final Integer customerId )
    {
        this.customerId = customerId;
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
    @Column( name = "create_date", nullable = false )
    public Timestamp getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate( final Timestamp createDate )
    {
        this.createDate = createDate;
    }

    @Basic
    @Column( name = "update_date", nullable = true )
    public Timestamp getUpdateDate()
    {
        return updateDate;
    }

    public void setUpdateDate( final Timestamp updateDate )
    {
        this.updateDate = updateDate;
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
    @JoinColumn( name = "notes_source_id", referencedColumnName = "id" )
    public StockNoteSourceEntity getStockNoteSourceByNoteSourceId()
    {
        return stockNoteSourceByNoteSourceId;
    }

    public void setStockNoteSourceByNoteSourceId( final StockNoteSourceEntity stockNoteSourceByNoteSourceId )
    {
        this.stockNoteSourceByNoteSourceId = stockNoteSourceByNoteSourceId;
    }

    @Basic
    @Column( name = "notes_source_id", insertable = false, updatable = false )
    public Integer getNoteSourceId()
    {
        return noteSourceId;
    }

    public void setNoteSourceId( final Integer noteSourceId )
    {
        this.noteSourceId = noteSourceId;
    }

    @Override
    public Integer getVersion()
    {
        return version;
    }

    @Basic
    @Column( name = "version" )
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
        if ( !(o instanceof StockAnalystConsensusEntity) )
        {
            return false;
        }
        final StockAnalystConsensusEntity that = (StockAnalystConsensusEntity) o;
        return Objects.equals( id, that.id );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( id );
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockAnalystConsensusEntity{" );
        sb.append( "id=" ).append( id );
        sb.append( ", customerId=" ).append( customerId );
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
        sb.append( ", createDate=" ).append( createDate );
        sb.append( ", updateDate=" ).append( updateDate );
        sb.append( ", stockPriceWhenCreated=" ).append( stockPriceWhenCreated );
        sb.append( ", noteSourceId=" ).append( noteSourceId );
        sb.append( ", stockNoteSourceByNoteSourceId=" ).append( stockNoteSourceByNoteSourceId );
        sb.append( ", version=" ).append( version );
        sb.append( '}' );
        return sb.toString();
    }
}
