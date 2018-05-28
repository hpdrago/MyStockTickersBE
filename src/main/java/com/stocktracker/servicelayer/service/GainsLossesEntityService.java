package com.stocktracker.servicelayer.service;

import com.stocktracker.repositorylayer.entity.GainsLossesEntity;
import com.stocktracker.repositorylayer.repository.GainsLossesRepository;
import com.stocktracker.weblayer.dto.GainsLossesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

/**
 * This service class manages the transactions with the STOCK_TO_BUY table.
 */
@Service
public class GainsLossesEntityService extends UuidEntityService<GainsLossesEntity,
                                                                GainsLossesDTO,
                                                                GainsLossesRepository>
{
    private GainsLossesRepository gainsLossesRepository;

    /**
     * Searches for the one entry for the customer UUID and ticker symbol -- this is a unique combination.
     * @param customerUuid
     * @return
     */
    public GainsLossesDTO getByCustomerUuidAndTickerSymbol( @NotNull final UUID customerUuid,
                                                            @NotNull final String tickerSymbol )
    {
        final String methodName = "getGainsLossesListForCustomerUuid";
        logMethodBegin( methodName, customerUuid, tickerSymbol );
        Objects.requireNonNull( customerUuid, "customerUuid cannot be null" );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        final GainsLossesEntity gainsLossesEntity = this.gainsLossesRepository
                                                        .findByCustomerUuidAndTickerSymbol( customerUuid, tickerSymbol );
        GainsLossesDTO gainsLossesDTO = null;
        if ( gainsLossesEntity != null )
        {
            gainsLossesDTO = this.entityToDTO( gainsLossesEntity );
        }
        logMethodEnd( methodName, gainsLossesDTO );
        return gainsLossesDTO;
    }

    /**
     * Get the list of all stock to buy for the customer
     * @param customerUuid
     * @return
     */
    public Page<GainsLossesDTO> getGainsLossesListForCustomerUuid( final Pageable pageRequest,
                                                                 @NotNull final UUID customerUuid )
    {
        final String methodName = "getGainsLossesListForCustomerUuid";
        logMethodBegin( methodName, pageRequest, customerUuid );
        Objects.requireNonNull( customerUuid, "customerUuid cannot be null" );
        Page<GainsLossesEntity> gainsLossesEntities = this.gainsLossesRepository
                                                          .findByCustomerUuid( pageRequest, customerUuid );
        Page<GainsLossesDTO> gainsLossesDTOs = this.entitiesToDTOs( pageRequest, gainsLossesEntities );
        logMethodEnd( methodName, "Found " + gainsLossesEntities.getContent().size() + " to buy" );
        return gainsLossesDTOs;
    }

    public Page<GainsLossesDTO> getGainsLossesListForCustomerUuidAndTickerSymbol( @NotNull final Pageable pageRequest,
                                                                                @NotNull final UUID customerUuid,
                                                                                @NotNull final String tickerSymbol )
    {
        final String methodName = "getGainsLossesListForCustomerUuidAndTickerSymbol";
        logMethodBegin( methodName, pageRequest, customerUuid, tickerSymbol );
        Objects.requireNonNull( customerUuid, "customerUuid cannot be null" );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Page<GainsLossesEntity> gainsLossesEntities = this.gainsLossesRepository
                                                          .findByCustomerUuidAndTickerSymbol( pageRequest, customerUuid,
                                                                                            tickerSymbol );
        Page<GainsLossesDTO> gainsLossesDTOs = this.entitiesToDTOs( pageRequest, gainsLossesEntities );
        logMethodEnd( methodName, "Found " + gainsLossesEntities.getContent().size() + " to buy" );
        return gainsLossesDTOs;
    }

    @Override
    protected GainsLossesDTO createDTO()
    {
        return this.context.getBean( GainsLossesDTO.class );
    }

    @Override
    protected GainsLossesEntity createEntity()
    {
        return this.context.getBean( GainsLossesEntity.class );
    }

    @Override
    protected GainsLossesRepository getRepository()
    {
        return this.gainsLossesRepository;
    }

    @Autowired
    public void setGainsLossesRepository( final GainsLossesRepository gainsLossesRepository )
    {
        this.gainsLossesRepository = gainsLossesRepository;
    }
}
