package com.stocktracker.servicelayer.stockinformationprovider;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.stocktracker.common.JSONMoneySerializer;
import com.stocktracker.common.JSONTimestampDateTimeSerializer;
import com.stocktracker.servicelayer.service.stocks.StockOpenPriceContainer;
import com.stocktracker.servicelayer.service.stocks.StockPriceContainer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * This class is used to return stock update information from the stock quote service
 *
 * Created by mike on 12/10/2016.
 */
public class StockPriceDTO implements StockPriceContainer,
                                      StockOpenPriceContainer
{
    private String tickerSymbol;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal lastPrice;
    @JsonSerialize( using = JSONTimestampDateTimeSerializer.class )
    private Timestamp lastPriceChange;
    private StockPriceCacheState stockPriceCacheState;
    @JsonSerialize( using = JSONTimestampDateTimeSerializer.class )
    private Timestamp expiration;
    private BigDecimal openPrice;

    /**
     * Create a new instance with the ticker and the state.
     * This method is used when the stock quote is being retrieved asynchronously.
     * @param tickerSymbol
     * @param stockPrice
     * @param stockPriceCacheState
     * @return
     */
    public static StockPriceDTO newInstance( final String tickerSymbol,
                                             final BigDecimal stockPrice,
                                             final StockPriceCacheState stockPriceCacheState )
    {
        StockPriceDTO stockPriceDTO = new StockPriceDTO();
        stockPriceDTO.tickerSymbol = tickerSymbol;
        stockPriceDTO.stockPriceCacheState = stockPriceCacheState;
        stockPriceDTO.setLastPrice( stockPrice );
        return stockPriceDTO;
    }

    public StockPriceCacheState getStockPriceCacheState()
    {
        return stockPriceCacheState;
    }

    @Override
    public Timestamp getExpirationTime()
    {
        return expiration;
    }

    public void setExpirationTime( final Timestamp expiration )
    {
        this.expiration = expiration;
    }

    public void setStockPriceCacheState( final StockPriceCacheState stockPriceCacheState )
    {
        this.stockPriceCacheState = stockPriceCacheState;
    }

    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public void setTickerSymbol( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    public BigDecimal getLastPrice()
    {
        return lastPrice;
    }

    public void setLastPrice( final BigDecimal lastPrice )
    {
        this.lastPrice = lastPrice;// .divide( new BigDecimal( 1 ), 2,  BigDecimal.ROUND_HALF_UP  );
        if ( lastPrice != null )
        {
            lastPrice.setScale( 2, RoundingMode.HALF_UP );
        }
    }

    public Timestamp getLastPriceChange()
    {
        return lastPriceChange;
    }

    public void setLastPriceChange( Timestamp lastPriceChange )
    {
        this.lastPriceChange = lastPriceChange;
    }

    public void setOpenPrice( final BigDecimal openPrice )
    {
        this.openPrice = openPrice;
    }

    public BigDecimal getOpenPrice()
    {
        return this.openPrice;
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
        final StockPriceDTO that = (StockPriceDTO) o;
        return Objects.equals( tickerSymbol, that.tickerSymbol );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( tickerSymbol );
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockPriceQuote{" );
        sb.append( "tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", lastPrice=" ).append( lastPrice );
        sb.append( ", lastPriceChange=" ).append( lastPriceChange );
        sb.append( ", openPrice=" ).append( lastPrice );
        sb.append( ", stockPriceCacheState=" ).append( stockPriceCacheState );
        sb.append( ", expiration=" ).append( expiration );
        sb.append( '}' );
        return sb.toString();
    }
}
