package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.SetComparator;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.LinkedAccountNotFoundException;
import com.stocktracker.repositorylayer.entity.TradeItAccountEntity;
import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.servicelayer.tradeit.apiresults.AuthenticateAPIResult;
import com.stocktracker.servicelayer.tradeit.types.TradeItAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class performs the comparison of brokerage accounts that are returned from an authenticate call to the current
 * list of linked accounts already in the database.  There are four possibilities: new accounts, deleted, accounts, updated
 * account, and no changes.  This class handles all of these scenarios although any database work is forwarded to the
 * {@code accountService}.
 */
@Service
public class TradeItAccountComparisonService implements MyLogger
{
    private TradeItAccountEntityService tradeItAccountEntityService;
    private LinkedAccountEntityService linkedAccountEntityService;

    /**
     * Compares the linked accounts in {@accountEntity} which contains a list of {@code LinkedAccountEntity} to the
     * {@code AuthenticateAPIResult} which contains a list of {@code TradeItAccountDTO}.
     * @param tradeItAccountEntity
     * @param authenticateAPIResult
     * @throws LinkedAccountNotFoundException
     * @throws EntityVersionMismatchException
     */
    public void compare( final TradeItAccountEntity tradeItAccountEntity, final AuthenticateAPIResult authenticateAPIResult )
        throws LinkedAccountNotFoundException,
               EntityVersionMismatchException
    {
        SetComparator<String>.SetComparatorResults validateLinkedAccountsResult = this.validateLinkedAccounts( tradeItAccountEntity,
                                                                                                               authenticateAPIResult );
        checkForNewAccounts( tradeItAccountEntity, authenticateAPIResult, validateLinkedAccountsResult );
        checkForDeletedAccounts( tradeItAccountEntity, authenticateAPIResult, validateLinkedAccountsResult );
        checkForUpdatedAccounts( tradeItAccountEntity, authenticateAPIResult, validateLinkedAccountsResult );
    }

    /**
     * Evaluates the account comparison results (@code ValidateLinkedAccountResult} to identify if there are any updated
     * account information.  Any differences in account information will be persisted to the database.
     * @param tradeItAccountEntity
     * @param authenticateAPIResult
     * @param validateLinkedAccountsResult
     * @throws LinkedAccountNotFoundException
     * @throws EntityVersionMismatchException
     */
    private void checkForUpdatedAccounts( final TradeItAccountEntity tradeItAccountEntity,
                                          final AuthenticateAPIResult authenticateAPIResult,
                                          final SetComparator<String>.SetComparatorResults validateLinkedAccountsResult )
        throws LinkedAccountNotFoundException,
               EntityVersionMismatchException
    {
        final String methodName = "checkForUpdatedAccounts";
        logMethodBegin( methodName, tradeItAccountEntity, authenticateAPIResult, validateLinkedAccountsResult );
        if ( validateLinkedAccountsResult.getMatchingItems().isEmpty() )
        {
            logDebug( methodName, "There are no updated accounts" );
        }
        else
        {
            /*
             * Compare the contents of each account to see if there are any changes.
             */
            for ( String accountNumber : validateLinkedAccountsResult.getMatchingItems() )
            {
                compareAccounts( tradeItAccountEntity, authenticateAPIResult, accountNumber );
            }
        }
        logMethodEnd( methodName );
    }

    /**
     * Searches {@code tradeItAccountEntity} and {@code authenticateAPIResult} for the account number {@code accountNumber}
     * and then compares their contents.  If the accounts are different, the TradeIt values are updated in the LinkedAccount.
     * @param tradeItAccountEntity
     * @param authenticateAPIResult
     * @param accountNumber
     * @throws IllegalArgumentException
     * @throws LinkedAccountNotFoundException
     * @throws EntityVersionMismatchException
     */
    private void compareAccounts( final TradeItAccountEntity tradeItAccountEntity,
                                  final AuthenticateAPIResult authenticateAPIResult,
                                  final String accountNumber )
        throws LinkedAccountNotFoundException,
               EntityVersionMismatchException
    {
        final String methodName = "compareAccounts";
        logMethodBegin( methodName, tradeItAccountEntity, authenticateAPIResult, accountNumber );
        Optional<TradeItAccount> tradeItAccount = authenticateAPIResult
            .getTradeItAccount( accountNumber );
        if ( tradeItAccount.isPresent() )
        {
            LinkedAccountEntity linkedAccountEntity = tradeItAccountEntity
                .getLinkedAccount( accountNumber, tradeItAccountEntity );
            if ( !linkedAccountEntity.isAccountDetailsEqual(
                 tradeItAccount.get().getName(),
                 tradeItAccount.get().getAccountNumber() ))
            {
                linkedAccountEntity.setAccountName( tradeItAccount.get().getName() );
                this.linkedAccountEntityService
                    .saveEntity( linkedAccountEntity );
            }
        }
        else
        {
            throw new IllegalArgumentException( "Could not found TradeItAccountDTO " +
                                                accountNumber );
        }
        logMethodEnd( methodName );
    }

