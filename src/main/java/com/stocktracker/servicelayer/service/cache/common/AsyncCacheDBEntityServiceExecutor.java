package com.stocktracker.servicelayer.service.cache.common;

import com.stocktracker.common.exceptions.DuplicateEntityException;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.common.VersionedEntity;
import com.stocktracker.servicelayer.service.VersionedEntityService;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This is an extension of the async service executor to be used for the cases where we save the retrieved information
 * into the database.
 *
 * @param <CK> - The key type to the cache -- this is key used to query the information form the async source.
 * @param <ASK> - The async cache key -- the key used to retrieve the information form the async source which can
 *                be different than the cache key.
 * @param <CD> - The cache data type which is the database entity that will be stored.
 * @param <ASD> - The external/async data.  This is the data that we will retrieve from the external/async.
 * @param <S> - The entity service.
 * @param <RQ>- The request type.
 * @param <RS>- The response type.
 */
public abstract class AsyncCacheDBEntityServiceExecutor<CK extends Serializable,
                                                        CD extends VersionedEntity<CK>,
                                                        ASK,
                                                        ASD,
                                                        S extends VersionedEntityService<CK,CD,?,?,?>,
                                                        RK extends AsyncBatchCacheRequestKey<CK,ASK>,
                                                        RQ extends AsyncBatchCacheRequest<CK,CD,ASK>,
                                                        RS extends AsyncBatchCacheResponse<CK,ASK,ASD>>
    extends BaseAsyncCacheBatchServiceExecutor<CK,CD,ASK,ASD,RK,RQ,RS>
{
    /**
     * Converts the data retrieved form the asynchronous source into a DB Entity instance.
     * @param cacheKey
     * @param asyncKey
     * @param asyncData
     * @return
     */
    @Override
    protected CD convertASyncData( @NotNull final CK cacheKey,
                                   @NotNull final ASK asyncKey,
                                   @NotNull final ASD asyncData )
    {
        final String methodName = "convertASyncData";
        logMethodBegin( methodName, cacheKey, asyncKey, asyncData );
        /*
         * Need to get the existing entity
         */
        CD entity = null;
        try
        {
            entity = this.getEntityService()
                         .getEntity( cacheKey );
            this.copyASyncData( cacheKey, asyncData, entity );
        }
        catch( VersionedEntityNotFoundException e )
        {
            /*
             * Not found so, create it
             */
            entity = this.createEntity();
            this.copyASyncData( cacheKey, asyncData, entity );
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
                             .getEntity( cacheKey );
                this.copyASyncData( cacheKey, asyncData, entity );
                entity = this.getEntityService()
                             .saveEntity( entity );
            }
            catch( Exception ex )
            {
                logError( methodName, ex );
            }
        }
        return entity;
    }

    /**
     * Get the async data for a single key from the asynchronous data source.
     * This is the call to the actual data provider.
     * @param searchKey
     * @return
     * @throws AsyncCacheDataNotFoundException
     */
    /*
    public abstract ASD getASyncData( final ASK searchKey )
        throws AsyncCacheDataNotFoundException;
        */

    /**
     * Fetches the information for {@code asyncKey} immediately.
     * @param asyncKey The key to the asynchronous data.
     * @return Information of type ASD wrapped in an Optional.
     * @throws AsyncCacheDataNotFoundException When the requested async was not found in the asynchronous data source.
     * @throws AsyncCacheDataRequestException When an exception occurs, other than the not found exception.
     */
//    public ASD getASyncData( @NotNull final ASK asyncKey )
//        throws AsyncCacheDataNotFoundException,
//               AsyncCacheDataRequestException
//    {
//        final String methodName = "getASyncData";
//        logMethodBegin( methodName, cacheKey, asyncKey );
//        ASD asyncData = null;
//        try
//        {
//            asyncData = super.getASyncData( asyncKey );
//        }
//        catch( AsyncCacheDataNotFoundException e )
//        {
//            throw e;
//        }
//        catch( Exception e )
//        {
//            logError( methodName, "searchKey not found: " + asyncKey );
//            throw this.createException( asyncKey, e );
//        }
//        boolean mismatch = false;
//        CD entity = null;
//        do
//        {
//            mismatch = false;
//            try
//            {
//                entity = this.getEntityService()
//                             .getEntity( cacheKey );
//                this.copyASyncData( cacheKey, asyncData, entity );
//            }
//            catch( EntityVersionMismatchException e1 )
//            {
//                logDebug( methodName, "Entity version mismatch, trying again.  {0}", entity );
//                mismatch = true;
//            }
//            catch( VersionedEntityNotFoundException e )
//            {
//                /*
//                 * If it doesn't exist then add it.
//                 */
//                try
//                {
//                    entity = this.createEntity();
//                    this.copyASyncData( cacheKey, asyncData, entity );
//                    entity = this.getEntityService()
//                                 .addEntity( entity );
//                }
//                catch( EntityVersionMismatchException e1 )
//                {
//                    mismatch = true;
//                    logDebug( methodName, "Entity version mismatch, trying again.  {0}", entity );
//                }
//                catch( DuplicateEntityException e1 )
//                {
//                    logError( methodName, "Should not get a duplicate after checking for existence: " +
//                                          entity, e );
//                }
//                catch( VersionedEntityNotFoundException e1 )
//                {
//                    logError( methodName, "Should not get a not found after checking for existence: " +
//                                          entity, e );
//                }
//            }
//        }
//        while ( mismatch );
//        return entity;
//    }

    /**
     * Copies the async data to the entity which is the cached information.
     * @param cacheKey
     * @param asyncData
     * @param entity
     * @return
     */
    protected void copyASyncData( final CK cacheKey, final ASD asyncData, final CD entity )
    {
        if ( asyncData != null )
        {
            BeanUtils.copyProperties( asyncData, entity );
            entity.setId( cacheKey );
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
    protected AsyncCacheDataRequestException createException( final ASK key, final Exception cause )
    {
        return new AsyncCacheDataRequestException( key, cause );
    }
}
