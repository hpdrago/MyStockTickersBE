package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import com.stocktracker.servicelayer.service.StockAnalyticsService;
import com.stocktracker.weblayer.dto.StockAnalyticsDTO;
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
 * This is the REST Controller for all Stock Analytics methods.
 */
@RestController
@CrossOrigin
public class StockAnalyticsController implements MyLogger
{
    private StockAnalyticsService stockAnalyticsService;

    /**
     * Get all of the stock summaries for a customer
     * @return
     */
    @RequestMapping( value = "/stockAnalytics/customer/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public List<StockAnalyticsDTO> getStockSummaries( @PathVariable int customerId )
    {
        final String methodName = "getStockSummaries";
        logMethodBegin( methodName );
        List<StockAnalyticsDTO> stockAnalyticsDTOs = this.stockAnalyticsService.getStockAnalyticsList( customerId );
        logMethodEnd( methodName, "stockSummaries size: " + stockAnalyticsDTOs.size() );
        return stockAnalyticsDTOs;
    }

    /**
     * Get a single stock summary
     * @return
     */
    @RequestMapping( value = "/stockAnalytics/{stockAnalyticsId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public StockAnalyticsDTO getStockAnalytics( @PathVariable int stockAnalyticsId )
    {
        final String methodName = "getStockAnalytics";
        logMethodBegin( methodName );
        StockAnalyticsDTO stockAnalyticsDTO = this.stockAnalyticsService.getStockAnalytics( stockAnalyticsId );
        logMethodEnd( methodName, stockAnalyticsDTO );
        return stockAnalyticsDTO;
    }

    /**
     * Deletes a stock summary entity
     * @param stockAnalyticsId
     * @return
     */
    @RequestMapping( value = "/stockAnalytics/{stockAnalyticsId}",
                     method = RequestMethod.DELETE,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<Void> deleteStockAnalytics( @PathVariable int stockAnalyticsId )
    {
        final String methodName = "deleteStockAnalytics";
        logMethodBegin( methodName, stockAnalyticsId );
        this.stockAnalyticsService.deleteStockAnalytics( stockAnalyticsId );
        logMethodEnd( methodName );
        return new ResponseEntity<>( HttpStatus.OK );
    }

    /**
     * Create a stock summary entity.
     * @param stockAnalyticsDTO
     * @return
     */
    @RequestMapping( value = "/stockAnalytics",
                     method = RequestMethod.POST )
    public ResponseEntity<StockAnalyticsDTO> addStockAnalytics( @RequestBody StockAnalyticsDTO stockAnalyticsDTO )
    {
        final String methodName = "addStockAnalytics";
        logMethodBegin( methodName, stockAnalyticsDTO );
        StockAnalyticsDTO newStockAnalyticsDTO = this.stockAnalyticsService.saveStockAnalytics( stockAnalyticsDTO );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( newStockAnalyticsDTO ).toUri());
        logMethodEnd( methodName );
        return new ResponseEntity<>( newStockAnalyticsDTO, httpHeaders, HttpStatus.CREATED );
    }

    @CrossOrigin
    @RequestMapping( value = "/stockAnalytics",
                     method = RequestMethod.PUT )
    public ResponseEntity<StockAnalyticsDTO> saveStockAnalytics( @RequestBody StockAnalyticsDTO portfolioStockDTO )
    {
        final String methodName = "saveStockAnalytics";
        logMethodBegin( methodName, portfolioStockDTO );
        /*
         * Save the stock
         */
        StockAnalyticsDTO returnStockAnalyticsDTO = this.stockAnalyticsService.saveStockAnalytics( portfolioStockDTO );
        /*
         * send the response
         */
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( returnStockAnalyticsDTO ).toUri());
        logMethodEnd( methodName, returnStockAnalyticsDTO );
        return new ResponseEntity<>( portfolioStockDTO, httpHeaders, HttpStatus.CREATED );
    }

    @Autowired
    public void setStockAnalyticsService( final StockAnalyticsService stockAnalyticsService )
    {
        this.stockAnalyticsService = stockAnalyticsService;
    }
}
