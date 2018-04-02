package com.stocktracker.servicelayer.service;

import com.stocktracker.common.EntityLoadingStatus;
import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.LinkedAccountNotFoundException;
import com.stocktracker.common.exceptions.TradeItAccountNotFoundException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.repositorylayer.entity.TradeItAccountEntity;
import com.stocktracker.repositorylayer.repository.LinkedAccountRepository;
import com.stocktracker.servicelayer.tradeit.TradeItService;
import com.stocktracker.weblayer.dto.LinkedAccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * This service provides methods to interact with the database to store and retrieved linked accounts.  Linked accounts
 * are the accounts returned by TradeIt when the user is authenticated.  A user can have multiple accounts linked to a
 * single log in to the brokerage.  These multiple accounts are termed linked accounts.
 *
 * Created by mike on 1/18/2018.
 */
@Service
@Transactional
public class LinkedAccountEntityService extends VersionedEntityService<Integer,
                                                                       LinkedAccountEntity,
                                                                       LinkedAccountDTO,
                                                                       LinkedAccountRepository>
                                        implements MyLogger
{
    private LinkedAccountRepository linkedAccountRepository;
    private TradeItAsyncUpdateService tradeItAsyncUpdateService;
    private TradeItService tradeItService;
    private TradeItAccountEntityService tradeItAccountEntityService;

    /**
     * This method will retrieve all of the linked accounts (child to the tradeit_account table) for the TradeIt account.
     * @param customerId
     * @param tradeItAccountId
     * @return
     * @throws TradeItAccountNotFoundException
     */
    public List<LinkedAccountDTO> getLinkedAccounts( final int customerId,
                                                     final int tradeItAccountId )
        throws TradeItAccountNotFoundException
    {
        final String methodName = "getLinkedAccounts";
        logMethodBegin( methodName, customerId, tradeItAccountId );
        final TradeItAccountEntity tradeItAccountEntity = this.tradeItAccountEntityService
                                                              .getTradeItAccountEntity( customerId, tradeItAccountId );
        List<LinkedAccountEntity> linkedAccountEntities = this.linkedAccountRepository
                                                              .findAllByCustomerIdAndTradeItAccountId( customerId,
                                                                                                       tradeItAccountId );
        /*
         * Getting the account overview information is a two step process because it is asynchronous in nature.
         * The first step is to set the linked accounts to be updated in the {@code TradeitAsyncUpdateService} this is
         * done first so that the service knows about the accounts that need to be updated in case the front end calls
         * to get the updated information before the second stop starts.
         * The next is to make the asynchronous calls to update the account overview status.
         *
         * These are asynchronous calls to refresh the account overview information
         */
        linkedAccountEntities
            .forEach( linkedAccountEntity -> this.tradeItAsyncUpdateService
                                                 .prepareToUpdateLinkedAccount( linkedAccountEntity ));
        linkedAccountEntities
            .forEach( linkedAccountEntity -> this.tradeItAsyncUpdateService
                                                 .updateLinkedAccount( tradeItAccountEntity, linkedAccountEntity ));
        /*
         * Return the values that are currently in the database, the front ent will load those values first and
         * then make another call to get the updated information for each account.
         */
        final List<LinkedAccountDTO> linkedAccountDTOs = this.entitiesToDTOs( linkedAccountEntities );
        linkedAccountDTOs.forEach( linkedAccountDTO -> linkedAccountDTO.setLoadingStatus( EntityLoadingStatus.LOADING ));
        logMethodEnd( methodName, String.format( "returning %d linked account", linkedAccountDTOs.size() ));
        return linkedAccountDTOs;
    }

    /**
     * Retrieves the linked account from the database by the primary key {@code linkedAccountId}.
     * A safety check against the {@code customerId} is performed to make sure the account is owned by the customer.
     * @param customerId
     * @param linkedAccountId
     * @return
     * @throws LinkedAccountNotFoundException if the linked account cannot be found by {@code linkeAccountId}.
     * @throws IllegalArgumentException if the customer id does not match.
     */
    public LinkedAccountEntity getLinkedAccountEntity( final int customerId, final int linkedAccountId )
        throws LinkedAccountNotFoundException
    {
        final LinkedAccountEntity linkedAccountEntity;
        try
        {
            linkedAccountEntity = this.getEntity( linkedAccountId );
        }
        catch( VersionedEntityNotFoundException e )
        {
            throw new LinkedAccountNotFoundException( linkedAccountId );
        }
        /*
         * Safety check for customer id
         */
        if ( linkedAccountEntity.getCustomerId() != customerId )
        {
            throw new IllegalArgumentException( String.format( "Wrong customer id(%d) on Linked Account(%d)",
                                                               customerId, linkedAccountId ));
        }
        return linkedAccountEntity;
    }

    /**
     * Blocks and waits for the get account summary information call that must have been made previously.
     * @param customerId
     * @param linkedAccountId
     * @return Updated linked account.
     * @throws IllegalArgumentException if the {@code linkedAccountId} is not registered to be updated.
     */
    public LinkedAccountDTO getUpdatedLinkedAccount( final int customerId, final int linkedAccountId )
    {
        final String methodName = "getUpdatedLinkedAccount";
        logMethodBegin( methodName, customerId, linkedAccountId );
        this.tradeItAsyncUpdateService
            .checkGetAccountOverviewExists( linkedAccountId );
        logDebug( methodName, "blocking for linked account: {0}", linkedAccountId );
        final LinkedAccountEntity linkedAccountEntity = this.tradeItAsyncUpdateService
                                                            .subscribeToGetAccountOverview( linkedAccountId )
                                                            .doOnError( throwable ->
                                                                        {
                                                                            logError( methodName, throwable.getCause() );
                                                                        })
                                                            .toBlocking()
                                                            .first();
        logDebug( methodName, "received after blocking {0}", linkedAccountEntity );
        this.tradeItAsyncUpdateService
            .removeGetAccountOverviewRequest( linkedAccountId );
        final LinkedAccountDTO linkedAccountDTO = this.entityToDTO( linkedAccountEntity );
        linkedAccountDTO.setLoadingStatus( EntityLoadingStatus.LOADED );
        logMethodEnd( methodName, linkedAccountDTO );
        return linkedAccountDTO;
    }

    @Override
    protected LinkedAccountDTO createDTO()
    {
        return this.context.getBean( LinkedAccountDTO.class );
    }

    @Override
    protected LinkedAccountEntity createEntity()
    {
        return this.context.getBean( LinkedAccountEntity.class );
    }

    @Override
    protected LinkedAccountRepository getRepository()
    {
        return this.linkedAccountRepository;
    }

    @Autowired
    public void setLinkedAccountRepository( final LinkedAccountRepository linkedAccountRepository )
    {
        this.linkedAccountRepository = linkedAccountRepository;
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

    @Autowired
    public void setTradeItAsyncUpdateService( final TradeItAsyncUpdateService tradeItAsyncUpdateService )
    {
        this.tradeItAsyncUpdateService = tradeItAsyncUpdateService;
        logInfo( "setTradeItAsyncUpdateService", "Injection of " + tradeItAsyncUpdateService );
    }

}
