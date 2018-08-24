package com.stocktracker.servicelayer.service;

import com.stocktracker.repositorylayer.entity.WatchListStockEntity;
import com.stocktracker.repositorylayer.repository.WatchListStockRepository;
import com.stocktracker.weblayer.dto.WatchListStockDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * This service contains the methods for the WatchListStockEntity.
 */
@Service
public class WatchListStockEntityService extends VersionedEntityService<UUID,
                                                                        WatchListStockEntity,
                                                                        UUID,
                                                                        WatchListStockDTO,
                                                                        WatchListStockRepository>
{
    @Autowired
    private WatchListStockRepository watchListStockRepository;

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
