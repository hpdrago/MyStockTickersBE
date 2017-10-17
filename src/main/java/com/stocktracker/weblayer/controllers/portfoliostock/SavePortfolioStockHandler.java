package com.stocktracker.weblayer.controllers.portfoliostock;

import com.stocktracker.common.exceptions.PortfolioStockNotFound;
import com.stocktracker.servicelayer.service.PortfolioStockService;
import com.stocktracker.weblayer.controllers.AbstractHandler;
import com.stocktracker.weblayer.dto.PortfolioStockDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class handles the saving of {@code PortfolioStock}
 * Created by mike on 12/3/2016.
 */
@Component
public class SavePortfolioStockHandler extends AbstractHandler<PortfolioStockDTO, PortfolioStockDTO>
{
    private PortfolioStockService portfolioStockService;

    /**
     * Handle the request.  Update the stock in the database and return the updated {@code PortfolioStockDTO}
     * @param portfolioStockDTO
     * @return
     */
    @Override
    public PortfolioStockDTO handleRequest( final PortfolioStockDTO portfolioStockDTO )
    {
        final String methodName = "handleRequest";
        if ( !portfolioStockService.isStockExists( portfolioStockDTO.getCustomerId(),
                                                   portfolioStockDTO.getPortfolioId(),
                                                   portfolioStockDTO.getTickerSymbol() ) )
        {
            logError( methodName, "Duplicate customer stock: " + portfolioStockDTO.getTickerSymbol() );
            throw new PortfolioStockNotFound( portfolioStockDTO.getCustomerId(),
                                              portfolioStockDTO.getPortfolioId(),
                                              portfolioStockDTO.getTickerSymbol() );
        }
        logDebug( methodName, "call addPorfolioStockDTO: {0}", portfolioStockDTO );
        PortfolioStockDTO newPortfolioStockDTO = portfolioStockService.addPortfolioStock( portfolioStockDTO );
        logDebug( methodName, "return addPorfolioStockDTO: {0}", portfolioStockDTO );
        return newPortfolioStockDTO;
    }

    @Autowired
    public void setPortfolioStockService( final PortfolioStockService portfolioStockService )
    {
        this.portfolioStockService = portfolioStockService;
    }
}
