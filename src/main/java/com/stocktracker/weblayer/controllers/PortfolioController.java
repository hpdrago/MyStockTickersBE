package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import com.stocktracker.servicelayer.service.PortfolioService;
import com.stocktracker.weblayer.dto.PortfolioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by mike on 9/11/2016.
 */
@RestController
public class PortfolioController implements MyLogger
{
    /**
     * Autowired service class
     */
    private PortfolioService portfolioService;

    /**
     * Allow DI to set the CustomerHandler
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
     * Get the portfolio for the {@code portfolioId}
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = "/portfolios/{portfolioId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public PortfolioDTO getPortfolio( @PathVariable int portfolioId )
    {
        final String methodName = "getPortfolios";
        logMethodBegin( methodName );
        PortfolioDTO portfolioDTO = portfolioService.getPortfolioById( portfolioId );
        logMethodEnd( methodName, portfolioDTO );
        return portfolioDTO;
    }

    /**
     * Get a single portfolio for {@code tickerSymbol}
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = "/portfolios/customer/{customerId}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<PortfolioDTO> getPortfoliosByCustomerId( @PathVariable int customerId )
    {
        final String methodName = "getPortfoliosByCustomerId";
        logMethodBegin( methodName, customerId );
        List<PortfolioDTO> portfolioDTOs = portfolioService.getPortfoliosByCustomerId( customerId );
        logMethodEnd( methodName, portfolioDTOs );
        return portfolioDTOs;
    }

}
