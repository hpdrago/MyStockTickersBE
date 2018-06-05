package com.stocktracker.servicelayer.service;

import com.stocktracker.common.JSONDateConverter;
import com.stocktracker.repositorylayer.entity.StockCatalystEventEntity;
import com.stocktracker.repositorylayer.repository.StockCatalystEventRepository;
import com.stocktracker.weblayer.dto.StockCatalystEventDTO;
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
public class StockCatalystEventEntityService extends StockInformationEntityService<StockCatalystEventEntity,
                                                                                   StockCatalystEventDTO,
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
    public Page<StockCatalystEventDTO> getStockCatalystEventsForCustomerUuid( @NotNull final Pageable pageRequest,
                                                                              @NotNull final UUID customerUuid )
    {
        final String methodName = "getStockCatalystEventsForCustomerUuid";
        logMethodBegin( methodName, pageRequest, customerUuid );
        Objects.requireNonNull( customerUuid, "customerId cannot be null" );
        Page<StockCatalystEventEntity> stockCatalystEventEntities = this.stockCatalystEventRepository
                                                                        .findByCustomerUuidOrderByTickerSymbol( pageRequest, customerUuid );
        Page<StockCatalystEventDTO> stockCatalystEventDTOs = this.entitiesToDTOs( pageRequest, stockCatalystEventEntities );
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
    public Page<StockCatalystEventDTO> getStockCatalystEventsForCustomerUuidAndTickerSymbol( @NotNull final Pageable pageRequest,
                                                                                             @NotNull final UUID customerUuid,
                                                                                             @NotNull final String tickerSymbol )
    {
        final String methodName = "getStockCatalystEventsForCustomerUuidAndTickerSymbol";
        logMethodBegin( methodName, pageRequest, customerUuid, tickerSymbol );
        Objects.requireNonNull( customerUuid, "customerId cannot be null" );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Page<StockCatalystEventEntity> stockCatalystEventEntities = this.stockCatalystEventRepository
            .findByCustomerUuidAndTickerSymbolOrderByTickerSymbol( pageRequest, customerUuid, tickerSymbol );
        Page<StockCatalystEventDTO> stockCatalystEventDTOs = this.entitiesToDTOs( pageRequest, stockCatalystEventEntities );
        logDebug( methodName, "stockCatalystEventList: {0}", stockCatalystEventDTOs );
        logMethodEnd( methodName, "Found " + stockCatalystEventEntities.getContent().size() + " catalyst events" );
        return stockCatalystEventDTOs;
    }

    @Override
    public StockCatalystEventDTO entityToDTO( final StockCatalystEventEntity stockCatalystEventEntity )
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
