package com.stocktracker.weblayer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.uuid.impl.UUIDUtil;
import com.stocktracker.common.exceptions.CustomerNotFoundException;
import com.stocktracker.common.exceptions.DuplicateEntityException;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.GainsLossesNotFoundException;
import com.stocktracker.common.exceptions.LinkedAccountNotFoundException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.servicelayer.service.LinkedAccountEntityService;
import com.stocktracker.servicelayer.service.gainslosses.GainsLossesEntityService;
import com.stocktracker.weblayer.dto.GainsLossesDTO;
import com.stocktracker.weblayer.dto.GainsLossesImportConfigurationDTO;
import com.stocktracker.weblayer.dto.StringDTO;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.UUID;

/**
 * This is the REST Controller for all Stock ToBuy methods.
 */
@RestController
@CrossOrigin
public class GainsLossesController extends AbstractController
{
    private static final String CONTEXT_URL = "/gainsLosses";

    @Autowired
    private GainsLossesEntityService gainsLossesService;

    @Autowired
    private LinkedAccountEntityService linkedAccountEntityService;

    /**
     * Import/upload a spreadsheet of gains/losses.
     * @param customerId
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/importResults/customerId/{customerId}",
                     method = RequestMethod.GET )
    public ResponseEntity<StringDTO> getImportResults( @PathVariable final String customerId )
        throws CustomerNotFoundException
    {
        final String methodName = "getImportResults";
        logMethodBegin( methodName, customerId );
        final UUID customerUuid = this.getCustomerUuid( customerId );
        final StringDTO results = this.gainsLossesService
                                      .getImportResults( customerUuid );
        logMethodEnd( methodName, results );
        return ResponseEntity.ok( results );
    }

    /**
     * Import/upload a spreadsheet of gains/losses.
     * @param customerId
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/import/customerId/{customerId}",
                     method=RequestMethod.POST,
                     consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
                     produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> importGainsLosses( @PathVariable final String customerId,
                                                   @RequestParam final String linkedAccountId,
                                                   @RequestParam( value = "configuration") final String configuration,
                                                   @RequestParam( "files" ) MultipartFile[] multipartFiles )
        throws IOException,
               LinkedAccountNotFoundException,
               CustomerNotFoundException,
               InvalidFormatException
    {
        final String methodName = "handleFileUpload";
        logMethodBegin( methodName, customerId, linkedAccountId, multipartFiles.length, configuration );
        final GainsLossesImportConfigurationDTO gainsLossesImportConfigurationDTO =
            new ObjectMapper().readValue( configuration, GainsLossesImportConfigurationDTO.class );
        logDebug( methodName, "configurationDTO: {0}", gainsLossesImportConfigurationDTO );
        final LinkedAccountEntity linkedAccountEntity = this.linkedAccountEntityService
                                                            .getLinkedAccountEntity( UUIDUtil.uuid( linkedAccountId ) );
        for ( final MultipartFile multipartFile: multipartFiles )
        {
            logDebug( methodName, "{0} size: {0}", multipartFile.getOriginalFilename(), multipartFile.getSize() );
        }
        final UUID customerUuid = this.getCustomerUuid( customerId );
        this.gainsLossesService
            .importGainsLosses( customerUuid, linkedAccountEntity, multipartFiles, gainsLossesImportConfigurationDTO );
        return new ResponseEntity<>( HttpStatus.OK );
    }

    /**
     * Get all of the stock to buy for a customer
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/page/customerId/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public Page<GainsLossesDTO> getStockGainsLossesPage( final Pageable pageRequest,
                                                         final @NotNull @PathVariable String customerId )
    {
        final String methodName = "getStockGainsLossesPage";
        logMethodBegin( methodName, pageRequest, customerId );
        Page<GainsLossesDTO> gainsLossesDTOs = this.gainsLossesService
                                                   .getGainsLosses( pageRequest, UUIDUtil.uuid( customerId ));
        logDebug( methodName, "StocksToBuy: {0}", gainsLossesDTOs.getContent() );
        logMethodEnd( methodName, "gainsLossesDTOs size: " + gainsLossesDTOs.getContent().size() );
        return gainsLossesDTOs;
    }

    /**
     * Get all of the stock to buy for a customer and a ticker symbol
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/page/tickerSymbol/{tickerSymbol}/customerId/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public Page<GainsLossesDTO> getStockGainsLossesPage( final Pageable pageRequest,
                                                         final @NotNull @PathVariable String customerId,
                                                         final @NotNull @PathVariable String linkedAccountId,
                                                         final @NotNull @PathVariable String tickerSymbol )
    {
        final String methodName = "getStockGainsLossesPage";
        logMethodBegin( methodName, pageRequest, customerId, tickerSymbol );
        Page<GainsLossesDTO> gainsLossesDTOs = this.gainsLossesService
                                                   .getGainsLosses( pageRequest,
                                                                    UUIDUtil.uuid( customerId ),
                                                                    UUIDUtil.uuid( linkedAccountId ),
                                                                    tickerSymbol );
        logDebug( methodName, "StocksToBuy: {0}", gainsLossesDTOs.getContent() );
        logMethodEnd( methodName, "gainsLossesDTOs size: " + gainsLossesDTOs.getContent().size() );
        return gainsLossesDTOs;
    }

    /**
     * Get all of the stock to buy for a customer and a
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/tickerSymbol/{tickerSymbol}/linkedAccountId/{linkedAccountId}/customerId/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public GainsLossesDTO getStockGainsLosses( @NotNull final Pageable pageRequest,
                                               @NotNull @PathVariable String customerId,
                                               @NotNull @PathVariable String linkedAccountId,
                                               @NotNull @PathVariable String tickerSymbol )
    {
        final String methodName = "getStockGainsLossesForTickerSymbol";
        logMethodBegin( methodName, pageRequest, customerId, tickerSymbol );
        final GainsLossesDTO gainsLossesDTO = this.gainsLossesService
                                                  .getGainsLosses(
                                                      UUIDUtil.uuid( customerId ),
                                                      UUIDUtil.uuid( linkedAccountId ),
                                                      tickerSymbol );
        logMethodEnd( methodName, gainsLossesDTO );
        return gainsLossesDTO;
    }

    /**
     * Get a single stock to buy
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/id/{gainsLossesId}/customerId/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public GainsLossesDTO getGainsLosses( @PathVariable String gainsLossesId,
                                          @PathVariable String customerId )
        throws GainsLossesNotFoundException
    {
        final String methodName = "getGainsLosses";
        logMethodBegin( methodName, gainsLossesId, customerId );
        GainsLossesDTO gainsLossesDTO = null;
        try
        {
            gainsLossesDTO = this.gainsLossesService
                                 .getDTO( UUIDUtil.uuid( gainsLossesId ));
        }
        catch( VersionedEntityNotFoundException e )
        {
            throw new GainsLossesNotFoundException( gainsLossesId, e );
        }
        logMethodEnd( methodName, gainsLossesDTO );
        return gainsLossesDTO;
    }

    /**
     * Deletes a stock to buy entity
     * @param gainsLossesId
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/id/{gainsLossesId}/customerId/{customerId}",
                     method = RequestMethod.DELETE,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<Void> deleteGainsLosses( @PathVariable String gainsLossesId,
                                                   @PathVariable String customerId )
        throws GainsLossesNotFoundException
    {
        final String methodName = "deleteGainsLosses";
        logMethodBegin( methodName, gainsLossesId, customerId );
        try
        {
            this.gainsLossesService
                .deleteEntity( gainsLossesId );
        }
        catch( VersionedEntityNotFoundException e )
        {
            throw new GainsLossesNotFoundException( gainsLossesId );
        }
        logMethodEnd( methodName );
        return new ResponseEntity<>( HttpStatus.OK );
    }

    /**
     * Create a stock to buy entity.
     * @param gainsLossesDTO
     * @return
     * @throws DuplicateEntityException
     * @throws EntityVersionMismatchException
     */
    @RequestMapping( value = CONTEXT_URL + "/customerId/{customerId}",
                     method = RequestMethod.POST )
    public ResponseEntity<GainsLossesDTO> addGainsLosses( @PathVariable String customerId,
                                                          @RequestBody GainsLossesDTO gainsLossesDTO )
        throws EntityVersionMismatchException,
               DuplicateEntityException
    {
        final String methodName = "addGainsLosses";
        logMethodBegin( methodName, customerId, gainsLossesDTO );
        final GainsLossesDTO newGainsLossesDTO = this.gainsLossesService
                                                     .addDTO( gainsLossesDTO );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( newGainsLossesDTO ).toUri());
        logMethodEnd( methodName );
        return new ResponseEntity<>( newGainsLossesDTO, httpHeaders, HttpStatus.CREATED );
    }

    /**
     * Save the stock to buy.
     * @param gainsLossesDTO
     * @return
     * @throws EntityVersionMismatchException
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/id/{gainsLossesId}/customerId/{customerId}",
                     method = RequestMethod.PUT )
    public ResponseEntity<GainsLossesDTO> saveGainsLosses( @PathVariable String gainsLossesId,
                                                           @PathVariable String customerId,
                                                           @RequestBody GainsLossesDTO gainsLossesDTO )
        throws EntityVersionMismatchException
    {
        final String methodName = "saveGainsLosses";
        logMethodBegin( methodName, customerId, gainsLossesId, gainsLossesDTO );
        /*
         * Save the stock
         */
        final GainsLossesDTO returnGainsLossesDTO = this.gainsLossesService.saveDTO( gainsLossesDTO );
        /*
         * send the response
         */
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( returnGainsLossesDTO ).toUri());
        logMethodEnd( methodName, returnGainsLossesDTO );
        return new ResponseEntity<>( gainsLossesDTO, httpHeaders, HttpStatus.CREATED );
    }
}
