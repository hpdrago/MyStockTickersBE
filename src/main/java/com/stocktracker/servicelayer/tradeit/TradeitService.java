package com.stocktracker.servicelayer.tradeit;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.entity.AccountEntity;
import com.stocktracker.servicelayer.service.AccountService;
import com.stocktracker.servicelayer.tradeit.apicalls.AuthenticateAPICall;
import com.stocktracker.servicelayer.tradeit.apicalls.GetBrokersAPICall;
import com.stocktracker.servicelayer.tradeit.apicalls.GetOAuthAccessTokenAPICall;
import com.stocktracker.servicelayer.tradeit.apicalls.RequestOAuthPopUpURLAPICall;
import com.stocktracker.servicelayer.tradeit.apiresults.AuthenticateAPIResult;
import com.stocktracker.weblayer.dto.AccountDTO;
import com.stocktracker.servicelayer.tradeit.apiresults.GetBrokersAPIResult;
import com.stocktracker.servicelayer.tradeit.apiresults.GetOAuthAccessTokenAPIResult;
import com.stocktracker.weblayer.dto.tradeit.AuthenticateDTO;
import com.stocktracker.weblayer.dto.tradeit.GetOAuthAccessTokenDTO;
import com.stocktracker.servicelayer.tradeit.apiresults.RequestOAuthPopUpURLAPIResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * This service contains all of the REST calls to TradeIt
 */
@Service
public class TradeItService implements MyLogger
{
    private static final Logger logger = LoggerFactory.getLogger( TradeItService.class );

    @Autowired
    private ApplicationContext context;
    private AccountService accountService;

    /**
     * Get the list of brokers supported by TradeIt
     * @return
     */
    public GetBrokersAPIResult getBrokers()
    {
        final String methodName = "getBrokers";
        logger.debug( methodName + ".begin" );
        final GetBrokersAPICall getBrokersAPICall = this.context.getBean( GetBrokersAPICall.class );
        final GetBrokersAPIResult getBrokersAPIResult = getBrokersAPICall.execute();
        logger.debug( methodName + ".end " + getBrokersAPIResult );
        return getBrokersAPIResult;
    }

    /**
     * This method will request a URL to be used to popup a authentication window for the user to login to their
     * brokerage account.
     * @param broker
     * @return
     */
    public RequestOAuthPopUpURLAPIResult requestOAuthPopUpURL( @NotNull final String broker )
    {
        final String methodName = "requestOAuthPopUpURL";
        logMethodBegin( methodName, broker );
        Objects.requireNonNull( broker, "Broker cannot be null" );
        final RequestOAuthPopUpURLAPICall requestOAuthPopUpURLAPICall = this.context.getBean( RequestOAuthPopUpURLAPICall.class );
        final RequestOAuthPopUpURLAPIResult requestOAuthPopUpURLAPIResult = requestOAuthPopUpURLAPICall.execute( broker );
        logMethodEnd( methodName, requestOAuthPopUpURLAPIResult );
        return requestOAuthPopUpURLAPIResult;
    }

    /**
     * Given the {@code oAuthVerifier} value returned from the user linking their broker account, this method
     * will obtain the user id and user token to authenticate the user to gain access to their broker account.
     * @param customerId
     * @param broker
     * @param oAuthVerifier
     * @return instance of GetOAuthAccessTokenAPIResult which contains the AccountDTO that was created if the TradeIt
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
        AccountDTO accountDTO = null;
        if ( getOAuthAccessTokenAPIResult.isSuccessful() )
        {
            accountDTO = this.accountService.createAccount( customerId, broker, accountName,
                                                            getOAuthAccessTokenAPIResult.getUserId(),
                                                            getOAuthAccessTokenAPIResult.getUserToken() );
        }
        GetOAuthAccessTokenDTO getOAuthAccessTokenDTO = new GetOAuthAccessTokenDTO( getOAuthAccessTokenAPIResult );
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
        final AccountEntity accountEntity = this.accountService.getAccountEntity( customerId, accountId );
        final AuthenticateAPICall authenticateAPICall = this.context.getBean( AuthenticateAPICall.class );
        final AuthenticateAPIResult authenticateAPIResult = authenticateAPICall.execute( accountEntity );
        final AuthenticateDTO authenticateDTO = new AuthenticateDTO( authenticateAPIResult );
        logDebug( methodName, "authenticateAPISuccessResult: {0}", authenticateAPIResult );
        logMethodEnd( methodName, authenticateDTO );
        return authenticateDTO;
    }

    @Autowired
    public void setAccountService( final AccountService accountService )
    {
        logInfo( "setAccountService", "Dependency Injection of " + accountService );
        this.accountService = accountService;
    }
}
