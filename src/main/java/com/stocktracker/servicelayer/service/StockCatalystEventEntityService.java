package com.stocktracker.servicelayer.service;

import com.stocktracker.common.JSONDateConverter;
import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
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
public class StockCatalystEventEntityService extends VersionedEntityService<Integer,
                                                                            StockCatalystEventEntity,
                                                                            StockCatalystEventDTO,
                                                                            StockCatalystEventRepository>
    implements MyLogger
{
    private StockCatalystEventRepository stockCatalystEventRepository;
    private StockCompanyEntityService stockCompanyEntityService;

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
     * @throws VersionedEntityNotFoundException
     */
    public StockCatalystEventDTO getStockCatalystEvent( @NotNull final Integer stockCatalystEventId )
        throws VersionedEntityNotFoundException
    {
        final String methodName = "getStockCatalystEvent";
        logMethodBegin( methodName, stockCatalystEventId );
        Objects.requireNonNull( stockCatalystEventId, "stockCatalystEventId cannot be null" );
        StockCatalystEventEntity stockCatalystEventEntity = this.getEntity( stockCatalystEventId );
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
        throws EntityVersionMismatchException
    {
        final String methodName = "saveStockCatalystEvent";
        logMethodBegin( methodName, stockCatalystEventDTO );
        Objects.requireNonNull( stockCatalystEventDTO, "stockCatalystEventDTO cannot be null" );
        this.stockCompanyEntityService.checkStockTableEntry( stockCatalystEventDTO.getTickerSymbol() );
        final StockCatalystEventDTO returnStockCatalystEventDTO = this.saveDTO( stockCatalystEventDTO );
        logMethodEnd( methodName, returnStockCatalystEventDTO );
        return returnStockCatalystEventDTO;
    }

    @Override
    protected StockCatalystEventDTO entityToDTO( final StockCatalystEventEntity stockCatalystEventEntity )
    {
        StockCatalystEventDTO stockCatalystEventDTO = super.entityToDTO( stockCatalystEventEntity );
        if ( stockCatalystEventEntity.getCatalystDate() != null )
        {
            stockCatalystEventDTO.setCatalystDate( JSONDateConverter.toY4MMDD( stockCatalystEventEntity.getCatalystDate() ));
        }
        return stockCatalystEventDTO;
    }

    @Override
    protected StockCatalystEventDTO createDTO()
    {
        return this.context.getBean( StockCatalystEventDTO.class );
    }

    @Override
    protected StockCatalystEventEntity dtoToEntity( final StockCatalystEventDTO stockCatalystEventDTO )
    {
        Objects.requireNonNull( stockCatalystEventDTO );
        StockCatalystEventEntity stockCatalystEventEntity = this.createEntity();
        BeanUtils.copyProperties( stockCatalystEventDTO, stockCatalystEventEntity );
        if ( stockCatalystEventEntity.getCatalystDate() != null )
        {
            stockCatalystEventEntity.setCatalystDate( JSONDateConverter.toTimestamp( stockCatalystEventDTO.getCatalystDate() ) );
        }
        return stockCatalystEventEntity;
    }

    @Override
    protected StockCatalystEventEntity createEntity()
    {
        return this.context.getBean( StockCatalystEventEntity.class );
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
    public void setStockCompanyEntityService( final StockCompanyEntityService stockCompanyEntityService )
    {
        this.stockCompanyEntityService = stockCompanyEntityService;
    }

}
