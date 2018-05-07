package com.stocktracker.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is thrown when the a stock quote cannot be found for a ticker symbol.
 * Created by mike on 11/21/2017.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND)  // 404
public class StockNotFoundException extends RuntimeException // RuntimeException so we don't need to declare throws
{
    private String tickerSymbol;
    private boolean discontinued;

    public StockNotFoundException( final String tickerSymbol )
    {
        super( "Unable to get a quote for " + tickerSymbol );
        this.tickerSymbol = tickerSymbol;
        this.discontinued = false;
    }

    public StockNotFoundException( final String tickerSymbol, final Exception e )
    {
        super( "Unable to get a quote for " + tickerSymbol, e ) ;
        this.tickerSymbol = tickerSymbol;
        this.discontinued = false;
    }

    public StockNotFoundException( final String tickerSymbol, final boolean discontinued )
    {
        this.tickerSymbol = tickerSymbol;
        this.discontinued = true;
    }

    public String getTickerSymbol()
    {
        return tickerSymbol;
    }

    public void setTickerSymbol( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    public boolean isDiscontinued()
    {
        return discontinued;
    }

    public void setDiscontinued( final boolean discontinued )
    {
        this.discontinued = discontinued;
    }
}
