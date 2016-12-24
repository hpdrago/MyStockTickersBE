package com.stocktracker.servicelayer.entity;

import com.stocktracker.repositorylayer.common.BooleanUtils;
import com.stocktracker.repositorylayer.db.entity.StockEntity;
import com.stocktracker.servicelayer.service.StockTickerQuote;
import com.stocktracker.weblayer.dto.StockDTO;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Created by mike on 9/11/2016.
 */
public class StockDE
{
    private String tickerSymbol;
    private String companyName;
    private String exchange;
    private int createdBy;
    private boolean userEntered;
    private BigDecimal lastPrice;
    private Timestamp lastPriceChange;
    private Timestamp lastPriceUpdate;

    /**
     * Create a new {@code StockDE} from a {@code StockEntity} instance
     * @param stockEntity
     * @return
     */
    public static StockDE newInstance( final StockEntity stockEntity )
    {
        StockDE stockDE = new StockDE();
        BeanUtils.copyProperties( stockEntity, stockDE );
        stockDE.setUserEntered( BooleanUtils.fromCharToBoolean( stockEntity.getUserEntered() ));
        return stockDE;
    }

    /**
     * Create a new {@code StockDE} from a {@code StockDTO} instance
     * @param stockDto
     * @return
     */
    public static StockDE newInstance( final StockDTO stockDto )
    {
        StockDE stockDE = new StockDE();
        BeanUtils.copyProperties( stockDto, stockDE );
        return stockDE;
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

    public String getExchange()
    {
        return exchange;
    }

    public void setExchange( final String exchange )
    {
        this.exchange = exchange;
    }

    public int getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy( int createdBy )
    {
        this.createdBy = createdBy;
    }

    public boolean isUserEntered()
    {
        return userEntered;
    }

    public void setUserEntered( final boolean userEntered )
    {
        this.userEntered = userEntered;
    }

    public BigDecimal getLastPrice()
    {
        return lastPrice;
    }

    public void setLastPrice( BigDecimal lastPrice )
    {
        this.lastPrice = lastPrice;
    }

    public Timestamp getLastPriceChange()
    {
        return lastPriceChange;
    }

    public void setLastPriceChange( Timestamp lastPriceUpdate )
    {
        this.lastPriceChange = lastPriceChange;
    }

    public Timestamp getLastPriceUpdate()
    {
        return lastPriceUpdate;
    }

    public void setLastPriceUpdate( Timestamp lastPriceUpdate )
    {
        this.lastPriceUpdate = lastPriceUpdate;
    }

    public void updateFromQuote( final StockTickerQuote stockTickerQuote )
    {
        this.setLastPrice( stockTickerQuote.getLastPrice() );
        this.setLastPriceChange( stockTickerQuote.getLastPriceChange() );
        this.setLastPriceUpdate( stockTickerQuote.getLastPriceUpdate() );
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockDE" );
        sb.append( "{" );
        sb.append( "tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", companyName='" ).append( companyName ).append( '\'' );
        sb.append( ", exchange='" ).append( exchange ).append( '\'' );
        sb.append( ", createdBy='" ).append( createdBy ).append( '\'' );
        sb.append( ", userEntered='" ).append( userEntered ).append( '\'' );
        sb.append( ", lastPrice='" ).append( lastPrice ).append( '\'' );
        sb.append( ", lastPriceChange='" ).append( lastPriceChange ).append( '\'' );
        sb.append( ", lastPriceUpdate='" ).append( lastPriceUpdate ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }

    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }
        final StockDE that = (StockDE) o;
        return Objects.equals( tickerSymbol, that.tickerSymbol );
    }
    @Override
    public int hashCode()
    {
        return Objects.hash( tickerSymbol, companyName, exchange );
    }
}
