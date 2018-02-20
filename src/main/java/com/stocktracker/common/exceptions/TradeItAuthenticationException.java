package com.stocktracker.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception indicates that a REST API call resulted in an authentication error or page redirection.
 */
@ResponseStatus( value= HttpStatus.UNAUTHORIZED )  // 401
public class TradeItAuthenticationException extends TradeItAPIException
{
    public TradeItAuthenticationException()
    {
    }

    public TradeItAuthenticationException( final String message )
    {
        super( message );
    }
}
