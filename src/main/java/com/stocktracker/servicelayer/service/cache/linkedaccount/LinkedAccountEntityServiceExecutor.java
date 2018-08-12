package com.stocktracker.servicelayer.service.cache.linkedaccount;

import com.stocktracker.AppConfig;
import com.stocktracker.common.exceptions.DuplicateEntityException;
import com.stocktracker.common.exceptions.LinkedAccountNotFoundException;
import com.stocktracker.common.exceptions.StockQuoteNotFoundException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.repositorylayer.entity.TradeItAccountEntity;
import com.stocktracker.servicelayer.service.LinkedAccountEntityService;
import com.stocktracker.servicelayer.service.TradeItAccountEntityService;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDBEntityServiceExecutor;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDataNotFoundException;
import com.stocktracker.servicelayer.tradeit.TradeItService;
import com.stocktracker.servicelayer.tradeit.apiresults.GetAccountOverviewAPIResult;
import com.stocktracker.servicelayer.tradeit.types.LinkedAccount;
import io.reactivex.processors.AsyncProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import pl.zankowski.iextrading4j.api.stocks.Quote;

import java.util.ArrayList;
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
public class LinkedAccountEntityServiceExecutor extends AsyncCacheDBEntityServiceExecutor<UUID,
                                                                                          LinkedAccountEntity,
                                                                                          LinkedAccountEntity,
                                                                                          GetAccountOverviewAPIResult,
                                                                                          LinkedAccountEntityService,
                                                                                          LinkedAccountNotFoundException,
                                                                                          LinkedAccountEntityCacheRequest,
                                                                                          LinkedAccountEntityCacheResponse>
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

    /**
     * Get the account overview from TradeIt
     * @param linkedAccountEntity
     * @return
     * @throws AsyncCacheDataNotFoundException
     */
    @Override
    protected GetAccountOverviewAPIResult getThirdPartyData( final LinkedAccountEntity linkedAccountEntity )
        throws AsyncCacheDataNotFoundException
    {
        final String methodName = "getThirdPartyData";
        logMethodBegin( methodName, linkedAccountEntity );
        GetAccountOverviewAPIResult accountOverview = null;
        try
        {
            final TradeItAccountEntity tradeItAccountEntity = this.tradeItAccountEntityService
                                                                  .getEntity( linkedAccountEntity.getTradeItAccountUuid() );
            accountOverview = this.tradeItService
                                  .getAccountOverview( tradeItAccountEntity, linkedAccountEntity.getAccountNumber() );
        }
        catch( DuplicateEntityException | VersionedEntityNotFoundException e )
        {
            logError( methodName, "This should never happen", e );
        }
        logMethodEnd( methodName, accountOverview );
        return accountOverview;
    }

    @Override
    protected UUID getCacheKeyFromThirdPartyData( final GetAccountOverviewAPIResult thirdPartyData )
    {
        return ;
    }

    /**
     * This method, when call is called on a new thread launched and managed by the Spring container.
     * In the new thread, the TradeIt account overview quote will be retrieved and the caller will be notified through
     * the {@code observable}
     * @param linkedAccountEntity
     * @param subject Behaviour subject to use to notify the caller that the request has been completed.
     */
    @Async( AppConfig.STOCK_QUOTE_THREAD_POOL )
    @Override
    public void asynchronousFetch( final LinkedAccountEntity linkedAccountEntity,
                                   final AsyncProcessor<LinkedAccountEntity> subject )
    {
        final String methodName = "asynchronousFetch";
        logMethodBegin( methodName, linkedAccountEntity );
        /*
         * The super class calls this.getThirdPartyData and takes care of the subject notification.
         */
        super.asynchronousFetch( linkedAccountEntity, subject );
        logMethodEnd( methodName );
    }

    /**
     * This method, when called, is run a new thread and makes a call to the super class to make perform the asynchronous
     * fetch logic which, in part, ends up calling the {@code getThirdPartyData} method below to perform the batch
     * stock company fetch.
     * @param asyncBatchCacheRequests
     */
    @Async( AppConfig.STOCK_QUOTE_THREAD_POOL )
    @Override
    public void asynchronousFetch( final Map<LinkedAccountEntity, LinkedAccountEntityCacheRequest> asyncBatchCacheRequests )
    {
        final String methodName = "asynchronousFetch";
        logMethodBegin( methodName, asyncBatchCacheRequests.keySet() );
        super.asynchronousFetch( asyncBatchCacheRequests );
        logMethodEnd( methodName );
    }

    @Override
    protected void copyThirdPartyData( final Quote thirdPartyData, final LinkedAccountEntity entity )
    {
        super.copyThirdPartyData( thirdPartyData, entity );
        entity.copyQuote( thirdPartyData );
    }

    /**
     * Creates a new entity.
     * @return
     */
    @Override
    protected LinkedAccountEntity createEntity()
    {
        return this.context.getBean( LinkedAccountEntity.class );
    }

    @Override
    protected String getCacheKeyFromThirdPartyData( final Quote quote )
    {
        return quote.getSymbol();
    }

    @Override
    protected String getCacheKey( final LinkedAccountEntity linkedAccountEntity )
    {
        return linkedAccountEntity.getId();
    }

    @Override
    protected LinkedAccountEntityCacheResponse newResponse()
    {
        return this.context.getBean( LinkedAccountEntityCacheResponse.class );
    }

    @Override
    protected LinkedAccountEntityService getEntityService()
    {
        return this.stockQuoteEntityService;
    }

    @Override
    protected LinkedAccountNotFoundException createException( final LinkedAccountEntity key, final Exception cause )
    {
        return null;
    }

    @Override
    protected LinkedAccountNotFoundException createException( final String key, final Exception cause )
    {
        return new StockQuoteNotFoundException( key, cause );
    }
}
