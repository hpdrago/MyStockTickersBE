package com.stocktracker.weblayer.dto.tradeit;

public class Authenticate extends TradeItAPIResult
{
    private String sessionToken;

    public String getSessionToken()
    {
        return sessionToken;
    }

    public void setSessionToken( final String sessionToken )
    {
        this.sessionToken = sessionToken;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "Authenticate{" );
        sb.append( "sessionToken='" ).append( sessionToken ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }
}
