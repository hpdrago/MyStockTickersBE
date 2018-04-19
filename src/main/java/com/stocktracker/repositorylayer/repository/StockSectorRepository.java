package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockSectorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * This portfoliostock handles access to the stock_sector table
 * Created by mike on 11/19/2016.
 */
@Transactional( readOnly = true )
public interface StockSectorRepository extends JpaRepository<StockSectorEntity, Integer>
{
    @Override
    @Transactional
    @Modifying
    StockSectorEntity save( StockSectorEntity stockSectorEntity );

    @Override
    @Transactional
    @Modifying
    <S extends StockSectorEntity> List<S> save( Iterable<S> iterable );

    @Override
    @Transactional
    @Modifying
    <S extends StockSectorEntity> S saveAndFlush( S s );

    @Override
    @Transactional
    @Modifying
    void delete( Integer integer );

    @Override
    @Transactional
    @Modifying
    void delete( StockSectorEntity stockSectorEntity );

    @Override
    @Transactional
    @Modifying
    void deleteInBatch( Iterable<StockSectorEntity> iterable );
}
