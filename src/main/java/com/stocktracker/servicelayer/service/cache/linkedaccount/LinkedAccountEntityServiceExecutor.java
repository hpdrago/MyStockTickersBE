package com.stocktracker.servicelayer.service.cache.linkedaccount;

import com.stocktracker.AppConfig;
import com.stocktracker.common.exceptions.DuplicateEntityException;
import com.stocktracker.common.exceptions.LinkedAccountNotFoundException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.repositorylayer.entity.TradeItAccountEntity;
import com.stocktracker.servicelayer.service.LinkedAccountEntityService;
import com.stocktracker.servicelayer.service.TradeItAccountEntityService;
import com.stocktracker.servicelayer.service.TradeItAsyncUpdateService;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDBEntityServiceExecutor;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDataNotFoundException;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDataRequestException;
import com.stocktracker.servicelayer.service.cache.common.BaseAsyncCacheBatchServiceExecutor;
import com.stocktracker.servicelayer.service.cache.common.BaseAsyncCacheServiceExecutor;
import com.stocktracker.servicelayer.tradeit.TradeItService;
import com.stocktracker.servicelayer.tradeit.apiresults.GetAccountOverviewAPIResult;
import io.reactivex.processors.AsyncProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import pl.zankowski.iextrading4j.api.stocks.Quote;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This class makes the calls to the TradeIt API to get the account overview information.
 * Account over information is stored in the linked account table.
 */
@Service
// Proxy target class to get past implementation of the interface and getting a runtime proxy error.
@EnableAsync(proxyTargetClass = true)
public class LinkedAccountEntityServiceExecutor extends BaseAsyncCacheServiceExecutor<UUID,
                                                                                      LinkedAccountEntity,
                                                                                      LinkedAccountEntityCacheAsyncKey>
{
    /**
     * Service for the stock quote entities.
     */
    @Autowired
    private LinkedAccountEntityService linkedAccountEntityService;

    @Autowired
    private TradeItAccountEntityService tradeItAccountEntityService;

    @Autowired
    private TradeItService tradeItService;

    @Autowired
    private TradeItAsyncUpdateService tradeItAsyncUpdateService;

    /**
     * Get the account overview from TradeIt
     * @param cacheKey The key to the cache.
     * @param asyncKey The key to the asynchronous data.
     * @return
     * @throws AsyncCacheDataNotFoundException
     * @throws AsyncCacheDataRequestException
     */
    @Override
    public LinkedAccountEntity getASyncData( final UUID cacheKey, final LinkedAccountEntityCacheAsyncKey asyncKey )
        throws AsyncCacheDataNotFoundException
    {
        final String methodName = "getASyncData";
        logMethodBegin( methodName, cacheKey, asyncKey );
        GetAccountOverviewAPIResult accountOverview = null;
        LinkedAccountEntity linkedAccountEntity = null;
        try
        {
            final TradeItAccountEntity tradeItAccountEntity = this.tradeItAccountEntityService
                                                                  .getEntity( asyncKey.getTradeItAccountUuid() );
            linkedAccountEntity = this.linkedAccountEntityService
                                      .getEntity( asyncKey.getLinkedAccountUuid() );
            tradeItAsyncUpdateService.updateLinkedAccount( tradeItAccountEntity, linkedAccountEntity );
        }
        catch( VersionedEntityNotFoundException e )
        {
            throw new AsyncCacheDataNotFoundException( "Linked account for UUID: " + asyncKey.getTradeItAccountUuid() +
                                                       " was not found" );
        }
        logMethodEnd( methodName, linkedAccountEntity );
        return linkedAccountEntity;
    }

    /**
     * This method, when call is called on a new thread launched and managed by the Spring container.
     * In the new thread, the TradeIt account overview quote will be retrieved and the caller will be notified through
     * the {@code observable}
     * @param linkedAccountEntity
     * @param subject Behaviour subject to use to notify the caller that the request has been completed.
     */
    @Async( AppConfig.LINKED_ACCOUNT_GET_OVERVIEW_THREAD_POOL )
    @Override
    public void asynchronousFetch( final LinkedAccountEntity linkedAccountEntity,
                                   final AsyncProcessor<LinkedAccountEntity> subject )
    {
        final String methodName = "asynchronousFetch";
        logMethodBegin( methodName, linkedAccountEntity );
        /*
         * The super class calls this.getASyncData and takes care of the subject notification.
         */
        super.asynchronousFetch( linkedAccountEntity, subject );
        logMethodEnd( methodName );
    }
}
