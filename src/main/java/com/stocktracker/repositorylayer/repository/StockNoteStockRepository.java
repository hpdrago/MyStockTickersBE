package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockNoteStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by mike on 9/3/2018.
 */
public interface StockNoteStockRepository extends JpaRepository<StockNoteStockEntity, Integer>
{
    @Query("select sns from StockNoteStockEntity sns where sns.customerId = ?1 and " +
           "                                               sns.tickerSymbol = ?2")
    List<StockNoteStockEntity> findStockNoteStockEntitiesByCustomerIdAndTickerSymbol( final int customerId,
                                                                                      final String tickerSymbol );

    /*
    @Query("SELECT s FROM STOCK_NOTE")
    List<StockNoteStockEntity> findStockNoteStockEntitiesById_StockNoteIdFetchEagerly( final int stockNoteId );
    */
}
