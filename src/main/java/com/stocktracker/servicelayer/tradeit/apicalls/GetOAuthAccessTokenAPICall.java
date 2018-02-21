package com.stocktracker.servicelayer.tradeit.apicalls;

import com.stocktracker.servicelayer.tradeit.apiresults.GetOAuthAccessTokenAPIResult;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This class encapsulates the call to get the userId and userToken after obtaining the oAuthVerifier result.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE)
public class GetOAuthAccessTokenAPICall extends TradeItAPIRestCall<GetOAuthAccessTokenAPIResult>
{
    /**
     * Make the API call.
     * @param parameterMap Must contain BROKER_PARAM and OAUTH_VERIFIER_PARAM.
     * @return
     * @throws IllegalArgumentException If the required parameters are not found in {@code parameterMap}.
     */
    public GetOAuthAccessTokenAPIResult execute( final TradeItAPICallParameters parameterMap )
    {
        final String methodName = "execute";
        logMethodBegin( methodName, parameterMap );
        GetOAuthAccessTokenAPIResult getOAuthAccessTokenAPIResult = this.callTradeIt( parameterMap );
        logMethodEnd( methodName, getOAuthAccessTokenAPIResult );
        return getOAuthAccessTokenAPIResult;
    }

    @Override
    protected String getAPIURL()
    {
        return this.tradeItURLs.getOauthAccessTokenURL();
    }

    @Override
    protected Class<GetOAuthAccessTokenAPIResult> getAPIResultsClass()
    {
        return GetOAuthAccessTokenAPIResult.class;
    }
}
