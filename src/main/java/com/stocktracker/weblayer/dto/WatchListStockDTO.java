package com.stocktracker.weblayer.dto;

import com.stocktracker.weblayer.dto.common.DatabaseEntityDTO;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

/**
 * Created by mike on 8/24/2018.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class WatchListStockDTO extends DatabaseEntityDTO<UUID>
{
    private byte[] watchListUuid;
    private String tickerSymbol;
    private String notes;

    public byte[] getWatchListUuid()
    {
        return watchListUuid;
    }

    public void setWatchListUuid( final byte[] watchListUuid )
    {
        this.watchListUuid = watchListUuid;
    }

    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public void setTickerSymbol( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes( final String notes )
    {
        this.notes = notes;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "WatchListStockDTO{" );
        sb.append( "watchListUuid=" ).append( Arrays.toString( watchListUuid ) );
        sb.append( ", tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", notes='" ).append( notes ).append( '\'' );
        sb.append( ", super='" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
