package com.stocktracker.weblayer.controllers;

import com.fasterxml.uuid.impl.UUIDUtil;
import com.stocktracker.common.exceptions.CustomerNotFoundException;
import com.stocktracker.common.exceptions.DuplicateEntityException;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.NotAuthorizedException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.servicelayer.service.WatchListStockEntityService;
import com.stocktracker.weblayer.dto.WatchListStockDTO;
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

import java.util.List;

/**
 * This is the REST Controller for all Watch List  methods.
 */
@RestController
@CrossOrigin
public class WatchListStockController extends AbstractController
{
    private static final String CONTEXT_URL = "/watchListStock";
    @Autowired
    private WatchListStockEntityService watchListStockService;

    /**
     * Retrieves the stocks for a single watch list.
     * @param watchListId
     * @param customerId
     * @return
     * @throws VersionedEntityNotFoundException
     * @throws CustomerNotFoundException
     * @throws NotAuthorizedException
     */
    @RequestMapping( value = CONTEXT_URL + "/watchListId/{watchListId}/customerId/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public List<WatchListStockDTO> getWatchListStocks( @PathVariable String watchListId,
                                                       @PathVariable String customerId )
        throws VersionedEntityNotFoundException,
               CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "getWatchListsStocks";
        logMethodBegin( methodName, watchListId, customerId );
        this.validateCustomerId( customerId );
        final List<WatchListStockDTO> watchListStockDTOs = this.watchListStockService
                                                               .getWatchListStocks( UUIDUtil.uuid( watchListId ));
        logMethodEnd( methodName, watchListStockDTOs.size() );
        return watchListStockDTOs;
    }

    /**
     * Delete a watch list stock.
     * @param watchListStockId
     * @param customerId
     * @return
     * @throws VersionedEntityNotFoundException
     * @throws DuplicateEntityException
     * @throws EntityVersionMismatchException
     */
    @RequestMapping( value = CONTEXT_URL + "/id/{watchListStockId}/customerId/{customerId}",
                     method = RequestMethod.DELETE,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<Void> deleteWatchList( @PathVariable String watchListStockId,
                                                 @PathVariable String customerId )
        throws VersionedEntityNotFoundException,
               CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "deleteWatchList";
        logMethodBegin( methodName, watchListStockId, customerId );
        this.validateCustomerId( customerId );
        this.watchListStockService.deleteEntity( UUIDUtil.uuid( watchListStockId ));
        logMethodEnd( methodName );
        return new ResponseEntity<>( HttpStatus.OK );
    }

    /**
     * Create a new watch list stock
     * @param customerId
     * @param watchListStockDTO
     * @return
     * @throws EntityVersionMismatchException
     * @throws VersionedEntityNotFoundException
     * @throws DuplicateEntityException
     * @throws CustomerNotFoundException
     * @throws NotAuthorizedException
     */
    @RequestMapping( value = CONTEXT_URL + "/customerId/{customerId}",
                     method = RequestMethod.POST )
    public ResponseEntity<WatchListStockDTO> addWatchList( @PathVariable String customerId,
                                                           @RequestBody WatchListStockDTO watchListStockDTO )
        throws EntityVersionMismatchException,
               VersionedEntityNotFoundException,
               DuplicateEntityException,
               CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "addWatchList";
        logMethodBegin( methodName, customerId, watchListStockDTO );
        this.validateCustomerId( customerId );
        final WatchListStockDTO newWatchListStockDTO = this.watchListStockService
                                                           .addDTO( watchListStockDTO );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( newWatchListStockDTO ).toUri());
        logMethodEnd( methodName );
        return new ResponseEntity<>( newWatchListStockDTO, httpHeaders, HttpStatus.CREATED );
    }

    /**
     * Save a watch list stock.
     * @param watchListstockDTO
     * @return
     * @throws EntityVersionMismatchException
     * @throws DuplicateEntityException
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/id/{watchListStockId}/customerId/{customerId}",
                     method = RequestMethod.PUT )
    public ResponseEntity<WatchListStockDTO> saveWatchListStock( @PathVariable String watchListStockId,
                                                                 @PathVariable String customerId,
                                                                 @RequestBody WatchListStockDTO watchListstockDTO )
        throws EntityVersionMismatchException,
               DuplicateEntityException
    {
        final String methodName = "saveWatchListStock";
        logMethodBegin( methodName, customerId, watchListStockId, watchListstockDTO );
        /*
         * Save the stock
         */
        final WatchListStockDTO returnWatchListStockDTO = this.watchListStockService
                                                              .saveDTO( watchListstockDTO );
        /*
         * send the response
         */
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( returnWatchListStockDTO ).toUri());
        logMethodEnd( methodName, returnWatchListStockDTO );
        return new ResponseEntity<>( watchListstockDTO, httpHeaders, HttpStatus.CREATED );
    }
}
