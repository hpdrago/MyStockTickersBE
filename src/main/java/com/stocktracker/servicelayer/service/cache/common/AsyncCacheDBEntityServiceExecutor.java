package com.stocktracker.servicelayer.service.cache.common;

import com.stocktracker.common.exceptions.DuplicateEntityException;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.common.VersionedEntity;
import com.stocktracker.servicelayer.service.VersionedEntityService;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * This is an extension of the async service executor to be used for the cases where we save the retrieved information
 * into the database.
 *
 * @param <K> - The key type to the cache -- this is key used to query the information from the third party.
 * @param <E> - The versioned entity type.
 * @param <S> - The entity service.
 * @param <D> - The type of information to obtain from the third party.
 * @param <EX>- The exception type used when the requested data is not found.
 * @param <RQ>- The request type.
 * @param <RS>- The response type.
 */
public abstract class AsyncCacheDBEntityServiceExecutor<K extends Serializable,
                                                        E extends VersionedEntity<K>,
                                                        S extends VersionedEntityService<K,E,?,?,?>,
                                                        D,
                                                        EX extends AsyncCacheDataNotFoundException,
                                                        RQ extends AsyncBatchCacheRequest<K,E>,
                                                        RS extends AsyncBatchCacheResponse<K,E>>
    extends BaseAsyncCacheBatchServiceExecutor<K,E,RQ,RS>
{
    /**
     * Fetches the information for {@code searchKey} from the external data source.
     * Also, converts the data retrieved from the external/third party source into the target database entity instance
     * and saves the information to the database.
     * @param searchKey The search key.
     * @return
     */
    @Override
    public E synchronousFetch( final K searchKey )
        throws AsyncCacheDataNotFoundException
    {
        final String methodName = "synchronousFetch";
        logMethodBegin( methodName, searchKey );
        D externalData = null;
        try
        {
            externalData = this.getExternalData( searchKey );
        }
        catch( AsyncCacheDataNotFoundException e )
        {
            throw e;
        }
        catch( Exception e )
        {
            logError( methodName, "searchKey not found: " + searchKey );
            throw this.createException( searchKey, e );
        }
        boolean mismatch = false;
        E entity = null;
        do
        {
            mismatch = false;
            try
            {
                entity = this.getEntityService()
                             .getEntity( searchKey );
                this.copyExternalDataToEntity( externalData, entity );
            }
            catch( EntityVersionMismatchException e1 )
            {
                logDebug( methodName, "Entity version mismatch, trying again.  {0}", entity );
                mismatch = true;
            }
            catch( VersionedEntityNotFoundException e )
            {
                /*
                 * If it doesn't exist then add it.
                 */
                try
                {
                    entity = this.createEntity();
                    this.copyExternalDataToEntity( externalData, entity );
                    entity = this.getEntityService()
                                 .addEntity( entity );
                }
                catch( EntityVersionMismatchException e1 )
                {
                    mismatch = true;
                    logDebug( methodName, "Entity version mismatch, trying again.  {0}", entity );
                }
                catch( DuplicateEntityException e1 )
                {
                    logError( methodName, "Should not get a duplicate after checking for existence: " +
                                          entity, e );
                }
            }
        }
        while ( mismatch );
        return entity;
    }

    /**
     * A new entity is needed when adding an entity to the database from the external data that was retrieved.
     * @return
     */
    protected abstract E createEntity();

    /**
     * This method is called after retrieving the external data.  By default, it simply calls {@code BeanUtils.copyProperties}.
     * @param externalData
     * @param entity
     */
    protected void copyExternalDataToEntity( final D externalData, final E entity )
    {
        BeanUtils.copyProperties( externalData, entity );
    }

    /**
     * Get the external data for {@code searchKey}
     * @param searchKey
     * @return
     */
    protected abstract D getExternalData( final K searchKey )
        throws AsyncCacheDataNotFoundException;

    /**
     * Get the instance of the entity service.
     * @return
     */
    protected abstract S getEntityService();

    /**
     * Subclasses override to provide a contextual exception.
     * @param key
     * @param cause
     * @return
     */
    protected abstract EX createException( final K key, final Exception cause );
}
