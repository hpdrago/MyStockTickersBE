package com.stocktracker.repositorylayer.entity;

import com.stocktracker.common.TradingHours;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDBEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.zankowski.iextrading4j.api.stocks.Quote;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
@Entity
@Table( name = "stock_quote", schema = "stocktracker", catalog = "" )
public class StockQuoteEntity extends TickerSymbolEntity
    implements AsyncCacheDBEntity<String>
{
    private String calculationPrice;
    private String companyName;
    private BigDecimal openPrice;
    private BigDecimal closePrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal latestPrice;
    private String latestPriceSource;
    private String latestPriceTime;
    private Long latestUpdate;
    private BigDecimal latestVolume;
    private BigDecimal delayedPrice;
    private Long delayedPriceTime;
    private BigDecimal previousClose;
    private BigDecimal changeAmount;
    private BigDecimal changePercent;
    private BigDecimal thirtyDayAvgVolume;
    private BigDecimal marketCap;
    private BigDecimal peRatio;
    private BigDecimal week52High;
    private BigDecimal week52Low;
    private BigDecimal ytdChangePercent;
    private Timestamp lastQuoteRequestDate;
    private String discontinuedInd;

    @Column( name = "company_name", nullable = false, length = 70 )
    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName( final String companyName )
    {
        this.companyName = companyName;
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

    /*
    @Transient
    @Override
    public void setDiscontinued( final boolean discontinuedInd )
    {
        this.setDiscontinuedInd( discontinuedInd ? "Y" : "N" );
    }

    @Transient
    public boolean isDiscontinued()
    {
        return this.discontinuedInd != null && this.discontinuedInd.equalsIgnoreCase( "Y" );
    }
    */

    @Basic
    @Column( name = "calculation_price", nullable = true, length = 20 )
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
    public BigDecimal getLatestVolume()
    {
        return latestVolume;
    }

    public void setLatestVolume( final BigDecimal latestVolume )
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
    @Column( name = "change_amount", nullable = true, precision = 2)
    public BigDecimal getChangeAmount()
    {
        return changeAmount;
    }

    public void setChangeAmount( final BigDecimal change )
    {
        this.changeAmount = change;
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
    public BigDecimal getThirtyDayAvgVolume()
    {
        return thirtyDayAvgVolume;
    }

    public void setThirtyDayAvgVolume( final BigDecimal thirtyDayAvgVolume )
    {
        this.thirtyDayAvgVolume = thirtyDayAvgVolume;
    }

    @Basic
    @Column( name = "market_cap", nullable = true )
    public BigDecimal getMarketCap()
    {
        return marketCap;
    }

    public void setMarketCap( final BigDecimal marketCap )
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

    @Transient
    @Override
    public Timestamp getExpiration()
    {
        Timestamp expirationDate;
        if ( TradingHours.isInSession() )
        {
            /*
             * Need to retrieve at the beginning of the session to get the open price.
             */
            if ( this.getUpdateDate().getTime() < TradingHours.getTodaysSessionStartTime() )
            {
                expirationDate = new Timestamp( TradingHours.getTodaysSessionStartTime() );
            }
            else // the quote is good for the trading day.
            {
                expirationDate = new Timestamp( TradingHours.getTodaysSessionEndTime() );
            }
        }
        else
        {
            if ( TradingHours.isWeekend() )
            {
                expirationDate = new Timestamp( TradingHours.getNextDaysSessionStartTime() );
            }
            /*
             * If not in session, we need to get the closing values so any time after 4:00pm EST
             */


            /*
             * TODO: NEED to revisit and set the database dates to UTC and then convert to EST here or int he method.
             */
            else if ( TradingHours.isAfterSessionEnd( this.getUpdateDate().getTime() ))
            {
                expirationDate = new Timestamp( TradingHours.getNextDaysSessionStartTime() );
            }
            else // expires at the end of the session.
            {
                expirationDate = new Timestamp( TradingHours.getTodaysSessionEndTime() );
            }
        }
        return expirationDate;
    }

    public void copyQuote( final Quote quote )
    {
        BeanUtils.copyProperties( quote, this );
        this.setTickerSymbol( quote.getSymbol() );
        this.setOpenPrice( quote.getOpen() );
        this.setClosePrice( quote.getClose() );
        this.setHighPrice( quote.getHigh() );
        this.setLowPrice( quote.getLow() );
        this.setLatestPriceSource( quote.getLatestSource() );
        this.setLatestPriceTime( quote.getLatestTime() );
        this.setChangeAmount( quote.getChange() );
        this.setChangePercent( quote.getChangePercent() );
        this.setThirtyDayAvgVolume( quote.getAvgTotalVolume() );
        this.setYtdChangePercent( quote.getYtdChange() );
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockQuoteEntity{" );
        sb.append( "tickerSymbol='" ).append( super.getTickerSymbol() ).append( '\'' );
        sb.append( ", calculationPrice='" ).append( calculationPrice ).append( '\'' );
        sb.append( ", openPrice=" ).append( openPrice );
        sb.append( ", closePrice=" ).append( closePrice );
        sb.append( ", highPrice=" ).append( highPrice );
        sb.append( ", lowPrice=" ).append( lowPrice );
        sb.append( ", latestPrice=" ).append( latestPrice );
        sb.append( ", latestPriceSource='" ).append( latestPriceSource ).append( '\'' );
        sb.append( ", latestPriceTime='" ).append( latestPriceTime ).append( '\'' );
        sb.append( ", latestUpdate=" ).append( latestUpdate );
        sb.append( ", latestVolume=" ).append( latestVolume );
        sb.append( ", delayedPrice=" ).append( delayedPrice );
        sb.append( ", delayedPriceTime=" ).append( delayedPriceTime );
        sb.append( ", previousClose=" ).append( previousClose );
        sb.append( ", change=" ).append( changeAmount );
        sb.append( ", changePercent=" ).append( changePercent );
        sb.append( ", thirtyDayAvgVolume=" ).append( thirtyDayAvgVolume );
        sb.append( ", marketCap=" ).append( marketCap );
        sb.append( ", peRatio=" ).append( peRatio );
        sb.append( ", week52High=" ).append( week52High );
        sb.append( ", week52Low=" ).append( week52Low );
        sb.append( ", ytdChangePercent=" ).append( ytdChangePercent );
        sb.append( ", lastQuoteRequestDate=" ).append( lastQuoteRequestDate );
        sb.append( ", discontinuedInd='" ).append( discontinuedInd ).append( '\'' );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
