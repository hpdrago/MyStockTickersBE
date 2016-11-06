package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import com.stocktracker.servicelayer.entity.StockDE;
import com.stocktracker.servicelayer.service.StockService;
import com.stocktracker.weblayer.dto.StockDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
 * Created by mike on 9/11/2016.
 */
@RestController
public class StockController extends BaseController implements MyLogger
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
        logMethodBegin( methodName, pageRequest );
        Page<StockDE> stockDEs = stockService.getPage( pageRequest );
        Page<StockDTO> stockDTOs = mapStockEntityPageIntoStockDEPage( pageRequest, stockDEs );
        logMethodEnd( methodName, stockDTOs );
        return stockDTOs;
    }

    /**
     * Get all of the stocks within the pageRequest parameters
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = "/stocks/companieslike/{companiesLike}",
        method = RequestMethod.GET,
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public Page<StockDTO> getStockCompaniesMatching( Pageable pageRequest, @PathVariable( "companiesLike" ) String companiesLike )
    {
        final String methodName = "StockCompaniesMatching";
        logMethodBegin( methodName, pageRequest, companiesLike );
        if ( companiesLike == null || companiesLike.isEmpty() )
        {

            throw new IllegalArgumentException( "companiesLike cannot be null or empty" );
        }
        Page<StockDE> stockDEs = stockService.getCompaniesLike( pageRequest, "%" + companiesLike + "%" );
        Page<StockDTO> stockDTOs = mapStockEntityPageIntoStockDEPage( pageRequest, stockDEs );
        logMethodEnd( methodName, stockDTOs );
        return stockDTOs;
    }

    /**
     * Transforms {@code Page<ENTITY>} objects into {@code Page<DTO>} objects.
     *
     * @param pageRequest The information of the requested page.
     * @param source      The {@code Page<ENTITY>} object.
     * @return The created {@code Page<DTO>} object.
     */
    private Page<StockDTO> mapStockEntityPageIntoStockDEPage( Pageable pageRequest, Page<StockDE> source )
    {
        List<StockDTO> stockDTOs = listCopyStockDEToStockDTO.copy( source.getContent() );
        return new PageImpl<>( stockDTOs, pageRequest, source.getTotalElements() );
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
        StockDE stockDE = stockService.getStock( tickerSymbol );
        StockDTO stockDTO = StockDTO.newInstance( stockDE );
        logMethodEnd( methodName, stockDTO );
        return stockDTO;
    }

    /**
     * Add a stock to the database
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = "/stocks",
        method = RequestMethod.POST )
    public ResponseEntity<Void> addStock( @RequestBody StockDTO stockDTO )
    {
        final String methodName = "addStock";
        logMethodBegin( methodName, stockDTO );
        StockDE stockDE = StockDE.newInstance( stockDTO );
        stockDE = stockService.addStock( stockDE );
        StockDTO returnStockDTO = StockDTO.newInstance( stockDE );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( stockDE ).toUri());
        logMethodEnd( methodName, returnStockDTO );
        return new ResponseEntity<>( null, httpHeaders, HttpStatus.CREATED );
    }

    /**
     * Delete a stock by ticker symbol
     * @param tickerSymbol
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/stocks/{tickerSymbol}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteStock( @PathVariable( "tickerSymbol" ) String tickerSymbol )
    {
        final String methodName = "deleteStock";
        logMethodBegin( methodName, tickerSymbol );
        StockDE stockDE = stockService.getStock( tickerSymbol );
        logDebug( methodName, "stock: %s", stockDE );
        stockService.deleteStock( stockDE );
        logMethodBegin( methodName, stockDE );
        return new ResponseEntity<>( HttpStatus.OK );
    }
}
