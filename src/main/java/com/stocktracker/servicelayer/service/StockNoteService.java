package com.stocktracker.servicelayer.service;

import com.stocktracker.common.JSONDateConverter;
import com.stocktracker.common.exceptions.StockNoteNotFoundException;
import com.stocktracker.repositorylayer.entity.StockNoteEntity;
import com.stocktracker.repositorylayer.entity.StockNoteSourceEntity;
import com.stocktracker.repositorylayer.repository.StockNoteRepository;
import com.stocktracker.repositorylayer.repository.StockNoteSourceRepository;
import com.stocktracker.repositorylayer.repository.VStockNoteCountRepository;
import com.stocktracker.servicelayer.service.stockinformationprovider.StockQuoteFetchMode;
import com.stocktracker.servicelayer.service.stockinformationprovider.YahooStockService;
import com.stocktracker.weblayer.dto.StockNoteDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private StockNoteRepository stockNoteRepository;
    private StockNoteSourceRepository stockNoteSourceRepository;
    private VStockNoteCountRepository vStockNoteCountRepository;
    private YahooStockService yahooStockService;

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
        /*
         * For now, maybe until we create a view -- if that makes sense, load the sources and populate the source
         * values in the DTOs
         */
        List<StockNoteSourceEntity> customerSources = this.stockNoteSourceRepository.findByCustomerIdOrderByTimesUsedDesc( customerId );
        Map<Integer, String> sourceEntityMap = customerSources.stream()
                                                              .collect( Collectors.toMap( StockNoteSourceEntity::getId,
                                                                                          StockNoteSourceEntity::getName ));
        List<StockNoteDTO> stockNoteDTOs = super.entitiesToDTOs( entities );
        for ( StockNoteDTO stockNoteDTO: stockNoteDTOs )
        {
            if ( stockNoteDTO.getNotesSourceId() != null )
            {
                stockNoteDTO.setNotesSourceName( sourceEntityMap.get( stockNoteDTO.getNotesSourceId() ) );
            }
        }
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
    {
        final String methodName = "createStockNote";
        logMethodBegin( methodName, stockNoteDTO );
        Objects.requireNonNull( stockNoteDTO );
        StockNoteEntity stockNoteEntity = this.dtoToEntity( stockNoteDTO );
        checkForNewSource( stockNoteEntity, stockNoteDTO );
        /*
         * Set the stock price when created for one stock note entity
         */
        stockNoteEntity.setStockPriceWhenCreated( this.stockService.getStockPrice( stockNoteEntity.getTickerSymbol() ));
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
        StockNoteEntity dbStockNoteEntity = this.dtoToEntity( stockNoteDTO );
        /*
         * Check for any changes to the sources
         */
        this.checkForNewSource( dbStockNoteEntity, stockNoteDTO );
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
     * Check {@code stockNoteDTO} to see if the user entered a new note source.
     * If a new source is detected, a new source will be added to the database for the customer.
     * @param stockNoteEntity
     * @param stockNoteDTO
     */
    private void checkForNewSource( final StockNoteEntity stockNoteEntity, final StockNoteDTO stockNoteDTO )
    {
        final String methodName = "checkForNewSource";
        logMethodEnd( methodName );
        if ( stockNoteDTO.getNotesSourceId() == 0 &&
             stockNoteDTO.getNotesSourceName() != null &&
             stockNoteDTO.getNotesSourceName().length() > 0 )
        {
            /*
             * Make sure it doesn't already exist
             */
            StockNoteSourceEntity stockNoteSourceEntity =
                this.stockNoteSourceRepository.findByCustomerIdAndName( stockNoteDTO.getCustomerId(),
                                                                        stockNoteDTO.getNotesSourceName() ) ;
            logDebug( methodName, "stockNoteSourceEntity: {0}", stockNoteSourceEntity );
            if ( stockNoteSourceEntity != null )
            {
                logDebug( methodName, "The source already exists, doing nothing" );
            }
            else
            {
                logDebug( methodName, "Saving stock note source entity" );
                stockNoteSourceEntity = new StockNoteSourceEntity();
                stockNoteSourceEntity.setCustomerId( stockNoteDTO.getCustomerId() );
                stockNoteSourceEntity.setName( stockNoteDTO.getNotesSourceName() );
                stockNoteSourceEntity = this.stockNoteSourceRepository.save( stockNoteSourceEntity );
                logDebug( methodName, "Created stock note source: {0}", stockNoteSourceEntity );
                /*
                 * update the reference in the stock note
                 */
                stockNoteEntity.setStockNoteSourceByNotesSourceId( stockNoteSourceEntity );
            }
        }
        logMethodEnd( methodName );
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
        Objects.requireNonNull( stockNoteEntity );
        StockNoteDTO stockNoteDTO = new StockNoteDTO();
        BeanUtils.copyProperties( stockNoteEntity, stockNoteDTO );
        stockNoteDTO.setNotesDate( JSONDateConverter.toString( stockNoteEntity.getNotesDate() ));
        stockNoteDTO.setCreateDate( JSONDateConverter.toString( stockNoteEntity.getCreateDate() ));
        stockNoteDTO.setUpdateDate( JSONDateConverter.toString( stockNoteEntity.getUpdateDate() ));
        this.stockService.setStockQuoteInformation( stockNoteDTO, StockQuoteFetchMode.ASYNCHRONOUS );
        if ( stockNoteDTO.getStockQuoteState().isCurrent() )
        {
            stockNoteDTO.setPercentChange( calculatePercentOfChange( stockNoteDTO.getStockPriceWhenCreated(),
                                                                     stockNoteDTO.getLastPrice() ) );
        }
        return stockNoteDTO;
    }

    @Override
    protected StockNoteEntity dtoToEntity( final StockNoteDTO stockNoteDTO )
    {
        StockNoteEntity stockNoteEntity = StockNoteEntity.newInstance();
        BeanUtils.copyProperties( stockNoteDTO, stockNoteEntity );
        stockNoteEntity.setNotesDate( JSONDateConverter.toTimestamp( stockNoteDTO.getNotesDate() ));
        return stockNoteEntity;
    }

    @Autowired
    public void setStockNoteSourceRepository( final StockNoteSourceRepository stockNoteSourceRepository )
    {
        this.stockNoteSourceRepository = stockNoteSourceRepository;
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

    /**
     * Autowired service class
     */

    @Autowired
    public void setYahooStockService( final YahooStockService yahooStockService )
    {
        this.yahooStockService = yahooStockService;
    }

}
