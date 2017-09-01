package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import com.stocktracker.servicelayer.entity.StockNoteCountDE;
import com.stocktracker.servicelayer.entity.StockNoteDE;
import com.stocktracker.servicelayer.entity.StockNoteSourceDE;
import com.stocktracker.weblayer.dto.StockNoteCountDTO;
import com.stocktracker.weblayer.dto.StockNoteDTO;
import com.stocktracker.weblayer.dto.StockNoteSourceDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by mike on 5/7/2017.
 */
@RestController
public class StockNotesController extends AbstractController implements MyLogger
{
    /**
     * Get all of the stocks notes for a customer and ticker symbol
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = "/stockNotes/{customerId}/{tickerSymbol}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<StockNoteDTO> getStockNotes( final @PathVariable int customerId,
                                             final @PathVariable String tickerSymbol )
    {
        final String methodName = "getStockNotes";
        logMethodBegin( methodName );
        List<StockNoteDE> stockNoteDEs = stockNoteService.getStockNotes( customerId, tickerSymbol );
        List<StockNoteDTO> stockNoteDTOs = listCopyStockNoteDEToStockNoteDTO.copy( stockNoteDEs );
        logMethodEnd( methodName, stockNoteDTOs.size() );
        return stockNoteDTOs;
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
        List<StockNoteCountDE> stockNoteCountDES = stockNoteService.getStockNotesCount( customerId );
        List<StockNoteCountDTO> stockNoteCountDTOS = listCopyStockNoteCountDEToStockNoteCountDTO.copy( stockNoteCountDES );
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
        List<StockNoteSourceDE> stockNoteSourceDEs = stockNoteService.getStockNoteSources( customerId );
        List<StockNoteSourceDTO> stockNoteSourceDTOs = listCopyStockNoteSourceDEToStockNoteSourceDTO.copy( stockNoteSourceDEs );
        logMethodEnd( methodName, stockNoteSourceDTOs.size() );
        return stockNoteSourceDTOs;
    }
}
