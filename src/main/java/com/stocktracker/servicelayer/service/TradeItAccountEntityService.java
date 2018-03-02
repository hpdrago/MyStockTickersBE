package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.LinkedAccountNotFoundException;
import com.stocktracker.common.exceptions.TradeItAccountNotFoundException;
import com.stocktracker.common.exceptions.TradeItAuthenticationException;
import com.stocktracker.repositorylayer.entity.CustomerEntity;
import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.repositorylayer.entity.TradeItAccountEntity;
import com.stocktracker.repositorylayer.repository.TradeItAccountRepository;
import com.stocktracker.servicelayer.tradeit.apiresults.KeepSessionAliveAPIResult;
import com.stocktracker.servicelayer.tradeit.types.TradeItAccount;
import com.stocktracker.weblayer.dto.LinkedAccountDTO;
import com.stocktracker.weblayer.dto.TradeItAccountDTO;
import com.stocktracker.weblayer.dto.tradeit.AuthenticateDTO;
import com.stocktracker.weblayer.dto.tradeit.KeepSessionAliveDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

/**
 * This service communicates between the Web layer and Repositories using the Domain Model
 * It is a stateless instance that provides business level services
 *
 * Created by mike on 12/4/2017.
 */
@Service
@Transactional
public class TradeItAccountEntityService extends DMLEntityService<Integer,
                                                                  TradeItAccountEntity,
                                                                  TradeItAccountDTO,
                                                                  TradeItAccountRepository>
    implements MyLogger
{
    private TradeItAccountRepository tradeItAccountRepository;
    private LinkedAccountEntityService linkedAccountEntityService;
    private CustomerEntityService customerService;
    private TradeItAccountComparisonService tradeItAccountComparisonService;

    /**
     * Get the account by id request
     * @param customerId
     * @param accountId
     * @return
     * @throws TradeItAccountNotFoundException
     */
    public TradeItAccountDTO getAccountDTO( final int customerId, final int accountId )
        throws TradeItAccountNotFoundException
    {
        final String methodName = "getAccountDTO";
        logMethodBegin( methodName, customerId, accountId );
        TradeItAccountEntity tradeItAccountEntity = this.getTradeItAccountEntity( customerId, accountId );
        TradeItAccountDTO tradeItAccountDTO = this.entityToDTO( tradeItAccountEntity );
        logMethodEnd( methodName, tradeItAccountDTO );
        return tradeItAccountDTO;
    }

    /**
     * Get the account for the customer and account id.
     * @param customerId
     * @param tradeItAccountId
     * @return
     * @throws TradeItAccountNotFoundException
     */
    public TradeItAccountEntity getTradeItAccountEntity( final int customerId, final int tradeItAccountId )
        throws TradeItAccountNotFoundException
    {
        final String methodName = "getAccountDTO";
        logMethodBegin( methodName, customerId, tradeItAccountId );
        TradeItAccountEntity tradeItAccountEntity = tradeItAccountRepository.findByCustomerIdAndId( customerId, tradeItAccountId );
        if ( tradeItAccountEntity == null )
        {
            throw new TradeItAccountNotFoundException( customerId, tradeItAccountId );
        }
        logMethodEnd( methodName, tradeItAccountEntity );
        return tradeItAccountEntity;
    }

    /**
     * Create the account for the customer.
     * @param customerId
     * @param tradeItAccountDTO
     * @return TradeItAccountDTO
     */
    public TradeItAccountDTO createAccount( final int customerId, final TradeItAccountDTO tradeItAccountDTO )
    {
        final String methodName = "createAccount";
        logMethodBegin( methodName, customerId, tradeItAccountDTO );
        TradeItAccountEntity tradeItAccountEntity = this.dtoToEntity( tradeItAccountDTO );
        tradeItAccountEntity.setVersion( 1 );
        CustomerEntity customerEntity = this.customerService.getCustomerEntity( customerId );
        logDebug( methodName, "customerEntity: {0}", customerEntity );
        tradeItAccountEntity.setCustomerByCustomerId( customerEntity );
        /*
         * Set it to null on create, there were JSON conversion issues on create.
         */
        tradeItAccountEntity.setAuthTimestamp( null );
        tradeItAccountEntity = this.addEntity( tradeItAccountEntity );
        logDebug( methodName, "tradeItAccountEntity: {0}", tradeItAccountEntity );
        TradeItAccountDTO returnTradeItAccountDTO = this.entityToDTO( tradeItAccountEntity );
        logMethodEnd( methodName, returnTradeItAccountDTO );
        return returnTradeItAccountDTO;
    }

    /**
     * This method is called during the GetOAuthAccessTokenAPIResult TradeIt call
     * @param customerId
     * @param broker
     * @param accountName
     * @param userId
     * @param userToken
     * @return
     * @throws EntityVersionMismatchException
     */
    public TradeItAccountDTO createAccount( final int customerId, final String broker, final String accountName,
                                            final String userId, final String userToken )
        throws EntityVersionMismatchException
    {
        final String methodName = "createAccount";
        logMethodBegin( methodName, customerId, broker, accountName, userId, userToken );
        Objects.requireNonNull( broker, "broker cannot be null" );
        Objects.requireNonNull( accountName, "accountName cannot be null" );
        Objects.requireNonNull( userId, "userId cannot be null" );
        Objects.requireNonNull( userToken, "userToken cannot be null" );
        TradeItAccountEntity tradeItAccountEntity = TradeItAccountEntity.newInstance();
        tradeItAccountEntity.setCustomerId( customerId );
        tradeItAccountEntity.setBrokerage( broker );
        tradeItAccountEntity.setName( accountName );
        tradeItAccountEntity.setUserId( userId );
        tradeItAccountEntity.setUserToken( userToken );
        tradeItAccountEntity.setCustomerByCustomerId( this.customerService.getCustomerEntity( customerId ) );
        tradeItAccountEntity = this.saveEntity( tradeItAccountEntity );
        logDebug( methodName, "saved entity: {0}", tradeItAccountEntity );
        TradeItAccountDTO tradeItAccountDTO = this.entityToDTO( tradeItAccountEntity );
        logMethodEnd( methodName, tradeItAccountDTO );
        return tradeItAccountDTO;
    }

    /**
     * Delete the account for the customer.
     * @param customerId
     * @param accountId
     * @throws TradeItAccountNotFoundException
     */
    public void deleteAccount( final int customerId, final int accountId )
        throws TradeItAccountNotFoundException
    {
        final String methodName = "deleteAccount";
        logMethodBegin( methodName, customerId, accountId );
        this.validateAccountId( customerId, accountId );
        this.tradeItAccountRepository.delete( accountId );
        logMethodEnd( methodName );
    }

    /**
     * Updates the database with the information in {@code tradeItAccountDTO}
     * @param customerId
     * @param tradeItAccountDTO
     * @throws EntityVersionMismatchException
     * @throws TradeItAccountNotFoundException
     */
    public TradeItAccountDTO updateAccount( final int customerId, @NotNull final TradeItAccountDTO tradeItAccountDTO )
        throws EntityVersionMismatchException,
               TradeItAccountNotFoundException
    {
        final String methodName = "updateAccount";
        logMethodBegin( methodName, customerId, tradeItAccountDTO );
        this.validateAccountId( customerId, tradeItAccountDTO.getId() );
        final TradeItAccountDTO returnTradeItAccountDTO = super.saveDTO( tradeItAccountDTO );
        logMethodEnd( methodName, returnTradeItAccountDTO );
        return returnTradeItAccountDTO;
    }

    /**
     * Get the list of customer accounts.
     * @param customerId
     * @return
     */
    public List<TradeItAccountDTO> getAccounts( final int customerId )
    {
        final String methodName = "getAccounts";
        logMethodBegin( methodName, customerId );
        List<TradeItAccountEntity> accountEntities = this.tradeItAccountRepository.findByCustomerId( customerId );
        List<TradeItAccountDTO> tradeItAccountDTOS = this.entitiesToDTOs( accountEntities );
        logMethodEnd( methodName, tradeItAccountDTOS );
        return tradeItAccountDTOS;
    }

    /**
     * Determines if the account exists for {@code accountId}
     * @param customerId
     * @param accountId
     * @throws TradeItAccountNotFoundException
     */
    private void validateAccountId( final int customerId, final int accountId )
        throws TradeItAccountNotFoundException
    {
        TradeItAccountEntity tradeItAccountEntity = this.tradeItAccountRepository.findById( accountId );
        if ( tradeItAccountEntity == null )
        {
            throw new TradeItAccountNotFoundException( customerId, accountId ) ;
        }
    }

    /**
     * Saves the account to the database.
     * @param tradeItAccountEntity
     * @return Updated TradeIt Account entity.
     */
    public TradeItAccountEntity saveAccount( final TradeItAccountEntity tradeItAccountEntity )
    {
        final String methodName = "saveAccount";
        logMethodBegin( methodName, tradeItAccountEntity );
        TradeItAccountEntity returnEntity = this.tradeItAccountRepository
                                                .save( tradeItAccountEntity );
        logMethodEnd( methodName, returnEntity );
        return returnEntity;
    }

    /**
     * This method is called after a successful authentication call.  This is a wrapper method for the follow method
     * with the same name.
     * @param customerId
     * @param tradeItAccountId
     * @param authenticateDTO
     * @throws LinkedAccountNotFoundException
     * @throws TradeItAccountNotFoundException
     * @throws EntityVersionMismatchException
     */
    public void synchronizeTradeItAccount( final int customerId,
                                           final int tradeItAccountId,
                                           final AuthenticateDTO authenticateDTO )
        throws LinkedAccountNotFoundException,
               TradeItAccountNotFoundException,
               EntityVersionMismatchException
    {
        final String methodName = "synchronizeTradeItAccount";
        logMethodBegin( methodName, customerId, tradeItAccountId, authenticateDTO );
        final TradeItAccountEntity tradeItAccountEntity = this.tradeItAccountRepository
                                                              .findByCustomerIdAndId( customerId, tradeItAccountId );
        this.synchronizeTradeItAccount( tradeItAccountEntity, authenticateDTO );
        logMethodEnd( methodName );
    }

    /**
     * This method is called when the user has been authenticated.
     *
     * Also, the LINKED_ACCOUNT table is updated (added/updated -- not deleted) to reflect the {@code TradeItAccount}
     * accounts returned from the authentication call.
     * @param tradeItAccountEntity
     * @param authenticateDTO The linked accounts will be set in this DTO from the linked accounts.
     * @throws LinkedAccountNotFoundException
     * @throws TradeItAccountNotFoundException
     * @throws EntityVersionMismatchException
     */
    public void synchronizeTradeItAccount( final TradeItAccountEntity tradeItAccountEntity,
                                           final AuthenticateDTO authenticateDTO )
        throws LinkedAccountNotFoundException,
               TradeItAccountNotFoundException,
               EntityVersionMismatchException
    {
        final String methodName = "synchronizeTradeItAccount";
        logMethodBegin( methodName, tradeItAccountEntity, authenticateDTO );
        this.tradeItAccountComparisonService
            .compare( tradeItAccountEntity, authenticateDTO );
        /*
         * Gather up all of the linked accounts and add them to the authenticate DTO.
         */
        List<LinkedAccountDTO> linkedAccountDTOs = this.linkedAccountEntityService
                                                       .getLinkedAccounts( tradeItAccountEntity.getCustomerId(),
                                                                           tradeItAccountEntity.getId() );
        authenticateDTO.setLinkedAccounts( linkedAccountDTOs );
        /*
         * Need to send back the updated account DTO
         */
        final TradeItAccountDTO tradeItAccountDTO = this.entityToDTO( tradeItAccountEntity );
        authenticateDTO.setTradeItAccount( tradeItAccountDTO );
        logMethodEnd( methodName, authenticateDTO );
    }

    /**
     * This method is called after a successful renewal of the user's authentication token.
     * {@code keepSessionAliveDTO} is populated with the TradeIt Account and linked account information.
     * @param tradeItAccountEntity
     * @param keepSessionAliveAPIResult
     * @throws TradeItAccountNotFoundException
     * @throws TradeItAccountNotFoundException,
     * @throws EntityVersionMismatchException
     */
    public void keepSessionAliveSuccess( final KeepSessionAliveDTO keepSessionAliveDTO,
                                         final TradeItAccountEntity tradeItAccountEntity,
                                         final KeepSessionAliveAPIResult keepSessionAliveAPIResult )
        throws TradeItAccountNotFoundException,
               EntityVersionMismatchException,
               LinkedAccountNotFoundException
    {
        final String methodName = "keepSessionAliveSuccess";
        logMethodBegin( methodName, tradeItAccountEntity, keepSessionAliveAPIResult );
        tradeItAccountEntity.setAuthTimestamp( new Timestamp( System.currentTimeMillis() ) );
        this.tradeItAccountRepository.save( tradeItAccountEntity );
        final TradeItAccountDTO tradeItAccountDTO = this.entityToDTO( tradeItAccountEntity );
        final List<LinkedAccountDTO> linkedAccountDTOs = this.linkedAccountEntityService
                                                             .getLinkedAccounts( tradeItAccountEntity.getCustomerId(),
                                                                                 tradeItAccountEntity.getId() );
        keepSessionAliveDTO.setTradeItAccount( tradeItAccountDTO );
        keepSessionAliveDTO.setLinkedAccounts( linkedAccountDTOs );
        logMethodEnd( methodName, tradeItAccountDTO );
    }

    /**
     * Adds a new linked account to the {@code tradeItAccountEntity} and the database.
     * @param tradeItAccountEntity
     * @param tradeItAccount The account information from TradeIt.
     * @throws EntityVersionMismatchException
     */
    public void addLinkedAccount( final TradeItAccountEntity tradeItAccountEntity,
                                  final TradeItAccount tradeItAccount )
        throws EntityVersionMismatchException
    {
        final String methodName = "addLinkedAccount";
        logMethodBegin( methodName, tradeItAccountEntity, tradeItAccount );
        /*
         * Convert the account information return from TradeIt to a LinkedEntityAccount instance.
         */
        final LinkedAccountEntity linkedAccountEntity = LinkedAccountEntity.newInstance( tradeItAccount );
        /*
         * Set the bidirectional relationship
         */
        linkedAccountEntity.setAccountByTradeItAccountId( tradeItAccountEntity );
        tradeItAccountEntity.addLinkedAccount( linkedAccountEntity );
        /*
         * Save the linked account
         */
        this.linkedAccountEntityService
            .saveEntity( linkedAccountEntity );
        logMethodEnd( methodName );
    }

    @Override
    protected TradeItAccountDTO entityToDTO( final TradeItAccountEntity entity )
    {
        Objects.requireNonNull( entity );
        TradeItAccountDTO tradeItAccountDTO = TradeItAccountDTO.newInstance();
        BeanUtils.copyProperties( entity, tradeItAccountDTO );
        return tradeItAccountDTO;
    }

    @Override
    protected TradeItAccountEntity dtoToEntity( final TradeItAccountDTO dto )
    {
        Objects.requireNonNull( dto );
        TradeItAccountEntity tradeItAccountEntity = TradeItAccountEntity.newInstance();
        BeanUtils.copyProperties( dto, tradeItAccountEntity );
        return tradeItAccountEntity;
    }

    @Override
    protected TradeItAccountRepository getRepository()
    {
        return this.tradeItAccountRepository;
    }

    @Autowired
    public void setTradeItAccountRepository( final TradeItAccountRepository tradeItAccountRepository )
    {
        this.tradeItAccountRepository = tradeItAccountRepository;
    }

    @Autowired
    public void setLinkedAccountEntityService( final LinkedAccountEntityService linkedAccountEntityService )
    {
        this.linkedAccountEntityService = linkedAccountEntityService;
    }

    @Autowired
    public void setCustomerService( final CustomerEntityService customerService )
    {
        this.customerService = customerService;
    }

    @Autowired
    public void setTradeItAccountComparisonService( final TradeItAccountComparisonService tradeItAccountComparisonService )
    {
        this.tradeItAccountComparisonService = tradeItAccountComparisonService;
    }
}
