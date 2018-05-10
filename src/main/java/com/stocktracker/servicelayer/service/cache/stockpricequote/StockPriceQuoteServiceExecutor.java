package com.stocktracker.servicelayer.service.cache.stockpricequote;

import com.stocktracker.AppConfig;
import com.stocktracker.servicelayer.service.StockCompanyEntityService;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheBaseCacheServiceExecutor;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheServiceExecutor;
import io.reactivex.processors.BehaviorProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * This class makes the calls to the IEXTrading API to get the Stock Price: https://iextrading.com/developer/docs/#price
 * and to Yahoo as well.
 */
@Service
// Proxy target class to get past implementation of the interface and getting a runtime proxy error.
@EnableAsync(proxyTargetClass = true)
public class StockPriceQuoteServiceExecutor extends AsyncCacheBaseCacheServiceExecutor<String,StockPriceQuote>
    implements AsyncCacheServiceExecutor<String,StockPriceQuote>
{
    private StockPriceServiceExecutor stockPriceServiceExecutor;
    private StockPriceQuoteCache stockPriceQuoteCache;
    private StockCompanyEntityService stockCompanyEntityService;

    /**
     * Fetches the StockQuote synchronously.
     * @param tickerSymbol The ticker symbol to search for.
     * @return
     */
    @Override
    public Optional<StockPriceQuote> synchronousFetch( final String tickerSymbol )
    {
        final String methodName = "synchronousFetch";
        logMethodBegin( methodName, tickerSymbol );
        final GetStockPriceResult getStockPriceResult = stockPriceServiceExecutor.synchronousGetStockPrice( tickerSymbol );
        final StockPriceQuote stockPriceQuote = this.context.getBean( StockPriceQuote.class );
        final StockPriceQuoteCacheEntry stockPriceQuoteCacheEntry = this.stockPriceQuoteCache
                                                                        .getCacheEntry( tickerSymbol );
        stockPriceQuote.setTickerSymbol( tickerSymbol );
        switch ( getStockPriceResult.getStockPriceFetchResult() )
        {
            case DISCONTINUED:
                //if ( stockPriceQuoteCacheEntry.isStockTableEntryValidated() )
                this.stockCompanyEntityService
                    .markStockAsDiscontinued( tickerSymbol );
                stockPriceQuoteCacheEntry.setDiscontinued( true );
                stockPriceQuote.setLastPrice( new BigDecimal( 0 ));
                break;

            case NOT_FOUND:
                stockPriceQuote.setLastPrice( new BigDecimal( 0 ));
                break;

            case SUCCESS:
                stockPriceQuote.setLastPrice( getStockPriceResult.getStockPrice() );
                break;

            case EXCEPTION:
                stockPriceQuote.setStockPriceQuoteCacheState( AsyncCacheEntryState.FAILURE );
                stockPriceQuote.setError( stockPriceQuoteCacheEntry.getFetchThrowable().getMessage() );
                break;
        }
        return Optional.of( stockPriceQuote );
    }

    /**
     * This method, when called, starts on a new thread launched and managed by the Spring container.
     * In the new thread, the stock quote will be retrieved and the caller will be notified through the {@code observable}
     * @param tickerSymbol
     * @param subject Behaviour subject to use to notify the caller that the request has been completed.
     */
    @Async( AppConfig.STOCK_QUOTE_THREAD_POOL )
    @Override
    public void asynchronousFetch( final String tickerSymbol, final BehaviorProcessor<Optional<StockPriceQuote>> subject )
    {
        final String methodName = "asynchronousFetch";
        logMethodBegin( methodName, tickerSymbol );
        super.asynchronousFetch( tickerSymbol, subject );
        logMethodEnd( methodName );
    }

    @Autowired
    public void setStockPriceServiceExecutor( final StockPriceServiceExecutor stockPriceServiceExecutor )
    {
        this.stockPriceServiceExecutor = stockPriceServiceExecutor;
    }

    @Autowired
    public void setStockPriceQuoteCache( final StockPriceQuoteCache stockPriceQuoteCache )
    {
        this.stockPriceQuoteCache = stockPriceQuoteCache;
    }

    @Autowired
    public void setStockCompanyEntityService( final StockCompanyEntityService stockCompanyEntityService )
    {
        this.stockCompanyEntityService = stockCompanyEntityService;
    }
}
