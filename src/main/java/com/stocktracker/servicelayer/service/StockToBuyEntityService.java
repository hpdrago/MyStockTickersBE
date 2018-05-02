package com.stocktracker.servicelayer.service;

import com.fasterxml.uuid.impl.UUIDUtil;
import com.stocktracker.common.JSONDateConverter;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.StockCompanyNotFoundException;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockToBuyNotFoundException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.entity.StockNoteSourceEntity;
import com.stocktracker.repositorylayer.entity.StockTagEntity;
import com.stocktracker.repositorylayer.entity.StockToBuyEntity;
import com.stocktracker.repositorylayer.repository.StockToBuyRepository;
import com.stocktracker.servicelayer.stockinformationprovider.StockPriceFetchMode;
import com.stocktracker.weblayer.dto.StockToBuyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Service
//@Transactional
public class StockToBuyEntityService extends StockInformationEntityService<StockToBuyEntity,
                                                                           StockToBuyDTO,
                                                                           StockToBuyRepository>
{
    private StockToBuyRepository stockToBuyRepository;
    private StockTagService stockTagService;
    private StockCompanyEntityService stockCompanyEntityService;
    private StockNoteSourceEntityService stockNoteSourceService;

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
        getStockQuotes( stockToBuyDTOs );
        logDebug( methodName, "stockToBuyList: {0}", stockToBuyDTOs );
        logMethodEnd( methodName, "Found " + stockToBuyEntities.getContent().size() + " to buy" );
        return stockToBuyDTOs;
    }

    /**
     * Get the list of all stock to buy for the customer and the ticker symbol
     * @param customerUuid
     * @return
     */
    public Page<StockToBuyDTO> getStockToBuyListForCustomerUuidAndTickerSymbol( @NotNull final Pageable pageRequest,
                                                                              @NotNull final UUID customerUuid,
                                                                              @NotNull final String tickerSymbol )
    {
        final String methodName = "getStockToBuyListForCustomerUuidAndTickerSymbol";
        logMethodBegin( methodName, customerUuid, tickerSymbol );
        Objects.requireNonNull( customerUuid, "customerId cannot be null" );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        final Page<StockToBuyEntity> stockToBuyEntities = this.stockToBuyRepository
                                                              .findByCustomerUuidAndTickerSymbol( pageRequest, customerUuid,
                                                                                                  tickerSymbol );
        final Page<StockToBuyDTO> stockToBuyDTOs = this.entitiesToDTOs( pageRequest, stockToBuyEntities );
        this.getStockQuotes( stockToBuyDTOs );
        logDebug( methodName, "stockToBuyList: {0}", stockToBuyDTOs );
        logMethodEnd( methodName, "Found " + stockToBuyEntities.getContent().size() + " to buy" );
        return stockToBuyDTOs;
    }

    /**
     * Get a single stock toBuy by stockToBuyUuid
     * @param stockToBuyUuid
     * @return StockToBuyQuoteDTO instance
     */
    public StockToBuyDTO getStockToBuy( final UUID stockToBuyUuid )
        throws StockToBuyNotFoundException
    {
        final String methodName = "getStockToBuy";
        logMethodBegin( methodName, stockToBuyUuid );
        Objects.requireNonNull( stockToBuyUuid, "stockToBuyId cannot be null" );
        final StockToBuyEntity stockToBuyEntity;
        try
        {
            stockToBuyEntity = this.getEntity( stockToBuyUuid );
        }
        catch( VersionedEntityNotFoundException e )
        {
            throw new StockToBuyNotFoundException( stockToBuyUuid );
        }
        final StockToBuyDTO stockToBuyDTO = this.entityToDTO( stockToBuyEntity );
        this.getStockInformationService()
            .setStockPrice( stockToBuyDTO, StockPriceFetchMode.ASYNCHRONOUS );
        logMethodEnd( methodName, stockToBuyDTO );
        return stockToBuyDTO;
    }

    /**
     * Creates a new StockToBuy table entry
     * @param stockToBuyDTO
     * @return
     * @throws StockNotFoundException
     * @throws EntityVersionMismatchException
     * @throws StockCompanyNotFoundException
     */
    public StockToBuyDTO createStockToBuy( final StockToBuyDTO stockToBuyDTO )
        throws StockNotFoundException,
               EntityVersionMismatchException,
               StockCompanyNotFoundException
    {
        final String methodName = "createStockToBuy";
        logMethodBegin( methodName, stockToBuyDTO );
        Objects.requireNonNull( stockToBuyDTO, "stockToBuyDTO cannot be null" );
        this.stockNoteSourceService.checkForNewSource( stockToBuyDTO );
        this.stockCompanyEntityService.checkStockTableEntry( stockToBuyDTO.getTickerSymbol() );
        StockToBuyEntity stockToBuyEntity = this.dtoToEntity( stockToBuyDTO );
        /*
         * The stock price needs to be set the first time as it records the stock price when the record was created.
         */
        if ( stockToBuyEntity.getStockPriceWhenCreated() == null )
        {
            stockToBuyEntity.setStockPriceWhenCreated( this.getStockInformationService()
                                                           .getStockPriceQuote( stockToBuyEntity.getTickerSymbol() ) );
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
        stockToBuyEntity = this.saveEntity( stockToBuyEntity );
        this.stockTagService.saveStockTags( stockToBuyEntity.getCustomerUuid(),
                                            stockToBuyEntity.getTickerSymbol(),
                                            StockTagEntity.StockTagReferenceType.STOCK_TO_BUY,
                                            stockToBuyEntity.getUuid(),
                                            stockToBuyDTO.getTags() );
        logDebug( methodName, "saved {0}", stockToBuyEntity );
        final StockToBuyDTO returnStockToBuyDTO = this.entityToDTO( stockToBuyEntity );
        this.getStockInformationService()
            .setStockPrice( returnStockToBuyDTO, StockPriceFetchMode.ASYNCHRONOUS );
        logMethodEnd( methodName, returnStockToBuyDTO );
        return returnStockToBuyDTO;
    }

    /**
     * Add a new stock toBuy to the database
     * @param stockToBuyDTO
     * @return
     */
    public StockToBuyDTO saveStockToBuy( @NotNull final StockToBuyDTO stockToBuyDTO )
        throws EntityVersionMismatchException
    {
        final String methodName = "saveStockToBuy";
        logMethodBegin( methodName, stockToBuyDTO );
        Objects.requireNonNull( stockToBuyDTO, "stockToBuyDTO cannot be null" );
        this.stockNoteSourceService.checkForNewSource( stockToBuyDTO );
        this.stockCompanyEntityService.checkStockTableEntry( stockToBuyDTO.getTickerSymbol() );
        final StockToBuyDTO returnStockToBuyDTO = this.saveDTO( stockToBuyDTO );
        this.stockTagService.saveStockTags( UUIDUtil.uuid( stockToBuyDTO.getCustomerId() ),
                                            stockToBuyDTO.getTickerSymbol(),
                                            StockTagEntity.StockTagReferenceType.STOCK_TO_BUY,
                                            UUIDUtil.uuid( stockToBuyDTO.getId() ),
                                            stockToBuyDTO.getTags() );
        logDebug( methodName, "saved {0}", stockToBuyDTO );
        this.getStockInformationService()
            .setStockPrice( returnStockToBuyDTO, StockPriceFetchMode.ASYNCHRONOUS );
        logMethodEnd( methodName, returnStockToBuyDTO );
        return returnStockToBuyDTO;
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
        this.getStockPrice( stockToBuyDTO );
        stockToBuyDTO.setTagsArray( this.stockTagService.findStockTags( UUIDUtil.uuid( stockToBuyDTO.getCustomerId() ),
                                                                        StockTagEntity.StockTagReferenceType.STOCK_TO_BUY,
                                                                        UUIDUtil.uuid( stockToBuyDTO.getId() )));
        stockToBuyDTO.setCreateDate( JSONDateConverter.toY4MMDD( stockToBuyEntity.getCreateDate() ) );
        stockToBuyDTO.setBuyAfterDate( JSONDateConverter.toY4MMDD( stockToBuyEntity.getBuyAfterDate() ) );
        if ( stockToBuyEntity.getStockNoteSourceByNotesSourceUuid() != null )
        {
            stockToBuyDTO.setNotesSourceName( stockToBuyEntity.getStockNoteSourceByNotesSourceUuid().getName() );
            stockToBuyDTO.setNotesSourceId( stockToBuyEntity.getStockNoteSourceByNotesSourceUuid().getUuid().toString() );
        }
        return stockToBuyDTO;
    }

    @Override
    protected StockToBuyDTO createDTO()
    {
        return this.context.getBean( StockToBuyDTO.class );
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
            this.getStockPrice( stockToBuyDTO );
        }
        logMethodEnd( methodName );
    }

    /**
     * Get a single stock quote.
     * @param stockToBuyDTO
     */
    private void getStockPrice( final StockToBuyDTO stockToBuyDTO )
    {
        this.getStockInformationService()
            .setStockPrice( stockToBuyDTO, StockPriceFetchMode.ASYNCHRONOUS );
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
        if ( stockToBuyDTO.getNotesSourceId() != null &&
             !stockToBuyDTO.getNotesSourceId().isEmpty() )
        {
            StockNoteSourceEntity stockNoteSourceEntity = this.stockNoteSourceService
                                                              .getStockNoteSource( stockToBuyDTO.getNotesSourceId() );
            stockToBuyEntity.setStockNoteSourceByNotesSourceUuid( stockNoteSourceEntity );
        }
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
    public void setStockCompanyEntityService( final StockCompanyEntityService stockCompanyEntityService )
    {
        this.stockCompanyEntityService = stockCompanyEntityService;
    }
}
