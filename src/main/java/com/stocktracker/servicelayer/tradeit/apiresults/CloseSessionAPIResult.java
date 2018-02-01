package com.stocktracker.servicelayer.tradeit.apiresults;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This class defines the return result from a close session request.
 * Only the standard TradeIt API results are needed as there are no custom values returned from this call.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class CloseSessionAPIResult extends TradeItAPIResult
{
}
