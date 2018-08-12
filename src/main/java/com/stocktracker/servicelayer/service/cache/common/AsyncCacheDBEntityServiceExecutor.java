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
 * @param <CK> - The key type to the cache -- this is key used to query the information from the third party.
 * @param <TPK> - The third party cache key -- the key used to retrieve the information from the third party which can
 *                be different than the cache key.
 * @param <CD> - The cache data type which is the database entity that will be stored.
 * @param <TPD> - The external/third party data.  This is the data that we will retrieve from the external/third party.
 * @param <S> - The entity service.
 * @param <EX>- The exception type used when the requested data is not found.
 * @param <RQ>- The request type.
 * @param <RS>- The response type.
 */
public abstract class AsyncCacheDBEntityServiceExecutor<CK extends Serializable,
                                                        TPK,
                                                        CD extends VersionedEntity<CK>,
                                                        TPD,
                                                        S extends VersionedEntityService<CK,CD,?,?,?>,
                                                        EX extends AsyncCacheDataNotFoundException,
                                                        RK extends AsyncBatchCacheRequestKey<CK,TPK>,
                                                        RQ extends AsyncBatchCacheRequest<CK,CD,TPK,RK>,
                                                        RS extends AsyncBatchCacheResponse<CK,TPK,TPD,RK>>
    extends BaseAsyncCacheBatchServiceExecutor<CK,CD,TPK,TPD,RK,RQ,RS>
{
//    @Override
//    protected List<TPD> batchFetch( final List<RK> requestKeys )
//    {
//        final String methodName = "batchFetch";
//        logMethodBegin( methodName, requestKeys );
//        /*
//         * Get the third party data for all of the request keys.
//         */
//        final List<RS> responseList = this.getThirdPartyData( requestKeys );
//        final List<CD> returnCacheDataList = new ArrayList<>();
//        /*
//         * Update existing entities and insert new entities.
//         */
//        responseList.forEach( ( response )->
//                             {
//                                /*
//                                 * Need to get the existing entity
//                                 */
//                                CD entity = null;
//                                try
//                                {
//                                    entity = this.getEntityService()
//                                                 .getEntity( response.getCacheKey() );
//                                    this.copyThirdPartyData( response.getRequestKey(),
//                                                             response.getData(),
//                                                             entity );
//                                }
//                                catch( VersionedEntityNotFoundException e )
//                                {
//                                    /*
//                                     * Not found so, create it
//                                     */
//                                    entity = this.createEntity();
//                                    this.copyThirdPartyData( response.getRequestKey(),
//                                                             response.getData(),
//                                                             entity );
//                                }
//                                try
//                                {
//                                    entity = this.getEntityService()
//                                                 .saveEntity( entity );
//                                }
//                                catch( DuplicateEntityException | EntityVersionMismatchException e )
//                                {
//                                    logDebug( methodName, "DuplicateEntityException encountered saving {0}",
//                                              entity );
//                                    /*
//                                     * Retrieve and resave information.
//                                     */
//                                    try
//                                    {
//                                        entity = this.getEntityService()
//                                                     .getEntity( response.getRequestKey()
//                                                                         .getCacheKey() );
//                                        this.copyThirdPartyData( response.getRequestKey(),
//                                                                 response.getData(),
//                                                                 entity );
//                                        entity = this.getEntityService()
//                                                     .saveEntity( entity );
//                                    }
//                                    catch( Exception ex )
//                                    {
//                                        logError( methodName, ex );
//                                    }
//                                }
//                                returnCacheDataList.add( entity );
//                            });
//        logDebug( methodName, "cached items: {0}", returnCacheDataList );
//        logMethodEnd( methodName, returnCacheDataList.size() + " cached items" );
//        return returnCacheDataList;
//    }

    /**
     * Retrieves a batch of third party data objects for the {@code requestKeys}.
     * By default, a request is made for each request key in {@code requestKeys}.
     * For those third parties that provide batch request services, this method should be overridden to work within the
     * batch request parameters of the third party.
     * @param requestKeys
     * @return
     */
    protected List<RS> getThirdPartyData( final List<RK> requestKeys )
    {
        final String methodName = "getThirdPartyData";
        logMethodBegin( methodName, requestKeys );
        final List<RS> resultList = new ArrayList<>( requestKeys.size() );
        for ( final RK requestKey : requestKeys )
        {
            try
            {
                final TPD thirdPartyData = this.getThirdPartyData( requestKey.getThirdPartyKey() );
                resultList.add( this.createResponse( requestKey, thirdPartyData ));
            }
            catch( AsyncCacheDataNotFoundException e )
            {
                logError( methodName, e );
            }
        }
        logMethodEnd( methodName, "Received " + resultList.size() + " responses" );
        return resultList;
    }

    /**
     * Subclasses must override this method to create a new response type that includes the request key and the
     * third party data retrieved.
     * @param requestKey
     * @param thirdPartyData
     * @return
     */
    protected abstract RS createResponse( final RK requestKey, final TPD thirdPartyData );

    /**
     * Get the third party data for a single key.
     * @param searchKey
     * @return
     * @throws AsyncCacheDataNotFoundException
     */
    protected abstract TPD getThirdPartyData( final TPK searchKey )
        throws AsyncCacheDataNotFoundException;

    /**
     * Fetches the information for {@code searchKey} from the external data source.
     * Also, converts the data retrieved from the external/third party source into the target database entity instance
     * and saves the information to the database.
     * @param requestKey The key to the cache.
     * @param thirdPartyKey The search key.
     * @return
     */
    @Override
    public TPD getThirdPartyData( final RK requestKey,
                                  final TPK thirdPartyKey )
        throws AsyncCacheDataNotFoundException
    {
        final String methodName = "getThirdPartyData";
        logMethodBegin( methodName, requestKey, thirdPartyKey );
        TPD externalData = null;
        try
        {
            externalData = this.getThirdPartyData( thirdPartyKey );
        }
        catch( AsyncCacheDataNotFoundException e )
        {
            throw e;
        }
        catch( Exception e )
        {
            logError( methodName, "searchKey not found: " + thirdPartyKey );
            throw this.createException( thirdPartyKey, e );
        }
        boolean mismatch = false;
        CD entity = null;
        do
        {
            mismatch = false;
            try
            {
                entity = this.getEntityService()
                             .getEntity( requestKey.getCacheKey() );
                this.copyThirdPartyData( requestKey, externalData, entity );
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
                    this.copyThirdPartyData( requestKey, externalData, entity );
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
    protected void copyThirdPartyData( final RK requestKey, final TPD thirdPartyData, final CD entity )
    {
        if ( thirdPartyData != null )
        {
            BeanUtils.copyProperties( thirdPartyData, entity );
            entity.setId( requestKey.getCacheKey() );
        }
    }

    /**
     * A new entity is needed when adding an entity to the database from the external data that was retrieved.
     * @return
     */
    protected abstract CD createEntity();

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
    protected abstract EX createException( final TPK key, final Exception cause );
}
