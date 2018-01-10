package com.stocktracker.weblayer.dto.tradeit;

import com.stocktracker.servicelayer.tradeit.apiresults.AuthenticateAPIResult;
import com.stocktracker.servicelayer.tradeit.apiresults.TradeItAPIResult;

/**
 * This DTO is returned to the client side to contain the result of a login to the user's brokerage account.
 */
public class AuthenticateDTO extends TradeItAPIResult
{
    public AuthenticateDTO( final AuthenticateAPIResult authenticateAPIResult )
    {
        super( authenticateAPIResult );
    }
}
