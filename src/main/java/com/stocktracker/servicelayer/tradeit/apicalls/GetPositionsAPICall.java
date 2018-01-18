package com.stocktracker.servicelayer.tradeit.apicalls;

import com.stocktracker.repositorylayer.entity.TradeItAccountEntity;
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
     * @param tradeItAccountEntity
     * @return
     */
    public GetPositionsAPIResult execute( final String accountNumber, final TradeItAccountEntity tradeItAccountEntity )
    {
        final String methodName = "execute";
        logMethodBegin( methodName, accountNumber, tradeItAccountEntity );
        this.addPostParameter( TradeItProperties.TOKEN_PARAM, tradeItAccountEntity.getAuthToken() );
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
