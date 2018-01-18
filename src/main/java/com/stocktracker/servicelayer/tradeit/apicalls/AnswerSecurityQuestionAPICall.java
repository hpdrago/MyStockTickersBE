package com.stocktracker.servicelayer.tradeit.apicalls;

import com.stocktracker.repositorylayer.entity.AccountEntity;
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
    private AccountEntity accountEntity;

    /**
     * Authenticate the user's account.
     * @param accountEntity
     * @return
     */
    public AnswerSecurityQuestionAPIResult execute( final AccountEntity accountEntity, final String answer )
    {
        final String methodName = "execute";
        logMethodBegin( methodName, accountEntity, answer );
        this.accountEntity = accountEntity;
        Objects.requireNonNull( accountEntity.getAuthToken(), "AuthToken is missing from the account" );
        Objects.requireNonNull( accountEntity.getAuthUuid(), "AuthUUID is missing from the account" );
        this.addPostParameter( this.tradeItProperties.TOKEN_PARAM, accountEntity.getAuthToken() );
        this.addPostParameter( this.tradeItProperties.SECURITY_ANSWER_PARAM, answer );
        AnswerSecurityQuestionAPIResult answerSecurityQuestionAPIResult = this.execute();
        logMethodBegin( methodName, answerSecurityQuestionAPIResult );
        return answerSecurityQuestionAPIResult;
    }

    @Override
    protected String getAPIURL()
    {
        return this.tradeItURLs.getAnswerSecurityQuestionURL( accountEntity.getAuthUuid() );
    }

    @Override
    protected Class<AnswerSecurityQuestionAPIResult> getAPIResultsClass()
    {
        return AnswerSecurityQuestionAPIResult.class;
    }
}
