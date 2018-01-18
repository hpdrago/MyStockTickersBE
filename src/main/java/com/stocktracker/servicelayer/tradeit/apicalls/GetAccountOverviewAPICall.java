package com.stocktracker.servicelayer.tradeit.apicalls;

import com.stocktracker.repositorylayer.entity.TradeItAccountEntity;
import com.stocktracker.servicelayer.tradeit.TradeItProperties;
import com.stocktracker.servicelayer.tradeit.apiresults.GetAccountOverViewAPIResult;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This class makes the TradeIt API call to get the account detail overview for a linked account.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class GetAccountOverviewAPICall extends TradeItAPIRestCall<GetAccountOverViewAPIResult>
{
    /**
     * Execute the Keep Session Alive API call.
     * @param tradeItAccountEntity
     * @return
     */
    public GetAccountOverViewAPIResult execute( final String accountNumber, final TradeItAccountEntity tradeItAccountEntity )
    {
        final String methodName = "execute";
        logMethodBegin( methodName, accountNumber, tradeItAccountEntity );
        this.addPostParameter( TradeItProperties.TOKEN_PARAM, tradeItAccountEntity.getAuthToken() );
        this.addPostParameter( TradeItProperties.ACCOUNT_NUMBER_PARAM, accountNumber );
        final GetAccountOverViewAPIResult getAccountOverViewAPIResult = super.execute();
        logMethodEnd( methodName, getAccountOverViewAPIResult );
        return getAccountOverViewAPIResult;
    }

    @Override
    protected String getAPIURL()
    {
        return this.tradeItURLs.getGetAccountOverviewURL();
    }

    @Override
    protected Class<GetAccountOverViewAPIResult> getAPIResultsClass()
    {
        return GetAccountOverViewAPIResult.class;
    }
}
