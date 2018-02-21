package com.stocktracker.servicelayer.tradeit.apicalls;

import com.stocktracker.servicelayer.tradeit.apiresults.GetBrokersAPIResult;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This class makes the REST API call to get the TradeIt supported brokers.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE)
public class GetBrokersAPICall extends TradeItAPIRestCall<GetBrokersAPIResult>
{
    @Override
    protected String getAPIURL()
    {
        return this.tradeItURLs.getBrokersURL();
    }

    @Override
    protected Class<GetBrokersAPIResult> getAPIResultsClass()
    {
        return GetBrokersAPIResult.class;
    }

    public GetBrokersAPIResult execute( final TradeItAPICallParameters parameterMap )
    {
        final String methodName = "execute";
        logMethodBegin( methodName );
        GetBrokersAPIResult getBrokersAPIResult = this.callTradeIt( parameterMap );
        logMethodEnd( methodName, getBrokersAPIResult );
        return getBrokersAPIResult;
    }
}
