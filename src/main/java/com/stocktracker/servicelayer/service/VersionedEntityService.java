package com.stocktracker.servicelayer.service;

import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.VersionedEntity;
import com.stocktracker.weblayer.dto.VersionedDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

/**
 * Base Service class for managing database entities.
 * @param <EK> The entity key type.
 * @param <E> The database entity type.
 * @param <DK> The DTO key.
 * @param <D> The DTO type.
 * @param <R> The entity repository type.
 */
public abstract class VersionedEntityService<EK extends Serializable,
                                             E extends VersionedEntity<EK>,
                                             DK extends Serializable,
                                             D extends VersionedDTO<DK>,
                                             R extends JpaRepository<E, EK>>
    extends BaseEntityService<EK,E,DK,D,R>
{
    /**
     * Get the DTO for the entity matching key.
     * @param key
     * @return
     * @throws VersionedEntityNotFoundException
     */
    public D getDTO( final EK key )
        throws VersionedEntityNotFoundException
    {
        final String methodName = "getDTO";
        logMethodBegin( methodName, key );
        Objects.requireNonNull( key, "key cannot be null" );
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
     * @param EK The primary key.
     * @return The entity.
     * @throws VersionedEntityNotFoundException If the entity is not found by EK
     */
    public E getEntity( final EK EK )
        throws VersionedEntityNotFoundException
    {
        final String methodName = "getEntity";
        logMethodBegin( methodName, EK );
        Objects.requireNonNull( EK, "EK cannot be null" );
        final E entity = this.getRepository()
                             .findOne( EK );
        if ( entity == null )
        {
            throw new VersionedEntityNotFoundException( EK );
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
        Objects.requireNonNull( deletedEntities, "deletedEntities cannot be null" );
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
        Objects.requireNonNull( entity, "entity cannot be null" );
        this.deleteEntity( entity.getId() );
    }

    /**
     * Delete the entity.
     * @param EK
     * @throws VersionedEntityNotFoundException
     */
    public void deleteEntity( final EK EK )
        throws VersionedEntityNotFoundException
    {
        final String methodName = "deleteEntity";
        logMethodBegin( methodName, EK );
        Objects.requireNonNull( EK, "EK cannot be null" );
        if ( this.getRepository().exists( EK ))
        {
            this.getRepository()
                .delete( EK );
        }
        else
        {
            throw new VersionedEntityNotFoundException( EK );
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
        Objects.requireNonNull( entities, "entities cannot be null" );
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
        Objects.requireNonNull( entity, "entity cannot be null" );
        if ( isExists( entity ) )
        {
            final E currentEntity = this.getRepository()
                                        .findOne( entity.getId() );
            throw new EntityVersionMismatchException( currentEntity.getVersion(),
                                                      "Attempting to create a new entity that already exists: " + entity );
        }
        entity.setVersion( 1 );
        entity.setCreateDate( new Timestamp( System.currentTimeMillis() ));
        final E returnEntity = this.getRepository()
                                   .save( entity );
        logMethodEnd( methodName, returnEntity );
        return returnEntity;
    }

    /**
     * Inserts or updates the {@code entity} depending on if it exists already or not.
     * @param entity
     * @throws EntityVersionMismatchException
     */
    public void mergeEntity( final E entity )
        throws EntityVersionMismatchException
    {
        if ( entity.getId() == null || !this.isExists( entity ) )
        {
            this.addEntity( entity );
        }
        else
        {
            this.saveEntity( entity );
        }
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
        Objects.requireNonNull( entity, "entity cannot be null" );
        this.checkEntityVersion( entity );
        final E savedEntity = this.getRepository()
                                  .save( entity );
        logMethodEnd( methodName, entity );
        return savedEntity;
    }

    /**
     * Determined if the entity exists.
     * @param entity
     * @return
     */
    public boolean isExists( final E entity )
    {
        return this.isExists( entity.getId() );
    }

    /**
     * Determines if the key exists.
     * @param key
     * @return
     */
    public boolean isExists( final EK key )
    {
        return this.getRepository().exists( key );
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
        Objects.requireNonNull( dto, "dto cannot be null" );
        final E entity = this.dtoToEntity( dto );
        this.checkEntityVersion( entity );
        final E savedEntity = this.saveEntity( entity );
        final D returnDTO = this.entityToDTO( savedEntity );
        logMethodEnd( methodName, returnDTO );
        return returnDTO;
    }

    /**
     * Compares the entity.version to the version in the database and if the version are not the same, then
     * {@code EntityVersionMismatchException} is thrown. If the entity doesn't exist, its version is set to 1.
     * @param entity
     * @return
     * @throws EntityVersionMismatchException if the database entity (if found) doesn't match the {@code entity}
     *         argument's version.
     */
    public void checkEntityVersion( final VersionedEntity<EK> entity )
        throws EntityVersionMismatchException
    {
        final String methodName = "checkEntityVersion";
        logMethodBegin( methodName, entity );
        Objects.requireNonNull( entity, "entity cannot be null" );
        E dbEntity = null;
        /*
         * If there is an EK then there is a database version of this entity.
         */
        if ( entity.getId() != null )
        {
            dbEntity = this.getRepository()
                           .findOne( entity.getId() );
            logDebug( methodName, "dbEntity:{0}", dbEntity );
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
        logMethodEnd( methodName );
    }
}
