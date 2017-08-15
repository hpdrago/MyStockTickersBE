package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.db.entity.StockNoteEntity;
import com.stocktracker.servicelayer.entity.StockNoteDE;
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
                                                    findByCustomerIdAndTickerSymbolOOrderByDateCreatedDesc(
                                                        customerId, tickerSymbol );
        List<StockNoteDE> stockNoteDEs = listCopyStockNoteEntityToStockNoteDE.copy( stockNoteEntities );
        logMethodEnd( methodName, stockNoteDEs.size() );
        return stockNoteDEs;
    }
}
