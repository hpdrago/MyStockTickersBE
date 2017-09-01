package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.StockNoteSourceNotFoundException;
import com.stocktracker.repositorylayer.db.entity.StockNoteEntity;
import com.stocktracker.repositorylayer.db.entity.StockNoteSourceEntity;
import com.stocktracker.repositorylayer.db.entity.VStockNoteCountEntity;
import com.stocktracker.servicelayer.entity.StockNoteCountDE;
import com.stocktracker.servicelayer.entity.StockNoteDE;
import com.stocktracker.servicelayer.entity.StockNoteSourceDE;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

/**
 * Created by mike on 5/7/2017.
 */
@Service
public class StockNoteService extends BaseService implements MyLogger
{
    /**
     * Get the stock notes for the customer and the ticker symbol.
     *
     * @param customerId
     * @param tickerSymbol
     * @return
     */
    public List<StockNoteDE> getStockNotes( final int customerId, final String tickerSymbol )
    {
        final String methodName = "getStockNotes";
        logMethodBegin( methodName, customerId, tickerSymbol );
        Assert.isTrue( customerId > 0, "customerId must be > 0" );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        List<StockNoteEntity> stockNoteEntities = stockNoteRepository.
                                                    findByCustomerIdAndTickerSymbolOrderByDateCreatedDesc(
                                                        customerId, tickerSymbol );
        List<StockNoteDE> stockNoteDEs = listCopyStockNoteEntityToStockNoteDE.copy( stockNoteEntities );
        logMethodEnd( methodName, stockNoteDEs.size() );
        return stockNoteDEs;
    }

    /**
     * Aggregates the number of notices for each stock (ticker symbol) for a customer.
     * @param customerId The customer id.
     * @return List of {@code StockNoteCountDE} instances.
     */
    public List<StockNoteCountDE> getStockNotesCount( final int customerId )
    {
        final String methodName = "getStockNotesCount";
        logMethodBegin( methodName, customerId );
        Assert.isTrue( customerId > 0, "customerId must be > 0" );
        List<VStockNoteCountEntity> stockNoteTickerSymbolCountEntities =
            vStockNoteCountRepository.findByCustomerId( customerId );
        List<StockNoteCountDE> stockNoteCountDES =
            listCopyVStockNoteCountEntityToStockNoteCountDE.copy( stockNoteTickerSymbolCountEntities );
        logMethodEnd( methodName, stockNoteCountDES.size() );
        return stockNoteCountDES;
    }

    /**
     * Get the stock note source for the stock note source id.
     *
     * @param stockNoteSourceId The id for the stock note source
     * @return StockNoteSourceDE domain entity for stock note source id
     * @throws StockNoteSourceNotFoundException If the stock source is not found
     */
    public StockNoteSourceDE getStockNoteSource( final int stockNoteSourceId )
    {
        final String methodName = "getNoteSource";
        logMethodBegin( methodName, stockNoteSourceId );
        Assert.isTrue( stockNoteSourceId > 0, "stockNoteId must be > 0" );
        StockNoteSourceEntity stockNoteSourceEntity = stockNoteSourceRepository.findOne( stockNoteSourceId );
        if ( stockNoteSourceEntity == null )
        {
            throw new StockNoteSourceNotFoundException( stockNoteSourceId );
        }
        StockNoteSourceDE stockNoteSourceDE = StockNoteSourceDE.newInstance( stockNoteSourceEntity );
        logMethodEnd( methodName, stockNoteSourceDE );
        return stockNoteSourceDE;
    }

    /**
     * Get all of the stock notes sources for a customer
     *
     * @param customerId The id for the stock note source
     */
    public List<StockNoteSourceDE> getStockNoteSources( final int customerId )
    {
        final String methodName = "getNotesSources";
        logMethodBegin( methodName, customerId );
        Assert.isTrue( customerId > 0, "customerId must be > 0" );
        List<StockNoteSourceEntity> stockNoteSourceEntities = stockNoteSourceRepository.findByCustomerId( customerId );
        List<StockNoteSourceDE> stockNoteSourceDEList = listCopyStockNoteSourceEntityToStockNoteSourceDE.copy( stockNoteSourceEntities );
        logMethodEnd( methodName, stockNoteSourceDEList.size() + " sources found" );
        return stockNoteSourceDEList;
    }
}
