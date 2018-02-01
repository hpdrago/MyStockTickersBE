package com.stocktracker.weblayer.dto.tradeit;

import com.stocktracker.servicelayer.tradeit.apiresults.GetBrokersAPIResult;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * DTO wrapper for the TradeIt GetBrokersAPIResult.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class GetBrokersDTO extends GetBrokersAPIResult
{
}