    /**
     * Evaluates the account comparison results (@code ValidateLinkedAccountResult} to identify if there are any linked
     * accounts that are not present in the TradeItResults.  For now, we'll just log this information.
     * @param tradeItAccountEntity
     * @param authenticateAPIResult
     * @param validateLinkedAccountsResult
     */
    private void checkForDeletedAccounts( final TradeItAccountEntity tradeItAccountEntity,
                                          final AuthenticateAPIResult authenticateAPIResult,
                                          final SetComparator<String>.SetComparatorResults validateLinkedAccountsResult )
    {
        final String methodName = "checkForDeletedAccounts";
        logMethodBegin( methodName, tradeItAccountEntity, authenticateAPIResult, validateLinkedAccountsResult );
        if ( validateLinkedAccountsResult.getDeletedItems().isEmpty() )
        {
            logDebug( methodName, "No deleted accounts found" );
        }
        else
        {
            logDebug( methodName, "This following accounts have been deleted: " +
                                  validateLinkedAccountsResult.getDeletedItems() );
        }
        logMethodEnd( methodName );
    }

    /**
     * Evaluates the account comparison results {@code ValidateLinkedAccountResult} to identify if there are any new
     * linked accounts.  If new linked accounts are found, the account information will be persisted in the database.
     * @param tradeItAccountEntity
     * @param authenticateAPIResult
     * @param validateLinkedAccountsResult
     * @throws EntityVersionMismatchException
     */
    private void checkForNewAccounts( final TradeItAccountEntity tradeItAccountEntity,
                                      final AuthenticateAPIResult authenticateAPIResult,
                                      final SetComparator<String>.SetComparatorResults validateLinkedAccountsResult )
        throws EntityVersionMismatchException
    {
        final String methodName = "checkForNewAccounts";
        logMethodBegin( methodName, tradeItAccountEntity, authenticateAPIResult, validateLinkedAccountsResult );
        if ( validateLinkedAccountsResult.getNewItems().isEmpty() )
        {
            logDebug( methodName, "No new accounts found" );
        }
        else
        {
            for ( final String accountNumber : validateLinkedAccountsResult.getNewItems() )
            {
                Optional<TradeItAccount> tradeItAccount = authenticateAPIResult
                    .getTradeItAccount( accountNumber );
                if ( tradeItAccount.isPresent() )
                {
                    this.tradeItAccountEntityService.addLinkedAccount( tradeItAccountEntity, tradeItAccount.get() );
                }
                else
                {
                    throw new IllegalArgumentException(
                        "Could not found trade it account by account number " + accountNumber );
                }
            }
        }
        logMethodEnd( methodName );
    }

    /**
     * Evaluates the current linked account(s) (if any) in {@accountEntity} with the accounts returned by the
     * authentication call.
     * @param tradeItAccountEntity
     * @param authenticateAPIResult
     * @return {@code ValidateLinkedAccountResult}
     */
    private SetComparator<String>.SetComparatorResults validateLinkedAccounts( final TradeItAccountEntity tradeItAccountEntity,
                                                                                final AuthenticateAPIResult authenticateAPIResult )
    {
        final String methodName = "validateLinkedAccounts";
        logMethodBegin( methodName, tradeItAccountEntity, authenticateAPIResult );
        Set<String> currentAccounts = new TreeSet();
        tradeItAccountEntity.getLinkedAccountsById()
                            .forEach( linkedAccount -> currentAccounts.add( linkedAccount.getAccountNumber() ));
        Set<String> tradeItAccounts = new TreeSet<>();
        if ( authenticateAPIResult.getAccounts().isPresent() )
        {
            for ( TradeItAccount tradeItAccount : authenticateAPIResult.getAccounts().get() )
            {
                tradeItAccounts.add( tradeItAccount.getAccountNumber() );
            }
        }
        SetComparator<String> accountComparator = new SetComparator<>();
        SetComparator<String>.SetComparatorResults results = accountComparator.compareSets( tradeItAccounts, currentAccounts );
        logMethodEnd( methodName, results );
        return results;
    }

    @Autowired
    public void setTradeItAccountService( final TradeItAccountEntityService tradeItAccountEntityService )
    {
        this.tradeItAccountEntityService = tradeItAccountEntityService;
    }

    @Autowired
    public void setLinkedAccountEntityService( final LinkedAccountEntityService linkedAccountEntityService )
    {
        this.linkedAccountEntityService = linkedAccountEntityService;
    }
}
