package com.stocktracker.weblayer.controllers;

import com.fasterxml.uuid.impl.UUIDUtil;
import com.stocktracker.common.exceptions.DuplicateEntityException;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.GainsLossesNotFoundException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.servicelayer.service.GainsLossesEntityService;
import com.stocktracker.weblayer.dto.GainsLossesDTO;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;

/**
 * This is the REST Controller for all Stock ToBuy methods.
 */
@RestController
@CrossOrigin
public class GainsLossesController extends AbstractController
{
    private static final String CONTEXT_URL = "/gainsLosses";
    private GainsLossesEntityService gainsLossesService;

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
                                                   .getGainsLossesListForCustomerUuid( pageRequest,
                                                                                       UUIDUtil.uuid( customerId ));
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
                                                         final @NotNull @PathVariable String tickerSymbol )
    {
        final String methodName = "getStockGainsLossesPage";
        logMethodBegin( methodName, pageRequest, customerId, tickerSymbol );
        Page<GainsLossesDTO> gainsLossesDTOs = this.gainsLossesService
                                                 .getGainsLossesListForCustomerUuidAndTickerSymbol( pageRequest,
                                                                                    UUIDUtil.uuid( customerId ),
                                                                                                   tickerSymbol );
        logDebug( methodName, "StocksToBuy: {0}", gainsLossesDTOs.getContent() );
        logMethodEnd( methodName, "gainsLossesDTOs size: " + gainsLossesDTOs.getContent().size() );
        return gainsLossesDTOs;
    }

    /**
     * Get all of the stock to buy for a customer and a
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/tickerSymbol/{tickerSymbol}/customerId/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public GainsLossesDTO getStockGainsLosses( @NotNull final Pageable pageRequest,
                                               @NotNull @PathVariable String customerId,
                                               @NotNull @PathVariable String tickerSymbol )
    {
        final String methodName = "getStockGainsLossesForTickerSymbol";
        logMethodBegin( methodName, pageRequest, customerId, tickerSymbol );
        final GainsLossesDTO gainsLossesDTO = this.gainsLossesService
                                                .getByCustomerUuidAndTickerSymbol( UUIDUtil.uuid( customerId ),
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
            this.gainsLossesService.deleteEntity( gainsLossesId );
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

    @Autowired
    public void setGainsLossesService( final GainsLossesEntityService gainsLossesService )
    {
        this.gainsLossesService = gainsLossesService;
    }
}
