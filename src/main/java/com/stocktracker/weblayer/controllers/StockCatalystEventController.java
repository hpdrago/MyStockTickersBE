package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockQuoteUnavailableException;
import com.stocktracker.servicelayer.service.StockCatalystEventService;
import com.stocktracker.weblayer.dto.StockCatalystEventDTO;
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
 * This is the REST Controller for all Stock Catalyst Event methods.
 */
@RestController
@CrossOrigin
public class StockCatalystEventController implements MyLogger
{
    private StockCatalystEventService stockCatalystEventService;

    /**
     * Get all of the stock catalyst events for a single customer.
     * @return
     */
    @RequestMapping( value = "/stockCatalystEvent/customer/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public List<StockCatalystEventDTO> getStockCatalystEventsForCustomerId( @PathVariable int customerId )
    {
        final String methodName = "getStockCatalystEventsForCustomerId";
        logMethodBegin( methodName, customerId );
        List<StockCatalystEventDTO> stockCatalystEventDTOs = this.stockCatalystEventService
                                                                 .getStockCatalystEventsForCustomerId( customerId );
        logMethodEnd( methodName, "stockCatalystEvent size: " + stockCatalystEventDTOs.size() );
        return stockCatalystEventDTOs;
    }

    /**
     * Get all of the stock catalyst events for a customer and a ticker symbol
     * @return
     */
    @RequestMapping( value = "/stockCatalystEvent/customer/{customerId}/{tickerSymbol}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public List<StockCatalystEventDTO> getStockCatalystEventsForCustomerIdAndTickerSymbol( @PathVariable int customerId,
                                                                                           @PathVariable String tickerSymbol )
    {
        final String methodName = "getStockCatalystEventsForCustomerIdAndTickerSymbol";
        logMethodBegin( methodName, customerId, tickerSymbol );
        List<StockCatalystEventDTO> stockCatalystEventDTOs = this.stockCatalystEventService
            .getStockCatalystEventsForCustomerIdAndTickerSymbol( customerId, tickerSymbol );
        logMethodEnd( methodName, "stockCatalystEvent size: " + stockCatalystEventDTOs.size() );
        return stockCatalystEventDTOs;
    }

    /**
     * Get a single stock catalyst event
     * @return
     */
    @RequestMapping( value = "/stockCatalystEvent/{stockCatalystEventId}",
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
    @RequestMapping( value = "/stockCatalystEvent/{stockCatalystEventId}",
                     method = RequestMethod.DELETE,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<Void> deleteStockCatalystEvent( @PathVariable int stockCatalystEventId )
    {
        final String methodName = "deleteStockCatalystEvent";
        logMethodBegin( methodName, stockCatalystEventId );
        this.stockCatalystEventService.deleteStockCatalystEvent( stockCatalystEventId );
        logMethodEnd( methodName );
        return new ResponseEntity<>( HttpStatus.OK );
    }

    /**
     * Create a stock summary entity.
     * @param stockCatalystEventDTO
     * @return
     */
    @RequestMapping( value = "/stockCatalystEvent",
                     method = RequestMethod.POST )
    public ResponseEntity<StockCatalystEventDTO> addStockCatalystEvent( @RequestBody StockCatalystEventDTO stockCatalystEventDTO )
        throws StockNotFoundException,
               StockQuoteUnavailableException
    {
        final String methodName = "addStockCatalystEvent";
        logMethodBegin( methodName, stockCatalystEventDTO );
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
    @RequestMapping( value = "/stockCatalystEvent",
                     method = RequestMethod.PUT )
    public ResponseEntity<StockCatalystEventDTO> saveStockCatalystEvent( @RequestBody StockCatalystEventDTO portfolioStockDTO )
        throws StockNotFoundException,
               StockQuoteUnavailableException
    {
        final String methodName = "saveStockCatalystEvent";
        logMethodBegin( methodName, portfolioStockDTO );
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
    public void setStockCatalystEventService( final StockCatalystEventService stockCatalystEventService )
    {
        this.stockCatalystEventService = stockCatalystEventService;
    }
}
