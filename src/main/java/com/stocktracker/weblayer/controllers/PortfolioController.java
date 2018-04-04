package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockQuoteUnavailableException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.servicelayer.service.PortfolioEntityService;
import com.stocktracker.servicelayer.service.PortfolioStockEntityService;
import com.stocktracker.weblayer.dto.PortfolioDTO;
import com.stocktracker.weblayer.dto.PortfolioStockQuoteDTO;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Objects;

/**
 * Created by mike on 9/11/2016.
 */
@RestController
@CrossOrigin
public class PortfolioController extends AbstractController implements MyLogger
{
    private static final String CONTEXT_URL = "/portfolio";
    private PortfolioEntityService portfolioService;
    private PortfolioStockEntityService portfolioStockService;

    /**
     * Get a list of portfolios for a single customer {@code customerId}
     * @param customerId
     * @return
     * @throws StockNotFoundException
     * @throws StockQuoteUnavailableException
     * @throws EntityVersionMismatchException
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/customer/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<PortfolioDTO> getPortfoliosByCustomerId( @PathVariable int customerId )
        throws StockNotFoundException,
               StockQuoteUnavailableException,
               EntityVersionMismatchException
    {
        final String methodName = "getPortfoliosByCustomerId";
        logMethodBegin( methodName, customerId );
        List<PortfolioDTO> portfolioDTOs = portfolioService.getPortfoliosByCustomerId( customerId );
        logMethodEnd( methodName, portfolioDTOs );
        return portfolioDTOs;
    }

    /**
     * Get a list of stocks in a customer's portfolio
     * @param customerId
     * @param portfolioId
     * @return
     * @throws StockNotFoundException
     * @throws StockQuoteUnavailableException
     * @throws EntityVersionMismatchException
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/id/{portfolioId}/customer/{customerId}",
        method = RequestMethod.GET,
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<PortfolioStockQuoteDTO> getPortfolioStocks( @PathVariable Integer customerId,
                                                            @PathVariable Integer portfolioId )
        throws StockNotFoundException,
               StockQuoteUnavailableException
    {
        final String methodName = "getPortfolioStocks";
        logMethodBegin( methodName, customerId, portfolioId );
        Objects.requireNonNull( customerId, "customerId cannot be null" );
        Objects.requireNonNull( portfolioId, "portfolioId cannot be null" );
        List<PortfolioStockQuoteDTO> portfolioStockDTOs = portfolioStockService.getPortfolioStocks( portfolioId );
        logMethodEnd( methodName, portfolioStockDTOs );
        return portfolioStockDTOs;
    }

    /**
     * Get a list of stocks in a customer's portfolio
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/customer/{customerId}",
        method = RequestMethod.POST )
    public ResponseEntity<PortfolioDTO> addPortfolio( @PathVariable Integer customerId,
                                                      @RequestBody PortfolioDTO portfolioDto )
        throws EntityVersionMismatchException
    {
        final String methodName = "addPortfolio";
        logMethodBegin( methodName, customerId, portfolioDto );
        PortfolioDTO portfolioDTO = portfolioService.addPortfolio( customerId, portfolioDto );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                    .fromCurrentRequest()
                                    .path("/{id}")
                                    .buildAndExpand( portfolioDTO )
                                    .toUri());
        logMethodEnd( methodName, portfolioDTO );
        return new ResponseEntity<>( portfolioDTO, httpHeaders, HttpStatus.CREATED);
    }

    /**
     * Delete a portfolio by portfolio id
     * @param portfolioId
     * @param customerId
     * @return
     * @throws StockNotFoundException
     * @throws StockQuoteUnavailableException
     * @throws EntityVersionMismatchException
     */
    @CrossOrigin
    @RequestMapping(value = CONTEXT_URL + "/id/{portfolioId}/customer/{customerId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deletePortfolio( @PathVariable( "portfolioId" ) Integer portfolioId,
                                                 @PathVariable( "customerId" ) Integer customerId )
        throws VersionedEntityNotFoundException
    {
        final String methodName = "deletePortfolio";
        logMethodBegin( methodName, customerId, portfolioId );
        Objects.requireNonNull( portfolioId, "portfolioId cannot be null" );
        Objects.requireNonNull( customerId, "customerId cannot be null" );
        portfolioService.deleteEntity( portfolioId );
        logMethodBegin( methodName );
        return new ResponseEntity<>( HttpStatus.OK );
    }

    /**
     * Get a single portfolio
     * @param portfolioId
     * @param customerId
     * @return
     * @throws StockNotFoundException
     * @throws StockQuoteUnavailableException
     * @throws EntityVersionMismatchException
     */
    @CrossOrigin
    @RequestMapping(value = CONTEXT_URL + "/id/{portfolioId}/customer/{customerId}", method = RequestMethod.GET)
    public ResponseEntity<PortfolioDTO> getPortfolio( @PathVariable int portfolioId,
                                                      @PathVariable int customerId )
        throws StockNotFoundException,
               StockQuoteUnavailableException,
               EntityVersionMismatchException
    {
        final String methodName = "getPortfolio";
        logMethodBegin( methodName, customerId, portfolioId );
        Objects.requireNonNull( customerId, "customerId cannot be null" );
        Objects.requireNonNull( portfolioId, "portfolioId cannot be null" );
        PortfolioDTO portfolioDTO = portfolioService.getPortfolioById( portfolioId );
        logDebug( methodName, "portfolio: {0}", portfolioDTO.toString() );
        logMethodBegin( methodName, portfolioDTO );
        return new ResponseEntity<>( portfolioDTO, HttpStatus.OK );
    }

    @Autowired
    public void setPortfolioStockService( final PortfolioStockEntityService portfolioStockService )
    {
        this.portfolioStockService = portfolioStockService;
    }

    @Autowired
    public void setPortfolioService( final PortfolioEntityService portfolioService )
    {
        this.portfolioService = portfolioService;
    }
}
