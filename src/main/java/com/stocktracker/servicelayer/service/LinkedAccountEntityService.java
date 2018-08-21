package com.stocktracker.servicelayer.service;

import com.fasterxml.uuid.impl.UUIDUtil;
import com.stocktracker.common.exceptions.LinkedAccountNotFoundException;
import com.stocktracker.common.exceptions.TradeItAccountNotFoundException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.repositorylayer.entity.LinkedAccountEntityList;
import com.stocktracker.repositorylayer.entity.TradeItAccountEntity;
import com.stocktracker.repositorylayer.repository.LinkedAccountRepository;
import com.stocktracker.servicelayer.service.cache.linkedaccount.LinkedAccountEntityCacheClient;
import com.stocktracker.servicelayer.service.cache.linkedaccount.LinkedAccountEntityCacheDataReceiver;
import com.stocktracker.weblayer.dto.LinkedAccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    @Autowired
    private LinkedAccountRepository linkedAccountRepository;
    @Autowired
    private TradeItAccountEntityService tradeItAccountEntityService;
    @Autowired
    private LinkedAccountEntityCacheClient linkedAccountEntityCacheClient;

    /**
     * Get the linked account entities for a TradeIt account.
     * @param tradeItAccountUuid
     * @return
     */
    public LinkedAccountEntityList getLinkedAccountEntities( final UUID tradeItAccountUuid )
    {
        final String methodName = "getLinkedAccountsForCustomer";
        LinkedAccountEntityList linkedAccountEntityList = this.context.getBean( LinkedAccountEntityList.class );
        linkedAccountEntityList.findByTradeItAccountUuid( tradeItAccountUuid );
        logMethodEnd( methodName );
        return linkedAccountEntityList;
    }

    /**
     * Get all of the linked account for a customer.
     * @param customerUuid
     * @return
     */
    public List<LinkedAccountDTO> getLinkedAccountsForCustomer( final UUID customerUuid )
    {
        final String methodName = "getLinkedAccountsForCustomer";
        logMethodBegin( methodName, customerUuid );
        final LinkedAccountEntityList linkedAccountEntities = this.context
                                                                  .getBean( LinkedAccountEntityList.class );
        linkedAccountEntities.findByCustomerUuid( customerUuid );
        linkedAccountEntities.requestAccountOverviewInformation();
        final List<LinkedAccountDTO> linkedAccountDTOs = this.entitiesToDTOs( linkedAccountEntities );
        logMethodEnd( methodName, linkedAccountDTOs );
        return linkedAccountDTOs;
    }

    /**
     * This method will retrieve all of the linked accounts (child to the tradeit_account table) for the TradeIt account.
     * If the TradeIt account is not a manual account, the TradeIt account will be synchronized between TradeIt and
     * the database.
     * @param tradeItAccountUuid
     * @return
     * @throws TradeItAccountNotFoundException
     */
    public List<LinkedAccountDTO> getLinkedAccountsForTradeItAccount( final UUID tradeItAccountUuid )
        throws TradeItAccountNotFoundException
    {
        final String methodName = "getLinkedAccountsForTradeItAccount";
        logMethodBegin( methodName, tradeItAccountUuid );
        final TradeItAccountEntity tradeItAccountEntity;
        try
        {
            tradeItAccountEntity = this.tradeItAccountEntityService
                                       .getEntity( tradeItAccountUuid );
            final LinkedAccountEntityList linkedAccountEntityList = this.context.getBean( LinkedAccountEntityList.class );
            linkedAccountEntityList.findByTradeItAccountUuid( tradeItAccountUuid );
            if ( tradeItAccountEntity.isTradeItAccount() )
            {
                linkedAccountEntityList.requestAccountOverviewInformation();
            }
            else
            {
                tradeItAccountEntity.setLinkedAccounts( linkedAccountEntityList );
            }

            /*
             * Return the values that are currently in the database, the front ent will load those values first and
             * then make another call to get the updated information for each account.
             */
            final List<LinkedAccountDTO> linkedAccountDTOs = this.entitiesToDTOs( linkedAccountEntityList );
            logMethodEnd( methodName, String.format( "returning %d linked account", linkedAccountDTOs.size() ));
            return linkedAccountDTOs;
        }
        catch( VersionedEntityNotFoundException e )
        {
            throw new TradeItAccountNotFoundException( tradeItAccountUuid, e );
        }
    }

    /**
     * Loads all of the linked accounts for the LinkedAccount entity.
     * @param tradeItAccountEntity
     */
    public void loadLinkedAccounts( final TradeItAccountEntity tradeItAccountEntity )
    {
        final String methodName = "getLinkedAccounts";
        logMethodBegin( methodName, tradeItAccountEntity );
        final LinkedAccountEntityList linkedAccountEntities = this.context
            .getBean( LinkedAccountEntityList.class );
        linkedAccountEntities.findByTradeItAccountUuid( tradeItAccountEntity.getUuid() );
        linkedAccountEntities.requestAccountOverviewInformation();
        tradeItAccountEntity.setLinkedAccounts( linkedAccountEntities );
        logMethodEnd( methodName );
    }


    /**
     * Gets the linked account for the {@code tradeItAccountUuid} and the {@code accountNumber}
     * @param tradeItAccountUuid
     * @param accountNumber
     * @return Optional {@code LinkedAccountEntity}
     */
    public Optional<LinkedAccountEntity> getLinkedAccount( final UUID tradeItAccountUuid, final String accountNumber )
    {
        final String methodName = "getLinkedAccountEntities";
        logMethodBegin( methodName, tradeItAccountUuid, accountNumber );
        final LinkedAccountEntity linkedAccountEntity = this.linkedAccountRepository
                                                            .findByTradeItAccountUuidAndAccountNumber( tradeItAccountUuid,
                                                                                                       accountNumber );
        logMethodEnd( methodName, linkedAccountEntity );
        return Optional.ofNullable( linkedAccountEntity );
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
     * @param tradeItAccountUuid
     * @param linkedAccountUuid
     * @return Updated linked account.
     * @throws IllegalArgumentException if the {@code linkedAccountId} is not registered to be updated.
     */
    public LinkedAccountDTO getUpdatedLinkedAccount( final UUID tradeItAccountUuid,
                                                     final UUID linkedAccountUuid,
                                                     final String accountNumber )
    {
        final String methodName = "getUpdatedLinkedAccount";
        logMethodBegin( methodName, tradeItAccountUuid, linkedAccountUuid );
        Objects.requireNonNull( tradeItAccountUuid, "tradeItAccountUUID argument cannot be null" );
        Objects.requireNonNull( linkedAccountUuid, "linkedAccountUuid argument cannot be null" );
        Objects.requireNonNull( accountNumber, "accountNumber argument cannot be null" );
        logDebug( methodName, "blocking for linked account: {0}", linkedAccountUuid );
        final LinkedAccountEntityCacheDataReceiver receiver = LinkedAccountEntityCacheDataReceiver.newInstance( tradeItAccountUuid,
                                                                                                                linkedAccountUuid,
                                                                                                                accountNumber );
        /*
         * block and wait for the background task to complete.
         */
        this.linkedAccountEntityCacheClient
            .synchronousGetCachedData( receiver );
        logDebug( methodName, "received after blocking {0}", receiver );
        final LinkedAccountEntity linkedAccountEntity = receiver.getCachedData();
        final LinkedAccountDTO linkedAccountDTO = this.entityToDTO( linkedAccountEntity );
        logMethodEnd( methodName, linkedAccountDTO );
        return linkedAccountDTO;
    }
    /**
     * Override to convert the binary uuid for the TradeIt account to a string uuid.
     * @param entity Contains the entity information.
     * @return
     */
    @Override
    public LinkedAccountDTO entityToDTO( final LinkedAccountEntity entity )
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
    public LinkedAccountEntity dtoToEntity( final LinkedAccountDTO dto )
    {
        final LinkedAccountEntity linkedAccountEntity = super.dtoToEntity( dto );
        if ( dto.getTradeItAccountId() != null )
        {
            linkedAccountEntity.setTradeItAccountUuid( UUIDUtil.uuid( dto.getTradeItAccountId() ) );
        }
        return linkedAccountEntity;
    }

    /**
     * Need to lookup the LinkedAccount entity before adding.
     * @param linkedAccountEntity
     * @throws VersionedEntityNotFoundException if the parent TradeIt account entity cannot be found.
     */
    @Override
    protected void preAddEntity( final LinkedAccountEntity linkedAccountEntity )
        throws VersionedEntityNotFoundException
    {
        final TradeItAccountEntity tradeItAccountEntity = this.tradeItAccountEntityService
                                                              .getEntity( linkedAccountEntity.getTradeItAccountUuid() );
        linkedAccountEntity.setTradeItAccountUuid( tradeItAccountEntity.getUuid() );
        super.preAddEntity( linkedAccountEntity );
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

}
