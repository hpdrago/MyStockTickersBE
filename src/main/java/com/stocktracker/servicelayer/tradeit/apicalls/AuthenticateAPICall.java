package com.stocktracker.servicelayer.tradeit.apicalls;

import com.stocktracker.repositorylayer.entity.AccountEntity;
import com.stocktracker.servicelayer.tradeit.apiresults.AuthenticateAPIResult;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This class encapsulates the Authenticate API call.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE)
public class AuthenticateAPICall extends TradeItAPIRestCall<AuthenticateAPIResult>
{
    /**
     * Authenticate the user's account.
     * @param accountEntity
     * @return
     */
    public AuthenticateAPIResult execute( final AccountEntity accountEntity )
    {
        final String methodName = "execute";
        logMethodBegin( methodName, accountEntity );
        final String authenticateURL = this.tradeItURLs.getAuthenticateUrl( accountEntity.getSrv() );
        this.addPostParameter( this.tradeItProperties.USER_ID_PARAM, accountEntity.getUserId() );
        this.addPostParameter( this.tradeItProperties.USER_TOKEN_PARAM, accountEntity.getUserToken() );
        this.addPostParameter( this.tradeItProperties.BROKER_PARAM, accountEntity.getBrokerage() );
        AuthenticateAPIResult authenticateAPIResult = this.callTradeIt( authenticateURL );
        logMethodBegin( methodName, authenticateAPIResult );
        return authenticateAPIResult;
    }

    @Override
    protected Class<AuthenticateAPIResult> getApiResponseClass()
    {
        return AuthenticateAPIResult.class;
    }
}
