package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.entity.StockAnalyticsEntity;
import com.stocktracker.repositorylayer.repository.StockAnalyticsRepository;
import com.stocktracker.weblayer.dto.StockAnalyticsDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class StockAnalyticsService extends BaseService<StockAnalyticsEntity, StockAnalyticsDTO> implements MyLogger
{
    private StockAnalyticsRepository stockAnalyticsRepository;

    private StockService stockService;

    /**
     * Get the list of all stock summaries for the customer
     * @param customerId
     * @return
     */
    public List<StockAnalyticsDTO> getStockAnalyticsList( @NotNull final Integer customerId )
    {
        final String methodName = "getStockAnalyticsList";
        logMethodBegin( methodName, customerId );
        Objects.requireNonNull( customerId, "customerId cannot be null" );
        List<StockAnalyticsEntity> stockAnalyticsEntities = this.stockAnalyticsRepository
            .findByCustomerIdOrderByTickerSymbol( customerId );
        List<StockAnalyticsDTO> stockAnalyticsDTOs = this.entitiesToDTOs( stockAnalyticsEntities );
        logDebug( methodName, "stockAnalyticsList: {0}", stockAnalyticsDTOs );
        logMethodEnd( methodName, "Found " + stockAnalyticsEntities.size() + " summaries" );
        return stockAnalyticsDTOs;
    }

    /**
     * Get a single stock analytics by stockAnalyticsId
     * @param stockAnalyticsId
     * @return StockAnalyticsDTO instance
     */
    public StockAnalyticsDTO getStockAnalytics( @NotNull final Integer stockAnalyticsId )
    {
        final String methodName = "getStockAnalytics";
        logMethodBegin( methodName, stockAnalyticsId );
        Objects.requireNonNull( stockAnalyticsId, "stockAnalyticsId cannot be null" );
        StockAnalyticsEntity stockAnalyticsEntity = this.stockAnalyticsRepository.findOne( stockAnalyticsId );
        StockAnalyticsDTO stockAnalyticsDTO = this.entityToDTO( stockAnalyticsEntity );
        logMethodEnd( methodName, stockAnalyticsDTO );
        return stockAnalyticsDTO;
    }

    /**
     * Add a new stock analytics to the database
     * @param stockAnalyticsDTO
     * @return
     */
    public StockAnalyticsDTO saveStockAnalytics( @NotNull final StockAnalyticsDTO stockAnalyticsDTO )
    {
        final String methodName = "saveStockAnalytics";
        logMethodBegin( methodName, stockAnalyticsDTO );
        Objects.requireNonNull( stockAnalyticsDTO, "stockAnalyticsDTO cannot be null" );
        StockAnalyticsEntity stockAnalyticsEntity = this.dtoToEntity( stockAnalyticsDTO );
        /*
         * use saveAndFlush so that we can get the updated values from the row which might be changed with insert
         * or update triggers.
         */
        stockAnalyticsEntity = this.stockAnalyticsRepository.saveAndFlush( stockAnalyticsEntity );
        logDebug( methodName, "saved {0}", stockAnalyticsEntity );
        StockAnalyticsDTO returnStockAnalyticsDTO = this.entityToDTO( stockAnalyticsEntity );
        logMethodEnd( methodName, returnStockAnalyticsDTO );
        return returnStockAnalyticsDTO;
    }

    /**
     * Deletes the stock analytics from the database
     * @param stockAnalyticsId
     */
    public void deleteStockAnalytics( @NotNull final Integer stockAnalyticsId )
    {
        final String methodName = "deleteStockAnalytics";
        Objects.requireNonNull( stockAnalyticsId, "stockAnalyticsId cannot be null" );
        logMethodBegin( methodName, stockAnalyticsId );
        this.stockAnalyticsRepository.delete( stockAnalyticsId );
        logMethodEnd( methodName );
    }

    @Override
    protected StockAnalyticsDTO entityToDTO( final StockAnalyticsEntity stockAnalyticsEntity )
    {
        Objects.requireNonNull( stockAnalyticsEntity );
        StockAnalyticsDTO stockAnalyticsDTO = StockAnalyticsDTO.newInstance();
        BeanUtils.copyProperties( stockAnalyticsEntity, stockAnalyticsDTO );
        this.stockService.setStockInformation( stockAnalyticsDTO );
        performCalculations( stockAnalyticsDTO );
        stockAnalyticsDTO.setAnalystPriceDate( stockAnalyticsEntity.getAnalystPriceDate() );
        stockAnalyticsDTO.setAnalystSentimentDate( stockAnalyticsEntity.getAnalystSentimentDate() );
        return stockAnalyticsDTO;
    }

    private void performCalculations( final StockAnalyticsDTO stockAnalyticsDTO )
    {
        final String methodName = "performCalculations";
        logMethodBegin( methodName, stockAnalyticsDTO );
        if ( stockAnalyticsDTO.getLastPrice() != null &&
             stockAnalyticsDTO.getAvgAnalystPriceTarget() != null &&
             stockAnalyticsDTO.getAvgAnalystPriceTarget().floatValue() > 0.0 )
        {
            /*
             * 1 - (last price / avg target price)
             */
            stockAnalyticsDTO.setAvgUpsidePercent( new BigDecimal( 1 ).subtract((stockAnalyticsDTO.getLastPrice()
                                                                           .divide( stockAnalyticsDTO.getAvgAnalystPriceTarget() ))));
        }
        logMethodEnd( methodName, stockAnalyticsDTO.getAvgUpsidePercent() );
    }

    @Override
    protected StockAnalyticsEntity dtoToEntity( final StockAnalyticsDTO stockAnalyticsDTO )
    {
        final String methodName = "dtoToEntity";
        Objects.requireNonNull( stockAnalyticsDTO );
        StockAnalyticsEntity stockAnalyticsEntity = StockAnalyticsEntity.newInstance();
        BeanUtils.copyProperties( stockAnalyticsDTO, stockAnalyticsEntity );
        return stockAnalyticsEntity;
    }

    @Autowired
    public void setStockAnalyticsRepository( final StockAnalyticsRepository stockAnalyticsRepository )
    {
        this.stockAnalyticsRepository = stockAnalyticsRepository;
    }

    @Autowired
    public void setStockService( final StockService stockService )
    {
        this.stockService = stockService;
    }

}
