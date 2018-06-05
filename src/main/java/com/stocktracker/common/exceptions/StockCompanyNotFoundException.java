package com.stocktracker.common.exceptions;

import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDataNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is thrown when the a stock quote cannot be found for a ticker symbol.
 * Created by mike on 11/21/2017.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND)  // 404
public class StockCompanyNotFoundException extends AsyncCacheDataNotFoundException
{
    public StockCompanyNotFoundException( final String tickerSymbol )
    {
        super( "Stock company not found for ticker symbol '" + tickerSymbol + "'" );
    }

    public StockCompanyNotFoundException( final String tickerSymbol, final Exception e )
    {
        super( "Stock company not found for ticker symbol '" + tickerSymbol + "'", e );
    }
}
