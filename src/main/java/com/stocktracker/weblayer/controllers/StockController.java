package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.DuplicateTickerSymbolException;
import com.stocktracker.common.exceptions.StockNotFoundInDatabaseException;
import com.stocktracker.common.exceptions.StockNotFoundInExchangeException;
import com.stocktracker.servicelayer.entity.StockDE;
import com.stocktracker.servicelayer.entity.StockSectorDE;
import com.stocktracker.servicelayer.entity.StockSubSectorDE;
import com.stocktracker.weblayer.dto.StockDTO;
import com.stocktracker.weblayer.dto.StockSectorsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import yahoofinance.Stock;

import java.io.IOException;
import java.util.List;

/**
 * Created by mike on 9/11/2016.
 */
@RestController
@CrossOrigin
public class StockController extends AbstractController implements MyLogger
{
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
        Page<StockDE> stockDEs = stockService.getPage( pageRequest,false );
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
    @RequestMapping( value = "/stocks/companiesLike/{companiesLike}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public Page<StockDTO> getStockCompaniesMatching( Pageable pageRequest, @PathVariable( "companiesLike" ) String companiesLike )
    {
        final String methodName = "getStockCompaniesMatching";
        logMethodBegin( methodName, pageRequest, companiesLike );
        if ( companiesLike == null || companiesLike.isEmpty() )
        {
            throw new IllegalArgumentException( "companiesLike cannot be null or empty" );
        }
        Page<StockDE> stockDEs = stockService.getCompaniesLike( pageRequest,
                                                                companiesLike,
                                                                false );
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
        StockDTO stockDTO;
        /*
         * Search the database first
         */
        try
        {
            StockDE stockDE = stockService.getStock( tickerSymbol );
            stockDTO = StockDTO.newInstance( stockDE );
        }
        catch( StockNotFoundInDatabaseException e )
        {
            /*
             * Not in DB, search yahoo
             */
            try
            {
                Stock yahooStock = stockService.getStockFromYahoo( tickerSymbol );
                logDebug( methodName, "yahooStock: {0}", yahooStock );
                if ( yahooStock.getName() == null )
                {
                    throw new StockNotFoundInExchangeException( tickerSymbol );
                }
                StockDE stockDE = stockService.addStock( yahooStock );
                stockDTO = StockDTO.newInstance( stockDE );
            }
            catch( IOException e2 )
            {
                throw new StockNotFoundInExchangeException( tickerSymbol, e2 );
            }
        }
        logMethodEnd( methodName, stockDTO );
        return stockDTO;
    }

    /**
     * Add a stock to the database
     *
     * @return The stock that was added
     */
    @CrossOrigin
    @RequestMapping( value = "/stocks",
                      method = RequestMethod.POST )
    public ResponseEntity<StockDTO> addStock( @RequestBody StockDTO stockDTO )
    {
        final String methodName = "addStock";
        logMethodBegin( methodName, stockDTO );
        if ( stockService.isStockExistsInDatabase( stockDTO.getTickerSymbol() ))
        {
            logError( methodName, "Duplicate stock: " + stockDTO.getTickerSymbol() );
            throw new DuplicateTickerSymbolException( stockDTO.getTickerSymbol() );
        }
        StockDE stockDE = StockDE.newInstance( stockDTO );
        logDebug( methodName, "call stockDE: {0}", stockDE );
        stockDE = stockService.addStock( stockDE );
        logDebug( methodName, "return stockDE: {0}", stockDE );
        StockDTO returnStockDTO = StockDTO.newInstance( stockDE );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( stockDE ).toUri());
        logMethodEnd( methodName, returnStockDTO );
        return new ResponseEntity<>( stockDTO, httpHeaders, HttpStatus.CREATED );
    }

    /**
     * Delete a stock by ticker symbol
     * @param tickerSymbol
     * @return
     * @throws StockNotFoundInDatabaseException if the stock does not exist
     */
    @CrossOrigin
    @RequestMapping( value = "/stocks/{tickerSymbol}",
                     method = RequestMethod.DELETE )
    public ResponseEntity<Void> deleteStock( @PathVariable( "tickerSymbol" ) String tickerSymbol )
    {
        final String methodName = "deleteStock";
        logMethodBegin( methodName, tickerSymbol );
        if ( stockService.isStockExistsInDatabase( tickerSymbol ))
        {
            logDebug( methodName, "tickerSymbol: {0}", tickerSymbol );
            stockService.deleteStock( tickerSymbol );
        }
        else
        {
            throw new StockNotFoundInDatabaseException( tickerSymbol );
        }
        logMethodEnd( methodName );
        return new ResponseEntity<>( HttpStatus.OK );
    }

    /**
     * Get all of the stocks sector information
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = "/stockSectors",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public StockSectorsDTO getStockSectors()
    {
        final String methodName = "getStockSectors";
        logMethodBegin( methodName );
        List<StockSectorDE> stockSectorDEList = stockService.getStockSectors();
        List<StockSubSectorDE> stockSubSectorDEList = stockService.getStockSubSectors();
        StockSectorsDTO stockSectorsDTO = StockSectorsDTO.newInstance( stockSectorDEList, stockSubSectorDEList );
        logMethodEnd( methodName, stockSectorsDTO );
        return stockSectorsDTO;
    }
}
