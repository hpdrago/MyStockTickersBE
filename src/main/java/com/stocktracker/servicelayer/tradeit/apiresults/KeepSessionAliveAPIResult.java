package com.stocktracker.servicelayer.tradeit.apiresults;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This class contains the values returned from a keepSessionAlive API call.
 * There are no custom values returned from this call so the standard TradeIt result values are sufficient.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class KeepSessionAliveAPIResult extends TradeItAPIResult
{
    /**
     * Default constructor.
     */
    public KeepSessionAliveAPIResult()
    {
    }
}
