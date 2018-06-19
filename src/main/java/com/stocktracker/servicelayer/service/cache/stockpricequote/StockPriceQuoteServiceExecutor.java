package com.stocktracker.servicelayer.service.cache.stockpricequote;

import com.stocktracker.AppConfig;
import com.stocktracker.servicelayer.service.IEXTradingStockService;
import com.stocktracker.servicelayer.service.StockCompanyEntityService;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheServiceExecutor;
import com.stocktracker.servicelayer.service.cache.common.BaseAsyncCacheBatchServiceExecutor;
import io.reactivex.processors.AsyncProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState.FAILURE;
import static com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState.NOT_FOUND;

/**
 * This class makes the calls to the IEXTrading API to get the Stock Price: https://iextrading.com/developer/docs/#price
 * and to Yahoo as well.
 */
@Service
// Proxy target class to get past implementation of the interface and getting a runtime proxy error.
@EnableAsync(proxyTargetClass = true)
public class StockPriceQuoteServiceExecutor extends BaseAsyncCacheBatchServiceExecutor<String,
                                                                                       StockPriceQuote,
                                                                                       StockPriceQuoteCacheRequest,
                                                                                       StockPriceQuoteCacheResponse>
    implements AsyncCacheServiceExecutor<String,StockPriceQuote>
{
    @Autowired
    private StockPriceServiceExecutor stockPriceServiceExecutor;

    @Autowired
    private StockPriceQuoteCache stockPriceQuoteCache;

    @Autowired
    private StockCompanyEntityService stockCompanyEntityService;

    @Autowired
    private IEXTradingStockService iexTradingStockService;

    /**
     * Get stock price quotes for a list of ticker symbols.
     * @param tickerSymbols
     * @return
     */
    @Override
    protected List<StockPriceQuote> getExternalData( final List<String> tickerSymbols )
    {
        final String methodName = "getExternalData";
        logMethodBegin( methodName, tickerSymbols );
        final Map<String,BigDecimal> stockPriceResults = this.iexTradingStockService.getStockPrices( tickerSymbols );
        final List<StockPriceQuote> stockPriceQuotes = new ArrayList<>( tickerSymbols.size() );
        for (  stockPriceResult : stockPriceResults )
        {
            StockPriceQuote stockPriceQuote;
            try
            {
                stockPriceQuote = processStockPriceQuoteResult( stockPriceResult );
            }
            catch( StockPriceNotFoundException e )
            {
                stockPriceQuote = this.context.getBean( StockPriceQuote.class );
                stockPriceQuote.setTickerSymbol( stockPriceResult.getTickerSymbol() );
                stockPriceQuote.setCacheError( e.getMessage() );
                stockPriceQuote.setCacheState( NOT_FOUND );
            }
            catch( Throwable e )
            {
                stockPriceQuote = this.context.getBean( StockPriceQuote.class );
                stockPriceQuote.setTickerSymbol( stockPriceResult.getTickerSymbol() );
                stockPriceQuote.setCacheError( e.getMessage() );
                stockPriceQuote.setCacheState( FAILURE );
            }
            stockPriceQuotes.add( stockPriceQuote );
        }
        logMethodEnd( stockPriceQuotes.size() + " stock quotes" );
        return stockPriceQuotes;
    }

    /**
     * Fetches the StockQuote synchronously.
     * @param tickerSymbol The ticker symbol to search for.
     * @return
     */
    @Override
    public StockPriceQuote getExternalData( final String tickerSymbol )
        throws StockPriceNotFoundException
    {
        final String methodName = "getExternalData";
        logMethodBegin( methodName, tickerSymbol );
        final GetStockPriceResult stockPriceResult = stockPriceServiceExecutor.synchronousGetStockPrice( tickerSymbol );
        final StockPriceQuote stockPriceQuote = processStockPriceQuoteResult( stockPriceResult );
        logMethodEnd( methodName, stockPriceQuote );
        return stockPriceQuote;
    }

    /**
     * Process the stock price quote result returned from the stock price service executor.
     * @param stockPriceResult
     * @return
     * @throws StockPriceNotFoundException
     */
    private StockPriceQuote processStockPriceQuoteResult( final GetStockPriceResult stockPriceResult )
        throws StockPriceNotFoundException
    {
        final StockPriceQuote stockPriceQuote = this.context.getBean( StockPriceQuote.class );
        final StockPriceQuoteCacheEntry stockPriceQuoteCacheEntry = this.stockPriceQuoteCache
                                                                        .getCacheEntry( stockPriceResult.getTickerSymbol() );
        stockPriceQuote.setTickerSymbol( stockPriceResult.getTickerSymbol() );
        switch ( stockPriceResult.getStockPriceFetchResult() )
        {
            case DISCONTINUED:
                this.stockCompanyEntityService
                    .markStockAsDiscontinued( stockPriceResult.getTickerSymbol() );
                stockPriceQuoteCacheEntry.setStockTableEntryValidated( true );
                throw new StockPriceNotFoundException( stockPriceResult.getTickerSymbol() );

            case NOT_FOUND:
                throw new StockPriceNotFoundException( stockPriceResult.getTickerSymbol() );

            case SUCCESS:
                stockPriceQuote.setCacheState( AsyncCacheEntryState.CURRENT );
                stockPriceQuote.setLastPrice( stockPriceResult.getStockPrice() );
                stockPriceQuote.setCacheError( null );
                break;

            case EXCEPTION:
                stockPriceQuoteCacheEntry.setStockTableEntryValidated( true );
                final StockPriceNotFoundException exception = new StockPriceNotFoundException( stockPriceResult.getTickerSymbol(),
                                                                                               stockPriceResult.getException() );
                throw exception;

        }
        return stockPriceQuote;
    }

    /**
     * This method, when called, starts on a new thread launched and managed by the Spring container.
     * In the new thread, the stock quote will be retrieved and the caller will be notified through the {@code observable}
     * @param tickerSymbol
     * @param subject Behaviour subject to use to notify the caller that the request has been completed.
     */
    @Async( AppConfig.STOCK_QUOTE_THREAD_POOL )
    @Override
    public void asynchronousFetch( final String tickerSymbol, final AsyncProcessor<StockPriceQuote> subject )
    {
        final String methodName = "asynchronousFetch";
        logMethodBegin( methodName, tickerSymbol );
        super.asynchronousFetch( tickerSymbol, subject );
        logMethodEnd( methodName );
    }

    @Override
    protected String getCacheKey( final StockPriceQuote externalData )
    {
        return externalData.getCacheKey();
    }

    @Override
    protected StockPriceQuoteCacheResponse newResponse()
    {
        return this.context.getBean( StockPriceQuoteCacheResponse.class );
    }
}
