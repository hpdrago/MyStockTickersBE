package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import com.stocktracker.servicelayer.entity.StockNoteDE;
import com.stocktracker.weblayer.dto.StockNoteDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by mike on 5/7/2017.
 */
public class StockNotesController extends AbstractController implements MyLogger
{
    /**
     * Get all of the stocks notes for a customer and ticker symbol
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = "/stocknotes/{customerId}/{tickerSymbol}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<StockNoteDTO> getStockNotes( final @PathVariable int customerId,
                                             final @PathVariable String tickerSymbol )
    {
        final String methodName = "getStockNotes";
        logMethodBegin( methodName );
        List<StockNoteDE> stockNoteDEs = stockNoteService.getStockNotes( customerId, tickerSymbol );
        List<StockNoteDTO> customerDTOs = listCopyStockNoteDEToStockNoteDTO.copy( stockNoteDEs );
        logMethodEnd( methodName, customerDTOs.size() );
        return customerDTOs;
    }
}
