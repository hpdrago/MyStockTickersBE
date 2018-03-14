package com.stocktracker.servicelayer.service;

import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.LinkedAccountNotFoundException;
import com.stocktracker.common.exceptions.TradeItAccountNotFoundException;
import com.stocktracker.common.exceptions.TradeItAuthenticationException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.repositorylayer.entity.StockPositionEntity;
import com.stocktracker.repositorylayer.entity.TradeItAccountEntity;
import com.stocktracker.repositorylayer.repository.StockPositionRepository;
import com.stocktracker.servicelayer.tradeit.TradeItService;
import com.stocktracker.servicelayer.tradeit.apiresults.GetPositionsAPIResult;
import com.stocktracker.servicelayer.tradeit.types.TradeItPosition;
import com.stocktracker.weblayer.dto.StockPositionDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * This service class handles requests for StockPosition actions.
 */
@Service
public class StockPositionService extends StockQuoteContainerEntityService<Integer,
                                                                           StockPositionEntity,
                                                                           StockPositionDTO,
                                                                           StockPositionRepository>
{
    private StockPositionRepository stockPositionRepository;
    private TradeItService tradeItService;
    private TradeItAccountEntityService tradeItAccountEntityService;
    private LinkedAccountEntityService linkedAccountEntityService;

    /**
     * Get the positions for the linked account.
     * @param customerId
     * @param tradeItAccountId
     * @param linkedAccountId
     * @return List of positions.
     * @throws LinkedAccountNotFoundException
     * @throws TradeItAccountNotFoundException
     * @throws TradeItAuthenticationException
     * @throws EntityVersionMismatchException
     * @throws VersionedEntityNotFoundException
     */
    public List<StockPositionDTO> getPositions( final int customerId,
                                                final int tradeItAccountId,
                                                final int linkedAccountId )
        throws LinkedAccountNotFoundException,
               TradeItAccountNotFoundException,
               TradeItAuthenticationException,
               EntityVersionMismatchException,
               VersionedEntityNotFoundException
    {
        final String methodName = "getPositions";
        logMethodBegin( methodName, customerId, tradeItAccountId, linkedAccountId );
        this.synchronizePositions( customerId, tradeItAccountId, linkedAccountId ) ;
        final List<StockPositionEntity> stockPositionEntities = this.stockPositionRepository
                                                                    .findByLinkedAccountId( linkedAccountId );
        final List<StockPositionDTO> stockPositionDTOs = this.entitiesToDTOs( stockPositionEntities );
        stockPositionDTOs.forEach( stockPositionDTO ->
                                   {
                                       stockPositionDTO.setCustomerId( customerId );
                                       stockPositionDTO.setLinkedAccountId( linkedAccountId );
                                       stockPositionDTO.setTradeItAccountId( tradeItAccountId );
                                   });
        logMethodEnd( methodName, "Returning " + stockPositionDTOs.size() + " positions" );
        return stockPositionDTOs;
    }

    /**
     * Calls TradeIt to retrieve the positions for the linked account and synchronized the TradeIt position with the
     * positions in the database.
     * @param customerId
     * @param tradeItAccountId
     * @param linkedAccountId
     * @return
     * @throws TradeItAccountNotFoundException
     * @throws LinkedAccountNotFoundException
     * @throws TradeItAuthenticationException
     * @throws EntityVersionMismatchException
     * @throws VersionedEntityNotFoundException
     */
    private List<StockPositionDTO> synchronizePositions( final int customerId,
                                                         final int tradeItAccountId,
                                                         final int linkedAccountId )
        throws TradeItAccountNotFoundException,
               LinkedAccountNotFoundException,
               TradeItAuthenticationException,
               EntityVersionMismatchException,
               VersionedEntityNotFoundException
    {
        final String methodName = "synchronizePositions";
        logMethodBegin( methodName, customerId, tradeItAccountId, linkedAccountId );
        final TradeItAccountEntity tradeItAccountEntity = this.tradeItAccountEntityService
                                                              .getTradeItAccountEntity( customerId, tradeItAccountId );
        final LinkedAccountEntity linkedAccountEntity = this.linkedAccountEntityService
                                                            .getLinkedAccountEntity( customerId, linkedAccountId );
        /*
         * Call to TradeIt to get the positions.
         */
        final GetPositionsAPIResult getPositionsAPIResult = this.tradeItService
                                                                .getPositions( tradeItAccountEntity,
                                                                               linkedAccountEntity );

        /*
         * Get the positions stored in the database.
         */
        final List<StockPositionEntity> stockPositionEntities = this.stockPositionRepository
                                                                     .findAllByLinkedAccountId( linkedAccountId );

        /*
         * Compare the positions returned from TradeIt with the contents of the database and make database updated
         * based on the comparison results.  This is an asynchronous calls so that we don't make the user wait for
         * the result as TradeIt is the source of truth concerning the positions the user has with the linked account.
         */
        final StockPositionComparator stockPositionComparator = new StockPositionComparator( this );
        stockPositionComparator.comparePositions( linkedAccountEntity, stockPositionEntities, getPositionsAPIResult );

        /*
         * need to update/insert into the database and get a list of entities back and then convert them to DTOs.
         */
        final List<StockPositionDTO> stockPositionList = this.createPositionDTOList( customerId, tradeItAccountId,
                                                                                     linkedAccountId, getPositionsAPIResult );
        this.updateStockQuoteInformation( stockPositionList );
        logMethodEnd( methodName, stockPositionList.size() + " positions" );
        return stockPositionList;
    }

    /**
     * Extracts the position results from {@code getPositionsAPIResult}
     * @param getPositionsAPIResult
     */
    private List<StockPositionDTO> createPositionDTOList( final int customerId,
                                                          final int tradeItAccountId,
                                                          final int linkedAccountId,
                                                          final GetPositionsAPIResult getPositionsAPIResult )
    {
        final List<StockPositionDTO> stockPositionDTOList = new ArrayList<>();
        if ( getPositionsAPIResult.getPositions() != null )
        {
            for ( final TradeItPosition tradeItPosition : getPositionsAPIResult.getPositions() )
            {
                final StockPositionDTO stockPositionDTO = this.context.getBean( StockPositionDTO.class );
                stockPositionDTO.setResults( tradeItPosition );
                stockPositionDTO.setCustomerId( customerId );
                stockPositionDTO.setTradeItAccountId( tradeItAccountId );
                stockPositionDTO.setLinkedAccountId( linkedAccountId );
                stockPositionDTOList.add( stockPositionDTO );
            }
        }
        return stockPositionDTOList;
    }

    @Override
    protected StockPositionDTO entityToDTO( final StockPositionEntity entity )
    {
        final StockPositionDTO stockPositionDTO = this.context.getBean( StockPositionDTO.class );
        BeanUtils.copyProperties( entity, stockPositionDTO );
        return stockPositionDTO;
    }

    @Override
    protected StockPositionEntity dtoToEntity( final StockPositionDTO dto )
    {
        final StockPositionEntity entity = this.context.getBean( StockPositionEntity.class );
        return entity;
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