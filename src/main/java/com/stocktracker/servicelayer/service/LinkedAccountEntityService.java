package com.stocktracker.servicelayer.service;

import com.stocktracker.common.EntityRefreshStatus;
import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.LinkedAccountNotFoundException;
import com.stocktracker.common.exceptions.TradeItAccountNotFoundException;
import com.stocktracker.common.exceptions.TradeItAuthenticationException;
import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.repositorylayer.entity.TradeItAccountEntity;
import com.stocktracker.repositorylayer.repository.LinkedAccountRepository;
import com.stocktracker.servicelayer.tradeit.TradeItService;
import com.stocktracker.weblayer.dto.LinkedAccountDTO;
import com.stocktracker.weblayer.dto.tradeit.GetAccountOverviewDTO;
import org.springframework.beans.BeanUtils;
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
public class LinkedAccountEntityService extends DMLEntityService<Integer,
                                                                 LinkedAccountEntity,
                                                                 LinkedAccountDTO,
                                                                 LinkedAccountRepository>
                                        implements MyLogger
{
    private LinkedAccountRepository linkedAccountRepository;
    private LinkedAccountGetOverviewService linkedAccountGetOverviewService;
    private TradeItService tradeItService;
    private TradeItAccountEntityService tradeItAccountEntityService;

    /**
     * This method will retrieve all of the linked accounts (child to the tradeit_account table) for the TradeIt account.
     * @param customerId
     * @param tradeItAccountId
     * @return
     * @throws TradeItAccountNotFoundException
     * @throws LinkedAccountNotFoundException
     * @throws EntityVersionMismatchException
     */
    public List<LinkedAccountDTO> getLinkedAccounts( final int customerId,
                                                     final int tradeItAccountId )
        throws TradeItAccountNotFoundException,
               LinkedAccountNotFoundException,
               EntityVersionMismatchException
    {
        final String methodName = "getLinkedAccounts";
        logMethodBegin( methodName, customerId, tradeItAccountId );
        final TradeItAccountEntity tradeItAccountEntity = this.tradeItAccountEntityService
                                                              .getTradeItAccountEntity( customerId, tradeItAccountId );
        List<LinkedAccountEntity> linkedAccountEntities = this.linkedAccountRepository
                                                              .findAllByCustomerIdAndTradeItAccountId( customerId,
                                                                                                       tradeItAccountId );
        /*
         * Update the accounts to UPDATING as the accounts will be updated asynchronously.
         */
        this.updateGetAccountOverviewStatus( linkedAccountEntities, EntityRefreshStatus.UPDATING );
        /*
         * Need to reload the entities as the version number will have changed after updating the status.
         */
        linkedAccountEntities = this.linkedAccountRepository
            .findAllByCustomerIdAndTradeItAccountId( customerId,
                                                     tradeItAccountId );
        /*
         * These are asynchronous calls to refresh the account overview information
         */
        linkedAccountEntities.forEach( linkedAccountEntity -> this.linkedAccountGetOverviewService
                             .updateLinkedAccount( tradeItAccountEntity, linkedAccountEntity ));
        final List<LinkedAccountDTO> linkedAccountDTOs = this.entitiesToDTOs( linkedAccountEntities );
        logMethodEnd( methodName, String.format( "returning %d linked account", linkedAccountDTOs.size() ));
        return linkedAccountDTOs;
    }

    /**
     * This method will set the get_account_overview_status column to {@code entityRefreshStatus}.
     * @param linkedAccountEntities Entities to update.
     * @param entityRefreshStatus Status to update to.
     * @throws LinkedAccountNotFoundException
     * @throws EntityVersionMismatchException
     */
    public void updateGetAccountOverviewStatus( final List<LinkedAccountEntity> linkedAccountEntities,
                                                final EntityRefreshStatus entityRefreshStatus )
        throws EntityVersionMismatchException,
               LinkedAccountNotFoundException
    {
        final String methodName = "updateGetAccountOverviewStatus";
        logMethodBegin( methodName, entityRefreshStatus );
        for ( LinkedAccountEntity linkedAccountEntity : linkedAccountEntities )
        {
            updateGetAccountOverviewStatus( linkedAccountEntity,
                                            entityRefreshStatus );
        }
        logMethodEnd( methodName );
    }

    /**
     * This method will set the get_account_overview_status column to {@code entityRefreshStatus}.
     * @param linkedAccountEntity The entity to update.
     * @param entityRefreshStatus Status to update to.
     * @return Updated entity instance.  This maybe a "fresher" version of the entity if there was a version mismatch.
     * @throws LinkedAccountNotFoundException
     * @throws EntityVersionMismatchException
     */
    public LinkedAccountEntity updateGetAccountOverviewStatus( final LinkedAccountEntity linkedAccountEntity,
                                                               final EntityRefreshStatus entityRefreshStatus )
        throws LinkedAccountNotFoundException,
               EntityVersionMismatchException
    {
        final String methodName = "updateGetAccountOverviewStatus";
        logMethodBegin( methodName, linkedAccountEntity.getId(), entityRefreshStatus );
        linkedAccountEntity.setGetAccountOverviewStatus( entityRefreshStatus.name() );
        LinkedAccountEntity returnEntity = null;
        try
        {
            returnEntity = this.saveEntity( linkedAccountEntity );
        }
        catch( EntityVersionMismatchException e )
        {
            /*
             * Retrieve and update entity
             */
            returnEntity = this.getLinkedAccountEntity( linkedAccountEntity.getCustomerId(), linkedAccountEntity.getId() );
            returnEntity.setGetAccountOverviewStatus( entityRefreshStatus.name() );
            returnEntity = this.saveEntity( linkedAccountEntity );
        }
        logMethodEnd( methodName );
        return returnEntity;
    }

    /**
     * Contacts TradeIt to get the account summary information for the linked account {@code linkedAccountDTO}.
     * The information retrieved from the TradeIt Result is then copied into the {@code linkedAccountDTO} which should
     * be populated with the database information for the linked account and thus the account summary information is
     * in addition that which is stored in the DB -- hence the use of the DTO here.
     * @param tradeItAccountEntity
     * @param linkedAccountDTO
     * @throws TradeItAuthenticationException
     */
    private void updateAccountSummaryInformation( final TradeItAccountEntity tradeItAccountEntity,
                                                  final LinkedAccountDTO linkedAccountDTO )
        throws TradeItAuthenticationException
    {
        final String methodName = "updateAccountSummaryInformation";
        logMethodBegin( methodName, linkedAccountDTO );
        final GetAccountOverviewDTO getAccountOverviewDTO = this.tradeItService
                                                                .getAccountOverview( tradeItAccountEntity,
                                                                                     linkedAccountDTO.getAccountNumber() );

        linkedAccountDTO.copyAccountSummary( getAccountOverviewDTO );
        logMethodEnd( methodName );
    }

    /**
     * Get the account over view.
     * @param customerId
     * @param tradeItAccountId
     * @param linkedAccountId
     * @return
     * @throws LinkedAccountNotFoundException
     * @throws TradeItAccountNotFoundException
     * @throws TradeItAuthenticationException
     */
    public GetAccountOverviewDTO getAccountOverview( final int customerId, final int tradeItAccountId, final int linkedAccountId )
        throws LinkedAccountNotFoundException,
               TradeItAccountNotFoundException,
               TradeItAuthenticationException
    {
        final String methodName = "getAccountOverview";
        logMethodBegin( methodName, customerId, tradeItAccountId, linkedAccountId );
        final TradeItAccountEntity tradeItAccountEntity = this.tradeItAccountEntityService
                                                              .getTradeItAccountEntity( customerId, tradeItAccountId );
        final LinkedAccountEntity linkedAccountEntity = this.getLinkedAccountEntity( customerId, linkedAccountId );
        final GetAccountOverviewDTO getAccountOverviewDTO = this.tradeItService
                                                                .getAccountOverview( tradeItAccountEntity,
                                                                                     linkedAccountEntity.getAccountNumber() );
        logMethodEnd( methodName );
        return getAccountOverviewDTO;
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
        final LinkedAccountEntity linkedAccountEntity = this.linkedAccountRepository.findById( linkedAccountId );
        if ( linkedAccountEntity == null )
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

    @Override
    protected LinkedAccountDTO entityToDTO( final LinkedAccountEntity linkedAccountEntity )
    {
        final LinkedAccountDTO linkedAccountDTO = new LinkedAccountDTO();
        BeanUtils.copyProperties( linkedAccountEntity, linkedAccountDTO );
        return linkedAccountDTO;
    }

    @Override
    protected LinkedAccountEntity dtoToEntity( final LinkedAccountDTO linkedAccountDTO )
    {
        final LinkedAccountEntity linkedAccountEntity = new LinkedAccountEntity();
        BeanUtils.copyProperties( linkedAccountDTO, linkedAccountEntity );
        return linkedAccountEntity;
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
    public void setLinkedAccountGetOverviewService( final LinkedAccountGetOverviewService linkedAccountGetOverviewService )
    {
        this.linkedAccountGetOverviewService = linkedAccountGetOverviewService;
    }

}
