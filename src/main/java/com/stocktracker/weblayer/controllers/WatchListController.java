package com.stocktracker.weblayer.controllers;

import com.fasterxml.uuid.impl.UUIDUtil;
import com.stocktracker.common.exceptions.CustomerNotFoundException;
import com.stocktracker.common.exceptions.DuplicateEntityException;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.NotAuthorizedException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.servicelayer.service.WatchListEntityService;
import com.stocktracker.weblayer.dto.WatchListDTO;
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

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * This is the REST Controller for all Watch List  methods.
 */
@RestController
@CrossOrigin
public class WatchListController extends AbstractController
{
    private static final String CONTEXT_URL = "/watchList";
    @Autowired
    private WatchListEntityService watchListService;

    /**
     * Get all of the watch lists for a customer
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/customerId/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public List<WatchListDTO> getWatchLists( final @NotNull @PathVariable String customerId )
        throws CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "getWatchLists";
        logMethodBegin( methodName, customerId );
        this.validateCustomerId( customerId );
        final List<WatchListDTO> watchListDTOs = this.watchListService
                                                     .getWatchListsForCustomerUuid( UUIDUtil.uuid( customerId ));
        logMethodEnd( methodName, "watchListDTOs size: " + watchListDTOs.size() );
        return watchListDTOs;
    }

    /**
     * Get a single stock to buy
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/id/{watchListId}/customerId/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public WatchListDTO getWatchList( @PathVariable String watchListId,
                                      @PathVariable String customerId )
        throws VersionedEntityNotFoundException,
               CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "getWatchList";
        logMethodBegin( methodName, watchListId, customerId );
        this.validateCustomerId( customerId );
        WatchListDTO watchListDTO = this.watchListService
                                        .getDTO( UUIDUtil.uuid( watchListId ));
        logMethodEnd( methodName, watchListDTO );
        return watchListDTO;
    }

    /**
     * Deletes a watch list
     * @param watchListId
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/id/{watchListId}/customerId/{customerId}",
                     method = RequestMethod.DELETE,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<Void> deleteWatchList( @PathVariable String watchListId,
                                                 @PathVariable String customerId )
        throws VersionedEntityNotFoundException,
               CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "deleteWatchList";
        logMethodBegin( methodName, watchListId, customerId );
        this.validateCustomerId( customerId );
        this.watchListService.deleteEntity( UUIDUtil.uuid( watchListId ));
        logMethodEnd( methodName );
        return new ResponseEntity<>( HttpStatus.OK );
    }

    /**
     * Create a new watch list
     * @param watchListDTO
     * @return
     * @throws EntityVersionMismatchException
     * @throws DuplicateEntityException
     * @throws VersionedEntityNotFoundException
     */
    @RequestMapping( value = CONTEXT_URL + "/customerId/{customerId}",
                     method = RequestMethod.POST )
    public ResponseEntity<WatchListDTO> addWatchList( @PathVariable String customerId,
                                                      @RequestBody WatchListDTO watchListDTO )
        throws EntityVersionMismatchException,
               VersionedEntityNotFoundException,
               DuplicateEntityException,
               CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "addWatchList";
        logMethodBegin( methodName, customerId, watchListDTO );
        this.validateCustomerId( customerId );
        final WatchListDTO newWatchListDTO = this.watchListService
                                                 .addDTO( watchListDTO );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( newWatchListDTO ).toUri());
        logMethodEnd( methodName );
        return new ResponseEntity<>( newWatchListDTO, httpHeaders, HttpStatus.CREATED );
    }

    /**
     * Save a watch list
     * @param watchListDTO
     * @return
     * @throws EntityVersionMismatchException
     * @throws DuplicateEntityException
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/id/{watchListId}/customerId/{customerId}",
                     method = RequestMethod.PUT )
    public ResponseEntity<WatchListDTO> saveWatchList( @PathVariable String watchListId,
                                                       @PathVariable String customerId,
                                                       @RequestBody WatchListDTO watchListDTO )
        throws EntityVersionMismatchException,
               DuplicateEntityException,
               CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "saveWatchList";
        logMethodBegin( methodName, customerId, watchListId, watchListDTO );
        this.validateCustomerId( customerId );
        /*
         * Save the stock
         */
        final WatchListDTO returnWatchListDTO = this.watchListService
                                                    .saveDTO( watchListDTO );
        /*
         * send the response
         */
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( returnWatchListDTO ).toUri());
        logMethodEnd( methodName, returnWatchListDTO );
        return new ResponseEntity<>( watchListDTO, httpHeaders, HttpStatus.CREATED );
    }
}
