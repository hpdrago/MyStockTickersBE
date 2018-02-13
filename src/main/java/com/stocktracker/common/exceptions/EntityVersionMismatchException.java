package com.stocktracker.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is thrown when comparing the version of an entity to the version that is in the database and if
 * the versions do not match then the entity is out of date and the update will abort.
 */
@ResponseStatus( value= HttpStatus.PRECONDITION_FAILED)  // 412
public class EntityVersionMismatchException extends Exception
{
    /**
     * Create with a message.
     * @param message
     */
    public EntityVersionMismatchException( final String message )
    {
        super( message );
    }
}
