package com.stocktracker.weblayer.dto;

import com.stocktracker.repositorylayer.entity.StockCompanyEntity;
import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState;
import com.stocktracker.servicelayer.service.cache.stockcompany.StockCompanyEntityContainer;
import com.stocktracker.servicelayer.service.cache.stockquote.StockQuoteEntityContainer;
import com.stocktracker.weblayer.dto.common.UuidDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class StockCatalystEventDTO extends StockQuoteDTO
                                   implements UuidDTO,
                                              CustomerIdContainer,
                                              StockQuoteEntityContainer,
                                              StockCompanyEntityContainer
{
    private String customerId;
    private String catalystDate;
    private String catalystDesc;
    private Byte dateOrTimePeriod;
    private Byte timePeriod;
    private Short timePeriodYear;
    private AsyncCacheEntryState stockCompanyCacheState;
    private String stockCompanyError;
    private AsyncCacheEntryState stockQuoteCacheState;
    private String stockQuoteError;

    @Override
    public void setStockCompanyEntity( final StockCompanyEntity stockCompanyEntity )
    {
        BeanUtils.copyProperties( stockCompanyEntity, this );
    }

    @Override
    public void setStockCompanyCacheEntryState( final AsyncCacheEntryState cacheEntryState )
    {
        this.stockQuoteCacheState = cacheEntryState;
    }

    @Override
    public void setStockCompanyCacheError( final String error )
    {
        this.stockCompanyError = error;
    }

    @Override
    public void setStockQuoteEntity( final StockQuoteEntity stockQuoteEntity )
    {
        BeanUtils.copyProperties( stockQuoteEntity, this );
    }

    @Override
    public void setStockQuoteCacheEntryState( final AsyncCacheEntryState stockQuoteEntityCacheState )
    {
        this.stockQuoteCacheState = stockQuoteEntityCacheState;
    }

    @Override
    public void setStockQuoteCacheError( final String stockQuoteCacheError )
    {
        this.stockQuoteError = stockQuoteCacheError;
    }

    public String getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( String customerId )
    {
        this.customerId = customerId;
    }

    public String getCatalystDate()
    {
        return catalystDate;
    }

    public void setCatalystDate( String catalystDate )
    {
        this.catalystDate = catalystDate;
    }

    public String getCatalystDesc()
    {
        return catalystDesc;
    }

    public void setCatalystDesc( String catalystDesc )
    {
        this.catalystDesc = catalystDesc;
    }

    public Byte getDateOrTimePeriod()
    {
        return dateOrTimePeriod;
    }

    public void setDateOrTimePeriod( final Byte dateOrTimePeriod )
    {
        this.dateOrTimePeriod = dateOrTimePeriod;
    }

    public Byte getTimePeriod()
    {
        return timePeriod;
    }

    public void setTimePeriod( final Byte timePeriod )
    {
        this.timePeriod = timePeriod;
    }

    public Short getTimePeriodYear()
    {
        return timePeriodYear;
    }

    public void setTimePeriodYear( final Short timePeriodYear )
    {
        this.timePeriodYear = timePeriodYear;
    }

    public AsyncCacheEntryState getStockCompanyCacheState()
    {
        return stockCompanyCacheState;
    }

    public void setStockCompanyCacheState( final AsyncCacheEntryState stockCompanyCacheState )
    {
        this.stockCompanyCacheState = stockCompanyCacheState;
    }

    public String getStockCompanyError()
    {
        return stockCompanyError;
    }

    public void setStockCompanyError( final String stockCompanyError )
    {
        this.stockCompanyError = stockCompanyError;
    }

    public AsyncCacheEntryState getStockQuoteCacheState()
    {
        return stockQuoteCacheState;
    }

    public void setStockQuoteCacheState( final AsyncCacheEntryState stockQuoteCacheState )
    {
        this.stockQuoteCacheState = stockQuoteCacheState;
    }

    public String getStockQuoteError()
    {
        return stockQuoteError;
    }

    public void setStockQuoteError( final String stockQuotError )
    {
        this.stockQuoteError = stockQuotError;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockCatalystEventDTOEntity{" );
        sb.append( "tickerSymbol='" ).append( super.getTickerSymbol() ).append( '\'' );
        sb.append( ", id='" ).append( super.getId() ).append( '\'' );
        sb.append( ", customerId='" ).append( customerId ).append( '\'' );
        sb.append( ", catalystDate='" ).append( catalystDate ).append( '\'' );
        sb.append( ", catalystDesc='" ).append( catalystDesc ).append( '\'' );
        sb.append( ", dateOrTimePeriod=" ).append( dateOrTimePeriod );
        sb.append( ", timePeriod=" ).append( timePeriod );
        sb.append( ", timePeriodYear=" ).append( timePeriodYear );
        sb.append( ", stockCompanyCacheState=" ).append( stockCompanyCacheState );
        sb.append( ", stockCompanyError='" ).append( stockCompanyError ).append( '\'' );
        sb.append( ", stockQuoteCacheState=" ).append( stockQuoteCacheState );
        sb.append( ", stockQuoteError='" ).append( stockQuoteError ).append( '\'' );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
