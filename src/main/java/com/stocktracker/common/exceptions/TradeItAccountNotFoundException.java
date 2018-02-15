package com.stocktracker.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by mike on 12/4/2017.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND)  // 404
public class TradeItAccountNotFoundException extends Exception
{
    /**
     * TradeItAccountDTO id not found
     * @param id
     */
    public TradeItAccountNotFoundException( final int id )
    {
        super( "TradeItAccountDTO id: " + id + " was not found" );
    }

    public TradeItAccountNotFoundException( final int customerId, final int accountId )
    {
        super( "TradeItAccountDTO " + accountId + " was not found for customer " + customerId );
    }
}
