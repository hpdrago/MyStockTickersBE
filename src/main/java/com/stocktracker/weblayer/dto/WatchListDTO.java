package com.stocktracker.weblayer.dto;

import com.stocktracker.weblayer.dto.common.CustomerIdContainer;
import com.stocktracker.weblayer.dto.common.DatabaseEntityDTO;
import com.stocktracker.weblayer.dto.common.UuidDTO;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by mike on 5/7/2017.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class WatchListDTO extends DatabaseEntityDTO<String>
                          implements CustomerIdContainer,
                                     UuidDTO
{
    private String name;
    private String customerId;
    private List<WatchListStockDTO> watchListStocks;

    public String getName()
    {
        return name;
    }

    @Override
    public void setCustomerId( final String customerId )
    {
        this.customerId = customerId;
    }

    @Override
    public String getCustomerId()
    {
        return this.customerId;
    }

    public void setName( final String name )
    {
        this.name = name;
    }

    public List<WatchListStockDTO> getWatchListStocks()
    {
        return watchListStocks;
    }

    public void setWatchListStocks( final List<WatchListStockDTO> watchListStocks )
    {
        this.watchListStocks = watchListStocks;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "WatchListDTO{" );
        sb.append( "name='" ).append( name ).append( '\'' );
        sb.append( ", customerId='" ).append( customerId ).append( '\'' );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( ", watchListStocks=" ).append( watchListStocks );
        sb.append( '}' );
        return sb.toString();
    }
}
