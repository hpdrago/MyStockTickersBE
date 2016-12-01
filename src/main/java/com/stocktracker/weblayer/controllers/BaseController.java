package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import com.stocktracker.servicelayer.service.CustomerService;
import com.stocktracker.servicelayer.service.CustomerStockService;
import com.stocktracker.servicelayer.service.PortfolioService;
import com.stocktracker.servicelayer.service.PortfolioStockService;
import com.stocktracker.servicelayer.service.StockService;
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
    /**
     * Autowired service
     */
    protected CustomerService customerService;
    /**
     * Autowired service class
     */
    protected StockService stockService;
    /**
     * Autowired service class
     */
    protected PortfolioService portfolioService;
    /**
     * Autowired service class
     */
    protected CustomerStockService customerStockService;
    /**
     * Autowired service class
     */
    protected PortfolioStockService portfolioStockService;


    protected ListCopyCustomerStockDEToCustomerStockDTO listCopyCustomerStockDEToCustomerStockDTO;
    protected ListCopyCustomerDEToCustomerDTO listCopyCustomerDEToCustomerDTO;
    protected ListCopyPortfolioDEToPortfolioDTO listCopyPortfolioDEToPortfolioDTO;
    protected ListCopyStockDEToStockDTO listCopyStockDEToStockDTO;

    /**
     * Allow DI to set the Customer Stock Service
     *
     * @param customerStockService
     */
    @Autowired
    public void setCustomerStockService( final CustomerStockService customerStockService )
    {
        final String methodName = "setCustomerStockService";
        logMethodBegin( methodName, customerStockService );
        this.customerStockService = customerStockService;
    }

    /**
     * Allow DI to set the Portfolio Service
     *
     * @param portfolioService
     */
    @Autowired
    public void setPortfolioService( final PortfolioService portfolioService )
    {
        final String methodName = "setPortfolioService";
        logMethodBegin( methodName, portfolioService );
        this.portfolioService = portfolioService;
    }

    /**
     * Allow DI to set the Portfolio Stock Service
     *
     * @param portfolioStockService
     */
    @Autowired
    public void setPortfolioStockService( final PortfolioStockService portfolioStockService )
    {
        final String methodName = "setPortfolioStockService";
        logMethodBegin( methodName, portfolioStockService );
        this.portfolioStockService = portfolioStockService;
    }

    /**
     * Allow DI to set the StockService
     *
     * @param stockService
     */
    @Autowired
    public void setStockService( final StockService stockService )
    {
        final String methodName = "setStockService";
        logMethodBegin( methodName, stockService );
        this.stockService = stockService;
    }

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
