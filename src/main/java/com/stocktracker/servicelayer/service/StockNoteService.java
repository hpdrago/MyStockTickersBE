package com.stocktracker.servicelayer.service;

import com.stocktracker.common.exceptions.StockNoteSourceNotFoundException;
import com.stocktracker.repositorylayer.entity.StockNoteEntity;
import com.stocktracker.repositorylayer.entity.StockNoteSourceEntity;
import com.stocktracker.repositorylayer.entity.StockNoteStockEntity;
import com.stocktracker.repositorylayer.entity.StockNoteStockEntityPK;
import com.stocktracker.repositorylayer.entity.VStockNoteCountEntity;
import com.stocktracker.weblayer.dto.StockNoteDTO;
import com.stocktracker.weblayer.dto.StockNoteStockDTO;
import org.springframework.beans.factory.annotation.Autowired;
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
        loadStockNoteStocks( stockNoteEntities );
        logMethodEnd( methodName, stockNoteEntities.size() );
        return stockNoteEntities;
    }

    /**
     * Load the {@code StockNoteStockEntity} instances for each {@code StockNoteEntity} in {@code stockNoteEntities}.
     * The {@code StockNoteStockEntity} insteads are not load automatically by Hibernate -- which is good -- so that
     * we can load them when we need to.
     * @param stockNoteEntities
     */
    private void loadStockNoteStocks( final List<StockNoteEntity> stockNoteEntities )
    {
        final String methodName = "loadStockNoteStocks";
        logMethodBegin( methodName, stockNoteEntities );
        for ( StockNoteEntity stockNoteEntity: stockNoteEntities )
        {
            /*
             * Make the call to the database to load the StockNoteStocks
             */
            List<StockNoteStockEntity> stockNoteStockEntities =
                this.stockNoteStockRepository.findStockNoteStockEntitiesById_StockNoteId( stockNoteEntity.getId() );
            logDebug( methodName, "stockNoteStockEntities: {0}", stockNoteStockEntities );
            for ( StockNoteStockEntity stockNoteStockEntity: stockNoteStockEntities )
            {
                stockNoteEntity.addStockNoteStock( stockNoteStockEntity );
            }
        }
        logMethodEnd( methodName );
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
        final String methodName = "getStockNotesStocks";
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
        StockNoteSourceEntity stockNoteSourceEntity = stockNoteSourceRepository.findOne( stockNoteSourceId );
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
    public List<StockNoteSourceEntity> getStockNoteSources( final int customerId )
    {
        final String methodName = "getStockNoteSources";
        logMethodBegin( methodName, customerId );
        Assert.isTrue( customerId > 0, "customerId must be > 0" );
        List<StockNoteSourceEntity> stockNoteSourceEntities = stockNoteSourceRepository.findByCustomerId( customerId );
        logMethodEnd( methodName, stockNoteSourceEntities.size() + " sources found" );
        return stockNoteSourceEntities;
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
        /*
         * Save the stock notes
         */
        stockNoteEntity = this.stockNoteRepository.save( stockNoteEntity );
        logDebug( methodName, "after saving stockNoteEntity: {0}", stockNoteEntity );

        /*
         * Now that the parent is inserted, we can insert the child note stocks.
         */
        List<StockNoteStockEntity> stockNoteStockEntities = this.listCopyStockNoteStockDTOToStockNoteStockEntity
            .copy( stockNoteEntity, stockNoteDTO.getStockNotesStocks() );

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
        returnStockNoteDTO.setStockNotesStocks( stockNoteStockDTOS );
        logMethodEnd( methodName, returnStockNoteDTO );
        return returnStockNoteDTO;
    }
}
