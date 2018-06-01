package com.stocktracker.servicelayer.service;

import com.fasterxml.uuid.impl.UUIDUtil;
import com.stocktracker.common.EntityLoadingStatus;
import com.stocktracker.common.exceptions.LinkedAccountNotFoundException;
import com.stocktracker.common.exceptions.TradeItAccountNotFoundException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.repositorylayer.entity.TradeItAccountEntity;
import com.stocktracker.repositorylayer.repository.LinkedAccountRepository;
import com.stocktracker.weblayer.dto.LinkedAccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * This service provides methods to interact with the database to store and retrieved linked accounts.  Linked accounts
 * are the accounts returned by TradeIt when the user is authenticated.  A user can have multiple accounts linked to a
 * single log in to the brokerage.  These multiple accounts are termed linked accounts.
 *
 * Created by mike on 1/18/2018.
 */
@Service
public class LinkedAccountEntityService extends UuidEntityService<LinkedAccountEntity,
                                                                  LinkedAccountDTO,
                                                                  LinkedAccountRepository>
{
    private LinkedAccountRepository linkedAccountRepository;
    private TradeItAsyncUpdateService tradeItAsyncUpdateService;
    private TradeItAccountEntityService tradeItAccountEntityService;


    /**
     * Get all of the linked account for a customer.
     * @param customerUuid
     * @return
     */
    public List<LinkedAccountDTO> getLinkedAccountsForCustomer( final UUID customerUuid )
    {
        final String methodName = "getLinkedAccountsForCustomer";
        logMethodBegin( methodName, customerUuid );
        final List<LinkedAccountEntity> linkedAccountEntities = this.linkedAccountRepository
                                                                    .findByCustomerUuid( customerUuid );
        final List<LinkedAccountDTO> linkedAccountDTOs = this.entitiesToDTOs( linkedAccountEntities );
        logMethodEnd( methodName, linkedAccountDTOs );
        return linkedAccountDTOs;
    }

    /**
     * This method will retrieve all of the linked accounts (child to the tradeit_account table) for the TradeIt account.
     * @param tradeItAccountUuid
     * @return
     * @throws TradeItAccountNotFoundException
     */
    public List<LinkedAccountDTO> getLinkedAccounts( final UUID tradeItAccountUuid )
        throws TradeItAccountNotFoundException
    {
        final String methodName = "getLinkedAccounts";
        logMethodBegin( methodName, tradeItAccountUuid );
        final TradeItAccountEntity tradeItAccountEntity;
        try
        {
            tradeItAccountEntity = this.tradeItAccountEntityService
                                       .getEntity( tradeItAccountUuid );
        }
        catch( VersionedEntityNotFoundException e )
        {
            throw new TradeItAccountNotFoundException( tradeItAccountUuid, e );
        }
        List<LinkedAccountEntity> linkedAccountEntities = this.linkedAccountRepository
                                                              .findAllByTradeItAccountUuid( tradeItAccountUuid );
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
     * @param linkedAccountUuid
     * @return
     * @throws LinkedAccountNotFoundException if the linked account cannot be found by {@code linkeAccountId}.
     * @throws IllegalArgumentException if the customer id does not match.
     */
    public LinkedAccountEntity getLinkedAccountEntity( final UUID linkedAccountUuid )
        throws LinkedAccountNotFoundException
    {
        final LinkedAccountEntity linkedAccountEntity;
        try
        {
            linkedAccountEntity = this.getEntity( linkedAccountUuid );
        }
        catch( VersionedEntityNotFoundException e )
        {
            throw new LinkedAccountNotFoundException( linkedAccountUuid );
        }
        return linkedAccountEntity;
    }

    /**
     * Blocks and waits for the get account summary information call that must have been made previously.
     * @param linkedAccountUuid
     * @return Updated linked account.
     * @throws IllegalArgumentException if the {@code linkedAccountId} is not registered to be updated.
     */
    public LinkedAccountDTO getUpdatedLinkedAccount( UUID linkedAccountUuid )
    {
        final String methodName = "getUpdatedLinkedAccount";
        logMethodBegin( methodName, linkedAccountUuid );
        this.tradeItAsyncUpdateService
            .checkGetAccountOverviewExists( linkedAccountUuid );
        logDebug( methodName, "blocking for linked account: {0}", linkedAccountUuid );
        final LinkedAccountEntity linkedAccountEntity = this.tradeItAsyncUpdateService
                                                            .subscribeToGetAccountOverview( linkedAccountUuid )
                                                            .doOnError( throwable ->
                                                                        {
                                                                            logError( methodName, throwable.getCause() );
                                                                        })
                                                            .blockingFirst();
        logDebug( methodName, "received after blocking {0}", linkedAccountEntity );
        this.tradeItAsyncUpdateService
            .removeGetAccountOverviewRequest( linkedAccountUuid );
        final LinkedAccountDTO linkedAccountDTO = this.entityToDTO( linkedAccountEntity );
        linkedAccountDTO.setLoadingStatus( EntityLoadingStatus.LOADED );
        logMethodEnd( methodName, linkedAccountDTO );
        return linkedAccountDTO;
    }

    /**
     * Override to convert the binary uuid for the TradeIt account to a string uuid.
     * @param entity Contains the entity information.
     * @return
     */
    @Override
    protected LinkedAccountDTO entityToDTO( final LinkedAccountEntity entity )
    {
        final LinkedAccountDTO linkedAccountDTO = super.entityToDTO( entity );
        linkedAccountDTO.setTradeItAccountId( entity.getTradeItAccountUuid().toString() );
        return linkedAccountDTO;
    }

    /**
     * Override to convert the UUID from string to binary.
     * @param dto of type <D>
     * @return
     */
    @Override
    protected LinkedAccountEntity dtoToEntity( final LinkedAccountDTO dto )
    {
        final LinkedAccountEntity linkedAccountEntity = super.dtoToEntity( dto );
        linkedAccountEntity.setTradeItAccountUuid( UUIDUtil.uuid( dto.getTradeItAccountId() ) );
        return linkedAccountEntity;
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
