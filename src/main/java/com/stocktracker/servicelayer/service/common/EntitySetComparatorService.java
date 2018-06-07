package com.stocktracker.servicelayer.service.common;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.SetComparator;
import com.stocktracker.common.exceptions.DuplicateEntityException;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.common.VersionedEntity;
import com.stocktracker.servicelayer.service.VersionedEntityService;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * This class compares two collections.  One collection is a collection of entities retrieved
 * from the database and the other entity collection is generated outside of the database.
 * This class will compare these two collections and determine new entities, entities to delete,
 * and entities to update.
 * @param <K> The entity key type.
 * @param <E> The entity type.
 */
public class EntitySetComparatorService<K extends Serializable,
                                        E extends VersionedEntity<K> & Comparable<E>>
    extends SetComparator<E>
    implements MyLogger
{
    /**
     * Compare the two collections and add, insert, and or delete the database entities based on the
     * comparison results. It is assumed that {@code collection2} is the "source of "truth" and
     * {@code collections2} is the database entities as they currently exist.
     * @param entityService The entity repository.
     * @param collection1 Collection of entities from non-database source.
     * @param collection2 Current entity values from the database.
     * @throws EntityVersionMismatchException if any of the entities to update is out of sync with the database version.
     * @throws VersionedEntityNotFoundException
     * @throws DuplicateEntityException
     */
    public void compareAndUpdateDatabase( final VersionedEntityService<K,E,?,?,?> entityService,
                                          final Collection<E> collection1,
                                          final Collection<E> collection2 )
        throws EntityVersionMismatchException,
               VersionedEntityNotFoundException,
               DuplicateEntityException
    {
        final String methodName = "compareAndUpdateDatabase";
        logMethodBegin( methodName );
        final SetComparatorResults comparatorResults = super.compareSets( collection1, collection2 );
        this.addEntities( entityService, comparatorResults.getNewItems() );
        this.deleteEntities( entityService, comparatorResults.getDeletedItems() );
        this.updateEntities( entityService, comparatorResults.getMatchingItems() );
        logMethodEnd( methodName );
    }

    /**
     * Updates the database for each entity in {@oode matchingEntities}
     * @param service
     * @param updateEntities List of entities to update.
     * @throws EntityVersionMismatchException if any of the entities to update is out of sync with the database version.
     * @throws DuplicateEntityException
     */
    private void updateEntities( final VersionedEntityService<K,E,?,?,?> service,
                                 final Set<E> updateEntities )
        throws EntityVersionMismatchException,
               DuplicateEntityException
    {
        final String methodName = "updateEntities";
        logMethodBegin( methodName );
        for ( final E entity: updateEntities )
        {
            logDebug( methodName, "Updating entity (0}", entity );
            service.saveEntity( entity );
        }
        logMethodEnd( methodName );
    }

    /**
     * Delete the entities in {@code deletedEntities}
     * @param service
     * @param deletedEntities The list of entities to delete.
     * @throws VersionedEntityNotFoundException
     */
    private void deleteEntities( final VersionedEntityService<K,E,?,?,?> service,
                                 final Set<E> deletedEntities )
        throws VersionedEntityNotFoundException
    {
        final String methodName = "deleteEntities";
        logMethodBegin( methodName );
        service.deleteEntities( deletedEntities );
        logMethodEnd( methodName );
    }

    /**
     * Adds all of the entities to the database.
     * @param service
     * @param newEntities
     * @throws EntityVersionMismatchException
     * @throws DuplicateEntityException
     */
    private void addEntities( final VersionedEntityService<K,E,?,?,?> service,
                              final Set<E> newEntities )
        throws EntityVersionMismatchException,
               DuplicateEntityException
    {
        final String methodName = "addEntities";
        logMethodBegin( methodName );
        service.addEntities( newEntities );
        logMethodEnd( methodName );
    }
}
