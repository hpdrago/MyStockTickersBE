package com.stocktracker.servicelayer.tradeit.apicalls;

import com.stocktracker.repositorylayer.entity.TradeItAccountEntity;
import com.stocktracker.common.exceptions.TradeItAuthenticationException;
import com.stocktracker.servicelayer.tradeit.TradeItProperties;
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
     * @param tradeItAccountEntity
     * @return
     */
    public CloseSessionAPIResult execute( final TradeItAccountEntity tradeItAccountEntity )
        throws TradeItAuthenticationException
    {
        final String methodName = "execute";
        this.addPostParameter( TradeItProperties.TOKEN_PARAM, tradeItAccountEntity.getAuthToken() );
        return execute();
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
