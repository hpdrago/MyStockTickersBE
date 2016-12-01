package com.stocktracker.weblayer.controllers;

/**
 * Created by mike on 11/25/2016.
 */

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.exceptions.CustomerStockNotFound;
import com.stocktracker.repositorylayer.exceptions.DuplicateCustomerStockException;
import com.stocktracker.servicelayer.entity.CustomerStockDE;
import com.stocktracker.weblayer.dto.CustomerStockDTO;
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
 * This class contains all of the CustomerStockDE related REST weblayer service call mapping and handling
 * <p>
 * Created by mike on 5/9/2016.
 */
@RestController
public class CustomerStockController extends BaseController implements MyLogger
{
    /**
     * Get a single customer stock entry
     *
     * @param customerId
     * @param tickerSymbol
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = "/customer/{customerId}/stock/{tickerSymbol}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public CustomerStockDTO getCustomerStock( @PathVariable int customerId, @PathVariable String tickerSymbol )
        throws CustomerStockNotFound
    {
        final String methodName = "getCustomerStock";
        logMethodBegin( methodName, customerId, tickerSymbol );
        CustomerStockDE customerStockDE = customerStockService.getCustomerStock( customerId, tickerSymbol );
        CustomerStockDTO customerStockDTO = CustomerStockDTO.newInstance( customerStockDE );
        logMethodEnd( methodName, customerStockDTO );
        return customerStockDTO;
    }

    /**
     * Add a customer stock to the database
     *
     * @return The customer stock that was added
     */
    @CrossOrigin
    @RequestMapping( value = "/customer/{customerId}/stock/{tickerSymbol}",
                     method = RequestMethod.POST )
    public ResponseEntity<CustomerStockDTO> addCustomerStock( @RequestBody CustomerStockDTO customerStockDTO )
    {
        final String methodName = "addCustomerStock";
        logMethodBegin( methodName, customerStockDTO );
        if ( customerStockService.isStockExists( customerStockDTO.getCustomerId(), customerStockDTO.getTickerSymbol() ))
        {
            logError( methodName, "Duplicate customer stock: " + customerStockDTO.getTickerSymbol() );
            throw new DuplicateCustomerStockException( customerStockDTO.getCustomerId(), customerStockDTO.getTickerSymbol() );
        }
        /*
         * Two database updates are required.
         * 1. Add the stock to the customer_stock table
         * 2. Associate the stock to the portfolio
         *
         * Add the customer stock
         */
        CustomerStockDE customerStockDE = CustomerStockDE.newInstance( customerStockDTO );
        logDebug( methodName, "call addCustomerStockDE: %s", customerStockDE );
        customerStockDE = customerStockService.addCustomerStock( customerStockDE );
        logDebug( methodName, "return addCustomerStockDE: %s", customerStockDE );
        CustomerStockDTO returnCustomerStockDTO = CustomerStockDTO.newInstance( customerStockDE );

        /*
         * Add the stock to the customer's portfolio
         */
        this.portfolioStockService.addPortfolioStock( customerStockDTO.getCustomerId(), customerStockDTO.getTickerSymbol() );

        /*
         * send the response
         */
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                     .fromCurrentRequest().path( "" )
                                     .buildAndExpand( customerStockDE ).toUri());
        logMethodEnd( methodName, returnCustomerStockDTO );
        return new ResponseEntity<>( customerStockDTO, httpHeaders, HttpStatus.CREATED );
    }

}
