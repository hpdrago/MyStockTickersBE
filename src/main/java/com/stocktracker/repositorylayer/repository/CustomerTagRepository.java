package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.CustomerTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * This is the database repository for Customer Tags
 */
@Transactional( readOnly = true )
public interface CustomerTagRepository extends JpaRepository<CustomerTagEntity, Integer>
{
    @Override
    @Transactional
    @Modifying
    CustomerTagEntity save( CustomerTagEntity customerTagEntity );

    @Override
    @Transactional
    @Modifying
    <S extends CustomerTagEntity> List<S> save( Iterable<S> iterable );

    @Override
    @Transactional
    @Modifying
    <S extends CustomerTagEntity> S saveAndFlush( S s );

    @Override
    @Transactional
    @Modifying
    void delete( Integer integer );

    @Override
    @Transactional
    @Modifying
    void deleteInBatch( Iterable<CustomerTagEntity> iterable );

    @Override
    @Transactional
    @Modifying
    void delete( CustomerTagEntity customerTagEntity );
}
