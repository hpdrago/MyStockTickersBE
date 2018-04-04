package com.stocktracker.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is thrown when required information is missing in a PortfolioStockQuoteDTO
 *
 * Created by mike on 12/03/2016.
 */
@ResponseStatus(value= HttpStatus.UNPROCESSABLE_ENTITY)  // 422
public class PortfolioStockMissingDataException extends RuntimeException
{
    public PortfolioStockMissingDataException( final Exception e )
    {
        super( e );
    }
}
