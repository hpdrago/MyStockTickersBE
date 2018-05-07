package com.stocktracker.common.exceptions;

import com.stocktracker.repositorylayer.common.VersionedEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.CONFLICT)
public class DuplicateEntityException extends Exception
{
    public DuplicateEntityException( VersionedEntity<?> existingEntity )
    {
        super( "Attempting to create a new entity that already exists: " + existingEntity );
    }

    public DuplicateEntityException( final VersionedEntity<?> entity,
                                     final DataIntegrityViolationException e )
    {
        super( entity.toString(), e );
    }
}
