package com.stocktracker.servicelayer.tradeit;

import com.stocktracker.common.MyLogger;
import com.stocktracker.weblayer.dto.tradeit.Brokers;
import com.stocktracker.weblayer.dto.tradeit.OAuthAccessToken;
import com.stocktracker.weblayer.dto.tradeit.RequestOAuthPopUpURLDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * This service contains all of the REST calls to TradeIt
 */
@Service
public class TradeItService implements MyLogger
{
    private static final String BASE_URL = "https://ems.qa.tradingticket.com/api/v1";
    private static final String KEY_TAG = "<API_KEY>";
    private static final String BROKER_TAG = "<BROKER>";
    private static final String OAUTH_VERIFIER_TAG = "<OAUTHVERIFIER>";
    private static final String GET_BROKER_LIST = BASE_URL + "/preference/getBrokerList?apiKey=" + KEY_TAG;
    private static final String REQUEST_OAUTH_POPUP_URL = BASE_URL + "/user/getOAuthLoginPopupUrlForWebApp?apiKey=" + KEY_TAG +
                                                                     "&broker=" + BROKER_TAG;
    private static final String GET_OAUTH_ACCESS_TOKEN_URL = BASE_URL + "/user/getOAuthAccessToken?apiKey=" + KEY_TAG +
                                                                        "&oAuthVerifier=" + OAUTH_VERIFIER_TAG;
    private static final Logger logger = LoggerFactory.getLogger( TradeItService.class );

    @Autowired
    private TradeItProperties tradeItProperties;


    /**
     * Get the list of brokers supported by TradeIt
     * @return
     */
    public Brokers getBrokers()
    {
        final String methodName = "getBrokers";
        logger.debug( methodName + ".begin" );
        checkProperties();
        RestTemplate restTemplate = new RestTemplate();
        Brokers brokers = restTemplate.getForObject( this.replaceKeyTag( GET_BROKER_LIST ), Brokers.class );
        logger.debug( methodName + ".end " + brokers );
        return brokers;
    }

    /**
     * This method will request a URL to be used to popup a authentication window for the user to login to their
     * brokerage account.
     * @param broker
     * @return
     */
    public RequestOAuthPopUpURLDTO requestOAuthPopUpURL( @NotNull final String broker )
    {
        final String methodName = "requestOAuthPopUpURL";
        logMethodBegin( methodName, broker );
        Objects.requireNonNull( broker, "Broker cannot be null" );
        checkProperties();
        String url = this.replaceKeyTag( REQUEST_OAUTH_POPUP_URL ).replace( BROKER_TAG, broker );
        logDebug( methodName, "url: {0}", url );
        RestTemplate restTemplate = new RestTemplate();
        RequestOAuthPopUpURLDTO requestOAuthPopUpURLDTO = restTemplate.getForObject( url, RequestOAuthPopUpURLDTO.class );
        logMethodEnd( methodName, requestOAuthPopUpURLDTO );
        return requestOAuthPopUpURLDTO;
    }

    /**
     * Given the {@code oAuthVerifier} value returned from the user linking their broker account, this method
     * will obtain the user id and user token to authenticate the user to gain access to their broker account.
     * @param oAuthVerifier
     * @return
     */
    public OAuthAccessToken getOAuthAccessToken( @NotNull final String oAuthVerifier )
    {
        final String methodName = "getOAuthAccessToken";
        logMethodBegin( methodName, oAuthVerifier );
        Objects.requireNonNull( oAuthVerifier, "oAuthVerifier cannot be null" );
        checkProperties();
        String url = this.replaceKeyTag( GET_OAUTH_ACCESS_TOKEN_URL ).replace( OAUTH_VERIFIER_TAG, oAuthVerifier );
        logDebug( methodName, "url: {0}", url );
        RestTemplate restTemplate = new RestTemplate();
        OAuthAccessToken oAuthAccessTokenDTO = restTemplate.getForObject( url, OAuthAccessToken.class );
        logMethodEnd( methodName, oAuthAccessTokenDTO );
        return oAuthAccessTokenDTO;
    }

    /*
    public Authenticate authenticate( @NotNull final OAuthAccessToken oAuthAccessTokenDTO )
    {
        final String methodName = "authenticate";
        logMethodBegin( methodName, oAuthAccessTokenDTO );
        Objects.requireNonNull( oAuthAccessTokenDTO, "oAuthAccessTokenDTO cannot be null" );
        checkProperties();
        String url = this.replaceKeyTag( GET_OAUTH_ACCESS_TOKEN_URL ).replace( OAUTH_VERIFIER_TAG, oAuthVerifier );
        logDebug( methodName, "url: {0}", url );
        RestTemplate restTemplate = new RestTemplate();
        OAuthAccessToken oAuthAccessTokenDTO = restTemplate.getForObject( url, OAuthAccessToken.class );
        logMethodEnd( methodName, oAuthAccessTokenDTO );
        return oAuthAccessTokenDTO;
    }
    */

    /**
     * Checks the required properties to be present to make rest calls to TradeIt
     */
    private void checkProperties()
    {
        Objects.requireNonNull( this.tradeItProperties, "TradeItProperties cannot be null" );
        Objects.requireNonNull( this.tradeItProperties.getApiKey(), "TradeItAPIKey cannot be null" );
    }

    /**
     * Adds the apiKey to {@code url}
     * @param url
     * @return
     */
    private String replaceKeyTag( @NotNull final String url )
    {
        Objects.requireNonNull( url, "url cannot be null" );
        if ( !url.contains( KEY_TAG ))
        {
            throw new IllegalArgumentException( "url " + url + " must contain " + KEY_TAG );
        }
        return url.replace( "<API_KEY>", this.tradeItProperties.getApiKey() );
    }
}
