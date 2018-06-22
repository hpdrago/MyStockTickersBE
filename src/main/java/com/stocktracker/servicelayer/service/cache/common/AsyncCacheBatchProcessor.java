package com.stocktracker.servicelayer.service.cache.common;

import com.stocktracker.servicelayer.service.BaseService;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class provides the ability to process a batch of containers (<C>) to obtain the async cache data for each container
 * and then update the containers with the DTO information for the cache data.
 * @param <K>  - Key type.
 * @param <T>  - Cache data type.
 * @param <CE> - Cache entry type.
 * @param <DR> - Data receiver type.
 * @param <RQ> - Asynchronous request data type.
 * @param <RS> - Asynchronous response data type.
 * @param <CDC> - Container type.
 * @param <DC> - DTO container type.
 * @param <X>  - Async executor type.
 * @param <C>  - Cache type.
 * @param <CL> - Batch client type.
 */
public abstract class AsyncCacheBatchProcessor<  K extends Serializable,
                                                 T extends AsyncCacheData,
                                                CE extends AsyncCacheEntry<K,T>,
                                                DR extends AsyncCacheDataReceiver<K,T>,
                                                RQ extends AsyncBatchCacheRequest<K,T>,
                                                RS extends AsyncBatchCacheResponse<K,T>,
                                               CDC extends AsyncCachedDataContainer<K,T>,
                                                DC extends AsyncCacheDTOContainer<K,DC>,
                                                 X extends AsyncCacheBatchServiceExecutor<K,T,RQ,RS>,
                                                 C extends AsyncBatchCache<K,T,CE,RQ,RS,X>,
                                                CL extends AsyncCacheBatchClient<K,T,CE,DR,RQ,RS,X,C>>
    extends BaseService
{
    /**
     * Populates the {@code dtoContainers} with the cached data.
     * @param dtoContainers
     */
    public void getCachedData( final List<? extends DC> dtoContainers )
    {
        /*
         * Create the cached data containers.
         */
        final List<? extends CDC> cachedDataContainers = this.createContainers( dtoContainers );
        /*
         * Make the batch request, the cachedDataContainers contain the request key and will be populated with the
         * cached information.
         */
        this.processBatch( cachedDataContainers );
        /*
         * The cachedDataContainers have been updated with the cache information including if the data is currently
         * being fetch asynchronously.  We need to copy that data and cache state information to the DTOs in the
         * dtoContainers.
         */
        this.updateDTOContainers( dtoContainers, cachedDataContainers );
    }

    /**
     * Copies the information from the cachedDataContainers, which contains the cache information and state, to the
     * source DTO containers.
     * @param dtoContainers
     * @param cachedDataContainers
     */
    protected void updateDTOContainers( final List<? extends DC> dtoContainers, final List<? extends CDC> cachedDataContainers )
    {
        for ( int i = 0; i < cachedDataContainers.size(); i++ )
        {
            CDC cachedDataContainer = cachedDataContainers.get( i );
            DC dtoContainer = dtoContainers.get( i );
            if ( !cachedDataContainer.getCacheKey().equals( dtoContainer.getCacheKey() ))
            {
                throw new IllegalArgumentException( String.format( "dto/cached data key mismatch error.  DTO: %s CachedData %s",
                                                                   dtoContainer.getCacheKey(), cachedDataContainer.getCacheKey() ));
            }
            this.setDTOContainer( cachedDataContainer, dtoContainer );
        }
    }

    /**
     * Subclasses must override this method to take the cached data from the {@code cachedDataContainer} and copy it
     * and the cache results to the {@code dtoContainer}.
     * @param cachedDataContainer
     * @param dtoContainer
     */
    protected abstract void setDTOContainer( final CDC cachedDataContainer, final DC dtoContainer );

    /**
     * Creates a matching list of containers from the DTOs.  The containers will contain all of the cache
     * information which can then be extracted and copied to the dtoContainers.
     * @param dtoContainers
     * @return
     */
    protected List<? extends CDC> createContainers( final List<? extends DC> dtoContainers )
    {
        final List<? extends CDC> containers = dtoContainers.stream()
                                                            .map( (DC dtoContainer) ->
                                                            {
                                                                 final CDC container = this.newContainer();
                                                                 container.setCacheKey( dtoContainer.getCacheKey() );
                                                                 return container;
                                                            })
                                                            .collect(Collectors.toList());
        return containers;
    }

    /**
     * Gets the cache information and state for all of the DTOs in {@code containers}.  The information is requested
     * as a batch and not individually.
     * @param containers
     */
    protected void processBatch( final List<? extends CDC> containers )
    {
        final String methodName = "processBatch";
        logMethodBegin( methodName, containers.size() );
        final List<DR> receivers = containers.stream()
                                             .map( (CDC container) ->
                                                   {
                                                       final DR receiver = this.newReceiver();
                                                       receiver.setCacheKey( this.getCacheKey( container ));
                                                       return receiver;
                                                   })
                                             .collect(Collectors.toList());
        /*
         * Make the batch data request and update the receivers with the status of the stock quote.
         */
        this.getAsyncCacheClient()
            .getCachedData( receivers );
        /*
         *  It is safe to assume that the containers list and the receivers list are the same size and have the same
         *  ticker symbol but we'll check to make sure.
         */
        if ( containers.size() != receivers.size() )
        {
            throw new IllegalStateException( String.format( "receivers size (%d) does not match containers size (%d)",
                                                            receivers.size(), containers.size() ));
        }
        /*
         *  Need to extract the receiver's cache information and update the containers with that information.
         */
        this.updateContainers( containers, receivers );
        logMethodEnd( methodName );
    }

    /**
     * Extracts the information from the receivers, which were updated by the cache client, and copy the status and
     * cache data to the containers that initiated this batch process.
     * @param containers
     * @param receivers
     */
    private void updateContainers( final List<? extends CDC> containers, final List<DR> receivers )
    {
        for ( int i = 0; i < containers.size(); i++ )
        {
            final CDC container = containers.get( i );
            final DR receiver = receivers.get( i );
            if ( !this.getCacheKey( container ).equals( receiver.getCacheKey() ))
            {
                throw new IllegalStateException( String.format( "Container dtoId (%s) does not match receiver cache key (%s)",
                                                                this.getCacheKey( container ), receiver.getCacheKey() ));
            }
            container.setCachedData( receiver.getCachedData() );
            container.setCachedDataState( receiver.getCachedDataState() );
            container.setExpirationTime( receiver.getExpirationTime() );
            container.setCacheError( receiver.getCacheError() );
        }
    }

    /**
     * Subclasses must return the cache key for the {@code container}.
     * @param container
     * @return
     */
    protected abstract K getCacheKey( final CDC container );

    /**
     * Subclasses must specify the receiver type.
     * @return
     */
    public abstract DR newReceiver();

    /**
     * Subclasses must specify the receiver type.
     * @return
     */
    protected abstract CDC newContainer();

    /**
     * Subclasses must specify the async cache client as it is used to process the batch.
     * @return
     */
    public abstract CL getAsyncCacheClient();
}
