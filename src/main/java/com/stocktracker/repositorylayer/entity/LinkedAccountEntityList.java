package com.stocktracker.repositorylayer.entity;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.repository.LinkedAccountRepository;
import com.stocktracker.servicelayer.service.cache.linkedaccount.LinkedAccountEntityCacheClient;
import com.stocktracker.servicelayer.service.cache.linkedaccount.LinkedAccountEntityCacheDataReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class LinkedAccountEntityList extends BaseEntityList<UUID,LinkedAccountEntity> implements MyLogger
{
    @Autowired
    private LinkedAccountRepository linkedAccountRepository;

    @Autowired
    private LinkedAccountEntityCacheClient linkedAccountEntityCacheClient;

    /**
     * Get all of the LinkedAccountEntity instances for the {@code tradeItAccountId}.
     * @param tradeItAccountUuid
     * @return
     */
    public void findByTradeItAccountUuid( final UUID tradeItAccountUuid )
    {
        final String methodName = "findByTradeItAccountUuid";
        logMethodBegin( methodName, tradeItAccountUuid );
        Objects.requireNonNull( tradeItAccountUuid );
        final List<LinkedAccountEntity> linkedAccountEntities = this.linkedAccountRepository
                                                                    .findAllByTradeItAccountUuid( tradeItAccountUuid );
        this.addEntities( linkedAccountEntities );
        logMethodEnd( methodName, linkedAccountEntities );
    }

    /**
     * Get all of the linked accounts for a customer uuid.
     * @param customerUuid
     */
    public void findByCustomerUuid( final UUID customerUuid )
    {
        final String methodName = "findByCustomerUuid";
        logMethodBegin( methodName, customerUuid );
        Objects.requireNonNull( customerUuid );
        final List<LinkedAccountEntity> linkedAccountEntities = this.linkedAccountRepository
                                                                    .findByCustomerUuid( customerUuid );
        this.addEntities( linkedAccountEntities );
        logMethodEnd( methodName, linkedAccountEntities );
    }

    /**
     * Getting the account overview information is a two step process because it is asynchronous in nature.
     * The first step is to get the linked accounts to be updated in the {@code LinkedAccountAsyncCache} this is
     * done first so that the service knows about the accounts that need to be updated in case the front end calls
     * to get the updated information before the second stop starts.
     * The next is to make the asynchronous calls to update the account overview status.
     *
     * These are asynchronous calls to refresh the account overview information
     */


    /**
     * Works with the {@code LinkedAccountSyncCache} to make asynchronous requests to get the account overview information
     * from TradeIt.  An asynchronous request is made for each {@code LinkedAccount}.
     */
    public void requestAccountOverviewInformation()
    {
        final String methodName = "getAccountOverviewInformation";
        logMethodBegin( methodName );
        forEach( linkedAccountEntity ->
                 {
                     final LinkedAccountEntityCacheDataReceiver receiver =
                         LinkedAccountEntityCacheDataReceiver.newInstance( linkedAccountEntity.getTradeItAccountUuid(),
                                                                           linkedAccountEntity.getId(),
                                                                            linkedAccountEntity.getAccountNumber() );
                     this.linkedAccountEntityCacheClient
                         .asynchronousGetCachedData( receiver );
                 });
        logMethodEnd( methodName );
    }


}
