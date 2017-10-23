package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockAnalyticsEntity;
import com.stocktracker.repositorylayer.entity.StockCatalystEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by mike on 5/7/2017.
 */
public interface StockCatalystEventRepository extends JpaRepository<StockCatalystEventEntity, Integer>
{
    /**
     * Get all of the stock catalyst event records for a customer
     * @param customerId
     * @return
     */
    List<StockCatalystEventEntity> findByCustomerIdOrderByTickerSymbol( final int customerId );
}
