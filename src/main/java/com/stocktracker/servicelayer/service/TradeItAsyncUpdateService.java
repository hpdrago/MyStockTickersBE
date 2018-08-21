package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.DuplicateEntityException;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.LinkedAccountNotFoundException;
import com.stocktracker.common.exceptions.TradeItAPIException;
import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.repositorylayer.entity.TradeItAccountEntity;
import com.stocktracker.servicelayer.tradeit.apiresults.GetAccountOverviewAPIResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This service provides an asynchronous method to get the account overview information which is stored in the
 * {@code LinkedAccountEntity}.  The method calls TradeIt to get the account overview information and updates the
 * database.  Using this class makes the loading of the linked accounts faster on the front end such that the list of
 * accounts is returned quickly while the backend continues to work to load the overview information from TradeIt.
 */
@Service
public class TradeItAsyncUpdateService implements MyLogger
{
    @Autowired
    private LinkedAccountEntityService linkedAccountEntityService;

    /**
     * This is an asynchronous method that will update the linked account with the account summary information from
     * TradeIt.
     * @param tradeItAccountEntity This contains the necessary auth token and user id in order to call TradeIt to
     *                             execute the API call to get the account summary information.
     * @param linkedAccountEntity This is the linked account that will be updated with the summary account information.
     * @param getAccountOverview
     */
    public void updateLinkedAccount( final TradeItAccountEntity tradeItAccountEntity,
                                     final LinkedAccountEntity linkedAccountEntity,
                                     final GetAccountOverviewAPIResult getAccountOverview )
        throws LinkedAccountNotFoundException,
               TradeItAPIException,
               DuplicateEntityException
    {
        final String methodName = "updateLinkedAccount";
        logMethodBegin( methodName, tradeItAccountEntity.getUuid(), linkedAccountEntity.getUuid() );
        if ( getAccountOverview.isSuccessful() )
        {
            linkedAccountEntity.setGetAccountOverviewValues( getAccountOverview );
            try
            {
                this.linkedAccountEntityService
                    .saveEntity( linkedAccountEntity );
            }
            catch( EntityVersionMismatchException e )
            {
                this.handleEntityMismatchException( linkedAccountEntity, getAccountOverview );
            }
        }
        else
        {
            TradeItAPIException tradeItAPIException = new TradeItAPIException( getAccountOverview );
            throw tradeItAPIException;
        }
        logMethodEnd( methodName, tradeItAccountEntity.getUuid() );
    }

    /**
     * When an entity mismatch exception occurs we can handle this event by retrieving the current linked account
     * from the database and then performing the getAccountSummary update.
     * @param linkedAccountEntity
     * @param getAccountOverviewDTO
     * @throws LinkedAccountNotFoundException
     */
    private void handleEntityMismatchException( final LinkedAccountEntity linkedAccountEntity,
                                                final GetAccountOverviewAPIResult getAccountOverviewDTO )
        throws LinkedAccountNotFoundException,
               DuplicateEntityException
    {
        final String methodName = "handleEntityMismatchException";
        logWarn( methodName, "Entity version mismatch for linked account {0}",
                 linkedAccountEntity.getUuid() );
        /*
         * Refresh the entity and try again.
         */
        final LinkedAccountEntity currentLinkedAccountEntity = this.linkedAccountEntityService
                                                                   .getLinkedAccountEntity( linkedAccountEntity.getUuid() );
        currentLinkedAccountEntity.setGetAccountOverviewValues( getAccountOverviewDTO );
        this.linkedAccountEntityService
            .saveEntity( currentLinkedAccountEntity );
        logInfo( methodName, "Resolved entity mismatch for linked account {0}", linkedAccountEntity.getUuid() );
    }
}
