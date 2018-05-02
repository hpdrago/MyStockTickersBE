package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockNoteEntity;
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
public interface StockNoteRepository extends JpaRepository<StockNoteEntity,UUID>
{
    /**
     * Get all of the stock notes for the customer and order by descending notes date
     * @param customerUuid
     * @return
     */
    Page<StockNoteEntity> findByCustomerUuid( final Pageable pageRequest,
                                              final UUID customerUuid );

    /**
     * Get all of the notes for a customer and ticker symbol.
     * @param customerUuid
     * @param tickerSymbol
     * @return
     */
    Page<StockNoteEntity> findByCustomerUuidAndTickerSymbol( final Pageable pageRequest,
                                                             final UUID customerUuid,
                                                             final String tickerSymbol );

    @Override
    @Transactional
    @Modifying
    StockNoteEntity save( StockNoteEntity stockNoteEntity );

    @Override
    @Transactional
    @Modifying
    <S extends StockNoteEntity> List<S> save( Iterable<S> iterable );

    @Override
    @Transactional
    @Modifying
    <S extends StockNoteEntity> S saveAndFlush( S s );

    @Override
    @Transactional
    @Modifying
    void delete( final UUID uuid );

    @Override
    @Transactional
    @Modifying
    void delete( StockNoteEntity stockNoteEntity );

    @Override
    @Transactional
    @Modifying
    void deleteInBatch( Iterable<StockNoteEntity> iterable );
}
