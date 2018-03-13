package com.stocktracker.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by mike on 11/25/2016.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND)  // 404
public class PortfolioStockNotFound extends RuntimeException
{
    public PortfolioStockNotFound( final int customerId, final int portfolioId, final String tickerSymbol )
    {
        super( String.format( "Portfolio stock not found for customer id: %d portfolio id: %d ticker symbol: %s",
                              customerId, portfolioId, tickerSymbol ));
    }

    public PortfolioStockNotFound( final Integer portfolioStockId )
    {
        super( String.format( "Could not delete portfolio stock because it does not exists.  id: %d", portfolioStockId  ));
    }
}
