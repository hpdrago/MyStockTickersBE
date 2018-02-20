package com.stocktracker.servicelayer.tradeit.apiresults;

import com.stocktracker.servicelayer.tradeit.TradeItErrorCode;

import java.util.Arrays;

/**
 * This is the base class for all TradeItAPI calls.  It contains the common result values.
 */
public class TradeItAPIResult
{
    private int code;
    /**
     * The error message is set based on the code value if non-zero.
     */
    private String errorMessage;
    private String status;
    private String token;
    private String shortMessage;
    private String[] longMessages;

    public TradeItAPIResult()
    {
    }

    public TradeItAPIResult( final TradeItAPIResult results )
    {
        this.setResults( results );
    }

    /**
     * Sets the result values from the other {@code result} instance.
     * @param results
     */
    public void setResults( final TradeItAPIResult results )
    {
        this.setCode( results.getCode() );
        this.status = results.getStatus();
        this.token = results.getToken();
        this.shortMessage = results.getShortMessage();
        this.longMessages = results.getLongMessages();
        this.errorMessage = results.getErrorMessage();
    }

    public boolean isSuccessful() { return this.status == null ? false : this.getAPIResultStatus().isSuccess() ;}
    public boolean isInformationNeeded() { return this.status == null ? false : this.getAPIResultStatus().isInformationNeeded() ;}
    public boolean isAuthenticationRequired() { return this.status == null ? false :
                                                       this.code == TradeItErrorCode.SESSION_EXPIRED_ERROR.getErrorNumber(); }

    public int getCode()
    {
        return code;
    }

    /**
     * Sets the code and error message based on the error code.
     * @param code
     */
    public void setCode( final int code )
    {
        this.code = code;
        if ( code == 0 )
        {
            this.errorMessage = "";
        }
        else
        {
            this.errorMessage = TradeItErrorCode.getErrorMessage( code );
        }
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public void setErrorMessage( final String errorMessage )
    {
        this.errorMessage = errorMessage;
    }


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

    public String[] getLongMessages()
    {
        return longMessages;
    }

    public void setStatus( String status )
    {
        this.status = status;
    }

    public void setToken( String token )
    {
        this.token = token;
    }

    public void setShortMessage( String shortMessage )
    {
        this.shortMessage = shortMessage;
    }

    public void setLongMessages( String[] longMessages )
    {
        this.longMessages = longMessages;
    }

    public TradeItAPIResultStatus getAPIResultStatus()
    {
        return TradeItAPIResultStatus.valueOf( this.status );
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "TradeItResult{" );
        sb.append( "status='" ).append( status ).append( '\'' );
        sb.append( ", code='" ).append( code ).append( '\'' );
        sb.append( ", errorMessage='" ).append( errorMessage ).append( '\'' );
        sb.append( ", token='" ).append( token ).append( '\'' );
        sb.append( ", shortMessage='" ).append( shortMessage ).append( '\'' );
        sb.append( ", longMessages='" ).append( Arrays.toString( longMessages )).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }
}
