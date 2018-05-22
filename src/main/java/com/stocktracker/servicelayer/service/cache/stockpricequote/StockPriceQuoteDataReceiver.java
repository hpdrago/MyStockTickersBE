package com.stocktracker.servicelayer.service.cache.stockpricequote;

import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDataReceiver;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class StockPriceQuoteDataReceiver implements AsyncCacheDataReceiver<String,StockPriceQuote>
{
    private String tickerSymbol;
    private StockPriceQuote stockPriceQuote;
    private AsyncCacheEntryState cacheState;
    private String error;
    private Date dataExpiration;

    @Override
    public void setCacheKey( final String cacheKey )
    {
        this.tickerSymbol = cacheKey;
    }

    @Override
    public String getCacheKey()
    {
        return this.tickerSymbol;
    }

    @Override
    public void setCachedData( final StockPriceQuote stockPriceQuote )
    {
        this.stockPriceQuote = stockPriceQuote;
    }

    @Override
    public StockPriceQuote getCachedData()
    {
        return this.stockPriceQuote;
    }

    @Override
    public void setCacheDataState( final AsyncCacheEntryState cacheState )
    {
        this.cacheState = cacheState;
    }

    @Override
    public AsyncCacheEntryState getCacheDataState()
    {
        return this.cacheState;
    }

    @Override
    public void setCacheError( final String error )
    {
        this.error = error;
    }

    @Override
    public String getCacheError()
    {
        return null;
    }

    @Override
    public void setDataExpiration( final Date dataExpiration )
    {
        this.dataExpiration = dataExpiration;
    }

    public Date getDataExpiration()
    {
        return dataExpiration;
    }

    public AsyncCacheEntryState getCacheState()
    {
        return cacheState;
    }

    public void setCacheState( final AsyncCacheEntryState cacheState )
    {
        this.cacheState = cacheState;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockPriceQuoteDataReceiver{" );
        sb.append( "tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", stockPriceQuote=" ).append( stockPriceQuote );
        sb.append( ", cacheState=" ).append( cacheState );
        sb.append( ", error='" ).append( error ).append( '\'' );
        sb.append( ", dataExpiration=" ).append( dataExpiration );
        sb.append( '}' );
        return sb.toString();
    }
}
