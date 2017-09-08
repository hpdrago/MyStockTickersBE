package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.entity.StockNoteEntity;
import com.stocktracker.repositorylayer.entity.StockNoteSourceEntity;
import com.stocktracker.repositorylayer.entity.StockNoteStockEntity;
import com.stocktracker.repositorylayer.entity.VStockNoteCountEntity;
import com.stocktracker.weblayer.dto.StockNoteCountDTO;
import com.stocktracker.weblayer.dto.StockNoteDTO;
import com.stocktracker.weblayer.dto.StockNoteSourceDTO;
import com.stocktracker.weblayer.dto.StockNoteStockDTO;
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
    @RequestMapping( value = "/stockNotes/{customerId}",
                     method = RequestMethod.POST )
    public ResponseEntity<StockNoteDTO> addStockNote( @RequestBody StockNoteDTO stockNoteDTO )
    {
        final String methodName = "addStockNote";
        logMethodBegin( methodName, stockNoteDTO );
        if ( stockNoteDTO.getStockNotesStocks().isEmpty() )
        {
            throw new IllegalArgumentException( "No stocks were specified" );
        }
        StockNoteDTO returnStockDTO = this.stockNoteService.createStockNote( stockNoteDTO );
        logDebug( methodName, "returnStockDTO: ", returnStockDTO );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( stockNoteDTO ).toUri());
        logMethodEnd( methodName, returnStockDTO );
        return new ResponseEntity<>( returnStockDTO, httpHeaders, HttpStatus.CREATED );
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
        logMethodBegin( methodName );
        List<StockNoteEntity> stockNoteEntities = stockNoteService.getStockNotes( customerId );
        List<StockNoteDTO> stockNoteDTOs =
            this.listCopyStockNoteEntityToStockNoteDTO.copy( stockNoteEntities );
        logDebug( methodName, "stockNoteDTOs: {0}", stockNoteDTOs );
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
        final String methodName = "getStockNotesStocks";
        logMethodBegin( methodName );
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
        logMethodBegin( methodName );
        List<VStockNoteCountEntity> stockNoteCountDES = stockNoteService.getStockNotesCount( customerId );
        List<StockNoteCountDTO> stockNoteCountDTOS = listCopyStockNoteCountEntityToStockNoteCountDTO
            .copy( stockNoteCountDES );
        logMethodEnd( methodName, stockNoteCountDTOS.size() );
        return stockNoteCountDTOS;
    }

    /**
     * Get all of the stocks notes sources for a customer
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = "/stockNotesSources/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<StockNoteSourceDTO> getStockNotesSources( final @PathVariable int customerId )
    {
        final String methodName = "getStockNotesSources";
        logMethodBegin( methodName );
        List<StockNoteSourceEntity> stockNoteSourceEntities = stockNoteService.getStockNoteSources( customerId );
        List<StockNoteSourceDTO> stockNoteSourceDTOs =
            this.listCopyStockNoteSourceEntityToStockNoteSourceDTO.copy( stockNoteSourceEntities );
        logMethodEnd( methodName, stockNoteSourceDTOs.size() );
        return stockNoteSourceDTOs;
    }
}
