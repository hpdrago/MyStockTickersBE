package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import com.stocktracker.servicelayer.service.StockSummaryService;
import com.stocktracker.weblayer.dto.StockSummaryDTO;
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
 * This is the REST Controller for all Stock Summary methods.
 */
@RestController
@CrossOrigin
public class StockSummaryController implements MyLogger
{
    private StockSummaryService stockSummaryService;

    /**
     * Get all of the stock summaries for a customer
     * @return
     */
    @RequestMapping( value = "/stockSummaries/{customer_id}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public List<StockSummaryDTO> getStockSummaries( @PathVariable int customerId )
    {
        final String methodName = "getStockSummaries";
        logMethodBegin( methodName );
        List<StockSummaryDTO> stockSummaryDTOs = this.stockSummaryService.getStockSummaries( customerId );
        logMethodEnd( methodName, "stockSummaries size: " + stockSummaryDTOs.size() );
        return stockSummaryDTOs;
    }

    /**
     * Get a single stock summary
     * @return
     */
    @RequestMapping( value = "/stockSummaries/stockSummary/{stockSummaryId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public StockSummaryDTO getStockSummary( @PathVariable int stockSummaryId )
    {
        final String methodName = "getStockSummary";
        logMethodBegin( methodName );
        StockSummaryDTO stockSummaryDTO = this.stockSummaryService.getStockSummary( stockSummaryId );
        logMethodEnd( methodName, stockSummaryDTO );
        return stockSummaryDTO;
    }

    /**
     * Deletes a stock summary entity
     * @param stockSummaryId
     * @return
     */
    @RequestMapping( value = "/stockSummaries/stockSummary/{stockSummaryId}",
                     method = RequestMethod.DELETE,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<Void> deleteStockSummary( @PathVariable int stockSummaryId )
    {
        final String methodName = "deleteStockSummary";
        logMethodBegin( methodName, stockSummaryId );
        this.stockSummaryService.deleteStockSummary( stockSummaryId );
        logMethodEnd( methodName );
        return new ResponseEntity<>( HttpStatus.OK );
    }

    /**
     * Create a stock summary entity.
     * @param stockSummaryDTO
     * @return
     */
    @RequestMapping( value = "/stockSummary",
                     method = RequestMethod.POST )
    public ResponseEntity<StockSummaryDTO> addStockSummary( @RequestBody StockSummaryDTO stockSummaryDTO )
    {
        final String methodName = "addStockSummary";
        logMethodBegin( methodName, stockSummaryDTO );
        StockSummaryDTO newStockSummaryDTO = this.stockSummaryService.saveStockSummary( stockSummaryDTO );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( newStockSummaryDTO ).toUri());
        logMethodEnd( methodName );
        return new ResponseEntity<>( newStockSummaryDTO, httpHeaders, HttpStatus.CREATED );
    }

    @CrossOrigin
    @RequestMapping( value = "/stockSummary",
                     method = RequestMethod.PUT )
    public ResponseEntity<StockSummaryDTO> saveStockSummary( @RequestBody StockSummaryDTO portfolioStockDTO )
    {
        final String methodName = "saveStockSummary";
        logMethodBegin( methodName, portfolioStockDTO );
        /*
         * Save the stock
         */
        StockSummaryDTO returnStockSummaryDTO = this.stockSummaryService.saveStockSummary( portfolioStockDTO );
        /*
         * send the response
         */
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( returnStockSummaryDTO ).toUri());
        logMethodEnd( methodName, returnStockSummaryDTO );
        return new ResponseEntity<>( portfolioStockDTO, httpHeaders, HttpStatus.CREATED );
    }

    @Autowired
    public void setStockSummaryService( final StockSummaryService stockSummaryService )
    {
        this.stockSummaryService = stockSummaryService;
    }
}
