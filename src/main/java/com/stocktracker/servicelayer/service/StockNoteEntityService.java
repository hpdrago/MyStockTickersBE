package com.stocktracker.servicelayer.service;

import com.stocktracker.common.JSONDateConverter;
import com.stocktracker.repositorylayer.entity.StockNoteEntity;
import com.stocktracker.repositorylayer.repository.StockNoteRepository;
import com.stocktracker.weblayer.dto.StockNoteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

/**
 * This is the service class for the StockNotesEntity
 *
 * Created by mike on 5/7/2017.
 */
@Service
public class StockNoteEntityService extends StockInformationEntityService<StockNoteEntity,
                                                                          StockNoteDTO,
                                                                          StockNoteRepository>
{
    /**
     * Autowired service classes
     */
    private StockNoteRepository stockNoteRepository;

    /**
     * Get all of the notes for a customer.
     * @param pageRequest
     * @param customerUuid
     * @return
     */
    public Page<StockNoteDTO> getStockNotesForCustomerUuid( @NotNull final Pageable pageRequest,
                                                            @NotNull final UUID customerUuid )
    {
        final String methodName = "getStockNotesForCustomerUuid";
        logMethodBegin( methodName, pageRequest, customerUuid );
        final Page<StockNoteEntity> stockNoteEntities = this.stockNoteRepository
                                                            .findByCustomerUuid( pageRequest, customerUuid );
        final Page<StockNoteDTO> stockNoteDTOs = this.entitiesToDTOs( pageRequest, stockNoteEntities );
        logMethodEnd( methodName, stockNoteDTOs );
        return stockNoteDTOs;
    }

    /**
     * Get all of the notes for a customer and ticker symbol.
     * @param pageRequest
     * @param customerUuid
     * @param tickerSymbol
     * @return
     */
    public Page<StockNoteDTO> getStockNotesForCustomerUuidAndTickerSymbol( @NotNull final Pageable pageRequest,
                                                                           @NotNull final UUID customerUuid,
                                                                           @NotNull final String tickerSymbol )
    {
        final String methodName = "getStockNotes";
        logMethodBegin( methodName, pageRequest, customerUuid, tickerSymbol );
        Objects.requireNonNull( pageRequest, "pageRequest cannot be null" );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Objects.requireNonNull( customerUuid, "customerUuid cannot be null" );
        final Page<StockNoteEntity> stockNoteEntities = this.stockNoteRepository
                                                            .findByCustomerUuidAndTickerSymbol( pageRequest, customerUuid, tickerSymbol );
        final Page<StockNoteDTO> stockNoteDTOs = this.entitiesToDTOs( pageRequest, stockNoteEntities );
        logMethodEnd( methodName, stockNoteDTOs );
        return stockNoteDTOs;
    }

    /**
     * Converts the {@code stockNoteEntity} to a DTO
     * @param stockNoteEntity
     * @return
     */
    @Override
    protected StockNoteDTO entityToDTO( final StockNoteEntity stockNoteEntity )
    {
        Objects.requireNonNull( stockNoteEntity );
        StockNoteDTO stockNoteDTO = super.entityToDTO( stockNoteEntity );
        stockNoteDTO.setNotesDate( JSONDateConverter.toY4MMDD( stockNoteEntity.getNotesDate() ));
        stockNoteDTO.setCreateDate( JSONDateConverter.toY4MMDD( stockNoteEntity.getCreateDate() ));
        stockNoteDTO.setUpdateDate( JSONDateConverter.toY4MMDD( stockNoteEntity.getUpdateDate() ));
        return stockNoteDTO;
    }

    @Override
    protected StockNoteEntity dtoToEntity( final StockNoteDTO stockNoteDTO )
    {
        final StockNoteEntity stockNoteEntity = super.dtoToEntity( stockNoteDTO );
        stockNoteEntity.setNotesDate( JSONDateConverter.toTimestamp( stockNoteDTO.getNotesDate() ));
        return stockNoteEntity;
    }

    @Override
    protected StockNoteDTO createDTO()
    {
        return this.context.getBean( StockNoteDTO.class );
    }

    @Override
    protected StockNoteEntity createEntity()
    {
        return this.context.getBean( StockNoteEntity.class );
    }

    @Override
    protected StockNoteRepository getRepository()
    {
        return this.stockNoteRepository;
    }

    @Autowired
    public void setStockNoteRepository( final StockNoteRepository stockNoteRepository )
    {
        this.stockNoteRepository = stockNoteRepository;
    }

}
