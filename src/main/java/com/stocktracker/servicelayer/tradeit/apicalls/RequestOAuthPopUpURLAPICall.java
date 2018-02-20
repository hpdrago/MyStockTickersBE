package com.stocktracker.servicelayer.tradeit.apicalls;

import com.stocktracker.common.exceptions.TradeItAuthenticationException;
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
     * @param broker
     * @return
     */
    public RequestOAuthPopUpURLAPIResult execute( final String broker )
    {
        final String methodName = "RequestOAuthPopUpURLAPIResult ";
        logMethodBegin( methodName, broker );
        this.addPostParameter( this.tradeItProperties.BROKER_PARAM, broker );
        RequestOAuthPopUpURLAPIResult requestOAuthPopUpURLAPIResult = null;
        try
        {
            requestOAuthPopUpURLAPIResult = this.execute();
        }
        catch( TradeItAuthenticationException e )
        {
            // should not get this exception in this context as this is part of the initial authentication process.
            logError( methodName, e );
        }
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
