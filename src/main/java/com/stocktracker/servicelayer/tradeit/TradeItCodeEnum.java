package com.stocktracker.servicelayer.tradeit;

/**
 * Created by michael.earl on 4/14/2017.
 */
public enum TradeItCodeEnum
{
    SUCCESS( 0, "SUCCESS", "SUCCESS" ),
    SYSTEM_ERROR( 100, "TradeIt - System Error", "System Error" ),
    CONCURRENT_AUTHENTICATION_ERROR( 101, "TradeIt - Concurrent Authentication Error", "Triggered when we are currently processing a login for a user and second request for the same user comes in." ),
    BROKER_EXECUTION_ERROR( 200, "TradeIt - Broker Execution Error", "User should modify the input for the trade request" ),
    BROKER_AUTHENTICATION_ERROR( 300, "TradeIt - Broker Authentication Error", "Authentication info is incorrect or the user may have changed their login information and the oAuth token is no longer valid." ),
    TOO_MANY_LOGIN_ATTEMPTS_ERROR( 301, "TradeIt - Too Many Login Attempts Error", "After 3 invalid login attempts in a row, the user IP will be blocked from TradeIt servers for a duration of 5 minutes." ),
    BROKER_ACCOUNT_ERROR( 400, "TradeIt - Broker TradeItAccount Error", "User credentials are valid, but needs to take action on the brokers site (ie. sign exchange agreement, sign margin agreement." ),
    PARAMS_ERROR( 500, "TradeIt - Parameters Error", "Publisher should check the parameters being passed in." ),
    SESSION_EXPIRED_ERROR( 600, "TradeIt - Session Expired", "Publisher should call authenticate again in order to generate a new session token." ),
    TOKEN_INVALID_OR_EXPIRED_ERROR( 700, "TradeIt - Token invalid or expired", "Publisher should call oAuthUpdate in order to refresh the token." );

    private int errorNumber;
    private String errorTitle;
    private String errorMessage;

    TradeItCodeEnum( final int errorNumber, final String errorTitle, final String errorMessage )
    {
        this.errorNumber = errorNumber;
        this.errorTitle = errorTitle;
        this.errorMessage = errorMessage;
    }

    public int getErrorNumber() { return this.errorNumber; }
    public String getErrorTitle() { return this.errorTitle; }
    public String getErrorMessage() { return this.errorMessage; }


    public boolean isSuccess() { return this == SUCCESS; }
    public boolean isSystemError() { return this == SYSTEM_ERROR; }
    public boolean isConcurrentAuthenticationError() { return this == CONCURRENT_AUTHENTICATION_ERROR; }
    public boolean isBrokerExecutionError() { return this == BROKER_EXECUTION_ERROR; }
    public boolean isBrokerAuthenticationError() { return this == BROKER_AUTHENTICATION_ERROR; }
    public boolean isTooManyLoginAttemptsError() { return this == TOO_MANY_LOGIN_ATTEMPTS_ERROR; }
    public boolean isBrokerAccountError() { return this == BROKER_ACCOUNT_ERROR; }
    public boolean isParametersError() { return this == PARAMS_ERROR; }
    public boolean isSessionExpired() { return this == SESSION_EXPIRED_ERROR; }
    public boolean isTokenInvalidOrExpired() { return this == TOKEN_INVALID_OR_EXPIRED_ERROR; }

    /**
     * Get the enum for the error code value.
     * @param errorCode
     * @return
     */
    public static String getErrorMessage( final int errorCode )
    {
        TradeItCodeEnum returnValue = null;
        for ( final TradeItCodeEnum value: TradeItCodeEnum.values() )
        {
            if ( value.errorNumber == errorCode )
            {
                returnValue = value;
            }
        }
        if ( returnValue == null )
        {
            throw new IllegalArgumentException( "Error code " + errorCode + " is not a valid error code" );
        }
        return returnValue.errorMessage;
    }

    /**
     * Converts the code value into the enum value.
     * @param code
     * @return
     * @throws IllegalArgumentException if the code cannot be converted to an enum.
     */
    public static TradeItCodeEnum valueOf( final int code )
    {
        TradeItCodeEnum tradeItCodeEnum = null;
        for ( final TradeItCodeEnum errorCode: TradeItCodeEnum.values() )
        {
            if ( errorCode.getErrorNumber() == code )
            {
                tradeItCodeEnum = errorCode;
            }
        }
        if ( tradeItCodeEnum == null )
        {
            throw new IllegalArgumentException( "No enum found in TradeItCodeEnum for code value " + code );
        }
        return tradeItCodeEnum;
    }
}
