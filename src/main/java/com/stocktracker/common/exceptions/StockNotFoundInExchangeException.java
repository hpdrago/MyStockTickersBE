package com.stocktracker.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

/**
 * Created by mike on 11/5/2016.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND)  // 404
public class StockNotFoundInExchangeException extends RuntimeException
{
    public StockNotFoundInExchangeException( final String tickerSymbol )
    {
        super( getMessage( tickerSymbol ) ) ;
    }

    private static String getMessage( final String tickerSymbol )
    {
        return "Ticker symbol " + tickerSymbol + " was not found";
    }

    public StockNotFoundInExchangeException( final String tickerSymbol, final IOException e )
    {
        super( getMessage( tickerSymbol ), e );
    }
}
