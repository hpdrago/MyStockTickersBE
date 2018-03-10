package com.stocktracker.common.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpServerErrorException;

@ResponseStatus( value= HttpStatus.SERVICE_UNAVAILABLE )  // 503
public class TradeItServiceUnavailableException extends RuntimeException
{
    public TradeItServiceUnavailableException( final Exception e )
    {
        super( "TradeIt Service Unavailable", e );
    }
}
