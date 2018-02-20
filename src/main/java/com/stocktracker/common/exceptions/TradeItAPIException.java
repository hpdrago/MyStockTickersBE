package com.stocktracker.common.exceptions;

/**
 * Common TradeItException that is thrown as a result of TradeIt API Calls.
 */
public class TradeItAPIException extends Exception
{
    public TradeItAPIException()
    {
    }

    public TradeItAPIException( final String message )
    {
        super( message );
    }
}
