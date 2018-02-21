package com.stocktracker.servicelayer.tradeit;

/**
 * This enum contains all of the TradeIt API Parameters.  Each enum value contains the parameter name that TradeIt is
 * expecting.
 */
public enum TradeItParameter
{
    BROKER_PARAM( "broker" ),
    TOKEN_PARAM( "token" ),
    ACCOUNT_NUMBER_PARAM( "accountNumber" ),
    USER_ID_PARAM( "userId" ),
    USER_TOKEN_PARAM( "userToken" ),
    SECURITY_ANSWER_PARAM( "securityAnswer" ),
    API_KEY_PARAM( "apiKey" ),
    OAUTH_VERIFIER_PARAM( "oAuthVerifier" ),
    AUTH_UUID( "AuthUUID" );

    /**
     * This is the name that TradeIt is expecting.
     */
    final String tradeItParameterName;
    TradeItParameter( final String tradeItParameterName )
    {
        this.tradeItParameterName = tradeItParameterName;
    }

    /**
     * Get the TradeIt parameter name.
     * @return
     */
    public String getTradeItParameterName()
    {
        return tradeItParameterName;
    }
}
