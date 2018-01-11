package com.stocktracker.servicelayer.tradeit.apiresults;

public class RequestOAuthPopUpURLAPIResult extends TradeItAPIResult
{
    private String oAuthURL;

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
