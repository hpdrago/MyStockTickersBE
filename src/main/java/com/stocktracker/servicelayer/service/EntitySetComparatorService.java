package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.SetComparator;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.repositorylayer.entity.VersionedEntity;

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
                                        E extends VersionedEntity<K>>
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
     */
    public void compareAndUpdateDatabase( final DMLEntityService<K,E,?,?> entityService,
                                          final Collection<E> collection1,
                                          final Collection<E> collection2 )
        throws EntityVersionMismatchException
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
     * @param repository
     * @param updateEntities List of entities to update.
     * @throws EntityVersionMismatchException if any of the entities to update is out of sync with the database version.
     */
    private void updateEntities( final DMLEntityService<K,E,?,?> repository,
                                 final Set<E> updateEntities )
        throws EntityVersionMismatchException
    {
        final String methodName = "updateEntities";
        logMethodBegin( methodName );
        for ( final E entity: updateEntities )
        {
            logDebug( methodName, "Updating entity (0}", entity );
            repository.saveEntity( entity );
        }
        logMethodEnd( methodName );
    }

    /**
     * Delete the entities in {@code deletedEntities}
     * @param repository
     * @param deletedEntities The list of entities to delete.
     */
    private void deleteEntities( final DMLEntityService<K,E,?,?> repository,
                                 final Set<E> deletedEntities )
    {
        final String methodName = "deleteEntities";
        logMethodBegin( methodName );
        for ( final E entity: deletedEntities )
        {
            logDebug( methodName, "Deleting entity (0}", entity );
            repository.deleteEntity( entity );
        }
        logMethodEnd( methodName );
    }

    /**
     * Adds all of the entities to the database.
     * @param repository
     * @param newEntities
     */
    private void addEntities( final DMLEntityService<K,E,?,?> repository,
                              final Set<E> newEntities )
    {
        final String methodName = "addEntities";
        logMethodBegin( methodName );
        for ( final E entity: newEntities )
        {
            logDebug( methodName, "Adding entity (0}", entity );
            repository.addEntity( entity );
        }
        logMethodEnd( methodName );
    }
}
