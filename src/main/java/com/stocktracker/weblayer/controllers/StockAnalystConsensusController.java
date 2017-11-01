package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import com.stocktracker.servicelayer.service.StockAnalystConsensusService;
import com.stocktracker.weblayer.dto.StockAnalystConsensusDTO;
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
public class StockAnalystConsensusController implements MyLogger
{
    private StockAnalystConsensusService stockAnalystConsensusService;

    /**
     * Get all of the stock summaries for a customer
     * @return
     */
    @RequestMapping( value = "/stockAnalystConsensus/customer/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public List<StockAnalystConsensusDTO> getStockSummaries( @PathVariable int customerId )
    {
        final String methodName = "getStockSummaries";
        logMethodBegin( methodName );
        List<StockAnalystConsensusDTO> stockAnalystConsensusDTOs = this.stockAnalystConsensusService.getStockAnalystConsensusList( customerId );
        logMethodEnd( methodName, "stockSummaries size: " + stockAnalystConsensusDTOs.size() );
        return stockAnalystConsensusDTOs;
    }

    /**
     * Get a single stock summary
     * @return
     */
    @RequestMapping( value = "/stockAnalystConsensus/{stockAnalystConsensusId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public StockAnalystConsensusDTO getStockAnalystConsensus( @PathVariable int stockAnalystConsensusId )
    {
        final String methodName = "getStockAnalystConsensus";
        logMethodBegin( methodName );
        StockAnalystConsensusDTO stockAnalystConsensusDTO = this.stockAnalystConsensusService.getStockAnalystConsensus( stockAnalystConsensusId );
        logMethodEnd( methodName, stockAnalystConsensusDTO );
        return stockAnalystConsensusDTO;
    }

    /**
     * Deletes a stock summary entity
     * @param stockAnalystConsensusId
     * @return
     */
    @RequestMapping( value = "/stockAnalystConsensus/{stockAnalystConsensusId}",
                     method = RequestMethod.DELETE,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<Void> deleteStockAnalystConsensus( @PathVariable int stockAnalystConsensusId )
    {
        final String methodName = "deleteStockAnalystConsensus";
        logMethodBegin( methodName, stockAnalystConsensusId );
        this.stockAnalystConsensusService.deleteStockAnalystConsensus( stockAnalystConsensusId );
        logMethodEnd( methodName );
        return new ResponseEntity<>( HttpStatus.OK );
    }

    /**
     * Create a stock summary entity.
     * @param stockAnalystConsensusDTO
     * @return
     */
    @RequestMapping( value = "/stockAnalystConsensus",
                     method = RequestMethod.POST )
    public ResponseEntity<StockAnalystConsensusDTO> addStockAnalystConsensus( @RequestBody StockAnalystConsensusDTO stockAnalystConsensusDTO )
    {
        final String methodName = "addStockAnalystConsensus";
        logMethodBegin( methodName, stockAnalystConsensusDTO );
        StockAnalystConsensusDTO newStockAnalystConsensusDTO = this.stockAnalystConsensusService.saveStockAnalystConsensus( stockAnalystConsensusDTO );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( newStockAnalystConsensusDTO ).toUri());
        logMethodEnd( methodName );
        return new ResponseEntity<>( newStockAnalystConsensusDTO, httpHeaders, HttpStatus.CREATED );
    }

    @CrossOrigin
    @RequestMapping( value = "/stockAnalystConsensus",
                     method = RequestMethod.PUT )
    public ResponseEntity<StockAnalystConsensusDTO> saveStockAnalystConsensus( @RequestBody StockAnalystConsensusDTO portfolioStockDTO )
    {
        final String methodName = "saveStockAnalystConsensus";
        logMethodBegin( methodName, portfolioStockDTO );
        /*
         * Save the stock
         */
        StockAnalystConsensusDTO returnStockAnalystConsensusDTO = this.stockAnalystConsensusService
            .saveStockAnalystConsensus( portfolioStockDTO );
        /*
         * send the response
         */
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( returnStockAnalystConsensusDTO ).toUri());
        logMethodEnd( methodName, returnStockAnalystConsensusDTO );
        return new ResponseEntity<>( portfolioStockDTO, httpHeaders, HttpStatus.CREATED );
    }

    @Autowired
    public void setStockAnalystConsensusService( final StockAnalystConsensusService stockAnalystConsensusService )
    {
        this.stockAnalystConsensusService = stockAnalystConsensusService;
    }
}