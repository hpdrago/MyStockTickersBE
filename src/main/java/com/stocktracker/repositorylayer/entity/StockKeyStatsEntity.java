package com.stocktracker.repositorylayer.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table( name = "stock_key_stats", schema = "stocktracker", catalog = "" )
public class StockKeyStatsEntity
{
    private String tickerSymbol;
    private Integer beta;
    private BigDecimal week52High;
    private BigDecimal week52Low;
    private BigDecimal week52Change;
    private Integer shortInterest;
    private BigDecimal latestEps;
    private Integer sharesOutstanding;
    private Integer currentFloat;
    private BigDecimal day200MovingAvg;
    private BigDecimal day50MovingAvg;
    private BigDecimal institutionalPercent;
    private BigDecimal shortRatio;
    private BigDecimal year5ChangePercent;
    private BigDecimal year2ChangePercent;
    private BigDecimal year1ChangePercent;
    private BigDecimal ytdChangePercent;
    private BigDecimal month6ChangePercent;
    private BigDecimal month3ChangePercent;
    private BigDecimal month1ChangePercent;
    private BigDecimal day5ChangePercent;
    private Timestamp keyStatsRequestDate;
    private String discontinuedInd;
    private Timestamp createDate;
    private Timestamp updateDate;
    private Integer version;

    @Id
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
    @Column( name = "beta", nullable = true )
    public Integer getBeta()
    {
        return beta;
    }

