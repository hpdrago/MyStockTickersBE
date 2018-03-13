package com.stocktracker.servicelayer.service;

import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.entity.VersionedEntity;
import com.stocktracker.weblayer.dto.VersionedDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.Collection;

/**
 * Base Service class for managing database entities.
 * @param <ID> The primary key type.
 * @param <E> The database entity type.
 * @param <D> The DTO type.
 * @param <R> The entity repository type.
 */
public abstract class VersionedEntityService<ID extends Serializable,
                                             E extends VersionedEntity<ID>,
                                             D extends VersionedDTO<ID>,
                                             R extends JpaRepository<E, ID>>
    extends BaseEntityService<ID,E,D,R>
{
    /**
     * Get the DTO for the entity matching key.
     * @param key
     * @return
     * @throws VersionedEntityNotFoundException
     */
    public D getDTO( final int customerId, final ID key )
        throws VersionedEntityNotFoundException
    {
        final String methodName = "getEntity";
        logMethodBegin( methodName, customerId, key );
        final E entity = getEntity( key );
        if ( entity == null )
        {
            throw new VersionedEntityNotFoundException( key );
        }
        final D dto = this.entityToDTO( entity );
        logMethodEnd( methodName, dto );
        return dto;
    }

    /**
     * Get a single entity.
     * @param id The primary key.
     * @return The entity.
     * @throws VersionedEntityNotFoundException If the entity is not found by id
     */
    public E getEntity( final ID id )
        throws VersionedEntityNotFoundException
    {
        final String methodName = "getEntity";
        logMethodBegin( methodName, id );
        final E entity = this.getRepository()
                             .findOne( id );
        if ( entity == null )
        {
            throw new VersionedEntityNotFoundException( id );
        }
        logMethodEnd( methodName, entity );
        return entity;
    }

    /**
     * Deletes a collection of entities.
     * @param deletedEntities
     * @throws VersionedEntityNotFoundException
     */
    public void deleteEntities( final Collection<E> deletedEntities )
        throws VersionedEntityNotFoundException
    {
        final String methodName = "deleteEntities";
        logMethodBegin( methodName, deletedEntities.size() + " entities" );
        for ( final E entity: deletedEntities )
        {
            logDebug( methodName, "Deleting entity (0}", entity );
            this.deleteEntity( entity.getId() );
        }
        logMethodEnd( methodName );
    }

    /**
     * Delete the entity.
     * @param entity
     * @throws VersionedEntityNotFoundException
     */
    public void deleteEntity( final E entity )
        throws VersionedEntityNotFoundException
    {
        this.deleteEntity( entity.getId() );
    }

    /**
     * Delete the entity.
     * @param id
     * @throws VersionedEntityNotFoundException
     */
    public void deleteEntity( final ID id )
        throws VersionedEntityNotFoundException
    {
        final String methodName = "deleteEntity";
        logMethodBegin( methodName, id );
        if ( this.getRepository().exists( id ))
        {
            this.getRepository()
                .delete( id );
        }
        else
        {
            throw new VersionedEntityNotFoundException( id );
        }
        logMethodEnd( methodName );
    }

    /**
     * Adds all of the entities in {@code entities}
     * @param entities
     * @throws EntityVersionMismatchException
     */
    public void addEntities( final Collection<E> entities )
        throws EntityVersionMismatchException
    {
        final String methodName = "addEntities";
        logMethodBegin( methodName, "Adding {0} entities", entities.size() );
        for ( final E entity: entities )
        {
            logDebug( methodName, "Adding entity (0}", entity );
            this.addEntity( entity );
        }
        logMethodEnd( methodName );
    }

    /**
     * Add the entity to the database.
     * @param entity
     * @return
     * @throws EntityVersionMismatchException When the entity already exists.
     */
    public E addEntity( final E entity )
        throws EntityVersionMismatchException
    {
        final String methodName = "addEntity";
        logMethodBegin( methodName, entity );
        if ( this.getRepository().exists( entity.getId() ) )
        {
            final E currentEntity = this.getRepository()
                                        .findOne( entity.getId() );
            throw new EntityVersionMismatchException( currentEntity.getVersion(),
                                                      "Attempting to create a new entity that already exists: " + entity );
        }
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
        final E savedEntity = this.saveEntity( entity );
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
    public void checkEntityVersion( final VersionedEntity<ID> entity )
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
