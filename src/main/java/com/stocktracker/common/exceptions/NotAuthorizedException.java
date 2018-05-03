package com.stocktracker.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

/**
 * Created by mike on 5/15/2016.
 */
@ResponseStatus(value= HttpStatus.UNAUTHORIZED)  // 404
public class NotAuthorizedException extends Exception
{
    public NotAuthorizedException( final UUID id )
    {
        super( "Customer id: " + id + " was not found" );
    }

    public NotAuthorizedException( final String message, final Exception exception )
    {
        super( message, exception );
    }

    public NotAuthorizedException( final String message )
    {
        super( message );
    }
}
