package com.stocktracker.servicelayer.service;

import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.repositorylayer.entity.VersionedEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public abstract class DMLEntityService<K extends Serializable,
                                       E extends VersionedEntity<K>,
                                       D extends VersionedEntity<K>,
                                       R extends JpaRepository<E,K>>
    extends BaseEntityService<K,E,D,R>
{


    /**
     * Converts the DTO to and Entity and saves it to the database.
     * @param dto
     * @return
     */
    /*
    protected D saveEntity( final D dto  )
        throws EntityVersionMismatchException
    {
        final String methodName = "saveEntity";
        logMethodBegin( methodName, dto );
        E entity = this.dtoToEntity( dto );
        E savedEntity = this.getRepository().save( entity );
        D returnDTO = this.entityToDTO( savedEntity );
        logMethodEnd( methodName, returnDTO );
        return returnDTO;
    }
    */

    /**
     * Converts the DTO to and Entity and saves it to the database.
     * @param dto
     * @return Entity that was saved to the database.
     * @throws EntityVersionMismatchException
     */
    protected D saveEntity( final D dto  )
        throws EntityVersionMismatchException
    {
        final String methodName = "saveEntity";
        logMethodBegin( methodName, dto );
        checkEntityVersion( dto );
        final E entity;
        entity = this.dtoToEntity( dto );
        E savedEntity = this.getRepository().save( entity );
        D returnDTO = this.entityToDTO( savedEntity );
        logMethodEnd( methodName, returnDTO );
        return returnDTO;
    }

    /**
     * Compares the dto.version to the version in the database and if the version are not the same, then
     * {@code EntityVersionMismatchException} is thrown.
     * @param dto
     * @return
     */
    protected E checkEntityVersion( final D dto )
        throws EntityVersionMismatchException
    {
        final E entity = this.getRepository()
                             .findOne( (K)dto.getId() );
        if ( entity.getVersion() != dto.getVersion() )
        {
            throw new EntityVersionMismatchException( String.format( "Entity version mismatch(%d <> %d)",
                                                                     dto.getVersion(), entity.getVersion() ));
        }
        return entity;
    }
}
