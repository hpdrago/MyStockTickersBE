package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockPositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * TradeItAccount entity repository
 * <p>
 * Created by mike on 12/4/2017.
 */
@Transactional( readOnly = true )
public interface StockPositionRepository extends JpaRepository<StockPositionEntity,UUID>
{
    /**
     * Gets all of the positions for the {@code linkedAccountId}
     * @param linkedAccountUuid
     * @return
     */
    List<StockPositionEntity> findAllByLinkedAccountUuid( UUID linkedAccountUuid );

    /**
     * Gets a page worth of stock positions.
     * @param linkedAccountUuid
     * @return
     */
    List<StockPositionEntity> findByLinkedAccountUuid( final UUID linkedAccountUuid );

    /**
     * Count the number of positions in the linked account.
     * @param linkedAccountUuid
     * @return
     */
    @Query( "SELECT COUNT(SP) FROM StockPositionEntity SP WHERE SP.linkedAccountUuid=?1" )
    Long countByLinkedAccountId( final int linkedAccountUuid );

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
    void delete( final UUID uuid );

    @Override
    @Transactional
    @Modifying
    void delete( StockPositionEntity stockPositionEntity );

    @Override
    @Transactional
    @Modifying
    void deleteInBatch( Iterable<StockPositionEntity> iterable );
}
