package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockPositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * TradeItAccount entity repository
 * <p>
 * Created by mike on 12/4/2017.
 */
@Transactional( readOnly = true )
public interface StockPositionRepository extends JpaRepository<StockPositionEntity, Integer>
{
    /**
     * Gets all of the positions for the {@code linkedAccountId}
     *
     * @param linkedAccountId
     * @return
     */
    List<StockPositionEntity> findAllByLinkedAccountId( int linkedAccountId );

    /**
     * Gets a page worth of stock positions.
     *
     * @param linkedAccountId
     * @return
     */
    List<StockPositionEntity> findByLinkedAccountId( final int linkedAccountId );

    /**
     * Count the number of positions in the linked account.
     *
     * @param linkedAccountId
     * @return
     */
    @Query( "SELECT COUNT(SP) FROM StockPositionEntity SP WHERE SP.linkedAccountId=?1" )
    Long countByLinkedAccountId( final int linkedAccountId );

    @Override
    @Transactional
    @Modifying
    StockPositionEntity save( StockPositionEntity stockPositionEntity );

    @Override
    @Transactional
    @Modifying
    <S extends StockPositionEntity> List<S> save( Iterable<S> iterable );

    @Override
    @Transactional
    @Modifying
    <S extends StockPositionEntity> S saveAndFlush( S s );

    @Override
    @Transactional
    @Modifying
    void delete( Integer integer );

    @Override
    @Transactional
    @Modifying
    void delete( StockPositionEntity stockPositionEntity );

    @Override
    @Transactional
    @Modifying
    void deleteInBatch( Iterable<StockPositionEntity> iterable );
}
