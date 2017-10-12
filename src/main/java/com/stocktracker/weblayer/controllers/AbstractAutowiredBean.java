package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import com.stocktracker.servicelayer.service.CustomerService;
import com.stocktracker.servicelayer.service.PortfolioService;
import com.stocktracker.servicelayer.service.PortfolioStockService;
import com.stocktracker.servicelayer.service.StockNoteService;
import com.stocktracker.servicelayer.service.StockNoteSourceService;
import com.stocktracker.servicelayer.service.StockService;
import com.stocktracker.servicelayer.service.YahooStockService;
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
    /**
     * Autowired service class
     */
    protected StockNoteSourceService stockNoteSourceService;
    /**
     * Autowired service class
     */
    protected YahooStockService yahooStockService;

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

    /**
     * Allow DI to set the Stock Notes Source Service
     *
     * @param stockNoteSourceService
     */
    @Autowired
    public void setStockNoteSourceService( final StockNoteSourceService stockNoteSourceService )
    {
        final String methodName = "setStockNotesSourceService";
        logMethodBegin( methodName, stockNoteSourceService );
        this.stockNoteSourceService = stockNoteSourceService;
    }

    @Autowired
    public void setYahooStockService( final YahooStockService yahooStockService )
    {
        logDebug( "setYahooStockService", "DI of " + yahooStockService );
        this.yahooStockService = yahooStockService;
    }
}
