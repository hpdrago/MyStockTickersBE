package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import com.stocktracker.servicelayer.service.listcopy.ListCopyCustomerDEToCustomerDTO;
import com.stocktracker.servicelayer.service.listcopy.ListCopyCustomerStockDEToCustomerStockDTO;
import com.stocktracker.servicelayer.service.listcopy.ListCopyPortfolioDEToPortfolioDTO;
import com.stocktracker.servicelayer.service.listcopy.ListCopyStockDEToStockDTO;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by mike on 11/3/2016.
 */
public class BaseController implements MyLogger
{
    protected ListCopyCustomerStockDEToCustomerStockDTO listCopyCustomerStockDEToCustomerStockDTO;
    protected ListCopyCustomerDEToCustomerDTO listCopyCustomerDEToCustomerDTO;
    protected ListCopyPortfolioDEToPortfolioDTO listCopyPortfolioDEToPortfolioDTO;
    protected ListCopyStockDEToStockDTO listCopyStockDEToStockDTO;

    @Autowired
    public void setListCopyStockDEToStockDTO(
        final ListCopyStockDEToStockDTO listCopyStockDEToStockDTO )
    {
        this.listCopyStockDEToStockDTO = listCopyStockDEToStockDTO;
    }

    /**
     * Dependency injection of the ListCopyCustomerDoToCustomerDo
     * @param
     */
    @Autowired
    public void setListCopyCustomerDoToCustomerDo( final ListCopyCustomerDEToCustomerDTO listCopyCustomerDEToCustomerDTO )
    {
        final String methodName = "setListCopyCustomerDoToCustomerDo";
        logDebug( methodName, "Dependency Injection of: " + listCopyCustomerDEToCustomerDTO );
        this.listCopyCustomerDEToCustomerDTO = listCopyCustomerDEToCustomerDTO;
    }

    /**
     * Dependency injection of the ListCopyPortfolioDoToPortfolioDo
     * @param
     */
    @Autowired
    public void setListCopyPortfolioDoToPortfolioDo( final ListCopyPortfolioDEToPortfolioDTO listCopyPortfolioDEToPortfolioDTO )
    {
        final String methodName = "setListCopyPortfolioDoToPortfolioDo";
        logDebug( methodName, "Dependency Injection of: " + listCopyPortfolioDEToPortfolioDTO );
        this.listCopyPortfolioDEToPortfolioDTO = listCopyPortfolioDEToPortfolioDTO;
    }

    /**
     * Dependency injection of the ListCopyPortfolioDoToPortfolioDo
     * @param
     */
    @Autowired
    public void setListCopyPortfolioDEToPortfolioDTO(
        final ListCopyPortfolioDEToPortfolioDTO listCopyPortfolioDEToPortfolioDTO )
    {
        final String methodName = "setListCopyPortfolioDomainEntityToPortfolioDTO";
        logDebug( methodName, "Dependency Injection of: " + listCopyPortfolioDEToPortfolioDTO );
        this.listCopyPortfolioDEToPortfolioDTO = listCopyPortfolioDEToPortfolioDTO;
    }

    /**
     * Dependency injection of the ListCopyCustomerStockDomainEntityToCustomerStockDTO
     * @param
     */
    @Autowired
    public void setListCopyCustomerStockDEToCustomerStockDTO(
        ListCopyCustomerStockDEToCustomerStockDTO listCopyCustomerStockDEToCustomerStockDTO )
    {
        final String methodName = "setListCopyCustomerStockDomainEntityToCustomerStockDTO";
        logDebug( methodName, "Dependency Injection of: " + listCopyCustomerStockDEToCustomerStockDTO );
        this.listCopyCustomerStockDEToCustomerStockDTO = listCopyCustomerStockDEToCustomerStockDTO;
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
  /*  @ExceptionHandler( Exception.class )
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
