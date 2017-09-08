package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockNoteStockEntity;
import com.stocktracker.repositorylayer.entity.StockNoteStockEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by mike on 9/3/2018.
 */
public interface StockNoteStockRepository extends JpaRepository<StockNoteStockEntity, StockNoteStockEntityPK>
{
    /*
    List<StockNoteStockEntity> findStockNoteStockEntitiesByCustomerIdAndTickerSymbol( final int customerId,
                                                                                      final String tickerSymbol );*/

    /*
    @Query("SELECT s FROM STOCK_NOTE")
    List<StockNoteStockEntity> findStockNoteStockEntitiesById_StockNoteIdFetchEagerly( final int stockNoteId );
    */
}
