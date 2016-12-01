package com.stocktracker.repositorylayer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is thrown when an attempt is made to add a customer stock table entry that already exists
 * Created by mike on 11/9/2016.
 */
@ResponseStatus(value= HttpStatus.CONFLICT, reason="Duplicate customer stock")
public class DuplicateCustomerStockException extends RuntimeException
{
    public DuplicateCustomerStockException( final int customerId, final String tickerSymbol )
    {
        super( String.format( "Customer stock entry already exists for customer Id: %d tickerSymbol: %s",
                              customerId, tickerSymbol ));
    }
}
