package com.stocktracker.servicelayer.service;

import com.stocktracker.common.JSONDateConverter;
import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.entity.StockCatalystEventEntity;
import com.stocktracker.repositorylayer.repository.StockCatalystEventRepository;
import com.stocktracker.weblayer.dto.StockCatalystEventDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.util.List;
import java.util.Objects;

import static org.springframework.data.repository.init.ResourceReader.Type.JSON;

/**
 * This is the service class for the Stock Catalyst Event table/entity
 */
@Service
@Transactional
public class StockCatalystEventService extends BaseService<StockCatalystEventEntity, StockCatalystEventDTO> implements MyLogger
{
    private StockCatalystEventRepository stockCatalystEventRepository;

    @Autowired
    public void setStockService( final StockService stockService )
    {
        this.stockService = stockService;
    }

    private StockService stockService;

    /**
     * Get the list of all stock summaries for the customer
     * @param customerId
     * @return
     */
    public List<StockCatalystEventDTO> getStockCatalystEventList( @NotNull final Integer customerId )
    {
        final String methodName = "getStockCatalystEventList";
        logMethodBegin( methodName, customerId );
        Objects.requireNonNull( customerId, "customerId cannot be null" );
        List<StockCatalystEventEntity> stockCatalystEventEntities = this.stockCatalystEventRepository
            .findByCustomerIdOrderByTickerSymbol( customerId );
        List<StockCatalystEventDTO> stockCatalystEventDTOs = this.entitiesToDTOs( stockCatalystEventEntities );
        logDebug( methodName, "stockCatalystEventList: {0}", stockCatalystEventDTOs );
        logMethodEnd( methodName, "Found " + stockCatalystEventEntities.size() + " summaries" );
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
    {
        final String methodName = "saveStockCatalystEvent";
        logMethodBegin( methodName, stockCatalystEventDTO );
        Objects.requireNonNull( stockCatalystEventDTO, "stockCatalystEventDTO cannot be null" );
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
            try
            {
                stockCatalystEventDTO.setCatalystDate( JSONDateConverter.toString( stockCatalystEventEntity.getCatalystDate() ));
            }
            catch ( ParseException e )
            {
                e.printStackTrace();
            }
        }
        this.stockService.setCompanyName( stockCatalystEventDTO );
        return stockCatalystEventDTO;
    }

    @Override
    protected StockCatalystEventEntity dtoToEntity( final StockCatalystEventDTO stockCatalystEventDTO )
    {
        final String methodName = "dtoToEntity";
        Objects.requireNonNull( stockCatalystEventDTO );
        StockCatalystEventEntity stockCatalystEventEntity = StockCatalystEventEntity.newInstance();
        BeanUtils.copyProperties( stockCatalystEventDTO, stockCatalystEventEntity );
        if ( stockCatalystEventEntity.getCatalystDate() != null )
        {
            try
            {
                stockCatalystEventEntity.setCatalystDate( JSONDateConverter.toTimestamp( stockCatalystEventDTO.getCatalystDate() ) );
            }
            catch ( ParseException e )
            {
                e.printStackTrace();
            }
        }
        return stockCatalystEventEntity;
    }

    @Autowired
    public void setStockCatalystEventRepository( final StockCatalystEventRepository stockCatalystEventRepository )
    {
        this.stockCatalystEventRepository = stockCatalystEventRepository;
    }
}
