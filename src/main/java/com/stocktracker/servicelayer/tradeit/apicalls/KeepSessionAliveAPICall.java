package com.stocktracker.servicelayer.tradeit.apicalls;

import com.stocktracker.common.exceptions.TradeItAuthenticationException;
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
     * @param authToken
     * @return
     * @throws TradeItAuthenticationException
     */
    public KeepSessionAliveAPIResult execute( final String authToken )
        throws TradeItAuthenticationException
    {
        final String methodName = "execute";
        logMethodBegin( methodName );
        Objects.requireNonNull( authToken, "authToken cannot be null" );
        this.addPostParameter( TradeItProperties.TOKEN_PARAM, authToken );
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
