package com.stocktracker.servicelayer.service;

import com.stocktracker.common.JSONDateConverter;
import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.entity.StockSummaryEntity;
import com.stocktracker.repositorylayer.repository.StockSummaryRepository;
import com.stocktracker.weblayer.dto.StockSummaryDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.text.ParseException;
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
        logDebug( methodName, "stockSummaries: {0}", stockSummaryDTOs );
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
        this.stockService.setStockInformation( stockSummaryDTO );
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
        this.stockService.setStockInformation( returnStockSummaryDTO );
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
    protected StockSummaryDTO entityToDTO( final StockSummaryEntity stockSummaryEntity )
    {
        final String methodName = "entityToDTO";
        Objects.requireNonNull( stockSummaryEntity );
        StockSummaryDTO stockSummaryDTO = StockSummaryDTO.newInstance();
        BeanUtils.copyProperties( stockSummaryEntity, stockSummaryDTO );
        performCalculations( stockSummaryDTO );
        stockSummaryDTO.setAnalystPriceDate( stockSummaryEntity.getAnalystPriceDate() );
        stockSummaryDTO.setAnalystSentimentDate( stockSummaryEntity.getAnalystSentimentDate() );
        stockSummaryDTO.setNextCatalystDate( stockSummaryEntity.getNextCatalystDate() );
        return stockSummaryDTO;
    }

    private void performCalculations( final StockSummaryDTO stockSummaryDTO )
    {
        if ( stockSummaryDTO.getLastPrice() != null &&
             stockSummaryDTO.getAvgAnalystPriceTarget() != null &&
             stockSummaryDTO.getAvgAnalystPriceTarget().intValue() > 0 )
        {
            stockSummaryDTO.setAvgUpsidePercent( stockSummaryDTO.getLastPrice()
                                                                .divide( stockSummaryDTO
                                                                             .getAvgAnalystPriceTarget() )
                                                                .multiply( new BigDecimal( 100 ) ) );
        }
    }

    @Override
    protected StockSummaryEntity dtoToEntity( final StockSummaryDTO stockSummaryDTO )
    {
        final String methodName = "dtoToEntity";
        Objects.requireNonNull( stockSummaryDTO );
        StockSummaryEntity stockSummaryEntity = StockSummaryEntity.newInstance();
        BeanUtils.copyProperties( stockSummaryDTO, stockSummaryEntity );
        try
        {
            if ( stockSummaryDTO.getAnalystPriceDate() != null )
            {
                stockSummaryEntity.setAnalystPriceDate(
                    JSONDateConverter.toTimestamp( stockSummaryDTO.getAnalystPriceDate() ) );
            }

            if ( stockSummaryDTO.getAnalystSentimentDate() != null )
            {
                stockSummaryEntity.setAnalystSentimentDate(
                    JSONDateConverter.toTimestamp( stockSummaryDTO.getAnalystSentimentDate() ) );
            }
            if ( stockSummaryDTO.getNextCatalystDate() != null )
            {
                stockSummaryEntity.setNextCatalystDate(
                    JSONDateConverter.toTimestamp( stockSummaryDTO.getNextCatalystDate() ) );
            }
        }
        catch ( ParseException e )
        {
            logError( methodName, e );
        }
        return stockSummaryEntity;
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
