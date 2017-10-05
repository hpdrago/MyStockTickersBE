package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.entity.StockNoteEntity;
import com.stocktracker.repositorylayer.entity.StockNoteStockEntity;
import com.stocktracker.repositorylayer.entity.VStockNoteCountEntity;
import com.stocktracker.weblayer.dto.StockNoteCountDTO;
import com.stocktracker.weblayer.dto.StockNoteDTO;
import com.stocktracker.weblayer.dto.StockNoteStockDTO;
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

import java.text.ParseException;
import java.util.List;
import java.util.Objects;

/**
 * Created by mike on 5/7/2017.
 */
@RestController
@CrossOrigin
public class StockNotesController extends AbstractController implements MyLogger
{
    /**
     * Add a stock note to the database
     *
     * @return The stock note that was added
     */
    @CrossOrigin
    @RequestMapping( value = "/stockNotes",
                     method = RequestMethod.POST )
    public ResponseEntity<StockNoteDTO> addStockNote( @RequestBody final StockNoteDTO stockNotesDTO )
    {
        final String methodName = "addStockNote";
        logMethodBegin( methodName, stockNotesDTO );
        if ( stockNotesDTO.getStocks().isEmpty() )
        {
            throw new IllegalArgumentException( "No stocks were specified" );
        }
        validateStockNoteDTOPostArgument( stockNotesDTO );
        StockNoteDTO returnStockDTO = this.stockNoteService.createStockNote( stockNotesDTO );
        logDebug( methodName, "returnStockDTO: ", returnStockDTO );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( stockNotesDTO ).toUri());
        logMethodEnd( methodName, returnStockDTO );
        return new ResponseEntity<>( returnStockDTO, httpHeaders, HttpStatus.CREATED );
    }

    /**
     * Add a stock to the database
     *
     * @return The stock that was added
     */
    @CrossOrigin
    @RequestMapping( value = "/stockNotes/{stockNotesId}",
                     method = RequestMethod.PUT )
    public ResponseEntity<StockNoteDTO> updateStockNote( @RequestBody final StockNoteDTO stockNotesDTO,
                                                         @PathVariable( "stockNotesId" ) final int stockNotesId )
    {
        final String methodName = "updateStockNote";
        logMethodBegin( methodName, stockNotesId, stockNotesDTO );
        if ( stockNotesDTO.getStocks().isEmpty() )
        {
            throw new IllegalArgumentException( "No stocks were specified" );
        }
        validateStockNoteDTOPostArgument( stockNotesDTO );
        StockNoteDTO returnStockDTO = null;
        try
        {
            returnStockDTO = this.stockNoteService.updateStockNote( stockNotesDTO );
        }
        catch ( ParseException e )
        {
            throw new IllegalArgumentException( e );
        }
        /*try
        {
            Thread.sleep( 5000 );
        }
        catch ( InterruptedException e )
        {
            e.printStackTrace();
        }*/
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
    @RequestMapping( value = "/stockNotes/{stockNotesId}",
                     method = RequestMethod.DELETE )
    public ResponseEntity<StockNoteDTO> deleteStockNote( @PathVariable( "stockNotesId" ) final int stockNotesId )
    {
        final String methodName = "updateStockNote";
        logMethodBegin( methodName, stockNotesId );
        StockNoteDTO stockNotesDTO = this.stockNoteService.delete( stockNotesId );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder.fromCurrentRequest()
                                                            .path( "" )
                                                            .buildAndExpand( stockNotesDTO )
                                                            .toUri() );
        logMethodEnd( methodName, stockNotesDTO );
        return new ResponseEntity<>( stockNotesDTO, httpHeaders, HttpStatus.CREATED );
    }

    private void validateStockNoteDTOPostArgument( final StockNoteDTO stockNotesDTO )
    {
        Objects.requireNonNull( stockNotesDTO.getCustomerId(), "customer id cannot be null" );
        Assert.isTrue( stockNotesDTO.getCustomerId() > 0, "customer id must be > 0" );
        Objects.requireNonNull( stockNotesDTO.getNotes(),"notes cannot be null" );
        Assert.isTrue( stockNotesDTO.getNotes().length() > 0, "customer id must be > 0" );
        Objects.requireNonNull( stockNotesDTO.getStocks(), "stock notes cannot be null" );
        Assert.isTrue( stockNotesDTO.getStocks().size() > 0, "stocks cannot be empty" );
    }

    /**
     * Get all of the stocks notes for a customer order by
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = "/stockNotes/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<StockNoteDTO> getStockNotes( @PathVariable final int customerId )
    {
        final String methodName = "getStockNotes";
        logMethodBegin( methodName, customerId );
        Assert.isTrue( customerId > 0, "customerId must be > 0" );
        List<StockNoteEntity> stockNoteEntities = stockNoteService.getStockNotes( customerId );
        //logDebug( methodName, "stockNoteEntities: {0}", stockNoteEntities );
        List<StockNoteDTO> stockNoteDTOs =
            this.listCopyStockNoteEntityToStockNoteDTO.copy( stockNoteEntities );
        //logDebug( methodName, "stockNoteDTOs: {0}", stockNoteDTOs );
        logMethodEnd( methodName, stockNoteDTOs.size() );
        return stockNoteDTOs;
    }

    /**
     * Get all of the stocks notes for a customer and ticker symbol
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = "/stockNotes/{customerId}/{tickerSymbol}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<StockNoteStockDTO> getStockNotes( @PathVariable final int customerId,
                                                  @PathVariable final String tickerSymbol )
    {
        final String methodName = "getStocks";
        logMethodBegin( methodName, customerId, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Assert.isTrue( customerId > 0, "customerId must be > 0" );
        List<StockNoteStockEntity> stockNoteStockEntities = stockNoteService.getStockNoteStocks( customerId, tickerSymbol );
        List<StockNoteStockDTO> stockNoteStockDTOs =
            this.listCopyStockNoteStockEntityToStockNoteStockDTO.copy( stockNoteStockEntities );
        logMethodEnd( methodName, stockNoteStockDTOs.size() );
        return stockNoteStockDTOs;
    }

    /**
     * Get all of the ticker symbols and number of notes for each ticker
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = "/stockNotes/tickerSymbols/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<StockNoteCountDTO> getStockNotesTickerSymbolCounts( @PathVariable final int customerId )
    {
        final String methodName = "getStockNotesTickerSymbols";
        logMethodBegin( methodName, customerId );
        Assert.isTrue( customerId > 0, "customerId must be > 0" );
        List<VStockNoteCountEntity> stockNoteCountDES = stockNoteService.getStockNotesCount( customerId );
        List<StockNoteCountDTO> stockNoteCountDTOS = listCopyStockNoteCountEntityToStockNoteCountDTO
            .copy( stockNoteCountDES );
        logMethodEnd( methodName, stockNoteCountDTOS.size() );
        return stockNoteCountDTOS;
    }

}
