package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional( readOnly = true )
public interface StockQuoteRepository extends JpaRepository<StockQuoteEntity, String>
{
    @Override
    @Transactional
    @Modifying
    StockQuoteEntity save( StockQuoteEntity stockQuoteEntity );

    @Override
    @Transactional
    @Modifying
    <S extends StockQuoteEntity> List<S> save( Iterable<S> iterable );

    @Override
    @Transactional
    @Modifying
    <S extends StockQuoteEntity> S saveAndFlush( S s );

    @Override
    @Transactional
    @Modifying
    void delete( String s );

    @Override
    @Transactional
    @Modifying
    void delete( StockQuoteEntity stockQuoteEntity );

    @Override
    @Transactional
    @Modifying
    void deleteInBatch( Iterable<StockQuoteEntity> iterable );
}
