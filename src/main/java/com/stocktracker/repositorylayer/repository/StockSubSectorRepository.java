package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockSubSectorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * This portfoliostock handles access to the stock_sub_sector table
 * Created by mike on 11/19/2016.
 */
@Transactional( readOnly = true )
public interface StockSubSectorRepository extends JpaRepository<StockSubSectorEntity, Integer>
{
    @Override
    @Transactional
    @Modifying
    StockSubSectorEntity save( StockSubSectorEntity stockSubSectorEntity );

    @Override
    @Transactional
    @Modifying
    <S extends StockSubSectorEntity> List<S> save( Iterable<S> iterable );

    @Override
    @Transactional
    @Modifying
    <S extends StockSubSectorEntity> S saveAndFlush( S s );

    @Override
    @Transactional
    @Modifying
    void delete( Integer integer );

    @Override
    @Transactional
    @Modifying
    void delete( StockSubSectorEntity stockSubSectorEntity );

    @Override
    @Transactional
    @Modifying
    void deleteInBatch( Iterable<StockSubSectorEntity> iterable );
}
