package com.stocktracker.servicelayer.service.cache.stockquote;

import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import com.stocktracker.servicelayer.service.cache.common.BaseAsyncCacheDataContainer;
import com.stocktracker.servicelayer.service.stocks.TickerSymbolContainer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/*
* *
 * This interface defines the methods for classes that will get Stock Quote information from the StockQuoteEntityCache
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class StockQuoteEntityContainer extends BaseAsyncCacheDataContainer<String,StockQuoteEntity,String>
                                       implements TickerSymbolContainer
{
    private String tickerSymbol;
    private StockQuoteEntity stockQuoteEntity;

    @Override
    public String getASyncKey()
    {
        return this.tickerSymbol;
    }

    @Override
    public String getCacheKey()
    {
        return this.tickerSymbol;
    }

    @Override
    public void setCacheKey( final String cacheKey )
    {
        this.tickerSymbol = cacheKey;
    }

    @Override
    public void setCachedData( final StockQuoteEntity cachedData )
    {
        this.stockQuoteEntity = stockQuoteEntity;
    }

    @Override
    public StockQuoteEntity getCachedData()
    {
        return this.stockQuoteEntity;
    }

    @Override
    public String getTickerSymbol()
    {
        return this.tickerSymbol;
    }

    @Override
    public void setTickerSymbol( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockQuoteEntityContainer{" );
        sb.append( "tickerSymbol='" ) .append( tickerSymbol ) .append( '\'' );
        sb.append( ", stockQuoteEntity=" ) .append( stockQuoteEntity );
        sb.append( ", super=" ) .append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
