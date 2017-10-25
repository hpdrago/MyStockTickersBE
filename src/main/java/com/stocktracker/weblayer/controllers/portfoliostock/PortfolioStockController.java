package com.stocktracker.weblayer.controllers.portfoliostock;

import com.stocktracker.common.exceptions.PortfolioStockMissingDataException;
import com.stocktracker.common.exceptions.PortfolioStockNotFound;
import com.stocktracker.servicelayer.service.PortfolioStockService;
import com.stocktracker.weblayer.controllers.AbstractController;
import com.stocktracker.weblayer.dto.PortfolioLastStockDTO;
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
    private PortfolioStockService portfolioStockService;
    private AddPortfolioStockHandler addPortfolioStockHandler;
    private SavePortfolioStockHandler savePortfolioStockHandler;

    /**
     * Get a single customer stock entry
     *
     * @param customerId
     * @param tickerSymbol
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = "/customer/{customerId}/portfolio/{portfolioId}/stock/{tickerSymbol}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public PortfolioLastStockDTO getPortfolioStock( @PathVariable int customerId,
                                                    @PathVariable int portfolioId,
                                                    @PathVariable String tickerSymbol )
        throws PortfolioStockNotFound
    {
        final String methodName = "getPortfolioStock";
        logMethodBegin( methodName, customerId, portfolioId, tickerSymbol );
        PortfolioLastStockDTO portfolioStockDTO = this.portfolioStockService
                                                  .getPortfolioStock( customerId, portfolioId, tickerSymbol );
        logMethodEnd( methodName, portfolioStockDTO );
        return portfolioStockDTO;
    }

    /**
     * Delete a single customer portfolio stock entry
     *
     * @param customerId
     * @param portfolioId
     * @param tickerSymbol
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = "/customer/{customerId}/portfolio/{portfolioId}/stock/{tickerSymbol}",
        method = RequestMethod.DELETE,
        produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<Void> deletePortfolioStock( @PathVariable int customerId,
                                                      @PathVariable int portfolioId,
                                                      @PathVariable String tickerSymbol )
        throws PortfolioStockNotFound
    {
        final String methodName = "deletePortfolioStock";
        logMethodBegin( methodName, customerId, portfolioId, tickerSymbol );
        PortfolioLastStockDTO portfolioStockDTO = PortfolioLastStockDTO.newInstance();
        portfolioStockDTO.setTickerSymbol( tickerSymbol );
        portfolioStockDTO.setCustomerId( customerId );
        portfolioStockDTO.setPortfolioId( portfolioId );
        this.portfolioStockService.deletePortfolioStock( portfolioStockDTO );
        logMethodEnd( methodName );
        return new ResponseEntity<>( HttpStatus.OK );
    }

    /**
     * Delete a single customer portfolio stock entry
     *
     * @param portfolioStockId
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = "/portfolioStock/{portfolioStockId}",
        method = RequestMethod.DELETE,
        produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<Void> deletePortfolioStock( @PathVariable int portfolioStockId )
        throws PortfolioStockNotFound
    {
        final String methodName = "deletePortfolioStock";
        logMethodBegin( methodName, portfolioStockId );
        this.portfolioStockService.deletePortfolioStock( portfolioStockId );
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
    @RequestMapping( value = "/customer/{customerId}/portfolio/{portfolioId}/stocks",
        method = RequestMethod.GET,
        produces = {MediaType.APPLICATION_JSON_VALUE} )
    public List<PortfolioLastStockDTO> getPortfolioStocks( @PathVariable int customerId,
                                                           @PathVariable int portfolioId )
        throws PortfolioStockNotFound
    {
        final String methodName = "getPortfolioStocks";
        logMethodBegin( methodName, customerId, portfolioId );
        List<PortfolioLastStockDTO> portfolioStockDTOList = this.portfolioStockService.getPortfolioStocks( customerId, portfolioId );
        logMethodEnd( methodName, String.format( "Found %d stocks", portfolioStockDTOList.size() ) );
        return portfolioStockDTOList;
    }

    /**
     * Add a portfolio stock to the database
     *
     * @return The customer stock that was added
     */
    @CrossOrigin
    @RequestMapping( value = "/portfolioStock",
                     method = RequestMethod.POST )
    public ResponseEntity<PortfolioLastStockDTO> addPortfolioStock( @RequestBody PortfolioLastStockDTO portfolioStockDTO )
    {
        final String methodName = "addPortfolioStock";
        logMethodBegin( methodName, portfolioStockDTO );
        checkRequiredFields( portfolioStockDTO );
        /*
         * Do the work
         */
        PortfolioLastStockDTO newPortfolioStockDTO = this.addPortfolioStockHandler.handleRequest( portfolioStockDTO );
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
    @RequestMapping( value = "/portfolioStock",
        method = RequestMethod.PUT )
    public ResponseEntity<PortfolioLastStockDTO> savePortfolioStock( @RequestBody PortfolioLastStockDTO portfolioStockDTO )
    {
        final String methodName = "savePortfolioStock";
        logMethodBegin( methodName, portfolioStockDTO );
        checkRequiredFields( portfolioStockDTO );
        /*
         * Save the stock
         */
        PortfolioLastStockDTO returnPortfolioStockDTO = this.savePortfolioStockHandler.handleRequest( portfolioStockDTO );
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
    private void checkRequiredFields( final @RequestBody PortfolioLastStockDTO portfolioStockDTO )
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
    public void setPortfolioStockService( final PortfolioStockService portfolioStockService )
    {
        this.portfolioStockService = portfolioStockService;
    }
}
