package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockToBuyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by mike on 10/24/2017.
 */
@Transactional( readOnly = true )
public interface StockToBuyRepository extends JpaRepository<StockToBuyEntity, Integer>
{
    /**
     * Get all of the stocks to buy records for a customer
     *
     * @param customerId
     * @return
     */
    Page<StockToBuyEntity> findByCustomerId( final Pageable pageRequest, final int customerId );

    /**
     * Get all of the stocks to buy for the customer and the ticker symbol
     *
     * @param pageRequest
     * @param customerId
     * @param tickerSymbol
     * @return
     */
    Page<StockToBuyEntity> findByCustomerIdAndTickerSymbol( final Pageable pageRequest,
                                                            final int customerId,
                                                            final String tickerSymbol );

    @Override
    @Transactional
    @Modifying
    StockToBuyEntity save( StockToBuyEntity stockToBuyEntity );

    @Override
    @Transactional
    @Modifying
    <S extends StockToBuyEntity> List<S> save( Iterable<S> iterable );

    @Override
    @Transactional
    @Modifying
    <S extends StockToBuyEntity> S saveAndFlush( S s );

    @Override
    @Transactional
    @Modifying
    void delete( Integer integer );

    @Override
    @Transactional
    @Modifying
    void delete( StockToBuyEntity stockToBuyEntity );

    @Override
    @Transactional
    @Modifying
    void deleteInBatch( Iterable<StockToBuyEntity> iterable );
}
