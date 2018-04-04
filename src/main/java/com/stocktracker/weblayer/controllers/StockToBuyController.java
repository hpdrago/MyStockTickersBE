package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.StockCompanyNotFoundException;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockQuoteUnavailableException;
import com.stocktracker.common.exceptions.StockToBuyNotFoundException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.servicelayer.service.StockToBuyEntityService;
import com.stocktracker.weblayer.dto.StockToBuyQuoteDTO;
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

import javax.validation.constraints.NotNull;

/**
 * This is the REST Controller for all Stock ToBuy methods.
 */
@RestController
@CrossOrigin
public class StockToBuyController extends AbstractController
{
    private static final String CONTEXT_URL = "/stockToBuy";
    private StockToBuyEntityService stockToBuyService;

    /**
     * Get all of the stock to buy for a customer
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/page/customer/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public Page<StockToBuyQuoteDTO> getStockStockToBuy( final Pageable pageRequest,
                                                        final @NotNull @PathVariable int customerId )
    {
        final String methodName = "getStockStockToBuy";
        logMethodBegin( methodName, pageRequest, customerId );
        Page<StockToBuyQuoteDTO> stockToBuyDTOs = this.stockToBuyService
                                                 .getStockToBuyListForCustomerId( pageRequest, customerId );
        logMethodEnd( methodName, "stockToBuyDTOs size: " + stockToBuyDTOs.getContent().size() );
        return stockToBuyDTOs;
    }

    /**
     * Get all of the stock to buy for a customer and a
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/page/tickerSymbol/{tickerSymbol}/customer/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public Page<StockToBuyQuoteDTO> getStockStockToBuy( @NotNull final Pageable pageRequest,
                                                        @NotNull @PathVariable int customerId,
                                                        @NotNull @PathVariable String tickerSymbol )
    {
        final String methodName = "getStockStockToBuyForTickerSymbol";
        logMethodBegin( methodName, pageRequest, customerId, tickerSymbol );
        Page<StockToBuyQuoteDTO> stockToBuyDTOs = this.stockToBuyService
                                                 .getStockToBuyListForCustomerIdAndTickerSymbol( pageRequest, customerId, tickerSymbol );
        logMethodEnd( methodName, "stockToBuyDTOs size: " + stockToBuyDTOs.getContent().size() );
        return stockToBuyDTOs;
    }

    /**
     * Get a single stock to buy
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/id/{stockToBuyId}/customer/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public StockToBuyQuoteDTO getStockToBuy( @PathVariable int stockToBuyId,
                                             @PathVariable int customerId )
        throws StockNotFoundException,
               StockQuoteUnavailableException,
               StockToBuyNotFoundException
    {
        final String methodName = "getStockToBuy";
        logMethodBegin( methodName, stockToBuyId, customerId );
        StockToBuyQuoteDTO stockToBuyDTO = this.stockToBuyService.getStockToBuy( stockToBuyId );
        logMethodEnd( methodName, stockToBuyDTO );
        return stockToBuyDTO;
    }

    /**
     * Deletes a stock to buy entity
     * @param stockToBuyId
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/id/{stockToBuyId}/customer/{customerId}",
                     method = RequestMethod.DELETE,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<Void> deleteStockToBuy( @PathVariable int stockToBuyId,
                                                  @PathVariable int customerId )
        throws StockToBuyNotFoundException
    {
        final String methodName = "deleteStockToBuy";
        logMethodBegin( methodName, stockToBuyId, customerId );
        try
        {
            this.stockToBuyService.deleteEntity( stockToBuyId );
        }
        catch( VersionedEntityNotFoundException e )
        {
            throw new StockToBuyNotFoundException( stockToBuyId );
        }
        logMethodEnd( methodName );
        return new ResponseEntity<>( HttpStatus.OK );
    }

    /**
     * Create a stock to buy entity.
     * @param stockToBuyDTO
     * @return
     * @throws StockNotFoundException
     * @throws StockCompanyNotFoundException
     * @throws EntityVersionMismatchException
     */
    @RequestMapping( value = CONTEXT_URL + "/customer/{customerId}",
                     method = RequestMethod.POST )
    public ResponseEntity<StockToBuyQuoteDTO> addStockToBuy( @PathVariable int customerId,
                                                             @RequestBody StockToBuyQuoteDTO stockToBuyDTO )
        throws StockNotFoundException,
               StockCompanyNotFoundException,
               EntityVersionMismatchException
    {
        final String methodName = "addStockToBuy";
        logMethodBegin( methodName, customerId, stockToBuyDTO );
        StockToBuyQuoteDTO newStockToBuyDTO = this.stockToBuyService.createStockToBuy( stockToBuyDTO );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( newStockToBuyDTO ).toUri());
        logMethodEnd( methodName );
        return new ResponseEntity<>( newStockToBuyDTO, httpHeaders, HttpStatus.CREATED );
    }

    /**
     * Save the stock to buy.
     * @param stockToBuyDTO
     * @return
     * @throws StockNotFoundException
     * @throws StockQuoteUnavailableException
     * @throws EntityVersionMismatchException
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/id/{stockToBuyId}/customer/{customerId}",
                     method = RequestMethod.PUT )
    public ResponseEntity<StockToBuyQuoteDTO> saveStockToBuy( @PathVariable int stockToBuyId,
                                                              @PathVariable int customerId,
                                                              @RequestBody StockToBuyQuoteDTO stockToBuyDTO )
        throws StockNotFoundException,
               StockQuoteUnavailableException,
               EntityVersionMismatchException
    {
        final String methodName = "saveStockToBuy";
        logMethodBegin( methodName, customerId, stockToBuyId, stockToBuyDTO );
        /*
         * Save the stock
         */
        StockToBuyQuoteDTO returnStockToBuyDTO = this.stockToBuyService.saveStockToBuy( stockToBuyDTO );
        /*
         * send the response
         */
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( returnStockToBuyDTO ).toUri());
        logMethodEnd( methodName, returnStockToBuyDTO );
        return new ResponseEntity<>( stockToBuyDTO, httpHeaders, HttpStatus.CREATED );
    }

    @Autowired
    public void setStockToBuyService( final StockToBuyEntityService stockToBuyService )
    {
        this.stockToBuyService = stockToBuyService;
    }
}
