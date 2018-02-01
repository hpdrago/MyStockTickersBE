package com.stocktracker.weblayer.dto.tradeit;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This is the DTO class to return to the client after making the TradeIt call to get the OAuth access token.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class GetOAuthAccessTokenUpdateURLDTO extends RequestOAuthPopUpURLDTO
{
}
