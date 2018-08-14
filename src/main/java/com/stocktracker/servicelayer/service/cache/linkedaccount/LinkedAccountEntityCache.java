package com.stocktracker.servicelayer.service.cache.linkedaccount;

import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.entity.LinkedAccountEntity;
import com.stocktracker.repositorylayer.entity.TradeItAccountEntity;
import com.stocktracker.servicelayer.service.LinkedAccountEntityService;
import com.stocktracker.servicelayer.service.TradeItAccountEntityService;
import com.stocktracker.servicelayer.service.TradeItAsyncUpdateService;
import com.stocktracker.servicelayer.service.cache.common.AsyncCache;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDataRequestException;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheStrategy;
import com.stocktracker.servicelayer.tradeit.apiresults.GetAccountOverviewAPIResult;
import com.stocktracker.servicelayer.tradeit.types.LinkedAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.stocktracker.servicelayer.service.cache.common.AsyncCacheStrategy.REMOVE;

/**
 * This is the cache for IEXTrading Stock Quotes.  See https://iextrading.com/developer/docs/#quote for information
 * contained in the quote.  It is information that only needs to be obtained a couple times a day, well mostly, that's
 * they way we plan to use it initially.
 */
@Service
public class LinkedAccountEntityCache extends AsyncCache<UUID,
                                                         LinkedAccountEntity,
                                                         GetAccountOverviewAsyncCacheKey,
                                                         GetAccountOverviewAPIResult,
                                                         LinkedAccountEntityCacheEntry,
                                                         LinkedAccountEntityServiceExecutor>
{
    @Autowired
    private LinkedAccountEntityServiceExecutor stockQuoteEntityServiceExecutor;

    @Autowired
    private TradeItAsyncUpdateService tradeItAsyncUpdateService;

    @Autowired
    private LinkedAccountEntityService linkedAccountEntityService;

    @Autowired
    private TradeItAccountEntityService tradeItAccountEntityService;

    /**
     * Converts the TradeIt account summary information to a linked account entity.
     * @param cacheKey
     * @param asyncKey
     * @param asyncData
     * @return
     * @throws AsyncCacheDataRequestException
     */
    @Override
    protected LinkedAccountEntity convertAsyncData( final UUID cacheKey,
                                                    final GetAccountOverviewAsyncCacheKey asyncKey,
                                                    final GetAccountOverviewAPIResult asyncData )
        throws AsyncCacheDataRequestException
    {
        final String methodName = "convertAsyncData";
        logMethodBegin( methodName, cacheKey, asyncKey, asyncData );
        LinkedAccountEntity linkedAccountEntity;
        try
        {
            linkedAccountEntity = this.linkedAccountEntityService
                                      .getEntity( asyncKey.getLinkedAccountUuid() );
            final TradeItAccountEntity tradeItAccountEntity = this.tradeItAccountEntityService
                                                                  .getEntity( asyncKey.getTradeItAccountUuid() );
            this.tradeItAsyncUpdateService
                .updateLinkedAccount( tradeItAccountEntity, linkedAccountEntity );
        }
        catch( VersionedEntityNotFoundException e )
        {
            throw new AsyncCacheDataRequestException( asyncKey, e );
        }
        logMethodEnd( methodName, linkedAccountEntity );
        return linkedAccountEntity;
    }

    /**
     * Creates the cache entry.
     * @return
     */
    @Override
    protected LinkedAccountEntityCacheEntry createCacheEntry()
    {
        return new LinkedAccountEntityCacheEntry();
    }

    /**
     * Creates the executor.
     * @return
     */
    @Override
    protected LinkedAccountEntityServiceExecutor getExecutor()
    {
        return this.stockQuoteEntityServiceExecutor;
    }

    /**
     * Identifies the strategy when retrieving cache entry from the cache.  Since the stock quote information is cached in the stock_quote table,
     * we don't need to cache it in the cache and thus we can remove the cache entry after we have the asynchronous work completed.
     * @return
     */
    @Override
    protected AsyncCacheStrategy getCacheStrategy()
    {
        return REMOVE;
    }
}
