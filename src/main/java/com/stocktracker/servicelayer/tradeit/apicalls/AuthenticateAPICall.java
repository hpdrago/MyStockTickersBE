package com.stocktracker.servicelayer.tradeit.apicalls;

import com.stocktracker.repositorylayer.entity.TradeItAccountEntity;
import com.stocktracker.common.exceptions.TradeItAuthenticationException;
import com.stocktracker.servicelayer.tradeit.apiresults.AuthenticateAPIResult;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * This class encapsulates the Authenticate API call.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE)
public class AuthenticateAPICall extends TradeItAPIRestCall<AuthenticateAPIResult>
{
    private TradeItAccountEntity tradeItAccountEntity;

    /**
     * Authenticate the user's account and add/update/delete linked accounts.
     * @param tradeItAccountEntity
     * @return
     */
    public AuthenticateAPIResult execute( final TradeItAccountEntity tradeItAccountEntity )
    {
        final String methodName = "execute";
        logMethodBegin( methodName, tradeItAccountEntity );
        this.tradeItAccountEntity = tradeItAccountEntity;
        Objects.requireNonNull( tradeItAccountEntity.getUserId(), "userId cannot be null" );
        Objects.requireNonNull( tradeItAccountEntity.getUserToken(), "userToken cannot be null" );
        Objects.requireNonNull( tradeItAccountEntity.getBrokerage(), "brokerage cannot be null" );
        this.addPostParameter( this.tradeItProperties.USER_ID_PARAM, tradeItAccountEntity.getUserId() );
        this.addPostParameter( this.tradeItProperties.USER_TOKEN_PARAM, tradeItAccountEntity.getUserToken() );
        this.addPostParameter( this.tradeItProperties.BROKER_PARAM, tradeItAccountEntity.getBrokerage() );
        AuthenticateAPIResult authenticateAPIResult = null;
        try
        {
            authenticateAPIResult = this.execute();
        }
        catch( TradeItAuthenticationException e )
        {
            logError( methodName, e );
        }
        logMethodEnd( methodName, authenticateAPIResult );
        return authenticateAPIResult;
    }

    @Override
    protected String getAPIURL()
    {
        return this.tradeItURLs.getAuthenticateUrl( tradeItAccountEntity.getAuthUuid() );
    }

    @Override
    protected Class<AuthenticateAPIResult> getAPIResultsClass()
    {
        return AuthenticateAPIResult.class;
    }
}
