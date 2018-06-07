package com.stocktracker.servicelayer.service;

import com.stocktracker.repositorylayer.entity.StockAnalystConsensusEntity;
import com.stocktracker.repositorylayer.repository.StockAnalystConsensusRepository;
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
public class StockAnalystConsensusEntityService extends StockInformationEntityService<StockAnalystConsensusEntity,
                                                                                      StockAnalystConsensusDTO,
                                                                                      StockAnalystConsensusRepository>
{
    @Autowired
    private StockAnalystConsensusRepository stockAnalystConsensusRepository;

    @Autowired
    private StockQuoteEntityService stockQuoteEntityService;

    /**
     * Get the DTO for the stock ticker symbol.
     * @param customerUuid
     * @param tickerSymbol
     * @return
     * @Throws IllegalArgumentException when customerId <= 0 and if tickerSymbol is null
     */
    public Page<StockAnalystConsensusDTO> getStockAnalystConsensusPage( @NotNull final Pageable pageRequest,
                                                                        @NotNull final UUID customerUuid,
                                                                        @NotNull final String tickerSymbol )
    {
        final String methodName = "getStockAnalystConsensusPage";
        logMethodBegin( methodName, customerUuid, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Objects.requireNonNull( customerUuid, "customerUuid cannot be null" );
        final Page<StockAnalystConsensusEntity> stockAnalystConsensusEntities = this.stockAnalystConsensusRepository
                                                                                    .findByCustomerUuidAndTickerSymbol( pageRequest,
                                                                                                                        customerUuid,
                                                                                                                        tickerSymbol );
        final Page<StockAnalystConsensusDTO> stockAnalystConsensusDTOS = this.entitiesToDTOs( pageRequest,
                                                                                              stockAnalystConsensusEntities );
        logMethodEnd( methodName, stockAnalystConsensusDTOS );
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
        final Page<StockAnalystConsensusEntity> stockAnalystConsensusEntities = this.stockAnalystConsensusRepository
                                                                                    .findByCustomerUuid( pageRequest,
                                                                                                         customerUuid );
        final Page<StockAnalystConsensusDTO> stockAnalystConsensusDTOS = this.entitiesToDTOs( pageRequest,
                                                                                              stockAnalystConsensusEntities );
        logMethodEnd( methodName, "Found " + stockAnalystConsensusEntities.getContent().size() + " records" );
        return stockAnalystConsensusDTOS;
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

    @Override
    public StockAnalystConsensusDTO entityToDTO( final StockAnalystConsensusEntity entity )
    {
        final StockAnalystConsensusDTO dto = super.entityToDTO( entity );
        this.stockQuoteEntityService
            .setStockQuoteInformation( dto );
        return dto;
    }

    @Override
    protected StockAnalystConsensusDTO createDTO()
    {
        return this.context.getBean( StockAnalystConsensusDTO.class );
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
}
