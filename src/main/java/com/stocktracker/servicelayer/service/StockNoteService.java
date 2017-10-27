package com.stocktracker.servicelayer.service;

import com.stocktracker.common.JSONDateConverter;
import com.stocktracker.common.exceptions.StockNoteNotFoundException;
import com.stocktracker.repositorylayer.entity.StockNoteEntity;
import com.stocktracker.repositorylayer.entity.StockNoteSourceEntity;
import com.stocktracker.repositorylayer.repository.StockNoteRepository;
import com.stocktracker.repositorylayer.repository.StockNoteSourceRepository;
import com.stocktracker.repositorylayer.repository.VStockNoteCountRepository;
import com.stocktracker.weblayer.dto.StockNoteDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
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
    private StockNoteRepository stockNoteRepository;
    private StockNoteSourceRepository stockNoteSourceRepository;
    private VStockNoteCountRepository vStockNoteCountRepository;

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
    private YahooStockService yahooStockService;

    @Autowired
    public void setYahooStockService( final YahooStockService yahooStockService )
    {
        this.yahooStockService = yahooStockService;
    }

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
        List<StockNoteDTO> stockNoteDTOs = this.entitiesToDTOs( stockNoteEntities );
        logMethodEnd( methodName, stockNoteDTOs.size() );
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
        StockNoteEntity stockNoteEntity = this.newStockNoteEntity( stockNoteDTO );
                checkForNewSource( stockNoteEntity, stockNoteDTO );
        setStockPrice( stockNoteEntity );
        stockNoteEntity = this.stockNoteRepository.save( stockNoteEntity );
        StockNoteDTO returnStockNoteDTO = this.entityToDTO( stockNoteEntity );
        logMethodEnd( methodName, returnStockNoteDTO );
        return returnStockNoteDTO;
    }

    /**
     * Set the stock price when created for one stock note entity
     * @param stockNoteEntity
     */
    private void setStockPrice( final StockNoteEntity stockNoteEntity )
    {
        stockNoteEntity.setStockPriceWhenCreated( this.stockService
                                                      .getStockPrice( stockNoteEntity.getTickerSymbol() ));
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
         * Retrieve the entity from the database so we can apply the changes which includes the stocks -- we need to
         * compare the current stocks in the DB with the changes made by the user.
         */
        StockNoteEntity dbStockNoteEntity = stockNoteRepository.findOne( stockNoteDTO.getId() );
        logDebug( methodName, "dbStockNoteEntity: {0}", dbStockNoteEntity );

        /*
         * Copy the new values into the database entity
         */
        this.copyUpdateProperties( stockNoteDTO, dbStockNoteEntity );

        /*
         * Check for any changes to the sources
         */
        logDebug( methodName, "saving stockNoteEntity: {0}", dbStockNoteEntity );
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
     * Create a new StockNoteEntity instance from a StockNoteDE instance.
     * @param stockNoteDTO
     * @return
     */
    private StockNoteEntity newStockNoteEntity( final StockNoteDTO stockNoteDTO )
    {
        Objects.requireNonNull( stockNoteDTO );
        StockNoteEntity stockNoteEntity = new StockNoteEntity();
        BeanUtils.copyProperties( stockNoteDTO, stockNoteEntity );
        if ( stockNoteEntity.getActionTaken() == null )
        {
            stockNoteEntity.setActionTaken( StockNoteActionTaken.NONE.name() );
        }
        if ( stockNoteDTO.getNotesDate() != null )
        {
            try
            {
                stockNoteEntity.setNotesDate( JSONDateConverter.toTimestamp( stockNoteDTO.getNotesDate() ));
            }
            catch ( ParseException e )
            {
                throw new IllegalArgumentException( "Error converting notesDate to timestamp", e );
            }
        }
        return stockNoteEntity;
    }

    /**
     * Copies the properties from the {@code dtoStockNoteEntity} to the {@code dbStockNoteEntity}
     * @param stockNoteDTO
     * @param dbStockNoteEntity
     */
    public void copyUpdateProperties( @NotNull final StockNoteDTO stockNoteDTO,
                                      @NotNull final StockNoteEntity dbStockNoteEntity )
        throws ParseException
    {
        final String methodName = "copyUpdateProperties";
        logMethodBegin( methodName, stockNoteDTO, dbStockNoteEntity );
        Objects.requireNonNull( stockNoteDTO );
        Objects.requireNonNull( dbStockNoteEntity );
        /*
         * Only copy the properties that can be updated
         */
        dbStockNoteEntity.setNotes( stockNoteDTO.getNotes() );
        dbStockNoteEntity.setNotesDate( JSONDateConverter.toTimestamp( stockNoteDTO.getNotesDate() ));
        dbStockNoteEntity.setNotesRating( stockNoteDTO.getNotesRating() );
        dbStockNoteEntity.setPublicInd( stockNoteDTO.getPublicInd() == null ? "N"
                                                                            : stockNoteDTO.getPublicInd() ? "Y" : "N" );
        dbStockNoteEntity.setBullOrBear( stockNoteDTO.getBullOrBear() );
        logMethodEnd( methodName, dbStockNoteEntity );
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
                this.stockNoteSourceRepository.findByCustomerIdAndAndName( stockNoteDTO.getCustomerId(),
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
        return stockNoteDTO;
    }

    @Override
    protected StockNoteEntity dtoToEntity( final StockNoteDTO stockNoteDTO )
    {
        StockNoteEntity stockNoteEntity = StockNoteEntity.newInstance();
        BeanUtils.copyProperties( stockNoteDTO, stockNoteEntity );
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
}
