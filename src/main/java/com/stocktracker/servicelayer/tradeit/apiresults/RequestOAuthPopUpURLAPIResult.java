package com.stocktracker.servicelayer.tradeit.apiresults;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This class contains the result from a call to get the TradeIt PopUp URL for a given broker.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
@Qualifier( "requestOAuthPopUpURLAPIResult")
public class RequestOAuthPopUpURLAPIResult extends TradeItAPIResult
{
    private String oAuthURL;

    public RequestOAuthPopUpURLAPIResult()
    {
    }

    /**
     * Sets the results.
     * @param requestOAuthPopUpURLAPIResult
     */
    public void setResults( final RequestOAuthPopUpURLAPIResult requestOAuthPopUpURLAPIResult )
    {
        super.setResults( requestOAuthPopUpURLAPIResult );
        this.oAuthURL = requestOAuthPopUpURLAPIResult.oAuthURL;
    }

    public String getoAuthURL()
    {
        return oAuthURL;
    }

    public void setoAuthURL( final String oAuthURL )
    {
        this.oAuthURL = oAuthURL;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "RequestOAuthPopUpURLAPIResult{" );
        sb.append( "oAuthURL='" ).append( oAuthURL ).append( '\'' );
        sb.append( "super=" ).append( super.toString() );
        sb.append( '}' );
        return sb.toString();
    }
}
