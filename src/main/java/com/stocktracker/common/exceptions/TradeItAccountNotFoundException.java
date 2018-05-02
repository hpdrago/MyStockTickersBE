package com.stocktracker.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

/**
 * Created by mike on 12/4/2017.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND)  // 404
public class TradeItAccountNotFoundException extends Exception
{
    /**
     * TradeItAccountDTO id not found
     * @param tradeItAccountUuid
     */
    public TradeItAccountNotFoundException( final UUID tradeItAccountUuid )
    {
        super( "TradeItAccountDTO uuid: " + tradeItAccountUuid + " was not found" );
    }

    /**
     * TradeItAccountDTO id not found
     * @param tradeItAccountUuid
     */
    public TradeItAccountNotFoundException( final String tradeItAccountUuid )
    {
        super( "TradeItAccountDTO uuid: " + tradeItAccountUuid + " was not found" );
    }
}
