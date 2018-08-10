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
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "GetOAuthAccessTokenUpdateURLAPIResult{" );
        sb.append( "super=" ).append( super.toString() );
        sb.append( "}" );
        return sb.toString();
    }
}
