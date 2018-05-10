package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * This is the database repository for Stock Tags
 */
@Transactional( readOnly = true )
public interface StockTagRepository extends JpaRepository<StockTagEntity, UUID>
{
    @Override
    @Transactional
    @Modifying
    StockTagEntity save( StockTagEntity stockTagEntity );

    @Override
    @Transactional
    @Modifying
    <S extends StockTagEntity> List<S> save( Iterable<S> iterable );

    @Override
    @Transactional
    @Modifying
    <S extends StockTagEntity> S saveAndFlush( S s );

    @Override
    @Transactional
    @Modifying
    void delete( final UUID uuid );

    @Override
    @Transactional
    @Modifying
    void delete( StockTagEntity stockTagEntity );

    @Override
    @Transactional
    @Modifying
    void deleteInBatch( Iterable<StockTagEntity> iterable );
}
