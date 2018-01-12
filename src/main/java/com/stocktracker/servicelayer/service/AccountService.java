package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.AccountNotFoundException;
import com.stocktracker.repositorylayer.entity.AccountEntity;
import com.stocktracker.repositorylayer.entity.CustomerEntity;
import com.stocktracker.repositorylayer.repository.AccountRepository;
import com.stocktracker.weblayer.dto.AccountDTO;
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
public class AccountService extends BaseService<AccountEntity, AccountDTO> implements MyLogger
{
    private AccountRepository accountRepository;
    private CustomerService customerService;

    /**
     * Get the account by id request
     * @param customerId
     * @param accountId
     * @return
     * @throws AccountNotFoundException
     */
    public AccountDTO getAccountDTO( final int customerId, final int accountId )
    {
        final String methodName = "getAccountDTO";
        logMethodBegin( methodName, customerId, accountId );
        AccountEntity accountEntity = this.getAccountEntity( customerId, accountId );
        AccountDTO accountDTO = this.entityToDTO( accountEntity );
        logMethodEnd( methodName, accountDTO );
        return accountDTO;
    }

    /**
     * Get the account for the customer and account id.
     * @param customerId
     * @param accountId
     * @return
     * @throws AccountNotFoundException
     */
    public AccountEntity getAccountEntity( final int customerId, final int accountId )
    {
        AccountEntity accountEntity = accountRepository.findOne( accountId );
        if ( accountEntity == null )
        {
            throw new AccountNotFoundException( customerId, accountId );
        }
        return accountEntity;
    }

    /**
     * Create the account for the customer.
     * @param customerId
     * @param accountDTO
     * @return AccountDTO
     */
    public AccountDTO createAccount( final int customerId, final AccountDTO accountDTO )
    {
        final String methodName = "createAccount";
        logMethodBegin( methodName, customerId, accountDTO );
        AccountEntity accountEntity = this.dtoToEntity( accountDTO );
        CustomerEntity customerEntity = this.customerService.getCustomerEntity( customerId );
        logDebug( methodName, "customerEntity: {0}", customerEntity );
        accountEntity.setCustomerByCustomerId( customerEntity );
        accountEntity = this.accountRepository.save( accountEntity );
        logDebug( methodName, "accountEntity: {0}", accountEntity );
        AccountDTO returnAccountDTO = this.entityToDTO( accountEntity );
        logMethodEnd( methodName, returnAccountDTO );
        return returnAccountDTO;
    }

    /**
     * This method is called during the GetOAuthAccessTokenAPIResult TradeIt call
     * @param customerId
     * @param broker
     * @param accountName
     * @param userId
     * @param userToken
     * @return
     */
    public AccountDTO createAccount( final int customerId, final String broker, final String accountName,
                                        final String userId, final String userToken )
    {
        final String methodName = "createAccount";
        logMethodBegin( methodName, customerId, broker, accountName );
        Objects.requireNonNull( broker, "broker cannot be null" );
        Objects.requireNonNull( accountName, "accountName cannot be null" );
        Objects.requireNonNull( userId, "userId cannot be null" );
        Objects.requireNonNull( userToken, "userToken cannot be null" );
        AccountEntity accountEntity = AccountEntity.newInstance();
        accountEntity.setCustomerId( customerId );
        accountEntity.setBrokerage( broker );
        accountEntity.setName( accountName );
        accountEntity.setUserId( userId );
        accountEntity.setUserToken( userToken );
        accountEntity.setCustomerByCustomerId( this.customerService.getCustomerEntity( customerId ) );
        accountEntity = this.accountRepository.save( accountEntity );
        AccountDTO accountDTO = this.entityToDTO( accountEntity );
        logMethodEnd( methodName, accountDTO );
        return accountDTO;
    }

    /**
     * Delete the account for the customer.
     * @param customerId
     * @param accountId
     */
    public void deleteAccount( final int customerId, final int accountId )
    {
        final String methodName = "createAccount";
        logMethodBegin( methodName, customerId, accountId );
        this.validateAccountId( customerId, accountId );
        this.accountRepository.delete( accountId );
        logMethodEnd( methodName );
    }

    /**
     * Updates the database with the information in {@code accountDTO}
     * @param customerId
     * @param accountDTO
     */
    public AccountDTO updateAccount( final int customerId, @NotNull final AccountDTO accountDTO )
    {
        final String methodName = "updateAccount";
        logMethodBegin( methodName, customerId, accountDTO );
        this.validateAccountId( customerId, accountDTO.getId() );
        AccountEntity accountEntity = this.dtoToEntity( accountDTO );
        accountEntity = this.accountRepository.save( accountEntity );
        AccountDTO returnAccountDTO = this.entityToDTO( accountEntity );
        logMethodEnd( methodName, returnAccountDTO );
        return returnAccountDTO;
    }

    /**
     * Get the list of customer accounts.
     * @param customerId
     * @return
     */
    public List<AccountDTO> getAccounts( final int customerId )
    {
        final String methodName = "getAccounts";
        logMethodBegin( methodName, customerId );
        List<AccountEntity> accountEntities = this.accountRepository.findByCustomerId( customerId );
        List<AccountDTO> accountDTOs = this.entitiesToDTOs( accountEntities );
        logMethodEnd( methodName, accountDTOs );
        return accountDTOs;
    }

    /**
     * Determines if the account exists for {@code accountId}
     * @param customerId
     * @param accountId
     * @throws AccountNotFoundException
     */
    private void validateAccountId( final int customerId, final int accountId )
    {
        AccountEntity accountEntity = this.accountRepository.findById( accountId );
        if ( accountEntity == null )
        {
            throw new AccountNotFoundException( customerId, accountId ) ;
        }
    }

    /**
     * Saves the account to the database.
     * @param accountEntity
     * @return
     */
    public AccountEntity saveAccount( final AccountEntity accountEntity )
    {
        final String methodName = "saveAccount";
        logMethodBegin( methodName, accountEntity );
        this.accountRepository.save( accountEntity );
        logMethodEnd( methodName, accountEntity );
        return accountEntity;
    }

    /**
     * This method is called when the user has been authenticated.  The account table auth_timestamp is set so that
     * we know when the authentication will expire which is after 15 minutes.
     * @param accountEntity
     */
    public void authenticationSuccessful( final AccountEntity accountEntity )
    {
        final String methodName = "authenticationSuccessful";
        logMethodBegin( methodName, accountEntity );
        /*
         * The timestamp is set and the UUID and Token are nulled because they need to be new values for the next
         * authentication.
         */
        accountEntity.setAuthTimestamp( new Timestamp( System.currentTimeMillis() ) );
        accountEntity.setAuthUUID( null );
        accountEntity.setAuthToken( null );
        this.accountRepository.save( accountEntity );
        logMethodEnd( methodName, accountEntity );
    }

    @Override
    protected AccountDTO entityToDTO( final AccountEntity entity )
    {
        Objects.requireNonNull( entity );
        AccountDTO accountDTO = AccountDTO.newInstance();
        BeanUtils.copyProperties( entity, accountDTO );
        return accountDTO;
    }

    @Override
    protected AccountEntity dtoToEntity( final AccountDTO dto )
    {
        Objects.requireNonNull( dto );
        AccountEntity accountEntity = AccountEntity.newInstance();
        BeanUtils.copyProperties( dto, accountEntity );
        return accountEntity;
    }

    @Autowired
    public void setAccountRepository( final AccountRepository accountRepository )
    {
        this.accountRepository = accountRepository;
    }

    @Autowired
    public void setCustomerService( final CustomerService customerService )
    {
        this.customerService = customerService;
    }
}
