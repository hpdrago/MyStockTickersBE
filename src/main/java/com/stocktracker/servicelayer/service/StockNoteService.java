package com.stocktracker.servicelayer.service;

import com.google.common.collect.Sets;
import com.stocktracker.common.JSONDateConverter;
import com.stocktracker.common.exceptions.StockNoteNotFoundException;
import com.stocktracker.repositorylayer.entity.StockNoteEntity;
import com.stocktracker.repositorylayer.entity.StockNoteSourceEntity;
import com.stocktracker.repositorylayer.entity.StockNoteStockEntity;
import com.stocktracker.repositorylayer.entity.StockNoteStockEntityPK;
import com.stocktracker.repositorylayer.entity.VStockNoteCountEntity;
import com.stocktracker.weblayer.dto.StockNoteDTO;
import com.stocktracker.weblayer.dto.StockNoteStockDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This is the service class for the StockNotesEntity
 *
 * Created by mike on 5/7/2017.
 */
@Service
@Transactional
public class StockNoteService extends BaseService
{
    /**
     * Autowired service class
     */
    private StockService stockService;

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
    public List<StockNoteEntity> getStockNotes( final int customerId )
    {
        final String methodName = "getStockNotes";
        logMethodBegin( methodName, customerId );
        Assert.isTrue( customerId > 0, "customerId must be > 0" );
        List<StockNoteEntity> stockNoteEntities =
            this.stockNoteRepository.findByCustomerIdOrderByNotesDateDesc( customerId );
        logMethodEnd( methodName, stockNoteEntities.size() );
        return stockNoteEntities;
    }

    /**
     * Get the stock notes for the customer and the ticker symbol.
     *
     * @param customerId
     * @param tickerSymbol
     * @return
     */
    public List<StockNoteStockEntity> getStockNoteStocks( final int customerId, final String tickerSymbol )
    {
        final String methodName = "getStocks";
        logMethodBegin( methodName, customerId, tickerSymbol );
        Assert.isTrue( customerId > 0, "customerId must be > 0" );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        //List<StockNoteStockEntity> stockNoteStockEntities =
        //    stockNoteStockRepository.findStockNoteStockEntitiesByCustomerIdAndTickerSymbol( customerId, tickerSymbol );
        //logMethodEnd( methodName, stockNoteStockEntities.size() );
        return null;// stockNoteStockEntities;
    }

    /**
     * Aggregates the number of notices for each stock (ticker symbol) for a customer.
     * @param customerId The customer id.
     * @return List of {@code StockNoteCountDE} instances.
     */
    public List<VStockNoteCountEntity> getStockNotesCount( final int customerId )
    {
        final String methodName = "getStockNotesCount";
        logMethodBegin( methodName, customerId );
        Assert.isTrue( customerId > 0, "customerId must be > 0" );
        List<VStockNoteCountEntity> stockNoteTickerSymbolCountEntities =
            this.vStockNoteCountRepository.findByCustomerId( customerId );
        logMethodEnd( methodName, stockNoteTickerSymbolCountEntities.size() );
        return stockNoteTickerSymbolCountEntities;
    }

    /**
     * Converts the DTO into DB Entities and stores the stock note information in the database.
     * The DTO contains information for a {@code StockNoteEntity} and one or more {@code StockNoteStockEntities}.
     * @param stockNoteDTO
     */
    public StockNoteDTO createStockNote( final StockNoteDTO stockNoteDTO )
    {
        final String methodName = "createStockNote";
        logMethodBegin( methodName, stockNoteDTO );
        Objects.requireNonNull( stockNoteDTO );
        StockNoteEntity stockNoteEntity = this.createStockNoteEntity( stockNoteDTO );
        logDebug( methodName, "saving stockNoteEntity: {0}", stockNoteEntity );
        checkForNewSource( stockNoteEntity, stockNoteDTO );
        /*
         * Save the stock notes
         * However, cannot insert the stock note stocks without a stock_note_id so we'll save these stock notes
         * now, and then insert them after the save of the StockNoteEnitty
         */
        List<StockNoteStockEntity> stockNoteStocks = stockNoteEntity.getStockNoteStocks();
        stockNoteEntity.setStockNoteStocks( null );
        stockNoteEntity = this.stockNoteRepository.save( stockNoteEntity );
        stockNoteEntity.setStockNoteStocks( stockNoteStocks );
        logDebug( methodName, "after saving stockNoteEntity: {0}", stockNoteEntity );

        /*
         * Convert back into a DTO to be sent back to the caller so that they have the updated information
         */
        StockNoteDTO returnStockNoteDTO = createStockNoteDTO( stockNoteEntity );
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
        StockNoteDTO returnStockNoteDTO = this.createStockNoteDTO( dbStockNoteEntity );

        logMethodEnd( methodName, returnStockNoteDTO );
        return returnStockNoteDTO;
    }

    /**
     * Creates a new StockNoteStockEntity instance from the {@code stockNoteDTO}
     * @param stockNoteDTO
     * @return
     */
    private StockNoteEntity createStockNoteEntity( final StockNoteDTO stockNoteDTO )
    {
        final String methodName = "createStockNoteEntity";
        logMethodBegin( methodName, stockNoteDTO );
        StockNoteEntity stockNoteEntity = newStockNoteEntity( stockNoteDTO );
        for ( final StockNoteStockDTO stockNoteStockDTO: stockNoteDTO.getStocks() )
        {
            StockNoteStockEntity stockNoteStockEntity = createStockNoteStockEntity( stockNoteEntity, stockNoteStockDTO );
            try
            {
                this.yahooStockService.setStockInformation( stockNoteStockEntity );
            }
            catch ( IOException e )
            {
                logError( methodName, "Error getting stock information", e );
            }
            stockNoteEntity.addStockNoteStock( stockNoteStockEntity );
        }
        logMethodEnd( methodName, stockNoteEntity );
        return stockNoteEntity;
    }

