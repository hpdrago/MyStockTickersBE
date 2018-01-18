package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.repositorylayer.repository.LinkedAccountRepository;
import com.stocktracker.weblayer.dto.LinkedAccountDTO;
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
public class LinkedAccountEntityService extends BaseEntityService<LinkedAccountEntity, LinkedAccountDTO> implements MyLogger
{
    private LinkedAccountRepository linkedAccountRepository;

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
        final List<LinkedAccountEntity> linkedAccountEntities = this.linkedAccountRepository
                                                                    .findAllByCustomerIdAndParentAccountId( customerId,
                                                                                                            tradeItAccountId );
        final List<LinkedAccountDTO> linkedAccountDTOs = this.entitiesToDTOs( linkedAccountEntities );
        logMethodEnd( methodName, linkedAccountDTOs );
        return linkedAccountDTOs;
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

    @Autowired
    public void setLinkedAccountRepository( final LinkedAccountRepository linkedAccountRepository )
    {
        this.linkedAccountRepository = linkedAccountRepository;
    }
}
