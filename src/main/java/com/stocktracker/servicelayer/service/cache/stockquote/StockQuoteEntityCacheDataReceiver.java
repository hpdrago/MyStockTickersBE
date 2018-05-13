package com.stocktracker.servicelayer.service.cache.stockquote;

import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDataReceiver;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState;

/**
 * This class implement the {@code AsyncCacheDataReceiver} interface in order to interact with the {@code StockQuoteAsyncCache}.
 * This class will contain the results of requesting a stock quote from the cache.
 */
public class StockQuoteEntityCacheDataReceiver implements AsyncCacheDataReceiver<String,StockQuoteEntity>
{
    private String tickerSymbol;
    private StockQuoteEntity stockQuoteEntity;
    private AsyncCacheEntryState cacheState;
    private String error;

    public StockQuoteEntityCacheDataReceiver( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public void setTickerSymbol( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    public StockQuoteEntity getStockQuoteEntity()
    {
        return stockQuoteEntity;
    }

    public void setStockQuoteEntity( final StockQuoteEntity stockQuoteEntity )
    {
        this.stockQuoteEntity = stockQuoteEntity;
    }

    public AsyncCacheEntryState getCacheState()
    {
        return cacheState;
    }

    public void setCacheState( final AsyncCacheEntryState cacheState )
    {
        this.cacheState = cacheState;
    }

    public String getError()
    {
        return error;
    }

    @Override
    public String getEntityKey()
    {
        return this.tickerSymbol;
    }

    @Override
    public void setCachedData( final StockQuoteEntity stockQuoteEntity )
    {
        this.stockQuoteEntity = stockQuoteEntity;
    }

    @Override
    public void setCacheDataState( final AsyncCacheEntryState cacheState )
    {
        this.cacheState = cacheState;
    }

    @Override
    public void setError( final String error )
    {
        this.error = error;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockQuoteEntityCacheDataReceiver{" );
        sb.append( "tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", stockQuoteEntity=" ).append( stockQuoteEntity );
        sb.append( ", cacheState=" ).append( cacheState );
        sb.append( ", error='" ).append( error ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }
}
