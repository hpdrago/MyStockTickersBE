package com.stocktracker.servicelayer.tradeit.apiresults;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * The result from the answer security question is the same as the Authenticate result as there could be a subsequent
 * request to answer another question.  Possibly the user didn't answer the question correctly.
 */

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class AnswerSecurityQuestionAPIResult extends AuthenticateAPIResult
{
    /**
     * Default Constructor
     */
    public AnswerSecurityQuestionAPIResult()
    {
    }


    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "AnswerSecurityQuestionAPIResult{" );
        sb.append( "super=" ).append( super.toString() );
        sb.append( "}" );
        return sb.toString();
    }

}
