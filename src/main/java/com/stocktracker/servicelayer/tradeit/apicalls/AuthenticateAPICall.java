package com.stocktracker.servicelayer.tradeit.apicalls;

import com.stocktracker.repositorylayer.entity.AccountEntity;
import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.servicelayer.tradeit.apiresults.AuthenticateAPIResult;
import com.stocktracker.servicelayer.tradeit.types.TradeItAccount;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class encapsulates the Authenticate API call.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE)
public class AuthenticateAPICall extends TradeItAPIRestCall<AuthenticateAPIResult>
{
    private AccountEntity accountEntity;

    /**
     * Authenticate the user's account and add/update/delete linked accounts.
     * @param accountEntity
     * @return
     */
    public AuthenticateAPIResult execute( final AccountEntity accountEntity )
    {
        final String methodName = "execute";
        logMethodBegin( methodName, accountEntity );
        this.accountEntity = accountEntity;
        Objects.requireNonNull( accountEntity.getUserId(), "userId cannot be null" );
        Objects.requireNonNull( accountEntity.getUserToken(), "userToken cannot be null" );
        Objects.requireNonNull( accountEntity.getBrokerage(), "brokerage cannot be null" );
        this.addPostParameter( this.tradeItProperties.USER_ID_PARAM, accountEntity.getUserId() );
        this.addPostParameter( this.tradeItProperties.USER_TOKEN_PARAM, accountEntity.getUserToken() );
        this.addPostParameter( this.tradeItProperties.BROKER_PARAM, accountEntity.getBrokerage() );
        AuthenticateAPIResult authenticateAPIResult = this.execute();
        logMethodBegin( methodName, authenticateAPIResult );
        return authenticateAPIResult;
    }

    @Override
    protected String getAPIURL()
    {
        return this.tradeItURLs.getAuthenticateUrl( accountEntity.getAuthUuid() );
    }

    @Override
    protected Class<AuthenticateAPIResult> getAPIResultsClass()
    {
        return AuthenticateAPIResult.class;
    }
}
