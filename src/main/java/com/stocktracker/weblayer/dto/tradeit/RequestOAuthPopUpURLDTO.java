package com.stocktracker.weblayer.dto.tradeit;

public class RequestOAuthPopUpURLDTO extends TradeItAPIResult
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
        final StringBuilder sb = new StringBuilder( "RequestOAuthPopUpURLDTO{" );
        sb.append( "oAuthURL='" ).append( oAuthURL ).append( '\'' );
        sb.append( "super={" ).append( super.toString() ).append( '}' );
        sb.append( '}' );
        return sb.toString();
    }
}
