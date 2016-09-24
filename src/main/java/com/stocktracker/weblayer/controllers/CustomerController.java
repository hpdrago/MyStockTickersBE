package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import com.stocktracker.servicelayer.service.CustomerService;
import com.stocktracker.weblayer.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * This class contains all of the CustomerDomainEntity related REST weblayer service call mapping and handling
 * <p>
 * Created by mike on 5/9/2016.
 */
@RestController
public class CustomerController implements MyLogger
{
    /**
     * Autowired service
     */
    private CustomerService customerService;

    /**
     * Allow DI to set the CustomerHandler
     *
     * @param customerService
     */
    @Autowired
    public void setCustomerService( final CustomerService customerService )
    {
        final String methodName = "setCustomerHandler";
        logMethodBegin( methodName, customerService );
        this.customerService = customerService;
    }

    /**
     * Get all of the customers
     *
     * @return
     */
    @RequestMapping( value = "/customers",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    @ExceptionHandler( Exception.class )
    public List<CustomerDTO> getCustomers()
    {
        final String methodName = "getCustomers";
        logMethodBegin( methodName );
        List<CustomerDTO> getAllCustomersResponse = customerService.getAllCustomers();
        logMethodEnd( methodName, getAllCustomersResponse );
        return getAllCustomersResponse;
    }

    /**
     * Get the customer by the customer id
     *
     * @param id
     * @return
     */
    @RequestMapping( value = "/customer/{id}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    @ExceptionHandler( Exception.class )
    public CustomerDTO getCustomer( @PathVariable int id )
    {
        final String methodName = "getCustomer";
        logMethodBegin( methodName, id );
        CustomerDTO getCustomerResponse = customerService.getCustomerById( id );
        logMethodEnd( methodName, getCustomerResponse );
        return getCustomerResponse;
    }

    /**
     * Get the customer by email
     *
     * @param email
     * @return
     */                                   // Added :.+ so that the extension is not truncated
    @RequestMapping( value = "/customer/email/{email:.+}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    @ExceptionHandler( Exception.class )
    public CustomerDTO getCustomer( @PathVariable String email )
    {
        final String methodName = "getCustomer";
        logMethodBegin( methodName, email );
        CustomerDTO customerDTO = customerService.getCustomerByEmail( email );
        logMethodEnd( methodName, customerDTO );
        return customerDTO;
    }

    /**
     * Handle any exceptions
     *
     * @param req
     * @param exception
     * @return
     */
    // Total control - setup a model and return the view name yourself. Or consider
    // subclassing ExceptionHandlerExceptionResolver (see below).
    @ExceptionHandler( Exception.class )
    public ModelAndView handleError( HttpServletRequest req, Exception exception )
    {
        logError( "handleError", "Request: " + req.getRequestURL() + " raised " + exception, exception );

        ModelAndView mav = new ModelAndView();
        mav.addObject( "exception", exception );
        mav.addObject( "url", req.getRequestURL() );
        mav.setViewName( "error" );
        return mav;
    }

}
