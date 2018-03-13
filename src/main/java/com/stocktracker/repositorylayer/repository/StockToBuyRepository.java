package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockToBuyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by mike on 10/24/2017.
 */
public interface StockToBuyRepository extends VersionedEntityRepository<Integer,StockToBuyEntity>
{
    /**
     * Get all of the stocks to buy records for a customer
     * @param customerId
     * @return
     */
    Page<StockToBuyEntity> findByCustomerId( final Pageable pageRequest, final int customerId );

    /**
     * Get all of the stocks to buy for the customer and the ticker symbol
     * @param pageRequest
     * @param customerId
     * @param tickerSymbol
     * @return
     */
    Page<StockToBuyEntity> findByCustomerIdAndTickerSymbol( final Pageable pageRequest,
                                                            final int customerId,
                                                            final String tickerSymbol );
}
