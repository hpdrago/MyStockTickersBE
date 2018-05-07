package com.stocktracker.common.exceptions;

import com.stocktracker.repositorylayer.common.VersionedEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.OptimisticLockException;

/**
 * This exception is thrown when comparing the version of an entity to the version that is in the database and if
 * the versions do not match then the entity is out of date and the update will abort.
 */
@ResponseStatus( value= HttpStatus.PRECONDITION_FAILED )  // 412
public class EntityVersionMismatchException extends RuntimeException // RuntimeException so we don't to declare throws
{
    public EntityVersionMismatchException( final VersionedEntity<?> currentEntity, OptimisticLockException e )
    {
        super( "Optimistic lock failed for: " + currentEntity, e );
    }

    /**
     * Get the current version in the database for the entity when the exception is generated.
     * @return
     */
    public int getCurrentVersion()
    {
        return currentVersion;
    }

    private int currentVersion;

    /**
     * Create with a message.
     * @param currentVersion The current database version.
     * @param message
     */
    public EntityVersionMismatchException( final int currentVersion, final String message )
    {
        super( message );
        this.currentVersion = currentVersion;
    }
}
