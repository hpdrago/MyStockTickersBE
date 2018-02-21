package com.stocktracker.servicelayer.tradeit.apicalls;

import com.stocktracker.servicelayer.tradeit.TradeItParameter;
import com.stocktracker.servicelayer.tradeit.apiresults.RequestOAuthPopUpURLAPIResult;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This class encapsulates the TradeIt API call to get the popup url to authenticate a user's brokerage.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class RequestOAuthPopUpURLAPICall extends TradeItAPIRestCall<RequestOAuthPopUpURLAPIResult>
{
    /**
     * Make the TradeIt API call to get the OAuth Popup URL.
     * @param parameterMap Must contain BROKER_PARAM
     * @return
     */
    public RequestOAuthPopUpURLAPIResult execute( final TradeItAPICallParameters parameterMap )
    {
        final String methodName = "RequestOAuthPopUpURLAPIResult ";
        logMethodBegin( methodName, parameterMap );
        parameterMap.parameterCheck( TradeItParameter.BROKER_PARAM );
        RequestOAuthPopUpURLAPIResult requestOAuthPopUpURLAPIResult = this.callTradeIt( parameterMap );
        logMethodEnd( methodName, requestOAuthPopUpURLAPIResult );
        return requestOAuthPopUpURLAPIResult;
    }

    @Override
    protected String getAPIURL()
    {
        return this.tradeItURLs.getRequestOauthPopupURL();
    }

    @Override
    protected Class<RequestOAuthPopUpURLAPIResult> getAPIResultsClass()
    {
        return RequestOAuthPopUpURLAPIResult.class;
    }
}
