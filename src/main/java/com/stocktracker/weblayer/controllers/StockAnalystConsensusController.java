package com.stocktracker.weblayer.controllers;

import com.fasterxml.uuid.impl.UUIDUtil;
import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.CustomerNotFoundException;
import com.stocktracker.common.exceptions.DuplicateEntityException;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.NotAuthorizedException;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
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

import java.util.List;

/**
 * This is the REST Controller for all Stock Analytics methods.
 */
@RestController
@CrossOrigin
public class StockAnalystConsensusController extends AbstractController implements MyLogger
{
    private static final String CONTEXT_URL = "stockAnalystConsensus";
    private StockAnalystConsensusEntityService stockAnalystConsensusService;

    /**
     * Get all of the stock analyst consensus for a customer
     * @return
     * @throws CustomerNotFoundException
     * @throws NotAuthorizedException
     */
    @RequestMapping( value = CONTEXT_URL + "/customerId/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public List<StockAnalystConsensusDTO> getStockAnalystConsensusList( @PathVariable String customerId )
        throws CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "getStockAnalystConsensusList";
        logMethodBegin( methodName, customerId );
        this.validateCustomerId( customerId );
        final List<StockAnalystConsensusDTO> stockAnalystConsensusDTOs = this.stockAnalystConsensusService
                                                                             .getAllStockAnalystConsensus(
                                                                                 UUIDUtil.uuid( customerId ));
        logMethodEnd( methodName, "stockAnalystConsensus size: " + stockAnalystConsensusDTOs.size() );
        return stockAnalystConsensusDTOs;
    }

