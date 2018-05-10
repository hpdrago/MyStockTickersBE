package com.stocktracker.servicelayer.service.cache.stockpricequote;

import com.stocktracker.servicelayer.service.stocks.TickerSymbolContainer;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * This interface defines the methods for classes that will get Stock Quote information from the StockQuoteEntityCache
 */
public interface StockQuoteContainer extends TickerSymbolContainer
{
    boolean isDiscontinued();
    void setDiscontinued( final boolean discontinuedInd );

    String getCalculationPrice();
    void setCalculationPrice( final String calculationPrice );

    BigDecimal getOpenPrice();
    void setOpenPrice( final BigDecimal openPrice );

    BigDecimal getClosePrice();
    void setClosePrice( final BigDecimal closePrice );

    BigDecimal getHighPrice();
    void setHighPrice( final BigDecimal highPrice );

    BigDecimal getLowPrice();
    void setLowPrice( final BigDecimal lowPrice );

    BigDecimal getLatestPrice();
    void setLatestPrice( final BigDecimal latestPrice );

    String getLatestPriceSource();
    void setLatestPriceSource( final String latestPriceSource );

    String getLatestPriceTime();
    void setLatestPriceTime( final String latestPriceTime );

    Long getLatestUpdate();
    void setLatestUpdate( final Long latestUpdate );

    Long getLatestVolume();
    void setLatestVolume( final Long latestVolume );

    BigDecimal getDelayedPrice();
    void setDelayedPrice( final BigDecimal delayedPrice );

    Long getDelayedPriceTime();

    void setDelayedPriceTime( final Long delayedPriceTime );

    BigDecimal getPreviousClose();
    void setPreviousClose( final BigDecimal previousClose );

    BigDecimal getChangeAmount();
    void setChangeAmount( final BigDecimal change );

    BigDecimal getChangePercent();
    void setChangePercent( final BigDecimal changePercent );

    Long getThirtyDayAvgVolume();
    void setThirtyDayAvgVolume( final Long thirtyDayAvgVolume );

    Long getMarketCap();
    void setMarketCap( final Long marketCap );

    BigDecimal getPeRatio();
    void setPeRatio( final BigDecimal peRatio );

    BigDecimal getWeek52High();
    void setWeek52High( final BigDecimal week52High );

    BigDecimal getWeek52Low();
    void setWeek52Low( final BigDecimal week52Low );

    BigDecimal getYtdChangePercent();
    void setYtdChangePercent( final BigDecimal ytdChangePercent );

    Timestamp getLastQuoteRequestDate();
    void setLastQuoteRequestDate( final Timestamp lastQuoteRequestDate );
}
