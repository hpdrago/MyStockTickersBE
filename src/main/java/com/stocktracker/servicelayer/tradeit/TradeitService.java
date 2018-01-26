package com.stocktracker.servicelayer.tradeit;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.LinkedAccountNotFoundException;
import com.stocktracker.repositorylayer.entity.TradeItAccountEntity;
import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.servicelayer.service.TradeItAccountEntityService;
import com.stocktracker.servicelayer.tradeit.apicalls.AnswerSecurityQuestionAPICall;
import com.stocktracker.servicelayer.tradeit.apicalls.AuthenticateAPICall;
import com.stocktracker.servicelayer.tradeit.apicalls.CloseSessionAPICall;
import com.stocktracker.servicelayer.tradeit.apicalls.GetAccountOverviewAPICall;
import com.stocktracker.servicelayer.tradeit.apicalls.GetBrokersAPICall;
import com.stocktracker.servicelayer.tradeit.apicalls.GetOAuthAccessTokenAPICall;
import com.stocktracker.servicelayer.tradeit.apicalls.GetPositionsAPICall;
import com.stocktracker.servicelayer.tradeit.apicalls.KeepSessionAliveAPICall;
import com.stocktracker.servicelayer.tradeit.apicalls.RequestOAuthPopUpURLAPICall;
import com.stocktracker.servicelayer.tradeit.apiresults.AnswerSecurityQuestionAPIResult;
import com.stocktracker.servicelayer.tradeit.apiresults.AuthenticateAPIResult;
import com.stocktracker.servicelayer.tradeit.apiresults.CloseSessionAPIResult;
import com.stocktracker.servicelayer.tradeit.apiresults.GetAccountOverViewAPIResult;
import com.stocktracker.servicelayer.tradeit.apiresults.GetPositionsAPIResult;
import com.stocktracker.servicelayer.tradeit.apiresults.KeepSessionAliveAPIResult;
import com.stocktracker.weblayer.dto.TradeItAccountDTO;
import com.stocktracker.servicelayer.tradeit.apiresults.GetBrokersAPIResult;
import com.stocktracker.servicelayer.tradeit.apiresults.GetOAuthAccessTokenAPIResult;
import com.stocktracker.weblayer.dto.tradeit.AnswerSecurityQuestionDTO;
import com.stocktracker.weblayer.dto.tradeit.AuthenticateDTO;
import com.stocktracker.weblayer.dto.tradeit.CloseSessionDTO;
import com.stocktracker.weblayer.dto.tradeit.GetAccountOverviewDTO;
import com.stocktracker.weblayer.dto.tradeit.GetBrokersDTO;
import com.stocktracker.weblayer.dto.tradeit.GetOAuthAccessTokenDTO;
import com.stocktracker.servicelayer.tradeit.apiresults.RequestOAuthPopUpURLAPIResult;
import com.stocktracker.weblayer.dto.tradeit.GetPositionsDTO;
import com.stocktracker.weblayer.dto.tradeit.KeepSessionAliveDTO;
import com.stocktracker.weblayer.dto.tradeit.RequestOAuthPopUpURLDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

/**
 * This service contains all of the REST calls to TradeIt
 */
@Service
public class TradeItService implements MyLogger
{
    private static final Logger logger = LoggerFactory.getLogger( TradeItService.class );

    @Autowired
    private ApplicationContext context;
    private TradeItAccountEntityService tradeItAccountService;

    /**
     * Get the list of brokers supported by TradeIt
     * @return
     */
    public GetBrokersDTO getBrokers()
    {
        final String methodName = "getBrokers";
        logger.debug( methodName + ".begin" );
        final GetBrokersAPICall getBrokersAPICall = this.context.getBean( GetBrokersAPICall.class );
        final GetBrokersAPIResult getBrokersAPIResult = getBrokersAPICall.execute();
        final GetBrokersDTO getBrokersDTO = new GetBrokersDTO( getBrokersAPIResult );
        logger.debug( methodName + ".end " + getBrokersDTO );
        return getBrokersDTO;
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
        final RequestOAuthPopUpURLAPICall requestOAuthPopUpURLAPICall = this.context.getBean( RequestOAuthPopUpURLAPICall.class );
        final RequestOAuthPopUpURLAPIResult requestOAuthPopUpURLAPIResult = requestOAuthPopUpURLAPICall.execute( broker );
        final RequestOAuthPopUpURLDTO requestOAuthPopUpURLDTO = new RequestOAuthPopUpURLDTO( requestOAuthPopUpURLAPIResult );
        logMethodEnd( methodName, requestOAuthPopUpURLDTO );
        return requestOAuthPopUpURLDTO;
    }

