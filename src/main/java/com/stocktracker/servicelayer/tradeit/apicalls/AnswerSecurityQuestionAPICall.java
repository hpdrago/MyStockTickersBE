package com.stocktracker.servicelayer.tradeit.apicalls;

import com.stocktracker.repositorylayer.entity.TradeItAccountEntity;
import com.stocktracker.common.exceptions.TradeItAuthenticationException;
import com.stocktracker.servicelayer.tradeit.apiresults.AnswerSecurityQuestionAPIResult;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * This class encapsulates the TradeIt API call to send the answer back to the broker to continue the authentication
 * process.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE)
public class AnswerSecurityQuestionAPICall extends TradeItAPIRestCall<AnswerSecurityQuestionAPIResult>
{
    private TradeItAccountEntity tradeItAccountEntity;

    /**
     * Authenticate the user's account.
     * @param tradeItAccountEntity
     * @return
     */
    public AnswerSecurityQuestionAPIResult execute( final TradeItAccountEntity tradeItAccountEntity, final String answer )
    {
        final String methodName = "execute";
        logMethodBegin( methodName, tradeItAccountEntity, answer );
        this.tradeItAccountEntity = tradeItAccountEntity;
        Objects.requireNonNull( tradeItAccountEntity.getAuthToken(), "AuthToken is missing from the account" );
        Objects.requireNonNull( tradeItAccountEntity.getAuthUuid(), "AuthUUID is missing from the account" );
        this.addPostParameter( this.tradeItProperties.TOKEN_PARAM, tradeItAccountEntity.getAuthToken() );
        this.addPostParameter( this.tradeItProperties.SECURITY_ANSWER_PARAM, answer );
        AnswerSecurityQuestionAPIResult answerSecurityQuestionAPIResult = null;
        try
        {
            answerSecurityQuestionAPIResult = this.execute();
        }
        catch( TradeItAuthenticationException e )
        {
            /*
             * Should not get this exception in this context since this is part of the authentication process.
             */
            logError( methodName, e );
        }
        logMethodEnd( methodName, answerSecurityQuestionAPIResult );
        return answerSecurityQuestionAPIResult;
    }

    @Override
    protected String getAPIURL()
    {
        return this.tradeItURLs.getAnswerSecurityQuestionURL( tradeItAccountEntity.getAuthUuid() );
    }

    @Override
    protected Class<AnswerSecurityQuestionAPIResult> getAPIResultsClass()
    {
        return AnswerSecurityQuestionAPIResult.class;
    }
}
