package com.stocktracker.weblayer.controllers;

import com.fasterxml.uuid.impl.UUIDUtil;
import com.stocktracker.common.exceptions.CustomerNotFoundException;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.LinkedAccountNotFoundException;
import com.stocktracker.common.exceptions.NotAuthorizedException;
import com.stocktracker.common.exceptions.TradeItAccountNotFoundException;
import com.stocktracker.common.exceptions.TradeItAuthenticationException;
import com.stocktracker.servicelayer.service.TradeItAccountEntityService;
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

import java.util.UUID;

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
    private TradeItAccountEntityService tradeItAccountEntityService;

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
     * @throws EntityVersionMismatchException
     * @throws CustomerNotFoundException
     * @throws NotAuthorizedException
     */
    @RequestMapping( value = CONTEXT_URL + "/getOAuthAccessToken"
                                         + "/customerId/{customerId}"
                                         + "/broker/{broker}"
                                         + "/accountName/{accountName}"
                                         + "/oAuthVerifier/{oAuthVerifier}",
                     method = GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public GetOAuthAccessTokenDTO getOAuthAccessToken( @PathVariable final String customerId,
                                                       @PathVariable final String broker,
                                                       @PathVariable final String accountName,
                                                       @PathVariable final String oAuthVerifier )
        throws EntityVersionMismatchException,
               CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "getOAuthAccessToken";
        logMethodBegin( methodName, customerId, oAuthVerifier );
        final UUID customerUuid = this.validateCustomerId( customerId );
        this.customerEntityService.getCustomerEntity( customerUuid );
        final GetOAuthAccessTokenDTO getOAuthAccessTokenDTO = this.tradeItService
                                                                  .getOAuthAccessToken( customerUuid,
                                                                                        broker,
                                                                                        accountName,
                                                                                        oAuthVerifier );
        logMethodEnd( methodName, getOAuthAccessTokenDTO );
        return getOAuthAccessTokenDTO;
    }

    /**
     * After obtaining an OAuth Verifier when the user successfully logged into their broker account, the oAuthVerifier
     * token returned from that process is then used to get the user id and user token to be used for later authentication.
     * @return GetOAuthAccessTokenDTO that contains the newly created account
     * @throws TradeItAccountNotFoundException
     * @throws TradeItAuthenticationException
     * @throws EntityVersionMismatchException
     * @throws CustomerNotFoundException
     * @throws NotAuthorizedException
     */
    @RequestMapping( value = CONTEXT_URL + "/getOAuthTokenUpdateURL"
                             + "/accountId/{accountId}"
                             + "/customerId/{customerId}",
                     method = GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public GetOAuthAccessTokenUpdateURLDTO getOAuthTokenUpdateURL( @PathVariable final String customerId,
                                                                   @PathVariable final String accountId )
        throws TradeItAccountNotFoundException,
               TradeItAuthenticationException,
               EntityVersionMismatchException,
               CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "getOAuthTokenUpdateURL";
        logMethodBegin( methodName, customerId, accountId );
        this.validateCustomerId( customerId );
        final GetOAuthAccessTokenUpdateURLDTO getOAuthAccessTokenUpdateURLDTO =
            this.tradeItService
                .getOAuthTokenUpdateURL( UUIDUtil.uuid( accountId ));
        logMethodEnd( methodName, getOAuthAccessTokenUpdateURLDTO );
        return getOAuthAccessTokenUpdateURLDTO;
    }

    /**
     * Using the {@code OAuthAccessTokenDTO} from {@code getOAuthAccessToken}, authenticate the user
     * @param customerId
     * @param accountId
     * @return AuthenticateAPICall - contains the session token and standard TradeIt results.
     * @throws TradeItAccountNotFoundException
     * @throws LinkedAccountNotFoundException
     * @throws TradeItAuthenticationException
     * @throws EntityVersionMismatchException
     * @throws CustomerNotFoundException
     * @throws NotAuthorizedException
     */
    @RequestMapping( value = CONTEXT_URL + "/authenticate/"
                                         + "/accountId/{accountId}"
                                         + "/customerId/{customerId}",
                     method = GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public AuthenticateDTO authenticate( @PathVariable final String customerId,
                                         @PathVariable final String accountId )
        throws TradeItAccountNotFoundException,
               TradeItAuthenticationException,
               EntityVersionMismatchException,
               LinkedAccountNotFoundException,
               CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "authenticate";
        logMethodBegin( methodName, accountId, customerId );
        this.validateCustomerId( customerId );
        final AuthenticateDTO authenticateDTO = this.tradeItService
                                                    .authenticate( UUIDUtil.uuid( accountId ) );
        /*
         * Synchronize the linked accounts identified by TradeIt with the linked account table.
         */
        if ( authenticateDTO.isSuccessful() )
        {
            this.tradeItAccountEntityService
                .synchronizeTradeItAccount( UUIDUtil.uuid( accountId ), authenticateDTO );
        }
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
     * @throws LinkedAccountNotFoundException
     * @throws TradeItAccountNotFoundException
     * @throws TradeItAuthenticationException
     * @throws EntityVersionMismatchException
     * @throws CustomerNotFoundException
     * @throws NotAuthorizedException
     */
    @RequestMapping( value = CONTEXT_URL + "/authenticate/"
                                         + "/accountId/{accountId}"
                                         + "/customerId/{customerId}",
                     method = RequestMethod.POST,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public AnswerSecurityQuestionDTO answerSecurityQuestion( @PathVariable final String customerId,
                                                             @PathVariable final String accountId,
                                                             @RequestBody final String answer )
        throws LinkedAccountNotFoundException,
               TradeItAccountNotFoundException,
               TradeItAuthenticationException,
               EntityVersionMismatchException,
               CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "answerSecurityQuestion";
        logMethodBegin( methodName, customerId, accountId, answer );
        this.validateCustomerId( customerId );
        final AnswerSecurityQuestionDTO answerSecurityQuestionDTO = this.tradeItService
                                                                        .answerSecurityQuestion( UUIDUtil.uuid( accountId ),
                                                                                                 answer );
        logMethodEnd( methodName, answerSecurityQuestionDTO );
        return answerSecurityQuestionDTO;
    }

    /**
     * Calls TradeIt to keep the customer's session alive for the account.
     * @param customerId
     * @param tradeItAccountId
     * @return
     * @throws TradeItAccountNotFoundException
     * @throws TradeItAuthenticationException
     * @throws LinkedAccountNotFoundException
     * @throws EntityVersionMismatchException
     * @throws NotAuthorizedException
     */
    @RequestMapping( value = CONTEXT_URL + "/keepSessionAlive/"
                             + "/tradeItAccountId/{tradeItAccountId}"
                             + "/customerId/{customerId}",
                     method = GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public KeepSessionAliveDTO keepSessionAlive( @PathVariable final String customerId,
                                                 @PathVariable final String tradeItAccountId )
        throws TradeItAccountNotFoundException,
               TradeItAuthenticationException,
               EntityVersionMismatchException,
               CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "keepSessionAlive";
        logMethodBegin( methodName, customerId, tradeItAccountId );
        final UUID customerUuid = this.validateCustomerId( customerId );
        final KeepSessionAliveDTO keepSessionAliveDTO = this.tradeItService
                                                            .keepSessionAlive( customerUuid );
        /*
         * Synchronize the linked accounts identified by TradeIt with the linked account table.
         */
        /*
        if ( keepSessionAliveDTO.isSuccessful() )
        {
            this.tradeItAccountEntityService
                .synchronizeTradeItAccount( customerId, tradeItAccountId, keepSessionAliveDTO );
        }
        */
        logMethodEnd( methodName, keepSessionAliveDTO );
        return keepSessionAliveDTO;
    }

    /**
     * Calls TradeIt to close the customer's session for the account.
     * @param customerId
     * @param accountId
     * @return
     * @throws TradeItAccountNotFoundException
     * @throws CustomerNotFoundException
     * @throws NotAuthorizedException
     */
    @RequestMapping( value = CONTEXT_URL + "/closeSession/"
                             + "/accountId/{accountId}"
                             + "/customerId/{customerId}",
                     method = GET,
                     produces = {MediaType.APPLICATION_JSON_VALUE} )
    public CloseSessionDTO closeSession( @PathVariable final String customerId,
                                         @PathVariable final String accountId )
        throws TradeItAccountNotFoundException,
               CustomerNotFoundException,
               NotAuthorizedException
    {
        final String methodName = "closeSession";
        logMethodBegin( methodName, accountId, customerId );
        this.validateCustomerId( customerId );
        final CloseSessionDTO closeSessionDTO = this.tradeItService
                                                    .closeSession( UUIDUtil.uuid( accountId ));
        logMethodEnd( methodName, closeSessionDTO );
        return closeSessionDTO;
    }

    @Autowired
    public void setTradeItService( final TradeItService tradeItService )
    {
        this.tradeItService = tradeItService;
    }

    @Autowired
    public void setTradeItAccountEntityService( final TradeItAccountEntityService tradeItAccountEntityService )
    {
        this.tradeItAccountEntityService = tradeItAccountEntityService;
    }

}
