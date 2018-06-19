package com.stocktracker.weblayer.controllers;

import com.fasterxml.uuid.impl.UUIDUtil;
import com.stocktracker.common.exceptions.CustomerNotFoundException;
import com.stocktracker.common.exceptions.DuplicateEntityException;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.NotAuthorizedException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.servicelayer.service.StockNoteSourceEntityService;
import com.stocktracker.weblayer.dto.StockNoteSourceDTO;
import org.springframework.beans.factory.annotation.Autowired;
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
import retrofit2.http.Path;

import java.util.List;
import java.util.Objects;

/**
 * This is the control (entry point) for all REST services for stock notes sources.
 */
@RestController
@CrossOrigin
public class StockNotesSourceController extends AbstractController
{
    private static final String CONTEXT_URL = "/stockNotesSource";

    @Autowired
    private StockNoteSourceEntityService stockNoteSourceService;

    /**
     * Get all of the stocks notes sources for a customer
     *
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/customerId/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<StockNoteSourceDTO> getStockNotesSources( final @PathVariable String customerId )
    {
        final String methodName = "getStockNotesSources";
        logMethodBegin( methodName, customerId );
        List<StockNoteSourceDTO> stockNoteSourceDTOs = this.stockNoteSourceService
                                                           .getStockNoteSources( UUIDUtil.uuid( customerId ));
        logMethodEnd( methodName, stockNoteSourceDTOs.size() );
        return stockNoteSourceDTOs;
    }

    /**
     * Get all of the stocks notes sources for a customer
     *
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/id/{id}/customerId/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public StockNoteSourceDTO getStockNotesSource( final @PathVariable String id,
                                                   final @PathVariable String customerId )
        throws VersionedEntityNotFoundException
    {
        final String methodName = "getStockNotesSource";
        logMethodBegin( methodName, customerId, id );
        StockNoteSourceDTO stockNoteSourceDTO = this.stockNoteSourceService
                                                    .getDTO( id );
        logMethodEnd( methodName, stockNoteSourceDTO );
        return stockNoteSourceDTO;
    }

    /**
     * Update a stock notes source.
     * @param stockNoteSourceDTO
     * @param customerId
     * @param id
     * @return
     * @throws CustomerNotFoundException
     * @throws NotAuthorizedException
     * @throws DuplicateEntityException
     */
    @RequestMapping( value = CONTEXT_URL + "/id/{id}/customerId/{customerId}",
                     method = RequestMethod.PUT )
    public ResponseEntity<StockNoteSourceDTO> updateStockNoteSource( @RequestBody final StockNoteSourceDTO stockNoteSourceDTO,
                                                                     @PathVariable( "customerId" ) final String customerId,
                                                                     @PathVariable( "id" ) final String id )
        throws CustomerNotFoundException,
               NotAuthorizedException,
               DuplicateEntityException
    {
        final String methodName = "updateStockNoteSource";
        logMethodBegin( methodName, customerId, id );
        this.validateCustomerId( customerId );
        StockNoteSourceDTO returnStockNotesSourceDTO = this.stockNoteSourceService
                                                           .saveDTO( stockNoteSourceDTO );
        logDebug( methodName, "returnStockDTO: ", returnStockNotesSourceDTO );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder.fromCurrentRequest()
                                                            .path( "" )
                                                            .buildAndExpand( stockNoteSourceDTO )
                                                            .toUri());
        logMethodEnd( methodName, returnStockNotesSourceDTO );
        return new ResponseEntity<>( returnStockNotesSourceDTO, httpHeaders, HttpStatus.CREATED );
    }

    /**
     * Delete a stock note source
     *
     * @return The stock notes source that was added
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/id/{id}/customerId/{customerId}",
                     method = RequestMethod.DELETE )
    public ResponseEntity<Void> deleteStockNote( @PathVariable( "customerId" ) final String customerId,
                                                 @PathVariable( "id" ) final String id )
        throws VersionedEntityNotFoundException,
               CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "deleteStockNoteSourace";
        logMethodBegin( methodName, customerId, id );
        this.validateCustomerId( customerId );
        this.stockNoteSourceService
            .deleteEntity( id );
        logMethodEnd( methodName );
        return new ResponseEntity<>( HttpStatus.OK );
    }

    /**
     * Add a stock note source to the database
     * @param stockNoteSourceDTO
     * @return The stock that was added
     * @return
     * @throws EntityVersionMismatchException
     * @throws DuplicateEntityException
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/customerId/customerId",
                     method = RequestMethod.POST )
    public ResponseEntity<StockNoteSourceDTO> addStockNoteSource( @RequestBody StockNoteSourceDTO stockNoteSourceDTO )
        throws EntityVersionMismatchException,
               DuplicateEntityException
    {
        final String methodName = "addStockNotesSource";
        logMethodBegin( methodName, stockNoteSourceDTO );
        validateStockNoteSourceDTO( stockNoteSourceDTO );
        StockNoteSourceDTO returnStockSourceDTO = this.stockNoteSourceService.createStockNoteSource( stockNoteSourceDTO );
        logDebug( methodName, "returnStockDTO: ", returnStockSourceDTO );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( stockNoteSourceDTO ).toUri());
        logMethodEnd( methodName, returnStockSourceDTO );
        return new ResponseEntity<>( returnStockSourceDTO, httpHeaders, HttpStatus.CREATED );
    }

    private void validateStockNoteSourceDTO( final StockNoteSourceDTO stockNoteSourceDTO )
    {
        Objects.requireNonNull( stockNoteSourceDTO.getId(), "stockNoteSourceDTO.id cannot be null" );
        Objects.requireNonNull( stockNoteSourceDTO.getName(), "stockNoteSourceDTO.name cannot be null" );
        Objects.requireNonNull( stockNoteSourceDTO.getCustomerId(), "stockNoteSourceDTO.customerId cannot be null" );
    }
}
