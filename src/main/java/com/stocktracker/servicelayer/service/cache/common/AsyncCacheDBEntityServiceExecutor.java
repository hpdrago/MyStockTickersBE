package com.stocktracker.servicelayer.service.cache.common;

import com.stocktracker.common.exceptions.DuplicateEntityException;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.common.VersionedEntity;
import com.stocktracker.servicelayer.service.VersionedEntityService;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This is an extension of the async service executor to be used for the cases where we save the retrieved information
 * into the database.
 *
 * @param <K> - The key type to the cache -- this is key used to query the information from the third party.
 * @param <T> - The cache data type which is the database entity that will be stored.
 * @param <D> - The external/third party data.  This is the data that we will retrieve from the external/third party.
 * @param <S> - The entity service.
 * @param <EX>- The exception type used when the requested data is not found.
 * @param <RQ>- The request type.
 * @param <RS>- The response type.
 */
public abstract class AsyncCacheDBEntityServiceExecutor<K extends Serializable,
                                                        T extends VersionedEntity<K>,
                                                        D,
                                                        S extends VersionedEntityService<K,T,?,?,?>,
                                                        EX extends AsyncCacheDataNotFoundException,
                                                        RQ extends AsyncBatchCacheRequest<K,T>,
                                                        RS extends AsyncBatchCacheResponse<K,T>>
    extends BaseAsyncCacheBatchServiceExecutor<K,T,RQ,RS>
{
    @Override
    protected List<T> getExternalData( final List<K> requestKeys )
    {
        final String methodName = "getExternalData";
        logMethodBegin( methodName, requestKeys );
        /*
         * Get the quotes for the ticker symbols.
         */
        final List<D> thirdPartyDataList = this.getThirdPartyData( requestKeys );
        final List<T> returnCacheDataList = new ArrayList<>();
        /*
         * Update existing companies and insert new companies.
         */
        thirdPartyDataList.forEach( ( D thirdPartyData ) ->
                                    {
                                        /*
                                         * Need to get the existing entity
                                         */
                                        T entity = null;
                                        try
                                        {
                                            entity = this.getEntityService()
                                                         .getEntity( this.getCacheKeyFromThirdPartyData( thirdPartyData ));
                                            this.copyThirdPartyData( thirdPartyData, entity );
                                        }
                                        catch( VersionedEntityNotFoundException e )
                                        {
                                            /*
                                             * Not found so, create it
                                             */
                                            entity = this.createEntity();
                                            this.copyThirdPartyData( thirdPartyData, entity );
                                        }
                                        try
                                        {
                                            entity = this.getEntityService()
                                                         .saveEntity( entity );
                                        }
                                        catch( DuplicateEntityException | EntityVersionMismatchException e )
                                        {
                                            logDebug( methodName, "DuplicateEntityException encountered saving {0}",
                                                      entity );
                                            /*
                                             * Retrieve and resave information.
                                             */
                                            try
                                            {
                                                entity = this.getEntityService()
                                                             .getEntity( this.getCacheKeyFromThirdPartyData( thirdPartyData ));
                                                this.copyThirdPartyData( thirdPartyData, entity );
                                                entity = this.getEntityService()
                                                             .saveEntity( entity );
                                            }
                                            catch( Exception ex )
                                            {
                                                logError( methodName, ex );
                                            }
                                        }
                                        returnCacheDataList.add( entity );
                                    });
        logDebug( methodName, "cached items: {0}", returnCacheDataList );
        logMethodEnd( methodName, returnCacheDataList.size() + " cached items" );
        return returnCacheDataList;
    }

    /**
     * Retrieves a batch of third party data objects for the {@code requestKeys}.
     * @param requestKeys
     * @return
     */
    protected abstract List<D> getThirdPartyData( final List<K> requestKeys );

    /**
     * Get the third party data for a single key.
     * @param searchKey
     * @return
     * @throws AsyncCacheDataNotFoundException
     */
    protected abstract D getThirdPartyData( final K searchKey )
        throws AsyncCacheDataNotFoundException;

    /**
     * Fetches the information for {@code searchKey} from the external data source.
     * Also, converts the data retrieved from the external/third party source into the target database entity instance
     * and saves the information to the database.
     * @param searchKey The search key.
     * @return
     */
    @Override
    public T getExternalData( final K searchKey )
        throws AsyncCacheDataNotFoundException
    {
        final String methodName = "getExternalData";
        logMethodBegin( methodName, searchKey );
        D externalData = null;
        try
        {
            externalData = this.getThirdPartyData( searchKey );
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
        T entity = null;
        do
        {
            mismatch = false;
            try
            {
                entity = this.getEntityService()
                             .getEntity( searchKey );
                this.copyThirdPartyData( externalData, entity );
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
                    this.copyThirdPartyData( externalData, entity );
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
                catch( VersionedEntityNotFoundException e1 )
                {
                    logError( methodName, "Should not get a not found after checking for existence: " +
                                          entity, e );
                }
            }
        }
        while ( mismatch );
        return entity;
    }

    /**
     * Copies the third party data to the entity which is the cached information.
     * @param thirdPartyData
     * @param entity
     * @return
     */
    protected void copyThirdPartyData( final D thirdPartyData, final T entity )
    {
        if ( thirdPartyData != null )
        {
            BeanUtils.copyProperties( thirdPartyData, entity );
            entity.setId( this.getCacheKeyFromThirdPartyData( thirdPartyData ) );
        }
    }

    /**
     * Extracts the cache key from the third party data.
     * @param thirdPartyData
     * @return
     */
    protected abstract K getCacheKeyFromThirdPartyData( final D thirdPartyData );

    /**
     * A new entity is needed when adding an entity to the database from the external data that was retrieved.
     * @return
     */
    protected abstract T createEntity();

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
