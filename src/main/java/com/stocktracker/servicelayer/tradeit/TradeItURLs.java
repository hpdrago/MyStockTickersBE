package com.stocktracker.servicelayer.tradeit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * This class encapsulates the handling of the URLs to make TradeIt REST calls.
 * All of the URLs and substitution tags are defined here along with user friendly method to return a complete URL
 * which can be used to make the API call to TradeIt.
 */
@Service
public class TradeItURLs
{
    public static final String SRV_TAG = "<SRV_KEY>";
    private static final String BASE_URL = "https://ems.qa.tradingticket.com/api/v1";
    private static final String GET_BROKER_LIST = BASE_URL + "/preference/getBrokerList";
    private static final String REQUEST_OAUTH_POPUP_URL = BASE_URL + "/user/getOAuthLoginPopupUrlForWebApp";
    private static final String GET_OAUTH_ACCESS_TOKEN_URL = BASE_URL + "/user/getOAuthAccessToken";
    private static final String UPDATE_OAUTH_ACCESS_TOKEN_URL = BASE_URL + "/user/getOAuthLoginPopupUrlForTokenUpdate";
    private static final String AUTHENTICATE_URL = BASE_URL + "/user/authenticate" + "?srv=" + SRV_TAG;
    private static final String ANSWER_SECURITY_QUESTION_URL = BASE_URL + "/user/answerSecurityQuestion" + "?srv=" + SRV_TAG;
    private static final String CLOSE_SESSION_URL = BASE_URL + "/user/closeSession";
    private static final String KEEP_SESSION_ALIVE_URL = BASE_URL + "/user/keepSessionAlive";
    private static final String GET_ACCOUNT_OVERVIEW_URL = BASE_URL + "/balance/getAccountOverv";
    private static final String GET_POSITIONS_URL = BASE_URL + "/position/getPositions";

    @Autowired
    private TradeItProperties tradeItProperties;

    /**
     * Get the URL for to authenticate a user's account.
     * @param srv
     * @return
     */
    public String getAuthenticateUrl( final String srv )
    {
        Objects.requireNonNull( srv, "srv cannot be null" );
        return this.AUTHENTICATE_URL.replace( SRV_TAG, srv );
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
     * @return
     */
    public String getOauthAccessTokenURL()
    {
        return GET_OAUTH_ACCESS_TOKEN_URL;
    }

    /**
     * Get the OAuthVerifier URL.
     * @return
     */
    public String getUpdateOauthAccessTokenUrl()
    {
        return UPDATE_OAUTH_ACCESS_TOKEN_URL;
    }

    /**
     * @return The URL to use to supply the answer to a security question as a result of authenticating.
     */
    public String getAnswerSecurityQuestionURL( final String srv )
    {
        Objects.requireNonNull( srv, "srv cannot be null" );
        return ANSWER_SECURITY_QUESTION_URL.replace( SRV_TAG, srv );
    }

    /**
     * Get the close session URL.
     * @return
     */
    public String getCloseSessionURL() { return CLOSE_SESSION_URL; }

    /**
     * Get the keep session alive URL.
     * @return
     */
    public String getKeepSessionAliveURL()
    {
        return KEEP_SESSION_ALIVE_URL;
    }

    /**
     * Get the account overview URL
     * @return
     */
    public String getGetAccountOverviewURL()
    {
        return GET_ACCOUNT_OVERVIEW_URL;
    }

    /**
     * Get the get positions URL
     * @return
     */
    public String getGetPositionsURL()
    {
        return GET_POSITIONS_URL;
    }
}
