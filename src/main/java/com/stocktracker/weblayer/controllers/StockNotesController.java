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
     * Add a stock to the database
     *
     * @return The stock that was added
     */
    @CrossOrigin
    @RequestMapping( value = "/stockNotes",
                     method = RequestMethod.POST )
    public ResponseEntity<StockNoteDTO> addStockNote( @RequestBody StockNoteDTO stockNoteDTO )
    {
        final String methodName = "addStockNote";
        logMethodBegin( methodName, stockNoteDTO );
        if ( stockNoteDTO.getStocks().isEmpty() )
        {
            throw new IllegalArgumentException( "No stocks were specified" );
        }
        validateStockNoteDTOPostArgument( stockNoteDTO );
        StockNoteDTO returnStockDTO = this.stockNoteService.createStockNote( stockNoteDTO );
        logDebug( methodName, "returnStockDTO: ", returnStockDTO );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( stockNoteDTO ).toUri());
        logMethodEnd( methodName, returnStockDTO );
        return new ResponseEntity<>( returnStockDTO, httpHeaders, HttpStatus.CREATED );
    }

    private void validateStockNoteDTOPostArgument( final StockNoteDTO stockNoteDTO )
    {
        Objects.requireNonNull( stockNoteDTO.getCustomerId(), "customer id cannot be null" );
        Assert.isTrue( stockNoteDTO.getCustomerId() > 0, "customer id must be > 0" );
        Objects.requireNonNull( stockNoteDTO.getNotes(),"notes cannote be null" );
        Assert.isTrue( stockNoteDTO.getNotes().length() > 0, "customer id must be > 0" );
        Objects.requireNonNull( stockNoteDTO.getStocks(), "stock notes cannot be null" );
        Assert.isTrue( stockNoteDTO.getStocks().size() > 0, "stocks cannot be empty" );
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
    public List<StockNoteDTO> getStockNotes( final @PathVariable int customerId )
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
    public List<StockNoteStockDTO> getStockNotes( final @PathVariable int customerId,
                                                  final @PathVariable String tickerSymbol )
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
    public List<StockNoteCountDTO> getStockNotesTickerSymbolCounts( final @PathVariable int customerId )
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
