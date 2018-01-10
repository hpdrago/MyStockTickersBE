package com.stocktracker.servicelayer.tradeit.apiresults;

import com.stocktracker.weblayer.dto.tradeit.Account;

import java.util.Arrays;

/**
 * This class contains the result from the API call to authenticate a user's account.
 */
public class AuthenticateAPIResult extends TradeItAPIResult
{
    private String informationType;
    private String securityQuestion;
    private String securityOptions[];
    private Account[] accounts;

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
    public Account[] getAccounts()
    {
        return accounts;
    }

    public void setAccounts( Account[] accounts )
    {
        this.accounts = accounts;
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

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "AuthenticateAPIResult{" );
        sb.append( "informationType='" ).append( informationType ).append( '\'' );
        sb.append( ", securityQuestion='" ).append( securityQuestion ).append( '\'' );
        sb.append( ", securityOptions='" ).append( securityOptions ).append( '\'' );
        sb.append( ", accounts=" ).append( Arrays.toString( accounts ) );
        sb.append( '}' );
        return sb.toString();
    }
}
