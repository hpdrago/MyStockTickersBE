package com.stocktracker.common.exceptions;

import com.stocktracker.servicelayer.tradeit.TradeItCodeEnum;
import com.stocktracker.servicelayer.tradeit.apiresults.AuthenticateAPIResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception indicates that a REST API call resulted in an authentication error or page redirection.
 */
@ResponseStatus( value=HttpStatus.UNAUTHORIZED )  // 401
public class TradeItAuthenticationException extends TradeItAPIException
{
    private final AuthenticateAPIResult authenticateAPIResult;

    public TradeItAuthenticationException( final AuthenticateAPIResult authenticateAPIResult )
    {
        super( TradeItCodeEnum.getErrorMessage( authenticateAPIResult.getCode() ));
        this.authenticateAPIResult = authenticateAPIResult;
    }

    public AuthenticateAPIResult getAuthenticateAPIResult()
    {
        return authenticateAPIResult;
    }

}
