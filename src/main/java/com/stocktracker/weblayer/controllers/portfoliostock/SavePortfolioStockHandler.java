package com.stocktracker.weblayer.controllers.portfoliostock;

import com.stocktracker.common.exceptions.PortfolioStockNotFound;
import com.stocktracker.servicelayer.entity.PortfolioStockDE;
import com.stocktracker.weblayer.controllers.AbstractHandler;
import com.stocktracker.weblayer.dto.PortfolioStockDTO;
import org.springframework.stereotype.Component;

/**
 * This class handles the saving of {@code PortfolioStock}
 * Created by mike on 12/3/2016.
 */
@Component
public class SavePortfolioStockHandler extends AbstractHandler<PortfolioStockDTO, PortfolioStockDTO>
{
    /**
     * Handle the request.  Update the stock in the database and return the updated {@code PortfolioStockDTO}
     * @param portfolioStockDTO
     * @return
     */
    @Override
    public PortfolioStockDTO handleRequest( final PortfolioStockDTO portfolioStockDTO )
    {
        final String methodName = "handleRequest";
        if ( portfolioStockService.isStockExists( portfolioStockDTO.getCustomerId(),
                                                  portfolioStockDTO.getPortfolioId(),
                                                  portfolioStockDTO.getTickerSymbol() ) )
        {
            logError( methodName, "Duplicate customer stock: " + portfolioStockDTO.getTickerSymbol() );
            throw new PortfolioStockNotFound( portfolioStockDTO.getCustomerId(),
                                              portfolioStockDTO.getPortfolioId(),
                                              portfolioStockDTO.getTickerSymbol() );
        }
        PortfolioStockDE portfolioStockDE = PortfolioStockDE.newInstance( portfolioStockDTO );
        logDebug( methodName, "call addPortfolioStockDE: {0}", portfolioStockDE );
        portfolioStockDE = portfolioStockService.addPortfolioStock( portfolioStockDE );
        logDebug( methodName, "return addPortfolioStockDE: {0}", portfolioStockDE );
        PortfolioStockDTO returnPortfolioStockDTO = PortfolioStockDTO.newInstance( portfolioStockDE );
        return returnPortfolioStockDTO;
    }
}
