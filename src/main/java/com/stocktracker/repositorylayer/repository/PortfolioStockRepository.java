package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.PortfolioStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by mike on 10/30/2016.
 */
@Transactional( readOnly = true )
public interface PortfolioStockRepository extends JpaRepository<PortfolioStockEntity, Integer>
{
    /**
     * Find one by the primary key
     *
     * @param portfolioId
     * @return
     */
    List<PortfolioStockEntity> findByCustomerIdAndPortfolioIdOrderByTickerSymbol( final int customerId,
                                                                                  final int portfolioId );

    /**
     * Find one by the secondary key
     *
     * @param customerId
     * @param portfolioId
     * @param tickerSymbol
     * @return
     */
    PortfolioStockEntity findFirstByCustomerIdAndPortfolioIdAndTickerSymbol( final int customerId,
                                                                             final int portfolioId,
                                                                             final String tickerSymbol );

    List<PortfolioStockEntity> findByPortfolioIdOrderByTickerSymbol( int portfolioId );

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
    void delete( Integer integer );

    @Override
    @Transactional
    @Modifying
    void delete( PortfolioStockEntity portfolioStockEntity );

    @Override
    @Transactional
    @Modifying
    void deleteInBatch( Iterable<PortfolioStockEntity> iterable );
}
