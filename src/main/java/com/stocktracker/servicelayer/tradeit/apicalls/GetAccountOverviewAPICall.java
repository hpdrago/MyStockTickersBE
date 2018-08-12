package com.stocktracker.servicelayer.tradeit.apicalls;

import com.stocktracker.servicelayer.tradeit.TradeItParameter;
import com.stocktracker.servicelayer.tradeit.apiresults.GetAccountOverviewAPIResult;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This class makes the TradeIt API call to get the account detail overview for a linked account.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class GetAccountOverviewAPICall extends TradeItAPIRestCall<GetAccountOverviewAPIResult>
{
    /**
     * Execute the Keep Session Alive API call.
     * @param parameterMap Map must contain TOKEN_PARAM and ACCOUNT_NUMBER_PARAM
     * @return Result of keep alive call.
     * @throws IllegalArgumentException If the required parameters are not found in {@code parameterMap}
     */
    public GetAccountOverviewAPIResult execute( final TradeItAPICallParameters parameterMap )
    {
        final String methodName = "execute";
        logMethodBegin( methodName, parameterMap );
        parameterMap.parameterCheck( TradeItParameter.TOKEN_PARAM, TradeItParameter.ACCOUNT_NUMBER_PARAM );
        final GetAccountOverviewAPIResult getAccountOverviewAPIResult = super.callTradeIt( parameterMap );
        logMethodEnd( methodName, getAccountOverviewAPIResult );
        return getAccountOverviewAPIResult;
    }

    @Override
    protected String getAPIURL()
    {
        return this.tradeItURLs.getGetAccountOverviewURL();
    }

    @Override
    protected Class<GetAccountOverviewAPIResult> getAPIResultsClass()
    {
        return GetAccountOverviewAPIResult.class;
    }
}
