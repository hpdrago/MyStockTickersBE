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
    protected Class<GetBrokersAPIResult> getApiResponseClass()
    {
        return GetBrokersAPIResult.class;
    }

    /**
     * Make the API REST call.
     * @return
     */
    public GetBrokersAPIResult execute()
    {
        final String methodName = "execute";
        logMethodBegin( methodName );
        final GetBrokersAPIResult getBrokersAPIResult = this.callTradeIt( this.tradeItURLs.getBrokersURL() ) ;
        logMethodEnd( methodName, getBrokersAPIResult );
        return getBrokersAPIResult;
    }
}
