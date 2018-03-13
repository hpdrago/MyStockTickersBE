package com.stocktracker.servicelayer.service;

import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.entity.VersionedEntity;
import com.stocktracker.repositorylayer.repository.VersionedEntityRepository;
import com.stocktracker.weblayer.dto.VersionedDTO;

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
                                       R extends VersionedEntityRepository<K,E>>
    extends BaseEntityService<K,E,D,R>
{
    /**
     * Get the DTO for the entity matching key.
     * @param key
     * @return
     * @throws VersionedEntityNotFoundException
     */
    public D getDTO( final int customerId, final K key )
        throws VersionedEntityNotFoundException
    {
        final String methodName = "getEntity";
        logMethodBegin( methodName, customerId, key );
        final E entity = this.getRepository()
                             .findByCustomerIdAndId( customerId, key );
        if ( entity == null )
        {
            throw new VersionedEntityNotFoundException( key );
        }
        final D dto = this.entityToDTO( entity );
        logMethodEnd( methodName, dto );
        return dto;
    }

    /**
     * Delete the entity.
     * @param entity
     */
    public void deleteEntity( final E entity )
    {
        final String methodName = "deleteEntity";
        logMethodBegin( methodName, entity );
        this.getRepository()
            .delete( entity.getId() );
        logMethodEnd( methodName );
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
        final E returnEntity = this.getRepository()
                                   .save( entity );
        logMethodEnd( methodName, returnEntity );
        return returnEntity;
    }

    /**
     * Converts the DTO to and Entity and saves it to the database.
     * @param dto
     * @return Entity that was saved to the database.
     * @throws EntityVersionMismatchException
     */
    public D saveDTO( final D dto  )
        throws EntityVersionMismatchException
    {
        final String methodName = "saveDTO";
        logMethodBegin( methodName, dto );
        checkEntityVersion( dto );
        final E entity = this.dtoToEntity( dto );
        final E savedEntity = saveEntity( entity );
        final D returnDTO = this.entityToDTO( savedEntity );
        logMethodEnd( methodName, returnDTO );
        return returnDTO;
    }

    /**
     * Saves the entity to the database.
     * @param entity
     * @return
     * @throws EntityVersionMismatchException
     */
    public E saveEntity( final E entity )
        throws EntityVersionMismatchException
    {
        final String methodName = "saveEntity";
        logMethodBegin( methodName, entity );
        this.checkEntityVersion( entity );
        final E savedEntity = this.getRepository()
                                  .save( entity );
        logMethodEnd( methodName, entity );
        return savedEntity;
    }

    /**
     * Compares the entity.version to the version in the database and if the version are not the same, then
     * {@code EntityVersionMismatchException} is thrown. If the entity doesn't exist, its version is set to 1.
     * @param entity
     * @return
     * @throws EntityVersionMismatchException if the database entity (if found) doesn't match the {@code entity}
     *         argument's version.
     */
    public void checkEntityVersion( final VersionedEntity<K> entity )
        throws EntityVersionMismatchException
    {
        E dbEntity = null;
        /*
         * If there is an ID then there is a database version of this entity.
         */
        if ( entity.getId() != null )
        {
            dbEntity = this.getRepository()
                           .findOne( entity.getId() );
        }
        if ( dbEntity == null )
        {
            entity.setVersion( 1 );
        }
        else
        {
            if ( entity.getVersion() == null )
            {
                entity.setVersion( 1 );
            }
            if ( entity.getVersion() != dbEntity.getVersion() )
            {
                throw new EntityVersionMismatchException( dbEntity.getVersion(),
                                                          String.format( "Entity version mismatch(%d <> %d)",
                                                                         dbEntity.getVersion(), entity.getVersion() ) );
            }
        }
    }
}
