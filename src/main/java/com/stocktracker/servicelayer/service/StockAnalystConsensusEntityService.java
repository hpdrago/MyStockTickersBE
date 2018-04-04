package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.DuplicateAnalystConsensusException;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.StockCompanyNotFoundException;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.entity.StockAnalystConsensusEntity;
import com.stocktracker.repositorylayer.entity.StockNoteSourceEntity;
import com.stocktracker.repositorylayer.repository.StockAnalystConsensusRepository;
import com.stocktracker.servicelayer.stockinformationprovider.StockPriceFetchMode;
import com.stocktracker.weblayer.dto.StockAnalystConsensusQuoteDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class StockAnalystConsensusEntityService extends StockInformationEntityService<Integer,
                                                                                         StockAnalystConsensusEntity,
    StockAnalystConsensusQuoteDTO,
                                                                                         StockAnalystConsensusRepository>
                                                implements MyLogger
{
    private StockAnalystConsensusRepository stockAnalystConsensusRepository;
    private StockNoteSourceEntityService stockNoteSourceService;

    /**
     * Get the DTO for the stock ticker symbol.
     * @param customerId
     * @param tickerSymbol
     * @return
     * @Throws IllegalArgumentException when customerId <= 0 and if tickerSymbol is null
     */
    public StockAnalystConsensusQuoteDTO getStockAnalystConsensus( final int customerId, final String tickerSymbol )
    {
        final String methodName = "getStockAnalystConsensus";
        logMethodBegin( methodName, customerId, tickerSymbol );
        Assert.isTrue( customerId > 0, "customerId must be > 0");
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        StockAnalystConsensusEntity stockAnalystConsensusEntity = this.stockAnalystConsensusRepository
                                                                      .findByCustomerIdAndTickerSymbol( customerId, tickerSymbol );
        StockAnalystConsensusQuoteDTO stockAnalystConsensusDTO = null;
        if ( stockAnalystConsensusEntity != null )
        {
            stockAnalystConsensusDTO = this.entityToDTO( stockAnalystConsensusEntity );
        }
        logMethodEnd( methodName, stockAnalystConsensusDTO );
        return stockAnalystConsensusDTO;
    }

    /**
     * Gets all of the consensus rows for the customer.
     * @param customerId
     * @return
     */
    public List<StockAnalystConsensusQuoteDTO> getAllStockAnalystConsensus( final Integer customerId )
    {
        final String methodName = "getAllStockAnalystConsensusList";
        logMethodBegin( methodName, customerId );
        Objects.requireNonNull( customerId, "customerId cannot be null" );
        final List<StockAnalystConsensusEntity> stockAnalystConsensusEntities = this.stockAnalystConsensusRepository
                                                                                    .findByCustomerId( customerId );
        final List<StockAnalystConsensusQuoteDTO> stockAnalystConsensusDTOS = this.entitiesToDTOs( stockAnalystConsensusEntities );
        logMethodEnd( methodName, "Found " + stockAnalystConsensusEntities.size() + " records" );
        return stockAnalystConsensusDTOS;
    }

    /**
     * Get the list of all stock summaries for the customer
     *
     * @param pageRequest
     * @param customerId
     * @return
     */
    public Page<StockAnalystConsensusQuoteDTO> getStockAnalystConsensusPage( @NotNull final Pageable pageRequest,
                                                                             @NotNull final Integer customerId )
    {
        final String methodName = "getStockAnalystConsensusPage";
        logMethodBegin( methodName, pageRequest, customerId );
        Objects.requireNonNull( customerId, "customerId cannot be null" );
        final  Page<StockAnalystConsensusEntity> stockAnalystConsensusEntities = this.stockAnalystConsensusRepository
                                                                                     .findByCustomerId( pageRequest, customerId );
        final Page<StockAnalystConsensusQuoteDTO> stockAnalystConsensusDTOS = this.entitiesToDTOs( pageRequest,
                                                                                                   stockAnalystConsensusEntities );
        logMethodEnd( methodName, "Found " + stockAnalystConsensusEntities.getContent().size() + " records" );
        return stockAnalystConsensusDTOS;
    }

    /**
     * Get a list of analyst consensus records for the customer and ticker symbol.
     * @param customerId
     * @param tickerSymbol
     * @return
     */
    public Page<StockAnalystConsensusQuoteDTO> getStockAnalystConsensusListForCustomerIdAndTickerSymbol( final Pageable pageRequest,
                                                                                                         final int customerId,
                                                                                                         final String tickerSymbol )
    {
        final String methodName = "getStockAnalystConsensusListForCustomerIdAndTickerSymbol";
        logMethodBegin( methodName, pageRequest, customerId, tickerSymbol );
        Objects.requireNonNull( customerId, "customerId cannot be null" );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Page<StockAnalystConsensusEntity> stockAnalystConsensusEntities = this.stockAnalystConsensusRepository
            .findByCustomerIdAndTickerSymbol( pageRequest, customerId, tickerSymbol );
        Page<StockAnalystConsensusQuoteDTO> stockAnalystConsensusDTOS = this.entitiesToDTOs( pageRequest, stockAnalystConsensusEntities );
        logDebug( methodName, "stockAnalystConsensusList: {0}", stockAnalystConsensusDTOS );
        logMethodEnd( methodName, "Found " + stockAnalystConsensusEntities.getContent().size() + " records" );
        return stockAnalystConsensusDTOS;
    }

    /**
     * Get a single stock analytics by stockAnalystConsensusId
     * @param stockAnalystConsensusId
     * @return StockAnalystConsensusQuoteDTO instance
     */
    public StockAnalystConsensusQuoteDTO getStockAnalystConsensus( @NotNull final Integer stockAnalystConsensusId )
        throws VersionedEntityNotFoundException
    {
        final String methodName = "getStockAnalystConsensus";
        logMethodBegin( methodName, stockAnalystConsensusId );
        Objects.requireNonNull( stockAnalystConsensusId, "stockAnalystConsensusId cannot be null" );
        StockAnalystConsensusEntity stockAnalystConsensusEntity = this.getEntity( stockAnalystConsensusId );
        StockAnalystConsensusQuoteDTO stockAnalystConsensusDTO = this.entityToDTO( stockAnalystConsensusEntity );
        logMethodEnd( methodName, stockAnalystConsensusDTO );
        return stockAnalystConsensusDTO;
    }

    /**
     * Creates a new Stock Analyst Consensus
     *
     * @param customerId
     * @param stockAnalystConsensusDTO
     * @return
     * @throws StockNotFoundException
     * @throws StockCompanyNotFoundException
     * @throws EntityVersionMismatchException
     */
    public StockAnalystConsensusQuoteDTO createStockAnalystConsensus( final Integer customerId,
                                                                      final StockAnalystConsensusQuoteDTO stockAnalystConsensusDTO )
        throws StockNotFoundException,
               StockCompanyNotFoundException,
               EntityVersionMismatchException
    {
        final String methodName = "createStockAnalystConsensus";
        logMethodBegin( methodName, customerId, stockAnalystConsensusDTO );
        Objects.requireNonNull( stockAnalystConsensusDTO, "stockAnalystConsensusDTO cannot be null" );
        if ( this.stockAnalystConsensusRepository
                 .findByCustomerIdAndTickerSymbol( customerId, stockAnalystConsensusDTO.getTickerSymbol() ) != null )
        {
            throw new DuplicateAnalystConsensusException( stockAnalystConsensusDTO.getTickerSymbol() ) ;
        }
        this.stockNoteSourceService.checkForNewSource( stockAnalystConsensusDTO );
        StockAnalystConsensusEntity stockAnalystConsensusEntity = this.dtoToEntity( stockAnalystConsensusDTO );
        stockAnalystConsensusEntity.setVersion( 1 );
        stockAnalystConsensusEntity.setStockPriceWhenCreated( this.getStockInformationService()
                                                                  .getStockPriceQuote( stockAnalystConsensusDTO.getTickerSymbol() ));
        /*
         * use saveAndFlush so that we can get the updated values from the row which might be changed with insert
         * or update triggers.
         */
        stockAnalystConsensusEntity = this.saveEntity( stockAnalystConsensusEntity );
        logDebug( methodName, "saved {0}", stockAnalystConsensusEntity );
        StockAnalystConsensusQuoteDTO returnStockAnalystConsensusDTO = this.entityToDTO( stockAnalystConsensusEntity );
        logMethodEnd( methodName, returnStockAnalystConsensusDTO );
        return returnStockAnalystConsensusDTO;
    }

    /**
     * Add a new stock analytics to the database
     * @param stockAnalystConsensusDTO
     * @return
     */
    public StockAnalystConsensusQuoteDTO saveStockAnalystConsensus( @NotNull final StockAnalystConsensusQuoteDTO stockAnalystConsensusDTO )
        throws EntityVersionMismatchException
    {
        final String methodName = "saveStockAnalystConsensus";
        logMethodBegin( methodName, stockAnalystConsensusDTO );
        Objects.requireNonNull( stockAnalystConsensusDTO, "stockAnalystConsensusDTO cannot be null" );
        this.stockNoteSourceService.checkForNewSource( stockAnalystConsensusDTO );
        StockAnalystConsensusQuoteDTO returnStockAnalystConsensusDTO = super.saveDTO( stockAnalystConsensusDTO );
        logMethodEnd( methodName, returnStockAnalystConsensusDTO );
        return returnStockAnalystConsensusDTO;
    }

    @Override
    protected StockAnalystConsensusQuoteDTO entityToDTO( final StockAnalystConsensusEntity stockAnalystConsensusEntity )
    {
        Objects.requireNonNull( stockAnalystConsensusEntity );
        StockAnalystConsensusQuoteDTO stockAnalystConsensusDTO = StockAnalystConsensusQuoteDTO.newInstance();
        BeanUtils.copyProperties( stockAnalystConsensusEntity, stockAnalystConsensusDTO );
        this.getStockInformationService()
            .setStockPrice( stockAnalystConsensusDTO, StockPriceFetchMode.ASYNCHRONOUS );
        stockAnalystConsensusDTO.setAnalystPriceDate( stockAnalystConsensusEntity.getAnalystPriceDate() );
        stockAnalystConsensusDTO.setAnalystSentimentDate( stockAnalystConsensusEntity.getAnalystSentimentDate() );
        if ( stockAnalystConsensusEntity.getStockNoteSourceByNoteSourceId() != null )
        {
            stockAnalystConsensusDTO.setNotesSourceName( stockAnalystConsensusEntity.getStockNoteSourceByNoteSourceId().getName() );
            stockAnalystConsensusDTO.setNotesSourceId( stockAnalystConsensusEntity.getStockNoteSourceByNoteSourceId().getId() );
        }
        return stockAnalystConsensusDTO;
    }

    @Override
    protected StockAnalystConsensusQuoteDTO createDTO()
    {
        return this.context.getBean( StockAnalystConsensusQuoteDTO.class );
    }

    @Override
    protected StockAnalystConsensusEntity dtoToEntity( final StockAnalystConsensusQuoteDTO stockAnalystConsensusDTO )
    {
        Objects.requireNonNull( stockAnalystConsensusDTO );
        StockAnalystConsensusEntity stockAnalystConsensusEntity = StockAnalystConsensusEntity.newInstance();
        BeanUtils.copyProperties( stockAnalystConsensusDTO, stockAnalystConsensusEntity );
        if ( stockAnalystConsensusDTO.getNotesSourceId() != null &&
             stockAnalystConsensusDTO.getNotesSourceId() > 0 )
        {
            StockNoteSourceEntity stockNoteSourceEntity = this.stockNoteSourceService
                .getStockNoteSource( stockAnalystConsensusDTO.getNotesSourceId() );
            stockAnalystConsensusEntity.setStockNoteSourceByNoteSourceId( stockNoteSourceEntity );
        }
        return stockAnalystConsensusEntity;
    }

    @Override
    protected StockAnalystConsensusEntity createEntity()
    {
        return this.context.getBean( StockAnalystConsensusEntity.class );
    }

    @Override
    protected StockAnalystConsensusRepository getRepository()
    {
        return this.stockAnalystConsensusRepository;
    }

    @Autowired
    public void setStockAnalystConsensusRepository( final StockAnalystConsensusRepository stockAnalystConsensusRepository )
    {
        this.stockAnalystConsensusRepository = stockAnalystConsensusRepository;
    }

    @Autowired
    public void setStockNoteSourceService( final StockNoteSourceEntityService stockNoteSourceService )
    {
        this.stockNoteSourceService = stockNoteSourceService;
    }
}
