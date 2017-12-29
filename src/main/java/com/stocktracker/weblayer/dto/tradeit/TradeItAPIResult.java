package com.stocktracker.weblayer.dto.tradeit;

import java.util.Arrays;

public abstract class TradeItAPIResult
{
    private String status;
    private String token;
    private String shortMessage;
    private String[] longMessages;

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
