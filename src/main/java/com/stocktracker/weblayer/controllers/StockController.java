package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import com.stocktracker.servicelayer.service.StockService;
import com.stocktracker.weblayer.dto.StockDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mike on 9/11/2016.
 */
@RestController
public class StockController implements MyLogger
{
    /**
     * Autowired service class
     */
    private StockService stockService;

    /**
     * Allow DI to set the CustomerHandler
     *
     * @param stockService
     */
    @Autowired
    public void setStockService( final StockService stockService )
    {
        final String methodName = "setStockService";
        logMethodBegin( methodName, stockService );
        this.stockService = stockService;
    }

    /**
     * Get all of the stocks within the pageRequest parameters
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = "/stocks",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public Page<StockDTO> getStocks( Pageable pageRequest )
    {
        final String methodName = "getStocks";
        logMethodBegin( methodName );
        Page<StockDTO> stockDTOs = stockService.getPage( pageRequest );
        logMethodEnd( methodName, stockDTOs );
        return stockDTOs;
    }

    /**
     * Get a single stock for {@code tickerSymbol}
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = "/stocks/{tickerSymbol}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public StockDTO getStock( @PathVariable String tickerSymbol )
    {
        final String methodName = "getStock";
        logMethodBegin( methodName, tickerSymbol );
        StockDTO stockDTO = stockService.getStock( tickerSymbol );
        logMethodEnd( methodName, stockDTO );
        return stockDTO;
    }

}
