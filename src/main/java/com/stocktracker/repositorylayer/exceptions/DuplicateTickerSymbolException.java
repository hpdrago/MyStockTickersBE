package com.stocktracker.repositorylayer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by mike on 11/9/2016.
 */
@ResponseStatus(value= HttpStatus.CONFLICT, reason="Duplicate ticker symbol")
public class DuplicateTickerSymbolException extends RuntimeException
{
    public DuplicateTickerSymbolException( final String tickerSymbol )
    {
        super( tickerSymbol + " already exists" );
    }
}
