package com.stocktracker.servicelayer.service;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * This class is used to return stock update information from the stock quote service
 *
 * Created by mike on 12/10/2016.
 */
public class StockTickerQuote
{
    private String tickerSymbol;
    private String companyName;
    private BigDecimal lastPrice;
    private Timestamp lastPriceChange;

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
        this.lastPrice = lastPrice;
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

    public void setCompanyName( final String companyName )
    {
        this.companyName = companyName;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockTickerQuote{" );
        sb.append( "tickerSymbol='" ).append( tickerSymbol ).append( '\'' );
        sb.append( ", lastPrice=" ).append( lastPrice );
        sb.append( ", lastPriceChange=" ).append( lastPriceChange );
        sb.append( ", companyName=" ).append( companyName );
        sb.append( '}' );
        return sb.toString();
    }
}
