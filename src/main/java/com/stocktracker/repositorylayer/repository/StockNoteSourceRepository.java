package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockNoteSourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by mike on 5/7/2017.
 */
public interface StockNoteSourceRepository extends JpaRepository<StockNoteSourceEntity, Integer>
{
    /**
     * Retrieves all of the stock notes sources for a single customer
     * @param customerId
     * @return
     */
    List<StockNoteSourceEntity> findByCustomerIdOrderByTimesUsedDesc( final int customerId );

}
