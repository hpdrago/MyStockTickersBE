package com.stocktracker.weblayer.controllers.portfoliostock;

import com.stocktracker.common.exceptions.DuplicatePortfolioStockException;
import com.stocktracker.servicelayer.entity.PortfolioStockDE;
import com.stocktracker.weblayer.controllers.AbstractHandler;
import com.stocktracker.weblayer.dto.PortfolioStockDTO;
import org.springframework.stereotype.Component;

/**
 * This class handles the REST call to add a Stock to a Portfolio
 * Created by mike on 12/3/2016.
 *
 */
@Component
public class AddPortfolioStockHandler extends AbstractHandler<PortfolioStockDTO, PortfolioStockDTO>
{
    /**
     * Adds a Stock to a Portfolio.
     * @param portfolioStockDTO
     * @return The Stock that was added, the Portfolio that the stock was added to, and the list of Stocks for the
     *         portfolio in an instance of {@code AddPortfolioStockDTO}
     */
    public PortfolioStockDTO handleRequest( final PortfolioStockDTO portfolioStockDTO )
    {
        final String methodName = "handleRequest";
        logMethodBegin( methodName, portfolioStockDTO );
        checkForDuplicate( portfolioStockDTO );
        PortfolioStockDE portfolioStockDE = PortfolioStockDE.newInstance( portfolioStockDTO );
        logDebug( methodName, "call addPortfolioStockDE: {0}", portfolioStockDE );
        /*
         * Add to the database
         */
        portfolioStockDE = this.portfolioStockService.addPortfolioStock( portfolioStockDE );
        logDebug( methodName, "return addPortfolioStockDE: {0}", portfolioStockDE );
        /*
         * Gather the rest of the return data
         */
        //PortfolioDE portfolioDE = this.portfolioService.getPortfolioById( portfolioStockDTO.getPortfolioId() );
        //List<PortfolioStockDE> portfolioStockDEList = this.portfolioService.getPortfolioStocks( portfolioStockDTO.getPortfolioId() );
        /*
         * Convert the DE's to DTO's
         */
        PortfolioStockDTO newPortfolioStockDTO = PortfolioStockDTO.newInstance( portfolioStockDE );
        //PortfolioDTO portfolioDTO = PortfolioDTO.newInstance( portfolioDE );
        //List<PortfolioStockDTO> portfolioStockDTOList = this.listCopyPortfolioStockDEToPortfolioStockDTO.copy( portfolioStockDEList );
        /*
         * Create the return DTO
         */
        /*
        AddPortfolioStockDTO addPortfolioStockDTO = AddPortfolioStockDTO.newInstance( newPortfolioStockDTO,
                                                                                      portfolioDTO,
                                                                                      portfolioStockDTOList );
        */
        logMethodEnd( methodName );
        return portfolioStockDTO;
    }

    /**
     * Check to see if the request will create a duplicate
     * @param portfolioStockDTO
     * @throws DuplicatePortfolioStockException if the stock is already in the portfolio
     */
    private void checkForDuplicate( final PortfolioStockDTO portfolioStockDTO )
    {

        if ( portfolioStockService.isStockExists( portfolioStockDTO.getCustomerId(),
                                                  portfolioStockDTO.getPortfolioId(),
                                                  portfolioStockDTO.getTickerSymbol() ))
        {
            logError( "checkForDuplicate", "Duplicate portfolio stock: " + portfolioStockDTO.getTickerSymbol() );
            throw new DuplicatePortfolioStockException( portfolioStockDTO.getCustomerId(),
                                                        portfolioStockDTO.getPortfolioId(),
                                                        portfolioStockDTO.getTickerSymbol() );
        }
    }
}