    /**
     * Creates a {@code StockNoteStockEntity} from an instance of a {@code StockNoteStockDTO}
     *
     * @param stockNoteEntity
     * @param stockNoteStockDTO
     * @return
     */
    private StockNoteStockEntity createStockNoteStockEntity( final StockNoteEntity stockNoteEntity,
                                                             final StockNoteStockDTO stockNoteStockDTO )
    {
        StockNoteStockEntity stockNoteStockEntity = new StockNoteStockEntity();
        stockNoteStockEntity.setId( stockNoteEntity.getId(), stockNoteStockDTO.getTickerSymbol() );
        stockNoteStockEntity.setCustomerId( stockNoteEntity.getCustomerId() );
        stockNoteStockEntity.setStockPrice( stockNoteStockDTO.getStockPrice() );
        return stockNoteStockEntity;
    }

    /**
     * Create a new instance from a StockNoteDE instance
     * @param stockNoteEntity
     * @return
     */
    public StockNoteDTO createStockNoteDTO( final StockNoteEntity stockNoteEntity )
    {
        Objects.requireNonNull( stockNoteEntity );
        StockNoteDTO stockNoteDTO = new StockNoteDTO();
        BeanUtils.copyProperties( stockNoteEntity, stockNoteDTO );
        try
        {
            stockNoteDTO.setNotesDate( JSONDateConverter.toString( stockNoteEntity.getNotesDate() ));
        }
        catch ( ParseException e )
        {
            throw new IllegalArgumentException( "Error converting UTC notes date to a string", e );
        }
        this.listCopyStockNoteStockEntityToStockNoteStockDTO
            .copy( stockNoteEntity.getStockNoteStocks(), stockNoteDTO.getStocks() );
        return stockNoteDTO;
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
        updateStockNoteStocks( stockNoteDTO, dbStockNoteEntity );
        logMethodEnd( methodName, dbStockNoteEntity );
    }

    /**
     * Updates the stock note stock entities in the {@code dbStockNoteEntity} with {@code dtoStockNoteEntity}
     * @param stockNoteDTO
     * @param dbStockNoteEntity
     */
    private void updateStockNoteStocks( final @NotNull StockNoteDTO stockNoteDTO,
                                        final @NotNull StockNoteEntity dbStockNoteEntity )
    {
        final String methodName = "updateStockNoteStocks";
        logMethodBegin( methodName, dbStockNoteEntity, dbStockNoteEntity );
        /*
         * Need to identify new or deleted stocks -- there is no updating of a stock
         */
        Set<String> originalStocks = dbStockNoteEntity.getStockNoteStocks()
                                                      .stream()
                                                      .map( stockNoteStockEntity -> stockNoteStockEntity.getId().getTickerSymbol() )
                                                      .collect( Collectors.toSet() );
        Set<String> currentStocks = stockNoteDTO.getStocks()
            .stream()
            .map( stockNoteStockDTO -> stockNoteStockDTO.getTickerSymbol() )
            .collect( Collectors.toSet() );
        Set<String> deletedStocks = Sets.difference( originalStocks, currentStocks );
        Set<String> newStocks = Sets.difference( currentStocks, originalStocks );
        logDebug( methodName, "deletedStocks: {0}", deletedStocks );
        logDebug( methodName, "newStocks: {0}", newStocks );
        /*
         * Remove the deleted stocks from the list
         */
        for ( String tickerSymbol: deletedStocks )
        {
            for ( int i = 0; i < dbStockNoteEntity.getStockNoteStocks().size(); i++ )
            {
                if ( dbStockNoteEntity.getStockNoteStocks().get( i )
                                      .getId()
                                      .getTickerSymbol()
                                      .equals( tickerSymbol ) )
                {
                    logDebug( methodName, "Removing stock: {0}", tickerSymbol );
                    dbStockNoteEntity.getStockNoteStocks().remove( i );
                    break;
                }
            }
        }

        /*
         * Add the new stocks to the list
         */
        for ( String tickerSymbol: newStocks )
        {
            logDebug( methodName, "Adding stock: {0}", tickerSymbol );
            StockNoteStockEntity stockNoteStockEntity = new StockNoteStockEntity();
            StockNoteStockEntityPK stockNoteStockEntityPK = new StockNoteStockEntityPK( dbStockNoteEntity.getId(),
                                                                                        tickerSymbol );
            stockNoteStockEntity.setId( stockNoteStockEntityPK );
            stockNoteStockEntity.setCustomerId( dbStockNoteEntity.getCustomerId() );
            dbStockNoteEntity.getStockNoteStocks().add( stockNoteStockEntity );
            stockNoteStockEntity.setStockPrice( this.stockService.getStockPrice( tickerSymbol ));
        }

        logMethodEnd( methodName, this );
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
        StockNoteDTO stockNoteDTO = this.createStockNoteDTO( stockNoteEntity );
        logMethodEnd( methodName, stockNoteDTO );
        return stockNoteDTO;
    }
}
