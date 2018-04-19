package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockCatalystEventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by mike on 5/7/2017.
 */
@Transactional( readOnly = true )
public interface StockCatalystEventRepository extends JpaRepository<StockCatalystEventEntity, Integer>
{
    /**
     * Get all of the stock catalyst event records for a customer
     *
     * @param customerId
     * @return
     */
    Page<StockCatalystEventEntity> findByCustomerIdOrderByTickerSymbol( final Pageable pageRequest,
                                                                        final int customerId );

    /**
     * Get the catalyst events for a customer and a ticker symbol.
     *
     * @param customerId
     * @param tickerSymbol
     * @return
     */
    Page<StockCatalystEventEntity> findByCustomerIdAndTickerSymbolOrderByTickerSymbol( final Pageable pageRequest,
                                                                                       final int customerId,
                                                                                       final String tickerSymbol );

    @Override
    @Transactional
    @Modifying
    StockCatalystEventEntity save( StockCatalystEventEntity stockCatalystEventEntity );

    @Override
    @Transactional
    @Modifying
    <S extends StockCatalystEventEntity> List<S> save( Iterable<S> iterable );

    @Override
    @Transactional
    @Modifying
    <S extends StockCatalystEventEntity> S saveAndFlush( S s );

    @Override
    @Transactional
    @Modifying
    void delete( Integer integer );

    @Override
    @Transactional
    @Modifying
    void delete( StockCatalystEventEntity stockCatalystEventEntity );

    @Override
    @Transactional
    @Modifying
    void deleteInBatch( Iterable<StockCatalystEventEntity> iterable );
}
