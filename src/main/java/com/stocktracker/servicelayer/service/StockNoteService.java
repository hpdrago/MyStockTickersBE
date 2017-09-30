package com.stocktracker.servicelayer.service;

import com.stocktracker.repositorylayer.entity.StockNoteEntity;
import com.stocktracker.repositorylayer.entity.StockNoteSourceEntity;
import com.stocktracker.repositorylayer.entity.StockNoteStockEntity;
import com.stocktracker.repositorylayer.entity.VStockNoteCountEntity;
import com.stocktracker.weblayer.dto.StockNoteDTO;
import com.stocktracker.weblayer.dto.StockNoteStockDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        StockNoteEntity stockNoteEntity = StockNoteEntity.newInstance( stockNoteDTO );
        logDebug( methodName, "saving stockNoteEntity: {0}", stockNoteEntity );
        checkForNewSource( stockNoteEntity, stockNoteDTO );
        /*
         * Save the stock notes
         */
        stockNoteEntity = this.stockNoteRepository.save( stockNoteEntity );
        logDebug( methodName, "after saving stockNoteEntity: {0}", stockNoteEntity );

        /*
         * Now that the parent is inserted, we can insert the child note stocks.
         */
        List<StockNoteStockEntity> stockNoteStockEntities = this.listCopyStockNoteStockDTOToStockNoteStockEntity
            .copy( stockNoteEntity, stockNoteDTO.getStocks() );

        /*
         * Save the stock not stocks
         */
        logDebug( methodName, "saving stockNoteStockEntities: {0}", stockNoteStockEntities );
        stockNoteStockEntities = stockNoteStockRepository.save( stockNoteStockEntities );

        /*
         * Convert back into a DTO to be sent back to the caller so that they have the updated information
         */
        StockNoteDTO returnStockNoteDTO = StockNoteDTO.newInstance( stockNoteEntity );
        List<StockNoteStockDTO> stockNoteStockDTOS = new ArrayList<>();
        this.listCopyStockNoteStockEntityToStockNoteStockDTO.copy( stockNoteStockEntities, stockNoteStockDTOS );
        returnStockNoteDTO.setStocks( stockNoteStockDTOS );
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
}
