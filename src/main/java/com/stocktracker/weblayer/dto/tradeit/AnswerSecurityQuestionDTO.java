package com.stocktracker.weblayer.dto.tradeit;

import com.stocktracker.servicelayer.tradeit.apiresults.AnswerSecurityQuestionAPIResult;

/**
 * This DTO is returned to the StoxTracker UI after a call to validate the user's response to a security question.
 * It has the same structure as the {@see AuthenticateDTO} as the user may have to answer another question.
 */
public class AnswerSecurityQuestionDTO extends AnswerSecurityQuestionAPIResult
{
    /**
     * Creates a new instance.
     * @param answerSecurityQuestionAPIResult
     */
    public AnswerSecurityQuestionDTO( final AnswerSecurityQuestionAPIResult answerSecurityQuestionAPIResult )
    {
        super( answerSecurityQuestionAPIResult );
    }
}
