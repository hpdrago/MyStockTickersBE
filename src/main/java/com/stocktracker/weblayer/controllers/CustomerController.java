package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import com.stocktracker.weblayer.dto.CustomerDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * This class contains all of the CustomerDTO related REST weblayer service call mapping and handling
 * <p>
 * Created by mike on 5/9/2016.
 */
@RestController
@CrossOrigin
public class CustomerController extends AbstractController implements MyLogger
{
    /**
     * Get all of the customers
     *
     * @return
     */
    @RequestMapping( value = "/customers",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public List<CustomerDTO> getCustomers()
    {
        final String methodName = "getCustomers";
        logMethodBegin( methodName );
        List<CustomerDTO> customerDTOs = customerService.getAllCustomers();
        logMethodEnd( methodName, customerDTOs );
        return customerDTOs;
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
    public CustomerDTO getCustomer( @PathVariable int id )
    {
        final String methodName = "getCustomer";
        logMethodBegin( methodName, id );
        CustomerDTO customerDTO = customerService.getCustomerById( id );
        logMethodEnd( methodName, customerDTO );
        return customerDTO;
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
    public CustomerDTO getCustomer( @PathVariable String email )
    {
        final String methodName = "getCustomer";
        logMethodBegin( methodName, email );
        CustomerDTO customerDTO = customerService.getCustomerByEmail( email );
        logMethodEnd( methodName, customerDTO );
        return customerDTO;
    }

}
