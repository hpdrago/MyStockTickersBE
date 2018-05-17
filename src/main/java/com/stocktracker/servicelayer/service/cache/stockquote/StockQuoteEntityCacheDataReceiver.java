package com.stocktracker.servicelayer.service.cache.stockquote;

import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDataReceiver;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState;

import java.util.Date;

/**
 * This class implements the {@code AsyncCacheDataReceiver} interface in order to interact with the {@code StockQuoteAsyncCache}.
 * This class will contain the results of requesting a stock quote from the cache.  It will then be used to by the
 * {@code StockQuoteEntityService} to set the quote values on the target target class that is requesting stock quote
 * information.
 */
public class StockQuoteEntityCacheDataReceiver implements AsyncCacheDataReceiver<String,StockQuoteEntity>
{
    private String tickerSymbol;
    private StockQuoteEntity stockQuoteEntity;
    private AsyncCacheEntryState cacheState;
    private String error;
    private Date dataExpiration;

    /**
     * Constructor.
     * @param tickerSymbol
     */
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

    /**
     * Key used to retrieve the {@code StockQuoteEntity} from the database.
     * @return
     */
    @Override
    public String getEntityKey()
    {
        return this.tickerSymbol;
    }

    /**
     * Set the StockQuoteEntity data.
     * @param stockQuoteEntity
     */
    @Override
    public void setCachedData( final StockQuoteEntity stockQuoteEntity )
    {
        this.stockQuoteEntity = stockQuoteEntity;
    }

    /**
     * Set the cache data state.
     * @param cacheState
     */
    @Override
    public void setCacheDataState( final AsyncCacheEntryState cacheState )
    {
        this.cacheState = cacheState;
    }

    /**
     * Set the error value from the asynchronous fetch process.
     * @param error
     */
    @Override
    public void setCacheError( final String error )
    {
        this.error = error;
    }

    public Date getDataExpiration()
    {
        return dataExpiration;
    }

    @Override
    public void setDataExpiration( final Date dataExpiration )
    {
        this.dataExpiration = dataExpiration;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockQuoteEntityCacheDataReceiver{" );
        sb.append( "tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", stockQuoteEntity=" ).append( stockQuoteEntity );
        sb.append( ", cacheState=" ).append( cacheState );
        sb.append( ", error='" ).append( error ).append( '\'' );
        sb.append( ", dataExpiration=" ).append( dataExpiration );
        sb.append( '}' );
        return sb.toString();
    }
}
