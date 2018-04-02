package com.stocktracker.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is thrown when the a stock quote cannot be found for a ticker symbol.
 * Created by mike on 11/21/2017.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND)  // 404
public class StockQuoteNotFoundException extends Exception
{
    public StockQuoteNotFoundException( final String tickerSymbol )
    {
        super( "Stock quote not found for ticker symbol " + tickerSymbol );
    }

    public StockQuoteNotFoundException( final String tickerSymbol, final Exception e )
    {
        super( "Stock quote not found for ticker symbol " + tickerSymbol, e );
    }
}
