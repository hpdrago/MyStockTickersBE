package com.stocktracker.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

/**
 * Created by mike on 11/25/2016.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND)  // 404
public class PortfolioStockNotFound extends RuntimeException
{
    public PortfolioStockNotFound( final UUID customerUuid, final UUID portfolioUuid, final String tickerSymbol )
    {
        super( String.format( "Portfolio stock not found for customer id: %s portfolio id: %s ticker symbol: %s",
                              customerUuid, portfolioUuid, tickerSymbol ));
    }

    public PortfolioStockNotFound( final String portfolioStockUuid )
    {
        super( String.format( "Could not delete portfolio stock because it does not exists.  id: %s", portfolioStockUuid  ));
    }

    public PortfolioStockNotFound( final String customerUuid, final String portfolioUuid, final String tickerSymbol )
    {
        super( String.format( "Portfolio stock not found for customer id: %s portfolio id: %s ticker symbol: %s",
                              customerUuid, portfolioUuid, tickerSymbol ));
    }
}
