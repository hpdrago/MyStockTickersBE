package com.stocktracker.weblayer.controllers;

import com.stocktracker.common.exceptions.LinkedAccountNotFoundException;
import com.stocktracker.servicelayer.tradeit.TradeItService;
import com.stocktracker.weblayer.dto.tradeit.AnswerSecurityQuestionDTO;
import com.stocktracker.weblayer.dto.tradeit.AuthenticateDTO;
import com.stocktracker.weblayer.dto.tradeit.CloseSessionDTO;
import com.stocktracker.weblayer.dto.tradeit.GetBrokersDTO;
import com.stocktracker.weblayer.dto.tradeit.GetOAuthAccessTokenDTO;
import com.stocktracker.weblayer.dto.tradeit.GetOAuthAccessTokenUpdateURLDTO;
import com.stocktracker.weblayer.dto.tradeit.KeepSessionAliveDTO;
import com.stocktracker.weblayer.dto.tradeit.RequestOAuthPopUpURLDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

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
    private GetBrokersDTO getBrokersDTO;

    /**
     * Get all of the stock to buy for a customer
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/brokers",
                     method = GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public GetBrokersDTO getBrokers()
    {
        final String methodName = "getBrokers";
        logMethodBegin( methodName );
        if ( this.getBrokersDTO == null || getBrokersDTO.brokerCount() == 0 )
        {
            logDebug( methodName, "GetBrokersAPIResult not cached, retrieving now" );
            this.getBrokersDTO = this.tradeItService.getBrokers();
        }
        else
        {
            logDebug( methodName, "returning cached broker list");
        }
        logMethodEnd( methodName );
        return this.getBrokersDTO;
    }

    /**
     * Request the URL to authenticate the user's login to their broker's account.
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/requestOAuthPopUpURL/broker/{broker}",
                     method = GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public RequestOAuthPopUpURLDTO getRequestOAuthPopUpURL( @PathVariable final String broker )
    {
        final String methodName = "getRequestOAuthPopUpURL";
        logMethodBegin( methodName );
        final RequestOAuthPopUpURLDTO requestOAuthPopUpURLDTO = this.tradeItService.requestOAuthPopUpURL( broker );
        logMethodEnd( methodName, requestOAuthPopUpURLDTO );
        return requestOAuthPopUpURLDTO;
    }

    /**
     * After obtaining an OAuth Verifier when the user successfully logged into their broker account, the oAuthVerifier
     * token returned from that process is then used to get the user id and user token to be used for later authentication.
     * @return GetOAuthAccessTokenDTO that contains the newly created account
     */
    @RequestMapping( value = CONTEXT_URL + "/getOAuthAccessToken"
                                         + "/customerId/{customerId}"
                                         + "/broker/{broker}"
                                         + "/accountName/{accountName}"
                                         + "/oAuthVerifier/{oAuthVerifier}",
                     method = GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public GetOAuthAccessTokenDTO getOAuthAccessToken( @PathVariable final int customerId,
                                                       @PathVariable final String broker,
                                                       @PathVariable final String accountName,
                                                       @PathVariable final String oAuthVerifier )
    {
        final String methodName = "getOAuthAccessToken";
        logMethodBegin( methodName, customerId, oAuthVerifier );
        final GetOAuthAccessTokenDTO getOAuthAccessTokenDTO = this.tradeItService.getOAuthAccessToken( customerId, broker,
                                                                                                       accountName,
                                                                                                       oAuthVerifier );
        logMethodEnd( methodName, getOAuthAccessTokenDTO );
        return getOAuthAccessTokenDTO;
    }

    /**
     * After obtaining an OAuth Verifier when the user successfully logged into their broker account, the oAuthVerifier
     * token returned from that process is then used to get the user id and user token to be used for later authentication.
     * @return GetOAuthAccessTokenDTO that contains the newly created account
     */
    @RequestMapping( value = CONTEXT_URL + "/getOAuthTokenUpdateURL"
                             + "/accountId/{accountId}"
                             + "/customerId/{customerId}",
                     method = GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public GetOAuthAccessTokenUpdateURLDTO getOAuthTokenUpdateURL( @PathVariable final int customerId,
                                                                   @PathVariable final int accountId )
    {
        final String methodName = "getOAuthTokenUpdateURL";
        logMethodBegin( methodName, customerId, accountId );
        final GetOAuthAccessTokenUpdateURLDTO getOAuthAccessTokenUpdateURLDTO = this.tradeItService.getOAuthTokenUpdateURL( customerId, accountId );
        logMethodEnd( methodName, getOAuthAccessTokenUpdateURLDTO );
        return getOAuthAccessTokenUpdateURLDTO;
    }

    /**
     * Using the {@code OAuthAccessTokenDTO} from {@code getOAuthAccessToken}, authenticate the user
     * @param customerId
     * @param accountId
     * @return AuthenticateAPICall - contains the session token and standard TradeIt results.
     */
    @RequestMapping( value = CONTEXT_URL + "/authenticate/"
                                         + "/accountId/{accountId}"
                                         + "/customerId/{customerId}",
                     method = GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public AuthenticateDTO authenticate( @PathVariable final int customerId,
                                         @PathVariable final int accountId )
        throws LinkedAccountNotFoundException
    {
        final String methodName = "authenticate";
        logMethodBegin( methodName, accountId, customerId );
        final AuthenticateDTO authenticateDTO = this.tradeItService.authenticate( customerId, accountId );
        logMethodEnd( methodName, authenticateDTO );
        return authenticateDTO;
    }

    /**
     * This method is called to validate the user's answer to the security question.  The security question is prompted
     * after a call to authenticate if the broker requires further authentication.
     * @param customerId
     * @param accountId
     * @param answer The user's answer to the question
     * @return AuthenticateAPICall - contains the session token and standard TradeIt results.
     */
    @RequestMapping( value = CONTEXT_URL + "/authenticate/"
                                         + "/accountId/{accountId}"
                                         + "/customerId/{customerId}",
                     method = RequestMethod.POST,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public AnswerSecurityQuestionDTO answerSecurityQuestion( @PathVariable final int customerId,
                                                             @PathVariable final int accountId,
                                                             @RequestBody final String answer )
        throws LinkedAccountNotFoundException
    {
        final String methodName = "answerSecurityQuestion";
        logMethodBegin( methodName, customerId, accountId, answer );
        final AnswerSecurityQuestionDTO answerSecurityQuestionDTO = this.tradeItService
                                                                        .answerSecurityQuestion( customerId, accountId,
                                                                                                 answer );
        logMethodEnd( methodName, answerSecurityQuestionDTO );
        return answerSecurityQuestionDTO;
    }

    /**
     * Calls TradeIt to keep the customer's session alive for the account.
     * @param customerId
     * @param accountId
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/keepSessionAlive/"
                             + "/accountId/{accountId}"
                             + "/customerId/{customerId}",
                     method = GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public KeepSessionAliveDTO keepSessionAlive( @PathVariable final int customerId,
                                                 @PathVariable final int accountId )
    {
        final String methodName = "keepSessionAlive";
        logMethodBegin( methodName, accountId, customerId );
        final KeepSessionAliveDTO keepSessionAliveDTO = this.tradeItService.keepSessionAlive( customerId, accountId );
        logMethodEnd( methodName, keepSessionAliveDTO );
        return keepSessionAliveDTO;
    }

    /**
     * Calls TradeIt to close the customer's session for the account.
     * @param customerId
     * @param accountId
     * @return
     */
    @RequestMapping( value = CONTEXT_URL + "/closeSession/"
                             + "/accountId/{accountId}"
                             + "/customerId/{customerId}",
                     method = GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public CloseSessionDTO closeSession( @PathVariable final int customerId,
                                         @PathVariable final int accountId )
    {
        final String methodName = "closeSession";
        logMethodBegin( methodName, accountId, customerId );
        final CloseSessionDTO closeSessionDTO = this.tradeItService.closeSession( customerId, accountId );
        logMethodEnd( methodName, closeSessionDTO );
        return closeSessionDTO;
    }

    @Autowired
    public void setTradeItService( final TradeItService tradeItService )
    {
        this.tradeItService = tradeItService;
    }
}
