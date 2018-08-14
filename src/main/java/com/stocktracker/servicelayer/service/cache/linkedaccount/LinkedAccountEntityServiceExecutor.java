package com.stocktracker.servicelayer.service.cache.linkedaccount;

import com.stocktracker.AppConfig;
import com.stocktracker.common.exceptions.DuplicateEntityException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.repositorylayer.entity.TradeItAccountEntity;
import com.stocktracker.servicelayer.service.LinkedAccountEntityService;
import com.stocktracker.servicelayer.service.TradeItAccountEntityService;
import com.stocktracker.servicelayer.service.TradeItAsyncUpdateService;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDataNotFoundException;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDataRequestException;
import com.stocktracker.servicelayer.service.cache.common.BaseAsyncCacheServiceExecutor;
import com.stocktracker.servicelayer.tradeit.TradeItService;
import com.stocktracker.servicelayer.tradeit.apiresults.GetAccountOverviewAPIResult;
import io.reactivex.processors.AsyncProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

/**
 * This class makes the calls to the TradeIt API to get the account overview information.
 * Account over information is stored in the linked account table.
 */
@Service
// Proxy target class to get past implementation of the interface and getting a runtime proxy error.
@EnableAsync(proxyTargetClass = true)
public class LinkedAccountEntityServiceExecutor extends BaseAsyncCacheServiceExecutor<GetAccountOverviewAsyncCacheKey,
                                                                                      GetAccountOverviewAPIResult>
{
    @Autowired
    private TradeItAccountEntityService tradeItAccountEntityService;

    @Autowired
    private TradeItService tradeItService;

    /**
     * Get the account overview from TradeIt
     * @param asyncKey The key to the asynchronous data.
     * @return
     * @throws AsyncCacheDataNotFoundException
     * @throws AsyncCacheDataRequestException
     */
    @Override
    public GetAccountOverviewAPIResult getASyncData( final GetAccountOverviewAsyncCacheKey asyncKey )
        throws AsyncCacheDataNotFoundException,
               AsyncCacheDataRequestException
    {
        final String methodName = "getASyncData";
        logMethodBegin( methodName, asyncKey );
        GetAccountOverviewAPIResult accountOverview = null;
        LinkedAccountEntity linkedAccountEntity = null;
        try
        {
            final TradeItAccountEntity tradeItAccountEntity = this.tradeItAccountEntityService
                                                                  .getEntity( asyncKey.getTradeItAccountUuid() );
            accountOverview = this.tradeItService
                                  .getAccountOverview( tradeItAccountEntity, asyncKey.getAccountNumber() );
        }
        catch( VersionedEntityNotFoundException e )
        {
            throw new AsyncCacheDataNotFoundException( "Linked account for UUID: " + asyncKey.getTradeItAccountUuid() +
                                                       " was not found" );
        }
        catch( DuplicateEntityException e )
        {
            throw new AsyncCacheDataRequestException( e );
        }
        logMethodEnd( methodName, linkedAccountEntity );
        return accountOverview;
    }

    /**
     * This method, when call is called on a new thread launched and managed by the Spring container.
     * In the new thread, the TradeIt account overview quote will be retrieved and the caller will be notified through
     * the {@code observable}
     * @param asyncCacheKey
     * @param subject Behaviour subject to use to notify the caller that the request has been completed.
     */
    @Async( AppConfig.LINKED_ACCOUNT_GET_OVERVIEW_THREAD_POOL )
    @Override
    public void asynchronousFetch( final GetAccountOverviewAsyncCacheKey asyncCacheKey,
                                   final AsyncProcessor<GetAccountOverviewAPIResult> subject )
    {
        final String methodName = "asynchronousFetch";
        logMethodBegin( methodName, asyncCacheKey );
        /*
         * The super class calls this.getASyncData and takes care of the subject notification.
         */
        super.asynchronousFetch( asyncCacheKey, subject );
        logMethodEnd( methodName );
    }
}
