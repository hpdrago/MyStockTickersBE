package com.stocktracker.servicelayer.service;

import com.stocktracker.common.exceptions.StockNoteSourceNotFoundException;
import com.stocktracker.repositorylayer.entity.StockNoteEntity;
import com.stocktracker.repositorylayer.entity.StockNoteSourceEntity;
import com.stocktracker.repositorylayer.entity.StockNoteStockEntity;
import com.stocktracker.repositorylayer.entity.VStockNoteCountEntity;
import com.stocktracker.weblayer.dto.StockNoteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

/**
 * Created by mike on 5/7/2017.
 */
@Service
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
        final String methodName = "setStockService";
        logMethodBegin( methodName, stockService );
        this.stockService = stockService;
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
            stockNoteRepository.findByCustomerIdOrderByNotesDateDesc( customerId );
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
        final String methodName = "getStockNotesStocks";
        logMethodBegin( methodName, customerId, tickerSymbol );
        Assert.isTrue( customerId > 0, "customerId must be > 0" );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        List<StockNoteStockEntity> stockNoteStockEntities =
            stockNoteStockRepository.findByCustomerIdAndTickerSymbol( customerId, tickerSymbol );
        logMethodEnd( methodName, stockNoteStockEntities.size() );
        return stockNoteStockEntities;
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
            vStockNoteCountRepository.findByCustomerId( customerId );
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
        final String methodName = "getNoteSource";
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
        final String methodName = "getNotesSources";
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
        final String methodName = "getNotesSources";
        logMethodBegin( methodName, stockNoteDTO );
        Objects.requireNonNull( stockNoteDTO );
        StockNoteEntity stockNoteEntity = StockNoteEntity.newInstance( stockNoteDTO );
        logDebug( methodName, "stockNoteEntity {0}", stockNoteEntity );
        stockNoteEntity = this.stockNoteRepository.save( stockNoteEntity );
        logDebug( methodName, "after insert stockNoteEntity {0}", stockNoteEntity );

        List<StockNoteStockEntity> stockNoteStockEntities =
            this.listCopyStockNoteStockDTOToStockNoteStockEntity.copy( stockNoteDTO.getStockNotesStocks() );
        /*
         * Need to set the stock note id that was just created in the stocks
         */
        for ( StockNoteStockEntity stockNoteStockEntity: stockNoteStockEntities )
        {
            stockNoteStockEntity.setCustomerId( stockNoteEntity.getCustomerId() );
            stockNoteStockEntity.setStockNoteId( stockNoteEntity.getId() );
            stockNoteStockEntity.setStockPrice( stockService.getStockPrice( stockNoteStockEntity.getTickerSymbol() ));
        }
        logDebug( methodName, "stockNoteStockEntities {0}", stockNoteStockEntities );
        stockNoteStockRepository.save( stockNoteStockEntities );
        StockNoteDTO returnStockNoteDTO = StockNoteDTO.newInstance( stockNoteEntity );
        logMethodEnd( methodName );
        return returnStockNoteDTO;
    }
}
