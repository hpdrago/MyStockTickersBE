package com.stocktracker.servicelayer.service;

import com.stocktracker.common.exceptions.DuplicateEntityException;
import com.stocktracker.repositorylayer.entity.WatchListEntity;
import com.stocktracker.repositorylayer.entity.WatchListStockEntity;
import com.stocktracker.repositorylayer.repository.WatchListStockRepository;
import com.stocktracker.weblayer.dto.WatchListStockDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * This service contains the methods for the WatchListStockEntity.
 */
@Service
public class WatchListStockEntityService extends StockInformationEntityService<WatchListStockEntity,
                                                                               WatchListStockDTO,
                                                                               WatchListStockRepository>
{
    @Autowired
    private WatchListStockRepository watchListStockRepository;

    /**
     * Add the a list of watch list stock instances for the watch list entity.
     * @param watchListEntity
     * @param watchListStocks
     */
    public List<WatchListStockDTO> addDTOs( final WatchListEntity watchListEntity,
                                            final List<WatchListStockDTO> watchListStocks )
        throws DuplicateEntityException
    {
        final String methodName = "addDTOs";
        logMethodBegin( methodName, watchListEntity );
        final List<WatchListStockEntity> watchListStockEntities = this.dtosToEntities( watchListStocks );
        watchListStockEntities.forEach( watchListStockEntity -> watchListStockEntity.setWatchListByWatchListUuid( watchListEntity ) );
        final List<WatchListStockEntity> addedEntities = this.addEntities( watchListStockEntities );
        final List<WatchListStockDTO> watchListStockDTOS = this.entitiesToDTOs( addedEntities );
        logMethodEnd( methodName );
        return watchListStockDTOS;
    }

    /**
     * Get all of the stocks for a watch list.
     * @param watchListUuid
     * @return
     */
    public List<WatchListStockDTO> getWatchListStocks( final UUID watchListUuid )
    {
        final String methodName = "addDTOs";
        logMethodBegin( methodName, watchListUuid );
        final List<WatchListStockEntity> entities = this.watchListStockRepository
                                                        .getAllByWatchListUuid( watchListUuid );
        final List<WatchListStockDTO> dtos = this.entitiesToDTOs( entities );
        logMethodEnd( methodName, dtos.size() );
        return dtos;
    }

    @Override
    public WatchListStockDTO entityToDTO( final WatchListStockEntity entity )
    {
        WatchListStockDTO watchListStockDTO = super.entityToDTO( entity );
        watchListStockDTO.setWatchListName( entity.getWatchListByWatchListUuid().getName() );
        return watchListStockDTO;
    }

    @Override
    protected WatchListStockDTO createDTO()
    {
        return this.context.getBean( WatchListStockDTO.class );
    }

    @Override
    protected WatchListStockEntity createEntity()
    {
        return this.context.getBean( WatchListStockEntity.class );
    }

    @Override
    protected WatchListStockRepository getRepository()
    {
        return this.watchListStockRepository;
    }
}
