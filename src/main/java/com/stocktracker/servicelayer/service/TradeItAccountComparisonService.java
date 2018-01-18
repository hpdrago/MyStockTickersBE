package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.SetComparator;
import com.stocktracker.common.exceptions.LinkedAccountNotFoundException;
import com.stocktracker.repositorylayer.entity.AccountEntity;
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
 * list of linked accounts already in the database.  There are for possibilities: new accounts, deleted, accounts, updated
 * account, and no changes.  This class handles all of these scenarios although any database work is forwarded to the
 * {@code accountService}.
 */
@Service
public class TradeItAccountComparisonService implements MyLogger
{
    private AccountService accountService;

    /**
     * Compares the linked accounts in {@accountEntity} which contains a list of {@code LinkedAccountEntity} to the
     * {@code AuthenticateAPIResult} which contains a list of {@code TradeItAccount}.
     * @param accountEntity
     * @param authenticateAPIResult
     * @throws LinkedAccountNotFoundException
     */
    public void compare( final AccountEntity accountEntity, final AuthenticateAPIResult authenticateAPIResult )
        throws LinkedAccountNotFoundException
    {
        SetComparator<String>.SetComparatorResults validateLinkedAccountsResult = this.validateLinkedAccounts( accountEntity,
                                                                                      authenticateAPIResult );
        checkForNewAccounts( accountEntity, authenticateAPIResult, validateLinkedAccountsResult );
        checkForDeletedAccounts( accountEntity, authenticateAPIResult, validateLinkedAccountsResult );
        checkForUpdatedAccounts( accountEntity, authenticateAPIResult, validateLinkedAccountsResult );
    }

    /**
     * Evaluates the account comparison results (@code ValidateLinkedAccountResult} to identify if there are any updated
     * account information.  Any differences in account information will be persisted to the database.
     * @param accountEntity
     * @param authenticateAPIResult
     * @param validateLinkedAccountsResult
     * @throws LinkedAccountNotFoundException
     */
    private void checkForUpdatedAccounts( final AccountEntity accountEntity,
                                          final AuthenticateAPIResult authenticateAPIResult,
                                          final SetComparator<String>.SetComparatorResults validateLinkedAccountsResult )
        throws LinkedAccountNotFoundException
    {
        final String methodName = "checkForUpdatedAccounts";
        logMethodBegin( methodName, accountEntity, authenticateAPIResult, validateLinkedAccountsResult );
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
                compareAccounts( accountEntity, authenticateAPIResult, accountNumber );
            }
        }
        logMethodEnd( methodName );
    }

    /**
     * Searches {@code accountEntity} and {@code authenticateAPIResult} for the account number {@code accountNumber}
     * and then compares their contents.  If the accounts are different, the TradeIt values are updated in the LinkedAccount.
     * @param accountEntity
     * @param authenticateAPIResult
     * @param accountNumber
     * @throws IllegalArgumentException if trade
     * @throws LinkedAccountNotFoundException
     */
    private void compareAccounts( final AccountEntity accountEntity,
                                  final AuthenticateAPIResult authenticateAPIResult,
                                  final String accountNumber )
        throws LinkedAccountNotFoundException
    {
        final String methodName = "compareAccounts";
        logMethodBegin( methodName, accountEntity, authenticateAPIResult, accountNumber );
        Optional<TradeItAccount> tradeItAccount = authenticateAPIResult
            .getTradeItAccount( accountNumber );
        if ( tradeItAccount.isPresent() )
        {
            LinkedAccountEntity linkedAccountEntity = accountEntity.getLinkedAccount( accountNumber, accountEntity );
            if ( !linkedAccountEntity.isAccountDetailsEqual(
                 tradeItAccount.get().getName(),
                 tradeItAccount.get().getAccountNumber() ))
            {
                linkedAccountEntity.setAccountName( tradeItAccount.get().getName() );
                this.accountService.updateLinkedAccount( linkedAccountEntity );
            }
        }
        else
        {
            throw new IllegalArgumentException( "Could not found TradeItAccount " +
                                                accountNumber );
        }
        logMethodEnd( methodName );
    }

    /**
     * Evaluates the account comparison results (@code ValidateLinkedAccountResult} to identify if there are any linked
     * accounts that are not present in the TradeItResults.  For now, we'll just log this information.
     * @param accountEntity
     * @param authenticateAPIResult
     * @param validateLinkedAccountsResult
     */
    private void checkForDeletedAccounts( final AccountEntity accountEntity,
                                          final AuthenticateAPIResult authenticateAPIResult,
                                          final SetComparator<String>.SetComparatorResults validateLinkedAccountsResult )
    {
        final String methodName = "checkForDeletedAccounts";
        logMethodBegin( methodName, accountEntity, authenticateAPIResult, validateLinkedAccountsResult );
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
     * @param accountEntity
     * @param authenticateAPIResult
     * @param validateLinkedAccountsResult
     */
    private void checkForNewAccounts( final AccountEntity accountEntity,
                                      final AuthenticateAPIResult authenticateAPIResult,
                                      final SetComparator<String>.SetComparatorResults validateLinkedAccountsResult )
    {
        final String methodName = "checkForNewAccounts";
        logMethodBegin( methodName, accountEntity, authenticateAPIResult, validateLinkedAccountsResult );
        if ( validateLinkedAccountsResult.getNewItems().isEmpty() )
        {
            logDebug( methodName, "No new accounts found" );
        }
        else
        {
            validateLinkedAccountsResult.getNewItems()
                .forEach( accountNumber ->
                          {
                              Optional<TradeItAccount> tradeItAccount = authenticateAPIResult
                                  .getTradeItAccount( accountNumber );
                              if ( tradeItAccount.isPresent() )
                              {
                                  this.accountService.addLinkedAccount( accountEntity, tradeItAccount.get() );
                              }
                              else
                              {
                                  throw new IllegalArgumentException(
                                      "Could not found trade it account by account number " + accountNumber );
                              }
                          });
        }
        logMethodEnd( methodName );
    }

    /**
     * Evaluates the current linked account(s) (if any) in {@accountEntity} with the accounts returned by the
     * authentication call.
     * @param accountEntity
     * @param authenticateAPIResult
     * @return {@code ValidateLinkedAccountResult}
     */
    private SetComparator<String>.SetComparatorResults validateLinkedAccounts( final AccountEntity accountEntity,
                                                                                final AuthenticateAPIResult authenticateAPIResult )
    {
        final String methodName = "validateLinkedAccounts";
        logMethodBegin( methodName, accountEntity, authenticateAPIResult );
        Set<String> currentAccounts = new TreeSet();
        accountEntity.getLinkedAccountsById()
                     .forEach( linkedAccount -> currentAccounts.add( linkedAccount.getAccountNumber() ));
        Set<String> tradeItAccounts = new TreeSet<>();
        for ( TradeItAccount tradeItAccount: authenticateAPIResult.getTradeItAccounts() )
        {
            tradeItAccounts.add( tradeItAccount.getAccountNumber() );
        }
        SetComparator<String> accountComparator = new SetComparator<>();
        SetComparator<String>.SetComparatorResults results = accountComparator.compareSets( tradeItAccounts, currentAccounts );
        logMethodEnd( methodName, results );
        return results;
    }

    @Autowired
    public void setAccountService( final AccountService accountService )
    {
        this.accountService = accountService;
    }

}
