package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.MyLogger;
import com.stocktracker.servicelayer.entity.CustomerStockDE;
import com.stocktracker.servicelayer.entity.PortfolioDE;
import com.stocktracker.weblayer.dto.CustomerStockDTO;
import com.stocktracker.weblayer.dto.PortfolioDTO;
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
public class PortfolioController extends BaseController implements MyLogger
{
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
        List<PortfolioDE> portfolioDEs = portfolioService.getPortfoliosByCustomerId( customerId );
        List<PortfolioDTO> portfolioDTOs = listCopyPortfolioDEToPortfolioDTO.copy( portfolioDEs );
        logMethodEnd( methodName, portfolioDTOs );
        return portfolioDTOs;
    }

    /**
     * Get a list of stocks in a customer's portfolio
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping( value = "/portfolios/{portfolioId}",
        method = RequestMethod.GET,
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<CustomerStockDTO> getPortfolioStocks( @PathVariable int portfolioId )
    {
        final String methodName = "getPortfolioStocks";
        logMethodBegin( methodName, portfolioId );
        List<CustomerStockDE> customerStockDEs = portfolioService.getPortfolioStocks( portfolioId );
        List<CustomerStockDTO> customerStockDTOs = listCopyCustomerStockDEToCustomerStockDTO.copy( customerStockDEs ) ;
        logMethodEnd( methodName, customerStockDTOs );
        return customerStockDTOs;
    }

    /**
     * Get a list of stocks in a customer's portfolio
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
        PortfolioDE portfolioDE = portfolioService.addPortfolio( customerId, portfolioDto );
        PortfolioDTO portfolioDTO = PortfolioDTO.newInstance( portfolioDE );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation( ServletUriComponentsBuilder
                                    .fromCurrentRequest().path("/{id}")
                                    .buildAndExpand( portfolioDE ).toUri());
        logMethodEnd( methodName, portfolioDTO );
        return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
    }

    /**
     * Delete a portfolio by portfolio id
     * @param portfolioId
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/portfolios/{portfolioId}", method = RequestMethod.DELETE)
    public ResponseEntity<PortfolioDTO> deletePortfolio( @PathVariable( "portfolioId" ) int portfolioId )
    {
        final String methodName = "deletePortfolio";
        logMethodBegin( methodName, portfolioId );
        PortfolioDE portfolioDE = portfolioService.getPortfolioById( portfolioId );
        logDebug( methodName, "portfolio: %s", portfolioDE.toString() );
        portfolioDE = portfolioService.deletePortfolio( portfolioId );
        PortfolioDTO portfolioDTO = PortfolioDTO.newInstance( portfolioDE );
        logMethodBegin( methodName, portfolioDTO );
        return new ResponseEntity<>( portfolioDTO, HttpStatus.OK );
    }
}
