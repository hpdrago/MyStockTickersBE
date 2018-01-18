package com.stocktracker.servicelayer.tradeit.apicalls;

import com.stocktracker.repositorylayer.entity.AccountEntity;
import com.stocktracker.servicelayer.tradeit.TradeItProperties;
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
     * @param accountEntity
     * @return
     */
    public KeepSessionAliveAPIResult execute( final AccountEntity accountEntity )
    {
        this.addPostParameter( TradeItProperties.TOKEN_PARAM, accountEntity.getAuthToken() );
        return execute();
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
