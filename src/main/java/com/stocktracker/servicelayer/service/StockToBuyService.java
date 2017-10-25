package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.entity.StockToBuyEntity;
import com.stocktracker.repositorylayer.repository.StockToBuyRepository;
import com.stocktracker.weblayer.dto.StockToBuyDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class StockToBuyService extends BaseService<StockToBuyEntity, StockToBuyDTO> implements MyLogger
{
    private StockToBuyRepository stockToBuyRepository;

    private StockService stockService;

    /**
     * Get the list of all stock summaries for the customer
     * @param customerId
     * @return
     */
    public List<StockToBuyDTO> getStockToBuyList( @NotNull final Integer customerId )
    {
        final String methodName = "getStockToBuyList";
        logMethodBegin( methodName, customerId );
        Objects.requireNonNull( customerId, "customerId cannot be null" );
        List<StockToBuyEntity> stockToBuyEntities = this.stockToBuyRepository
            .findByCustomerIdOrderByTickerSymbol( customerId );
        List<StockToBuyDTO> stockToBuyDTOs = this.entitiesToDTOs( stockToBuyEntities );
        stockToBuyDTOs.forEach( stockToBuyDTO ->  this.stockService.setStockInformation( stockToBuyDTO ));
        logDebug( methodName, "stockToBuyList: {0}", stockToBuyDTOs );
        logMethodEnd( methodName, "Found " + stockToBuyEntities.size() + " summaries" );
        return stockToBuyDTOs;
    }

    /**
     * Get a single stock toBuy by stockToBuyId
     * @param stockToBuyId
     * @return StockToBuyDTO instance
     */
    public StockToBuyDTO getStockToBuy( @NotNull final Integer stockToBuyId )
    {
        final String methodName = "getStockToBuy";
        logMethodBegin( methodName, stockToBuyId );
        Objects.requireNonNull( stockToBuyId, "stockToBuyId cannot be null" );
        StockToBuyEntity stockToBuyEntity = this.stockToBuyRepository.findOne( stockToBuyId );
        StockToBuyDTO stockToBuyDTO = this.entityToDTO( stockToBuyEntity );
        this.stockService.setStockInformation( stockToBuyDTO );
        logMethodEnd( methodName, stockToBuyDTO );
        return stockToBuyDTO;
    }

    /**
     * Add a new stock toBuy to the database
     * @param stockToBuyDTO
     * @return
     */
    public StockToBuyDTO saveStockToBuy( @NotNull final StockToBuyDTO stockToBuyDTO )
    {
        final String methodName = "saveStockToBuy";
        logMethodBegin( methodName, stockToBuyDTO );
        Objects.requireNonNull( stockToBuyDTO, "stockToBuyDTO cannot be null" );
        StockToBuyEntity stockToBuyEntity = this.dtoToEntity( stockToBuyDTO );
        stockToBuyEntity = this.stockToBuyRepository.save( stockToBuyEntity );
        logDebug( methodName, "saved {0}", stockToBuyEntity );
        StockToBuyDTO returnStockToBuyDTO = this.entityToDTO( stockToBuyEntity );
        this.stockService.setStockInformation( returnStockToBuyDTO );
        logMethodEnd( methodName, returnStockToBuyDTO );
        return returnStockToBuyDTO;
    }

    /**
     * Deletes the stock toBuy from the database
     * @param stockToBuyId
     */
    public void deleteStockToBuy( @NotNull final Integer stockToBuyId )
    {
        final String methodName = "deleteStockToBuy";
        Objects.requireNonNull( stockToBuyId, "stockToBuyId cannot be null" );
        logMethodBegin( methodName, stockToBuyId );
        this.stockToBuyRepository.delete( stockToBuyId );
        logMethodEnd( methodName );
    }

    @Override
    protected StockToBuyDTO entityToDTO( final StockToBuyEntity stockToBuyEntity )
    {
        Objects.requireNonNull( stockToBuyEntity );
        StockToBuyDTO stockToBuyDTO = StockToBuyDTO.newInstance();
        BeanUtils.copyProperties( stockToBuyEntity, stockToBuyDTO );
        this.stockService.setStockInformation( stockToBuyDTO );
        return stockToBuyDTO;
    }

    @Override
    protected StockToBuyEntity dtoToEntity( final StockToBuyDTO stockToBuyDTO )
    {
        Objects.requireNonNull( stockToBuyDTO );
        StockToBuyEntity stockToBuyEntity = StockToBuyEntity.newInstance();
        BeanUtils.copyProperties( stockToBuyDTO, stockToBuyEntity );
        return stockToBuyEntity;
    }

    @Autowired
    public void setStockToBuyRepository( final StockToBuyRepository stockToBuyRepository )
    {
        this.stockToBuyRepository = stockToBuyRepository;
    }

    @Autowired
    public void setStockService( final StockService stockService )
    {
        this.stockService = stockService;
    }

}
