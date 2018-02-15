package com.stocktracker.servicelayer.service;

import com.stocktracker.common.exceptions.LinkedAccountNotFoundException;
import com.stocktracker.common.exceptions.TradeItAccountNotFoundException;
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
     */
    public List<StockPositionDTO> getPositions( final int customerId,
                                                final int tradeItAccountId,
                                                final int linkedAccountId )
        throws LinkedAccountNotFoundException,
               TradeItAccountNotFoundException
    {
        final String methodName = "getPositions";
        logMethodBegin( methodName, customerId, tradeItAccountId, linkedAccountId );
        final TradeItAccountEntity tradeItAccountEntity = this.tradeItAccountEntityService
            .getAccountEntity( customerId, tradeItAccountId );
        final LinkedAccountEntity linkedAccountEntity = this.linkedAccountEntityService
                                                            .getLinkedAccountEntity( customerId, linkedAccountId );
        final GetPositionsAPIResult getPositionsAPIResult = this.tradeItService
                                                                .getPositions( linkedAccountEntity.getAccountNumber(),
                                                                               tradeItAccountEntity.getAuthToken() );
        /*
         * need to update/insert into the database and get a list of entities back and then convert them to DTOs.
         */
        final List<StockPositionDTO> positions = new ArrayList<>();
        this.createPositionDTOList( customerId, tradeItAccountId, linkedAccountId, getPositionsAPIResult );
        logMethodEnd( methodName, "Returning " + positions.size() + " positions" );
        return positions;
    }

    /**
     * Exracts the position results from {@code getPositionsAPIResult}
     * @param getPositionsAPIResult
     */
    private void createPositionDTOList( final int customerId,
                                        final int tradeItAccountId,
                                        final int linkedAccountId,
                                        final GetPositionsAPIResult getPositionsAPIResult )
    {
        for ( final TradeItPosition tradeItPosition:  getPositionsAPIResult.getPositions() )
        {
            final StockPositionDTO stockPositionDTO = this.context.getBean( StockPositionDTO.class );
            stockPositionDTO.setResults( tradeItPosition );
            stockPositionDTO.setCustomerId( customerId );
            stockPositionDTO.setTradeItAccountId( tradeItAccountId );
            stockPositionDTO.setLinkedAccountId( linkedAccountId );
        }
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