package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockAnalystConsensusEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Created by mike on 5/7/2017.
 */
@Transactional( readOnly = true )
public interface StockAnalystConsensusRepository extends JpaRepository<StockAnalystConsensusEntity,UUID>
{
    /**
     * Get all of the stock analytics records for a customer
     * @param customerUuid
     * @return
     */
    Page<StockAnalystConsensusEntity> findByCustomerUuid( final Pageable pageRequest,
                                                          final UUID customerUuid );

    /**
     * Get the list of analyst consensus by customer and ticker symbol.
     * @param customerUuid
     * @param tickerSymbol
     * @return
     */
    Page<StockAnalystConsensusEntity> findByCustomerUuidAndTickerSymbol( final Pageable pageRequest,
                                                                         final UUID customerUuid,
                                                                         final String tickerSymbol );

    /**
     * Get the list of analyst consensus by customer and ticker symbol.
     * @param customerUuid
     * @param tickerSymbol
     * @return
     */
    StockAnalystConsensusEntity findByCustomerUuidAndTickerSymbol( final UUID customerUuid, final String tickerSymbol );

    /**
     * Get all of the stock consensus rows for a customer.
     * @param customerUuid
     * @return
     */
    List<StockAnalystConsensusEntity> findByCustomerUuid( final UUID customerUuid );

    @Override
    @Transactional
    @Modifying
    StockAnalystConsensusEntity save( StockAnalystConsensusEntity stockAnalystConsensusEntity );

    @Override
    @Transactional
    @Modifying
    <S extends StockAnalystConsensusEntity> S saveAndFlush( S s );

    @Override
    @Transactional
    @Modifying
    void delete( StockAnalystConsensusEntity stockAnalystConsensusEntity );

    @Override
    @Transactional
    @Modifying
    void deleteInBatch( Iterable<StockAnalystConsensusEntity> iterable );
}
