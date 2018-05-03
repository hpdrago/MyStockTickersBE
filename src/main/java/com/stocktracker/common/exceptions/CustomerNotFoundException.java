package com.stocktracker.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

/**
 * Created by mike on 5/15/2016.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND)  // 404
public class CustomerNotFoundException extends Exception
{
    /**
     * Customer id not found
     * @param id
     */
    public CustomerNotFoundException( final UUID id )
    {
        super( "Customer id: " + id + " was not found" );
    }

    /**
     * Customer email not found
     * @param email
     */
    public CustomerNotFoundException( final String email )
    {
        super( "Customer email: " + email + " was not found" );
    }
}
