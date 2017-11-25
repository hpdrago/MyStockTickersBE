package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockQuoteUnavailableException;
import com.stocktracker.servicelayer.service.StockToBuyService;
import com.stocktracker.weblayer.dto.StockToBuyDTO;
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
 * This is the REST Controller for all Stock ToBuy methods.
 */
@RestController
@CrossOrigin
public class StockToBuyController implements MyLogger
{
    private StockToBuyService stockToBuyService;

    /**
     * Get all of the stock summaries for a customer
     * @return
     */
    @RequestMapping( value = "/stockToBuy/customer/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public List<StockToBuyDTO> getStockStockToBuy( @PathVariable int customerId )
        throws StockNotFoundException,
               StockQuoteUnavailableException
    {
        final String methodName = "getStockStockToBuy";
        logMethodBegin( methodName );
        List<StockToBuyDTO> stockToBuyDTOs = this.stockToBuyService.getStockToBuyList( customerId );
        logMethodEnd( methodName, "stockToBuyDTOs size: " + stockToBuyDTOs.size() );
        return stockToBuyDTOs;
    }

    /**
     * Get a single stock summary
     * @return
     */
    @RequestMapping( value = "/stockToBuy/{stockToBuyId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public StockToBuyDTO getStockToBuy( @PathVariable int stockToBuyId )
        throws StockNotFoundException,
               StockQuoteUnavailableException
    {
        final String methodName = "getStockToBuy";
        logMethodBegin( methodName );
        StockToBuyDTO stockToBuyDTO = this.stockToBuyService.getStockToBuy( stockToBuyId );
        logMethodEnd( methodName, stockToBuyDTO );
        return stockToBuyDTO;
    }

    /**
     * Deletes a stock summary entity
     * @param stockToBuyId
     * @return
     */
    @RequestMapping( value = "/stockToBuy/{stockToBuyId}",
                     method = RequestMethod.DELETE,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<Void> deleteStockToBuy( @PathVariable int stockToBuyId )
    {
        final String methodName = "deleteStockToBuy";
        logMethodBegin( methodName, stockToBuyId );
        this.stockToBuyService.deleteStockToBuy( stockToBuyId );
        logMethodEnd( methodName );
        return new ResponseEntity<>( HttpStatus.OK );
    }

    /**
     * Create a stock summary entity.
     * @param stockToBuyDTO
     * @return
     */
    @RequestMapping( value = "/stockToBuy",
                     method = RequestMethod.POST )
    public ResponseEntity<StockToBuyDTO> addStockToBuy( @RequestBody StockToBuyDTO stockToBuyDTO )
        throws StockNotFoundException,
               StockQuoteUnavailableException
    {
        final String methodName = "addStockToBuy";
        logMethodBegin( methodName, stockToBuyDTO );
        StockToBuyDTO newStockToBuyDTO = this.stockToBuyService.saveStockToBuy( stockToBuyDTO );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( newStockToBuyDTO ).toUri());
        logMethodEnd( methodName );
        return new ResponseEntity<>( newStockToBuyDTO, httpHeaders, HttpStatus.CREATED );
    }

    @CrossOrigin
    @RequestMapping( value = "/stockToBuy",
                     method = RequestMethod.PUT )
    public ResponseEntity<StockToBuyDTO> saveStockToBuy( @RequestBody StockToBuyDTO portfolioStockDTO )
        throws StockNotFoundException,
               StockQuoteUnavailableException
    {
        final String methodName = "saveStockToBuy";
        logMethodBegin( methodName, portfolioStockDTO );
        /*
         * Save the stock
         */
        StockToBuyDTO returnStockToBuyDTO = this.stockToBuyService.saveStockToBuy( portfolioStockDTO );
        /*
         * send the response
         */
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( returnStockToBuyDTO ).toUri());
        logMethodEnd( methodName, returnStockToBuyDTO );
        return new ResponseEntity<>( portfolioStockDTO, httpHeaders, HttpStatus.CREATED );
    }

    @Autowired
    public void setStockToBuyService( final StockToBuyService stockToBuyService )
    {
        this.stockToBuyService = stockToBuyService;
    }
}
