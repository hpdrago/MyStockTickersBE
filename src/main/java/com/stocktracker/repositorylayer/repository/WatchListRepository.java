package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.WatchListEntity;
import com.stocktracker.repositorylayer.entity.WatchListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Watch list entity repository
 * <p>
 * Created by mike on 8/24/2018.
 */
@Transactional( readOnly = true )
public interface WatchListRepository extends JpaRepository<WatchListEntity,UUID>
{
    /**
     * Get the watch lists for the customer.
     * @param customerId
     * @return
     */
    List<WatchListEntity> findByCustomerUuid( final UUID customerId );

    @Override
    @Transactional
    @Modifying
    WatchListEntity save( WatchListEntity linkedAccountEntity );

    @Override
    @Transactional
    @Modifying
    <S extends WatchListEntity> S saveAndFlush( S s );

    @Override
    @Transactional
    @Modifying
    void delete( WatchListEntity linkedAccountEntity );

    @Override
    @Transactional
    @Modifying
    void deleteInBatch( Iterable<WatchListEntity> iterable );
}
