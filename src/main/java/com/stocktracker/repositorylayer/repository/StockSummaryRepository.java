package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockSummaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by mike on 5/7/2017.
 */
public interface StockSummaryRepository extends JpaRepository<StockSummaryEntity, Integer>
{
    /**
     * Get all of the stock summary records for a customer
     * @param customerId
     * @return
     */
    List<StockSummaryEntity> findByCustomerIdOrderByTickerSymbol( final int customerId );
}
