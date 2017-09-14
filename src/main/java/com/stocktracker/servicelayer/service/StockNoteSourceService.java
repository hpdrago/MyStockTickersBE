package com.stocktracker.servicelayer.service;

import com.stocktracker.common.exceptions.StockNoteSourceNotFoundException;
import com.stocktracker.repositorylayer.entity.StockNoteSourceEntity;
import com.stocktracker.weblayer.dto.StockNoteSourceDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class StockNoteSourceService extends BaseService
{
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
        List<StockNoteSourceEntity> stockNoteSourceEntities =
            stockNoteSourceRepository.findByCustomerIdOrderByTimesUsedDesc( customerId );
        logMethodEnd( methodName, stockNoteSourceEntities.size() + " sources found" );
        return stockNoteSourceEntities;
    }

    /**
     * Create a new stock note source
     * @param stockNoteSourceDTO
     * @return
     */
    public StockNoteSourceDTO createStockNoteSource( final StockNoteSourceDTO stockNoteSourceDTO )
    {
        final String methodName = "createStockNoteSource";
        logMethodBegin( methodName, stockNoteSourceDTO );
        StockNoteSourceEntity stockNoteSourceEntity = StockNoteSourceEntity.newInstance( stockNoteSourceDTO );
        StockNoteSourceEntity newStockNoteSourceEntity = this.stockNoteSourceRepository.save( stockNoteSourceEntity );
        StockNoteSourceDTO newStockNoteSourceDTO = StockNoteSourceDTO.newInstance( newStockNoteSourceEntity );
        logMethodEnd( methodName, newStockNoteSourceDTO );
        return newStockNoteSourceDTO;
    }
}
