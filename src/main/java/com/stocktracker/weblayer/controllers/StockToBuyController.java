package com.stocktracker.weblayer.controllers;

import com.fasterxml.uuid.impl.UUIDUtil;
import com.stocktracker.common.exceptions.DuplicateEntityException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.StockToBuyNotFoundException;
import com.stocktracker.servicelayer.service.StockToBuyEntityService;
import com.stocktracker.weblayer.dto.StockToBuyDTO;
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
    @RequestMapping( value = CONTEXT_URL + "/page/customerId/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public Page<StockToBuyDTO> getStockStockToBuy( final Pageable pageRequest,
                                                   final @NotNull @PathVariable String customerId )
    {
        final String methodName = "getStockStockToBuy";
        logMethodBegin( methodName, pageRequest, customerId );
        Page<StockToBuyDTO> stockToBuyDTOs = this.stockToBuyService
                                                 .getStockToBuyListForCustomerUuid( pageRequest,
                                                                                    UUIDUtil.uuid( customerId ));
        logMethodEnd( methodName, "stockToBuyDTOs size: " + stockToBuyDTOs.getContent().size() );
        return stockToBuyDTOs;
    }

    /**
     * Get all of the stock to buy for a customer and a
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/tickerSymbol/{tickerSymbol}/customerId/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public StockToBuyDTO getStockStockToBuy( @NotNull final Pageable pageRequest,
                                             @NotNull @PathVariable String customerId,
                                             @NotNull @PathVariable String tickerSymbol )
    {
        final String methodName = "getStockStockToBuyForTickerSymbol";
        logMethodBegin( methodName, pageRequest, customerId, tickerSymbol );
        final StockToBuyDTO stockToBuyDTO = this.stockToBuyService
                                                .getByCustomerUuidAndTickerSymbol( UUIDUtil.uuid( customerId ),
                                                                                  tickerSymbol );
        logMethodEnd( methodName, stockToBuyDTO );
        return stockToBuyDTO;
    }

    /**
     * Get a single stock to buy
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/id/{stockToBuyId}/customerId/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public StockToBuyDTO getStockToBuy( @PathVariable String stockToBuyId,
                                        @PathVariable String customerId )
        throws StockToBuyNotFoundException
    {
        final String methodName = "getStockToBuy";
        logMethodBegin( methodName, stockToBuyId, customerId );
        StockToBuyDTO stockToBuyDTO = null;
        try
        {
            stockToBuyDTO = this.stockToBuyService
                                .getDTO( UUIDUtil.uuid( stockToBuyId ));
        }
        catch( VersionedEntityNotFoundException e )
        {
            throw new StockToBuyNotFoundException( stockToBuyId, e );
        }
        logMethodEnd( methodName, stockToBuyDTO );
        return stockToBuyDTO;
    }

    /**
     * Deletes a stock to buy entity
     * @param stockToBuyId
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/id/{stockToBuyId}/customerId/{customerId}",
                     method = RequestMethod.DELETE,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<Void> deleteStockToBuy( @PathVariable String stockToBuyId,
                                                  @PathVariable String customerId )
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
     * @throws DuplicateEntityException
     * @throws EntityVersionMismatchException
     */
    @RequestMapping( value = CONTEXT_URL + "/customerId/{customerId}",
                     method = RequestMethod.POST )
    public ResponseEntity<StockToBuyDTO> addStockToBuy( @PathVariable String customerId,
                                                        @RequestBody StockToBuyDTO stockToBuyDTO )
        throws EntityVersionMismatchException,
               DuplicateEntityException
    {
        final String methodName = "addStockToBuy";
        logMethodBegin( methodName, customerId, stockToBuyDTO );
        final StockToBuyDTO newStockToBuyDTO = this.stockToBuyService
                                                   .addDTO( stockToBuyDTO );
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
     * @throws EntityVersionMismatchException
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/id/{stockToBuyId}/customerId/{customerId}",
                     method = RequestMethod.PUT )
    public ResponseEntity<StockToBuyDTO> saveStockToBuy( @PathVariable String stockToBuyId,
                                                         @PathVariable String customerId,
                                                         @RequestBody StockToBuyDTO stockToBuyDTO )
        throws EntityVersionMismatchException
    {
        final String methodName = "saveStockToBuy";
        logMethodBegin( methodName, customerId, stockToBuyId, stockToBuyDTO );
        /*
         * Save the stock
         */
        final StockToBuyDTO returnStockToBuyDTO = this.stockToBuyService.saveDTO( stockToBuyDTO );
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
