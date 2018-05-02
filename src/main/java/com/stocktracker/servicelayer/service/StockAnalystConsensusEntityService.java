package com.stocktracker.servicelayer.service;

import com.stocktracker.common.exceptions.DuplicateAnalystConsensusException;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.StockCompanyNotFoundException;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.entity.StockAnalystConsensusEntity;
import com.stocktracker.repositorylayer.entity.StockNoteSourceEntity;
import com.stocktracker.repositorylayer.repository.StockAnalystConsensusRepository;
import com.stocktracker.servicelayer.stockinformationprovider.StockPriceFetchMode;
import com.stocktracker.weblayer.dto.StockAnalystConsensusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
//@Transactional
public class StockAnalystConsensusEntityService extends StockInformationEntityService<StockAnalystConsensusEntity,
                                                                                      StockAnalystConsensusDTO,
                                                                                      StockAnalystConsensusRepository>
{
    private StockAnalystConsensusRepository stockAnalystConsensusRepository;
    private StockNoteSourceEntityService stockNoteSourceService;

    /**
     * Get the DTO for the stock ticker symbol.
     * @param customerUuid
     * @param tickerSymbol
     * @return
     * @Throws IllegalArgumentException when customerId <= 0 and if tickerSymbol is null
     */
    public StockAnalystConsensusDTO getStockAnalystConsensus( final UUID customerUuid, final String tickerSymbol )
    {
        final String methodName = "getStockAnalystConsensus";
        logMethodBegin( methodName, customerUuid, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Objects.requireNonNull( customerUuid, "customerUuid cannot be null" );
        StockAnalystConsensusEntity stockAnalystConsensusEntity = this.stockAnalystConsensusRepository
                                                                      .findByCustomerUuidAndTickerSymbol( customerUuid, tickerSymbol );
        StockAnalystConsensusDTO stockAnalystConsensusDTO = null;
        if ( stockAnalystConsensusEntity != null )
        {
            stockAnalystConsensusDTO = this.entityToDTO( stockAnalystConsensusEntity );
        }
        logMethodEnd( methodName, stockAnalystConsensusDTO );
        return stockAnalystConsensusDTO;
    }

    /**
     * Gets all of the consensus rows for the customer.
     * @param customerUuid
     * @return
     */
    public List<StockAnalystConsensusDTO> getAllStockAnalystConsensus( final UUID customerUuid )
    {
        final String methodName = "getAllStockAnalystConsensusList";
        logMethodBegin( methodName, customerUuid );
        Objects.requireNonNull( customerUuid, "customerId cannot be null" );
        final List<StockAnalystConsensusEntity> stockAnalystConsensusEntities = this.stockAnalystConsensusRepository
                                                                                    .findByCustomerUuid( customerUuid );
        final List<StockAnalystConsensusDTO> stockAnalystConsensusDTOS = this.entitiesToDTOs( stockAnalystConsensusEntities );
        logMethodEnd( methodName, "Found " + stockAnalystConsensusEntities.size() + " records" );
        return stockAnalystConsensusDTOS;
    }

    /**
     * Get the list of all stock summaries for the customer
     *
     * @param pageRequest
     * @param customerUuid
     * @return
     */
    public Page<StockAnalystConsensusDTO> getStockAnalystConsensusPage( @NotNull final Pageable pageRequest,
                                                                        @NotNull final UUID customerUuid )
    {
        final String methodName = "getStockAnalystConsensusPage";
        logMethodBegin( methodName, pageRequest, customerUuid );
        Objects.requireNonNull( customerUuid, "customerId cannot be null" );
        final  Page<StockAnalystConsensusEntity> stockAnalystConsensusEntities = this.stockAnalystConsensusRepository
                                                                                     .findByCustomerUuid( pageRequest, customerUuid );
        final Page<StockAnalystConsensusDTO> stockAnalystConsensusDTOS = this.entitiesToDTOs( pageRequest,
                                                                                              stockAnalystConsensusEntities );
        logMethodEnd( methodName, "Found " + stockAnalystConsensusEntities.getContent().size() + " records" );
        return stockAnalystConsensusDTOS;
    }

    /**
     * Get a list of analyst consensus records for the customer and ticker symbol.
     * @param customerUuid
     * @param tickerSymbol
     * @return
     */
    public Page<StockAnalystConsensusDTO> getStockAnalystConsensusListForCustomerUuidAndTickerSymbol( final Pageable pageRequest,
                                                                                                    final UUID customerUuid,
                                                                                                    final String tickerSymbol )
    {
        final String methodName = "getStockAnalystConsensusListForCustomerUuidAndTickerSymbol";
        logMethodBegin( methodName, pageRequest, customerUuid, tickerSymbol );
        Objects.requireNonNull( customerUuid, "customerId cannot be null" );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Page<StockAnalystConsensusEntity> stockAnalystConsensusEntities = this.stockAnalystConsensusRepository
            .findByCustomerUuidAndTickerSymbol( pageRequest, customerUuid, tickerSymbol );
        Page<StockAnalystConsensusDTO> stockAnalystConsensusDTOS = this.entitiesToDTOs( pageRequest, stockAnalystConsensusEntities );
        logDebug( methodName, "stockAnalystConsensusList: {0}", stockAnalystConsensusDTOS );
        logMethodEnd( methodName, "Found " + stockAnalystConsensusEntities.getContent().size() + " records" );
        return stockAnalystConsensusDTOS;
    }

    /**
     * Get a single stock analytics by stockAnalystConsensusId
     * @param stockAnalystConsensusUuid
     * @return StockAnalystConsensusQuoteDTO instance
     */
    public StockAnalystConsensusDTO getStockAnalystConsensus( @NotNull final UUID stockAnalystConsensusUuid )
        throws VersionedEntityNotFoundException
    {
        final String methodName = "getStockAnalystConsensus";
        logMethodBegin( methodName, stockAnalystConsensusUuid );
        Objects.requireNonNull( stockAnalystConsensusUuid, "stockAnalystConsensusId cannot be null" );
        StockAnalystConsensusEntity stockAnalystConsensusEntity = this.getEntity( stockAnalystConsensusUuid );
        StockAnalystConsensusDTO stockAnalystConsensusDTO = this.entityToDTO( stockAnalystConsensusEntity );
        logMethodEnd( methodName, stockAnalystConsensusDTO );
        return stockAnalystConsensusDTO;
    }

    /**
     * Creates a new Stock Analyst Consensus
     *
     * @param customerUuid
     * @param stockAnalystConsensusDTO
     * @return
     * @throws StockNotFoundException
     * @throws StockCompanyNotFoundException
     * @throws EntityVersionMismatchException
     */
    public StockAnalystConsensusDTO createStockAnalystConsensus( final UUID customerUuid,
                                                                 final StockAnalystConsensusDTO stockAnalystConsensusDTO )
        throws StockNotFoundException,
               StockCompanyNotFoundException,
               EntityVersionMismatchException
    {
        final String methodName = "createStockAnalystConsensus";
        logMethodBegin( methodName, customerUuid, stockAnalystConsensusDTO );
        Objects.requireNonNull( stockAnalystConsensusDTO, "stockAnalystConsensusDTO cannot be null" );
        if ( this.stockAnalystConsensusRepository
                 .findByCustomerUuidAndTickerSymbol( customerUuid, stockAnalystConsensusDTO.getTickerSymbol() ) != null )
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
        final StockAnalystConsensusDTO returnStockAnalystConsensusDTO = super.saveDTO( stockAnalystConsensusDTO );
        logMethodEnd( methodName, returnStockAnalystConsensusDTO );
        return returnStockAnalystConsensusDTO;
    }

    @Override
    protected StockAnalystConsensusDTO entityToDTO( final StockAnalystConsensusEntity stockAnalystConsensusEntity )
    {
        Objects.requireNonNull( stockAnalystConsensusEntity );
        final StockAnalystConsensusDTO stockAnalystConsensusDTO = super.entityToDTO( stockAnalystConsensusEntity );
        stockAnalystConsensusDTO.setCustomerId( stockAnalystConsensusEntity.getCustomerUuid().toString() );
        this.getStockInformationService()
            .setStockPrice( stockAnalystConsensusDTO, StockPriceFetchMode.ASYNCHRONOUS );
        stockAnalystConsensusDTO.setAnalystPriceDate( stockAnalystConsensusEntity.getAnalystPriceDate() );
        stockAnalystConsensusDTO.setAnalystSentimentDate( stockAnalystConsensusEntity.getAnalystSentimentDate() );
        if ( stockAnalystConsensusEntity.getStockNoteSourceByNoteSourceUuid() != null )
        {
            stockAnalystConsensusDTO.setNotesSourceName( stockAnalystConsensusEntity.getStockNoteSourceByNoteSourceUuid()
                                                                                    .getName() );
            stockAnalystConsensusDTO.setNotesSourceId( stockAnalystConsensusEntity.getStockNoteSourceByNoteSourceUuid()
                                                                                  .getUuid().toString() );
        }
        return stockAnalystConsensusDTO;
    }

    @Override
    protected StockAnalystConsensusDTO createDTO()
    {
        return this.context.getBean( StockAnalystConsensusDTO.class );
    }

    @Override
    protected StockAnalystConsensusEntity dtoToEntity( final StockAnalystConsensusDTO stockAnalystConsensusDTO )
    {
        Objects.requireNonNull( stockAnalystConsensusDTO );
        final StockAnalystConsensusEntity stockAnalystConsensusEntity = super.dtoToEntity( stockAnalystConsensusDTO );
        if ( stockAnalystConsensusDTO.getNotesSourceId() != null &&
             !stockAnalystConsensusDTO.getNotesSourceId().isEmpty() )
        {
            StockNoteSourceEntity stockNoteSourceEntity = this.stockNoteSourceService
                .getStockNoteSource( stockAnalystConsensusDTO.getNotesSourceId() );
            stockAnalystConsensusEntity.setStockNoteSourceByNoteSourceUuid( stockNoteSourceEntity );
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
