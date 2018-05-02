package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.PortfolioStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Created by mike on 10/30/2016.
 */
@Transactional( readOnly = true )
public interface PortfolioStockRepository extends JpaRepository<PortfolioStockEntity,UUID>
{
    /**
     * Find one by the primary key
     * @param customerUuid
     * @param portfolioUuid
     * @return
     */
    List<PortfolioStockEntity> findByCustomerUuidAndPortfolioUuidOrderByTickerSymbol( final UUID customerUuid,
                                                                                      final UUID portfolioUuid );

    /**
     * Find one by the secondary key
     * @param customerUuid
     * @param portfolioUuid
     * @param tickerSymbol
     * @return
     */
    PortfolioStockEntity findFirstByCustomerUuidAndPortfolioUuidAndTickerSymbol( final UUID customerUuid,
                                                                                 final UUID portfolioUuid,
                                                                                 final String tickerSymbol );

    List<PortfolioStockEntity> findByPortfolioUuidOrderByTickerSymbol( final UUID portfolioUuid );

    @Override
    @Transactional
    @Modifying
    PortfolioStockEntity save( PortfolioStockEntity portfolioStockEntity );

    @Override
    @Transactional
    @Modifying
    <S extends PortfolioStockEntity> List<S> save( Iterable<S> iterable );

    @Override
    @Transactional
    @Modifying
    <S extends PortfolioStockEntity> S saveAndFlush( S s );

    @Override
    @Transactional
    @Modifying
    void delete( UUID uuid );

    @Override
    @Transactional
    @Modifying
    void delete( PortfolioStockEntity portfolioStockEntity );

    @Override
    @Transactional
    @Modifying
    void deleteInBatch( Iterable<PortfolioStockEntity> iterable );
}
