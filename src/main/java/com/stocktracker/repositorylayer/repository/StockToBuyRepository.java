package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockToBuyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by mike on 10/24/2017.
 */
public interface StockToBuyRepository extends JpaRepository<StockToBuyEntity, Integer>
{
    /**
     * Get all of the stock analytics records for a customer
     * @param customerId
     * @return
     */
    List<StockToBuyEntity> findByCustomerIdOrderByTickerSymbol( final int customerId );
}
