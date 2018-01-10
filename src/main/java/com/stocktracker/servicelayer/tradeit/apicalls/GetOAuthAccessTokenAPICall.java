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
     * @param oAuthVerifier
     * @return
     */
    public GetOAuthAccessTokenAPIResult execute( final String oAuthVerifier )
    {
        final String methodName = "execute ";
        logMethodBegin( methodName, oAuthVerifier );
        final String url = this.tradeItURLs.getOauthAccessTokenURL( oAuthVerifier );
        logDebug( methodName, "url: {0}", url );
        final GetOAuthAccessTokenAPIResult getOAuthAccessTokenAPIResult = super.callTradeIt( url );
        logMethodEnd( methodName, getOAuthAccessTokenAPIResult );
        return getOAuthAccessTokenAPIResult;
    }

    @Override
    protected Class<GetOAuthAccessTokenAPIResult> getApiResponseClass()
    {
        return GetOAuthAccessTokenAPIResult.class;
    }
}
