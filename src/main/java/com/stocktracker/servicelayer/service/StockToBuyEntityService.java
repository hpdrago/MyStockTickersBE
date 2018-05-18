package com.stocktracker.servicelayer.service;

import com.stocktracker.common.JSONDateConverter;
import com.stocktracker.repositorylayer.entity.StockToBuyEntity;
import com.stocktracker.repositorylayer.repository.StockToBuyRepository;
import com.stocktracker.weblayer.dto.StockToBuyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

/**
 * This service class manages the transactions with the STOCK_TO_BUY table.
 */
@Service
public class StockToBuyEntityService extends StockInformationEntityService<StockToBuyEntity,
                                                                           StockToBuyDTO,
                                                                           StockToBuyRepository>
{
    private StockToBuyRepository stockToBuyRepository;

    /**
     * Searches for the one entry for the customer UUID and ticker symbol -- this is a unique combination.
     * @param customerUuid
     * @return
     */
    public StockToBuyDTO getByCustomerUuidAndTickerSymbol( @NotNull final UUID customerUuid,
                                                           @NotNull final String tickerSymbol )
    {
        final String methodName = "getStockToBuyListForCustomerUuid";
        logMethodBegin( methodName, customerUuid, tickerSymbol );
        Objects.requireNonNull( customerUuid, "customerUuid cannot be null" );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        final StockToBuyEntity stockToBuyEntity = this.stockToBuyRepository
                                                        .findByCustomerUuidAndTickerSymbol( customerUuid, tickerSymbol );
        StockToBuyDTO stockToBuyDTO = null;
        if ( stockToBuyEntity != null )
        {
            stockToBuyDTO = this.entityToDTO( stockToBuyEntity );
        }
        logMethodEnd( methodName, stockToBuyDTO );
        return stockToBuyDTO;
    }

    /**
     * Get the list of all stock to buy for the customer
     * @param customerUuid
     * @return
     */
    public Page<StockToBuyDTO> getStockToBuyListForCustomerUuid( final Pageable pageRequest,
                                                                 @NotNull final UUID customerUuid )
    {
        final String methodName = "getStockToBuyListForCustomerUuid";
        logMethodBegin( methodName, pageRequest, customerUuid );
        Objects.requireNonNull( customerUuid, "customerUuid cannot be null" );
        Page<StockToBuyEntity> stockToBuyEntities = this.stockToBuyRepository
                                                        .findByCustomerUuid( pageRequest, customerUuid );
        Page<StockToBuyDTO> stockToBuyDTOs = this.entitiesToDTOs( pageRequest, stockToBuyEntities );
        logMethodEnd( methodName, "Found " + stockToBuyEntities.getContent().size() + " to buy" );
        return stockToBuyDTOs;
    }

    public Page<StockToBuyDTO> getStockToBuyListForCustomerUuidAndTickerSymbol( @NotNull final Pageable pageRequest,
                                                                                @NotNull final UUID customerUuid,
                                                                                @NotNull final String tickerSymbol )
    {
        final String methodName = "getStockToBuyListForCustomerUuidAndTickerSymbol";
        logMethodBegin( methodName, pageRequest, customerUuid, tickerSymbol );
        Objects.requireNonNull( customerUuid, "customerUuid cannot be null" );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Page<StockToBuyEntity> stockToBuyEntities = this.stockToBuyRepository
                                                        .findByCustomerUuidAndTickerSymbol( pageRequest, customerUuid,
                                                                                            tickerSymbol );
        Page<StockToBuyDTO> stockToBuyDTOs = this.entitiesToDTOs( pageRequest, stockToBuyEntities );
        logMethodEnd( methodName, "Found " + stockToBuyEntities.getContent().size() + " to buy" );
        return stockToBuyDTOs;
    }

    @Override
    protected StockToBuyDTO createDTO()
    {
        return this.context.getBean( StockToBuyDTO.class );
    }

    /**
     * Converts the entity to a dto.
     * @param stockToBuyEntity
     * @return
     */
    @Override
    protected StockToBuyDTO entityToDTO( final StockToBuyEntity stockToBuyEntity )
    {
        Objects.requireNonNull( stockToBuyEntity );
        final StockToBuyDTO stockToBuyDTO = super.entityToDTO( stockToBuyEntity );
        stockToBuyDTO.setCompleted( stockToBuyEntity.getCompleted().equalsIgnoreCase( "Y" ) );
        //stockToBuyDTO.setCreateDate( JSONDateConverter.toY4MMDD( stockToBuyEntity.getCreateDate() ) );
        stockToBuyDTO.setBuyAfterDate( JSONDateConverter.toY4MMDD( stockToBuyEntity.getBuyAfterDate() ) );
        return stockToBuyDTO;
    }

    @Override
    protected StockToBuyEntity dtoToEntity( final StockToBuyDTO stockToBuyDTO )
    {
        Objects.requireNonNull( stockToBuyDTO );
        StockToBuyEntity stockToBuyEntity = super.dtoToEntity( stockToBuyDTO );
        if ( stockToBuyDTO.getBuyAfterDate() != null )
        {
            stockToBuyEntity.setBuyAfterDate( JSONDateConverter.toTimestamp( stockToBuyDTO.getBuyAfterDate() ));
        }
        stockToBuyEntity.setCompleted( stockToBuyDTO.isCompleted() ? "Y" : "N" );
        return stockToBuyEntity;
    }

    @Override
    protected StockToBuyEntity createEntity()
    {
        return this.context.getBean( StockToBuyEntity.class );
    }

    @Override
    protected StockToBuyRepository getRepository()
    {
        return this.stockToBuyRepository;
    }

    @Autowired
    public void setStockToBuyRepository( final StockToBuyRepository stockToBuyRepository )
    {
        this.stockToBuyRepository = stockToBuyRepository;
    }
}
