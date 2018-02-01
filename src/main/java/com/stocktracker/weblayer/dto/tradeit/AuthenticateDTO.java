package com.stocktracker.weblayer.dto.tradeit;

import com.stocktracker.servicelayer.tradeit.apiresults.AuthenticateAPIResult;
import com.stocktracker.weblayer.dto.LinkedAccountDTO;
import com.stocktracker.weblayer.dto.TradeItAccountDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is the DTO returned to the client after a call to authenticate a user.  It contains the Authenticate API
 * Result that was received from TradeIt and the UUID to be used if the user needs to answer a security question.
 *
 * NOTE: this does not inherit from {@code AuthenticateAPIResult} because we want to return the {@code LinkedAccountEntity}
 * instances not {@code TradeItAccount} instances that were returned from the TradeIt authentication call.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE)
@Qualifier( "authenticateDTO")
public class AuthenticateDTO extends AuthenticateAPIResult
{
    private TradeItAccountDTO tradeItAccount;
    private List<LinkedAccountDTO> linkedAccounts;

    /**
     * Copy the results from an authenticate result.
     * @param authenticateAPIResult
     */
    public void setResults( final AuthenticateAPIResult authenticateAPIResult )
    {
        super.setResults( authenticateAPIResult );
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

    public TradeItAccountDTO getTradeItAccount()
    {
        return tradeItAccount;
    }

    public void setTradeItAccountDTO( final TradeItAccountDTO tradeItAccount )
    {
        this.tradeItAccount = tradeItAccount;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "AuthenticateDTO{" );
        sb.append( "tradeItAccount=" ).append( tradeItAccount );
        sb.append( ", linkedAccounts=" ).append( linkedAccounts );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
