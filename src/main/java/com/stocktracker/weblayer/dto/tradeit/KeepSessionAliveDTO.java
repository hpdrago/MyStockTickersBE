package com.stocktracker.weblayer.dto.tradeit;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * The DTO returned when calling TradeIt to keep the session alive.
 * The contents are the same as with authentication as we need the LinkedAccount and LinkedAccounts.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class KeepSessionAliveDTO extends AuthenticateDTO
{
    public KeepSessionAliveDTO()
    {
    }

    public KeepSessionAliveDTO( final AuthenticateDTO authenticateDTO )
    {
        super( authenticateDTO );
    }
}
