package com.stocktracker.weblayer.dto.tradeit;

import com.stocktracker.servicelayer.tradeit.apiresults.AuthenticateAPIResult;

import java.util.UUID;

/**
 * This class is the DTO returned to the client after a call to authenticate a user.  It contains the Authenticate API
 * Result that was received from TradeIt and the UUID to be used if the user needs to answer a security question.
 */
public class AuthenticateDTO extends AuthenticateAPIResult
{
    /**
     * Creates a new instance and copies values from {@code authenticateAPIResult} as this class inherits from that class.
     * Also sets the {@code uuid} if the results status is "INFORMATION_NEEDED".
     * @param authenticateAPIResult
     */
    public AuthenticateDTO( final AuthenticateAPIResult authenticateAPIResult )
    {
        super( authenticateAPIResult ) ;
    }
}
