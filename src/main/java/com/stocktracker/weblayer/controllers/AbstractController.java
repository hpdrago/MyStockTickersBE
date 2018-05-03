package com.stocktracker.weblayer.controllers;

import com.fasterxml.uuid.impl.UUIDUtil;
import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.CustomerNotFoundException;
import com.stocktracker.common.exceptions.NotAuthorizedException;
import com.stocktracker.servicelayer.service.CustomerEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.UUID;

/**
 * Created by mike on 11/3/2016.
 */
public class AbstractController implements MyLogger
{
    @Autowired
    protected ApplicationContext context;
    protected CustomerEntityService customerEntityService;

    // Total control - setup a model and return the view name yourself. Or consider
    // subclassing ExceptionHandlerExceptionResolver (see below).
    /*
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
    */

    /**
     * Validates that the customer id is not null and that it is a valid customer UUID.
     * @param customerId
     * @return
     * @throws CustomerNotFoundException If the customer id is not a valid customer UUID.
     * @throws NotAuthorizedException if the customer id is null.
     */
    protected UUID validateCustomerId( final String customerId )
        throws CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "validateCustomerId";
        if ( customerId == null )
        {
            logWarn( methodName, "customer id is null" );
            throw new NotAuthorizedException( "Customer id is null" );
        }
        UUID customerUuid;
        try
        {
            customerUuid = this.getCustomerUuid( customerId );
        }
        catch( NumberFormatException e )
        {
            throw new NotAuthorizedException( "Invalid customer id", e );
        }
        return customerUuid;
    }

    /**
     * Determines if the customerId is a valid customer UUID.
     * @param customerId
     * @return Customer UUID
     * @throws CustomerNotFoundException
     */
    protected UUID getCustomerUuid( final String customerId )
        throws CustomerNotFoundException
    {
        return this.customerEntityService
                   .getCustomerEntity( UUIDUtil.uuid( customerId ) )
                   .getUuid();
    }

    @Autowired
    public void setCustomerEntityService( final CustomerEntityService customerEntityService )
    {
        this.customerEntityService = customerEntityService;
    }

}
