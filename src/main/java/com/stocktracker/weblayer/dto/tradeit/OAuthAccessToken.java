package com.stocktracker.weblayer.dto.tradeit;

public class OAuthAccessToken extends TradeItAPIResult
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
        final StringBuilder sb = new StringBuilder( "OAuthAccessToken{" );
        sb.append( "userId='" ).append( userId ).append( '\'' );
        sb.append( ", userToken='" ).append( userToken ).append( '\'' );
        sb.append( ", super={" ).append( super.toString() ).append( '}' );
        sb.append( '}' );
        return sb.toString();
    }

}
