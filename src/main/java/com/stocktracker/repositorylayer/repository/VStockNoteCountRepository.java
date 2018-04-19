package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.VStockNoteCountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by mike on 5/7/2017.
 */
@Transactional( readOnly = true )
public interface VStockNoteCountRepository extends JpaRepository<VStockNoteCountEntity, Integer>
{
    /**
     * Get the list of ticker symbols and the number of notes for a single customer.
     *
     * @param customerId The customer's id
     * @return List of entities
     */
    List<VStockNoteCountEntity> findByCustomerId( final int customerId );

    @Override
    @Transactional
    @Modifying
    VStockNoteCountEntity save( VStockNoteCountEntity vStockNoteCountEntity );


    @Override
    @Transactional
    @Modifying
    <S extends VStockNoteCountEntity> List<S> save( Iterable<S> iterable );

    @Override
    @Transactional
    @Modifying
    <S extends VStockNoteCountEntity> S saveAndFlush( S s );

    @Override
    @Transactional
    @Modifying
    void delete( Integer integer );

    @Override
    @Transactional
    @Modifying
    void delete( VStockNoteCountEntity vStockNoteCountEntity );

    @Override
    @Transactional
    @Modifying
    void deleteInBatch( Iterable<VStockNoteCountEntity> iterable );
}
