package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;

/**
 * Created by mike on 11/3/2016.
 */
public class AbstractController implements MyLogger
{
    /*
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
    }*/
}
