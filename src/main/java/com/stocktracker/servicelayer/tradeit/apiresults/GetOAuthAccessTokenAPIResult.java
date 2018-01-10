package com.stocktracker.servicelayer.tradeit.apiresults;

/**
 * This class contains the results from calling TradeIt to obtain the userId and userToken using the oAuthVerifier.
 */
public class GetOAuthAccessTokenAPIResult extends TradeItAPIResult
{
    private String userId;
    private String userToken;

    public String getUserId()
    {
        return userId;
    }

    public void setUserId( String userId )
    {
        this.userId = userId;
    }

    public String getUserToken()
    {
        return userToken;
    }

    public void setUserToken( String userToken )
    {
        this.userToken = userToken;
    }


    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "GetOAuthAccessTokenAPIResult{" );
        sb.append( "userId='" ).append( userId ).append( '\'' );
        sb.append( ", userToken='" ).append( userToken ).append( '\'' );
        sb.append( ", super={" ).append( super.toString() ).append( '}' );
        sb.append( '}' );
        return sb.toString();
    }
}
