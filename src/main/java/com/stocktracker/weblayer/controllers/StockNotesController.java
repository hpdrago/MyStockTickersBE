package com.stocktracker.weblayer.controllers;

import com.fasterxml.uuid.impl.UUIDUtil;
import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.CustomerNotFoundException;
import com.stocktracker.common.exceptions.DuplicateEntityException;
import com.stocktracker.common.exceptions.EntityNotFoundException;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.NotAuthorizedException;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockNoteNotFoundException;
import com.stocktracker.servicelayer.service.StockNoteCountEntityService;
import com.stocktracker.servicelayer.service.StockNoteEntityService;
import com.stocktracker.weblayer.dto.StockNoteCountDTO;
import com.stocktracker.weblayer.dto.StockNoteDTO;
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
import java.util.Objects;

/**
 * Created by mike on 5/7/2017.
 */
@RestController
@CrossOrigin
public class StockNotesController extends AbstractController implements MyLogger
{
    private StockNoteCountEntityService stockNoteCountService;
    private StockNoteEntityService stockNoteService;
    private static final String URL_CONTEXT = "/stockNotes";

    /**
     * Add a stock note to the database
     * @param stockNotesDTO
     * @param customerId
     * @return The stock note that was added
     * @throws StockNotFoundException
     * @throws DuplicateEntityException
     * @throws EntityVersionMismatchException
     * @throws CustomerNotFoundException
     * @throws NotAuthorizedException
     */
    @CrossOrigin
    @RequestMapping( value = URL_CONTEXT + "/customerId/{customerId}",
                     method = RequestMethod.POST )
    public ResponseEntity<StockNoteDTO> addStockNote( @RequestBody final StockNoteDTO stockNotesDTO,
                                                      @PathVariable( "customerId") final String customerId )
        throws StockNotFoundException,
               EntityVersionMismatchException,
               CustomerNotFoundException,
               NotAuthorizedException,
               DuplicateEntityException
    {
        final String methodName = "addStockNote";
        logMethodBegin( methodName, customerId, stockNotesDTO );
        this.validateCustomerId( customerId );
        validateStockNoteDTOPostArgument( stockNotesDTO );
        StockNoteDTO returnStockDTO = this.stockNoteService.addDTO( stockNotesDTO );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( stockNotesDTO ).toUri());
        logMethodEnd( methodName, returnStockDTO );
        return new ResponseEntity<>( returnStockDTO, httpHeaders, HttpStatus.CREATED );
    }

    /**
     * Update a stock note.
     * @return The updated stock note.
     * @throws EntityVersionMismatchException
     */
    @CrossOrigin
    @RequestMapping( value = URL_CONTEXT + "/id/{stockNotesId}/customerId/{customerId}",
                     method = RequestMethod.PUT )
    public ResponseEntity<StockNoteDTO> updateStockNote( @RequestBody final StockNoteDTO stockNotesDTO,
                                                         @PathVariable( "customerId" ) final String customerId,
                                                         @PathVariable( "stockNotesId" ) final String stockNotesId )
        throws EntityVersionMismatchException,
               CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "updateStockNote";
        logMethodBegin( methodName, customerId, stockNotesId, stockNotesDTO );
        this.validateCustomerId( customerId );
        validateStockNoteDTOPostArgument( stockNotesDTO );
        StockNoteDTO returnStockDTO = null;
        returnStockDTO = this.stockNoteService
                             .saveDTO( stockNotesDTO );
        logDebug( methodName, "returnStockDTO: ", returnStockDTO );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder.fromCurrentRequest()
                                                            .path( "" )
                                                            .buildAndExpand( stockNotesDTO )
                                                            .toUri());
        logMethodEnd( methodName, returnStockDTO );
        return new ResponseEntity<>( returnStockDTO, httpHeaders, HttpStatus.CREATED );
    }

    /**
     * Delete a stock note
     *
     * @return The stock that was added
     */
    @CrossOrigin
    @RequestMapping( value = URL_CONTEXT + "/id/{stockNotesId}/customerId/{customerId}",
                     method = RequestMethod.DELETE )
    public ResponseEntity<Void> deleteStockNote( @PathVariable( "customerId" ) final String customerId,
                                                 @PathVariable( "stockNotesId" ) final String stockNotesId )
        throws EntityNotFoundException,
               CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "deleteStockNote";
        logMethodBegin( methodName, customerId, stockNotesId );
        this.validateCustomerId( customerId );
        this.stockNoteService.deleteEntity( stockNotesId );
        logMethodEnd( methodName );
        return new ResponseEntity<>( HttpStatus.OK );
    }

    /**
     * Get a stock note
     *
     * @return The stock that was added
     */
    @CrossOrigin
    @RequestMapping( value = URL_CONTEXT + "/id/{stockNotesId}/customerId/{customerId}",
                     method = RequestMethod.GET )
    public ResponseEntity<StockNoteDTO> getStockNote( @PathVariable( "customerId" ) final String customerId,
                                                      @PathVariable( "stockNotesId" ) final String stockNotesId )
        throws CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "getStockNote";
        logMethodBegin( methodName, customerId, stockNotesId );
        this.validateCustomerId( customerId );
        StockNoteDTO stockNotesDTO = null;
        try
        {
            stockNotesDTO = this.stockNoteService
                                .getDTO( stockNotesId );
        }
        catch( EntityNotFoundException e )
        {
            throw new StockNoteNotFoundException( stockNotesId );
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder.fromCurrentRequest()
                                                            .path( "" )
                                                            .buildAndExpand( stockNotesDTO )
                                                            .toUri() );
        logMethodEnd( methodName, stockNotesDTO );
        return new ResponseEntity<>( stockNotesDTO, httpHeaders, HttpStatus.OK );
    }

    private void validateStockNoteDTOPostArgument( final StockNoteDTO stockNotesDTO )
    {
        Objects.requireNonNull( stockNotesDTO.getCustomerId(), "customer id cannot be null" );
        Objects.requireNonNull( stockNotesDTO.getNotes(),"notes cannot be null" );
    }

    /**
     * Get all of the stocks notes for a customer order by
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = URL_CONTEXT + "/page/customerId/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public Page<StockNoteDTO> getStockNotes( final Pageable pageRequest,
                                             @PathVariable final String customerId )
        throws CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "getStockNotes";
        logMethodBegin( methodName, pageRequest, customerId );
        this.validateCustomerId( customerId );
        Page<StockNoteDTO> stockNoteDTOs = this.stockNoteService
                                               .getStockNotesForCustomerUuid( pageRequest,
                                                                              UUIDUtil.uuid( customerId ));
        logDebug( methodName, "stockNoteDTOs: {0}", stockNoteDTOs );
        logMethodEnd( methodName, stockNoteDTOs.getTotalElements() );
        return stockNoteDTOs;
    }

    /**
     * Get all of the stocks notes for a customer and ticker symbol
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = URL_CONTEXT + "/page/tickerSymbol/{tickerSymbol}/customerId/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public Page<StockNoteDTO> getStockNotes( final Pageable pageRequest,
                                             @PathVariable final String customerId,
                                             @PathVariable final String tickerSymbol )
        throws CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "getStocks";
        logMethodBegin( methodName, pageRequest, customerId, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        this.validateCustomerId( customerId );
        Page<StockNoteDTO> stockNoteDTOs = this.stockNoteService
                                               .getStockNotesForCustomerUuidAndTickerSymbol( pageRequest,
                                                                                           UUIDUtil.uuid( customerId ),
                                                                                           tickerSymbol );
        //logDebug( methodName, "stockNoteDTOs: {0}", stockNoteDTOs );
        logMethodEnd( methodName, stockNoteDTOs.getTotalElements() );
        return stockNoteDTOs;
    }

    /**
     * Get all of the ticker symbols and number of notes for each ticker
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = URL_CONTEXT + "/customerId/{customerId}/tickerSymbols",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<StockNoteCountDTO> getStockNotesTickerSymbolCounts( @PathVariable final String customerId )
        throws CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "getStockNotesTickerSymbols";
        logMethodBegin( methodName, customerId );
        this.validateCustomerId( customerId );
        List<StockNoteCountDTO> stockNoteCountDTOs = stockNoteCountService.getStockNotesCount( UUIDUtil.uuid( customerId ) );
        logMethodEnd( methodName, stockNoteCountDTOs.size() );
        return stockNoteCountDTOs;
    }

    @Autowired
    public void setStockNoteService( final StockNoteEntityService stockNoteService )
    {
        this.stockNoteService = stockNoteService;
    }

    @Autowired
    public void setStockNoteCountService( final StockNoteCountEntityService stockNoteCountService )
    {
        this.stockNoteCountService = stockNoteCountService;
    }
}
