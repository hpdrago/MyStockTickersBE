package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import com.stocktracker.servicelayer.service.CustomerService;
import com.stocktracker.servicelayer.service.PortfolioService;
import com.stocktracker.servicelayer.service.PortfolioStockService;
import com.stocktracker.servicelayer.service.StockNoteService;
import com.stocktracker.servicelayer.service.StockService;
import com.stocktracker.servicelayer.service.listcopy.ListCopyCustomerDEToCustomerDTO;
import com.stocktracker.servicelayer.service.listcopy.ListCopyCustomerStockDEToCustomerStockDTO;
import com.stocktracker.servicelayer.service.listcopy.ListCopyPortfolioDEToPortfolioDTO;
import com.stocktracker.servicelayer.service.listcopy.ListCopyPortfolioStockDEToPortfolioStockDTO;
import com.stocktracker.servicelayer.service.listcopy.ListCopyStockDEToStockDTO;
import com.stocktracker.servicelayer.service.listcopy.ListCopyStockNoteDEToStockNoteDTO;
import com.stocktracker.servicelayer.service.listcopy.ListCopyStockNoteCountDEToStockNoteCountDTO;
import com.stocktracker.servicelayer.service.listcopy.ListCopyStockNoteSourceDEToStockNoteSourceDTO;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by mike on 12/3/2016.
 */
public class AbstractAutowiredBean implements MyLogger
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
    protected PortfolioStockService portfolioStockService;
    /**
     * Autowired service class
     */
    protected StockNoteService stockNoteService;


    protected ListCopyCustomerStockDEToCustomerStockDTO listCopyCustomerStockDEToCustomerStockDTO;
    protected ListCopyCustomerDEToCustomerDTO listCopyCustomerDEToCustomerDTO;
    protected ListCopyPortfolioDEToPortfolioDTO listCopyPortfolioDEToPortfolioDTO;
    protected ListCopyStockDEToStockDTO listCopyStockDEToStockDTO;
    protected ListCopyPortfolioStockDEToPortfolioStockDTO listCopyPortfolioStockDEToPortfolioStockDTO;
    protected ListCopyStockNoteDEToStockNoteDTO listCopyStockNoteDEToStockNoteDTO;
    protected ListCopyStockNoteSourceDEToStockNoteSourceDTO listCopyStockNoteSourceDEToStockNoteSourceDTO;
    protected ListCopyStockNoteCountDEToStockNoteCountDTO listCopyStockNoteCountDEToStockNoteCountDTO;

    @Autowired
    public void setListCopyStockNoteDEToStockNoteDTO( final ListCopyStockNoteDEToStockNoteDTO listCopyStockNoteDEToStockNoteDTO )
    {
        logDebug( "setListCopyStockNoteDEToStockNoteDTO", "Dependency Injection of: " +
                                                          listCopyStockNoteDEToStockNoteDTO );
        this.listCopyStockNoteDEToStockNoteDTO = listCopyStockNoteDEToStockNoteDTO;
    }

    @Autowired
    public void setListCopyStockNoteSourceDEToStockNoteSourceDTO( final ListCopyStockNoteSourceDEToStockNoteSourceDTO
                                                                        listCopyStockNoteSourceDEToStockNoteSourceDTO )
    {
        this.listCopyStockNoteSourceDEToStockNoteSourceDTO = listCopyStockNoteSourceDEToStockNoteSourceDTO;
    }

    @Autowired
    public void setListCopyCustomerDEToCustomerDTO( final ListCopyCustomerDEToCustomerDTO listCopyCustomerDEToCustomerDTO )
    {
        this.listCopyCustomerDEToCustomerDTO = listCopyCustomerDEToCustomerDTO;
    }

    @Autowired
    public void setListCopyPortfolioStockDEToPortfolioStockDTO( final ListCopyPortfolioStockDEToPortfolioStockDTO listCopyPortfolioStockDEToPortfolioStockDTO )
    {
        this.listCopyPortfolioStockDEToPortfolioStockDTO = listCopyPortfolioStockDEToPortfolioStockDTO;
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
     * Allow DI to set the Stock Notes Service
     *
     * @param stockNoteService
     */
    @Autowired
    public void setStockNoteService( final StockNoteService stockNoteService )
    {
        final String methodName = "setStockNotesService";
        logMethodBegin( methodName, stockNoteService );
        this.stockNoteService = stockNoteService;
    }
}
