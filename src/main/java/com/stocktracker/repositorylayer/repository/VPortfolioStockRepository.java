package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.VPortfolioStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by mike on 10/30/2016.
 */
@Transactional( readOnly = true )
public interface VPortfolioStockRepository extends JpaRepository<VPortfolioStockEntity, Integer>
{
    List<VPortfolioStockEntity> findByPortfolioIdOrderByTickerSymbol( int portfolioId );

    @Override
    @Transactional
    @Modifying
    VPortfolioStockEntity save( VPortfolioStockEntity vPortfolioStockEntity );


    @Override
    @Transactional
    @Modifying
    <S extends VPortfolioStockEntity> List<S> save( Iterable<S> iterable );

    @Override
    @Transactional
    @Modifying
    <S extends VPortfolioStockEntity> S saveAndFlush( S s );

    @Override
    @Transactional
    @Modifying
    void delete( Integer integer );

    @Override
    @Transactional
    @Modifying
    void delete( VPortfolioStockEntity vPortfolioStockEntity );

    @Override
    @Transactional
    @Modifying
    void deleteInBatch( Iterable<VPortfolioStockEntity> iterable );
}
