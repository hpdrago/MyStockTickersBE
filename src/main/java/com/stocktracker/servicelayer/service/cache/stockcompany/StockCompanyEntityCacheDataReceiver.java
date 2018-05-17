package com.stocktracker.servicelayer.service.cache.stockcompany;

import com.stocktracker.repositorylayer.entity.StockCompanyEntity;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDataReceiver;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState;

import java.util.Date;

/**
 * This class implement the {@code AsyncCacheDataReceiver} interface in order to interact with the {@code StockCompanyAsyncCache}.
 * This class will contain the results of requesting a stock company from the cache.
 */
public class StockCompanyEntityCacheDataReceiver implements AsyncCacheDataReceiver<String,StockCompanyEntity>
{
    private String tickerSymbol;
    private StockCompanyEntity stockCompanyEntity;
    private AsyncCacheEntryState cacheState;
    private String error;
    private Date dataExpiration;

    public StockCompanyEntityCacheDataReceiver( final String tickerSymbol )
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

    public StockCompanyEntity getStockCompanyEntity()
    {
        return stockCompanyEntity;
    }

    public void setStockCompanyEntity( final StockCompanyEntity stockCompanyEntity )
    {
        this.stockCompanyEntity = stockCompanyEntity;
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
    public void setCachedData( final StockCompanyEntity stockCompanyEntity )
    {
        this.stockCompanyEntity = stockCompanyEntity;
    }

    @Override
    public void setCacheDataState( final AsyncCacheEntryState cacheState )
    {
        this.cacheState = cacheState;
    }

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
        final StringBuilder sb = new StringBuilder( "StockCompanyEntityCacheDataReceiver{" );
        sb.append( "tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", stockCompanyEntity=" ).append( stockCompanyEntity );
        sb.append( ", cacheState=" ).append( cacheState );
        sb.append( ", error='" ).append( error ).append( '\'' );
        sb.append( ", dataExpiration='" ).append( dataExpiration ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }
}
