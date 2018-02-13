package com.stocktracker.servicelayer.service;

import com.stocktracker.common.JSONDateConverter;
import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockQuoteUnavailableException;
import com.stocktracker.common.exceptions.StockToBuyNoteFoundException;
import com.stocktracker.repositorylayer.entity.StockNoteSourceEntity;
import com.stocktracker.repositorylayer.entity.StockTagEntity;
import com.stocktracker.repositorylayer.entity.StockToBuyEntity;
import com.stocktracker.repositorylayer.repository.StockToBuyRepository;
import com.stocktracker.servicelayer.stockinformationprovider.StockQuoteFetchMode;
import com.stocktracker.weblayer.dto.StockToBuyDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Objects;

@Service
@Transactional
public class StockToBuyEntityService extends BaseStockQuoteContainerEntityService<Integer,
                                                                                  StockToBuyEntity,
                                                                                  StockToBuyDTO,
                                                                                  StockToBuyRepository>
    implements MyLogger
{
    private StockToBuyRepository stockToBuyRepository;
    private StockTagService stockTagService;
    private StockEntityService stockService;
    private StockNoteSourceEntityService stockNoteSourceService;

    /**
     * Get the list of all stock to buy for the customer
     * @param customerId
     * @return
     */
    public Page<StockToBuyDTO> getStockToBuyListForCustomerId( final Pageable pageRequest,
                                                               @NotNull final Integer customerId )
    {
        final String methodName = "getStockToBuyListForCustomerId";
        logMethodBegin( methodName, customerId );
        Objects.requireNonNull( customerId, "customerId cannot be null" );
        Page<StockToBuyEntity> stockToBuyEntities = this.stockToBuyRepository.findByCustomerId( pageRequest, customerId );
        Page<StockToBuyDTO> stockToBuyDTOs = this.entitiesToDTOs( pageRequest, stockToBuyEntities );
        getStockQuotes( stockToBuyDTOs );
        logDebug( methodName, "stockToBuyList: {0}", stockToBuyDTOs );
        logMethodEnd( methodName, "Found " + stockToBuyEntities.getContent().size() + " to buy" );
        return stockToBuyDTOs;
    }

    /**
     * Get the list of all stock to buy for the customer and the ticker symbol
     * @param customerId
     * @return
     */
    public Page<StockToBuyDTO> getStockToBuyListForCustomerIdAndTickerSymbol( @NotNull final Pageable pageRequest,
                                                                              @NotNull final Integer customerId,
                                                                              @NotNull final String tickerSymbol )
    {
        final String methodName = "getStockToBuyListForCustomerIdAndTickerSymbol";
        logMethodBegin( methodName, customerId, tickerSymbol );
        Objects.requireNonNull( customerId, "customerId cannot be null" );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        final Page<StockToBuyEntity> stockToBuyEntities = this.stockToBuyRepository
                                                              .findByCustomerIdAndTickerSymbol( pageRequest, customerId,
                                                                                                tickerSymbol );
        final Page<StockToBuyDTO> stockToBuyDTOs = this.entitiesToDTOs( pageRequest, stockToBuyEntities );
        this.getStockQuotes( stockToBuyDTOs );
        logDebug( methodName, "stockToBuyList: {0}", stockToBuyDTOs );
        logMethodEnd( methodName, "Found " + stockToBuyEntities.getContent().size() + " to buy" );
        return stockToBuyDTOs;
    }

    /**
     * Get a single stock toBuy by stockToBuyId
     * @param stockToBuyId
     * @return StockToBuyDTO instance
     */
    public StockToBuyDTO getStockToBuy( final int stockToBuyId )
        throws StockNotFoundException,
               StockQuoteUnavailableException,
               StockToBuyNoteFoundException
    {
        final String methodName = "getStockToBuy";
        logMethodBegin( methodName, stockToBuyId );
        Objects.requireNonNull( stockToBuyId, "stockToBuyId cannot be null" );
        final StockToBuyEntity stockToBuyEntity = this.stockToBuyRepository.findOne( stockToBuyId );
        if ( stockToBuyEntity == null )
        {
            throw new StockToBuyNoteFoundException( stockToBuyId );
        }
        final StockToBuyDTO stockToBuyDTO = this.entityToDTO( stockToBuyEntity );
        this.getStockQuoteService()
            .setStockQuoteInformation(  stockToBuyDTO, StockQuoteFetchMode.ASYNCHRONOUS );
        logMethodEnd( methodName, stockToBuyDTO );
        return stockToBuyDTO;
    }

    /**
     * Creates a new StockToBuy table entry
     * @param stockToBuyDTO
     * @return
     * @throws StockNotFoundException
     * @throws StockQuoteUnavailableException
     */
    public StockToBuyDTO createStockToBuy( final StockToBuyDTO stockToBuyDTO )
        throws StockNotFoundException,
               StockQuoteUnavailableException
    {
        final String methodName = "createStockToBuy";
        logMethodBegin( methodName, stockToBuyDTO );
        Objects.requireNonNull( stockToBuyDTO, "stockToBuyDTO cannot be null" );
        this.stockNoteSourceService.checkForNewSource( stockToBuyDTO );
        this.stockService.checkStockTableEntry( stockToBuyDTO.getTickerSymbol() );
        StockToBuyEntity stockToBuyEntity = this.dtoToEntity( stockToBuyDTO );
        /*
         * The stock price needs to be set the first time as it records the stock price when the record was created.
         */
        if ( stockToBuyEntity.getStockPriceWhenCreated() == null )
        {
            stockToBuyEntity.setStockPriceWhenCreated( this.getStockQuoteService()
                                                           .getStockPrice( stockToBuyEntity.getTickerSymbol() ) );
        }
        /*
         * The create date is set by a trigger, but since this record might not be immediately inserted, set a date
         * now so it shows in the table for the user.
         */
        if ( stockToBuyEntity.getCreateDate() == null )
        {
            stockToBuyEntity.setCreateDate( new Timestamp( System.currentTimeMillis() ) );
        }
        stockToBuyEntity.setVersion( 1 );
        stockToBuyEntity = this.stockToBuyRepository.save( stockToBuyEntity );
        this.stockTagService.saveStockTags( stockToBuyEntity.getCustomerId(),
                                            stockToBuyEntity.getTickerSymbol(),
                                            StockTagEntity.StockTagReferenceType.STOCK_TO_BUY,
                                            stockToBuyEntity.getId(),
                                            stockToBuyDTO.getTags() );
        logDebug( methodName, "saved {0}", stockToBuyEntity );
        final StockToBuyDTO returnStockToBuyDTO = this.entityToDTO( stockToBuyEntity );
        this.getStockQuoteService()
            .setStockQuoteInformation( returnStockToBuyDTO, StockQuoteFetchMode.ASYNCHRONOUS );
        logMethodEnd( methodName, returnStockToBuyDTO );
        return returnStockToBuyDTO;
    }

    /**
     * Add a new stock toBuy to the database
     * @param stockToBuyDTO
     * @return
     */
    public StockToBuyDTO saveStockToBuy( @NotNull final StockToBuyDTO stockToBuyDTO )
        throws StockNotFoundException,
               StockQuoteUnavailableException
    {
        final String methodName = "saveStockToBuy";
        logMethodBegin( methodName, stockToBuyDTO );
        Objects.requireNonNull( stockToBuyDTO, "stockToBuyDTO cannot be null" );
        this.stockNoteSourceService.checkForNewSource( stockToBuyDTO );
        this.stockService.checkStockTableEntry( stockToBuyDTO.getTickerSymbol() );
        StockToBuyDTO returnStockToBuyDTO = super.saveEntity( stockToBuyDTO );
        this.stockTagService.saveStockTags( stockToBuyDTO.getCustomerId(),
                                            stockToBuyDTO.getTickerSymbol(),
                                            StockTagEntity.StockTagReferenceType.STOCK_TO_BUY,
                                            stockToBuyDTO.getId(),
                                            stockToBuyDTO.getTags() );
        logDebug( methodName, "saved {0}", stockToBuyDTO );
        this.getStockQuoteService()
            .setStockQuoteInformation( returnStockToBuyDTO, StockQuoteFetchMode.ASYNCHRONOUS );
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

    /**
     * Converts the entity to a dto.
     * @param stockToBuyEntity
     * @return
     */
    @Override
    protected StockToBuyDTO entityToDTO( final StockToBuyEntity stockToBuyEntity )
    {
        final String methodName = "entityToDTO";
        Objects.requireNonNull( stockToBuyEntity );
        final StockToBuyDTO stockToBuyDTO = StockToBuyDTO.newInstance();
        BeanUtils.copyProperties( stockToBuyEntity, stockToBuyDTO );
        stockToBuyDTO.setCompleted( stockToBuyEntity.getCompleted().equalsIgnoreCase( "Y" ) );
        this.getStockQuote( stockToBuyDTO );
        stockToBuyDTO.setTagsArray( this.stockTagService.findStockTags( stockToBuyDTO.getCustomerId(),
                                                                        StockTagEntity.StockTagReferenceType.STOCK_TO_BUY,
                                                                        stockToBuyDTO.getId() ) );
        stockToBuyDTO.setCreateDate( JSONDateConverter.toY4MMDD( stockToBuyEntity.getCreateDate() ) );
        stockToBuyDTO.setBuyAfterDate( JSONDateConverter.toY4MMDD( stockToBuyEntity.getBuyAfterDate() ) );
        if ( stockToBuyEntity.getStockNoteSourceByNotesSourceId() != null )
        {
            stockToBuyDTO.setNotesSourceName( stockToBuyEntity.getStockNoteSourceByNotesSourceId().getName() );
            stockToBuyDTO.setNotesSourceId( stockToBuyEntity.getStockNoteSourceByNotesSourceId().getId() );
        }
        return stockToBuyDTO;
    }

    /**
     * Get the stock quotes for the stock to by list.
     * @param stockToBuyDTOs
     */
    private void getStockQuotes( final Page<StockToBuyDTO> stockToBuyDTOs )
    {
        final String methodName = "getStockQuotes";
        logMethodBegin( methodName );
        for ( StockToBuyDTO stockToBuyDTO : stockToBuyDTOs )
        {
            this.getStockQuote( stockToBuyDTO );
        }
        logMethodEnd( methodName );
    }

    /**
     * Get a single stock quote.
     * @param stockToBuyDTO
     */
    private void getStockQuote( final StockToBuyDTO stockToBuyDTO )
    {
        final String methodName = "getStockQuote";
        try
        {
            this.getStockQuoteService()
                .setStockQuoteInformation( stockToBuyDTO, StockQuoteFetchMode.ASYNCHRONOUS );
        }
        catch ( StockQuoteUnavailableException e )
        {
            logError( methodName, e );
        }
        catch ( StockNotFoundException e )
        {
            logError( methodName, e );
        }
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
        stockToBuyEntity.setCompleted( stockToBuyDTO.isCompleted() ? "Y" : "N" );
        if ( stockToBuyDTO.getNotesSourceId() != null &&
             stockToBuyDTO.getNotesSourceId() > 0 )
        {
            StockNoteSourceEntity stockNoteSourceEntity = this.stockNoteSourceService
                .getStockNoteSource( stockToBuyDTO.getNotesSourceId() );
            stockToBuyEntity.setStockNoteSourceByNotesSourceId( stockNoteSourceEntity );
        }
        return stockToBuyEntity;
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

    @Autowired
    public void setStockTagService( final StockTagService stockTagService )
    {
        this.stockTagService = stockTagService;
    }

    @Autowired
    public void setStockNoteSourceService( final StockNoteSourceEntityService stockNoteSourceService )
    {
        this.stockNoteSourceService = stockNoteSourceService;
    }

    @Autowired
    public void setStockService( final StockEntityService stockService )
    {
        this.stockService = stockService;
    }
}
