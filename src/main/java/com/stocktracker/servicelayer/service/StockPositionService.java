package com.stocktracker.servicelayer.service;

import com.stocktracker.common.exceptions.DuplicateEntityException;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.LinkedAccountNotFoundException;
import com.stocktracker.common.exceptions.TradeItAPIException;
import com.stocktracker.common.exceptions.TradeItAccountNotFoundException;
import com.stocktracker.common.exceptions.TradeItAuthenticationException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.repositorylayer.entity.StockPositionEntity;
import com.stocktracker.repositorylayer.entity.TradeItAccountEntity;
import com.stocktracker.repositorylayer.repository.StockPositionRepository;
import com.stocktracker.servicelayer.service.common.StockPositionComparator;
import com.stocktracker.servicelayer.tradeit.TradeItCodeEnum;
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
    @Autowired
    private StockPositionRepository stockPositionRepository;
    @Autowired
    private TradeItService tradeItService;
    @Autowired
    private TradeItAccountEntityService tradeItAccountEntityService;
    @Autowired
    private LinkedAccountEntityService linkedAccountEntityService;
    @Autowired
    private StockQuoteEntityService stockQuoteEntityService;

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
     * @throws DuplicateEntityException
     */
    public List<StockPositionDTO> getPositions( final UUID customerUuid,
                                                final UUID tradeItAccountUuid,
                                                final UUID linkedAccountUuid )
        throws LinkedAccountNotFoundException,
               TradeItAccountNotFoundException,
               TradeItAPIException,
               EntityVersionMismatchException,
               VersionedEntityNotFoundException,
               DuplicateEntityException
    {
        final String methodName = "getPositions";
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

        /*
         * For TradeIt account, we need to synchronize the database with TradeIt to get any changes that may have been
         * made.  For manual accounts, we just return what's in the database.
         */
        if ( tradeItAccountEntity.isTradeItAccount() )
        {
            this.synchronizePositions( customerUuid, tradeItAccountEntity, linkedAccountUuid );
        }
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
     * @param tradeItAccountEntity
     * @param linkedAccountUuid
     * @return
     * @throws LinkedAccountNotFoundException
     * @throws TradeItAuthenticationException
     * @throws EntityVersionMismatchException
     * @throws VersionedEntityNotFoundException
     * @throws TradeItAPIException
     * @throws DuplicateEntityException
     */
    private List<StockPositionDTO> synchronizePositions( final UUID customerUuid,
                                                         final TradeItAccountEntity tradeItAccountEntity,
                                                         final UUID linkedAccountUuid )
        throws LinkedAccountNotFoundException,
               TradeItAPIException,
               EntityVersionMismatchException,
               VersionedEntityNotFoundException,
               DuplicateEntityException
    {
        final String methodName = "synchronizePositions";
        logMethodBegin( methodName, customerUuid, tradeItAccountEntity.getUuid(), linkedAccountUuid );
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
            stockPositionComparator.comparePositions( linkedAccountEntity, stockPositionEntities, getPositionsAPIResult );

            /*
             * need to update/insert into the database and get a list of entities back and then convert them to DTOs.
             */
            this.createPositionDTOList( customerUuid, tradeItAccountEntity.getUuid(), linkedAccountUuid, getPositionsAPIResult );
            this.stockQuoteEntityService
                .setStockQuoteInformation( stockPositionList );
        }
        // Handle expiration issue -- just report to log since this method is reporting the same error as the account
        // authentication code is reporting the issue back to the user.
        else if ( getPositionsAPIResult.getCode() == TradeItCodeEnum.SESSION_EXPIRED_ERROR.getErrorNumber() ||
                  getPositionsAPIResult.getCode() == TradeItCodeEnum.TOKEN_INVALID_OR_EXPIRED_ERROR.getErrorNumber() )
        {
            logDebug( methodName, "Session or token has expired, cannot obtain positions at this time" );
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
    public List<StockPositionDTO> entitiesToDTOs( final Iterable<StockPositionEntity> stockPositions )
    {
        final List<StockPositionDTO> dtos = super.entitiesToDTOs( stockPositions );
        this.stockPriceQuoteService
            .setStockPriceQuotes( dtos );
        this.stockQuoteEntityService
            .setStockQuoteInformation( dtos );
        return dtos;
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
}