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
        super( getMessage( tradeItAccountUuid ) );
    }

    /**
     * TradeItAccountDTO id not found
     * @param tradeItAccountUuid
     */
    public TradeItAccountNotFoundException( final String tradeItAccountUuid )
    {
        super( getMessage( tradeItAccountUuid ) );
    }

    public TradeItAccountNotFoundException( final UUID tradeItAccountUuid, final VersionedEntityNotFoundException e )
    {
        super( getMessage( tradeItAccountUuid ), e);
    }

    public TradeItAccountNotFoundException( final UUID customerUuid, final String accountName )
    {
        super( "TradeIt account not found for customer UUID: " + customerUuid + " and account name: " + accountName );
    }

    private static String getMessage( final Object tradeItAccountUuid )
    {
        return "TradeItAccountDTO uuid: " + tradeItAccountUuid + " was not found";
    }
}
