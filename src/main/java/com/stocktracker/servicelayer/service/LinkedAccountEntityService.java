package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.LinkedAccountNotFoundException;
import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.repositorylayer.entity.TradeItAccountEntity;
import com.stocktracker.repositorylayer.repository.LinkedAccountRepository;
import com.stocktracker.servicelayer.tradeit.TradeItService;
import com.stocktracker.weblayer.dto.LinkedAccountDTO;
import com.stocktracker.weblayer.dto.tradeit.GetAccountOverviewDTO;
import com.stocktracker.weblayer.dto.tradeit.GetPositionsDTO;
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
    private TradeItService tradeItService;
    private TradeItAccountEntityService tradeItAccountEntityService;

    /**
     * Get the positions for the linked account.
     * @param customerId Used for validation.
     * @param tradeItAccountId Need to retrieve the authToken from the TradeIt account.
     * @param linkedAccountId Need to get the account number from the linked account.
     * @return
     */
    public GetPositionsDTO getPositions( final int customerId, final int tradeItAccountId, final int linkedAccountId )
        throws LinkedAccountNotFoundException
    {
        final String methodName = "getPositions";
        logMethodBegin( methodName, customerId, tradeItAccountId, linkedAccountId );
        final TradeItAccountEntity tradeItAccountEntity = this.tradeItAccountEntityService
                                                              .getAccountEntity( customerId, tradeItAccountId );
        final LinkedAccountEntity linkedAccountEntity = this.getLinkedAccountEntity( customerId, linkedAccountId );
        final GetPositionsDTO getPositionsDTO = this.tradeItService
                                                    .getPositions( linkedAccountEntity.getAccountNumber(),
                                                                   tradeItAccountEntity.getAuthToken() );
        logMethodEnd( methodName );
        return getPositionsDTO;
    }

    /**
     * This method will retrieve all of the linked accounts (child to the tradeit_account table) for the TradeIt account.
     * @param customerId
     * @param tradeItAccountId
     * @return
     */
    public List<LinkedAccountDTO> getLinkedAccounts( final int customerId, final int tradeItAccountId )
    {
        final String methodName = "getLinkedAccounts";
        logMethodBegin( methodName, customerId, tradeItAccountId );
        final TradeItAccountEntity tradeItAccountEntity = this.tradeItAccountEntityService
                                                              .getAccountEntity( customerId, tradeItAccountId );
        final List<LinkedAccountEntity> linkedAccountEntities = this.linkedAccountRepository
                                                                    .findAllByCustomerIdAndParentAccountId( customerId,
                                                                                                            tradeItAccountId );
        final List<LinkedAccountDTO> linkedAccountDTOs = this.entitiesToDTOs( linkedAccountEntities );
        linkedAccountDTOs.forEach( linkedAccountDTO -> this.updateAccountSummaryInformation( tradeItAccountEntity.getAuthToken(),
                                                                                             linkedAccountDTO ) );
        logMethodEnd( methodName, linkedAccountDTOs );
        return linkedAccountDTOs;
    }

    /**
     * Contacts TradeIt to get the account summary information for the linked account {@code linkedAccountDTO}.
     * The information retrieved from the TradeIt Result is then copied into the {@code linkedAccountDTO} which should
     * be populated with the database information for the linked account and thus the account summary information is
     * in addition that which is stored in the DB -- hence the use of the DTO here.
     * @param authToken
     * @param linkedAccountDTO
     */
    private void updateAccountSummaryInformation( final String authToken, final LinkedAccountDTO linkedAccountDTO )
    {
        final String methodName = "updateAccountSummaryInformation";
        logMethodBegin( methodName, linkedAccountDTO );
        final GetAccountOverviewDTO getAccountOverviewDTO = this.tradeItService
                                                                .getAccountOverview( linkedAccountDTO.getAccountNumber(),
                                                                                     authToken );
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
     */
    public GetAccountOverviewDTO getAccountOverview( final int customerId, final int tradeItAccountId, final int linkedAccountId )
        throws LinkedAccountNotFoundException
    {
        final String methodName = "getAccountOverview";
        logMethodBegin( methodName, customerId, tradeItAccountId, linkedAccountId );
        final TradeItAccountEntity tradeItAccountEntity = this.tradeItAccountEntityService
            .getAccountEntity( customerId, tradeItAccountId );
        final LinkedAccountEntity linkedAccountEntity = this.getLinkedAccountEntity( customerId, linkedAccountId );
        final GetAccountOverviewDTO getAccountOverviewDTO = this.tradeItService
            .getAccountOverview( linkedAccountEntity.getAccountNumber(),
                                 tradeItAccountEntity.getAuthToken() );
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

    /**
     * Save the linked account.
     * @param linkedAccountDTO
     * @return
     */
    public LinkedAccountDTO saveLinkedAccount( final LinkedAccountDTO linkedAccountDTO )
        throws EntityVersionMismatchException
    {
        final String methodName = "saveLinkedAccount";
        logMethodBegin( methodName, linkedAccountDTO );
        LinkedAccountDTO returnLinkedAccountDTO = super.saveEntity( linkedAccountDTO );
        logMethodEnd( methodName, returnLinkedAccountDTO );
        return returnLinkedAccountDTO;
    }

    /**
     * Save the linked account to the database.
     * @param linkedAccountEntity
     */
    public void saveLinkedAccount( final LinkedAccountEntity linkedAccountEntity )
    {
        final String methodName = "getLinkedAccounts";
        logMethodBegin( methodName, linkedAccountEntity );
        this.linkedAccountRepository.save( linkedAccountEntity );
        logMethodEnd( methodName );
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
}
