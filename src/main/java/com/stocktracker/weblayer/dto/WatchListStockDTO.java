package com.stocktracker.weblayer.dto;

import com.stocktracker.servicelayer.service.stocks.TickerSymbolContainer;
import com.stocktracker.weblayer.dto.common.DatabaseEntityDTO;
import com.stocktracker.weblayer.dto.common.UuidDTO;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by mike on 8/24/2018.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class WatchListStockDTO extends DatabaseEntityDTO<String>
                               implements UuidDTO,
                                          TickerSymbolContainer
{
    private String watchListId;
    private String watchListName;
    private String tickerSymbol;
    private String notes;
    private BigDecimal stockPriceWhenCreated;
    private Integer shares;
    private BigDecimal costBasis;

    public String getWatchListId()
    {
        return watchListId;
    }

    public void setWatchListId( final String watchListId )
    {
        this.watchListId = watchListId;
    }

    @Override
    public void setId( final String id )
    {
        this.watchListId = id;
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

    public BigDecimal getStockPriceWhenCreated()
    {
        return stockPriceWhenCreated;
    }

    public void setStockPriceWhenCreated( final BigDecimal stockPriceWhenCreated )
    {
        this.stockPriceWhenCreated = stockPriceWhenCreated;
    }

    public Integer getShares()
    {
        return shares;
    }

    public void setShares( final Integer shares )
    {
        this.shares = shares;
    }

    public BigDecimal getCostBasis()
    {
        return costBasis;
    }

    public void setCostBasis( final BigDecimal costBasis )
    {
        this.costBasis = costBasis;
    }

    public String getWatchListName()
    {
        return watchListName;
    }

    public void setWatchListName( final String watchListName )
    {
        this.watchListName = watchListName;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "WatchListStockDTO{" );
        sb.append( "watchListId='" ).append( watchListId ).append( '\'' );
        sb.append( ", watchListName='" ).append( watchListName ).append( '\'' );
        sb.append( ", tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", notes='" ).append( notes ).append( '\'' );
        sb.append( ", stockPriceWhenCreated=" ).append( stockPriceWhenCreated );
        sb.append( ", shares=" ).append( shares );
        sb.append( ", costBasis=" ).append( costBasis );
        sb.append( '}' );
        return sb.toString();
    }
}
