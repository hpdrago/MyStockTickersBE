package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.VStockTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional( readOnly = true )
public interface VStockTagRepository extends JpaRepository<VStockTagEntity, Integer>
{
    @Override
    @Transactional
    @Modifying
    VStockTagEntity save( VStockTagEntity vStockTagEntity );

    @Override
    @Transactional
    @Modifying
    <S extends VStockTagEntity> List<S> save( Iterable<S> iterable );

    @Override
    @Transactional
    @Modifying
    <S extends VStockTagEntity> S saveAndFlush( S s );

    @Override
    @Transactional
    @Modifying
    void delete( Integer integer );

    @Override
    @Transactional
    @Modifying
    void delete( VStockTagEntity vStockTagEntity );

    @Override
    @Transactional
    @Modifying
    void deleteInBatch( Iterable<VStockTagEntity> iterable );
}
