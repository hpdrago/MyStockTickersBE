package com.stocktracker.repositorylayer;

import com.stocktracker.repositorylayer.db.entity.StockNoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by mike on 5/7/2017.
 */
public interface StockNoteRepository extends JpaRepository<StockNoteEntity, Integer>
{
    List<StockNoteEntity> findByCustomerIdAndTickerSymbolOrderByDateCreatedDesc( final int customerId,
                                                                                 final String tickerSymbol );
}
