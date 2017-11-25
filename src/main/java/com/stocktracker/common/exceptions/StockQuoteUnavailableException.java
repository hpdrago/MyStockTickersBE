package com.stocktracker.common.exceptions;

/**
 * This exception is thrown when a stock quote can be obtained from Yahoo or IEXTrading.
 */
public class StockQuoteUnavailableException extends Exception
{
    public StockQuoteUnavailableException( final String message )
    {
        super( message );
    }

    public StockQuoteUnavailableException( final Exception e )
    {
        super( e );
    }
}
