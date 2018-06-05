package com.stocktracker.servicelayer.service.gainslosses;

import com.stocktracker.repositorylayer.entity.GainsLossesEntity;
import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.repositorylayer.repository.GainsLossesRepository;
import com.stocktracker.servicelayer.service.LinkedAccountEntityService;
import com.stocktracker.servicelayer.service.UuidEntityService;
import com.stocktracker.weblayer.dto.GainsLossesDTO;
import com.stocktracker.weblayer.dto.GainsLossesImportConfigurationDTO;
import com.stocktracker.weblayer.dto.LinkedAccountDTO;
import com.stocktracker.weblayer.dto.StringDTO;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;
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
    @Autowired
    private GainsLossesRepository gainsLossesRepository;

    @Autowired
    private GainsLossesImportService gainsLossesImportService;

    @Autowired
    private LinkedAccountEntityService linkedAccountEntityService;

    /**
     * Contains the import results for the customer.  The import is a two step process, first step is to import
     * and then the results are then obtained.  This is due to the way the PrimeNG FileUpload component works.
     */
    private GainsLossesResultsMap gainsLossesResultsMap = new GainsLossesResultsMap();

    /**
     * Constructor.
     * @param customerUuid
     * @param linkedAccountEntity
     * @param multipartFiles
     * @param gainsLossesImportConfigurationDTO
     */
    public void importGainsLosses( final UUID customerUuid,
                                   final LinkedAccountEntity linkedAccountEntity,
                                   final MultipartFile[] multipartFiles,
                                   final GainsLossesImportConfigurationDTO gainsLossesImportConfigurationDTO )
        throws IOException,
               InvalidFormatException
    {
        final String methodName = "importGainsLosses";
        logMethodBegin( methodName, customerUuid, linkedAccountEntity.getAccountNumber(), gainsLossesImportConfigurationDTO );
        this.gainsLossesResultsMap
            .createEntry( customerUuid );
        StringBuilder results = new StringBuilder();
        try
        {
            if ( gainsLossesImportConfigurationDTO.isClearEntries() )
            {
                logDebug( methodName, "Deleting all gains and losses..." );
                this.gainsLossesRepository
                    .deleteAll();
            }
            for ( final MultipartFile multipartFile : multipartFiles )
            {
                this.gainsLossesImportService
                    .importGainsLosses( customerUuid, linkedAccountEntity,
                                        multipartFile, gainsLossesImportConfigurationDTO, results );
            }
            this.gainsLossesResultsMap
                .addResults( customerUuid, results.toString() );
        }
        /*
         * Catch the exception and add the results to the import results map so the GUI can retrieve the results.
         */
        catch( Throwable throwable )
        {
            this.logError( methodName, throwable );
            results.append( "\n" );
            results.append( throwable.getMessage() == null ? throwable.getClass().getName()
                                                           : throwable.getMessage() );
            this.gainsLossesResultsMap
                .addResults( customerUuid, results.toString() );
            throw throwable;
        }
        logMethodEnd( methodName );
    }

    /**
     * Get the import results from a previous call to import gains and losses.  This method will block and wait until
     * the import has been completed.
     * @param customerUuid
     * @return
     */
    public StringDTO getImportResults( final UUID customerUuid )
    {
        final String methodName = "getImportResults";
        logMethodBegin( methodName, customerUuid );
        final StringDTO stringDTO = this.context.getBean( StringDTO.class );
        final String results = this.gainsLossesResultsMap
                                   .getResults( customerUuid );
        stringDTO.setValue( results );
        logMethodEnd( methodName, stringDTO );
        return stringDTO;
    }

    /**
     * Searches for the one entry for the customer UUID and ticker symbol -- this is a unique combination.
     * @param customerUuid
     * @return
     */
    public GainsLossesDTO getGainsLosses( @NotNull final UUID customerUuid,
                                          @NotNull final UUID linkedAccountUuid,
                                          @NotNull final String tickerSymbol )
    {
        final String methodName = "getGainsLossesListForCustomerUuid";
        logMethodBegin( methodName, customerUuid, linkedAccountUuid, tickerSymbol );
        Objects.requireNonNull( customerUuid, "customerUuid cannot be null" );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        final GainsLossesEntity gainsLossesEntity = this.gainsLossesRepository
                                                        .findByCustomerUuidAndLinkedAccountUuidAndTickerSymbol(
                                                            customerUuid, linkedAccountUuid, tickerSymbol );
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
    public Page<GainsLossesDTO> getGainsLosses( final Pageable pageRequest,
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

    public Page<GainsLossesDTO> getGainsLosses( @NotNull final Pageable pageRequest,
                                                @NotNull final UUID customerUuid,
                                                @NotNull final UUID linkedAccountUuid,
                                                @NotNull final String tickerSymbol )
    {
        final String methodName = "getGainsLossesListForCustomerUuidAndTickerSymbol";
        logMethodBegin( methodName, pageRequest, customerUuid, linkedAccountUuid, tickerSymbol );
        Objects.requireNonNull( customerUuid, "customerUuid cannot be null" );
        Objects.requireNonNull( linkedAccountUuid, "customerUuid cannot be null" );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Page<GainsLossesEntity> gainsLossesEntities = this.gainsLossesRepository
                                                          .findByCustomerUuidAndLinkedAccountUuidAndTickerSymbol(
                                                              pageRequest, customerUuid, linkedAccountUuid, tickerSymbol );
        Page<GainsLossesDTO> gainsLossesDTOs = this.entitiesToDTOs( pageRequest, gainsLossesEntities );
        logMethodEnd( methodName, "Found " + gainsLossesEntities.getContent().size() + " to buy" );
        return gainsLossesDTOs;
    }

    @Override
    public GainsLossesDTO entityToDTO( final GainsLossesEntity entity )
    {
        final GainsLossesDTO dto = super.entityToDTO( entity );
        final LinkedAccountDTO linkedAccountDTO = linkedAccountEntityService.entityToDTO( entity.getLinkedAccountByLinkedAccountUuid() );
        dto.setLinkedAccount( linkedAccountDTO );
        return dto;
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
}
