package com.stocktracker.repositorylayer.entity;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
@Entity
@Table( name = "stock_quote", schema = "stocktracker", catalog = "" )
public class StockQuoteEntity implements VersionedEntity<String>
{
    private String tickerSymbol;
    private String calculationPrice;
    private BigDecimal openPrice;
    private BigDecimal closePrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal latestPrice;
    private String latestPriceSource;
    private String latestPriceTime;
    private Long latestUpdate;
    private Long latestVolume;
    private BigDecimal delayedPrice;
    private Long delayedPriceTime;
    private BigDecimal previousClose;
    private BigDecimal change;
    private BigDecimal changePercent;
    private Long thirtyDayAvgVolume;
    private Long marketCap;
    private BigDecimal peRatio;
    private BigDecimal week52High;
    private BigDecimal week52Low;
    private BigDecimal ytdChangePercent;
    private Timestamp lastQuoteRequestDate;
    private String discontinuedInd;
    private Timestamp createDate;
    private Timestamp updateDate;
    private Integer version;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column( name = "ticker_symbol", nullable = false, length = 25 )
    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public void setTickerSymbol( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    @Basic
    @Column( name = "discontinued_ind", nullable = true, length = 1 )
    public String getDiscontinuedInd()
    {
        return discontinuedInd;
    }

    public void setDiscontinuedInd( final String discontinuedInd )
    {
        this.discontinuedInd = discontinuedInd;
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

    @Transient
    @Override
    public String getId()
    {
        return this.tickerSymbol;
    }

    @Basic
    @Column( name = "version", nullable = false )
    public Integer getVersion()
    {
        return version;
    }

    public void setVersion( final Integer version )
    {
        this.version = version;
    }

    @Basic
    @Column( name = "calculation_price", nullable = true, length = 12 )
    public String getCalculationPrice()
    {
        return calculationPrice;
    }

    public void setCalculationPrice( final String calculationPrice )
    {
        this.calculationPrice = calculationPrice;
    }

    @Basic
    @Column( name = "open_price", nullable = true, precision = 2 )
    public BigDecimal getOpenPrice()
    {
        return openPrice;
    }

    public void setOpenPrice( final BigDecimal openPrice )
    {
        this.openPrice = openPrice;
    }

    @Basic
    @Column( name = "close_price", nullable = true, precision = 2 )
    public BigDecimal getClosePrice()
    {
        return closePrice;
    }

    public void setClosePrice( final BigDecimal closePrice )
    {
        this.closePrice = closePrice;
    }

    @Basic
    @Column( name = "high_price", nullable = true, precision = 2 )
    public BigDecimal getHighPrice()
    {
        return highPrice;
    }

    public void setHighPrice( final BigDecimal highPrice )
    {
        this.highPrice = highPrice;
    }

    @Basic
    @Column( name = "low_price", nullable = true, precision = 2 )
    public BigDecimal getLowPrice()
    {
        return lowPrice;
    }

    public void setLowPrice( final BigDecimal lowPrice )
    {
        this.lowPrice = lowPrice;
    }

    @Basic
    @Column( name = "latest_price", nullable = true, precision = 2 )
    public BigDecimal getLatestPrice()
    {
        return latestPrice;
    }

    public void setLatestPrice( final BigDecimal latestPrice )
    {
        this.latestPrice = latestPrice;
    }

    @Basic
    @Column( name = "latest_price_source", nullable = true, length = 25 )
    public String getLatestPriceSource()
    {
        return latestPriceSource;
    }

    public void setLatestPriceSource( final String latestPriceSource )
    {
        this.latestPriceSource = latestPriceSource;
    }

    @Basic
    @Column( name = "latest_price_time", nullable = true )
    public String getLatestPriceTime()
    {
        return latestPriceTime;
    }

    public void setLatestPriceTime( final String latestPriceTime )
    {
        this.latestPriceTime = latestPriceTime;
    }

    @Basic
    @Column( name = "latest_update", nullable = true )
    public Long getLatestUpdate()
    {
        return latestUpdate;
    }

    public void setLatestUpdate( final Long latestUpdate )
    {
        this.latestUpdate = latestUpdate;
    }

    @Basic
    @Column( name = "latest_volume", nullable = true )
    public Long getLatestVolume()
    {
        return latestVolume;
    }

    public void setLatestVolume( final Long latestVolume )
    {
        this.latestVolume = latestVolume;
    }

    @Basic
    @Column( name = "delayed_price", nullable = true, precision = 2 )
    public BigDecimal getDelayedPrice()
    {
        return delayedPrice;
    }

    public void setDelayedPrice( final BigDecimal delayedPrice )
    {
        this.delayedPrice = delayedPrice;
    }

    @Basic
    @Column( name = "delayed_price_time", nullable = true )
    public Long getDelayedPriceTime()
    {
        return delayedPriceTime;
    }

    public void setDelayedPriceTime( final Long delayedPriceTime )
    {
        this.delayedPriceTime = delayedPriceTime;
    }

    @Basic
    @Column( name = "previous_close", nullable = true, precision = 2 )
    public BigDecimal getPreviousClose()
    {
        return previousClose;
    }

    public void setPreviousClose( final BigDecimal previousClose )
    {
        this.previousClose = previousClose;
    }

    @Basic
    @Column( name = "change", nullable = true, length = 45 )
    public BigDecimal getChange()
    {
        return change;
    }

    public void setChange( final BigDecimal change )
    {
        this.change = change;
    }

    @Basic
    @Column( name = "change_percent", nullable = true, precision = 2 )
    public BigDecimal getChangePercent()
    {
        return changePercent;
    }

    public void setChangePercent( final BigDecimal changePercent )
    {
        this.changePercent = changePercent;
    }

    @Basic
    @Column( name = "thirty_day_avg_volume", nullable = true )
    public Long getThirtyDayAvgVolume()
    {
        return thirtyDayAvgVolume;
    }

    public void setThirtyDayAvgVolume( final Long thirtyDayAvgVolume )
    {
        this.thirtyDayAvgVolume = thirtyDayAvgVolume;
    }

    @Basic
    @Column( name = "market_cap", nullable = true )
    public Long getMarketCap()
    {
        return marketCap;
    }

    public void setMarketCap( final Long marketCap )
    {
        this.marketCap = marketCap;
    }

    @Basic
    @Column( name = "pe_ratio", nullable = true, precision = 2 )
    public BigDecimal getPeRatio()
    {
        return peRatio;
    }

    public void setPeRatio( final BigDecimal peRatio )
    {
        this.peRatio = peRatio;
    }

    @Basic
    @Column( name = "week_52_high", nullable = true, precision = 2 )
    public BigDecimal getWeek52High()
    {
        return week52High;
    }

    public void setWeek52High( final BigDecimal week52High )
    {
        this.week52High = week52High;
    }

    @Basic
    @Column( name = "week_52_low", nullable = true, precision = 2 )
    public BigDecimal getWeek52Low()
    {
        return week52Low;
    }

    public void setWeek52Low( final BigDecimal week52Low )
    {
        this.week52Low = week52Low;
    }

    @Basic
    @Column( name = "ytd_change_percent", nullable = true, precision = 2 )
    public BigDecimal getYtdChangePercent()
    {
        return ytdChangePercent;
    }

    public void setYtdChangePercent( final BigDecimal ytdChangePercent )
    {
        this.ytdChangePercent = ytdChangePercent;
    }

    @Basic
    @Column( name = "last_quote_request_date", nullable = true )
    public Timestamp getLastQuoteRequestDate()
    {
        return lastQuoteRequestDate;
    }

    public void setLastQuoteRequestDate( final Timestamp lastQuoteRequestDate )
    {
        this.lastQuoteRequestDate = lastQuoteRequestDate;
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
        final StockQuoteEntity that = (StockQuoteEntity) o;
        return Objects.equals( tickerSymbol, that.tickerSymbol ) &&
               Objects.equals( discontinuedInd, that.discontinuedInd ) &&
               Objects.equals( createDate, that.createDate ) &&
               Objects.equals( updateDate, that.updateDate ) &&
               Objects.equals( version, that.version ) &&
               Objects.equals( calculationPrice, that.calculationPrice ) &&
               Objects.equals( openPrice, that.openPrice ) &&
               Objects.equals( closePrice, that.closePrice ) &&
               Objects.equals( highPrice, that.highPrice ) &&
               Objects.equals( lowPrice, that.lowPrice ) &&
               Objects.equals( latestPrice, that.latestPrice ) &&
               Objects.equals( latestPriceSource, that.latestPriceSource ) &&
               Objects.equals( latestPriceTime, that.latestPriceTime ) &&
               Objects.equals( latestUpdate, that.latestUpdate ) &&
               Objects.equals( latestVolume, that.latestVolume ) &&
               Objects.equals( delayedPrice, that.delayedPrice ) &&
               Objects.equals( delayedPriceTime, that.delayedPriceTime ) &&
               Objects.equals( previousClose, that.previousClose ) &&
               Objects.equals( change, that.change ) &&
               Objects.equals( changePercent, that.changePercent ) &&
               Objects.equals( thirtyDayAvgVolume, that.thirtyDayAvgVolume ) &&
               Objects.equals( marketCap, that.marketCap ) &&
               Objects.equals( peRatio, that.peRatio ) &&
               Objects.equals( week52High, that.week52High ) &&
               Objects.equals( week52Low, that.week52Low ) &&
               Objects.equals( ytdChangePercent, that.ytdChangePercent ) &&
               Objects.equals( lastQuoteRequestDate, that.lastQuoteRequestDate );
    }

    @Override
    public int hashCode()
    {
        return Objects
            .hash( tickerSymbol, discontinuedInd, createDate, updateDate, version, calculationPrice, openPrice, closePrice, highPrice, lowPrice, latestPrice, latestPriceSource, latestPriceTime, latestUpdate, latestVolume, delayedPrice, delayedPriceTime, previousClose, change, changePercent, thirtyDayAvgVolume, marketCap, peRatio, week52High, week52Low, ytdChangePercent, lastQuoteRequestDate );
    }
}
