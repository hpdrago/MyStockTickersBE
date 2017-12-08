package com.stocktracker.weblayer.dto;

public abstract class TradeItResult
{
    public String status;
    public String token;
    public String shortMessage;
    public String longMessages;

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

    public String getLongMessages()
    {
        return longMessages;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "TradeitResult{" );
        sb.append( "status='" ).append( status ).append( '\'' );
        sb.append( ", token='" ).append( token ).append( '\'' );
        sb.append( ", shortMessage='" ).append( shortMessage ).append( '\'' );
        sb.append( ", longMessages='" ).append( longMessages ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }
}