    public void setBeta( final Integer beta )
    {
        this.beta = beta;
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
    @Column( name = "week_52_change", nullable = true, precision = 2 )
    public BigDecimal getWeek52Change()
    {
        return week52Change;
    }

    public void setWeek52Change( final BigDecimal week52Change )
    {
        this.week52Change = week52Change;
    }

    @Basic
    @Column( name = "short_interest", nullable = true )
    public Integer getShortInterest()
    {
        return shortInterest;
    }

    public void setShortInterest( final Integer shortInterest )
    {
        this.shortInterest = shortInterest;
    }

    @Basic
    @Column( name = "latest_eps", nullable = true, precision = 2 )
    public BigDecimal getLatestEps()
    {
        return latestEps;
    }

    public void setLatestEps( final BigDecimal latestEps )
    {
        this.latestEps = latestEps;
    }

    @Basic
    @Column( name = "shares_outstanding", nullable = true )
    public Integer getSharesOutstanding()
    {
        return sharesOutstanding;
    }

    public void setSharesOutstanding( final Integer sharesOutstanding )
    {
        this.sharesOutstanding = sharesOutstanding;
    }

    @Basic
    @Column( name = "current_float", nullable = true )
    public Integer getCurrentFloat()
    {
        return currentFloat;
    }

    public void setCurrentFloat( final Integer currentFloat )
    {
        this.currentFloat = currentFloat;
    }

    @Basic
    @Column( name = "day_200_moving_avg", nullable = true, precision = 2 )
    public BigDecimal getDay200MovingAvg()
    {
        return day200MovingAvg;
    }

    public void setDay200MovingAvg( final BigDecimal day200MovingAvg )
    {
        this.day200MovingAvg = day200MovingAvg;
    }

    @Basic
    @Column( name = "day_50_moving_avg", nullable = true, precision = 2 )
    public BigDecimal getDay50MovingAvg()
    {
        return day50MovingAvg;
    }

    public void setDay50MovingAvg( final BigDecimal day50MovingAvg )
    {
        this.day50MovingAvg = day50MovingAvg;
    }

    @Basic
    @Column( name = "institutional_percent", nullable = true, precision = 2 )
    public BigDecimal getInstitutionalPercent()
    {
        return institutionalPercent;
    }

    public void setInstitutionalPercent( final BigDecimal institutionalPercent )
    {
        this.institutionalPercent = institutionalPercent;
    }

    @Basic
    @Column( name = "short_ratio", nullable = true, precision = 2 )
    public BigDecimal getShortRatio()
    {
        return shortRatio;
    }

    public void setShortRatio( final BigDecimal shortRatio )
    {
        this.shortRatio = shortRatio;
    }

    @Basic
    @Column( name = "year5_change_percent", nullable = true, precision = 2 )
    public BigDecimal getYear5ChangePercent()
    {
        return year5ChangePercent;
    }

    public void setYear5ChangePercent( final BigDecimal year5ChangePercent )
    {
        this.year5ChangePercent = year5ChangePercent;
    }

    @Basic
    @Column( name = "year2_change_percent", nullable = true, precision = 2 )
    public BigDecimal getYear2ChangePercent()
    {
        return year2ChangePercent;
    }

    public void setYear2ChangePercent( final BigDecimal year2ChangePercent )
    {
        this.year2ChangePercent = year2ChangePercent;
    }

    @Basic
    @Column( name = "year1_change_percent", nullable = true, precision = 2 )
    public BigDecimal getYear1ChangePercent()
    {
        return year1ChangePercent;
    }

    public void setYear1ChangePercent( final BigDecimal year1ChangePercent )
    {
        this.year1ChangePercent = year1ChangePercent;
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
    @Column( name = "month_6_change_percent", nullable = true, precision = 2 )
    public BigDecimal getMonth6ChangePercent()
    {
        return month6ChangePercent;
    }

    public void setMonth6ChangePercent( final BigDecimal month6ChangePercent )
    {
        this.month6ChangePercent = month6ChangePercent;
    }

    @Basic
    @Column( name = "month_3_change_percent", nullable = true, precision = 2 )
    public BigDecimal getMonth3ChangePercent()
    {
        return month3ChangePercent;
    }

    public void setMonth3ChangePercent( final BigDecimal month3ChangePercent )
    {
        this.month3ChangePercent = month3ChangePercent;
    }

    @Basic
    @Column( name = "month_1_change_percent", nullable = true, precision = 2 )
    public BigDecimal getMonth1ChangePercent()
    {
        return month1ChangePercent;
    }

    public void setMonth1ChangePercent( final BigDecimal month1ChangePercent )
    {
        this.month1ChangePercent = month1ChangePercent;
    }

    @Basic
    @Column( name = "day_5_change_percent", nullable = true, precision = 2 )
    public BigDecimal getDay5ChangePercent()
    {
        return day5ChangePercent;
    }

    public void setDay5ChangePercent( final BigDecimal day5ChangePercent )
    {
        this.day5ChangePercent = day5ChangePercent;
    }

    @Basic
    @Column( name = "key_stats_request_date", nullable = true )
    public Timestamp getKeyStatsRequestDate()
    {
        return keyStatsRequestDate;
    }

    public void setKeyStatsRequestDate( final Timestamp keyStatsRequestDate )
    {
        this.keyStatsRequestDate = keyStatsRequestDate;
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
        final StockKeyStatsEntity that = (StockKeyStatsEntity) o;
        return Objects.equals( tickerSymbol, that.tickerSymbol ) &&
               Objects.equals( beta, that.beta ) &&
               Objects.equals( week52High, that.week52High ) &&
               Objects.equals( week52Low, that.week52Low ) &&
               Objects.equals( week52Change, that.week52Change ) &&
               Objects.equals( shortInterest, that.shortInterest ) &&
               Objects.equals( latestEps, that.latestEps ) &&
               Objects.equals( sharesOutstanding, that.sharesOutstanding ) &&
               Objects.equals( currentFloat, that.currentFloat ) &&
               Objects.equals( day200MovingAvg, that.day200MovingAvg ) &&
               Objects.equals( day50MovingAvg, that.day50MovingAvg ) &&
               Objects.equals( institutionalPercent, that.institutionalPercent ) &&
               Objects.equals( shortRatio, that.shortRatio ) &&
               Objects.equals( year5ChangePercent, that.year5ChangePercent ) &&
               Objects.equals( year2ChangePercent, that.year2ChangePercent ) &&
               Objects.equals( year1ChangePercent, that.year1ChangePercent ) &&
               Objects.equals( ytdChangePercent, that.ytdChangePercent ) &&
               Objects.equals( month6ChangePercent, that.month6ChangePercent ) &&
               Objects.equals( month3ChangePercent, that.month3ChangePercent ) &&
               Objects.equals( month1ChangePercent, that.month1ChangePercent ) &&
               Objects.equals( day5ChangePercent, that.day5ChangePercent ) &&
               Objects.equals( keyStatsRequestDate, that.keyStatsRequestDate ) &&
               Objects.equals( discontinuedInd, that.discontinuedInd ) &&
               Objects.equals( createDate, that.createDate ) &&
               Objects.equals( updateDate, that.updateDate ) &&
               Objects.equals( version, that.version );
    }

    @Override
    public int hashCode()
    {

        return Objects
            .hash( tickerSymbol, beta, week52High, week52Low, week52Change, shortInterest, latestEps, sharesOutstanding, currentFloat, day200MovingAvg, day50MovingAvg, institutionalPercent, shortRatio, year5ChangePercent, year2ChangePercent, year1ChangePercent, ytdChangePercent, month6ChangePercent, month3ChangePercent, month1ChangePercent, day5ChangePercent, keyStatsRequestDate, discontinuedInd, createDate, updateDate, version );
    }
}
