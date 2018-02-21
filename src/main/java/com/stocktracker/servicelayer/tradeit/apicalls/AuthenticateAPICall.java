package com.stocktracker.servicelayer.tradeit.apicalls;

import com.stocktracker.servicelayer.tradeit.TradeItParameter;
import com.stocktracker.servicelayer.tradeit.apiresults.AuthenticateAPIResult;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This class encapsulates the Authenticate API call.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE)
public class AuthenticateAPICall extends TradeItAPIRestCall<AuthenticateAPIResult>
{
    private TradeItAPICallParameters parameterMap;

    /**
     * Authenticate the user's account and add/update/delete linked accounts.
     * @param parameterMap Must contain USER_ID_PARAM, USER_TOKEN_PARAM, AUTH_UUID, and BROKER_PARAM.
     * @return Authentication result.
     * @throws IllegalArgumentException if {@code parameterMap} does not contain the required parameters.
     */
    public AuthenticateAPIResult execute( final TradeItAPICallParameters parameterMap )
    {
        final String methodName = "execute";
        logMethodBegin( methodName, parameterMap );
        parameterMap.parameterCheck( TradeItParameter.USER_ID_PARAM,
                                     TradeItParameter.USER_TOKEN_PARAM,
                                     TradeItParameter.AUTH_UUID,
                                     TradeItParameter.BROKER_PARAM );
        this.parameterMap = parameterMap;
        AuthenticateAPIResult authenticateAPIResult = this.callTradeIt( parameterMap );
        logMethodEnd( methodName, authenticateAPIResult );
        return authenticateAPIResult;
    }

    @Override
    protected String getAPIURL()
    {
        return this.tradeItURLs.getAuthenticateUrl( this.parameterMap.getParameterValue( TradeItParameter.AUTH_UUID ));
    }

    @Override
    protected Class<AuthenticateAPIResult> getAPIResultsClass()
    {
        return AuthenticateAPIResult.class;
    }
}
