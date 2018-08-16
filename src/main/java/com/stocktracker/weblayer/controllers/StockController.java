package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.servicelayer.service.StockCompanyEntityService;
import com.stocktracker.servicelayer.service.StockQuoteEntityService;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDataRequestException;
import com.stocktracker.servicelayer.service.stocks.StockPriceQuoteService;
import com.stocktracker.weblayer.dto.StockCompanyDTO;
import com.stocktracker.weblayer.dto.StockPriceQuoteDTO;
import com.stocktracker.weblayer.dto.StockQuoteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    private StockPriceQuoteService stockPriceQuoteService;
    private StockQuoteEntityService stockQuoteEntityService;

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
        final Page<StockCompanyDTO> stockCompanyDTOs = this.stockCompanyEntityService
                                                           .getPage( pageRequest, false );
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
     * Get a stock price quote for {@code tickerSymbol}
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/stockPriceQuote/{tickerSymbol}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public StockPriceQuoteDTO getStockPriceQuote( @PathVariable final String tickerSymbol )
    {
        final String methodName = "getStockPriceQuote";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be nulls");
        Assert.isTrue( !tickerSymbol.equalsIgnoreCase( "null" ), "ticker symbol cannot be 'null'");
        final StockPriceQuoteDTO stockPriceQuoteDTO = this.stockPriceQuoteService
                                                          .getSynchronousStockPriceQuote( tickerSymbol );
        logMethodEnd( methodName, stockPriceQuoteDTO );
        return stockPriceQuoteDTO;
    }

    /**
     * Get a single stock for {@code tickerSymbol}
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/company/tickerSymbol/{tickerSymbol}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public StockCompanyDTO getStockCompany( @PathVariable final String tickerSymbol )
        throws StockNotFoundException
    {
        final String methodName = "getStockCompany";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be nulls");
        Assert.isTrue( !tickerSymbol.equalsIgnoreCase( "null" ), "ticker symbol cannot be 'null'");
        StockCompanyDTO stockCompanyDTO = null;
        try
        {
            stockCompanyDTO = this.stockCompanyEntityService
                                                  .getStockCompanyDTO( tickerSymbol );
        }
        catch( AsyncCacheDataRequestException e )
        {
            throw new StockNotFoundException( tickerSymbol, e );
        }
        logMethodEnd( methodName, stockCompanyDTO );
        return stockCompanyDTO;
    }

    /**
     * Get a stock quote for {@code tickerSymbol}
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/quote/tickerSymbol/{tickerSymbol}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public StockQuoteDTO getStockQuote( @PathVariable final String tickerSymbol )
        throws StockNotFoundException
    {
        final String methodName = "getStockQuote";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be nulls");
        Assert.isTrue( !tickerSymbol.equalsIgnoreCase( "null" ), "ticker symbol cannot be 'null'");
        final StockQuoteDTO stockQuoteDTO = this.stockQuoteEntityService
                                                .getStockQuoteDTO( tickerSymbol );
        logMethodEnd( methodName, stockQuoteDTO );
        return stockQuoteDTO;
    }

    @Autowired
    public void setStockCompanyEntityService( final StockCompanyEntityService stockCompanyEntityService )
    {
        this.stockCompanyEntityService = stockCompanyEntityService;
    }

    @Autowired
    public void setStockPriceQuoteService( final StockPriceQuoteService stockPriceQuoteService )
    {
        this.stockPriceQuoteService = stockPriceQuoteService;
    }

    @Autowired
    public void setStockQuoteEntityService( final StockQuoteEntityService stockQuoteEntityService )
    {
        this.stockQuoteEntityService = stockQuoteEntityService;
    }

}
