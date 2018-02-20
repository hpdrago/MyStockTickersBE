package com.stocktracker.servicelayer.tradeit.apicalls;

import com.stocktracker.common.exceptions.TradeItAuthenticationException;
import com.stocktracker.servicelayer.tradeit.TradeItProperties;
import com.stocktracker.servicelayer.tradeit.apiresults.GetOAuthAccessTokenUpdateURLAPIResult;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This class encapsulates the call to get the URL to update the user's access token.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE)
public class GetOAuthAccessTokenUpdateURLAPICall extends TradeItAPIRestCall<GetOAuthAccessTokenUpdateURLAPIResult>
{
    /**
     * Make the API call to get the URL to update the user's token.
     * @param userId
     * @param userToken
     * @param broker
     * @return
     */
    public GetOAuthAccessTokenUpdateURLAPIResult execute( final String userId, final String userToken, final String broker )
    {
        final String methodName = "execute ";
        logMethodBegin( methodName, userId, userToken, broker );
        this.addPostParameter( TradeItProperties.USER_ID_PARAM, userId );
        this.addPostParameter( TradeItProperties.TOKEN_PARAM, userToken );
        this.addPostParameter( TradeItProperties.BROKER_PARAM, broker );
        GetOAuthAccessTokenUpdateURLAPIResult GetOAuthAccessTokenUpdateURLAPIResult = null;
        try
        {
            GetOAuthAccessTokenUpdateURLAPIResult = this.execute();
        }
        catch( TradeItAuthenticationException e )
        {
            /*
             * Should not get this exception in this context.
             */
            logError( methodName, e );
        }
        logMethodEnd( methodName, GetOAuthAccessTokenUpdateURLAPIResult );
        return GetOAuthAccessTokenUpdateURLAPIResult;
    }

    @Override
    protected String getAPIURL()
    {
        return this.tradeItURLs.getUpdateOauthAccessTokenUrl();
    }

    @Override
    protected Class<GetOAuthAccessTokenUpdateURLAPIResult> getAPIResultsClass()
    {
        return GetOAuthAccessTokenUpdateURLAPIResult.class;
    }
}
