package com.stocktracker.repositorylayer.repository;

import com.stocktracker.repositorylayer.entity.WatchListStockEntity;
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
public interface WatchListStockRepository extends JpaRepository<WatchListStockEntity,UUID>
{
    /**
     * Get all of the stocks for a watch list.
     * @param watchListUUID
     * @return
     */
    List<WatchListStockEntity> getAllByWatchListUuid( final UUID watchListUUID );

    @Override
    @Transactional
    @Modifying
    WatchListStockEntity save( WatchListStockEntity linkedAccountEntity );

    @Override
    @Transactional
    @Modifying
    <S extends WatchListStockEntity> S saveAndFlush( S s );

    @Override
    @Transactional
    @Modifying
    void delete( WatchListStockEntity linkedAccountEntity );

    @Override
    @Transactional
    @Modifying
    void deleteInBatch( Iterable<WatchListStockEntity> iterable );
}
