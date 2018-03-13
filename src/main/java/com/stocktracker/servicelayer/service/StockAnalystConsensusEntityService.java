package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.DuplicateAnalystConsensusException;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockQuoteUnavailableException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.entity.StockAnalystConsensusEntity;
import com.stocktracker.repositorylayer.entity.StockNoteSourceEntity;
import com.stocktracker.repositorylayer.repository.StockAnalystConsensusRepository;
import com.stocktracker.servicelayer.stockinformationprovider.StockQuoteFetchMode;
import com.stocktracker.weblayer.dto.StockAnalystConsensusDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Service
@Transactional
public class StockAnalystConsensusEntityService extends StockQuoteContainerEntityService<Integer,
                                                                                             StockAnalystConsensusEntity,
                                                                                             StockAnalystConsensusDTO,
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
    public StockAnalystConsensusDTO getStockAnalystConsensus( final int customerId, final String tickerSymbol )
    {
        final String methodName = "getStockAnalystConsensus";
        logMethodBegin( methodName, customerId, tickerSymbol );
        Assert.isTrue( customerId > 0, "customerId must be > 0");
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        StockAnalystConsensusEntity stockAnalystConsensusEntity = this.stockAnalystConsensusRepository
                                                                      .findByCustomerIdAndTickerSymbol( customerId, tickerSymbol );
        StockAnalystConsensusDTO stockAnalystConsensusDTO = null;
        if ( stockAnalystConsensusEntity != null )
        {
            stockAnalystConsensusDTO = this.entityToDTO( stockAnalystConsensusEntity );
        }
        logMethodEnd( methodName, stockAnalystConsensusDTO );
        return stockAnalystConsensusDTO;
    }

    /**
     * Get the list of all stock summaries for the customer
     *
     * @param pageRequest
     * @param customerId
     * @return
     */
    public Page<StockAnalystConsensusDTO> getStockAnalystConsensusListForCustomerId( @NotNull final Pageable pageRequest,
                                                                                     @NotNull final Integer customerId )
    {
        final String methodName = "getStockAnalystConsensusListForCustomerId";
        logMethodBegin( methodName, pageRequest, customerId );
        Objects.requireNonNull( customerId, "customerId cannot be null" );
        final  Page<StockAnalystConsensusEntity> stockAnalystConsensusEntities = this.stockAnalystConsensusRepository
                                                                                     .findByCustomerId( pageRequest, customerId );
        final Page<StockAnalystConsensusDTO> stockAnalystConsensusDTOS = this.entitiesToDTOs( pageRequest,
                                                                                              stockAnalystConsensusEntities );
        logDebug( methodName, "stockAnalystConsensusList: {0}", stockAnalystConsensusDTOS );
        logMethodEnd( methodName, "Found " + stockAnalystConsensusEntities.getContent().size() + " records" );
        return stockAnalystConsensusDTOS;
    }

    /**
     * Get a list of analyst consensus records for the customer and ticker symbol.
     * @param customerId
     * @param tickerSymbol
     * @return
     */
    public Page<StockAnalystConsensusDTO> getStockAnalystConsensusListForCustomerIdAndTickerSymbol( final Pageable pageRequest,
                                                                                                    final int customerId,
                                                                                                    final String tickerSymbol )
    {
        final String methodName = "getStockAnalystConsensusListForCustomerIdAndTickerSymbol";
        logMethodBegin( methodName, pageRequest, customerId, tickerSymbol );
        Objects.requireNonNull( customerId, "customerId cannot be null" );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Page<StockAnalystConsensusEntity> stockAnalystConsensusEntities = this.stockAnalystConsensusRepository
            .findByCustomerIdAndTickerSymbol( pageRequest, customerId, tickerSymbol );
        Page<StockAnalystConsensusDTO> stockAnalystConsensusDTOS = this.entitiesToDTOs( pageRequest, stockAnalystConsensusEntities );
        logDebug( methodName, "stockAnalystConsensusList: {0}", stockAnalystConsensusDTOS );
        logMethodEnd( methodName, "Found " + stockAnalystConsensusEntities.getContent().size() + " records" );
        return stockAnalystConsensusDTOS;
    }

    /**
     * Get a single stock analytics by stockAnalystConsensusId
     * @param stockAnalystConsensusId
     * @return StockAnalystConsensusDTO instance
     */
    public StockAnalystConsensusDTO getStockAnalystConsensus( @NotNull final Integer stockAnalystConsensusId )
        throws VersionedEntityNotFoundException
    {
        final String methodName = "getStockAnalystConsensus";
        logMethodBegin( methodName, stockAnalystConsensusId );
        Objects.requireNonNull( stockAnalystConsensusId, "stockAnalystConsensusId cannot be null" );
        StockAnalystConsensusEntity stockAnalystConsensusEntity = this.getEntity( stockAnalystConsensusId );
        StockAnalystConsensusDTO stockAnalystConsensusDTO = this.entityToDTO( stockAnalystConsensusEntity );
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
     * @throws StockQuoteUnavailableException
     * @throws EntityVersionMismatchException
     */
    public StockAnalystConsensusDTO createStockAnalystConsensus( final Integer customerId,
                                                                 final StockAnalystConsensusDTO stockAnalystConsensusDTO )
        throws StockNotFoundException,
               StockQuoteUnavailableException,
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
        stockAnalystConsensusEntity.setStockPriceWhenCreated( this.getStockQuoteService()
                                                                  .getStockPrice( stockAnalystConsensusDTO.getTickerSymbol() ));
        /*
         * use saveAndFlush so that we can get the updated values from the row which might be changed with insert
         * or update triggers.
         */
        stockAnalystConsensusEntity = this.saveEntity( stockAnalystConsensusEntity );
        logDebug( methodName, "saved {0}", stockAnalystConsensusEntity );
        StockAnalystConsensusDTO returnStockAnalystConsensusDTO = this.entityToDTO( stockAnalystConsensusEntity );
        logMethodEnd( methodName, returnStockAnalystConsensusDTO );
        return returnStockAnalystConsensusDTO;
    }

    /**
     * Add a new stock analytics to the database
     * @param stockAnalystConsensusDTO
     * @return
     */
    public StockAnalystConsensusDTO saveStockAnalystConsensus( @NotNull final StockAnalystConsensusDTO stockAnalystConsensusDTO )
        throws EntityVersionMismatchException
    {
        final String methodName = "saveStockAnalystConsensus";
        logMethodBegin( methodName, stockAnalystConsensusDTO );
        Objects.requireNonNull( stockAnalystConsensusDTO, "stockAnalystConsensusDTO cannot be null" );
        this.stockNoteSourceService.checkForNewSource( stockAnalystConsensusDTO );
        StockAnalystConsensusDTO returnStockAnalystConsensusDTO = super.saveDTO( stockAnalystConsensusDTO );
        logMethodEnd( methodName, returnStockAnalystConsensusDTO );
        return returnStockAnalystConsensusDTO;
    }

    @Override
    protected StockAnalystConsensusDTO entityToDTO( final StockAnalystConsensusEntity stockAnalystConsensusEntity )
    {
        final String methodName = "entityToDTO";
        Objects.requireNonNull( stockAnalystConsensusEntity );
        StockAnalystConsensusDTO stockAnalystConsensusDTO = StockAnalystConsensusDTO.newInstance();
        BeanUtils.copyProperties( stockAnalystConsensusEntity, stockAnalystConsensusDTO );
        try
        {
            this.getStockQuoteService()
                .setStockQuoteInformation( stockAnalystConsensusDTO, StockQuoteFetchMode.ASYNCHRONOUS );
        }
        catch ( StockQuoteUnavailableException e )
        {
            logError( methodName, e );
        }
        catch ( StockNotFoundException e )
        {
            logError( methodName, e );
        }
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
    protected StockAnalystConsensusEntity dtoToEntity( final StockAnalystConsensusDTO stockAnalystConsensusDTO )
    {
        final String methodName = "dtoToEntity";
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
