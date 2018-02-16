package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by mike on 12/3/2016.
 *
 * Reference: https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
 */
@ControllerAdvice
public class ControllerExceptionHandler implements MyLogger
{
    public static final String DEFAULT_ERROR_VIEW = "error";

    /*
    @ResponseStatus( HttpStatus.PARTIAL_CONTENT )  // 409
    @ExceptionHandler(PortfolioStockMissingDataException.class)
    public void handlePortfolioStockMissingData()
    {
        // Nothing to do
    }
    */

    /**
     * This handler is called if there no other handlers catch the exception.
     *
     * @param req
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler( HttpServletRequest req, Exception e)
        throws Exception
    {
        logError( "defaultErrorHandler", e );
        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handleRequest it - like the OrderNotFoundException example
        // at the start of this post.
        // AnnotationUtils is a Spring Framework utility class.
        if ( AnnotationUtils.findAnnotation
            (e.getClass(), ResponseStatus.class) != null)
            throw e;
        else
        {
            throw e;
        }

        // Otherwise setup and send the user to a default error-view.
        //ModelAndView mav = new ModelAndView();
        //mav.addObject("exception", e);
        //mav.addObject("url", req.getRequestURL());
        //mav.setViewName(DEFAULT_ERROR_VIEW);
        //return mav;
    }
}
