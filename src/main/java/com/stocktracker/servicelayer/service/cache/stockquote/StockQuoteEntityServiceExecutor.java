package com.stocktracker.servicelayer.service.cache.stockquote;

import com.stocktracker.AppConfig;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import com.stocktracker.servicelayer.service.StockQuoteEntityService;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheBaseCacheServiceExecutor;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheServiceExecutor;
import com.stocktracker.servicelayer.service.cache.stockpricequote.StockPriceQuoteCache;
import io.reactivex.processors.BehaviorProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This class makes the calls to the IEXTrading API to get the Stock Quote: https://iextrading.com/developer/docs/#quote
 */
@Service
// Proxy target class to get past implementation of the interface and getting a runtime proxy error.
@EnableAsync(proxyTargetClass = true)
public class StockQuoteEntityServiceExecutor extends AsyncCacheBaseCacheServiceExecutor<String,StockQuoteEntity>
    implements AsyncCacheServiceExecutor<String,StockQuoteEntity>
{
    /**
     * Service for the stock quote entities.
     */
    private StockQuoteEntityService stockQuoteEntityService;

    /**
     * Fetches the StockQuote synchronously.
     * @param tickerSymbol The ticker symbol to search for.
     * @return
     */
    @Override
    public Optional<StockQuoteEntity> synchronousFetch( final String tickerSymbol )
    {
        final String methodName = "synchronousFetch";
        logMethodBegin( methodName, tickerSymbol );
        StockQuoteEntity stockQuoteEntity;
        try
        {
            stockQuoteEntity = this.stockQuoteEntityService
                                   .getEntity( tickerSymbol );
            /*
             * Check to see if the quote has expired and if so, fetch a new quote.
             */
            if ( stockQuoteEntity.getUpdateDate().getTime() < (System.currentTimeMillis() - StockPriceQuoteCache.EXPIRATION_TIME ))
            {
                logDebug( methodName, "Quote for {0} has expired.  Fetching fresh quote", tickerSymbol );
                stockQuoteEntity = this.stockQuoteEntityService.
                                       getQuoteFromIEXTrading( tickerSymbol );
            }
        }
        catch( VersionedEntityNotFoundException e )
        {
            stockQuoteEntity = this.stockQuoteEntityService
                                   .getQuoteFromIEXTrading( tickerSymbol );
        }
        return Optional.ofNullable( stockQuoteEntity );
    }

    /**
     * This method, when call is called on a new thread launched and managed by the Spring container.
     * In the new thread, the stock quote will be retrieved and the caller will be notified through the {@code observable}
     * @param tickerSymbol
     * @param subject Behaviour subject to use to notify the caller that the request has been completed.
     */
    @Async( AppConfig.STOCK_QUOTE_THREAD_POOL )
    @Override
    public void asynchronousFetch( final String tickerSymbol, final BehaviorProcessor<Optional<StockQuoteEntity>> subject )
    {
        final String methodName = "asynchronousFetch";
        logMethodBegin( methodName, tickerSymbol );
        /*
         * The super class calls this.synchronousFetch and takes care of the subject notification.
         */
        super.asynchronousFetch( tickerSymbol, subject );
        logMethodEnd( methodName );
    }

    @Autowired
    public void setStockQuoteEntityService( final StockQuoteEntityService stockQuoteEntityService )
    {
        this.stockQuoteEntityService = stockQuoteEntityService;
    }
}
