package com.stocktracker.weblayer.dto.tradeit;

/**
 * This DTO is returned to the client side to contain the result of a login to the user's brokerage account.
 */
public class AuthenticateDTO extends TradeItAPIResult
{
    public AuthenticateDTO( final Authenticate authenticate )
    {
        super( authenticate );
    }
}
