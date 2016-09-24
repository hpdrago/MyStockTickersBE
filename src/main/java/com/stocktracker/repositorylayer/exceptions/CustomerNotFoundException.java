package com.stocktracker.repositorylayer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by mike on 5/15/2016.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="CustomerDomainEntity not found")  // 404
public class CustomerNotFoundException extends RuntimeException
{
    /**
     * CustomerDomainEntity id not found
     * @param id
     */
    public CustomerNotFoundException( final int id )
    {
        super( "CustomerDomainEntity id: " + id + " was not found" );
    }

    /**
     * CustomerDomainEntity email not found
     * @param email
     */
    public CustomerNotFoundException( final String email )
    {
        super( "CustomerDomainEntity email: " + email + " was not found" );
    }
}
