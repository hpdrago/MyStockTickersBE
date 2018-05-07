package com.stocktracker.weblayer.controllers;

import com.fasterxml.uuid.impl.UUIDUtil;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.PortfolioStockMissingDataException;
import com.stocktracker.common.exceptions.PortfolioStockNotFound;
import com.stocktracker.common.exceptions.EntityNotFoundException;
import com.stocktracker.servicelayer.service.PortfolioStockEntityService;
import com.stocktracker.weblayer.controllers.portfoliostock.AddPortfolioStockHandler;
import com.stocktracker.weblayer.controllers.portfoliostock.SavePortfolioStockHandler;
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
 * This class contains all of the PortfolioLastStockDTO related REST weblayer service call mapping and handling
 * <p>
 * Created by mike on 5/9/2016.
 */
@RestController
public class PortfolioStockController extends AbstractController
{
    private static final String CONTEXT_URL = "/portfolioStock";
    private PortfolioStockEntityService portfolioStockService;
    private AddPortfolioStockHandler addPortfolioStockHandler;
    private SavePortfolioStockHandler savePortfolioStockHandler;

    /**
     * Get a single customer stock entry
     * @param customerId
     * @param portfolioId
     * @param tickerSymbol
     * @return
     * @throws PortfolioStockNotFound
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/customerId/{customerId}/portfolio/{portfolioId}/stock/{tickerSymbol}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public PortfolioStockDTO getPortfolioStock( @PathVariable String customerId,
                                                @PathVariable String portfolioId,
                                                @PathVariable String tickerSymbol )
        throws PortfolioStockNotFound
    {
        final String methodName = "getPortfolioStock";
        logMethodBegin( methodName, customerId, portfolioId, tickerSymbol );
        PortfolioStockDTO portfolioStockDTO = this.portfolioStockService
                                                  .getPortfolioStock( UUIDUtil.uuid( customerId ),
                                                                      UUIDUtil.uuid( portfolioId ),
                                                                      tickerSymbol );
        logMethodEnd( methodName, portfolioStockDTO );
        return portfolioStockDTO;
    }

    /**
     * Delete a single customer portfolio stock entry
     *
     * @param portfolioStockId
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/{portfolioStockId}/customerId/{customerId}",
        method = RequestMethod.DELETE,
        produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<Void> deletePortfolioStock( @PathVariable String portfolioStockId,
                                                      @PathVariable String customerId )
        throws PortfolioStockNotFound
    {
        final String methodName = "deletePortfolioStock";
        logMethodBegin( methodName, customerId, portfolioStockId );
        try
        {
            this.portfolioStockService
                .deleteEntity( portfolioStockId );
        }
        catch( EntityNotFoundException e )
        {
            throw new PortfolioStockNotFound( portfolioStockId );
        }
        logMethodEnd( methodName );
        return new ResponseEntity<>( HttpStatus.OK );
    }

    /**
     * Get all of the stocks for a portfolio
     *
     * @param customerId
     * @param portfolioId
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/customerId/{customerId}/portfolio/{portfolioId}/stocks",
        method = RequestMethod.GET,
        produces = {MediaType.APPLICATION_JSON_VALUE} )
    public List<PortfolioStockDTO> getPortfolioStocks( @PathVariable String customerId,
                                                       @PathVariable String portfolioId )
        throws PortfolioStockNotFound
    {
        final String methodName = "getPortfolioStocks";
        logMethodBegin( methodName, customerId, portfolioId );
        List<PortfolioStockDTO> portfolioStockDTOList = this.portfolioStockService
                                                            .getPortfolioStocks( UUIDUtil.uuid( portfolioId ));
        logMethodEnd( methodName, String.format( "Found %d stocks", portfolioStockDTOList.size() ) );
        return portfolioStockDTOList;
    }

    /**
     * Add a portfolio stock to the database
     *
     * @return The customer stock that was added
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/customerId/{customerId}",
                     method = RequestMethod.POST )
    public ResponseEntity<PortfolioStockDTO> addPortfolioStock( @RequestBody PortfolioStockDTO portfolioStockDTO,
                                                                @PathVariable String customerId )
        throws Exception
    {
        final String methodName = "addPortfolioStock";
        logMethodBegin( methodName, customerId, portfolioStockDTO );
        checkRequiredFields( portfolioStockDTO );
        /*
         * Do the work
         */
        final PortfolioStockDTO newPortfolioStockDTO = this.portfolioStockService
                                                           .addDTO( portfolioStockDTO );
        /*
         * send the response
         */
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( newPortfolioStockDTO ).toUri());
        logMethodEnd( methodName );
        return new ResponseEntity<>( newPortfolioStockDTO, httpHeaders, HttpStatus.CREATED );
    }

    /**
     * Add a portfolio stock to the database
     *
     * @return The customer stock that was added
     */
    @CrossOrigin
    @RequestMapping( value = CONTEXT_URL + "/customerId/{customerId}",
        method = RequestMethod.PUT )
    public ResponseEntity<PortfolioStockDTO> savePortfolioStock( @RequestBody PortfolioStockDTO portfolioStockDTO,
                                                                 @PathVariable String customerId )
        throws EntityVersionMismatchException
    {
        final String methodName = "savePortfolioStock";
        logMethodBegin( methodName, customerId, portfolioStockDTO );
        checkRequiredFields( portfolioStockDTO );
        /*
         * Save the stock
         */
        PortfolioStockDTO returnPortfolioStockDTO = this.portfolioStockService
                                                        .saveDTO( portfolioStockDTO );
        /*
         * send the response
         */
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( returnPortfolioStockDTO ).toUri());
        logMethodEnd( methodName, returnPortfolioStockDTO );
        return new ResponseEntity<>( portfolioStockDTO, httpHeaders, HttpStatus.CREATED );
    }

    /**
     * Checks the {@code portfolioStockDTO} for the required fields
     * @param portfolioStockDTO
     */
    private void checkRequiredFields( final @RequestBody PortfolioStockDTO portfolioStockDTO )
        throws PortfolioStockMissingDataException
    {
        try
        {
            Objects.requireNonNull( portfolioStockDTO.getCustomerId(), "Customer id cannot be null" );
            Objects.requireNonNull( portfolioStockDTO.getPortfolioId(), "Portfolio id cannot be null" );
            Objects.requireNonNull( portfolioStockDTO.getTickerSymbol(), "Ticker Symbol id cannot be null" );
            if ( portfolioStockDTO.getTickerSymbol().isEmpty() )
            {
                throw new IllegalArgumentException( "Ticker symbol cannot be empty" );
            }
        }
        catch( Exception e )
        {
            throw new PortfolioStockMissingDataException( e );
        }
    }

    @Autowired
    public void setAddPortfolioStockHandler( final AddPortfolioStockHandler addPortfolioStockHandler )
    {
        this.addPortfolioStockHandler = addPortfolioStockHandler;
    }

    @Autowired
    public void setSavePortfolioStockHandler( final SavePortfolioStockHandler savePortfolioStockHandler )
    {
        this.savePortfolioStockHandler = savePortfolioStockHandler;
    }

    @Autowired
    public void setPortfolioStockService( final PortfolioStockEntityService portfolioStockService )
    {
        this.portfolioStockService = portfolioStockService;
    }
}
