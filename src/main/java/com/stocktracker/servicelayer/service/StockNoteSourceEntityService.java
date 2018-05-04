package com.stocktracker.servicelayer.service;

import com.stocktracker.common.UUIDUtil;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.StockNoteSourceNotFoundException;
import com.stocktracker.repositorylayer.entity.StockNoteSourceEntity;
import com.stocktracker.repositorylayer.repository.StockNoteSourceRepository;
import com.stocktracker.weblayer.dto.StockNoteSourceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for the StockNoteSource entity interface to the database.
 */
@Service
//@Transactional
public class StockNoteSourceEntityService extends UuidEntityService<StockNoteSourceEntity,
                                                                    StockNoteSourceDTO,
                                                                    StockNoteSourceRepository>
{
    private StockNoteSourceRepository stockNoteSourceRepository;

    @Autowired
    public void setStockNoteSourceRepository( final StockNoteSourceRepository stockNoteSourceRepository )
    {
        this.stockNoteSourceRepository = stockNoteSourceRepository;
    }

    public interface StockNoteSourceEntityContainer
    {
        Optional<StockNoteSourceEntity> getNotesSourceEntity();
        Optional<UUID> getStockNoteSourceUuid();
        void setNotesSourceEntity( final StockNoteSourceEntity stockNoteSourceEntity );
    }

    /*
     * DTO's that contain stock notes source information must implement this interface in order for common
     * methods can be used to extract the necessary information to create the stock note source.
     */
    public interface StockNoteSourceDTOContainer
    {
        String getCustomerId();
        void setNotesSourceName( final String noteSourceName );
        void setNotesSourceId( final String notesSourceId );
        String getNotesSourceName();
        String getNotesSourceId();
    }

    /**
     * Sets the source name for all {@Code StockNoteSourceContainer} entries
     * @param customerUuid The customer id is used to retrieve the sources for the customer.
     * @param stockNoteSourceDTOContainers The list of {@code StockNoteSourceDTOContainer} instances.
     */
    public void setNotesSourceName( final UUID customerUuid,
                                    final List<? extends StockNoteSourceDTOContainer> stockNoteSourceDTOContainers )
    {
        Objects.requireNonNull( customerUuid, "customerUuid cannot be null" );
        Objects.requireNonNull( stockNoteSourceDTOContainers, "stockNoteSourceDTOContainers cannot be null" );
        /*
         * For now, maybe until we create a view -- if that makes sense, load the sources and populate the source
         * values in the DTOs
         */
        List<StockNoteSourceEntity> customerSources = this.stockNoteSourceRepository
            .findByCustomerUuidOrderByTimesUsedDesc( customerUuid );
        Map<UUID, String> sourceEntityMap = customerSources.stream()
                                                           .collect( Collectors.toMap( StockNoteSourceEntity::getUuid,
                                                                                       StockNoteSourceEntity::getName ) );
        for ( StockNoteSourceDTOContainer stockNoteSourceDTOContainer : stockNoteSourceDTOContainers )
        {
            if ( stockNoteSourceDTOContainer.getNotesSourceId() != null )
            {
                stockNoteSourceDTOContainer.setNotesSourceName(
                    sourceEntityMap.get( stockNoteSourceDTOContainer.getNotesSourceId() ) );
            }
        }
    }

    /**
     * Check {@code stockNoteSourceDTOContainer} to see if the user entered a new note source.
     * If a new source is detected, a new source will be added to the database for the customer.
     * @param stockNoteSourceDTOContainer This should be the DTO that contains the source id and the source name.
     * @throws EntityVersionMismatchException
     */
    public void checkForNewSource( final StockNoteSourceDTOContainer stockNoteSourceDTOContainer )
        throws EntityVersionMismatchException
    {
        final String methodName = "checkForNewSource";
        logDebug( methodName, "{0}", stockNoteSourceDTOContainer );
        /*
         * If the source id <> null and it's not a UUID, then it's a new value.
         */
        if ( stockNoteSourceDTOContainer.getNotesSourceId() != null &&
             !UUIDUtil.isUUID( stockNoteSourceDTOContainer.getNotesSourceId() ))
        {
            /*
             * Make sure it doesn't already exist
             */
            StockNoteSourceEntity stockNoteSourceEntity =
                this.stockNoteSourceRepository.findByCustomerUuidAndName( UUIDUtil.uuid( stockNoteSourceDTOContainer.getCustomerId()),
                                                                          stockNoteSourceDTOContainer.getNotesSourceName());
            logDebug( methodName, "stockNoteSourceEntity: {0}", stockNoteSourceEntity );
            if ( stockNoteSourceEntity != null )
            {
                logDebug( methodName, "The source already exists, doing nothing" );
            }
            else
            {
                logDebug( methodName, "Saving stock note source entity" );
                stockNoteSourceEntity = new StockNoteSourceEntity();
                stockNoteSourceEntity.setCustomerUuid( UUIDUtil.uuid( stockNoteSourceDTOContainer.getCustomerId() ));
                stockNoteSourceEntity.setName( stockNoteSourceDTOContainer.getNotesSourceId() );
                try
                {
                    stockNoteSourceEntity = this.saveEntity( stockNoteSourceEntity );
                    logDebug( methodName, "Created stock note source: {0}", stockNoteSourceEntity );
                    /*
                     * update the reference in the stock note id container
                     */
                    stockNoteSourceDTOContainer.setNotesSourceId( stockNoteSourceEntity.getUuid().toString() );
                    stockNoteSourceDTOContainer.setNotesSourceName( stockNoteSourceEntity.getName() );
                }
                catch( org.springframework.dao.DataIntegrityViolationException e )
                {
                    logWarn( methodName, "duplicate source insert attempt: " +
                                         stockNoteSourceDTOContainer.getNotesSourceName() );
                }
            }
        }
        logMethodEnd( methodName );
    }

    /**
     * Get the stock note source for the stock note source uuid.
     *
     * @param notesSourceId The id for the stock note source
     * @return StockNoteSourceEntity domain entity for stock note source id
     * @throws StockNoteSourceNotFoundException If the stock source is not found
     */
    public StockNoteSourceEntity getStockNoteSource( final String notesSourceId )
    {
        return this.getStockNoteSource( UUIDUtil.uuid( notesSourceId ) );
    }

    /**
     * Get the stock note source for the stock note source uuid.
     *
     * @param stockNoteSourceUuid The id for the stock note source
     * @return StockNoteSourceEntity domain entity for stock note source id
     * @throws StockNoteSourceNotFoundException If the stock source is not found
     */
    public StockNoteSourceEntity getStockNoteSource( final UUID stockNoteSourceUuid )
    {
        final String methodName = "getStockNoteSource";
        logMethodBegin( methodName, stockNoteSourceUuid );
        Objects.requireNonNull( stockNoteSourceUuid, "stockNotesSourceUuid cannot be null" );
        StockNoteSourceEntity stockNoteSourceEntity = this.stockNoteSourceRepository.findOne( stockNoteSourceUuid );
        if ( stockNoteSourceEntity == null )
        {
            throw new StockNoteSourceNotFoundException( stockNoteSourceUuid );
        }
        logMethodEnd( methodName, stockNoteSourceEntity );
        return stockNoteSourceEntity;
    }

    /**
     * Get all of the stock notes sources for a customer
     *
     * @param customerUuid The id for the stock note source
     */
    public List<StockNoteSourceDTO> getStockNoteSources( final UUID customerUuid )
    {
        final String methodName = "getStockNoteSources";
        logMethodBegin( methodName, customerUuid );
        Objects.requireNonNull( customerUuid, "customerUuid cannot be null" );
        List<StockNoteSourceEntity> stockNoteSourceEntities =
            stockNoteSourceRepository.findByCustomerUuidOrderByTimesUsedDesc( customerUuid );
        List<StockNoteSourceDTO> stockNoteSourceDTOs = this.entitiesToDTOs( stockNoteSourceEntities );
        logMethodEnd( methodName, stockNoteSourceEntities.size() + " sources found" );
        return stockNoteSourceDTOs;
    }

    /**
     * Create a new stock note source
     * @param stockNoteSourceDTO
     * @return
     */
    public StockNoteSourceDTO createStockNoteSource( final StockNoteSourceDTO stockNoteSourceDTO )
        throws EntityVersionMismatchException
    {
        final String methodName = "createStockNoteSource";
        logMethodBegin( methodName, stockNoteSourceDTO );
        StockNoteSourceEntity stockNoteSourceEntity = this.dtoToEntity( stockNoteSourceDTO );
        StockNoteSourceEntity newStockNoteSourceEntity = this.saveEntity( stockNoteSourceEntity );
        StockNoteSourceDTO newStockNoteSourceDTO = this.entityToDTO( newStockNoteSourceEntity );
        logMethodEnd( methodName, newStockNoteSourceDTO );
        return newStockNoteSourceDTO;
    }

    @Override
    protected StockNoteSourceDTO createDTO()
    {
        return this.context.getBean( StockNoteSourceDTO.class );
    }

    @Override
    protected StockNoteSourceEntity createEntity()
    {
        return this.context.getBean( StockNoteSourceEntity.class );
    }

    @Override
    protected StockNoteSourceRepository getRepository()
    {
        return this.stockNoteSourceRepository;
    }
}
