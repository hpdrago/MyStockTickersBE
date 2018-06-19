package com.stocktracker.servicelayer.service.cache.stockpricequote;

import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDataReceiver;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class StockPriceQuoteCacheDataReceiver implements AsyncCacheDataReceiver<String,StockPriceQuote>
{
    private String tickerSymbol;
    private StockPriceQuote stockPriceQuote = new StockPriceQuote();

    @Override
    public void setCacheKey( final String cacheKey )
    {
        this.tickerSymbol = cacheKey;
        this.stockPriceQuote
            .setTickerSymbol( cacheKey );
    }

    @Override
    public String getCacheKey()
    {
        return this.tickerSymbol;
    }

    @Override
    public void setCachedData( final StockPriceQuote stockPriceQuote )
    {
        /*
         * We never want the stock price quote to be null because it is the container for all of the cache values
         * and settings.
         */
        if ( stockPriceQuote != null )
        {
            this.stockPriceQuote = stockPriceQuote;
        }
    }

    @Override
    public StockPriceQuote getCachedData()
    {
        return this.stockPriceQuote;
    }

    @Override
    public void setCacheDataState( final AsyncCacheEntryState cacheState )
    {
        this.stockPriceQuote.setCacheState( cacheState );
    }

    @Override
    public AsyncCacheEntryState getCachedDataState()
    {
        return this.stockPriceQuote.getCacheState();
    }

    @Override
    public void setCacheError( final String error )
    {
        this.stockPriceQuote.setCacheError( error );
    }

    @Override
    public String getCacheError()
    {
        return this.stockPriceQuote.getCacheError();
    }

    @Override
    public void setExpirationTime( final Timestamp dataExpiration )
    {
        this.stockPriceQuote.setExpirationTime( dataExpiration );
    }

    public Timestamp getExpirationTime()
    {
        return this.stockPriceQuote.getExpiration();
    }

    public AsyncCacheEntryState getCacheState()
    {
        return this.stockPriceQuote.getCacheState();
    }

    public void setCacheState( final AsyncCacheEntryState cacheState )
    {
        this.stockPriceQuote.setCacheState( cacheState );
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockPriceQuoteDataReceiver{" );
        sb.append( "tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", stockPriceQuote=" ).append( stockPriceQuote );
        sb.append( '}' );
        return sb.toString();
    }
}
