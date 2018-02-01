package com.stocktracker.weblayer.dto.tradeit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This DTO is returned to the StoxTracker UI after a call to validate the user's response to a security question.
 * It has the same structure as the {@see AuthenticateDTO} as the user may have to answer another question.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE)
@Qualifier( "answerSecurityQuestionDTO")
public class AnswerSecurityQuestionDTO extends AuthenticateDTO
{
}
