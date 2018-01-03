package com.stocktracker.servicelayer.tradeit;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.entity.AccountEntity;
import com.stocktracker.servicelayer.service.AccountService;
import com.stocktracker.servicelayer.service.CustomerService;
import com.stocktracker.weblayer.dto.AccountDTO;
import com.stocktracker.weblayer.dto.tradeit.Authenticate;
import com.stocktracker.weblayer.dto.tradeit.AuthenticateDTO;
import com.stocktracker.weblayer.dto.tradeit.Brokers;
import com.stocktracker.weblayer.dto.tradeit.GetOAuthAccessToken;
import com.stocktracker.weblayer.dto.tradeit.GetOAuthAccessTokenDTO;
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
    private static final String USER_ID_TAG = "<USER_ID>";
    private static final String USER_TOKEN_TAG = "<USER_TOKEN>";
    private static final String OAUTH_VERIFIER_TAG = "<OAUTHVERIFIER>";
    private static final String GET_BROKER_LIST = BASE_URL + "/preference/getBrokerList"
                                                           + "?apiKey=" + KEY_TAG;
    private static final String REQUEST_OAUTH_POPUP_URL = BASE_URL + "/user/getOAuthLoginPopupUrlForWebApp"
                                                                   + "?apiKey=" + KEY_TAG +
                                                                     "&broker=" + BROKER_TAG;
    private static final String GET_OAUTH_ACCESS_TOKEN_URL = BASE_URL + "/user/getOAuthAccessToken"
                                                                      + "?apiKey=" + KEY_TAG
                                                                      + "&oAuthVerifier=" + OAUTH_VERIFIER_TAG;
    private static final String AUTHENTICATE_URL = BASE_URL + "/user/authenticate"
                                                            + "?srv="
                                                            + "&apiKey=" + KEY_TAG
                                                            + "&userId=" + USER_ID_TAG
                                                            + "&userToken=" + USER_TOKEN_TAG;
    private static final Logger logger = LoggerFactory.getLogger( TradeItService.class );

    @Autowired
    private TradeItProperties tradeItProperties;

    private CustomerService customerService;
    private AccountService accountService;


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
     * @param customerId
     * @param broker
     * @param oAuthVerifier
     * @return instance of GetOAuthAccessToken which contains the AccountDTO that was created if the TradeIt
     * getOAuthAccessToken was successful.
     */
    public GetOAuthAccessTokenDTO getOAuthAccessToken( final int customerId,
                                                       @NotNull final String broker,
                                                       @NotNull final String accountName,
                                                       @NotNull final String oAuthVerifier )
    {
        final String methodName = "getOAuthAccessToken";
        logMethodBegin( methodName, customerId, broker, accountName, oAuthVerifier );
        Objects.requireNonNull( broker, "broker cannot be null" );
        Objects.requireNonNull( accountName, "accountName cannot be null" );
        Objects.requireNonNull( oAuthVerifier, "oAuthVerifier cannot be null" );
        checkProperties();
        String url = this.replaceKeyTag( GET_OAUTH_ACCESS_TOKEN_URL ).replace( OAUTH_VERIFIER_TAG, oAuthVerifier );
        logDebug( methodName, "url: {0}", url );
        RestTemplate restTemplate = new RestTemplate();
        GetOAuthAccessToken getOAuthAccessToken = restTemplate.getForObject( url, GetOAuthAccessToken.class );
        AccountDTO accountDTO = null;
        if ( getOAuthAccessToken.isSuccessful() )
        {
            accountDTO = this.accountService.createAccount( customerId, broker, accountName,
                                                            getOAuthAccessToken.getUserId(),
                                                            getOAuthAccessToken.getUserToken() );
        }
        GetOAuthAccessTokenDTO getOAuthAccessTokenDTO = new GetOAuthAccessTokenDTO( getOAuthAccessToken );
        getOAuthAccessTokenDTO.setCustomerAccount( accountDTO );
        logMethodEnd( methodName, getOAuthAccessTokenDTO );
        return getOAuthAccessTokenDTO;
    }

    /**
     * Using the {@code userId} and {@code userToken} from {@code getOAuthAccessToken}, these values will be authenticated
     * with TradeIt and if success, a 15 minute session token will be obtained.
     * @param customerId
     * @param accountId
     * @return
     */
    public AuthenticateDTO authenticate( final int customerId, final int accountId )
    {
        final String methodName = "authenticate";
        logMethodBegin( methodName, customerId, accountId );
        checkProperties();
        AccountEntity accountEntity = this.accountService.getAccountEntity( customerId, accountId );
        String url = this.replaceKeyTag( AUTHENTICATE_URL )
                         .replace( USER_ID_TAG, accountEntity.getUserId() )
                         .replace( USER_TOKEN_TAG, accountEntity.getUserToken() );
        logDebug( methodName, "url: {0}", url );
        RestTemplate restTemplate = new RestTemplate();
        Authenticate authenticate = restTemplate.getForObject( url, Authenticate.class );
        AuthenticateDTO authenticateDTO = new AuthenticateDTO( authenticate );
        logMethodEnd( methodName, authenticateDTO );
        return authenticateDTO;
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

    @Autowired
    public void setCustomerService( final CustomerService customerService )
    {
        this.customerService = customerService;
    }

    @Autowired
    public void setAccountService( final AccountService accountService )
    {
        this.accountService = accountService;
    }
}
