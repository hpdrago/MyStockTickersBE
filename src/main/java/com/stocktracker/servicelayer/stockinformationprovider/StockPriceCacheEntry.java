package com.stocktracker.servicelayer.stockinformationprovider;

import com.stocktracker.common.MyLogger;
import rx.subjects.BehaviorSubject;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * POJO implementation for interface {@code CachedStock}
 */
public class StockPriceCacheEntry implements MyLogger
{
    private String tickerSymbol;
    private BigDecimal stockPrice;
    /**
     * Identifies the cached state: CURRENT, STALE, FAILURE, NOT_FOUND
     */
    private StockPriceCacheState cacheState = StockPriceCacheState.STALE;
    private long stockPriceRefreshTime;
    /**
     * Determines when the stock price is stale.  Stock prices are stale after 15 minutes during trading hours.
     */
    private Timestamp expirationTime;
    /**
     * Determines if the stock price is currently being fetched.
     */
    private StockPriceFetchState fetchState = StockPriceFetchState.NOT_FETCHING;

    /**
     * The RxJava subject to contain any requests waiting for the stock price while it's being fetched.
     */
    private BehaviorSubject<BigDecimal> fetchSubject = BehaviorSubject.create();
    /**
     * Identifies if the stock company table's existence has already been checked.
     */
    private boolean stockTableEntryValidated;
    /**
     * Identifies if the stock is no longer used.
     */
    private boolean discontinued;
    private Exception fetchException;

    /**
     * Creates a new STALE instance.
     * @param tickerSymbol
     * @return
     */
    public static StockPriceCacheEntry newInstance( final String tickerSymbol )
    {
        return newInstance( tickerSymbol, StockPriceCacheState.STALE );
    }

    /**
     * Creates a new instance.
     * @param tickerSymbol
     * @param stockPriceCacheState
     * @return
     */
    public static StockPriceCacheEntry newInstance( final String tickerSymbol,
                                                    final StockPriceCacheState stockPriceCacheState )
    {
        return new StockPriceCacheEntry( tickerSymbol, new BigDecimal( 0 ), stockPriceCacheState );
    }

    /**
     * Create a new instance with only the ticker symbol and stock quote state.  This method is used to return a
     * quote that provides a caching status when the stock needs to be retreived asynchronously.
     * @param tickerSymbol
     * @param stockPriceCacheState
     * @return
     */
    public static StockPriceCacheEntry newInstance( final String tickerSymbol,
                                                    final BigDecimal stockPrice,
                                                    final StockPriceCacheState stockPriceCacheState )
    {
        return new StockPriceCacheEntry( tickerSymbol, stockPrice, stockPriceCacheState );
    }

    /**
     * Constructor.
     * @param tickerSymbol
     * @param stockPrice
     * @param stockPriceCacheState
     */
    private StockPriceCacheEntry( final String tickerSymbol,
                                  final BigDecimal stockPrice,
                                  final StockPriceCacheState stockPriceCacheState )
    {
        this.tickerSymbol = tickerSymbol;
        this.stockPrice = stockPrice;
        this.cacheState = stockPriceCacheState;
        this.expirationTime = new Timestamp( this.stockPriceRefreshTime + StockPriceCache.EXPIRATION_TIME );
    }

    /**
     * Subscribers will get one of three values:
     *   null -- if the stock notes found
     *   onError -- if there was an exception
     *   stock price -- if all went well.
     */
    public void notifySubscribers()
    {
        final String methodName = "notifySubscribers";
        logDebug( methodName, "{0} cacheState: {1}", methodName, this.cacheState );
        switch ( this.cacheState )
        {
            case NOT_FOUND:
                this.fetchSubject
                    .onNext( null );
                break;
            case FAILURE:
                this.fetchSubject
                    .onError( this.fetchException );
                break;
            case CURRENT:
                this.fetchSubject
                    .onNext( this.stockPrice );
                break;
        }
    }

    public long getStockPriceRefreshTime()
    {
        return this.stockPriceRefreshTime;
    }

    public void setCacheState( final StockPriceCacheState cacheState )
    {
        this.cacheState = cacheState;
    }

    public StockPriceCacheState getCacheState()
    {
        return this.cacheState;
    }

    public String getTickerSymbol()
    {
        return this.tickerSymbol;
    }

    public BigDecimal getStockPrice()
    {
        return this.stockPrice;
    }

    public Timestamp getExpiration()
    {
        return this.expirationTime;
    }

    public boolean isStockTableEntryValidated()
    {
        return stockTableEntryValidated;
    }

    public void setStockTableEntryValidated( final boolean stockTableEntryValidated )
    {
        this.stockTableEntryValidated = stockTableEntryValidated;
    }

    public void setStockPrice( final BigDecimal lastPrice )
    {
        this.stockPrice = lastPrice;
        this.expirationTime = new Timestamp( System.currentTimeMillis() + StockPriceCache.EXPIRATION_TIME );
    }

    public StockPriceFetchState getFetchState()
    {
        return fetchState;
    }

    public void setFetchState( final StockPriceFetchState fetchState )
    {
        this.fetchState = fetchState;
    }

    public boolean isDiscontinued()
    {
        return discontinued;
    }

    public void setDiscontinued( final boolean discontinued )
    {
        this.discontinued = discontinued;
    }

    public Exception getFetchException()
    {
        return fetchException;
    }

    public void setFetchException( final Exception fetchException )
    {
        this.fetchException = fetchException;
    }

    public BehaviorSubject<BigDecimal> getFetchSubject()
    {
        return fetchSubject;
    }

    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !(o instanceof StockPriceCacheEntry) )
        {
            return false;
        }
        final StockPriceCacheEntry that = (StockPriceCacheEntry) o;
        return Objects.equals( stockPrice, that.stockPrice );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( stockPrice );
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockPriceCacheEntry{" );
        sb.append( "tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", stockPrice=" ).append( stockPrice );
        sb.append( ", cacheState=" ).append( cacheState );
        sb.append( ", stockPriceRefreshTime=" ).append( stockPriceRefreshTime );
        sb.append( ", expirationTime=" ).append( expirationTime );
        sb.append( ", fetchState=" ).append( fetchState );
        sb.append( ", stockTableEntryValidated=" ).append( stockTableEntryValidated );
        sb.append( ", discontinued=" ).append( discontinued );
        sb.append( ", fetchException=" ).append( fetchException == null ? "null" : fetchException.getClass().getSimpleName() );
        sb.append( '}' );
        return sb.toString();
    }
}

