package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockToBuyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by mike on 10/24/2017.
 */
public interface StockToBuyRepository extends JpaRepository<StockToBuyEntity, Integer>
{
    /**
     * Get all of the stocks to buy records for a customer
     * @param customerId
     * @return
     */
    Page<StockToBuyEntity> findByCustomerIdOrderByTickerSymbol( final Pageable pageRequest, final int customerId );

    /**
     * Get all of the stocks to buy for the customer and the ticker symbol
     * @param customerId
     * @param tickerSymbol
     * @return
     */
    Page<StockToBuyEntity> findByCustomerIdAndTickerSymbol( final Pageable pageRequest, final int customerId, final String tickerSymbol );
}
