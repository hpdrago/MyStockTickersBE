package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.StockCompanyNotFoundException;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.servicelayer.service.StockCompanyEntityService;
import com.stocktracker.servicelayer.service.stocks.StockInformationService;
import com.stocktracker.servicelayer.stockinformationprovider.StockPriceFetchMode;
import com.stocktracker.servicelayer.stockinformationprovider.StockPriceQuoteDTO;
import com.stocktracker.weblayer.dto.StockCompanyDTO;
import com.stocktracker.weblayer.dto.StockSectorsDTO;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Objects;

/**
 * Created by mike on 9/11/2016.
 */
@RestController
@CrossOrigin
public class StockController extends AbstractController implements MyLogger
{
    private static final String CONTEXT_URL = "/stocks";
    private StockCompanyEntityService stockCompanyEntityService;
    private StockInformationService stockInformationService;

    /**
     * Get all of the stocks within the pageRequest parameters
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/page",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public Page<StockCompanyDTO> getStockCompanyPage( final Pageable pageRequest )
    {
        final String methodName = "getStockCompanyPage";
        logMethodBegin( methodName, pageRequest );
        Page<StockCompanyDTO> stockCompanyDTOs = this.stockCompanyEntityService.getPage( pageRequest, false );
        logMethodEnd( methodName, stockCompanyDTOs );
        return stockCompanyDTOs;
    }

    /**
     * Get all of the stocks within the pageRequest parameters
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/companiesLike/{companiesLike}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public Page<StockCompanyDTO> getStockCompaniesMatching( final Pageable pageRequest,
                                                            final @PathVariable( "companiesLike" ) String companiesLike )
    {
        final String methodName = "getStockCompaniesMatching";
        logMethodBegin( methodName, pageRequest, companiesLike );
        if ( companiesLike == null || companiesLike.isEmpty() )
        {
            throw new IllegalArgumentException( "companiesLike cannot be null or empty" );
        }
        Page<StockCompanyDTO> stockDTOs = this.stockCompanyEntityService
                                              .getCompaniesLike( pageRequest, companiesLike );
        logMethodEnd( methodName, stockDTOs );
        return stockDTOs;
    }

    /**
     * Get a stock quote for {@code tickerSymbol}
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/stockPriceQuote/{tickerSymbol}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<StockPriceQuoteDTO> getStockPrice( @PathVariable final String tickerSymbol )
    {
        final String methodName = "getStockPriceQuote";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be nulls");
        Assert.isTrue( !tickerSymbol.equalsIgnoreCase( "null" ), "ticker symbol cannot be 'null'");
        StockPriceQuoteDTO stockPriceQuoteDTO = null;
        HttpStatus httpStatus = HttpStatus.OK;
        try
        {
            stockPriceQuoteDTO = this.stockInformationService
                                     .getStockPriceQuote( tickerSymbol, StockPriceFetchMode.SYNCHRONOUS );
        }
        catch( StockNotFoundException |
               StockCompanyNotFoundException |
               javax.ws.rs.NotFoundException e )
        {
            httpStatus = HttpStatus.NOT_FOUND;
        }
        logMethodEnd( methodName, stockPriceQuoteDTO );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest()
                                     .path( "" )
                                     .buildAndExpand( stockPriceQuoteDTO )
                                     .toUri());
        logMethodEnd( methodName, stockPriceQuoteDTO );
        return new ResponseEntity<>( stockPriceQuoteDTO, httpHeaders, httpStatus );
    }
    /**
     * Get a single stock for {@code tickerSymbol}
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/{tickerSymbol}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public StockCompanyDTO getStock( @PathVariable final String tickerSymbol )
        throws StockNotFoundException
    {
        final String methodName = "getStock";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be nulls");
        Assert.isTrue( !tickerSymbol.equalsIgnoreCase( "null" ), "ticker symbol cannot be 'null'");
        StockCompanyDTO stockCompanyDTO;
        /*
         * Search the database first
         */
        try
        {
            stockCompanyDTO = this.stockCompanyEntityService.getDTO( tickerSymbol );
        }
        catch( VersionedEntityNotFoundException e )
        {
            throw new StockNotFoundException( tickerSymbol );
        }
        logMethodEnd( methodName, stockCompanyDTO );
        return stockCompanyDTO;
    }

    /**
     * Get all of the stocks sector information
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/stockSectors",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public StockSectorsDTO getStockSectors()
    {
        final String methodName = "getStockSectors";
        logMethodBegin( methodName );
        /*
        List<StockSectorDE> stockSectorDEList = this.stockCompanyEntityService.getStockSectors();
        List<StockSubSectorDE> stockSubSectorDEList = this.stockCompanyEntityService.getStockSubSectors();
        StockSectorsDTO stockSectorsDTO = StockSectorsDTO.newInstance( stockSectorDEList, stockSubSectorDEList );
        logMethodEnd( methodName, stockSectorsDTO );
        return stockSectorsDTO;
        */
        return null;
    }

    @Autowired
    public void setStockCompanyEntityService( final StockCompanyEntityService stockCompanyEntityService )
    {
        this.stockCompanyEntityService = stockCompanyEntityService;
    }

    @Autowired
    public void setStockInformationService( final StockInformationService stockInformationService )
    {
        this.stockInformationService = stockInformationService;
    }

}
