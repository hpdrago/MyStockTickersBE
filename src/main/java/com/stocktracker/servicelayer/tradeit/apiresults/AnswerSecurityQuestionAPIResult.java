package com.stocktracker.servicelayer.tradeit.apiresults;

/**
 * The result from the answer security question is the same as the Authenticate result as there could be a subsequent
 * request to answer another question.  Possibly the user didn't answer the question correctly.
 */
public class AnswerSecurityQuestionAPIResult extends AuthenticateAPIResult<AnswerSecurityQuestionAPIResult>
{
    /**
     * Default Constructor
     */
    public AnswerSecurityQuestionAPIResult()
    {
    }

    /**
     * Creates a new instance.
     * @param answerSecurityQuestionAPIResult
     */
    public AnswerSecurityQuestionAPIResult( final AnswerSecurityQuestionAPIResult answerSecurityQuestionAPIResult )
    {
        super( answerSecurityQuestionAPIResult );
    }
}
