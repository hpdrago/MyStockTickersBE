package com.stocktracker.servicelayer.tradeit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * This class encapsulates the handling of the URLs to make TradeIt REST calls.
 * All of the URLs and substitution tags are defined here along with user friendly method to return a complete URL
 * which can be used to make the API call to TradeIt.
 */
@Service
public class TradeItURLs
{
    private static final String BASE_URL = "https://ems.qa.tradingticket.com/api/v1";
    public static final String SRV_TAG = "<SRV_KEY>";
    private static final String GET_BROKER_LIST = BASE_URL + "/preference/getBrokerList";
    private static final String REQUEST_OAUTH_POPUP_URL = BASE_URL + "/user/getOAuthLoginPopupUrlForWebApp";
    private static final String GET_OAUTH_ACCESS_TOKEN_URL = BASE_URL + "/user/getOAuthAccessToken";
    private static final String AUTHENTICATE_URL = BASE_URL + "/user/authenticate" + "?srv=" + SRV_TAG;

    @Autowired
    private TradeItProperties tradeItProperties;

    /**
     * Get the URL for to authenticate a user's account.
     * @param srv
     * @return
     */
    public String getAuthenticateUrl( final String srv )
    {
        this.checkProperties();
        return this.replaceAPIKeyTag( AUTHENTICATE_URL ).replace( SRV_TAG, srv == null ? "" : srv );
    }

    /**
     * Get the URL to get the broker list
     * @return
     */
    public String getBrokersURL()
    {
        //return this.replaceAPIKeyTag( GET_BROKER_LIST );
        return this.GET_BROKER_LIST;
    }

    /**
     * Get the URL for the request OAuth popup URL.
     * @return
     */
    public String getRequestOauthPopupURL()
    {
        return REQUEST_OAUTH_POPUP_URL;
    }

    /**
     * Get the OAuthVerifier URL.
     * @param oAuthVerifier
     * @return
     */
    public String getOauthAccessTokenURL( final String oAuthVerifier )
    {
        this.checkProperties();
        return GET_OAUTH_ACCESS_TOKEN_URL;
    }

    /**
     * Adds the apiKey to {@code url}
     * @param url
     * @return
     */
    private String replaceAPIKeyTag( @NotNull final String url )
    {
        Objects.requireNonNull( url, "url cannot be null" );
        return url.replace( "<API_KEY>", this.tradeItProperties.getApiKey() );
    }

    /**
     * Checks the required properties to be present to make rest calls to TradeIt
     */
    private void checkProperties()
    {
        Objects.requireNonNull( this.tradeItProperties, "TradeItProperties cannot be null" );
        Objects.requireNonNull( this.tradeItProperties.getApiKey(), "TradeItAPIKey cannot be null" );
    }

    /**
     * Get the API Key.
     * @return
     */
    public String getAPIKey()
    {
        return this.tradeItProperties.getApiKey();
    }
}
