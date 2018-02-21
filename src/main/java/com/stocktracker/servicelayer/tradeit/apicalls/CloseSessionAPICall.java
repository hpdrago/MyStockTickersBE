package com.stocktracker.servicelayer.tradeit.apicalls;

import com.stocktracker.servicelayer.tradeit.apiresults.CloseSessionAPIResult;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This class is used to close the TradeIt session.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class CloseSessionAPICall extends TradeItAPIRestCall<CloseSessionAPIResult>
{
    /**
     * Execute the Keep Session Alive API call.
     * @param parameterMap Must contain TOKEN_PARAM.
     * @return Close session result.
     * @throws IllegalArgumentException if TOKEN_PARAM is not in {@code parameterMap}.
     */
    public CloseSessionAPIResult execute( final TradeItAPICallParameters parameterMap )
    {
        final String methodName = "execute";
        return this.callTradeIt( parameterMap );
    }

    @Override
    protected String getAPIURL()
    {
        return this.tradeItURLs.getCloseSessionURL();
    }

    @Override
    protected Class<CloseSessionAPIResult> getAPIResultsClass()
    {
        return CloseSessionAPIResult.class;
    }
}
