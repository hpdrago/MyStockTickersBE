package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.weblayer.dto.PortfolioDTO;
import com.stocktracker.weblayer.dto.PortfolioLastStockDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * This class provides the necessary methods to perform the many calculations for a portfolio
 */
@Service
public class PortfolioCalculator implements MyLogger
{
    private PortfolioStockService portfolioStockService;

    /**
     * Perform the portfolio calculations
     * @param portfolios
     */
    public void calculate( final List<PortfolioDTO> portfolios )
    {
        final String methodName = "calculate";
        logMethodBegin( methodName );
        Objects.requireNonNull( portfolios );
        if ( portfolios.size() > 0 )
        {
            logDebug( methodName, "Customer: {0}", portfolios.get( 0 ).getCustomerId() );
            portfolios.forEach( portfolioDTO -> this.calculate( portfolioDTO ) );
        }
        logMethodBegin( methodName );
    }

    /**
     * Calculates a single Portfolio's values
     * @param portfolioDTO
     */
    public void calculate( final PortfolioDTO portfolioDTO )
    {
        List<PortfolioLastStockDTO> portfolioStocks = this.portfolioStockService.getPortfolioStocks( portfolioDTO.getId() );
        int portfolioRealizedGL = 0;
        int portfolioUnRealizedGL = 0;
        int marketValue = 0;
        for ( PortfolioLastStockDTO portfolioStockDTO: portfolioStocks )
        {
            int stockRealizedGL = portfolioStockDTO.getRealizedGains().intValue() -
                                  portfolioStockDTO.getRealizedLosses().intValue();
            portfolioRealizedGL += stockRealizedGL;

            int stockUnRealizedGL = portfolioStockDTO.getMarketValue() - portfolioStockDTO.getCostBasis();
            portfolioUnRealizedGL += stockUnRealizedGL;

            marketValue += portfolioStockDTO.getMarketValue();
        }
        portfolioDTO.setRealizedGL( portfolioRealizedGL );
        portfolioDTO.setUnrealizedGL( portfolioUnRealizedGL );
        portfolioDTO.setMarketValue( marketValue );
    }

    @Autowired
    public void setPortfolioStockService( PortfolioStockService portfolioStockService )
    {
        this.portfolioStockService = portfolioStockService;
    }
}
