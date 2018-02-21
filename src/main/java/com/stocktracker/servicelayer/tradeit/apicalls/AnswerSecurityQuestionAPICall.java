package com.stocktracker.servicelayer.tradeit.apicalls;

import com.stocktracker.servicelayer.tradeit.TradeItParameter;
import com.stocktracker.servicelayer.tradeit.apiresults.AnswerSecurityQuestionAPIResult;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This class encapsulates the TradeIt API call to send the answer back to the broker to continue the authentication
 * process.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE)
public class AnswerSecurityQuestionAPICall extends TradeItAPIRestCall<AnswerSecurityQuestionAPIResult>
{
    private TradeItAPICallParameters parameterMap;

    /**
     * Authenticate the user's account.
     * @param parameterMap Must contain TOKEN_PARAM, SECURITY_ANSWER_PARAM.
     * @return
     * @throws IllegalArgumentException if {@code parameterMap} does not contain the required parameters.
     */
    public AnswerSecurityQuestionAPIResult execute( final TradeItAPICallParameters parameterMap )
    {
        final String methodName = "execute";
        logMethodBegin( methodName, parameterMap );
        this.parameterMap = parameterMap;
        parameterMap.parameterCheck( TradeItParameter.TOKEN_PARAM, TradeItParameter.SECURITY_ANSWER_PARAM );
        final AnswerSecurityQuestionAPIResult answerSecurityQuestionAPIResult = this.callTradeIt( parameterMap );
        logMethodEnd( methodName, answerSecurityQuestionAPIResult );
        return answerSecurityQuestionAPIResult;
    }

    @Override
    protected String getAPIURL()
    {
        return this.tradeItURLs.getAnswerSecurityQuestionURL( this.parameterMap.getParameterValue( TradeItParameter.AUTH_UUID ));
    }

    @Override
    protected Class<AnswerSecurityQuestionAPIResult> getAPIResultsClass()
    {
        return AnswerSecurityQuestionAPIResult.class;
    }
}
