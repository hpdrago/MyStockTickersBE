package com.stocktracker.servicelayer.tradeit.apiresults;

/**
 * This class contains the result from a call to get the TradeIt PopUp URL for a given broker.
 */
public class RequestOAuthPopUpURLAPIResult extends TradeItAPIResult
{
    private String oAuthURL;

    public RequestOAuthPopUpURLAPIResult()
    {
    }

    /**
     * Creates a new instance.
     * @param requestOAuthPopUpURLAPIResult
     */
    public RequestOAuthPopUpURLAPIResult( final RequestOAuthPopUpURLAPIResult requestOAuthPopUpURLAPIResult )
    {
        super( requestOAuthPopUpURLAPIResult );
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
