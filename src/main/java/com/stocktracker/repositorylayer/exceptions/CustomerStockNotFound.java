package com.stocktracker.repositorylayer.exceptions;

import com.stocktracker.repositorylayer.db.entity.CustomerStockEntityPK;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by mike on 11/25/2016.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Customer Stock entry not found")  // 404
public class CustomerStockNotFound extends Throwable
{
    public CustomerStockNotFound( final CustomerStockEntityPK customerStockEntityPK )
    {
        super( String.format( "Customer stock not found for customer id: %d ticker symbol: %s",
                              customerStockEntityPK.getCustomerId(), customerStockEntityPK.getTickerSymbol() ));
    }
}
