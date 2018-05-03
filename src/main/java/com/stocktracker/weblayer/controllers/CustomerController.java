package com.stocktracker.weblayer.controllers;

import com.fasterxml.uuid.impl.UUIDUtil;
import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.CustomerNotFoundException;
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
    private static final String CONTEXT_URL = "/customer";

    /**
     * Get all of the customers
     *
     * @return
     */
    @RequestMapping( value = CONTEXT_URL,
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public List<CustomerDTO> getCustomers()
        throws CustomerNotFoundException
    {
        final String methodName = "getCustomers";
        logMethodBegin( methodName );
        List<CustomerDTO> customerDTOs = customerEntityService.getAllCustomers();
        logMethodEnd( methodName, customerDTOs );
        return customerDTOs;
    }

    /**
     * Get the customer by the customer id
     *
     * @param id
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/id/{id}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public CustomerDTO getCustomer( @PathVariable String id )
        throws CustomerNotFoundException
    {
        final String methodName = "getCustomer";
        logMethodBegin( methodName, id );
        CustomerDTO customerDTO = this.customerEntityService
                                      .getCustomerDTO( UUIDUtil.uuid( id ));
        logMethodEnd( methodName, customerDTO );
        return customerDTO;
    }

    /**
     * Get the customer by email
     *
     * @param email
     * @return
     */                                   // Added :.+ so that the extension is not truncated
    @RequestMapping( value = CONTEXT_URL + "/email/{email:.+}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public CustomerDTO getCustomerByEmail( @PathVariable String email )
        throws CustomerNotFoundException
    {
        final String methodName = "getCustomerByEmail";
        logMethodBegin( methodName, email );
        CustomerDTO customerDTO = customerEntityService.getCustomerByEmail( email );
        logMethodEnd( methodName, customerDTO );
        return customerDTO;
    }

}
