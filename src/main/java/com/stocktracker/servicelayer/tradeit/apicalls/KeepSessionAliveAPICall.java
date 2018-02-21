package com.stocktracker.servicelayer.tradeit.apicalls;

import com.stocktracker.servicelayer.tradeit.TradeItParameter;
import com.stocktracker.servicelayer.tradeit.apiresults.KeepSessionAliveAPIResult;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * this class will execute the Keep Session Alive TradeIt API call.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class KeepSessionAliveAPICall extends TradeItAPIRestCall<KeepSessionAliveAPIResult>
{
    /**
     * Execute the Keep Session Alive API call.
     * @param parameterMap Must contain TOKEN_PARAM
     * @return
     * @throws IllegalArgumentException if TOKEN_PARAM is not in {@code parameterMap}
     */
    public KeepSessionAliveAPIResult execute( final TradeItAPICallParameters parameterMap )
    {
        final String methodName = "execute";
        logMethodBegin( methodName, parameterMap );
        parameterMap.parameterCheck( TradeItParameter.TOKEN_PARAM );
        KeepSessionAliveAPIResult keepSessionAliveAPIResult = this.callTradeIt( parameterMap );
        logMethodEnd( methodName, keepSessionAliveAPIResult );
        return keepSessionAliveAPIResult;
    }

    @Override
    protected String getAPIURL()
    {
        return this.tradeItURLs.getKeepSessionAliveURL();
    }

    @Override
    protected Class<KeepSessionAliveAPIResult> getAPIResultsClass()
    {
        return KeepSessionAliveAPIResult.class;
    }
}
