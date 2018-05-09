package com.stocktracker.servicelayer.service;

import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.LinkedAccountNotFoundException;
import com.stocktracker.common.exceptions.TradeItAPIException;
import com.stocktracker.common.exceptions.TradeItAccountNotFoundException;
import com.stocktracker.common.exceptions.TradeItAuthenticationException;
import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.repositorylayer.entity.StockPositionEntity;
import com.stocktracker.repositorylayer.entity.TradeItAccountEntity;
import com.stocktracker.repositorylayer.repository.StockPositionRepository;
import com.stocktracker.servicelayer.service.common.StockPositionComparator;
import com.stocktracker.servicelayer.tradeit.TradeItService;
import com.stocktracker.servicelayer.tradeit.apiresults.GetPositionsAPIResult;
import com.stocktracker.servicelayer.tradeit.types.TradeItPosition;
import com.stocktracker.weblayer.dto.StockPositionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This service class handles requests for StockPosition actions.
 */
@Service
public class StockPositionService extends StockInformationEntityService<StockPositionEntity,
                                                                        StockPositionDTO,
                                                                        StockPositionRepository>
{
    private StockPositionRepository stockPositionRepository;
    private TradeItService tradeItService;
    private TradeItAccountEntityService tradeItAccountEntityService;
    private LinkedAccountEntityService linkedAccountEntityService;

    /**
     * Get the positions for the linked account.
     * @param customerUuid
     * @param tradeItAccountUuid
     * @param linkedAccountUuid
     * @return List of positions.
     * @throws LinkedAccountNotFoundException
     * @throws TradeItAccountNotFoundException
     * @throws TradeItAPIException
     * @throws EntityVersionMismatchException
     * @throws VersionedEntityNotFoundException
     */
    public List<StockPositionDTO> getPositions( final UUID customerUuid,
                                                final UUID tradeItAccountUuid,
                                                final UUID linkedAccountUuid )
        throws LinkedAccountNotFoundException,
               TradeItAccountNotFoundException,
               TradeItAPIException,
               EntityVersionMismatchException,
               VersionedEntityNotFoundException
    {
        final String methodName = "getPositions";
        logMethodBegin( methodName, customerUuid, tradeItAccountUuid, linkedAccountUuid );
        this.synchronizePositions( customerUuid, tradeItAccountUuid, linkedAccountUuid ) ;
        final List<StockPositionEntity> stockPositionEntities = this.stockPositionRepository
                                                                    .findByLinkedAccountUuid( linkedAccountUuid );
        final List<StockPositionDTO> stockPositionDTOs = this.entitiesToDTOs( stockPositionEntities );
        stockPositionDTOs.forEach( stockPositionDTO ->
                                   {
                                       stockPositionDTO.setCustomerUuid( customerUuid );
                                       stockPositionDTO.setLinkedAccountId( linkedAccountUuid );
                                       stockPositionDTO.setTradeItAccountId( tradeItAccountUuid );
                                   });
        logMethodEnd( methodName, "Returning " + stockPositionDTOs.size() + " positions" );
        return stockPositionDTOs;
    }

    /**
     * Calls TradeIt to retrieve the positions for the linked account and synchronized the TradeIt position with the
     * positions in the database.
     * @param customerUuid
     * @param tradeItAccountUuid
     * @param linkedAccountUuid
     * @return
     * @throws TradeItAccountNotFoundException
     * @throws LinkedAccountNotFoundException
     * @throws TradeItAuthenticationException
     * @throws EntityVersionMismatchException
     * @throws VersionedEntityNotFoundException
     * @throws TradeItAPIException
     */
    private List<StockPositionDTO> synchronizePositions( final UUID customerUuid,
                                                         final UUID tradeItAccountUuid,
                                                         final UUID linkedAccountUuid )
        throws TradeItAccountNotFoundException,
               LinkedAccountNotFoundException,
               TradeItAPIException,
               EntityVersionMismatchException,
               VersionedEntityNotFoundException
    {
        final String methodName = "synchronizePositions";
        logMethodBegin( methodName, customerUuid, tradeItAccountUuid, linkedAccountUuid );
        TradeItAccountEntity tradeItAccountEntity = null;
        try
        {
            tradeItAccountEntity = this.tradeItAccountEntityService
                .getEntity( tradeItAccountUuid );
        }
        catch( VersionedEntityNotFoundException e )
        {
            throw new TradeItAccountNotFoundException( tradeItAccountUuid, e );
        }
        final LinkedAccountEntity linkedAccountEntity = this.linkedAccountEntityService
                                                            .getLinkedAccountEntity( linkedAccountUuid );
        /*
         * Call to TradeIt to get the positions.
         */
        final GetPositionsAPIResult getPositionsAPIResult = this.tradeItService
                                                                .getPositions( tradeItAccountEntity,
                                                                               linkedAccountEntity );
        List<StockPositionDTO> stockPositionList = new ArrayList<>();
        if ( getPositionsAPIResult.isSuccessful() )
        {
            /*
             * Get the positions stored in the database.
             */
            final List<StockPositionEntity> stockPositionEntities = this.stockPositionRepository
                .findAllByLinkedAccountUuid( linkedAccountUuid );

            /*
             * Compare the positions returned from TradeIt with the contents of the database and make database updated
             * based on the comparison results.  This is an asynchronous calls so that we don't make the user wait for
             * the result as TradeIt is the source of truth concerning the positions the user has with the linked account.
             */
            final StockPositionComparator stockPositionComparator = new StockPositionComparator( this );
            stockPositionComparator
                .comparePositions( linkedAccountEntity, stockPositionEntities, getPositionsAPIResult );

            /*
             * need to update/insert into the database and get a list of entities back and then convert them to DTOs.
             */
            this.createPositionDTOList( customerUuid, tradeItAccountUuid, linkedAccountUuid, getPositionsAPIResult );
            this.setStockPrices( stockPositionList );
        }
        else
        {
            throw new TradeItAPIException( getPositionsAPIResult );
        }
        logMethodEnd( methodName, stockPositionList.size() + " positions" );
        return stockPositionList;
    }

    /**
     * Extracts the position results from {@code getPositionsAPIResult}
     * @param getPositionsAPIResult
     */
    private List<StockPositionDTO> createPositionDTOList( final UUID customerUuid,
                                                          final UUID tradeItAccountUuid,
                                                          final UUID linkedAccountUuid,
                                                          final GetPositionsAPIResult getPositionsAPIResult )
    {
        final List<StockPositionDTO> stockPositionDTOList = new ArrayList<>();
        if ( getPositionsAPIResult.getPositions() != null )
        {
            for ( final TradeItPosition tradeItPosition : getPositionsAPIResult.getPositions() )
            {
                final StockPositionDTO stockPositionDTO = this.context.getBean( StockPositionDTO.class );
                stockPositionDTO.setResults( tradeItPosition );
                stockPositionDTO.setCustomerUuid( customerUuid );
                stockPositionDTO.setTradeItAccountId( tradeItAccountUuid);
                stockPositionDTO.setLinkedAccountId( linkedAccountUuid );
                stockPositionDTOList.add( stockPositionDTO );
            }
        }
        return stockPositionDTOList;
    }

    @Override
    protected StockPositionDTO createDTO()
    {
        return this.context.getBean( StockPositionDTO.class );
    }

    @Override
    protected StockPositionEntity createEntity()
    {
        return this.context.getBean( StockPositionEntity.class );
    }

    @Override
    protected StockPositionRepository getRepository()
    {
        return stockPositionRepository;
    }

    @Autowired
    public void setStockPositionRepository( final StockPositionRepository stockPositionRepository )
    {
        this.stockPositionRepository = stockPositionRepository;
    }

    @Autowired
    public void setTradeItService( final TradeItService tradeItService )
    {
        this.tradeItService = tradeItService;
    }

    @Autowired
    public void setTradeItAccountEntityService( final TradeItAccountEntityService tradeItAccountEntityService )
    {
        this.tradeItAccountEntityService = tradeItAccountEntityService;
    }

    @Autowired
    public void setLinkedAccountEntityService( final LinkedAccountEntityService linkedAccountEntityService )
    {
        this.linkedAccountEntityService = linkedAccountEntityService;
    }
}