    /**
     * Get a page of the stock analyst consensus for a customer
     * @return
     * @throws CustomerNotFoundException
     * @throws NotAuthorizedException
     */
    @RequestMapping( value = CONTEXT_URL + "/page/customerId/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public Page<StockAnalystConsensusDTO> getStockAnalystConsensusPage( final Pageable pageRequest,
                                                                        @PathVariable String customerId )
        throws CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "getStockAnalystConsensusPage";
        logMethodBegin( methodName, customerId );
        this.validateCustomerId( customerId );
        final Page<StockAnalystConsensusDTO> stockAnalystConsensusDTOs = this.stockAnalystConsensusService
                                                                             .getStockAnalystConsensusPage( pageRequest,
                                                                                                            UUIDUtil.uuid( customerId ));
        logMethodEnd( methodName, "stockAnalystConsensus size: " + stockAnalystConsensusDTOs.getContent().size() );
        return stockAnalystConsensusDTOs;
    }

    /**
     * Get a page of the stock analyst consensus for a customer and a ticker symbol.
     * @return
     * @throws CustomerNotFoundException
     * @throws NotAuthorizedException
     */
    @RequestMapping( value = CONTEXT_URL + "/page/tickerSymbol/{tickerSymbol}/customerId/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public Page<StockAnalystConsensusDTO> getStockAnalystConsensusPage( final Pageable pageRequest,
                                                                        @PathVariable String tickerSymbol,
                                                                        @PathVariable String customerId )
        throws CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "getStockAnalystConsensusPage";
        logMethodBegin( methodName, customerId, tickerSymbol );
        this.validateCustomerId( customerId );
        final Page<StockAnalystConsensusDTO> stockAnalystConsensusDTOs = this.stockAnalystConsensusService
                                                                             .getStockAnalystConsensusPage( pageRequest,
                                                                                                            UUIDUtil.uuid( customerId ),
                                                                                                            tickerSymbol );
        logMethodEnd( methodName, "stockAnalystConsensus size: " + stockAnalystConsensusDTOs.getContent().size() );
        return stockAnalystConsensusDTOs;
    }

    /**
     * Get all of the stock analyst consensus for a customer id and ticker symbol
     * @return
     * @throws CustomerNotFoundException
     * @throws NotAuthorizedException
     */
    @RequestMapping( value = CONTEXT_URL + "/tickerSymbol/{tickerSymbol}/customerId/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public Page<StockAnalystConsensusDTO> getStockAnalystConsensusList( final Pageable pageRequest,
                                                                        @PathVariable String customerId,
                                                                        @PathVariable String tickerSymbol )
        throws CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "getStockAnalystConsensusList";
        logMethodBegin( methodName, pageRequest, customerId, tickerSymbol );
        this.validateCustomerId( customerId );
        Page<StockAnalystConsensusDTO> stockAnalystConsensusDTOs = this.stockAnalystConsensusService
            .getStockAnalystConsensusListForCustomerUuidAndTickerSymbol( pageRequest,
                                                                         UUIDUtil.uuid( customerId ),
                                                                         tickerSymbol );
        logMethodEnd( methodName, "stockAnalystConsensus size: " + stockAnalystConsensusDTOs.getContent().size() );
        return stockAnalystConsensusDTOs;
    }

    /**
     * Get a single stock analyst consensus
     * @return
     * @throws VersionedEntityNotFoundException
     * @throws CustomerNotFoundException
     * @throws NotAuthorizedException
     */
    @RequestMapping( value = CONTEXT_URL + "/id/{stockAnalystConsensusId}/customerId/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public StockAnalystConsensusDTO getStockAnalystConsensus( @PathVariable String stockAnalystConsensusId,
                                                              @PathVariable String customerId )
        throws VersionedEntityNotFoundException,
               CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "getStockAnalystConsensus";
        logMethodBegin( methodName, stockAnalystConsensusId, customerId );
        this.validateCustomerId( customerId );
        final StockAnalystConsensusDTO stockAnalystConsensusDTO = this.stockAnalystConsensusService
                                                                      .getDTO( UUIDUtil.uuid( stockAnalystConsensusId ));
        logMethodEnd( methodName, stockAnalystConsensusDTO );
        return stockAnalystConsensusDTO;
    }

    /**
     * Deletes a stock summary entity
     * @param stockAnalystConsensusId
     * @return
     * @throws CustomerNotFoundException
     * @throws VersionedEntityNotFoundException
     * @throws NotAuthorizedException
     */
    @RequestMapping( value = CONTEXT_URL + "/id/{stockAnalystConsensusId}/customerId/{customerId}",
                     method = RequestMethod.DELETE,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<Void> deleteStockAnalystConsensus( @PathVariable String stockAnalystConsensusId,
                                                             @PathVariable String customerId )
        throws VersionedEntityNotFoundException,
               CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "deleteStockAnalystConsensus";
        logMethodBegin( methodName, customerId, stockAnalystConsensusId );
        this.validateCustomerId( customerId );
        this.stockAnalystConsensusService.deleteEntity( stockAnalystConsensusId );
        logMethodEnd( methodName );
        return new ResponseEntity<>( HttpStatus.OK );
    }

    /**
     * Create a stock summary entity.
     * @param stockAnalystConsensusDTO
     * @param customerId
     * @return
     * @throws StockNotFoundException
     * @throws EntityVersionMismatchException
     * @throws DuplicateEntityException
     * @throws CustomerNotFoundException
     * @throws NotAuthorizedException
     */
    @RequestMapping( value = CONTEXT_URL + "/customerId/{customerId}",
                     method = RequestMethod.POST )
    public ResponseEntity<StockAnalystConsensusDTO> addStockAnalystConsensus( @RequestBody StockAnalystConsensusDTO stockAnalystConsensusDTO,
                                                                              @PathVariable String customerId )
        throws StockNotFoundException,
               EntityVersionMismatchException,
               DuplicateEntityException,
               CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "addStockAnalystConsensus";
        logMethodBegin( methodName, customerId, stockAnalystConsensusDTO );
        this.validateCustomerId( customerId );
        final StockAnalystConsensusDTO newStockAnalystConsensusDTO = this.stockAnalystConsensusService
                                                                         .addDTO( stockAnalystConsensusDTO );
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
     * @throws EntityVersionMismatchException
     * @throws CustomerNotFoundException
     * @throws NotAuthorizedException
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/id/{stockAnalystConsensusId}/customerId/{customerId}",
                     method = RequestMethod.PUT )
    public ResponseEntity<StockAnalystConsensusDTO> updateStockAnalystConsensus( @RequestBody StockAnalystConsensusDTO stockAnalystConsensusDTO,
                                                                                 @PathVariable String stockAnalystConsensusId,
                                                                                 @PathVariable String customerId )
        throws EntityVersionMismatchException,
               CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "saveStockAnalystConsensus";
        logMethodBegin( methodName, stockAnalystConsensusId, customerId, stockAnalystConsensusDTO );
        this.validateCustomerId( customerId );
        /*
         * Save the stock
         */
        final StockAnalystConsensusDTO returnStockAnalystConsensusDTO = this.stockAnalystConsensusService
                                                                            .saveDTO( stockAnalystConsensusDTO );
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
