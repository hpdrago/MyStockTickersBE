package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.StockPositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * TradeItAccount entity repository
 *
 * Created by mike on 12/4/2017.
 */
public interface StockPositionRepository extends JpaRepository<StockPositionEntity,Integer>
{
    /**
     * Gets all of the positions for the {@code linkedAccountId}
     * @param linkedAccountId
     * @return
     */
    List<StockPositionEntity> findAllByLinkedAccountId( int linkedAccountId );

    /**
     * Gets a page worth of stock positions.
     * @param linkedAccountId
     * @return
     */
    List<StockPositionEntity> findByLinkedAccountId( final int linkedAccountId );

    /**
     * Count the number of positions in the linked account.
     * @param linkedAccountId
     * @return
     */
    @Query( "SELECT COUNT(SP) FROM StockPositionEntity SP WHERE SP.linkedAccountId=?1")
    Long countByLinkedAccountId( final int linkedAccountId );
}
