package com.stocktracker.servicelayer.service.cache.common;

import com.stocktracker.servicelayer.service.BaseService;
import com.stocktracker.servicelayer.service.VersionedEntityService;
import com.stocktracker.weblayer.dto.common.AsyncCacheDTOContainer;
import com.stocktracker.weblayer.dto.common.StockQuoteDTOContainer;
import com.stocktracker.weblayer.dto.common.VersionedDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class provides the ability to process a batch of containers (<C>) to obtain the async cache data for each container
 * and then update the containers with the DTO information for the cache data.
 * @param <K>  - Key type.
 * @param <T>  - Cache data type.
 * @param <DR> - Data receiver type.
 * @param <D>  - DTO type.
 * @param <C>  - Container type.
 * @param <CL> - Batch client type.
 * @param <ES> - Entity service type.
 */
public abstract class AsyncCacheBatchProcessor< K extends Serializable,
                                                T extends AsyncCacheDBEntity<K>,
                                               DR extends AsyncCacheDataReceiver<K,T>,
                                                D extends VersionedDTO<K> & AsyncCacheDTOContainer<K,D>,
                                                C,
                                               CL extends AsyncCacheDBEntityBatchClient<K,T,?,?,?,?,?,DR,?>,
                                               ES extends VersionedEntityService<K,T,?,D,?>>
    extends BaseService
{
    /**
     * Gets the stock quote information for all of the DTOs in {@code containers}.  The quotes are fetched as a batch
     * using the stock quote client.
     * @param containers
     */
    public void processBatch( final List<? extends C> containers )
    {
        final String methodName = "processBatch";
        logMethodBegin( methodName, containers.size() );
        final List<DR> receivers = new ArrayList<>();
        /*
         * Create the request data.
         */
        for ( final C container: containers )
        {
            final DR receiver = this.newReceiver();
            receiver.setCacheKey( this.getCacheKey( container ));
            receivers.add( receiver );
        }
        /*
         * Make the batch data request and update the receivers with the status of the stock quote.
         */
        this.getAsyncCacheClient()
            .setCachedData( receivers );
        /*
         *  Need to extract the receiver's stock quote information and update the containers with that information.
         *  It is safe to assume that the containers list and the receivers list are the same size and have the same
         *  ticker symbol but we'll check to make sure.
         */
        if ( containers.size() != receivers.size() )
        {
            throw new IllegalStateException( String.format( "receivers size (%d) does not match containers size (%d)",
                                                            receivers.size(), containers.size() ));
        }
        for ( int i = 0; i < containers.size(); i++ )
        {
            final C container = containers.get( i );
            final DR receiver = receivers.get( i );
            if ( !this.getCacheKey( container ).equals( receiver.getCacheKey() ))
            {
                throw new IllegalStateException( String.format( "Container dtoId (%s) does not match receiver cache key (%s)",
                                                                this.getCacheKey( container ), receiver.getCacheKey() ));
            }
            //Objects.requireNonNull( receiver.getCachedData(), "CachedData is null for " + receiver.getCacheKey() );
            /*
             * Convert the entity to a dto, copy the cache status values, and set the container's dto value.
             */
            final D dto = this.getEntityService()
                              .entityToDTO( receiver.getCachedData() );
            dto.setCacheError( receiver.getCacheError() );
            dto.setExpirationTime( receiver.getExpirationTime() );
            dto.setCacheState( receiver.getCacheDataState() );
            this.setCachedDTO( dto, container );
        }
        logMethodEnd( methodName );
    }

    /**
     * Subclasses must handle the assignment of the DTO to the container.
     * @param dto
     * @param container
     */
    protected abstract void setCachedDTO( final D dto, final C container );

    /**
     * Subclasses must return the cache key for the {@code container}.
     * @param container
     * @return
     */
    protected abstract K getCacheKey( final C container );

    /**
     * Subclasses must specify the recevier type.
     * @return
     */
    public abstract DR newReceiver();

    /**
     * Subclasses must specify the async cache client as it is used to process the batch.
     * @return
     */
    public abstract CL getAsyncCacheClient();

    /**
     * The entity service must be defined to convert cached entity data into DTOs.
     * @return
     */
    public abstract ES getEntityService();
}
