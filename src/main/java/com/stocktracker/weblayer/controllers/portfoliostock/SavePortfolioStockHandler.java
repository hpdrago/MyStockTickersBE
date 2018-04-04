package com.stocktracker.weblayer.controllers.portfoliostock;

import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.PortfolioStockNotFound;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockQuoteUnavailableException;
import com.stocktracker.servicelayer.service.PortfolioStockEntityService;
import com.stocktracker.weblayer.controllers.AbstractHandler;
import com.stocktracker.weblayer.dto.PortfolioStockQuoteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class handles the saving of {@code PortfolioStock}
 * Created by mike on 12/3/2016.
 */
@Component
public class SavePortfolioStockHandler extends AbstractHandler<PortfolioStockQuoteDTO, PortfolioStockQuoteDTO>
{
    private PortfolioStockEntityService portfolioStockService;

    /**
     * Handle the request.  Update the stock in the database and return the updated {@code PortfolioLastStockDTO}
     * @param portfolioStockDTO
     * @return
     */
    @Override
    public PortfolioStockQuoteDTO handleRequest( final PortfolioStockQuoteDTO portfolioStockDTO )
        throws EntityVersionMismatchException
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
        PortfolioStockQuoteDTO newPortfolioStockDTO = null;
        try
        {
            newPortfolioStockDTO = portfolioStockService.addPortfolioStock( portfolioStockDTO );
        }
        catch ( StockNotFoundException e )
        {
            logError( methodName, e );
        }
        catch ( StockQuoteUnavailableException e )
        {
            logError( methodName, e );
        }
        logDebug( methodName, "return addPorfolioStockDTO: {0}", portfolioStockDTO );
        return newPortfolioStockDTO;
    }

    @Autowired
    public void setPortfolioStockService( final PortfolioStockEntityService portfolioStockService )
    {
        this.portfolioStockService = portfolioStockService;
    }
}
