package com.stocktracker.weblayer.controllers;

import com.stocktracker.servicelayer.tradeit.TradeItService;
import com.stocktracker.weblayer.dto.tradeit.Authenticate;
import com.stocktracker.weblayer.dto.tradeit.Brokers;
import com.stocktracker.weblayer.dto.tradeit.OAuthAccessToken;
import com.stocktracker.weblayer.dto.tradeit.RequestOAuthPopUpURLDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * TradeIt REST Controller
 */
@RestController
@CrossOrigin
public class TradeItController extends AbstractController
{
    private static final String CONTEXT_URL = "/tradeIt";
    private TradeItService tradeItService;

    /**
     * Cache this result as it should hardly change
     */
    private Brokers brokersDTO;

    /**
     * Get all of the stock to buy for a customer
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/brokers",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public Brokers getBrokers()
    {
        final String methodName = "getBrokers";
        logMethodBegin( methodName );
        if ( this.brokersDTO == null )
        {
            logDebug( methodName, "Brokers not cached, retrieving now" );
            this.brokersDTO = this.tradeItService.getBrokers();
        }
        else
        {
            logDebug( methodName, "returning cached broker list");
        }
        logMethodEnd( methodName );
        return this.brokersDTO;
    }

    /**
     * Request the URL to authenticate the user's login to their broker's account.
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/requestOAuthPopUpURL/broker/{broker}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public RequestOAuthPopUpURLDTO getRequestOAuthPopUpURL( @PathVariable final String broker )
    {
        final String methodName = "getRequestOAuthPopUpURL";
        logMethodBegin( methodName );
        RequestOAuthPopUpURLDTO requestOAuthPopUpURLDTO = this.tradeItService.requestOAuthPopUpURL( broker );
        logMethodEnd( methodName, requestOAuthPopUpURLDTO );
        return requestOAuthPopUpURLDTO;
    }

    /**
     * After obtaining an OAuth Verifier when the user successfully logged into their broker account, the oAuthVerifier
     * token returned from that process is then used to get the user id and user token to be used for later authentication.
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/getOAuthAccessToken/oAuthVerifier/{oAuthVerifier}",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public OAuthAccessToken getOAuthAccessToken( @PathVariable final String oAuthVerifier )
    {
        final String methodName = "getOAuthAccessToken";
        logMethodBegin( methodName );
        OAuthAccessToken oAuthAccessTokenDTO = this.tradeItService.getOAuthAccessToken( oAuthVerifier );
        logMethodEnd( methodName, oAuthAccessTokenDTO );
        return oAuthAccessTokenDTO;
    }

    /**
     * Using the {@code OAuthAcccessTokenDTO} from {@code getOAuthAccessToken}, authenticate the user
     * @param oAuthAccessTokenDTO
     * @return Authenticate - contains the session token and standard TradeIt results.
     */
    @RequestMapping( value = CONTEXT_URL + "/authenticate",
                     method = RequestMethod.GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public Authenticate authenticate( @RequestBody final OAuthAccessToken oAuthAccessTokenDTO )
    {
        final String methodName = "authenticate";
        logMethodBegin( methodName );
        Authenticate authenticateDTO = null;// this.tradeItService.authenticate( oAuthAccessTokenDTO );
        logMethodEnd( methodName, oAuthAccessTokenDTO );
        return authenticateDTO;
    }

    @Autowired
    public void setTradeItService( final TradeItService tradeItService )
    {
        this.tradeItService = tradeItService;
    }
}
