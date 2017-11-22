package com.stocktracker.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is thrown when the a stock quote cannot be found for a ticker symbol.
 * Created by mike on 11/21/2017.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND)  // 404
public class StockNotFoundException extends RuntimeException
{
    public StockNotFoundException( final String tickerSymbol )
    {
        super( "Unable to get a quote for " + tickerSymbol );
    }
}
