package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockNoteStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by mike on 9/3/2018.
 */
public interface StockNoteStockRepository extends JpaRepository<StockNoteStockEntity, Integer>
{
    List<StockNoteStockEntity> findByCustomerIdAndTickerSymbol( final int customerId, final String tickerSymbol );
}
