package com.stocktracker.servicelayer.service;

import com.stocktracker.common.exceptions.DuplicateEntityException;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.entity.StockCompanyEntity;
import com.stocktracker.repositorylayer.repository.StockCompanyRepository;
import com.stocktracker.servicelayer.service.cache.stockcompany.StockCompanyEntityCacheClient;
import com.stocktracker.servicelayer.service.cache.stockcompany.StockCompanyEntityCacheDataReceiver;
import com.stocktracker.weblayer.dto.StockCompanyDTO;
import com.stocktracker.weblayer.dto.common.StockCompanyDTOContainer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This is the service class for the stock company table entity.
 * The stock company table contains all know stocks that are currently in use and those that have been discontinued.
 */
@Service
public class StockCompanyEntityService extends VersionedEntityService<String,
                                                                      StockCompanyEntity,
                                                                      String,
                                                                      StockCompanyDTO,
                                                                      StockCompanyRepository>
{
    @Autowired
    private StockCompanyRepository stockCompanyRepository;
    @Autowired
    private StockCompanyEntityCacheClient stockCompanyEntityCacheClient;

    /**
     * Set the stock company information for each container.
     * @param containers
     */
    public void setCompanyInformation( final List<StockCompanyDTOContainer> containers )
    {
        final String methodName = "setStockCompanyInformation";
        logMethodBegin( methodName, "containers.size=" + containers.size() );
        final List<StockCompanyEntityCacheDataReceiver > receivers = new ArrayList<>();
        for ( final StockCompanyDTOContainer container: containers )
        {
            final StockCompanyEntityCacheDataReceiver receiver = this.context
                                                                     .getBean( StockCompanyEntityCacheDataReceiver.class );
            receiver.setCacheKey( container.getTickerSymbol() );
            receivers.add( receiver );
        }
        this.stockCompanyEntityCacheClient
            .getCachedData( receivers );
        /*
         * Mark the DTO's as quote is requested.
         */
        //containers.forEach( container -> container.setStockCompanyRequested( true ) );
        logMethodBegin( methodName );
    }

    /**
     * Updates the stock company information in {@code container}. It works with the {@code StockCompanyCache} to
     * retrieve the stock company asynchronously if needed.
     * @param container The container to set the value.
     */
    public void setCompanyInformation( final StockCompanyDTOContainer container )
    {
        final String methodName = "setStockCompanyInformation";
        logMethodBegin( methodName, container.getTickerSymbol() );
        /*
         * Create the cached data receiver.
         */
        final StockCompanyEntityCacheDataReceiver receiver = this.context.getBean( StockCompanyEntityCacheDataReceiver.class );
        receiver.setCacheKey( container.getTickerSymbol() );
        /*
         * Get the cached data or make an asynchronous request for the data.
         */
        this.stockCompanyEntityCacheClient
            .getCachedData( receiver );
        /*
         * Set the cache data and status results.
         */
        if ( receiver.getCachedData() != null )
        {
            final StockCompanyDTO stockCompanyDTO = this.entityToDTO( receiver.getCachedData() );
            container.setStockCompanyDTO( stockCompanyDTO );
        }
        container.setStockCompanyCacheEntryState( receiver.getCacheState() );
        container.setStockCompanyCacheError( receiver.getCacheError() );
        logMethodEnd( methodName, container );
    }

    /**
     * Checks the stock company cache to see if the stock company is being fetched and if so, will block and wait for
     * the fetch to complete.  If the company is in the cache but not being fetch, the cached version will be returned.
     * Otherwise, database entity will be loaded.
     * @param tickerSymbol
     * @return Stock Company DTO
     */
    public StockCompanyDTO getStockCompanyDTO( final String tickerSymbol )
    {
        final String methodName = "getStockCompanyDTO";
        logMethodBegin( methodName, tickerSymbol );
        /*
         * Create the cached data receiver.
         */
        final StockCompanyEntityCacheDataReceiver receiver = this.context.getBean( StockCompanyEntityCacheDataReceiver.class );
        receiver.setCacheKey( tickerSymbol );
        this.stockCompanyEntityCacheClient
            .getCachedData( tickerSymbol, receiver );
        /*
         * This maybe the first time the stock company is being fetched so we need to check to see if it is being
         * fetch and then wait for the result.
         */
        if ( receiver.getCacheState().isStale() )
        {
            logDebug( methodName, "Waiting for asynchronous fetch to complete for {0}", tickerSymbol );
            this.stockCompanyEntityCacheClient
                .getCachedData( tickerSymbol, receiver );
        }
        StockCompanyDTO stockCompanyDTO;
        if ( receiver.getCachedData() == null )
        {
            stockCompanyDTO = this.context.getBean( StockCompanyDTO.class );
            stockCompanyDTO.setTickerSymbol( tickerSymbol );
        }
        else
        {
            stockCompanyDTO = this.entityToDTO( receiver.getCachedData() );
        }
        stockCompanyDTO.setCacheState( receiver.getCacheState() );
        stockCompanyDTO.setCacheError( receiver.getCacheError() );
        logMethodEnd( methodName, stockCompanyDTO );
        return stockCompanyDTO;
    }

    /**
     * Get a page of StockDomainEntities's
     * @param pageRequest
     * @return
     */
    public Page<StockCompanyDTO> getPage( final Pageable pageRequest, final boolean withStockPrices )
    {
        final String methodName = "getPage";
        logMethodBegin( methodName, pageRequest );
        Objects.requireNonNull( pageRequest, "pageRequest cannot be null" );
        /*
         * Get the page from the database
         */
        final Page<StockCompanyEntity> stockEntities = this.stockCompanyRepository
                                                           .findAll( pageRequest );
        /*
         * Map from Entity to DomainEntity
         */
        final Page<StockCompanyDTO> stockDTOPage = this.entitiesToDTOs( pageRequest, stockEntities );
        logMethodEnd( methodName );
        return stockDTOPage;
    }

    /**
     * Get companies matching either the company name or ticker symbol strings
     * @param pageRequest
     * @param companiesLike
     * @return
     */
    public Page<StockCompanyDTO> getCompaniesLike( final Pageable pageRequest, final String companiesLike )
    {
        final String methodName = "getCompaniesLike";
        logMethodBegin( methodName, pageRequest, companiesLike );
        Objects.requireNonNull( pageRequest, "pageRequest cannot be null" );
        Objects.requireNonNull( companiesLike, "companiesLike cannot be null" );
        /*
         * Get the page from the database
         */
        final Page<StockCompanyEntity> stockEntities = this.stockCompanyRepository
                                                           .findByCompanyNameIsLikeOrTickerSymbolIsLike(
            pageRequest, companiesLike + "%", companiesLike + "%" );
        /*
         * Map from Entity to DomainEntity
         */
        final Page<StockCompanyDTO> stockDTOPage = this.entitiesToDTOs( pageRequest, stockEntities );
        logMethodEnd( methodName );
        return stockDTOPage;
    }

    /**
     * This method will validate that the ticker symbol exists in the database.  If it doesn't, it will
     * attempt to get a stock quote and create the database entry.
     * @param tickerSymbol
     * @return
     */
    public StockCompanyEntity checkStockCompanyTableEntry( final String tickerSymbol )
    {
        final String methodName = "checkStock";
        logMethodBegin( methodName, tickerSymbol );
        StockCompanyEntity stockCompanyEntity = null;
        try
        {
            stockCompanyEntity = this.getEntity( tickerSymbol );
        }
        catch( StockNotFoundException e )
        {
            /*
             * Add a discontinued company
             */
            stockCompanyEntity = this.context.getBean( StockCompanyEntity.class );
            stockCompanyEntity.setTickerSymbol( tickerSymbol );
            stockCompanyEntity.setDiscontinuedInd( true );
            try
            {
                stockCompanyEntity = this.addEntity( stockCompanyEntity );
            }
            catch( EntityVersionMismatchException | DuplicateEntityException | VersionedEntityNotFoundException e1 )
            {
                // ignore, it means another thread already added the company.
            }
        }
        catch( VersionedEntityNotFoundException e )
        {
            logError( methodName, e );
        }
        logMethodEnd( methodName, stockCompanyEntity );
        return stockCompanyEntity;
    }

    /**
     * Sets the discontinued flag on the stock table for {@code tickerSymbol}
     * @param tickerSymbol
     */
    public void markStockAsDiscontinued( final String tickerSymbol )
    {
        final String methodName = "markStockAsDiscontinued";
        logMethodBegin( methodName, tickerSymbol );
        StockCompanyEntity stockCompanyEntity;
        try
        {
            stockCompanyEntity = this.getEntity( StringUtils.truncate( tickerSymbol, StockCompanyEntity.TICKER_SYMBOL_LEN ) );
            stockCompanyEntity.setDiscontinuedInd( true );
            try
            {
                stockCompanyEntity = this.saveEntity( stockCompanyEntity );
            }
            catch( EntityVersionMismatchException e )
            {
                logError( methodName, "Failed version check but saving anyway " + stockCompanyEntity, e );
                stockCompanyEntity.setVersion( e.getCurrentVersion() );
                this.stockCompanyRepository
                    .save( stockCompanyEntity );
            }
            catch( DuplicateEntityException e )
            {
                // ignore
            }
        }
        catch( VersionedEntityNotFoundException e )
        {
            logError( methodName, "Failed to retrieve a stock company but it should exists or we wouldn't" +
                                  "be marking it as discontinued." );
        }
        logMethodEnd( methodName );
    }

    @Override
    protected StockCompanyDTO createDTO()
    {
        return this.context.getBean( StockCompanyDTO.class );
    }

    @Override
    protected StockCompanyEntity createEntity()
    {
        return this.context.getBean( StockCompanyEntity.class );
    }

    @Override
    protected StockCompanyRepository getRepository()
    {
        return this.stockCompanyRepository;
    }
}
