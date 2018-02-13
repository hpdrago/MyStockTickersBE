package com.stocktracker.servicelayer.service;

import com.stocktracker.common.JSONDateConverter;
import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockQuoteUnavailableException;
import com.stocktracker.repositorylayer.entity.StockCatalystEventEntity;
import com.stocktracker.repositorylayer.repository.StockCatalystEventRepository;
import com.stocktracker.weblayer.dto.StockCatalystEventDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * This is the service class for the Stock Catalyst Event table/entity
 */
@Service
@Transactional
public class StockCatalystEventEntityService extends DMLEntityService<Integer,
                                                                      StockCatalystEventEntity,
                                                                      StockCatalystEventDTO,
                                                                      StockCatalystEventRepository>
    implements MyLogger
{
    private StockCatalystEventRepository stockCatalystEventRepository;
    private StockQuoteService stockQuoteService;
    private StockEntityService stockService;

    /**
     * Get the list of all stock catalyst event for the customer
     *
     * @param pageRequest
     * @param customerId
     * @return
     */
    public Page<StockCatalystEventDTO> getStockCatalystEventsForCustomerId( @NotNull final Pageable pageRequest,
                                                                            @NotNull final Integer customerId )
    {
        final String methodName = "getStockCatalystEventsForCustomerId";
        logMethodBegin( methodName, pageRequest, customerId );
        Objects.requireNonNull( customerId, "customerId cannot be null" );
        Page<StockCatalystEventEntity> stockCatalystEventEntities = this.stockCatalystEventRepository
            .findByCustomerIdOrderByTickerSymbol( pageRequest, customerId );
        Page<StockCatalystEventDTO> stockCatalystEventDTOs = this.entitiesToDTOs( pageRequest, stockCatalystEventEntities );
        logDebug( methodName, "stockCatalystEventList: {0}", stockCatalystEventDTOs );
        logMethodEnd( methodName, "Found " + stockCatalystEventEntities.getContent().size() + " catalyst events" );
        return stockCatalystEventDTOs;
    }


    /**
     * Get the list of all stock catalyst event for the customer and ticker symbol
     * @param customerId
     * @param tickerSymbol
     * @return
     */
    public Page<StockCatalystEventDTO> getStockCatalystEventsForCustomerIdAndTickerSymbol( @NotNull final Pageable pageRequest,
                                                                                           @NotNull final int customerId,
                                                                                           @NotNull final String tickerSymbol )
    {
        final String methodName = "getStockCatalystEventsForCustomerIdAndTickerSymbol";
        logMethodBegin( methodName, pageRequest, customerId, tickerSymbol );
        Objects.requireNonNull( customerId, "customerId cannot be null" );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Page<StockCatalystEventEntity> stockCatalystEventEntities = this.stockCatalystEventRepository
            .findByCustomerIdAndTickerSymbolOrderByTickerSymbol( pageRequest, customerId, tickerSymbol );
        Page<StockCatalystEventDTO> stockCatalystEventDTOs = this.entitiesToDTOs( pageRequest, stockCatalystEventEntities );
        logDebug( methodName, "stockCatalystEventList: {0}", stockCatalystEventDTOs );
        logMethodEnd( methodName, "Found " + stockCatalystEventEntities.getContent().size() + " catalyst events" );
        return stockCatalystEventDTOs;
    }

    /**
     * Get a single stock analytics by stockCatalystEventId
     * @param stockCatalystEventId
     * @return StockCatalystEventDTO instance
     */
    public StockCatalystEventDTO getStockCatalystEvent( @NotNull final Integer stockCatalystEventId )
    {
        final String methodName = "getStockCatalystEvent";
        logMethodBegin( methodName, stockCatalystEventId );
        Objects.requireNonNull( stockCatalystEventId, "stockCatalystEventId cannot be null" );
        StockCatalystEventEntity stockCatalystEventEntity = this.stockCatalystEventRepository.findOne( stockCatalystEventId );
        StockCatalystEventDTO stockCatalystEventDTO = this.entityToDTO( stockCatalystEventEntity );
        logMethodEnd( methodName, stockCatalystEventDTO );
        return stockCatalystEventDTO;
    }

    /**
     * Add a new stock analytics to the database
     * @param stockCatalystEventDTO
     * @return
     */
    public StockCatalystEventDTO saveStockCatalystEvent( @NotNull final StockCatalystEventDTO stockCatalystEventDTO )
        throws StockNotFoundException,
               StockQuoteUnavailableException
    {
        final String methodName = "saveStockCatalystEvent";
        logMethodBegin( methodName, stockCatalystEventDTO );
        Objects.requireNonNull( stockCatalystEventDTO, "stockCatalystEventDTO cannot be null" );
        this.stockService.checkStockTableEntry( stockCatalystEventDTO.getTickerSymbol() );
        StockCatalystEventEntity stockCatalystEventEntity = this.dtoToEntity( stockCatalystEventDTO );
        stockCatalystEventEntity = this.stockCatalystEventRepository.save( stockCatalystEventEntity );
        logDebug( methodName, "saved {0}", stockCatalystEventEntity );
        StockCatalystEventDTO returnStockCatalystEventDTO = this.entityToDTO( stockCatalystEventEntity );
        logMethodEnd( methodName, returnStockCatalystEventDTO );
        return returnStockCatalystEventDTO;
    }

    /**
     * Deletes the stock analytics from the database
     * @param stockCatalystEventId
     */
    public void deleteStockCatalystEvent( @NotNull final Integer stockCatalystEventId )
    {
        final String methodName = "deleteStockCatalystEvent";
        Objects.requireNonNull( stockCatalystEventId, "stockCatalystEventId cannot be null" );
        logMethodBegin( methodName, stockCatalystEventId );
        this.stockCatalystEventRepository.delete( stockCatalystEventId );
        logMethodEnd( methodName );
    }

    @Override
    protected StockCatalystEventDTO entityToDTO( final StockCatalystEventEntity stockCatalystEventEntity )
    {
        final String methodName = "entityToDTO";
        Objects.requireNonNull( stockCatalystEventEntity );
        StockCatalystEventDTO stockCatalystEventDTO = StockCatalystEventDTO.newInstance();
        BeanUtils.copyProperties( stockCatalystEventEntity, stockCatalystEventDTO );
        if ( stockCatalystEventEntity.getCatalystDate() != null )
        {
            stockCatalystEventDTO.setCatalystDate( JSONDateConverter.toY4MMDD( stockCatalystEventEntity.getCatalystDate() ));
        }
        try
        {
            this.stockQuoteService.setCompanyName( stockCatalystEventDTO );
        }
        catch ( StockNotFoundException e )
        {
            logError( methodName, e );
        }
        catch ( StockQuoteUnavailableException e )
        {
            logError( methodName, e );
        }
        return stockCatalystEventDTO;
    }

    @Override
    protected StockCatalystEventEntity dtoToEntity( final StockCatalystEventDTO stockCatalystEventDTO )
    {
        Objects.requireNonNull( stockCatalystEventDTO );
        StockCatalystEventEntity stockCatalystEventEntity = StockCatalystEventEntity.newInstance();
        BeanUtils.copyProperties( stockCatalystEventDTO, stockCatalystEventEntity );
        if ( stockCatalystEventEntity.getCatalystDate() != null )
        {
            stockCatalystEventEntity.setCatalystDate( JSONDateConverter.toTimestamp( stockCatalystEventDTO.getCatalystDate() ) );
        }
        return stockCatalystEventEntity;
    }

    @Override
    protected StockCatalystEventRepository getRepository()
    {
        return this.stockCatalystEventRepository;
    }

    @Autowired
    public void setStockCatalystEventRepository( final StockCatalystEventRepository stockCatalystEventRepository )
    {
        this.stockCatalystEventRepository = stockCatalystEventRepository;
    }

    @Autowired
    public void setStockQuoteService( final StockQuoteService stockQuoteService )
    {
        this.stockQuoteService = stockQuoteService;
    }

    @Autowired
    public void setStockService( final StockEntityService stockService )
    {
        this.stockService = stockService;
    }
}
