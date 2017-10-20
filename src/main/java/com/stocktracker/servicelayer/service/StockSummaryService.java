package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.entity.StockSummaryEntity;
import com.stocktracker.repositorylayer.repository.StockSummaryRepository;
import com.stocktracker.weblayer.dto.StockSummaryDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class StockSummaryService extends BaseService<StockSummaryEntity, StockSummaryDTO> implements MyLogger
{
    private StockSummaryRepository stockSummaryRepository;

    private StockService stockService;

    /**
     * Get the list of all stock summaries for the customer
     * @param customerId
     * @return
     */
    public List<StockSummaryDTO> getStockSummaries( @NotNull final Integer customerId )
    {
        final String methodName = "getStockSummaries";
        logMethodBegin( methodName, customerId );
        Objects.requireNonNull( customerId, "customerId cannot be null" );
        List<StockSummaryEntity> stockSummaryEntities = this.stockSummaryRepository.findByCustomerIdOrderByTickerSymbol( customerId );
        List<StockSummaryDTO> stockSummaryDTOs = this.entitiesToDTOs( stockSummaryEntities );
        stockSummaryDTOs.forEach( stockSummaryDTO ->  this.stockService.setStockInformation( stockSummaryDTO ));
        logMethodEnd( methodName, "Found " + stockSummaryEntities.size() + " summaries" );
        return stockSummaryDTOs;
    }

    /**
     * Get a single stock summary by stockSummaryId
     * @param stockSummaryId
     * @return StockSummaryDTO instance
     */
    public StockSummaryDTO getStockSummary( @NotNull final Integer stockSummaryId )
    {
        final String methodName = "getStockSummary";
        logMethodBegin( methodName, stockSummaryId );
        Objects.requireNonNull( stockSummaryId, "stockSummaryId cannot be null" );
        StockSummaryEntity stockSummaryEntity = this.stockSummaryRepository.findOne( stockSummaryId );
        StockSummaryDTO stockSummaryDTO = this.entityToDTO( stockSummaryEntity );
        logMethodEnd( methodName, stockSummaryDTO );
        return stockSummaryDTO;
    }

    /**
     * Add a new stock summary to the database
     * @param stockSummaryDTO
     * @return
     */
    public StockSummaryDTO saveStockSummary( @NotNull final StockSummaryDTO stockSummaryDTO )
    {
        final String methodName = "saveStockSummary";
        logMethodBegin( methodName, stockSummaryDTO );
        Objects.requireNonNull( stockSummaryDTO, "stockSummaryDTO cannot be null" );
        StockSummaryEntity stockSummaryEntity = this.dtoToEntity( stockSummaryDTO );
        stockSummaryEntity = this.stockSummaryRepository.save( stockSummaryEntity );
        logDebug( methodName, "saved {0}", stockSummaryEntity );
        StockSummaryDTO returnStockSummaryDTO = this.entityToDTO( stockSummaryEntity );
        logMethodEnd( methodName, returnStockSummaryDTO );
        return returnStockSummaryDTO;
    }

    /**
     * Deletes the stock summary from the database
     * @param stockSummaryId
     */
    public void deleteStockSummary( @NotNull final Integer stockSummaryId )
    {
        final String methodName = "deleteStockSummary";
        Objects.requireNonNull( stockSummaryId, "stockSummaryId cannot be null" );
        logMethodBegin( methodName, stockSummaryId );
        this.stockSummaryRepository.delete( stockSummaryId );
        logMethodEnd( methodName );
    }

    @Override
    protected StockSummaryDTO entityToDTO( final StockSummaryEntity stockEntity )
    {
        Objects.requireNonNull( stockEntity );
        StockSummaryDTO stockDTO = StockSummaryDTO.newInstance();
        BeanUtils.copyProperties( stockEntity, stockDTO );
        return stockDTO;
    }

    @Override
    protected StockSummaryEntity dtoToEntity( final StockSummaryDTO stockDTO )
    {
        Objects.requireNonNull( stockDTO );
        StockSummaryEntity stockEntity = StockSummaryEntity.newInstance();
        BeanUtils.copyProperties( stockDTO, stockEntity );
        return stockEntity;
    }

    @Autowired
    public void setStockSummaryRepository( final StockSummaryRepository stockSummaryRepository )
    {
        this.stockSummaryRepository = stockSummaryRepository;
    }

    @Autowired
    public void setStockService( final StockService stockService )
    {
        this.stockService = stockService;
    }

}
