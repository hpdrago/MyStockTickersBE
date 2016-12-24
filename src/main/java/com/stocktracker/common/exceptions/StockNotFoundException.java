package com.stocktracker.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by mike on 11/5/2016.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND)  // 404
public class StockNotFoundException extends RuntimeException
{
    public StockNotFoundException( final String tickerSymbol )
    {
        super( "Ticker symbol " + tickerSymbol + " was not found" ) ;
    }
}
