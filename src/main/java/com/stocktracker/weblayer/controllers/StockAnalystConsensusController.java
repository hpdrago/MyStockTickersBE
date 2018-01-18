package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockQuoteUnavailableException;
import com.stocktracker.servicelayer.service.StockAnalystConsensusEntityService;
import com.stocktracker.weblayer.dto.StockAnalystConsensusDTO;
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
 * This is the REST Controller for all Stock Analytics methods.
 */
@RestController
@CrossOrigin
public class StockAnalystConsensusController implements MyLogger
{
    private static final String CONTEXT_URL = "stockAnalystConsensus";
    private StockAnalystConsensusEntityService stockAnalystConsensusService;

    /**
     * Get all of the stock analyst consensus for a customer
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/customer/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public Page<StockAnalystConsensusDTO> getStockAnalystConsensusListForCustomerId( final Pageable pageRequest,
                                                                                     @PathVariable Integer customerId )
    {
        final String methodName = "getStockAnalystConsensusForCustomerId";
        logMethodBegin( methodName, customerId );
        Page<StockAnalystConsensusDTO> stockAnalystConsensusDTOs = this.stockAnalystConsensusService
                                                                       .getStockAnalystConsensusListForCustomerId( pageRequest, customerId );
        logMethodEnd( methodName, "stockAnalystConsensus size: " + stockAnalystConsensusDTOs.getContent().size() );
        return stockAnalystConsensusDTOs;
    }

    /**
     * Get all of the stock analyst consensus for a customer id and ticker symbol
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/tickerSymbol/{tickerSymbol}/customer/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public Page<StockAnalystConsensusDTO> getStockAnalystConsensusListForCustomerId( final Pageable pageRequest,
                                                                                     @PathVariable int customerId,
                                                                                     @PathVariable String tickerSymbol )
    {
        final String methodName = "getStockAnalystConsensusForCustomerIdAndTickerSymbol";
        logMethodBegin( methodName, customerId, tickerSymbol );
        Page<StockAnalystConsensusDTO> stockAnalystConsensusDTOs = this.stockAnalystConsensusService
            .getStockAnalystConsensusListForCustomerIdAndTickerSymbol( pageRequest, customerId, tickerSymbol );
        logMethodEnd( methodName, "stockAnalystConsensus size: " + stockAnalystConsensusDTOs.getContent().size() );
        return stockAnalystConsensusDTOs;
    }

    /**
     * Get a single stock analyst consensus
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/id/{stockAnalystConsensusId}/customer/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public StockAnalystConsensusDTO getStockAnalystConsensus( @PathVariable int stockAnalystConsensusId,
                                                              @PathVariable int customerId )
    {
        final String methodName = "getStockAnalystConsensus";
        logMethodBegin( methodName, stockAnalystConsensusId, customerId );
        StockAnalystConsensusDTO stockAnalystConsensusDTO = this.stockAnalystConsensusService
                                                                .getStockAnalystConsensus( stockAnalystConsensusId );
        logMethodEnd( methodName, stockAnalystConsensusDTO );
        return stockAnalystConsensusDTO;
    }

    /**
     * Deletes a stock summary entity
     * @param stockAnalystConsensusId
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/id/{stockAnalystConsensusId}/customer/{customerId}",
                     method = RequestMethod.DELETE,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<Void> deleteStockAnalystConsensus( @PathVariable int stockAnalystConsensusId,
                                                             @PathVariable int customerId )
    {
        final String methodName = "deleteStockAnalystConsensus";
        logMethodBegin( methodName, customerId, stockAnalystConsensusId );
        this.stockAnalystConsensusService.deleteStockAnalystConsensus( stockAnalystConsensusId );
        logMethodEnd( methodName );
        return new ResponseEntity<>( HttpStatus.OK );
    }

    /**
     * Create a stock summary entity.
     * @param stockAnalystConsensusDTO
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/customer/{customerId}",
                     method = RequestMethod.POST )
    public ResponseEntity<StockAnalystConsensusDTO> addStockAnalystConsensus( @RequestBody StockAnalystConsensusDTO stockAnalystConsensusDTO,
                                                                              @PathVariable Integer customerId )
        throws StockNotFoundException,
               StockQuoteUnavailableException
    {
        final String methodName = "addStockAnalystConsensus";
        logMethodBegin( methodName, customerId, stockAnalystConsensusDTO );
        StockAnalystConsensusDTO newStockAnalystConsensusDTO = this.stockAnalystConsensusService
                                                                   .createStockAnalystConsensus( customerId, stockAnalystConsensusDTO );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( newStockAnalystConsensusDTO ).toUri());
        logMethodEnd( methodName );
        return new ResponseEntity<>( newStockAnalystConsensusDTO, httpHeaders, HttpStatus.CREATED );
    }

    /**
     *
     * @param stockAnalystConsensusDTO
     * @param stockAnalystConsensusId
     * @param customerId
     * @return
     * @throws StockNotFoundException
     * @throws StockQuoteUnavailableException
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/id/{stockAnalystConsensusId}/customer/{customerId}",
                     method = RequestMethod.PUT )
    public ResponseEntity<StockAnalystConsensusDTO> updateStockAnalystConsensus( @RequestBody StockAnalystConsensusDTO stockAnalystConsensusDTO,
                                                                                 @PathVariable Integer stockAnalystConsensusId,
                                                                                 @PathVariable Integer customerId )
        throws StockNotFoundException,
               StockQuoteUnavailableException
    {
        final String methodName = "saveStockAnalystConsensus";
        logMethodBegin( methodName, stockAnalystConsensusId, customerId, stockAnalystConsensusDTO );
        /*
         * Save the stock
         */
        StockAnalystConsensusDTO returnStockAnalystConsensusDTO = this.stockAnalystConsensusService
                                                                      .saveStockAnalystConsensus( stockAnalystConsensusDTO );
        /*
         * send the response
         */
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( returnStockAnalystConsensusDTO ).toUri());
        logMethodEnd( methodName, returnStockAnalystConsensusDTO );
        return new ResponseEntity<>( stockAnalystConsensusDTO, httpHeaders, HttpStatus.CREATED );
    }

    @Autowired
    public void setStockAnalystConsensusService( final StockAnalystConsensusEntityService stockAnalystConsensusService )
    {
        this.stockAnalystConsensusService = stockAnalystConsensusService;
    }
}
