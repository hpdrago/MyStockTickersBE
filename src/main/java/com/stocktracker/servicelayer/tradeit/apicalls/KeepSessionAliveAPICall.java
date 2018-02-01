package com.stocktracker.servicelayer.tradeit.apicalls;

import com.stocktracker.repositorylayer.entity.TradeItAccountEntity;
import com.stocktracker.servicelayer.tradeit.TradeItProperties;
import com.stocktracker.servicelayer.tradeit.apiresults.KeepSessionAliveAPIResult;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * this class will execute the Keep Session Alive TradeIt API call.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class KeepSessionAliveAPICall extends TradeItAPIRestCall<KeepSessionAliveAPIResult>
{
    /**
     * Execute the Keep Session Alive API call.
     * @param tradeItAccountEntity
     * @return
     */
    public KeepSessionAliveAPIResult execute( final TradeItAccountEntity tradeItAccountEntity )
    {
        final String methodName = "execute";
        logMethodBegin( methodName, tradeItAccountEntity );
        Objects.requireNonNull( tradeItAccountEntity, "tradeItAccountEntity cannot be null" );
        Objects.requireNonNull( tradeItAccountEntity.getUserToken(), "tradeItAccountEntity.authToken cannot be null" );
        this.addPostParameter( TradeItProperties.TOKEN_PARAM, tradeItAccountEntity.getAuthToken() );
        logMethodEnd( methodName, tradeItAccountEntity );
        KeepSessionAliveAPIResult keepSessionAliveAPIResult = this.execute();
        logMethodEnd( methodName, keepSessionAliveAPIResult );
        return keepSessionAliveAPIResult;
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
