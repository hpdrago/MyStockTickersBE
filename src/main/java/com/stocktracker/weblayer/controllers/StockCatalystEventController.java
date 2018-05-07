package com.stocktracker.weblayer.controllers;

import com.fasterxml.uuid.impl.UUIDUtil;
import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.CustomerNotFoundException;
import com.stocktracker.common.exceptions.DuplicateEntityException;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.NotAuthorizedException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.servicelayer.service.StockCatalystEventEntityService;
import com.stocktracker.weblayer.dto.StockCatalystEventDTO;
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

import java.util.UUID;

/**
 * This is the REST Controller for all Stock Catalyst Event methods.
 */
@RestController
@CrossOrigin
public class StockCatalystEventController extends AbstractController implements MyLogger
{
    private static final String CONTEXT_URL = "/stockCatalystEvent";
    private StockCatalystEventEntityService stockCatalystEventService;

    /**
     * Get all of the stock catalyst events for a single customer.
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/page/customerId/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public Page<StockCatalystEventDTO> getStockCatalystEventsForCustomerUuid( final Pageable pageRequest,
                                                                              @PathVariable String customerId )
        throws CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "getStockCatalystEventsForCustomerUuid";
        logMethodBegin( methodName, customerId );
        final UUID customerUuid = this.validateCustomerId( customerId );
        Page<StockCatalystEventDTO> stockCatalystEventDTOs = this.stockCatalystEventService
                                                                 .getStockCatalystEventsForCustomerUuid( pageRequest, customerUuid );
        logMethodEnd( methodName, "stockCatalystEvent size: " + stockCatalystEventDTOs.getContent().size() );
        return stockCatalystEventDTOs;
    }

    /**
     * Get all of the stock catalyst events for a customer and a ticker symbol
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/page/tickerSymbol/{tickerSymbol}/customerId/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public Page<StockCatalystEventDTO> getStockCatalystEventsForCustomerUuidAndTickerSymbol( final Pageable pageRequest,
                                                                                           @PathVariable String customerId,
                                                                                           @PathVariable String tickerSymbol )
        throws CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "getStockCatalystEventsForCustomerUuidAndTickerSymbol";
        logMethodBegin( methodName, customerId, tickerSymbol );
        final UUID customerUuid = this.validateCustomerId( customerId );
        Page<StockCatalystEventDTO> stockCatalystEventDTOs = this.stockCatalystEventService
            .getStockCatalystEventsForCustomerUuidAndTickerSymbol( pageRequest,
                                                                   customerUuid,
                                                                   tickerSymbol );
        logMethodEnd( methodName, "stockCatalystEvent size: " + stockCatalystEventDTOs.getContent().size() );
        return stockCatalystEventDTOs;
    }

    /**
     * Get a single stock catalyst event
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/id/{stockCatalystEventId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public StockCatalystEventDTO getStockCatalystEvent( @PathVariable String stockCatalystEventId )
        throws VersionedEntityNotFoundException
    {
        final String methodName = "getStockCatalystEvent";
        logMethodBegin( methodName );
        final StockCatalystEventDTO stockCatalystEventDTO = this.stockCatalystEventService
                                                                .getDTO( UUIDUtil.uuid( stockCatalystEventId ));
        logMethodEnd( methodName, stockCatalystEventDTO );
        return stockCatalystEventDTO;
    }

    /**
     * Deletes a stock summary entity
     * @param stockCatalystEventId
     * @throws VersionedEntityNotFoundException
     * @throws CustomerNotFoundException
     * @throws NotAuthorizedException
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/id/{stockCatalystEventId}/customerId/{customerId}",
                     method = RequestMethod.DELETE,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<Void> deleteStockCatalystEvent( @PathVariable String stockCatalystEventId,
                                                          @PathVariable String customerId )
        throws VersionedEntityNotFoundException,
               CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "deleteStockCatalystEvent";
        logMethodBegin( methodName, customerId, stockCatalystEventId );
        this.validateCustomerId( customerId );
        this.stockCatalystEventService
            .deleteEntity( stockCatalystEventId );
        logMethodEnd( methodName );
        return new ResponseEntity<>( HttpStatus.OK );
    }

    /**
     * Create a stock summary entity.
     * @param customerId
     * @param stockCatalystEventDTO
     * @return
     * @throws CustomerNotFoundException
     * @throws NotAuthorizedException
     * @throws EntityVersionMismatchException
     * @throws DuplicateEntityException
     */
    @RequestMapping( value = CONTEXT_URL + "/customerId/{customerId}",
                     method = RequestMethod.POST )
    public ResponseEntity<StockCatalystEventDTO> addStockCatalystEvent( @PathVariable String customerId,
                                                                        @RequestBody StockCatalystEventDTO stockCatalystEventDTO )
        throws EntityVersionMismatchException,
               CustomerNotFoundException,
               NotAuthorizedException,
               DuplicateEntityException
    {
        final String methodName = "addStockCatalystEvent";
        logMethodBegin( methodName, customerId, stockCatalystEventDTO );
        this.validateCustomerId( customerId );
        StockCatalystEventDTO newStockCatalystEventDTO = this.stockCatalystEventService
                                                             .addDTO( stockCatalystEventDTO );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( newStockCatalystEventDTO ).toUri());
        logMethodEnd( methodName );
        return new ResponseEntity<>( newStockCatalystEventDTO, httpHeaders, HttpStatus.CREATED );
    }

    /**
     * Save the catalyst event to the database.
     * @param customerId
     * @param portfolioStockDTO
     * @return
     * @throws EntityVersionMismatchException
     * @throws CustomerNotFoundException
     * @throws NotAuthorizedException
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/id/{stockCatalystEventId}/customerId/{customerId}",
                     method = RequestMethod.PUT )
    public ResponseEntity<StockCatalystEventDTO> saveStockCatalystEvent( @PathVariable String stockCatalystEventId,
                                                                         @PathVariable String customerId,
                                                                         @RequestBody StockCatalystEventDTO portfolioStockDTO )
        throws EntityVersionMismatchException,
               CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "saveStockCatalystEvent";
        logMethodBegin( methodName, stockCatalystEventId, customerId, portfolioStockDTO );
        this.validateCustomerId( customerId );
        if ( portfolioStockDTO.getId() != stockCatalystEventId )
        {
            throw new IllegalArgumentException( String.format( "id argument %d does not match DTO content.id %d",
                                                               stockCatalystEventId, portfolioStockDTO.getId() ));
        }
        /*
         * Save the stock
         */
        StockCatalystEventDTO returnStockCatalystEventDTO = this.stockCatalystEventService
                                                                .saveDTO( portfolioStockDTO );
        /*
         * send the response
         */
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( returnStockCatalystEventDTO ).toUri());
        logMethodEnd( methodName, returnStockCatalystEventDTO );
        return new ResponseEntity<>( portfolioStockDTO, httpHeaders, HttpStatus.CREATED );
    }

    @Autowired
    public void setStockCatalystEventService( final StockCatalystEventEntityService stockCatalystEventService )
    {
        this.stockCatalystEventService = stockCatalystEventService;
    }
}
