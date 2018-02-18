package com.stocktracker.servicelayer.service;

import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.repositorylayer.entity.VersionedEntity;
import com.stocktracker.weblayer.dto.VersionedDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

/**
 * Base Service class for managing database entities.
 * @param <K> The primary key type.
 * @param <E> The database entity type.
 * @param <D> The DTO type.
 * @param <R> The entity repository type.
 */
public abstract class DMLEntityService<K extends Serializable,
                                       E extends VersionedEntity<K>,
                                       D extends VersionedDTO<K>,
                                       R extends JpaRepository<E,K>>
    extends BaseEntityService<K,E,D,R>
{
    /**
     * Delete the entity.
     * @param entity
     */
    public void deleteEntity( final E entity )
    {
        this.getRepository()
            .delete( entity.getId() );
    }

    /**
     * Converts the DTO to and Entity and saves it to the database.
     * @param dto
     * @return Entity that was saved to the database.
     * @throws EntityVersionMismatchException
     */
    public D saveEntity( final D dto  )
        throws EntityVersionMismatchException
    {
        final String methodName = "saveEntity";
        logMethodBegin( methodName, dto );
        checkEntityVersion( dto );
        final E entity = this.dtoToEntity( dto );
        final E savedEntity = saveEntity( entity );
        final D returnDTO = this.entityToDTO( savedEntity );
        logMethodEnd( methodName, returnDTO );
        return returnDTO;
    }

    /**
     * Add the entity to the database.
     * @param entity
     * @return
     */
    public E addEntity( final E entity )
    {
        final String methodName = "addEntity";
        logMethodBegin( methodName, entity );
        entity.setVersion( 1 );
        final E returnEntity = this.saveEntity( entity );
        logMethodEnd( methodName, returnEntity );
        return returnEntity;
    }

    /**
     * Saves the entity to the database.
     * @param entity
     * @return
     */
    public E saveEntity( final E entity )
    {
        final String methodName = "saveEntity";
        logMethodBegin( methodName, entity );
        final E savedEntity = this.getRepository().save( entity );
        logMethodEnd( methodName, entity );
        return savedEntity;
    }

    /**
     * Compares the dto.version to the version in the database and if the version are not the same, then
     * {@code EntityVersionMismatchException} is thrown.
     * @param dto
     * @return
     */
    public E checkEntityVersion( final VersionedEntity<K> dto )
        throws EntityVersionMismatchException
    {
        final E entity = this.getRepository()
                             .findOne( dto.getId() );
        if ( entity.getVersion() != dto.getVersion() )
        {
            throw new EntityVersionMismatchException( String.format( "Entity version mismatch(%d <> %d)",
                                                                     dto.getVersion(), entity.getVersion() ));
        }
        return entity;
    }
}
