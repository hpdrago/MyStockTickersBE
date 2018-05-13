package com.stocktracker.servicelayer.service;

import com.stocktracker.common.JSONDateConverter;
import com.stocktracker.repositorylayer.entity.StockCatalystEventEntity;
import com.stocktracker.repositorylayer.repository.StockCatalystEventRepository;
import com.stocktracker.weblayer.dto.StockCatalystEventDTOEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

/**
 * This is the service class for the Stock Catalyst Event table/entity
 */
@Service
public class StockCatalystEventEntityService extends UuidEntityService<StockCatalystEventEntity,
    StockCatalystEventDTOEntity,
                                                                       StockCatalystEventRepository>
{
    private StockCatalystEventRepository stockCatalystEventRepository;

    /**
     * Get the list of all stock catalyst event for the customer
     *
     * @param pageRequest
     * @param customerUuid
     * @return
     */
    public Page<StockCatalystEventDTOEntity> getStockCatalystEventsForCustomerUuid( @NotNull final Pageable pageRequest,
                                                                                    @NotNull final UUID customerUuid )
    {
        final String methodName = "getStockCatalystEventsForCustomerUuid";
        logMethodBegin( methodName, pageRequest, customerUuid );
        Objects.requireNonNull( customerUuid, "customerId cannot be null" );
        Page<StockCatalystEventEntity> stockCatalystEventEntities = this.stockCatalystEventRepository
                                                                        .findByCustomerUuidOrderByTickerSymbol( pageRequest, customerUuid );
        Page<StockCatalystEventDTOEntity> stockCatalystEventDTOs = this.entitiesToDTOs( pageRequest, stockCatalystEventEntities );
        logDebug( methodName, "stockCatalystEventList: {0}", stockCatalystEventDTOs );
        logMethodEnd( methodName, "Found " + stockCatalystEventEntities.getContent().size() + " catalyst events" );
        return stockCatalystEventDTOs;
    }


    /**
     * Get the list of all stock catalyst event for the customer and ticker symbol
     * @param customerUuid
     * @param tickerSymbol
     * @return
     */
    public Page<StockCatalystEventDTOEntity> getStockCatalystEventsForCustomerUuidAndTickerSymbol( @NotNull final Pageable pageRequest,
                                                                                                   @NotNull final UUID customerUuid,
                                                                                                   @NotNull final String tickerSymbol )
    {
        final String methodName = "getStockCatalystEventsForCustomerUuidAndTickerSymbol";
        logMethodBegin( methodName, pageRequest, customerUuid, tickerSymbol );
        Objects.requireNonNull( customerUuid, "customerId cannot be null" );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Page<StockCatalystEventEntity> stockCatalystEventEntities = this.stockCatalystEventRepository
            .findByCustomerUuidAndTickerSymbolOrderByTickerSymbol( pageRequest, customerUuid, tickerSymbol );
        Page<StockCatalystEventDTOEntity> stockCatalystEventDTOs = this.entitiesToDTOs( pageRequest, stockCatalystEventEntities );
        logDebug( methodName, "stockCatalystEventList: {0}", stockCatalystEventDTOs );
        logMethodEnd( methodName, "Found " + stockCatalystEventEntities.getContent().size() + " catalyst events" );
        return stockCatalystEventDTOs;
    }

    @Override
    protected StockCatalystEventDTOEntity entityToDTO( final StockCatalystEventEntity stockCatalystEventEntity )
    {
        StockCatalystEventDTOEntity stockCatalystEventDTO = super.entityToDTO( stockCatalystEventEntity );
        if ( stockCatalystEventEntity.getCatalystDate() != null )
        {
            stockCatalystEventDTO.setCatalystDate( JSONDateConverter.toY4MMDD( stockCatalystEventEntity.getCatalystDate() ));
        }
        return stockCatalystEventDTO;
    }

    @Override
    protected StockCatalystEventDTOEntity createDTO()
    {
        return this.context.getBean( StockCatalystEventDTOEntity.class );
    }

    @Override
    protected StockCatalystEventEntity dtoToEntity( final StockCatalystEventDTOEntity stockCatalystEventDTO )
    {
        Objects.requireNonNull( stockCatalystEventDTO );
        final StockCatalystEventEntity stockCatalystEventEntity = super.dtoToEntity( stockCatalystEventDTO );
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
}
