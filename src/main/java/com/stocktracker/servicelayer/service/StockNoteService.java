package com.stocktracker.servicelayer.service;

import com.stocktracker.common.JSONDateConverter;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockNoteNotFoundException;
import com.stocktracker.common.exceptions.StockQuoteUnavailableException;
import com.stocktracker.repositorylayer.entity.StockNoteEntity;
import com.stocktracker.repositorylayer.entity.StockNoteSourceEntity;
import com.stocktracker.repositorylayer.repository.StockNoteRepository;
import com.stocktracker.repositorylayer.repository.VStockNoteCountRepository;
import com.stocktracker.servicelayer.service.stockinformationprovider.StockQuoteFetchMode;
import com.stocktracker.weblayer.dto.StockNoteDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.util.List;
import java.util.Objects;

/**
 * This is the service class for the StockNotesEntity
 *
 * Created by mike on 5/7/2017.
 */
@Service
@Transactional
public class StockNoteService extends BaseService<StockNoteEntity, StockNoteDTO>
{
    /**
     * Autowired service classes
     */
    private StockService stockService;
    private StockQuoteService stockQuoteService;
    private StockNoteRepository stockNoteRepository;
    private VStockNoteCountRepository vStockNoteCountRepository;
    private StockTagService stockTagService;
    private StockNoteSourceService stockNoteSourceService;

    /**
     * Get all of the notes for a customer.
     * @param customerId
     * @return
     */
    public List<StockNoteDTO> getStockNotes( final int customerId )
    {
        final String methodName = "getStockNotes";
        logMethodBegin( methodName, customerId );
        Assert.isTrue( customerId > 0, "customerId must be > 0" );
        List<StockNoteEntity> stockNoteEntities =
            this.stockNoteRepository.findByCustomerIdOrderByNotesDateDesc( customerId );
        List<StockNoteDTO> stockNoteDTOs = this.entitiesToDTOs( customerId, stockNoteEntities );
        logMethodEnd( methodName, stockNoteDTOs );
        return stockNoteDTOs;
    }

    /**
     * Converts the list of entities to DTOs
     * @param customerId
     * @param entities
     * @return
     */
    private List<StockNoteDTO> entitiesToDTOs( final int customerId,
                                               final List<StockNoteEntity> entities )
    {
        List<StockNoteDTO> stockNoteDTOs = super.entitiesToDTOs( entities );
        this.stockNoteSourceService.setNotesSourceName( customerId, stockNoteDTOs );
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
        stockNoteEntity.setStockPriceWhenCreated( this.stockQuoteService
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
        throws ParseException
    {
        final String methodName = "updateStockNote";
        logMethodBegin( methodName, stockNoteDTO );
        Objects.requireNonNull( stockNoteDTO );
        /*
         * Check for any changes to the sources
         */
        this.stockNoteSourceService.checkForNewSource( stockNoteDTO );
        StockNoteEntity dbStockNoteEntity = this.dtoToEntity( stockNoteDTO );
        /*
         * Save the stock notes
         */
        dbStockNoteEntity = this.stockNoteRepository.save( dbStockNoteEntity );
        logDebug( methodName, "after saving dbStockNoteEntity: {0}", dbStockNoteEntity );

        /*
         * Convert back into a DTO to be sent back to the caller so that they have the updated information
         */
        StockNoteDTO returnStockNoteDTO = this.entityToDTO( dbStockNoteEntity );

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
            this.stockQuoteService.setStockQuoteInformation( stockNoteDTO, StockQuoteFetchMode.ASYNCHRONOUS );
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

    @Autowired
    public void setvStockNoteCountRepository( final VStockNoteCountRepository vStockNoteCountRepository )
    {
        this.vStockNoteCountRepository = vStockNoteCountRepository;
    }

    @Autowired
    public void setStockNoteRepository( final StockNoteRepository stockNoteRepository )
    {
        this.stockNoteRepository = stockNoteRepository;
    }

    /**
     * Allow DI to set the StockService
     *
     * @param stockService
     */
    @Autowired
    public void setStockService( final StockService stockService )
    {
        this.stockService = stockService;
    }

    @Autowired
    public void setStockTagService( final StockTagService stockTagService )
    {
        this.stockTagService = stockTagService;
    }

    @Autowired
    public void setStockNoteSourceService( final StockNoteSourceService stockNoteSourceService )
    {
        this.stockNoteSourceService = stockNoteSourceService;
    }

    @Autowired
    public void setStockQuoteService( final StockQuoteService stockQuoteService )
    {
        this.stockQuoteService = stockQuoteService;
    }

}
