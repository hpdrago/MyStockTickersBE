package com.stocktracker.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

/**
 * Generic exception used when an entity cannot be found.
 */
@ResponseStatus( value= HttpStatus.NOT_FOUND )  // 404
public class EntityNotFoundException extends Exception
{
    private Serializable key;

    /**
     * Stores the key that was not found which can then be used by the exception handling routines to give some context
     * to the error.
     * @param key
     * @param <K>
     */
    public <K extends Serializable> EntityNotFoundException( final K key )
    {
        this.key = key;
    }
}
