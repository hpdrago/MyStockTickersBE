package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.AccountNotFoundException;
import com.stocktracker.repositorylayer.entity.AccountEntity;
import com.stocktracker.repositorylayer.entity.PortfolioEntity;
import com.stocktracker.repositorylayer.repository.AccountRepository;
import com.stocktracker.repositorylayer.repository.PortfolioRepository;
import com.stocktracker.weblayer.dto.AccountDTO;
import com.stocktracker.weblayer.dto.PortfolioDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private PortfolioService portfolioService;
    private AccountRepository accountRepository;
    private PortfolioRepository portfolioRepository;

    /**
     * Get the account by id request
     * @param id
     * @return
     */
    public AccountDTO getAccountById( final int id )
    {
        final String methodName = "getAccountById";
        logMethodBegin( methodName, id );
        /*
         * Get the account entity
         */
        AccountEntity accountEntity = accountRepository.findOne( id );
        if ( accountEntity == null )
        {
            throw new AccountNotFoundException( id );
        }
        AccountDTO accountDTO = this.entityToDTO( accountEntity );
        logMethodEnd( methodName, accountDTO );
        return accountDTO;
    }

    /**
     * Updates the database with the information in {@code accountDTO}
     * @param accountDTO
     */
    public void updateAccount( final AccountDTO accountDTO )
    {
        final String methodName = "updateAccount";
        logMethodBegin( methodName, accountDTO );
        AccountEntity accountEntity = this.dtoToEntity( accountDTO );
        this.accountRepository.save( accountEntity );
        logMethodEnd( methodName );
    }

    /**
     * Get all of the accounts for a accounts
     * @param accountId
     * @return
     */
    /*
    public List<AccountDTO> getAccounts( final int accountId )
    {
        final String methodName = "getAllAccounts";
        logMethodBegin( methodName, accountId );
        List<AccountEntity> accountEntities = accountRepository.();
        if ( accountEntities == null )
        {
            throw new AccountNotFoundException( "There are no accounts" );
        }
        List<AccountDTO> accountDTOs = this.entitiesToDTOs( accountEntities );
        logMethodEnd( methodName, accountDTOs );
        return accountDTOs;
    }
    */

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
    public void setPortfolioRepository( final PortfolioRepository portfolioRepository )
    {
        this.portfolioRepository = portfolioRepository;
    }

    @Autowired
    public void setAccountRepository( final AccountRepository accountRepository )
    {
        this.accountRepository = accountRepository;
    }

    @Autowired
    public void setPortfolioService( final PortfolioService portfolioService )
    {
        this.portfolioService = portfolioService;
    }

}
