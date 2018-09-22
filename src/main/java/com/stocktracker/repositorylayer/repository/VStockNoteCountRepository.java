package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.VStockNoteCountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Created by mike on 5/7/2017.
 */
@Transactional( readOnly = true )
public interface VStockNoteCountRepository extends JpaRepository<VStockNoteCountEntity,UUID>
{
    /**
     * Get the list of ticker symbols and the number of notes for a single customer.
     *
     * @param customerUuid The customer's id
     * @return List of entities
     */
    List<VStockNoteCountEntity> findByCustomerUuid( final UUID customerUuid );

    @Override
    @Transactional
    @Modifying
    VStockNoteCountEntity save( VStockNoteCountEntity vStockNoteCountEntity );

    @Override
    @Transactional
    @Modifying
    <S extends VStockNoteCountEntity> S saveAndFlush( S s );

    @Override
    @Transactional
    @Modifying
    void delete( VStockNoteCountEntity vStockNoteCountEntity );

    @Override
    @Transactional
    @Modifying
    void deleteInBatch( Iterable<VStockNoteCountEntity> iterable );
}
