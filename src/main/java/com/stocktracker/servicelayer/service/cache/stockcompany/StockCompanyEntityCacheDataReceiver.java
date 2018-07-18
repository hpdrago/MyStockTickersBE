package com.stocktracker.servicelayer.service.cache.stockcompany;

import com.stocktracker.repositorylayer.entity.StockCompanyEntity;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDataReceiver;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * This class implement the {@code AsyncCacheDataReceiver} interface in order to interact with the {@code StockCompanyAsyncCache}.
 * This class will contain the results of requesting a stock company from the cache.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class StockCompanyEntityCacheDataReceiver implements AsyncCacheDataReceiver<String,StockCompanyEntity>
{
    private String tickerSymbol;
    private StockCompanyEntity stockCompanyEntity;
    private AsyncCacheEntryState cacheState;
    private String error;
    private Timestamp dataExpiration;

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
    public void setCachedData( final StockCompanyEntity stockCompanyEntity )
    {
        this.stockCompanyEntity = stockCompanyEntity;
    }

    @Override
    public StockCompanyEntity getCachedData()
    {
        return this.stockCompanyEntity;
    }

    @Override
    public void setCacheState( final AsyncCacheEntryState cacheState )
    {
        this.cacheState = cacheState;
    }

    @Override
    public AsyncCacheEntryState getCacheState()
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
        return this.error;
    }

    @Override
    public Timestamp getExpirationTime()
    {
        return dataExpiration;
    }

    @Override
    public void setExpirationTime( final Timestamp dataExpiration )
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
