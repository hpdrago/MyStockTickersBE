package com.stocktracker.weblayer.dto.tradeit;

import com.stocktracker.servicelayer.tradeit.apiresults.KeepSessionAliveAPIResult;
import com.stocktracker.weblayer.dto.TradeItAccountDTO;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * The DTO returned when calling TradeIt to keep the session alive.
 * The contents are the same as with authentication as we need the TradeItAccount and LinkedAccounts.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class KeepSessionAliveDTO extends AuthenticateDTO
{
    public KeepSessionAliveDTO()
    {
    }
}