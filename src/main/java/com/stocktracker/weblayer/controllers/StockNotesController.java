package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.StockCompanyNotFoundException;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockNoteNotFoundException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.servicelayer.service.StockNoteCountEntityService;
import com.stocktracker.servicelayer.service.StockNoteEntityService;
import com.stocktracker.weblayer.dto.StockNoteCountDTO;
import com.stocktracker.weblayer.dto.StockNoteQuoteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
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
     * @throws StockCompanyNotFoundException
     * @throws EntityVersionMismatchException
     */
    @CrossOrigin
    @RequestMapping( value = URL_CONTEXT + "/customer/{customerId}",
                     method = RequestMethod.POST )
    public ResponseEntity<StockNoteQuoteDTO> addStockNote( @RequestBody final StockNoteQuoteDTO stockNotesDTO,
                                                           @PathVariable( "customerId") final int customerId )
        throws StockNotFoundException,
               StockCompanyNotFoundException,
               EntityVersionMismatchException
    {
        final String methodName = "addStockNote";
        logMethodBegin( methodName, customerId, stockNotesDTO );
        validateStockNoteDTOPostArgument( stockNotesDTO );
        StockNoteQuoteDTO returnStockDTO = this.stockNoteService.createStockNote( stockNotesDTO );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( stockNotesDTO ).toUri());
        logMethodEnd( methodName, returnStockDTO );
        return new ResponseEntity<>( returnStockDTO, httpHeaders, HttpStatus.CREATED );
    }

    /**
     * Add a stock to the database
     * @return The stock that was added
     * @throws EntityVersionMismatchException
     */
    @CrossOrigin
    @RequestMapping( value = URL_CONTEXT + "/id/{stockNotesId}/customerId/{customerId}",
                     method = RequestMethod.PUT )
    public ResponseEntity<StockNoteQuoteDTO> updateStockNote( @RequestBody final StockNoteQuoteDTO stockNotesDTO,
                                                              @PathVariable( "customerId" ) final int customerId,
                                                              @PathVariable( "stockNotesId" ) final int stockNotesId )
        throws EntityVersionMismatchException
    {
        final String methodName = "updateStockNote";
        logMethodBegin( methodName, customerId, stockNotesId, stockNotesDTO );
        validateStockNoteDTOPostArgument( stockNotesDTO );
        StockNoteQuoteDTO returnStockDTO = null;
        returnStockDTO = this.stockNoteService.updateStockNote( stockNotesDTO );
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
    @RequestMapping( value = URL_CONTEXT + "/id/{stockNotesId}/customer/{customerId}",
                     method = RequestMethod.DELETE )
    public ResponseEntity<Void> deleteStockNote( @PathVariable( "customerId" ) final int customerId,
                                                 @PathVariable( "stockNotesId" ) final int stockNotesId )
        throws VersionedEntityNotFoundException
    {
        final String methodName = "deleteStockNote";
        logMethodBegin( methodName, customerId, stockNotesId );
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
    @RequestMapping( value = URL_CONTEXT + "/id/{stockNotesId}/customer/{customerId}",
                     method = RequestMethod.GET )
    public ResponseEntity<StockNoteQuoteDTO> getStockNote( @PathVariable( "customerId" ) final int customerId,
                                                           @PathVariable( "stockNotesId" ) final int stockNotesId )
    {
        final String methodName = "getStockNote";
        logMethodBegin( methodName, customerId, stockNotesId );
        StockNoteQuoteDTO stockNotesDTO = null;
        try
        {
            stockNotesDTO = this.stockNoteService
                                .getDTO( stockNotesId );
        }
        catch( VersionedEntityNotFoundException e )
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

    private void validateStockNoteDTOPostArgument( final StockNoteQuoteDTO stockNotesDTO )
    {
        Objects.requireNonNull( stockNotesDTO.getCustomerId(), "customer id cannot be null" );
        Assert.isTrue( stockNotesDTO.getCustomerId() > 0, "customer id must be > 0" );
        Objects.requireNonNull( stockNotesDTO.getNotes(),"notes cannot be null" );
        Assert.isTrue( stockNotesDTO.getNotes().length() > 0, "notes length must be > 0" );
    }

    /**
     * Get all of the stocks notes for a customer order by
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = URL_CONTEXT + "/page/customer/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public Page<StockNoteQuoteDTO> getStockNotes( final Pageable pageRequest,
                                                  @PathVariable final int customerId )
    {
        final String methodName = "getStockNotes";
        logMethodBegin( methodName, pageRequest, customerId );
        Assert.isTrue( customerId > 0, "customerId must be > 0" );
        Page<StockNoteQuoteDTO> stockNoteDTOs = stockNoteService.getStockNotesForCustomerId( pageRequest, customerId );
        //logDebug( methodName, "stockNoteDTOs: {0}", stockNoteDTOs );
        logMethodEnd( methodName, stockNoteDTOs.getTotalElements() );
        return stockNoteDTOs;
    }

    /**
     * Get all of the stocks notes for a customer and ticker symbol
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = URL_CONTEXT + "/page/tickerSymbol/{tickerSymbol}/customer/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public Page<StockNoteQuoteDTO> getStockNotes( final Pageable pageRequest,
                                                  @PathVariable final int customerId,
                                                  @PathVariable final String tickerSymbol )
    {
        final String methodName = "getStocks";
        logMethodBegin( methodName, pageRequest, customerId, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Assert.isTrue( customerId > 0, "customerId must be > 0" );
        Page<StockNoteQuoteDTO> stockNoteDTOs = stockNoteService.getStockNotesForCustomerIdAndTickerSymbol( pageRequest,
                                                                                                            customerId,
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
    @RequestMapping( value = URL_CONTEXT + "/customer/{customerId}/tickerSymbols",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<StockNoteCountDTO> getStockNotesTickerSymbolCounts( @PathVariable final int customerId )
    {
        final String methodName = "getStockNotesTickerSymbols";
        logMethodBegin( methodName, customerId );
        Assert.isTrue( customerId > 0, "customerId must be > 0" );
        List<StockNoteCountDTO> stockNoteCountDTOs = stockNoteCountService.getStockNotesCount( customerId );
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
