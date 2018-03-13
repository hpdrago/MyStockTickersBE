package com.stocktracker.servicelayer.service;

import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.StockNoteSourceNotFoundException;
import com.stocktracker.repositorylayer.entity.StockNoteSourceEntity;
import com.stocktracker.repositorylayer.repository.StockNoteSourceRepository;
import com.stocktracker.weblayer.dto.StockNoteSourceDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for the StockNoteSource entity interface to the database.
 */
@Service
@Transactional
public class StockNoteSourceEntityService extends VersionedEntityService<Integer,
                                                                         StockNoteSourceEntity,
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
        Optional<Integer> getStockNoteSourceId();
        void setNotesSourceEntity( final StockNoteSourceEntity stockNoteSourceEntity );
    }

    public interface StockNoteSourceDTOContainer
    {
        Integer getCustomerId();
        void setNotesSourceName( final String noteSourceName );
        void setNotesSourceId( final Integer notesSourceId );
        String getNotesSourceName();
        Integer getNotesSourceId();

    }

    /**
     * Sets the source name for all {@Code StockNoteSourceContainer} entries
     * @param customerId The customer id is used to retrieve the sources for the customer.
     * @param stockNoteSourceDTOContainers The list of {@code StockNoteSourceDTOContainer} instances.
     */
    public void setNotesSourceName( final Integer customerId,
                                    final List<? extends StockNoteSourceDTOContainer> stockNoteSourceDTOContainers )
    {
        /*
         * For now, maybe until we create a view -- if that makes sense, load the sources and populate the source
         * values in the DTOs
         */
        List<StockNoteSourceEntity> customerSources = this.stockNoteSourceRepository
            .findByCustomerIdOrderByTimesUsedDesc( customerId );
        Map<Integer, String> sourceEntityMap = customerSources.stream()
                                                              .collect( Collectors.toMap( StockNoteSourceEntity::getId,
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
         * If the source id is null and there is a name, this means the user assigned a source
         */
        if ( stockNoteSourceDTOContainer.getNotesSourceId() != null &&
             stockNoteSourceDTOContainer.getNotesSourceName() != null &&
             !stockNoteSourceDTOContainer.getNotesSourceName().isEmpty() )
        {
            /*
             * Make sure it doesn't already exist
             */
            StockNoteSourceEntity stockNoteSourceEntity =
                this.stockNoteSourceRepository.findByCustomerIdAndName( stockNoteSourceDTOContainer.getCustomerId(),
                                                                        stockNoteSourceDTOContainer.getNotesSourceName() ) ;
            logDebug( methodName, "stockNoteSourceEntity: {0}", stockNoteSourceEntity );
            if ( stockNoteSourceEntity != null )
            {
                logDebug( methodName, "The source already exists, doing nothing" );
            }
            else
            {
                logDebug( methodName, "Saving stock note source entity" );
                stockNoteSourceEntity = new StockNoteSourceEntity();
                stockNoteSourceEntity.setCustomerId( stockNoteSourceDTOContainer.getCustomerId() );
                stockNoteSourceEntity.setName( stockNoteSourceDTOContainer.getNotesSourceName() );
                try
                {
                    stockNoteSourceEntity = this.saveEntity( stockNoteSourceEntity );
                    logDebug( methodName, "Created stock note source: {0}", stockNoteSourceEntity );
                    /*
                     * update the reference in the stock note id container
                     */
                    stockNoteSourceDTOContainer.setNotesSourceId( stockNoteSourceEntity.getId() );
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
     * Get the stock note source for the stock note source id.
     *
     * @param stockNoteSourceId The id for the stock note source
     * @return StockNoteSourceDE domain entity for stock note source id
     * @throws StockNoteSourceNotFoundException If the stock source is not found
     */
    public StockNoteSourceEntity getStockNoteSource( final int stockNoteSourceId )
    {
        final String methodName = "getStockNoteSource";
        logMethodBegin( methodName, stockNoteSourceId );
        Assert.isTrue( stockNoteSourceId > 0, "stockNoteId must be > 0" );
        StockNoteSourceEntity stockNoteSourceEntity = this.stockNoteSourceRepository.findOne( stockNoteSourceId );
        if ( stockNoteSourceEntity == null )
        {
            throw new StockNoteSourceNotFoundException( stockNoteSourceId );
        }
        logMethodEnd( methodName, stockNoteSourceEntity );
        return stockNoteSourceEntity;
    }

    /**
     * Get all of the stock notes sources for a customer
     *
     * @param customerId The id for the stock note source
     */
    public List<StockNoteSourceDTO> getStockNoteSources( final int customerId )
    {
        final String methodName = "getStockNoteSources";
        logMethodBegin( methodName, customerId );
        Assert.isTrue( customerId > 0, "customerId must be > 0" );
        List<StockNoteSourceEntity> stockNoteSourceEntities =
            stockNoteSourceRepository.findByCustomerIdOrderByTimesUsedDesc( customerId );
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
    protected StockNoteSourceDTO entityToDTO( final StockNoteSourceEntity stockNoteSourceEntity )
    {
        Objects.requireNonNull( stockNoteSourceEntity );
        StockNoteSourceDTO stockNoteSourceDTO = StockNoteSourceDTO.newInstance();
        BeanUtils.copyProperties( stockNoteSourceEntity, stockNoteSourceDTO );
        return stockNoteSourceDTO;
    }

    @Override
    protected StockNoteSourceEntity dtoToEntity( final StockNoteSourceDTO stockNoteSourceDTO )
    {
        Objects.requireNonNull( stockNoteSourceDTO );
        StockNoteSourceEntity stockNoteSourceEntity = StockNoteSourceEntity.newInstance();
        BeanUtils.copyProperties( stockNoteSourceDTO, stockNoteSourceEntity );
        return stockNoteSourceEntity;
    }

    @Override
    protected StockNoteSourceRepository getRepository()
    {
        return this.stockNoteSourceRepository;
    }
}
