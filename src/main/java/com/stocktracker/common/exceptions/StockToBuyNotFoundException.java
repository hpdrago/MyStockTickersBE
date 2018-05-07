package com.stocktracker.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus( value= HttpStatus.NOT_FOUND)  // 404
public class StockToBuyNotFoundException extends Throwable
{
    public StockToBuyNotFoundException( final String stockToBuyId )
    {
        super( "Stock to buy was not found for id: " + stockToBuyId );
    }

    public StockToBuyNotFoundException( final UUID stockToBuyUuid )
    {
        this( stockToBuyUuid.toString() );
    }

    public StockToBuyNotFoundException( final String stockToBuyId, final EntityNotFoundException e )
    {
        super( "Stock to buy was not found for id: " + stockToBuyId, e );
    }
}
