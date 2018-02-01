package com.stocktracker.servicelayer.tradeit.apiresults;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This class contains the results from calling TradeIt to update the userToken.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class GetOAuthAccessTokenUpdateURLAPIResult extends RequestOAuthPopUpURLAPIResult
{
}
