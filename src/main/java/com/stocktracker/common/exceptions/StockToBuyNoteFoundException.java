package com.stocktracker.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value= HttpStatus.NOT_FOUND)  // 404
public class StockToBuyNoteFoundException extends Throwable
{
    public StockToBuyNoteFoundException( final int stockToBuyId )
    {
        super( "Stock to buy was not found for id: " + stockToBuyId );
    }
}
