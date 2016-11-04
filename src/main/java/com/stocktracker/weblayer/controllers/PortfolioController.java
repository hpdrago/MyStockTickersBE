package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.db.entity.PortfolioEntity;
import com.stocktracker.servicelayer.entity.PortfolioDomainEntity;
import com.stocktracker.servicelayer.service.PortfolioService;
import com.stocktracker.weblayer.dto.CustomerStockDTO;
import com.stocktracker.weblayer.dto.PortfolioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
     * Get a list of portfolios for a single customer {@code customerId}
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

    /**
     * Get a list of stocks in a customer's portoflio
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = "/portfolio/{portfolioId}",
        method = RequestMethod.GET,
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<CustomerStockDTO> getPortfolioStocks( @PathVariable int portfolioId )
    {
        final String methodName = "getPortfolioStocks";
        logMethodBegin( methodName, portfolioId );
        List<CustomerStockDTO> customerStockDTOs = portfolioService.getPortfolioStocks( portfolioId );
        logMethodEnd( methodName, customerStockDTOs );
        return customerStockDTOs;
    }

    /**
     * Get a list of stocks in a customer's portoflio
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = "/customer/{customerId}/portfolio",
        method = RequestMethod.POST )
    public ResponseEntity<Void> addPortfolio( @PathVariable int customerId, @RequestBody PortfolioDTO portfolioDto )
    {
        final String methodName = "addPortfolio";
        logMethodBegin( methodName, customerId, portfolioDto );
        PortfolioEntity portfolioEntity = portfolioService.addPortfolio( customerId, portfolioDto );
        PortfolioDomainEntity portfolioDomainEntity = PortfolioDomainEntity.newInstance( portfolioEntity );
        PortfolioDTO portfolioDTO = PortfolioDTO.newInstance( portfolioDomainEntity );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                    .fromCurrentRequest().path("/{id}")
                                    .buildAndExpand( portfolioDomainEntity ).toUri());
        logMethodEnd( methodName, portfolioDTO );
        return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
    }
}
