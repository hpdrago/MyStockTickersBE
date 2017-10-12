package com.stocktracker.weblayer.controllers.portfoliostock;

import com.stocktracker.common.exceptions.DuplicatePortfolioStockException;
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
        logDebug( methodName, "call addPortfolioStockDTO: {0}", portfolioStockDTO );
        /*
         * Add to the database
         */
        PortfolioStockDTO newPotfolioStockDTO = this.portfolioStockService.addPortfolioStock( portfolioStockDTO );
        logDebug( methodName, "return addPortfolioStockDTO: {0}", portfolioStockDTO );
        /*
         * Gather the rest of the return data
         */
        //PortfolioDE portfolioDE = this.portfolioService.getPortfolioById( portfolioStockDTO.getPortfolioId() );
        //List<PortfolioStockDTO> portfolioStockDTOList = this.portfolioService.getPortfolioStocks( portfolioStockDTO.getPortfolioId() );
        /*
         * Convert the DE's to DTO's
         */
        //PortfolioDTO portfolioDTO = PortfolioDTO.newInstance( portfolioDE );
        //List<PortfolioStockDTO> portfolioStockDTOList = this.listCopyPortfolioStockDTOToPortfolioStockDTO.copy( portfolioStockDTOList );
        /*
         * Create the return DTO
         */
        /*
        AddPortfolioStockDTO addPortfolioStockDTO = AddPortfolioStockDTO.newInstance( newPortfolioStockDTO,
                                                                                      portfolioDTO,
                                                                                      portfolioStockDTOList );
        */
        logMethodEnd( methodName, newPotfolioStockDTO );
        return newPotfolioStockDTO;
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
