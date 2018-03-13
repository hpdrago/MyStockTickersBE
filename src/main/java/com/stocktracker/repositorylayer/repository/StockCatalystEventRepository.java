package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockCatalystEventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by mike on 5/7/2017.
 */
public interface StockCatalystEventRepository extends VersionedEntityRepository<Integer,StockCatalystEventEntity>
{
    /**
     * Get all of the stock catalyst event records for a customer
     * @param customerId
     * @return
     */
    Page<StockCatalystEventEntity> findByCustomerIdOrderByTickerSymbol( final Pageable pageRequest,
                                                                        final int customerId );

    /**
     * Get the catalyst events for a customer and a ticker symbol.
     * @param customerId
     * @param tickerSymbol
     * @return
     */
    Page<StockCatalystEventEntity> findByCustomerIdAndTickerSymbolOrderByTickerSymbol( final Pageable pageRequest,
                                                                                       final int customerId,
                                                                                       final String tickerSymbol );
}
