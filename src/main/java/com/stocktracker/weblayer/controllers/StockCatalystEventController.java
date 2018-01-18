package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockQuoteUnavailableException;
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

/**
 * This is the REST Controller for all Stock Catalyst Event methods.
 */
@RestController
@CrossOrigin
public class StockCatalystEventController implements MyLogger
{
    private static final String CONTEXT_URL = "/stockCatalystEvent";
    private StockCatalystEventEntityService stockCatalystEventService;

    /**
     * Get all of the stock catalyst events for a single customer.
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/customer/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public Page<StockCatalystEventDTO> getStockCatalystEventsForCustomerId( final Pageable pageRequest,
                                                                            @PathVariable int customerId )
    {
        final String methodName = "getStockCatalystEventsForCustomerId";
        logMethodBegin( methodName, customerId );
        Page<StockCatalystEventDTO> stockCatalystEventDTOs = this.stockCatalystEventService
                                                                 .getStockCatalystEventsForCustomerId( pageRequest, customerId );
        logMethodEnd( methodName, "stockCatalystEvent size: " + stockCatalystEventDTOs.getContent().size() );
        return stockCatalystEventDTOs;
    }

    /**
     * Get all of the stock catalyst events for a customer and a ticker symbol
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/tickerSymbol/{tickerSymbol}/customer/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public Page<StockCatalystEventDTO> getStockCatalystEventsForCustomerIdAndTickerSymbol( final Pageable pageRequest,
                                                                                           @PathVariable int customerId,
                                                                                           @PathVariable String tickerSymbol )
    {
        final String methodName = "getStockCatalystEventsForCustomerIdAndTickerSymbol";
        logMethodBegin( methodName, customerId, tickerSymbol );
        Page<StockCatalystEventDTO> stockCatalystEventDTOs = this.stockCatalystEventService
            .getStockCatalystEventsForCustomerIdAndTickerSymbol( pageRequest, customerId, tickerSymbol );
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
    public StockCatalystEventDTO getStockCatalystEvent( @PathVariable int stockCatalystEventId )
    {
        final String methodName = "getStockCatalystEvent";
        logMethodBegin( methodName );
        StockCatalystEventDTO stockCatalystEventDTO = this.stockCatalystEventService.getStockCatalystEvent( stockCatalystEventId );
        logMethodEnd( methodName, stockCatalystEventDTO );
        return stockCatalystEventDTO;
    }

    /**
     * Deletes a stock summary entity
     * @param stockCatalystEventId
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/id/{stockCatalystEventId}/customer/{customerId}",
                     method = RequestMethod.DELETE,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<Void> deleteStockCatalystEvent( @PathVariable int stockCatalystEventId,
                                                          @PathVariable int customerId )
    {
        final String methodName = "deleteStockCatalystEvent";
        logMethodBegin( methodName, customerId, stockCatalystEventId );
        this.stockCatalystEventService.deleteStockCatalystEvent( stockCatalystEventId );
        logMethodEnd( methodName );
        return new ResponseEntity<>( HttpStatus.OK );
    }

    /**
     * Create a stock summary entity.
     * @param stockCatalystEventDTO
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/customer/{customerId}",
                     method = RequestMethod.POST )
    public ResponseEntity<StockCatalystEventDTO> addStockCatalystEvent( @PathVariable Integer customerId,
                                                                        @RequestBody StockCatalystEventDTO stockCatalystEventDTO )
        throws StockNotFoundException,
               StockQuoteUnavailableException
    {
        final String methodName = "addStockCatalystEvent";
        logMethodBegin( methodName, customerId, stockCatalystEventDTO );
        StockCatalystEventDTO newStockCatalystEventDTO = this.stockCatalystEventService
                                                             .saveStockCatalystEvent( stockCatalystEventDTO );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( newStockCatalystEventDTO ).toUri());
        logMethodEnd( methodName );
        return new ResponseEntity<>( newStockCatalystEventDTO, httpHeaders, HttpStatus.CREATED );
    }

    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/customer/{customerId}",
                     method = RequestMethod.PUT )
    public ResponseEntity<StockCatalystEventDTO> saveStockCatalystEvent( @PathVariable int customerId,
                                                                         @RequestBody StockCatalystEventDTO portfolioStockDTO )
        throws StockNotFoundException,
               StockQuoteUnavailableException
    {
        final String methodName = "saveStockCatalystEvent";
        logMethodBegin( methodName, customerId, portfolioStockDTO );
        /*
         * Save the stock
         */
        StockCatalystEventDTO returnStockCatalystEventDTO = this.stockCatalystEventService
                                                                .saveStockCatalystEvent( portfolioStockDTO );
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
