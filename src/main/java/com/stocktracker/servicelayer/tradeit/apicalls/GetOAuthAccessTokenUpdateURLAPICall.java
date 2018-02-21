package com.stocktracker.servicelayer.tradeit.apicalls;

import com.stocktracker.servicelayer.tradeit.TradeItParameter;
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
     * @param parameterMap Must contain USER_ID_PARAM, TOKEN_PARAM, and BROKER_PARAM.
     * @return OAuth access token result.
     * @throws IllegalArgumentException If the required parameters are not in {@code parameterMap}
     */
    public GetOAuthAccessTokenUpdateURLAPIResult execute( final TradeItAPICallParameters parameterMap )
    {
        final String methodName = "execute ";
        logMethodBegin( methodName, parameterMap );
        parameterMap.parameterCheck( TradeItParameter.USER_ID_PARAM,
                                     TradeItParameter.TOKEN_PARAM,
                                     TradeItParameter.BROKER_PARAM );
        GetOAuthAccessTokenUpdateURLAPIResult GetOAuthAccessTokenUpdateURLAPIResult = this.callTradeIt( parameterMap );
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
