package com.stocktracker.common.exceptions;

import com.stocktracker.servicelayer.tradeit.apiresults.TradeItAPIResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Common TradeItException that is thrown as a result of TradeIt API Calls.
 */
@ResponseStatus( value= HttpStatus.UNPROCESSABLE_ENTITY )  // 422
public class TradeItAPIException extends Exception
{
    public TradeItAPIException()
    {
    }

    public TradeItAPIException( final String message )
    {
        super( message );
    }

    /**
     * Creates an exception with error message information extracted from the {@code apiResult}.
     * @param apiResult
     */
    public TradeItAPIException( final TradeItAPIResult apiResult )
    {
        super( extractMessage( apiResult ) );
    }

    /**
     * Extracts the error messages from the TradeItAPIResult.
     * @param apiResult
     * @return
     */
    private static String extractMessage( final TradeItAPIResult apiResult )
    {
        StringBuilder message = new StringBuilder();
        message.append( apiResult.getErrorMessage() );
        if ( apiResult.getShortMessage() != null && apiResult.getShortMessage().length() > 0 )
        {
            message.append( "\n" ).append( apiResult.getShortMessage() );
        }
        if ( apiResult.getLongMessages() != null &&
             apiResult.getLongMessages().length > 0 )
        {
            for ( final String longMessage: apiResult.getLongMessages() )
            {
                message.append( "\n" ).append( longMessage );
            }
        }
        return message.toString();
    }
}
