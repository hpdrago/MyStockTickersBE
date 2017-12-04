package com.stocktracker.servicelayer.stockinformationprovider;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * POJO implementation for interface {@code CachedStock}
 */
public class StockTickerQuoteCacheEntry implements StockQuote
{
    private StockTickerQuote stockTickerQuote;
    private long lastQuoteRefreshTime;
    private boolean stockTableEntryValidated;

    /**
     * Create a new instance with only the ticker symbol and stock quote state.  This method is used to return a
     * quote that provides a caching status when the stock needs to be retreived asynchronously.
     * @param tickerSymbol
     * @param stockQuoteState
     * @return
     */
    public static StockTickerQuoteCacheEntry newInstance( final String tickerSymbol,
                                                          final StockQuoteState stockQuoteState )
    {
        return new StockTickerQuoteCacheEntry( tickerSymbol, stockQuoteState );
    }

    /**
     * Creates a new instance from a stock ticker quote instance.
     * @param stockTickerQuote
     * @return
     */
    public static StockTickerQuoteCacheEntry newInstance( final StockTickerQuote stockTickerQuote )
    {
        return new StockTickerQuoteCacheEntry( stockTickerQuote );
    }

    private StockTickerQuoteCacheEntry( final String tickerSymbol, final StockQuoteState stockQuoteState )
    {
        this.stockTickerQuote = StockTickerQuote.newInstance( tickerSymbol, stockQuoteState );
    }

    private StockTickerQuoteCacheEntry( final StockTickerQuote stockTickerQuote )
    {
        this.lastQuoteRefreshTime = System.currentTimeMillis();
        this.stockTickerQuote = stockTickerQuote;
    }

    public long getLastQuoteRefreshTime()
    {
        return this.lastQuoteRefreshTime;
    }

    public void setLastQuoteRefreshTime( long lastQuoteRefreshTime )
    {
        this.lastQuoteRefreshTime = lastQuoteRefreshTime;
    }

    public void setStockQuoteState( final StockQuoteState stockQuoteState )
    {
        this.stockTickerQuote.setStockQuoteState( stockQuoteState );
    }

    @Override
    public String getTickerSymbol()
    {
        return this.stockTickerQuote.getTickerSymbol();
    }

    @Override
    public String getCompanyName()
    {
        return this.stockTickerQuote.getCompanyName();
    }

    @Override
    public String getStockExchange()
    {
        return null;
    }

    @Override
    public BigDecimal getLastPrice()
    {
        return this.stockTickerQuote.getLastPrice();
    }

    @Override
    public Timestamp getLastPriceChange()
    {
        return this.stockTickerQuote.getLastPriceChange();
    }

    @Override
    public StockQuoteState getStockQuoteState()
    {
        return this.stockTickerQuote.getStockQuoteState();
    }

    public boolean isStockTableEntryValidated()
    {
        return stockTableEntryValidated;
    }

    public void setStockTableEntryValidated( final boolean stockTableEntryValidated )
    {
        this.stockTableEntryValidated = stockTableEntryValidated;
    }

    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !(o instanceof StockTickerQuoteCacheEntry) )
        {
            return false;
        }
        final StockTickerQuoteCacheEntry that = (StockTickerQuoteCacheEntry) o;
        return Objects.equals( stockTickerQuote, that.stockTickerQuote );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( stockTickerQuote );
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockTickerQuoteCacheEntry{" );
        sb.append( "stockTickerQuote=" ).append( stockTickerQuote );
        sb.append( ", lastQuoteRefreshTime=" ).append( lastQuoteRefreshTime );
        sb.append( ", stockTableEntryValidated=" ).append( stockTableEntryValidated );
        sb.append( '}' );
        return sb.toString();
    }

}

