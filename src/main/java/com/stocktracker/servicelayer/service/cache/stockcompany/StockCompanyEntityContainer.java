package com.stocktracker.servicelayer.service.cache.stockcompany;

import com.stocktracker.repositorylayer.entity.StockCompanyEntity;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState;
import com.stocktracker.servicelayer.service.stocks.TickerSymbolContainer;

/**
 * This interface defines the methods for classes that will get Stock Quote information from the StockQuoteEntityCache
 */
public interface StockCompanyEntityContainer extends TickerSymbolContainer
{
    /**
     * This method is called to set the {@code StockCompanyEntity}.  Classes implementing this interface can then
     * extract any or all of the Stock Company (IEXTrading Company) properties as needed.
     * @param stockCompanyEntity
     */
    void setStockCompanyEntity( final StockCompanyEntity stockCompanyEntity );

    /**
     * Identifies the state of the {@code StockCompanyEntity} received from the stock company cache.
     * @param cacheEntryState
     */
    void setStockCompanyCacheEntryState( final AsyncCacheEntryState cacheEntryState );

    /**
     * This method is called to pass the error encountered while try to retrieve stock company from IEXTrading.
     * @param error
     */
    void setStockCompanyCacheError( final String error );

    /*
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
    */
}
