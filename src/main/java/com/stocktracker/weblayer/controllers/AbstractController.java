package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by mike on 11/3/2016.
 */
public class AbstractController implements MyLogger
{
    @Autowired
    protected ApplicationContext context;

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
}
