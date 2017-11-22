package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.entity.StockAnalystConsensusEntity;
import com.stocktracker.repositorylayer.repository.StockAnalystConsensusRepository;
import com.stocktracker.servicelayer.service.stockinformationprovider.StockQuoteFetchMode;
import com.stocktracker.weblayer.dto.StockAnalystConsensusDTO;
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
public class StockAnalystConsensusService extends BaseService<StockAnalystConsensusEntity, StockAnalystConsensusDTO> implements MyLogger
{
    private StockAnalystConsensusRepository stockAnalystConsensusRepository;
    private StockQuoteService stockQuoteService;

    /**
     * Get the list of all stock summaries for the customer
     * @param customerId
     * @return
     */
    public List<StockAnalystConsensusDTO> getStockAnalystConsensusList( @NotNull final Integer customerId )
    {
        final String methodName = "getStockAnalystConsensusList";
        logMethodBegin( methodName, customerId );
        Objects.requireNonNull( customerId, "customerId cannot be null" );
        List<StockAnalystConsensusEntity> stockAnalystConsensusEntities = this.stockAnalystConsensusRepository
            .findByCustomerIdOrderByTickerSymbol( customerId );
        List<StockAnalystConsensusDTO> stockAnalystConsensusDTOS = this.entitiesToDTOs( stockAnalystConsensusEntities );
        logDebug( methodName, "stockAnalystConsensusList: {0}", stockAnalystConsensusDTOS );
        logMethodEnd( methodName, "Found " + stockAnalystConsensusEntities.size() + " summaries" );
        return stockAnalystConsensusDTOS;
    }

    /**
     * Get a single stock analytics by stockAnalystConsensusId
     * @param stockAnalystConsensusId
     * @return StockAnalystConsensusDTO instance
     */
    public StockAnalystConsensusDTO getStockAnalystConsensus( @NotNull final Integer stockAnalystConsensusId )
    {
        final String methodName = "getStockAnalystConsensus";
        logMethodBegin( methodName, stockAnalystConsensusId );
        Objects.requireNonNull( stockAnalystConsensusId, "stockAnalystConsensusId cannot be null" );
        StockAnalystConsensusEntity stockAnalystConsensusEntity = this.stockAnalystConsensusRepository
            .findOne( stockAnalystConsensusId );
        StockAnalystConsensusDTO stockAnalystConsensusDTO = this.entityToDTO( stockAnalystConsensusEntity );
        logMethodEnd( methodName, stockAnalystConsensusDTO );
        return stockAnalystConsensusDTO;
    }

    /**
     * Add a new stock analytics to the database
     * @param stockAnalystConsensusDTO
     * @return
     */
    public StockAnalystConsensusDTO saveStockAnalystConsensus( @NotNull final StockAnalystConsensusDTO stockAnalystConsensusDTO )
    {
        final String methodName = "saveStockAnalystConsensus";
        logMethodBegin( methodName, stockAnalystConsensusDTO );
        Objects.requireNonNull( stockAnalystConsensusDTO, "stockAnalystConsensusDTO cannot be null" );
        StockAnalystConsensusEntity stockAnalystConsensusEntity = this.dtoToEntity( stockAnalystConsensusDTO );
        /*
         * use saveAndFlush so that we can get the updated values from the row which might be changed with insert
         * or update triggers.
         */
        stockAnalystConsensusEntity = this.stockAnalystConsensusRepository.saveAndFlush( stockAnalystConsensusEntity );
        logDebug( methodName, "saved {0}", stockAnalystConsensusEntity );
        StockAnalystConsensusDTO returnStockAnalystConsensusDTO = this.entityToDTO( stockAnalystConsensusEntity );
        logMethodEnd( methodName, returnStockAnalystConsensusDTO );
        return returnStockAnalystConsensusDTO;
    }

    /**
     * Deletes the stock analytics from the database
     * @param stockAnalystConsensusId
     */
    public void deleteStockAnalystConsensus( @NotNull final Integer stockAnalystConsensusId )
    {
        final String methodName = "deleteStockAnalystConsensus";
        Objects.requireNonNull( stockAnalystConsensusId, "stockAnalystConsensusId cannot be null" );
        logMethodBegin( methodName, stockAnalystConsensusId );
        this.stockAnalystConsensusRepository.delete( stockAnalystConsensusId );
        logMethodEnd( methodName );
    }

    @Override
    protected StockAnalystConsensusDTO entityToDTO( final StockAnalystConsensusEntity stockAnalystConsensusEntity )
    {
        Objects.requireNonNull( stockAnalystConsensusEntity );
        StockAnalystConsensusDTO stockAnalystConsensusDTO = StockAnalystConsensusDTO.newInstance();
        BeanUtils.copyProperties( stockAnalystConsensusEntity, stockAnalystConsensusDTO );
        this.stockQuoteService.setStockQuoteInformation( stockAnalystConsensusDTO, StockQuoteFetchMode.ASYNCHRONOUS );
        performCalculations( stockAnalystConsensusDTO );
        stockAnalystConsensusDTO.setAnalystPriceDate( stockAnalystConsensusEntity.getAnalystPriceDate() );
        stockAnalystConsensusDTO.setAnalystSentimentDate( stockAnalystConsensusEntity.getAnalystSentimentDate() );
        return stockAnalystConsensusDTO;
    }

    private void performCalculations( final StockAnalystConsensusDTO stockAnalystConsensusDTO )
    {
        final String methodName = "performCalculations";
        logMethodBegin( methodName, stockAnalystConsensusDTO );
        if ( stockAnalystConsensusDTO.getLastPrice() != null &&
             stockAnalystConsensusDTO.getAvgAnalystPriceTarget() != null &&
             stockAnalystConsensusDTO.getAvgAnalystPriceTarget().floatValue() > 0.0 )
        {
            /*
             * 1 - (last price / avg target price)
             */
            stockAnalystConsensusDTO.setAvgUpsidePercent( new BigDecimal( 1 ).subtract( (stockAnalystConsensusDTO.getLastPrice()
                                                                                                                 .divide( stockAnalystConsensusDTO
                                                                                                                              .getAvgAnalystPriceTarget() ))));
        }
        logMethodEnd( methodName, stockAnalystConsensusDTO.getAvgUpsidePercent() );
    }

    @Override
    protected StockAnalystConsensusEntity dtoToEntity( final StockAnalystConsensusDTO stockAnalystConsensusDTO )
    {
        final String methodName = "dtoToEntity";
        Objects.requireNonNull( stockAnalystConsensusDTO );
        StockAnalystConsensusEntity stockAnalystConsensusEntity = StockAnalystConsensusEntity.newInstance();
        BeanUtils.copyProperties( stockAnalystConsensusDTO, stockAnalystConsensusEntity );
        return stockAnalystConsensusEntity;
    }

    @Autowired
    public void setStockAnalystConsensusRepository( final StockAnalystConsensusRepository stockAnalystConsensusRepository )
    {
        this.stockAnalystConsensusRepository = stockAnalystConsensusRepository;
    }

    @Autowired
    public void setStockQuoteService( final StockQuoteService stockQuoteService )
    {
        this.stockQuoteService = stockQuoteService;
    }

}
