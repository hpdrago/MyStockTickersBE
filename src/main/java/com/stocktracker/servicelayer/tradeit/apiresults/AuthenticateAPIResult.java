package com.stocktracker.servicelayer.tradeit.apiresults;

import com.stocktracker.servicelayer.tradeit.types.TradeItAccount;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

/**
 * This is the base class for the Authenticate API Result.  Depending on the result status of SUCCESS or
 * INFORMATION_NEDED, different field values will be set.
 *
 * @author mike 1/10/2018
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class AuthenticateAPIResult extends TradeItAPIResult
{
    /*
     * status == SUCCESS
     */
    private TradeItAccount[] accounts;

    /*
     * status = INFORMATION_NEEDED
     */
    private String informationType;
    private String securityQuestion;
    private String securityOptions[];

    /**
     * Default constructor.
     */
    public AuthenticateAPIResult()
    {
    }

    public void setResults( final AuthenticateAPIResult results )
    {
        super.setResults( results );
        this.accounts = results.getAccounts();
        this.informationType = results.getInformationType();
        this.securityOptions = results.getSecurityOptions();
        this.securityQuestion = results.getSecurityQuestion();
    }

    public String getInformationType()
    {
        return informationType;
    }

    public void setInformationType( String informationType )
    {
        this.informationType = informationType;
    }

    public String getSecurityQuestion()
    {
        return securityQuestion;
    }

    public void setSecurityQuestion( String securityQuestion )
    {
        this.securityQuestion = securityQuestion;
    }

    public String[] getSecurityOptions()
    {
        return securityOptions;
    }

    public void setSecurityOptions( String[] securityOptions )
    {
        this.securityOptions = securityOptions;
    }

    /**
     * Provide a more readable get method to indicate that the token is actually the session token.
     * @return
     */
    public String getSessionToken()
    {
        return super.getToken();
    }

    public void setSessionToken( final String sessionToken )
    {
        super.setToken( sessionToken );
    }

    /**
     * Get the user's account.
     * @return
     */
    public TradeItAccount[] getAccounts()
    {
        return accounts;
    }

    public void setAccounts( TradeItAccount[] accounts )
    {
        this.accounts = accounts;
    }

    /**
     * Returns the {@code TradeItAccountDTO} by the {@code accountNumber}.
     * @param accountNumber
     * @return
     */
    public Optional<TradeItAccount> getTradeItAccount( final String accountNumber )
    {
        return Arrays.stream( this.accounts )
                     .filter( tradeItAccount -> tradeItAccount.getAccountNumber()
                                                              .equals( accountNumber ) )
                     .findFirst();
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "AuthenticateAPIResult{" );
        sb.append( "tradeItAccounts=" ).append( Arrays.toString( accounts ) );
        sb.append( ", informationType='" ).append( informationType ).append( '\'' );
        sb.append( ", securityQuestion='" ).append( securityQuestion ).append( '\'' );
        sb.append( ", securityOptions=" ).append( Arrays.toString( securityOptions ) );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
