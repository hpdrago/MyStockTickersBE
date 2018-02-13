package com.stocktracker.servicelayer.service;

import com.stocktracker.common.JSONDateConverter;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockNoteNotFoundException;
import com.stocktracker.common.exceptions.StockQuoteUnavailableException;
import com.stocktracker.repositorylayer.entity.StockNoteEntity;
import com.stocktracker.repositorylayer.entity.StockNoteSourceEntity;
import com.stocktracker.repositorylayer.repository.StockNoteRepository;
import com.stocktracker.servicelayer.stockinformationprovider.StockQuoteFetchMode;
import com.stocktracker.weblayer.dto.StockNoteDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * This is the service class for the StockNotesEntity
 *
 * Created by mike on 5/7/2017.
 */
@Service
@Transactional
public class StockNoteEntityService extends StockQuoteContainerEntityService<Integer,
                                                                                 StockNoteEntity,
                                                                                 StockNoteDTO,
                                                                                 StockNoteRepository>
{
    /**
     * Autowired service classes
     */
    private StockEntityService stockService;
    private StockNoteRepository stockNoteRepository;
    private StockTagService stockTagService;
    private StockNoteSourceEntityService stockNoteSourceService;

    /**
     * Get all of the notes for a customer.
     * @param pageRequest
     * @param customerId
     * @return
     */
    public Page<StockNoteDTO> getStockNotesForCustomerId( @NotNull final Pageable pageRequest,
                                                          @NotNull final int customerId )
    {
        final String methodName = "getStockNotesForCustomerId";
        logMethodBegin( methodName, pageRequest, customerId );
        Assert.isTrue( customerId > 0, "customerId must be > 0" );
        Page<StockNoteEntity> stockNoteEntities =
            this.stockNoteRepository.findByCustomerId( pageRequest, customerId );
        Page<StockNoteDTO> stockNoteDTOs = this.entitiesToDTOs( pageRequest, customerId, stockNoteEntities );
        logMethodEnd( methodName, stockNoteDTOs );
        return stockNoteDTOs;
    }

    /**
     * Get all of the notes for a customer and ticker symbol.
     * @param customerId
     * @return
     */
    public Page<StockNoteDTO> getStockNotesForCustomerIdAndTickerSymbol( @NotNull final Pageable pageRequest,
                                                                         @NotNull final int customerId,
                                                                         @NotNull final String tickerSymbol )
    {
        final String methodName = "getStockNotes";
        logMethodBegin( methodName, pageRequest, customerId, tickerSymbol );
        Objects.requireNonNull( tickerSymbol );
        Assert.isTrue( customerId > 0, "customerId must be > 0" );
        final Page<StockNoteEntity> stockNoteEntities =
            this.stockNoteRepository.findByCustomerIdAndTickerSymbol( pageRequest, customerId, tickerSymbol );
        final Page<StockNoteDTO> stockNoteDTOs = this.entitiesToDTOs( pageRequest, customerId, stockNoteEntities );
        logMethodEnd( methodName, stockNoteDTOs );
        return stockNoteDTOs;
    }

    /**
     * Converts the list of entities to DTOs and also populates the notes source values.
     * @param customerId
     * @param entities
     * @return
     */
    private Page<StockNoteDTO> entitiesToDTOs( final @NotNull Pageable pageRequest,
                                               final int customerId,
                                               final @NotNull Page<StockNoteEntity> entities )
    {
        Page<StockNoteDTO> stockNoteDTOs = super.entitiesToDTOs( pageRequest, entities );
        this.stockNoteSourceService.setNotesSourceName( customerId, stockNoteDTOs.getContent() );
        return stockNoteDTOs;
    }

    /**
     * Converts the DTO into DB Entities and stores the stock note information in the database.
     * The DTO contains information for one or more {@code StockNoteEntity}.
     * The user can specify multiple stocks for a note and that results in multiple stock note entries created one
     * for each stock.
     * @param stockNoteDTO
     */
    public StockNoteDTO createStockNote( final StockNoteDTO stockNoteDTO )
        throws StockNotFoundException,
               StockQuoteUnavailableException
    {
        final String methodName = "createStockNote";
        logMethodBegin( methodName, stockNoteDTO );
        Objects.requireNonNull( stockNoteDTO );
        this.stockNoteSourceService.checkForNewSource( stockNoteDTO );
        /*
         * Check to see if the stock exists
         */
        this.stockService.checkStockTableEntry( stockNoteDTO.getTickerSymbol() );
        StockNoteEntity stockNoteEntity = this.dtoToEntity( stockNoteDTO );
        /*
         * Set the stock price when created for one stock note entity
         */
        stockNoteEntity.setStockPriceWhenCreated( this.getStockQuoteService()
                                                      .getStockPrice( stockNoteEntity.getTickerSymbol() ));
        stockNoteEntity.setVersion( 1 );
        stockNoteEntity = this.stockNoteRepository.save( stockNoteEntity );
        StockNoteDTO returnStockNoteDTO = this.entityToDTO( stockNoteEntity );
        logMethodEnd( methodName, returnStockNoteDTO );
        return returnStockNoteDTO;
    }

    /**
     * Updates the STOCK_NOTE and STOCK_NOTE_STOCK tables with the information contained with {@code stockNoteDTO}
     * @param stockNoteDTO
     * @return
     */
    public StockNoteDTO updateStockNote( final StockNoteDTO stockNoteDTO )
        throws EntityVersionMismatchException
    {
        final String methodName = "updateStockNote";
        logMethodBegin( methodName, stockNoteDTO );
        Objects.requireNonNull( stockNoteDTO );
        /*
         * Check for any changes to the sources
         */
        this.stockNoteSourceService.checkForNewSource( stockNoteDTO );
        final StockNoteDTO returnStockNoteDTO = super.saveEntity( stockNoteDTO );
        logMethodEnd( methodName, returnStockNoteDTO );
        return returnStockNoteDTO;
    }

    /**
     * Delete a stock note
     * @param stockNotesId
     * @return
     */
    public StockNoteDTO delete( final int stockNotesId )
    {
        final String methodName = "updateStockNoteStocks";
        logMethodBegin( methodName, stockNotesId );
        StockNoteEntity stockNoteEntity = this.stockNoteRepository.findOne( stockNotesId );
        if ( stockNoteEntity == null )
        {
            throw new StockNoteNotFoundException( stockNotesId ) ;
        }
        this.stockNoteRepository.delete( stockNoteEntity );
        StockNoteDTO stockNoteDTO = this.entityToDTO( stockNoteEntity );
        logMethodEnd( methodName, stockNoteDTO );
        return stockNoteDTO;
    }

    /**
     * Converts the {@code stockNoteEntity} to a DTO
     * @param stockNoteEntity
     * @return
     */
    @Override
    protected StockNoteDTO entityToDTO( final StockNoteEntity stockNoteEntity )
    {
        final String methodName = "entityToDTO";
        Objects.requireNonNull( stockNoteEntity );
        StockNoteDTO stockNoteDTO = new StockNoteDTO();
        BeanUtils.copyProperties( stockNoteEntity, stockNoteDTO );
        stockNoteDTO.setNotesDate( JSONDateConverter.toY4MMDD( stockNoteEntity.getNotesDate() ));
        stockNoteDTO.setCreateDate( JSONDateConverter.toY4MMDD( stockNoteEntity.getCreateDate() ));
        stockNoteDTO.setUpdateDate( JSONDateConverter.toY4MMDD( stockNoteEntity.getUpdateDate() ));
        if ( stockNoteEntity.getStockNoteSourceByNotesSourceId() != null )
        {
            stockNoteDTO.setNotesSourceName( stockNoteEntity.getStockNoteSourceByNotesSourceId().getName() );
            stockNoteDTO.setNotesSourceId( stockNoteEntity.getStockNoteSourceByNotesSourceId().getId() );
        }
        try
        {
            this.getStockQuoteService().setStockQuoteInformation( stockNoteDTO, StockQuoteFetchMode.ASYNCHRONOUS );
        }
        catch ( StockQuoteUnavailableException e )
        {
            logError( methodName, e );
        }
        catch ( StockNotFoundException e )
        {
            e.printStackTrace();
        }
        return stockNoteDTO;
    }

    @Override
    protected StockNoteEntity dtoToEntity( final StockNoteDTO stockNoteDTO )
    {
        StockNoteEntity stockNoteEntity = StockNoteEntity.newInstance();
        BeanUtils.copyProperties( stockNoteDTO, stockNoteEntity );
        if ( stockNoteDTO.getNotesSourceId() != null &&
             stockNoteDTO.getNotesSourceId() > 0 )
        {
            StockNoteSourceEntity stockNoteSourceEntity = this.stockNoteSourceService
                                                              .getStockNoteSource( stockNoteDTO.getNotesSourceId() );
            stockNoteEntity.setStockNoteSourceByNotesSourceId( stockNoteSourceEntity );
        }
        stockNoteEntity.setNotesDate( JSONDateConverter.toTimestamp( stockNoteDTO.getNotesDate() ));
        return stockNoteEntity;
    }

    @Override
    protected StockNoteRepository getRepository()
    {
        return this.stockNoteRepository;
    }

    @Autowired
    public void setStockNoteRepository( final StockNoteRepository stockNoteRepository )
    {
        this.stockNoteRepository = stockNoteRepository;
    }

    /**
     * Allow DI to set the StockContainerService
     *
     * @param stockService
     */
    @Autowired
    public void setStockService( final StockEntityService stockService )
    {
        this.stockService = stockService;
    }

    @Autowired
    public void setStockTagService( final StockTagService stockTagService )
    {
        this.stockTagService = stockTagService;
    }

    @Autowired
    public void setStockNoteSourceService( final StockNoteSourceEntityService stockNoteSourceService )
    {
        this.stockNoteSourceService = stockNoteSourceService;
    }

}
