package com.stocktracker.servicelayer.stockinformationprovider;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.stocktracker.common.JSONMoneySerializer;
import com.stocktracker.common.JSONTimestampToDateTimeSerializer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * This class is used to return stock update information from the stock quote service
 *
 * Created by mike on 12/10/2016.
 */
public class StockTickerQuote implements StockQuote
{
    private String tickerSymbol;
    private String companyName;
    private String stockExchange;
    @JsonSerialize( using = JSONMoneySerializer.class )
    private BigDecimal lastPrice;
    @JsonSerialize( using = JSONTimestampToDateTimeSerializer.class )
    private Timestamp lastPriceChange;
    private StockQuoteState stockQuoteState;
    @JsonSerialize( using = JSONTimestampToDateTimeSerializer.class )
    private Timestamp expiration;

    /**
     * Create a new instance with the ticker and the state.
     * This method is used when the stock quote is being retrieved asynchronously.
     * @param tickerSymbol
     * @param stockQuoteState
     * @return
     */
    public static StockTickerQuote newInstance( final String tickerSymbol,
                                                final StockQuoteState stockQuoteState )
    {
        StockTickerQuote stockTickerQuote = new StockTickerQuote();
        stockTickerQuote.tickerSymbol = tickerSymbol;
        stockTickerQuote.stockQuoteState = stockQuoteState;
        return stockTickerQuote;
    }

    public StockQuoteState getStockQuoteState()
    {
        return stockQuoteState;
    }

    @Override
    public Timestamp getExpiration()
    {
        return expiration;
    }

    public void setExpiration( final Timestamp expiration )
    {
        this.expiration = expiration;
    }

    public void setStockQuoteState( final StockQuoteState stockQuoteState )
    {
        this.stockQuoteState = stockQuoteState;
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


    public String getCompanyName()
    {
        return companyName;
    }

    @Override
    public String getStockExchange()
    {
        return stockExchange;
    }

    public void setCompanyName( final String companyName )
    {
        this.companyName = companyName;
    }

    public void setStockExchange( final String stockExchange )
    {
        this.stockExchange = stockExchange;
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
        final StockTickerQuote that = (StockTickerQuote) o;
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
        final StringBuilder sb = new StringBuilder( "StockTickerQuote{" );
        sb.append( "tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", companyName='" ).append( companyName ).append( '\'' );
        sb.append( ", stockExchange='" ).append( companyName ).append( '\'' );
        sb.append( ", lastPrice=" ).append( lastPrice );
        sb.append( ", lastPriceChange=" ).append( lastPriceChange );
        sb.append( ", stockQuoteState=" ).append( stockQuoteState );
        sb.append( ", expiration=" ).append( expiration );
        sb.append( '}' );
        return sb.toString();
    }
}
