package com.stocktracker.weblayer.dto.tradeit;

import com.stocktracker.servicelayer.tradeit.apiresults.RequestOAuthPopUpURLAPIResult;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * DTO wrapper for RequestOAuthPopUpURLAPIResult
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
@Qualifier( "requestOAuthPopUpURLDTO")
public class RequestOAuthPopUpURLDTO extends RequestOAuthPopUpURLAPIResult
{
}
