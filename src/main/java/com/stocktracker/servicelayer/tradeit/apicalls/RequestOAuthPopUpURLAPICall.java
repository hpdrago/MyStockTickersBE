package com.stocktracker.servicelayer.tradeit.apicalls;

import com.stocktracker.servicelayer.tradeit.TradeItURLs;
import com.stocktracker.servicelayer.tradeit.apiresults.RequestOAuthPopUpURLAPIResult;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This class encapsulates the TradeIt API call to get the popup url to authenticate a user's brokerage.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE)
public class RequestOAuthPopUpURLAPICall extends TradeItAPIRestCall<RequestOAuthPopUpURLAPIResult>
{
    /**
     * Make the TradeIt API call.
     * @param broker
     * @return
     */
    public RequestOAuthPopUpURLAPIResult execute( final String broker )
    {
        final String methodName = "RequestOAuthPopUpURLAPIResult ";
        logMethodBegin( methodName, broker );
        final String url = this.tradeItURLs.getRequestOauthPopupURL();
        logDebug( methodName, "url: {0}", url );
        this.addPostParameter( TradeItURLs.BROKER_TAG, broker );
        RequestOAuthPopUpURLAPIResult requestOAuthPopUpURLAPIResult = this.callTradeIt( url );
        logMethodEnd( methodName, requestOAuthPopUpURLAPIResult );
        return requestOAuthPopUpURLAPIResult;
    }

    @Override
    protected Class<RequestOAuthPopUpURLAPIResult> getApiResponseClass()
    {
        return RequestOAuthPopUpURLAPIResult.class;
    }
}
