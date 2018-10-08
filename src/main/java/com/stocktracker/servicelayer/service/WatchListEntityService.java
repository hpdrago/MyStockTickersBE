package com.stocktracker.servicelayer.service;

import com.stocktracker.common.exceptions.DuplicateEntityException;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.entity.WatchListEntity;
import com.stocktracker.repositorylayer.repository.WatchListRepository;
import com.stocktracker.weblayer.dto.WatchListDTO;
import com.stocktracker.weblayer.dto.WatchListStockDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * This service contains the methods for the WatchListEntity.
 */
@Service
public class WatchListEntityService extends UuidEntityService< WatchListEntity,
                                                               WatchListDTO,
                                                               WatchListRepository>
{
    @Autowired
    private WatchListRepository watchListRepository;

    @Autowired
    private WatchListStockEntityService watchListStockEntityService;

    /**
     * Adds {@code watchListDTO} and any watch list stocks to the database.
     * @param watchListDTO
     * @return
     * @throws EntityVersionMismatchException
     * @throws DuplicateEntityException
     * @throws VersionedEntityNotFoundException
     */
    @Override
    public WatchListDTO addDTO( final WatchListDTO watchListDTO )
        throws EntityVersionMismatchException, DuplicateEntityException, VersionedEntityNotFoundException
    {
        final String methodName = "addDTO";
        logMethodBegin( methodName, watchListDTO );
        final List<WatchListStockDTO> watchListStocks = watchListDTO.getWatchListStocks();
        /*
         * Save parent entity first
         */
        final WatchListEntity watchListEntity = this.dtoToEntity( watchListDTO );
        final WatchListEntity savedWatchListEntity = super.addEntity( watchListEntity );
        /*
         * Need to add any watch list stocks.
         */
        List<WatchListStockDTO> savedWatchListStockDTOs = null;
        if ( !watchListStocks.isEmpty() )
        {
            savedWatchListStockDTOs = this.watchListStockEntityService
                                          .addDTOs( savedWatchListEntity, watchListStocks );
        }
        final WatchListDTO returnWatchListDTO = this.entityToDTO( savedWatchListEntity );
        returnWatchListDTO.setWatchListStocks( savedWatchListStockDTOs );
        logMethodEnd( methodName, returnWatchListDTO );
        return returnWatchListDTO;
    }

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
