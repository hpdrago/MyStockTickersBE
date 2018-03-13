package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockAnalystConsensusEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by mike on 5/7/2017.
 */
public interface StockAnalystConsensusRepository extends VersionedEntityRepository<Integer,StockAnalystConsensusEntity>
{
    /**
     * Get all of the stock analytics records for a customer
     * @param customerId
     * @return
     */
    Page<StockAnalystConsensusEntity> findByCustomerId( final Pageable pageRequest,
                                                        final int customerId );

    /**
     * Get the list of analyst consensus by customer and ticker symbol.
     * @param customerId
     * @param tickerSymbol
     * @return
     */
    Page<StockAnalystConsensusEntity> findByCustomerIdAndTickerSymbol( final Pageable pageRequest,
                                                                       final int customerId, final String tickerSymbol );

    /**
     * Get the list of analyst consensus by customer and ticker symbol.
     * @param customerId
     * @param tickerSymbol
     * @return
     */
    StockAnalystConsensusEntity findByCustomerIdAndTickerSymbol( final int customerId, final String tickerSymbol );
}
