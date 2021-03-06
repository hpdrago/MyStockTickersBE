package com.stocktracker.servicelayer.service.cache.stockpricequote;

import com.stocktracker.common.MyLogger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * https://www.mkyong.com/spring/spring-and-java-thread-example/
 *
 * created 11-2-2017 by Mike
 */
@Component
@Scope("prototype")
public class GetStockQuoteTask implements Runnable, MyLogger
{
    private StockPriceQuoteServiceProvider serviceProvider;
    //private StockPriceCache stockPriceCache;
    private String tickerSymbol;

    @Override
    public void run()
    {
        logMethodBegin( "run", this.tickerSymbol );
        /*
         * We don't care about the return value, we just want to load the cache with the stock quote.
         */
        /*
        this.stockQuoteCache.getStockFromProvider( this.serviceProvider,
                                                   this.tickerSymbol );*/
        logMethodEnd( "run", this.tickerSymbol );
    }

    public void setTickerSymbol( final String tickerSymbol )
    {
        this.tickerSymbol = tickerSymbol;
    }

    /*
    public void setStockPriceCache( final StockPriceCache stockPriceCache )
    {
        this.stockPriceCache = stockPriceCache;
    }
    */


    public void setServiceProvider( final StockPriceQuoteServiceProvider serviceProvider )
    {
        this.serviceProvider = serviceProvider;
    }
}
