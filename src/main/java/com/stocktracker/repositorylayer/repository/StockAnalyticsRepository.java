package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockAnalyticsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

import static javafx.scene.input.KeyCode.T;

/**
 * Created by mike on 5/7/2017.
 */
public interface StockAnalyticsRepository extends JpaRepository<StockAnalyticsEntity, Integer>
{
    /**
     * Get all of the stock analytics records for a customer
     * @param customerId
     * @return
     */
    List<StockAnalyticsEntity> findByCustomerIdOrderByTickerSymbol( final int customerId );
}
