package com.stocktracker.weblayer.dto;

import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState;
import com.stocktracker.servicelayer.service.cache.stockpricequote.StockPriceQuote;
import com.stocktracker.servicelayer.service.cache.stockquote.StockQuoteEntityContainer;
import com.stocktracker.servicelayer.service.stocks.StockPriceQuoteContainer;
import com.stocktracker.weblayer.dto.common.DatabaseEntityDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
@Qualifier( "stockQuoteDTO")
public class StockQuoteDTO extends DatabaseEntityDTO<String>
                           implements StockPriceQuoteContainer,
                                      StockQuoteEntityContainer
{
    private String tickerSymbol;
    private String companyName;
    private String calculationPrice;
    private BigDecimal openPrice;
    private BigDecimal closePrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal latestPrice;
    private String latestPriceSource;
    private String latestPriceTime;
    private Long latestUpdate;
    private BigDecimal latestVolume;
    private BigDecimal thirtyDayAvgVolume;
    private BigDecimal changeAmount;
    private BigDecimal delayedPrice;
    private Long delayedPriceTime;
    private BigDecimal previousClose;
    private BigDecimal changePercent;
    private BigDecimal marketCap;
    private BigDecimal peRatio;
    private BigDecimal week52High;
    private BigDecimal week52Low;
    private BigDecimal week52Change;
    private BigDecimal ytdChangePercent;
    private Timestamp lastQuoteRequestDate;
    private String discontinuedInd;

    // Stock Price Quote fields
    private BigDecimal lastPrice;

    private AsyncCacheEntryState stockPriceQuoteCacheState;
    private String stockPriceQuoteError;
    private AsyncCacheEntryState stockQuoteCacheState;
    private String stockQuoteError;
    private Timestamp expirationTime;

    /**
     * This method is called by the {@code StockQuoteCache} to set the stock quote properties on this DTO.
     * @param stockQuoteEntity
     */
    @Override
    public void setStockQuoteEntity( final StockQuoteEntity stockQuoteEntity )
    {
        BeanUtils.copyProperties( stockQuoteEntity, this );
    }

    @Override
    public void setStockQuoteCacheEntryState( final AsyncCacheEntryState stockQuoteEntityCacheState )
    {
        this.stockQuoteCacheState = stockPriceQuoteCacheState;
    }

    @Override
    public void setStockQuoteCacheError( final String stockQuoteCacheError )
    {
        this.stockQuoteError = stockQuoteCacheError;
    }

    public String getTickerSymbol()
    {
        return tickerSymbol;
    }
    public void setTickerSymbol( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    public String getCalculationPrice()
    {
        return calculationPrice;
    }

    public void setCalculationPrice( final String calculationPrice )
    {
        this.calculationPrice = calculationPrice;
    }

    public BigDecimal getOpenPrice()
    {
        return openPrice;
    }

    public void setOpenPrice( final BigDecimal openPrice )
    {
        this.openPrice = openPrice;
    }

    public BigDecimal getClosePrice()
    {
        return closePrice;
    }
    public void setClosePrice( final BigDecimal closePrice )
    {
        this.closePrice = closePrice;
    }

    public BigDecimal getHighPrice()
    {
        return highPrice;
    }
    public void setHighPrice( final BigDecimal highPrice )
    {
        this.highPrice = highPrice;
    }

    public BigDecimal getLowPrice()
    {
        return lowPrice;
    }
    public void setLowPrice( final BigDecimal lowPrice )
    {
        this.lowPrice = lowPrice;
    }

    public BigDecimal getLatestPrice()
    {
        return latestPrice;
    }
    public void setLatestPrice( final BigDecimal latestPrice )
    {
        this.latestPrice = latestPrice;
    }

    public String getLatestPriceSource()
    {
        return latestPriceSource;
    }
    public void setLatestPriceSource( final String latestPriceSource )
    {
        this.latestPriceSource = latestPriceSource;
    }

    public String getLatestPriceTime()
    {
        return latestPriceTime;
    }
    public void setLatestPriceTime( final String latestPriceTime )
    {
        this.latestPriceTime = latestPriceTime;
    }

    public Long getLatestUpdate()
    {
        return latestUpdate;
    }
    public void setLatestUpdate( final Long latestUpdate )
    {
        this.latestUpdate = latestUpdate;
    }

    public BigDecimal getLatestVolume()
    {
        return latestVolume;
    }
    public void setLatestVolume( final BigDecimal latestVolume )
    {
        this.latestVolume = latestVolume;
    }

    public BigDecimal getDelayedPrice()
    {
        return delayedPrice;
    }
    public void setDelayedPrice( final BigDecimal delayedPrice )
    {
        this.delayedPrice = delayedPrice;
    }

    public Long getDelayedPriceTime()
    {
        return delayedPriceTime;
    }
    public void setDelayedPriceTime( final Long delayedPriceTime )
    {
        this.delayedPriceTime = delayedPriceTime;
    }

    public BigDecimal getPreviousClose()
    {
        return previousClose;
    }
    public void setPreviousClose( final BigDecimal previousClose )
    {
        this.previousClose = previousClose;
    }

    public BigDecimal getChangePercent()
    {
        return changePercent;
    }
    public void setChangePercent( final BigDecimal changePercent )
    {
        this.changePercent = changePercent;
    }

    public BigDecimal getThirtyDayAvgVolume()
    {
        return thirtyDayAvgVolume;
    }
    public void setThirtyDayAvgVolume( final BigDecimal thirtyDayAvgVolume )
    {
        this.thirtyDayAvgVolume = thirtyDayAvgVolume;
    }

    public BigDecimal getMarketCap()
    {
        return marketCap;
    }
    public void setMarketCap( final BigDecimal marketCap )
    {
        this.marketCap = marketCap;
    }

    public BigDecimal getPeRatio()
    {
        return peRatio;
    }
    public void setPeRatio( final BigDecimal peRatio )
    {
        this.peRatio = peRatio;
    }

    public BigDecimal getWeek52High()
    {
        return week52High;
    }
    public void setWeek52High( final BigDecimal week52High )
    {
        this.week52High = week52High;
    }

    public BigDecimal getWeek52Low()
    {
        return week52Low;
    }
    public void setWeek52Low( final BigDecimal week52Low )
    {
        this.week52Low = week52Low;
    }

    public BigDecimal getWeek52Change()
    {
        return week52Change;
    }
    public void setWeek52Change( final BigDecimal week52Change )
    {
        this.week52Change = week52Change;
    }

    public BigDecimal getYtdChangePercent()
    {
        return ytdChangePercent;
    }
    public void setYtdChangePercent( final BigDecimal ytdChangePercent )
    {
        this.ytdChangePercent = ytdChangePercent;
    }

    public Timestamp getLastQuoteRequestDate()
    {
        return lastQuoteRequestDate;
    }
    public void setLastQuoteRequestDate( final Timestamp lastQuoteRequestDate )
    {
        this.lastQuoteRequestDate = lastQuoteRequestDate;
    }

    public String getDiscontinuedInd()
    {
        return discontinuedInd;
    }
    public void setDiscontinuedInd( final String discontinuedInd )
    {
        this.discontinuedInd = discontinuedInd;
    }

    @Override
    public void setStockPriceQuote( final AsyncCacheEntryState stockPriceQuoteCacheState, final StockPriceQuote stockPriceQuote )
    {
        this.setStockPriceQuoteCacheState( stockPriceQuoteCacheState );
    }

    @Override
    public void setLastPrice( final BigDecimal lastPrice )
    {
        this.lastPrice = lastPrice;
    }

    @Override
    public BigDecimal getLastPrice()
    {
        return this.lastPrice;
    }

    @Override
    public void setStockPriceQuoteCacheState( final AsyncCacheEntryState stockPriceQuoteCacheState )
    {
        this.stockPriceQuoteCacheState = stockPriceQuoteCacheState;
    }

    @Override
    public AsyncCacheEntryState getStockPriceQuoteCacheState()
    {
        return this.stockPriceQuoteCacheState;
    }

    @Override
    public void setExpirationTime( final Timestamp expirationTime )
    {
        this.expirationTime = expirationTime;
    }

    @Override
    public Timestamp getExpirationTime()
    {
        return this.expirationTime;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName( final String companyName )
    {
        this.companyName = companyName;
    }

    public BigDecimal getChangeAmount()
    {
        return changeAmount;
    }

    public void setChangeAmount( final BigDecimal changeAmount )
    {
        this.changeAmount = changeAmount;
    }

    public AsyncCacheEntryState getStockQuoteCacheState()
    {
        return stockQuoteCacheState;
    }

    public void setStockQuoteCacheState( final AsyncCacheEntryState stockQuoteCacheState )
    {
        this.stockQuoteCacheState = stockQuoteCacheState;
    }

    public String getStockQuoteError()
    {
        return stockQuoteError;
    }

    public void setStockQuoteError( final String stockQuoteError )
    {
        this.stockQuoteError = stockQuoteError;
    }

    public String getStockPriceQuoteError()
    {
        return stockPriceQuoteError;
    }

    public void setStockPriceQuoteError( final String stockPriceQuoteError )
    {
        this.stockPriceQuoteError = stockPriceQuoteError;
    }


    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockQuoteDTO{" );
        sb.append( "tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", companyName='" ).append( companyName ).append( '\'' );
        sb.append( ", calculationPrice='" ).append( calculationPrice ).append( '\'' );
        sb.append( ", openPrice=" ).append( openPrice );
        sb.append( ", closePrice=" ).append( closePrice );
        sb.append( ", highPrice=" ).append( highPrice );
        sb.append( ", lowPrice=" ).append( lowPrice );
        sb.append( ", latestPrice=" ).append( latestPrice );
        sb.append( ", latestPriceSource='" ).append( latestPriceSource ).append( '\'' );
        sb.append( ", latestPriceTime=" ).append( latestPriceTime );
        sb.append( ", latestUpdate=" ).append( latestUpdate );
        sb.append( ", latestVolume=" ).append( latestVolume );
        sb.append( ", changeAmount=" ).append( changeAmount );
        sb.append( ", delayedPrice=" ).append( delayedPrice );
        sb.append( ", delayedPriceTime=" ).append( delayedPriceTime );
        sb.append( ", previousClose=" ).append( previousClose );
        sb.append( ", changePercent=" ).append( changePercent );
        sb.append( ", thirtyDayAvgVolume=" ).append( thirtyDayAvgVolume );
        sb.append( ", marketCap=" ).append( marketCap );
        sb.append( ", peRatio=" ).append( peRatio );
        sb.append( ", week52High=" ).append( week52High );
        sb.append( ", week52Low=" ).append( week52Low );
        sb.append( ", week52Change=" ).append( week52Change );
        sb.append( ", ytdChangePercent=" ).append( ytdChangePercent );
        sb.append( ", lastQuoteRequestDate=" ).append( lastQuoteRequestDate );
        sb.append( ", discontinuedInd='" ).append( discontinuedInd ).append( '\'' );
        sb.append( ", lastPrice=" ).append( lastPrice );
        sb.append( ", stockPriceQuoteCacheState=" ).append( stockPriceQuoteCacheState );
        sb.append( ", stockPriceQuoteError=" ).append( stockPriceQuoteError );
        sb.append( ", stockQuoteCacheState=" ).append( stockQuoteCacheState );
        sb.append( ", stockQuoteError='" ).append( stockQuoteError ).append( '\'' );
        sb.append( ", expirationTime=" ).append( expirationTime );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
