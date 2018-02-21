package com.stocktracker.servicelayer.tradeit.apicalls;

import com.stocktracker.servicelayer.tradeit.TradeItParameter;
import com.stocktracker.servicelayer.tradeit.apiresults.GetPositionsAPIResult;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This class handles the call to get the positions for an account.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class GetPositionsAPICall extends TradeItAPIRestCall<GetPositionsAPIResult>
{
    @Override
    protected String getAPIURL()
    {
        return this.tradeItURLs.getGetPositionsURL();
    }

    /**
     * Execute the get positions API call to TradeIt.
     * @param parameterMap Must contain TOKEN_PARAM and ACCOUNT_NUMBER_PARAM.
     * @return The positions for the account.
     * @throws IllegalArgumentException if the required parameters are not in {@code parameterMap}.
     */
    public GetPositionsAPIResult execute( final TradeItAPICallParameters parameterMap )
    {
        final String methodName = "execute";
        logMethodBegin( methodName, parameterMap );
        parameterMap.parameterCheck( TradeItParameter.TOKEN_PARAM, TradeItParameter.ACCOUNT_NUMBER_PARAM );
        final GetPositionsAPIResult getPositionsAPIResult = super.callTradeIt( parameterMap );
        logMethodEnd( methodName, getPositionsAPIResult );
        return getPositionsAPIResult;
    }

    @Override
    protected Class<GetPositionsAPIResult> getAPIResultsClass()
    {
        return GetPositionsAPIResult.class;
    }
}
