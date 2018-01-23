package com.stocktracker.weblayer.dto.tradeit;

import com.stocktracker.servicelayer.tradeit.apiresults.AuthenticateAPIResult;
import com.stocktracker.weblayer.dto.LinkedAccountDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is the DTO returned to the client after a call to authenticate a user.  It contains the Authenticate API
 * Result that was received from TradeIt and the UUID to be used if the user needs to answer a security question.
 *
 * NOTE: this does not inherit from {@code AuthenticateAPIResult} because we want to return the {@code LinkedAccountEntity}
 * instances not {@code TradeItAccount} instances that were returned from the TradeIt authentication call.
 */
public class AuthenticateDTO extends AuthenticateAPIResult
{
    private List<LinkedAccountDTO> linkedAccounts;

    /**
     * Creates a new instance and copies values from {@code authenticateAPIResult} as this class inherits from that class.
     * Also sets the {@code uuid} if the results status is "INFORMATION_NEEDED".
     * @param authenticateAPIResult
     */
    public AuthenticateDTO( final AuthenticateAPIResult authenticateAPIResult )
    {
        super( authenticateAPIResult ) ;
        this.linkedAccounts = new ArrayList<>();
    }

    /**
     * Set the linked accounts.
     * @param linkedAccountDTOs
     */
    public void setLinkedAccounts( final List<LinkedAccountDTO> linkedAccountDTOs )
    {
        this.linkedAccounts = linkedAccountDTOs;
    }

    /**
     * Get the linked accounts.
     * @return
     */
    public List<LinkedAccountDTO> getLinkedAccounts()
    {
        return linkedAccounts;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "AuthenticateDTO{" );
        sb.append( "linkedAccounts=" ).append( linkedAccounts );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
