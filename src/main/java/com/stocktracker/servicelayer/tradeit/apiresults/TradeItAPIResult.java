package com.stocktracker.servicelayer.tradeit.apiresults;

import java.util.Arrays;

/**
 * This is the base class for all TradeItAPI calls.  It contains the common result values.
 */
public class TradeItAPIResult
{
    private String status;
    private String token;
    private String shortMessage;
    private String[] longMessages;

    public TradeItAPIResult()
    {
    }

    public TradeItAPIResult( final TradeItAPIResult otherResult )
    {
        this.status = otherResult.status;
        this.token = otherResult.token;
        this.shortMessage = otherResult.shortMessage;
        this.longMessages = otherResult.longMessages;
    }

    public boolean isSuccessful() { return this.status == null ? false : this.status.equals( "SUCCESS" );}

    public String getStatus()
    {
        return status;
    }

    public String getToken()
    {
        return token;
    }

    public String getShortMessage()
    {
        return shortMessage;
    }

    public String[] getLongMessages()
    {
        return longMessages;
    }

    public void setStatus( String status )
    {
        this.status = status;
    }

    public void setToken( String token )
    {
        this.token = token;
    }

    public void setShortMessage( String shortMessage )
    {
        this.shortMessage = shortMessage;
    }

    public void setLongMessages( String[] longMessages )
    {
        this.longMessages = longMessages;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "TradeitResult{" );
        sb.append( "status='" ).append( status ).append( '\'' );
        sb.append( ", token='" ).append( token ).append( '\'' );
        sb.append( ", shortMessage='" ).append( shortMessage ).append( '\'' );
        sb.append( ", longMessages='" ).append( Arrays.toString( longMessages )).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }

}
