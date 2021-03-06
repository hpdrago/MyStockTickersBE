package com.stocktracker.servicelayer.tradeit.apiresults;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This class contains the results from calling TradeIt to obtain the userId and userToken using the oAuthVerifier.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class GetOAuthAccessTokenAPIResult extends TradeItAPIResult
{
    private String userId;
    private String userToken;

    public GetOAuthAccessTokenAPIResult()
    {
    }

    /**
     * Sets the results.
     * @param getOAuthAccessTokenAPIResult
     */
    public void setResults( final GetOAuthAccessTokenAPIResult getOAuthAccessTokenAPIResult )
    {
        super.setResults( getOAuthAccessTokenAPIResult );
        this.userId = getOAuthAccessTokenAPIResult.userId;
        this.userToken = getOAuthAccessTokenAPIResult.userToken;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId( String userId )
    {
        this.userId = userId;
    }

    public String getUserToken()
    {
        return userToken;
    }

    public void setUserToken( String userToken )
    {
        this.userToken = userToken;
    }


    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "GetOAuthAccessTokenAPIResult{" );
        sb.append( "userId='" ).append( userId ).append( '\'' );
        sb.append( ", userToken='" ).append( userToken ).append( '\'' );
        sb.append( ", super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
