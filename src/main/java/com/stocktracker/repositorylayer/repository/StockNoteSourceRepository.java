package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockNoteSourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Created by mike on 5/7/2017.
 */
@Transactional( readOnly = true )
public interface StockNoteSourceRepository extends JpaRepository<StockNoteSourceEntity,UUID>
{
    /**
     * Retrieves all of the stock notes sources for a single customer
     *
     * @param customerUuid
     * @return
     */
    List<StockNoteSourceEntity> findByCustomerUuidOrderByTimesUsedDesc( final UUID customerUuid );

    /**
     * Finds a customer's stock note source by name
     *
     * @param customerUuid
     * @param sourceName
     * @return
     */
    StockNoteSourceEntity findByCustomerUuidAndName( final UUID customerUuid, final String sourceName );

    @Override
    @Transactional
    @Modifying
    StockNoteSourceEntity save( StockNoteSourceEntity stockNoteSourceEntity );

    @Override
    @Transactional
    @Modifying
    <S extends StockNoteSourceEntity> List<S> save( Iterable<S> iterable );

    @Override
    @Transactional
    @Modifying
    <S extends StockNoteSourceEntity> S saveAndFlush( S s );

    @Override
    @Transactional
    @Modifying
    void delete( Integer integer );

    @Override
    @Transactional
    @Modifying
    void delete( StockNoteSourceEntity stockNoteSourceEntity );

    @Override
    @Transactional
    @Modifying
    void deleteInBatch( Iterable<StockNoteSourceEntity> iterable );
}