    /**
     * Given the {@code oAuthVerifier} value returned from the user linking their broker account, this method
     * will obtain the user id and user token to authenticate the user to gain access to their broker account.
     * @param customerId
     * @param broker
     * @param oAuthVerifier
     * @return instance of GetOAuthAccessTokenAPIResult which contains the TradeItAccountDTO that was created if the TradeIt
     * getOAuthAccessToken was successful.
     */
    public GetOAuthAccessTokenDTO getOAuthAccessToken( final int customerId,
                                                       @NotNull final String broker,
                                                       @NotNull final String accountName,
                                                       @NotNull final String oAuthVerifier )
    {
        final String methodName = "getOAuthAccessTokenAPIResult";
        logMethodBegin( methodName, customerId, broker, accountName, oAuthVerifier );
        Objects.requireNonNull( broker, "broker cannot be null" );
        Objects.requireNonNull( accountName, "accountName cannot be null" );
        Objects.requireNonNull( oAuthVerifier, "oAuthVerifier cannot be null" );
        final GetOAuthAccessTokenAPICall getOAuthAccessTokenAPICall = this.context.getBean( GetOAuthAccessTokenAPICall.class );
        final GetOAuthAccessTokenAPIResult getOAuthAccessTokenAPIResult = getOAuthAccessTokenAPICall.execute( oAuthVerifier, broker );
        TradeItAccountDTO tradeItAccountDTO = null;
        if ( getOAuthAccessTokenAPIResult.isSuccessful() )
        {
            tradeItAccountDTO = this.tradeItAccountService.createAccount( customerId, broker, accountName,
                                                                          getOAuthAccessTokenAPIResult.getUserId(),
                                                                          getOAuthAccessTokenAPIResult.getUserToken() );
        }
        GetOAuthAccessTokenDTO getOAuthAccessTokenDTO = new GetOAuthAccessTokenDTO( getOAuthAccessTokenAPIResult );
        getOAuthAccessTokenDTO.setTradeItAccount( tradeItAccountDTO );
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
        throws LinkedAccountNotFoundException
    {
        final String methodName = "authenticate";
        logMethodBegin( methodName, customerId, accountId );
        final TradeItAccountEntity tradeItAccountEntity = this.tradeItAccountService.getAccountEntity( customerId, accountId );
        /*
         * Need to generate a UUID for the authentication process
         */
        if ( tradeItAccountEntity.getAuthUuid() == null )
        {
            String authUUID = UUID.randomUUID().toString();
            tradeItAccountEntity.setAuthUuid( authUUID );
            logDebug( methodName, "Setting UUID {0}", authUUID );
            this.tradeItAccountService.saveAccount( tradeItAccountEntity );
        }
        /*
         * Make the auth call
         */
        final AuthenticateAPICall authenticateAPICall = this.context.getBean( AuthenticateAPICall.class );
        final AuthenticateAPIResult authenticateAPIResult = authenticateAPICall.execute( tradeItAccountEntity );
        final AuthenticateDTO authenticateDTO = new AuthenticateDTO( authenticateAPIResult );
        /*
         * Check the auth results
         */
        if ( authenticateAPIResult.isInformationNeeded() )
        {
            logDebug( methodName, "INFORMATION_NEEDED - Need to prompt security question." );
            /*
             * Save the token as it is needed after the user answers the necessary security questions.
             */
            Objects.requireNonNull( authenticateAPIResult.getToken(), "The token cannot be null from the auth result" );
            tradeItAccountEntity.setAuthToken( authenticateAPIResult.getToken() );
            this.tradeItAccountService.saveAccount( tradeItAccountEntity );
        }
        else
        {
            this.tradeItAccountService.authenticationSuccessful( tradeItAccountEntity, authenticateAPIResult,
                                                                 authenticateDTO );
        }
        logDebug( methodName, "authenticateAPISuccessResult: {0}", authenticateAPIResult );
        logMethodEnd( methodName, authenticateDTO );
        return authenticateDTO;
    }

    /**
     * This method is called to send back the user's answer to the security question.
     * @param customerId
     * @param accountId
     * @param questionResponse The user's text response
     * @return
     */
    public AnswerSecurityQuestionDTO answerSecurityQuestion( final int customerId, final int accountId,
                                                             final String questionResponse )
        throws LinkedAccountNotFoundException
    {
        final String methodName = "answerSecurityQuestion";
        logMethodBegin( methodName, customerId, accountId, questionResponse );
        final TradeItAccountEntity tradeItAccountEntity = this.tradeItAccountService.getAccountEntity( customerId, accountId );
        final AnswerSecurityQuestionAPICall answerSecurityQuestionAPICall = this.context.getBean( AnswerSecurityQuestionAPICall.class );
        final AnswerSecurityQuestionAPIResult answerSecurityQuestionAPIResult =
            answerSecurityQuestionAPICall.execute( tradeItAccountEntity, questionResponse );
        final AnswerSecurityQuestionDTO authenticateDTO = new AnswerSecurityQuestionDTO( answerSecurityQuestionAPIResult );
        if ( answerSecurityQuestionAPIResult.isSuccessful() )
        {
            this.tradeItAccountService.authenticationSuccessful( tradeItAccountEntity, answerSecurityQuestionAPIResult,
                                                                 authenticateDTO );
        }
        logDebug( methodName, "authenticateAPISuccessResult: {0}", answerSecurityQuestionAPIResult );
        logMethodEnd( methodName, authenticateDTO );
        return authenticateDTO;
    }

    /**
     * Calls TradeIt to keep the session alive for the customer account.
     * @param customerId
     * @param accountId
     * @return
     */
    public KeepSessionAliveDTO keepSessionAlive( final int customerId, final int accountId )
    {
        final String methodName = "keepSessionAlive";
        logMethodBegin( methodName, customerId, accountId  );
        final TradeItAccountEntity tradeItAccountEntity = this.tradeItAccountService.getAccountEntity( customerId, accountId );
        final KeepSessionAliveAPICall keepSessionAliveAPICall = this.context.getBean( KeepSessionAliveAPICall.class );
        final KeepSessionAliveAPIResult keepSessionAliveAPIResult = keepSessionAliveAPICall.execute( tradeItAccountEntity );
        final KeepSessionAliveDTO keepSessionAliveDTO = new KeepSessionAliveDTO( keepSessionAliveAPIResult );
        logMethodEnd( methodName, keepSessionAliveDTO );
        return keepSessionAliveDTO;
    }

    /**
     * Calls TradeIt to close the sesssion for the customer account.
     * @param customerId
     * @param accountId
     * @return
     */
    public CloseSessionDTO closeSession( final int customerId, final int accountId )
    {
        final String methodName = "keepSessionAlive";
        logMethodBegin( methodName, customerId, accountId  );
        final TradeItAccountEntity tradeItAccountEntity = this.tradeItAccountService.getAccountEntity( customerId, accountId );
        final CloseSessionAPICall closeSessionAPICall = this.context.getBean( CloseSessionAPICall.class );
        final CloseSessionAPIResult closeSessionAPIResult = closeSessionAPICall.execute( tradeItAccountEntity );
        final CloseSessionDTO keepSessionAliveDTO = new CloseSessionDTO( closeSessionAPIResult );
        logMethodEnd( methodName, keepSessionAliveDTO );
        return keepSessionAliveDTO;
    }

    /**
     * Cycles through all of the linked accounts in {@code tradeItAccountEntity} and calls TradeIt to get the account
     * overview information and the positions.
     * @param tradeItAccountEntity
     */
    private void getAccountOverviewAndPositions( final TradeItAccountEntity tradeItAccountEntity )
    {
        final String methodName = "getAccountOverviewAndPositions";
        logMethodBegin( methodName, tradeItAccountEntity );
        tradeItAccountEntity.getLinkedAccountsById()
                            .forEach( linkedAccount -> this.getAccountOverviewAndPositions( tradeItAccountEntity, linkedAccount ));
        logMethodEnd( methodName );
    }

    /**
     * Gets the account overview and positions for the {@code linkedAccount}.
     * @param tradeItAccountEntity
     * @param linkedAccount
     */
    public void getAccountOverviewAndPositions( final TradeItAccountEntity tradeItAccountEntity,
                                                final LinkedAccountEntity linkedAccount )
    {
        final String methodName = "getAccountOverviewAndPositions";
        logMethodBegin( methodName, tradeItAccountEntity, linkedAccount );
        GetAccountOverviewDTO getAccountOverviewDTO = this.getAccountAccountOverview( tradeItAccountEntity.getCustomerId(),
                                                                                      tradeItAccountEntity.getId(),
                                                                                      linkedAccount.getAccountNumber() );
        linkedAccount.setAccountOverviewValues( getAccountOverviewDTO );
        GetPositionsDTO getPositionsDTO = this.getPositions( tradeItAccountEntity.getCustomerId(),
                                                             tradeItAccountEntity.getId(),
                                                             linkedAccount.getAccountNumber() );
    }

    /**
     * Get the account overview from TradeIt for the account number.  This is a sub account of the account identified
     * by {@code accountId}.
     * @param customerId The customer id.
     * @param accountId The account id of the {@AccountEntity}.
     * @param accountNumber The brokerage account number.
     * @return
     */
    public GetAccountOverviewDTO getAccountAccountOverview( final int customerId, final int accountId,
                                                            final String accountNumber )
    {
        final String methodName = "getAccountAccountOverview";
        logMethodBegin( methodName, customerId, accountId, accountNumber  );
        final TradeItAccountEntity tradeItAccountEntity = this.tradeItAccountService.getAccountEntity( customerId, accountId );
        final GetAccountOverviewAPICall getAccountOverviewAPICall = this.context.getBean( GetAccountOverviewAPICall.class );
        final GetAccountOverViewAPIResult getAccountOverviewAPIResult = getAccountOverviewAPICall.execute( accountNumber,
                                                                                                           tradeItAccountEntity );
        final GetAccountOverviewDTO getAccountOverviewDTO = new GetAccountOverviewDTO( getAccountOverviewAPIResult );
        logMethodEnd( methodName, getAccountOverviewDTO );
        return getAccountOverviewDTO;
    }

    /**
     * Get the the stock positions for the account.
     * @param customerId The customer id.
     * @param accountId The account id of the {@AccountEntity}.
     * @param accountNumber The brokerage account number.
     * @return
     */
    public GetPositionsDTO getPositions( final int customerId, final int accountId, final String accountNumber )
    {
        final String methodName = "getPositions";
        logMethodBegin( methodName, customerId, accountId, accountNumber  );
        final TradeItAccountEntity tradeItAccountEntity = this.tradeItAccountService.getAccountEntity( customerId, accountId );
        final GetPositionsAPICall getPositionsAPICall = this.context.getBean( GetPositionsAPICall.class );
        final GetPositionsAPIResult getPositionsAPIResult = getPositionsAPICall.execute( accountNumber, tradeItAccountEntity );
        final GetPositionsDTO getPositionsDTO = new GetPositionsDTO( getPositionsAPIResult );
        logMethodEnd( methodName, getPositionsDTO );
        return getPositionsDTO;
    }

    @Autowired
    public void setTradeItAccountService( final TradeItAccountEntityService tradeItAccountService )
    {
        logInfo( "setAccountService", "Dependency Injection of " + tradeItAccountService );
        this.tradeItAccountService = tradeItAccountService;
    }
}
