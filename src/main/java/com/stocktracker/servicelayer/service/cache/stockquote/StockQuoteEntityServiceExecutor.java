package com.stocktracker.servicelayer.service.cache.stockquote;

import com.stocktracker.AppConfig;
import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import com.stocktracker.servicelayer.service.StockQuoteEntityService;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDataNotFoundException;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDBEntityServiceExecutor;
import com.stocktracker.servicelayer.service.cache.stockpricequote.IEXTradingStockService;
import io.reactivex.processors.BehaviorProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import pl.zankowski.iextrading4j.api.stocks.Quote;

import java.sql.Timestamp;

/**
 * This class makes the calls to the IEXTrading API to get the Stock Quote: https://iextrading.com/developer/docs/#quote.
 * Quotes are stored to the stock_quote table.
 */
@Service
// Proxy target class to get past implementation of the interface and getting a runtime proxy error.
@EnableAsync(proxyTargetClass = true)
public class StockQuoteEntityServiceExecutor extends AsyncCacheDBEntityServiceExecutor<String,
                                                                                             StockQuoteEntity,
                                                                                             StockQuoteEntityService,
                                                                                             Quote>
{
    /**
     * Service for the stock quote entities.
     */
    private StockQuoteEntityService stockQuoteEntityService;
    /**
     * IEXTrading service.
     */
    private IEXTradingStockService iexTradingStockService;

    /**
     * Get the IEXTrading quote.
     * @param tickerSymbol
     * @return
     * @throws AsyncCacheDataNotFoundException
     */
    @Override
    protected Quote getExternalData( final String tickerSymbol )
        throws AsyncCacheDataNotFoundException
    {
        final String methodName = "getExternalData";
        logMethodBegin( methodName, tickerSymbol );
        final Quote quote = this.iexTradingStockService
                                .getQuote( tickerSymbol );
        if ( quote == null )
        {
            throw new AsyncCacheDataNotFoundException( tickerSymbol );
        }
        logMethodEnd( methodName, quote );
        return quote;
    }

    /**
     * Copies the properties from {@code quote} to {@code stockQuoteEntity}
     * @param quote
     * @param stockQuoteEntity
     */
    @Override
    protected void copyExternalDataToEntity( final Quote quote, final StockQuoteEntity stockQuoteEntity )
    {
        stockQuoteEntity.copyQuote( quote );
    }

    /**
     * This method, when call is called on a new thread launched and managed by the Spring container.
     * In the new thread, the stock quote will be retrieved and the caller will be notified through the {@code observable}
     * @param tickerSymbol
     * @param subject Behaviour subject to use to notify the caller that the request has been completed.
     */
    @Async( AppConfig.STOCK_QUOTE_THREAD_POOL )
    @Override
    public void asynchronousFetch( final String tickerSymbol, final BehaviorProcessor<StockQuoteEntity> subject )
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

    @Autowired
    public void setIexTradingStockService( final IEXTradingStockService iexTradingStockService )
    {
        this.iexTradingStockService = iexTradingStockService;
    }

    @Override
    protected StockQuoteEntityService getEntityService()
    {
        return this.stockQuoteEntityService;
    }
}
