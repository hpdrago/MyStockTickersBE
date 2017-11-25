package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.DuplicateTickerSymbolException;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockNotFoundInDatabaseException;
import com.stocktracker.common.exceptions.StockQuoteUnavailableException;
import com.stocktracker.servicelayer.service.StockQuoteService;
import com.stocktracker.servicelayer.service.StockService;
import com.stocktracker.servicelayer.service.stockinformationprovider.StockQuote;
import com.stocktracker.servicelayer.service.stockinformationprovider.StockQuoteFetchMode;
import com.stocktracker.weblayer.dto.StockDTO;
import com.stocktracker.weblayer.dto.StockSectorsDTO;
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

/**
 * Created by mike on 9/11/2016.
 */
@RestController
@CrossOrigin
public class StockController extends AbstractController implements MyLogger
{
    private StockService stockService;
    private StockQuoteService stockQuoteService;

    /**
     * Get all of the stocks within the pageRequest parameters
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = "/stocks",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public Page<StockDTO> getStocks( final Pageable pageRequest )
        throws StockNotFoundException,
               StockQuoteUnavailableException
    {
        final String methodName = "getStockNotesStocks";
        logMethodBegin( methodName, pageRequest );
        Page<StockDTO> stockDTOs = this.stockService.getPage( pageRequest,false );
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
    public Page<StockDTO> getStockCompaniesMatching( final Pageable pageRequest,
                                                     final @PathVariable( "companiesLike" ) String companiesLike )
        throws StockNotFoundException,
               StockQuoteUnavailableException
    {
        final String methodName = "getStockCompaniesMatching";
        logMethodBegin( methodName, pageRequest, companiesLike );
        if ( companiesLike == null || companiesLike.isEmpty() )
        {
            throw new IllegalArgumentException( "companiesLike cannot be null or empty" );
        }
        Page<StockDTO> stockDTOs = this.stockService.getCompaniesLike( pageRequest,
                                                                      companiesLike,
                                                                false );
        logMethodEnd( methodName, stockDTOs );
        return stockDTOs;
    }

    /**
     * Get a stock quote for {@code tickerSymbol}
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = "/stockQuote/{tickerSymbol}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<StockQuote> getStockQuote( @PathVariable final String tickerSymbol )
        throws StockNotFoundException,
               StockQuoteUnavailableException
    {
        final String methodName = "getStockQuote";
        logMethodBegin( methodName, tickerSymbol );
        StockQuote stockTickerQuote = null;
        HttpStatus httpStatus = HttpStatus.OK;
        try
        {
            stockTickerQuote = this.stockQuoteService
                                   .getStockQuote( tickerSymbol, StockQuoteFetchMode.SYNCHRONOUS );
        }
        catch( javax.ws.rs.NotFoundException e )
        {
            httpStatus = HttpStatus.NOT_FOUND;
        }
        logMethodEnd( methodName, stockTickerQuote );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest()
                                     .path( "" )
                                     .buildAndExpand( stockTickerQuote )
                                     .toUri());
        logMethodEnd( methodName, stockTickerQuote );
        return new ResponseEntity<>( stockTickerQuote, httpHeaders, httpStatus );
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
    public StockDTO getStock( @PathVariable final String tickerSymbol )
    {
        final String methodName = "getStock";
        logMethodBegin( methodName, tickerSymbol );
        StockDTO stockDTO;
        /*
         * Search the database first
         */
        stockDTO = this.stockService.getStock( tickerSymbol );
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
    public ResponseEntity<StockDTO> addStock( @RequestBody final StockDTO stockDTO )
    {
        final String methodName = "saveStock";
        logMethodBegin( methodName, stockDTO );
        if ( this.stockService.isStockExistsInDatabase( stockDTO.getTickerSymbol() ))
        {
            logError( methodName, "Duplicate stock: " + stockDTO.getTickerSymbol() );
            throw new DuplicateTickerSymbolException( stockDTO.getTickerSymbol() );
        }
        logDebug( methodName, "call stockDTO: {0}", stockDTO );
        StockDTO newStockDTO = this.stockService.saveStock( stockDTO );
        logDebug( methodName, "return stockDTO: {0}", newStockDTO );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest()
                                     .path( "" )
                                     .buildAndExpand( newStockDTO )
                                     .toUri());
        logMethodEnd( methodName, newStockDTO );
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
    public ResponseEntity<Void> deleteStock( @PathVariable( "tickerSymbol" ) final String tickerSymbol )
    {
        final String methodName = "deleteStock";
        logMethodBegin( methodName, tickerSymbol );
        if ( this.stockService.isStockExistsInDatabase( tickerSymbol ))
        {
            logDebug( methodName, "tickerSymbol: {0}", tickerSymbol );
            this.stockService.deleteStock( tickerSymbol );
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
        /*
        List<StockSectorDE> stockSectorDEList = this.stockService.getStockSectors();
        List<StockSubSectorDE> stockSubSectorDEList = this.stockService.getStockSubSectors();
        StockSectorsDTO stockSectorsDTO = StockSectorsDTO.newInstance( stockSectorDEList, stockSubSectorDEList );
        logMethodEnd( methodName, stockSectorsDTO );
        return stockSectorsDTO;
        */
        return null;
    }

    @Autowired
    public void setStockService( final StockService stockService )
    {
        this.stockService = stockService;
    }

    @Autowired
    public void setStockQuoteService( final StockQuoteService stockQuoteService )
    {
        this.stockQuoteService = stockQuoteService;
    }

}
