package com.stocktracker.servicelayer.service;

import com.stocktracker.repositorylayer.entity.WatchListEntity;
import com.stocktracker.repositorylayer.repository.WatchListRepository;
import com.stocktracker.weblayer.dto.WatchListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * This service contains the methods for the WatchListEntity.
 */
@Service
public class WatchListEntityService extends VersionedEntityService<UUID,
                                                                   WatchListEntity,
                                                                   UUID,
                                                                   WatchListDTO,
                                                                   WatchListRepository>
{
    @Autowired
    private WatchListRepository watchListRepository;

    /**
     * Get the watch lists for a customer.
     * @param customerUuid
     * @return
     */
    public List<WatchListDTO> getWatchListsForCustomerUuid( final UUID customerUuid )
    {
        final String methodName = "getWatchListsForCustomerUuid";
        logMethodBegin( methodName, customerUuid );
        final List<WatchListEntity> watchListEntities = this.watchListRepository
                                                            .findByCustomerUuid( customerUuid );
        logMethodEnd( methodName, "Watch lists " + watchListEntities.size() );
        return this.entitiesToDTOs( watchListEntities );
    }

    @Override
    protected WatchListDTO createDTO()
    {
        return this.context.getBean( WatchListDTO.class );
    }

    @Override
    protected WatchListEntity createEntity()
    {
        return this.context.getBean( WatchListEntity.class );
    }

    @Override
    protected WatchListRepository getRepository()
    {
        return this.watchListRepository;
    }
}
