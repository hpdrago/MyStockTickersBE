package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.StockNoteSourceNotFoundException;
import com.stocktracker.repositorylayer.db.entity.StockNoteSourceEntity;
import com.stocktracker.servicelayer.entity.StockNoteSourceDE;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Created by mike on 5/7/2017.
 */
@Service
public class StockNoteSourceService extends BaseService implements MyLogger
{
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
}
