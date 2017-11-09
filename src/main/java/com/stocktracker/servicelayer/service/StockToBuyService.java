package com.stocktracker.servicelayer.service;

import com.stocktracker.common.JSONDateConverter;
import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.entity.StockTagEntity;
import com.stocktracker.repositorylayer.entity.StockToBuyEntity;
import com.stocktracker.repositorylayer.repository.StockToBuyRepository;
import com.stocktracker.servicelayer.service.stockinformationprovider.StockQuoteFetchMode;
import com.stocktracker.weblayer.dto.StockToBuyDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class StockToBuyService extends BaseService<StockToBuyEntity, StockToBuyDTO> implements MyLogger
{
    private StockToBuyRepository stockToBuyRepository;
    private StockService stockService;
    private StockTagService stockTagService;
    private StockNoteSourceService stockNoteSourceService;

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
        stockToBuyDTOs.forEach( stockToBuyDTO ->  this.stockService.setStockQuoteInformation( stockToBuyDTO,
                                                                                              StockQuoteFetchMode.ASYNCHRONOUS ));
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
        this.stockService.setStockQuoteInformation( stockToBuyDTO, StockQuoteFetchMode.ASYNCHRONOUS );
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
        this.stockNoteSourceService.checkForNewSource( stockToBuyDTO.getCustomerId(), stockToBuyEntity, stockToBuyDTO );
        /*
         * The stock price needs to be set the first time as it records the stock price when the record was created.
         */
        if ( stockToBuyEntity.getStockPriceWhenCreated() == null )
        {
            stockToBuyEntity.setStockPriceWhenCreated( this.stockService.getStockPrice( stockToBuyEntity.getTickerSymbol() ) );
        }
        /*
         * The create date is set by a trigger, but since this record might not be immediately inserted, set a date
         * now so it shows in the table for the user.
         */
        if ( stockToBuyEntity.getCreateDate() == null )
        {
            stockToBuyEntity.setCreateDate( new Timestamp( System.currentTimeMillis() ) );
        }
        stockToBuyEntity = this.stockToBuyRepository.save( stockToBuyEntity );
        this.stockTagService.saveStockTags( stockToBuyEntity.getCustomerId(),
                                            stockToBuyEntity.getTickerSymbol(),
                                            StockTagEntity.StockTagReferenceType.STOCK_TO_BUY,
                                            stockToBuyEntity.getId(),
                                            stockToBuyDTO.getTags() );
        logDebug( methodName, "saved {0}", stockToBuyEntity );
        StockToBuyDTO returnStockToBuyDTO = this.entityToDTO( stockToBuyEntity );
        this.stockService.setStockQuoteInformation( returnStockToBuyDTO, StockQuoteFetchMode.ASYNCHRONOUS );
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
        this.stockService.setStockQuoteInformation( stockToBuyDTO, StockQuoteFetchMode.ASYNCHRONOUS );
        stockToBuyDTO.setTagsArray( this.stockTagService.findStockTags( stockToBuyDTO.getCustomerId(),
                                                                        StockTagEntity.StockTagReferenceType.STOCK_TO_BUY,
                                                                        stockToBuyDTO.getId() ) );
        stockToBuyDTO.setCreateDate( JSONDateConverter.toString( stockToBuyEntity.getCreateDate() ) );
        stockToBuyDTO.setBuyAfterDate( JSONDateConverter.toString( stockToBuyEntity.getBuyAfterDate() ) );
        if ( stockToBuyEntity.getStockNoteSourceByNotesSourceId() != null )
        {
            stockToBuyDTO.setNotesSourceName( stockToBuyEntity.getStockNoteSourceByNotesSourceId().getName() );
            stockToBuyDTO.setNotesSourceId( stockToBuyEntity.getStockNoteSourceByNotesSourceId().getId() );
        }
        return stockToBuyDTO;
    }

    @Override
    protected StockToBuyEntity dtoToEntity( final StockToBuyDTO stockToBuyDTO )
    {
        Objects.requireNonNull( stockToBuyDTO );
        StockToBuyEntity stockToBuyEntity = StockToBuyEntity.newInstance();
        BeanUtils.copyProperties( stockToBuyDTO, stockToBuyEntity );
        if ( stockToBuyDTO.getBuyAfterDate() != null )
        {
            stockToBuyEntity.setBuyAfterDate( JSONDateConverter.toTimestamp( stockToBuyDTO.getBuyAfterDate() ));
        }
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

    @Autowired
    public void setStockTagService( final StockTagService stockTagService )
    {
        this.stockTagService = stockTagService;
    }

    @Autowired
    public void setStockNoteSourceService( final StockNoteSourceService stockNoteSourceService )
    {
        this.stockNoteSourceService = stockNoteSourceService;
    }

}
