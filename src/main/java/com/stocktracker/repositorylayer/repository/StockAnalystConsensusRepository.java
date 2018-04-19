package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockAnalystConsensusEntity;
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
public interface StockAnalystConsensusRepository extends JpaRepository<StockAnalystConsensusEntity, Integer>
{
    /**
     * Get all of the stock analytics records for a customer
     *
     * @param customerId
     * @return
     */
    Page<StockAnalystConsensusEntity> findByCustomerId( final Pageable pageRequest,
                                                        final int customerId );

    /**
     * Get the list of analyst consensus by customer and ticker symbol.
     *
     * @param customerId
     * @param tickerSymbol
     * @return
     */
    Page<StockAnalystConsensusEntity> findByCustomerIdAndTickerSymbol( final Pageable pageRequest,
                                                                       final int customerId, final String tickerSymbol );

    /**
     * Get the list of analyst consensus by customer and ticker symbol.
     *
     * @param customerId
     * @param tickerSymbol
     * @return
     */
    StockAnalystConsensusEntity findByCustomerIdAndTickerSymbol( final int customerId, final String tickerSymbol );

    /**
     * Get all of the stock consensus rows for a customer.
     *
     * @param customerId
     * @return
     */
    List<StockAnalystConsensusEntity> findByCustomerId( Integer customerId );

    @Override
    @Transactional
    @Modifying
    StockAnalystConsensusEntity save( StockAnalystConsensusEntity stockAnalystConsensusEntity );

    @Override
    @Transactional
    @Modifying
    <S extends StockAnalystConsensusEntity> List<S> save( Iterable<S> iterable );

    @Override
    @Transactional
    @Modifying
    <S extends StockAnalystConsensusEntity> S saveAndFlush( S s );

    @Override
    @Transactional
    @Modifying
    void delete( Integer integer );

    @Override
    @Transactional
    @Modifying
    void delete( StockAnalystConsensusEntity stockAnalystConsensusEntity );

    @Override
    @Transactional
    @Modifying
    void deleteInBatch( Iterable<StockAnalystConsensusEntity> iterable );
}
