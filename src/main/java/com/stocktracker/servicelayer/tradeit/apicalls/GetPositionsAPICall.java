package com.stocktracker.servicelayer.tradeit.apicalls;

import com.stocktracker.common.exceptions.TradeItAuthenticationException;
import com.stocktracker.servicelayer.tradeit.TradeItProperties;
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
     * @param accountNumber
     * @param authToken
     * @return
     * @throws TradeItAuthenticationException
     */
    public GetPositionsAPIResult execute( final String accountNumber, final String authToken )
        throws TradeItAuthenticationException
    {
        final String methodName = "execute";
        logMethodBegin( methodName, accountNumber, authToken );
        this.addPostParameter( TradeItProperties.TOKEN_PARAM, authToken );
        this.addPostParameter( TradeItProperties.ACCOUNT_NUMBER_PARAM, accountNumber );
        final GetPositionsAPIResult getPositionsAPIResult = super.execute();
        logMethodEnd( methodName, getPositionsAPIResult );
        return getPositionsAPIResult;
    }

    @Override
    protected Class<GetPositionsAPIResult> getAPIResultsClass()
    {
        return GetPositionsAPIResult.class;
    }
}
