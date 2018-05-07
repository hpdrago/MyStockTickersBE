package com.stocktracker.weblayer.controllers;

import com.fasterxml.uuid.impl.UUIDUtil;
import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.DuplicateEntityException;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockQuoteUnavailableException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.servicelayer.service.PortfolioEntityService;
import com.stocktracker.servicelayer.service.PortfolioStockEntityService;
import com.stocktracker.weblayer.dto.PortfolioDTO;
import com.stocktracker.weblayer.dto.PortfolioStockDTO;
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
    @RequestMapping( value = CONTEXT_URL + "/customerId/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<PortfolioDTO> getPortfoliosByCustomerUuid( @PathVariable String customerId )
        throws StockNotFoundException,
               StockQuoteUnavailableException,
               EntityVersionMismatchException
    {
        final String methodName = "getPortfoliosByCustomerUuid";
        logMethodBegin( methodName, customerId );
        final List<PortfolioDTO> portfolioDTOs = this.portfolioService
                                                     .getPortfoliosByCustomerUuid( UUIDUtil.uuid( customerId ));
        logMethodEnd( methodName, portfolioDTOs );
        return portfolioDTOs;
    }

    /**
     * Get a list of stocks in a customer's portfolio
     * @param customerId
     * @param portfolioId
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/id/{portfolioId}/customerId/{customerId}",
        method = RequestMethod.GET,
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<PortfolioStockDTO> getPortfolioStocks( @PathVariable String customerId,
                                                       @PathVariable String portfolioId )
    {
        final String methodName = "getPortfolioStocks";
        logMethodBegin( methodName, customerId, portfolioId );
        Objects.requireNonNull( customerId, "customerId cannot be null" );
        Objects.requireNonNull( portfolioId, "portfolioId cannot be null" );
        final List<PortfolioStockDTO> portfolioStockDTOs = this.portfolioStockService
                                                               .getPortfolioStocks( UUIDUtil.uuid( portfolioId ));
        logMethodEnd( methodName, portfolioStockDTOs );
        return portfolioStockDTOs;
    }

    /**
     * Get a list of stocks in a customer's portfolio
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/customerId/{customerId}",
        method = RequestMethod.POST )
    public ResponseEntity<PortfolioDTO> addPortfolio( @PathVariable String customerId,
                                                      @RequestBody PortfolioDTO portfolioDTO )
        throws EntityVersionMismatchException,
               DuplicateEntityException
    {
        final String methodName = "addPortfolio";
        logMethodBegin( methodName, customerId, portfolioDTO );
        final PortfolioDTO returnPortfolioDTO = portfolioService.addDTO( portfolioDTO );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                    .fromCurrentRequest()
                                    .path("/{id}")
                                    .buildAndExpand( returnPortfolioDTO )
                                    .toUri());
        logMethodEnd( methodName, returnPortfolioDTO );
        return new ResponseEntity<>( returnPortfolioDTO, httpHeaders, HttpStatus.CREATED);
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
    @RequestMapping(value = CONTEXT_URL + "/id/{portfolioId}/customerId/{customerId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deletePortfolio( @PathVariable( "portfolioId" ) String portfolioId,
                                                 @PathVariable( "customerId" ) String customerId )
        throws VersionedEntityNotFoundException
    {
        final String methodName = "deletePortfolio";
        logMethodBegin( methodName, customerId, portfolioId );
        Objects.requireNonNull( portfolioId, "portfolioId cannot be null" );
        Objects.requireNonNull( customerId, "customerId cannot be null" );
        portfolioService.deleteEntity( UUIDUtil.uuid( portfolioId ) );
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
    @RequestMapping(value = CONTEXT_URL + "/id/{portfolioId}/customerId/{customerId}",
                    method = RequestMethod.GET)
    public ResponseEntity<PortfolioDTO> getPortfolio( @PathVariable String portfolioId,
                                                      @PathVariable String customerId )
        throws StockNotFoundException,
               StockQuoteUnavailableException,
               EntityVersionMismatchException
    {
        final String methodName = "getPortfolio";
        logMethodBegin( methodName, customerId, portfolioId );
        Objects.requireNonNull( customerId, "customerId cannot be null" );
        Objects.requireNonNull( portfolioId, "portfolioId cannot be null" );
        final PortfolioDTO portfolioDTO = this.portfolioService
                                              .getPortfolioByUuid( UUIDUtil.uuid( portfolioId ));
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
