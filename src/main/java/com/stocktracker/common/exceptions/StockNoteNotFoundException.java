package com.stocktracker.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by mike on 10/04/2017.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND)  // 404
public class StockNoteNotFoundException extends RuntimeException
{
    public StockNoteNotFoundException( final int stockNoteId )
    {
        super( String.format( "Stock note not found for stock note id: %d", stockNoteId ));
    }
}
