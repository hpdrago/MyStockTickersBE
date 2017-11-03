package com.stocktracker.servicelayer.tradeit;

/**
 * Created by michael.earl on 4/14/2017.
 */
public enum TradeItErrorCode
{
    SYSTEM_ERROR( 100, "System Error", "System Error" ),
    CONCURRENT_AUTHENTICATION_ERROR( 101, "Concurrent Authentication Error", "Triggered when we are currently processing a login for a user and second request for the same user comes in." ),
    BROKER_EXECUTION_ERROR( 200, "Broker Execution Error", "User should modify the input for the trade request" ),
    BROKER_AUTHENTICATION_ERROR( 300, "Broker Authentication Error", "Authentication info is incorrect or the user may have changed their login information and the oAuth token is no longer valid." ),
    TOO_MANY_LOGIN_ATTEMPTS_ERROR( 301, "Too Many Login Attempts Error", "After 3 invalid login attempts in a row, the user IP will be blocked from TradeIt servers for a duration of 5 minutes." ),
    BROKER_ACCOUNT_ERROR( 400, "Broker Account Error", "User credentials are valid, but needs to take action on the brokers site (ie. sign exchange agreement, sign margin agreement." ),
    PARAMS_ERROR( 500, "Parameters Error", "Publisher should check the parameters being passed in." ),
    SESSION_EXPIRED_ERROR( 600, "Session Expired", "Publisher should call authenticate again in order to generate a new session token." ),
    TOKEN_INVALID_OR_EXPIRED_ERROR( 700, "Token invalid or expired", "Publisher should call oAuthUpdate in order to refresh the token." );

    private int errorNumber;
    private String errorTitle;
    private String errorMessage;

    TradeItErrorCode( final int errorNumber, final String errorTitle, final String errorMessage )
    {
        this.errorNumber = errorNumber;
        this.errorTitle = errorTitle;
        this.errorMessage = errorMessage;
    }

    public boolean isSystemError() { return this == SYSTEM_ERROR; }
    public boolean isConcurrentAuthenticationError() { return this == CONCURRENT_AUTHENTICATION_ERROR; }
    public boolean isBrokerExecutionError() { return this == BROKER_EXECUTION_ERROR; }
    public boolean isBrokerAuthenticationError() { return this == BROKER_AUTHENTICATION_ERROR; }
    public boolean isTooManyLoginAttemptsError() { return this == TOO_MANY_LOGIN_ATTEMPTS_ERROR; }
    public boolean isBrokerAccountError() { return this == BROKER_ACCOUNT_ERROR; }
    public boolean isParametersError() { return this == PARAMS_ERROR; }
    public boolean isSessionExpired() { return this == SESSION_EXPIRED_ERROR; }
    public boolean isTokenInvalidOrExpired() { return this == TOKEN_INVALID_OR_EXPIRED_ERROR; }
}
