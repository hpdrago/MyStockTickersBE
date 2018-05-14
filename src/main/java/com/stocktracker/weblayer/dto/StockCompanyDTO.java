package com.stocktracker.weblayer.dto;

import com.stocktracker.repositorylayer.common.VersionedEntity;
import com.stocktracker.repositorylayer.entity.StockCompanyEntity;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState;
import com.stocktracker.servicelayer.service.cache.stockcompany.StockCompanyEntityContainer;
import com.stocktracker.servicelayer.service.stocks.StockCompanyContainer;
import com.stocktracker.weblayer.dto.common.DatabaseEntityDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * The DTO class for a stock company entity {@code StockCompanyEntity} which is retrieved from IEXTrading.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class StockCompanyDTO extends DatabaseEntityDTO<String>
                             implements VersionedEntity,
                                        StockCompanyContainer,
                                        StockCompanyEntityContainer
{
    private String tickerSymbol;
    private String companyName;
    private String website;
    private String sector;
    private String industry;
    private String cacheError;
    private AsyncCacheEntryState cacheState;

    @Override
    public void setStockCompanyEntity( final StockCompanyEntity stockCompanyEntity )
    {
        BeanUtils.copyProperties( stockCompanyEntity, this );
    }

    @Override
    public void setStockCompanyCacheEntryState( final AsyncCacheEntryState cacheEntryState )
    {
        this.cacheState = cacheEntryState;
    }

    @Override
    public void setStockCompanyCacheError( final String cacheError )
    {
        this.cacheError = cacheError;
    }

    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public void setTickerSymbol( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName( final String companyName )
    {
        this.companyName = companyName;
    }

    public String getWebsite()
    {
        return website;
    }

    public void setWebsite( final String website )
    {
        this.website = website;
    }

    public String getSector()
    {
        return sector;
    }

    public void setSector( final String sector )
    {
        this.sector = sector;
    }

    public String getIndustry()
    {
        return industry;
    }

    public void setIndustry( final String industry )
    {
        this.industry = industry;
    }

    public AsyncCacheEntryState getCacheState()
    {
        return cacheState;
    }

    public void setCacheState( final AsyncCacheEntryState cacheState )
    {
        this.cacheState = cacheState;
    }

    public String getCacheError()
    {
        return cacheError;
    }

    public void setCacheError( final String cacheError )
    {
        this.cacheError = cacheError;
    }


    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockCompanyEntityCacheDTO{" );
        sb.append( "tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", companyName='" ).append( companyName ).append( '\'' );
        sb.append( ", website='" ).append( website ).append( '\'' );
        sb.append( ", sector='" ).append( sector ).append( '\'' );
        sb.append( ", industry='" ).append( industry ).append( '\'' );
        sb.append( ", cacheError='" ).append( cacheError ).append( '\'' );
        sb.append( ", cacheState=" ).append( cacheState );
        sb.append( '}' );
        return sb.toString();
    }
}
