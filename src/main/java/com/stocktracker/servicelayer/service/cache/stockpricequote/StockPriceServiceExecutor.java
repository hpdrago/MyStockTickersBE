package com.stocktracker.servicelayer.service.cache.stockpricequote;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.servicelayer.service.IEXTradingStockService;
import com.stocktracker.servicelayer.service.StockCompanyEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

/**
 * This class provides synchronous and asynchronous methods to obtain stock quotes.  A call to {@code asynchronousGetStockPrice}
 * will result in the request being run on a separate thread.
 */
@Service
public class StockPriceServiceExecutor implements MyLogger
{
    @Autowired
    private IEXTradingStockService iexTradingStockService;
    @Autowired
    private YahooStockPriceService yahooStockService;
    @Autowired
    private StockCompanyEntityService stockCompanyEntityService;
    private TreeSet<String> discontinuedStocks = new TreeSet();

    /**
     * Get stock price quotes for a list of ticker symbols.
     * @param tickerSymbols
     * @return
     */
    public List<GetStockPriceResult> synchronousGetStockPrices( final List<String> tickerSymbols )
    {
        final String methodName = "synchronousGetStockPrices";
        logMethodBegin( methodName, tickerSymbols );
        final List<GetStockPriceResult> stockPriceResults = new ArrayList<>( tickerSymbols.size() );
        tickerSymbols.forEach( tickerSymbol ->
                               {
                                   GetStockPriceResult stockPriceResult = this.synchronousGetStockPrice( tickerSymbol );
                                   stockPriceResults.add( stockPriceResult );
                               });
        logMethodEnd( methodName, stockPriceResults.size() + " stock price quotes" );
        return stockPriceResults;
    }

    /**
     * Fetch the stock quote synchronously.
     * @param tickerSymbol
     * @return
     * @throws StockNotFoundException
     */
    public GetStockPriceResult synchronousGetStockPrice( final String tickerSymbol )
    {
        final String methodName = "synchronousGetStockPrice";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Assert.isTrue( !tickerSymbol.equalsIgnoreCase( "null" ), "ticker symbol cannot be 'null'");
        final GetStockPriceResult getStockPriceResult = this.getStockPrice( tickerSymbol );
        logMethodEnd( methodName, getStockPriceResult );
        return getStockPriceResult;
    }

    /**
     * Gets the stock price from IEXTrading or Yahoo if there is a failure with IEXTrading.
     * @param tickerSymbol
     * @return
     */
    private GetStockPriceResult getStockPrice( final String tickerSymbol )
    {
        final String methodName = "getStockPrice";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Assert.isTrue( !tickerSymbol.equalsIgnoreCase( "null" ), "ticker symbol cannot be 'null'");
        final GetStockPriceResult getStockPriceResult = new GetStockPriceResult();
        getStockPriceResult.setTickerSymbol( tickerSymbol );
        getStockPriceResult.setStockPriceResult( StockPriceFetchResult.NOT_FOUND );
        final String myTickerSymbol = checkTickerSymbol( tickerSymbol );
        BigDecimal stockPrice = null;
        if ( this.discontinuedStocks.contains( myTickerSymbol ))
        {
            logDebug( methodName, " myTickerSymbol {0} is no longer valid.", myTickerSymbol );
            getStockPriceResult.setStockPriceFetchResult( StockPriceFetchResult.DISCONTINUED );
        }
        else
        {
            boolean triedYahoo = false;
            try
            {
                stockPrice = this.getPriceFromIEXTrading( myTickerSymbol );
                if ( stockPrice == null )
                {
                    stockPrice = this.getPriceFromYahoo(  myTickerSymbol );
                    triedYahoo = true;
                }
                else
                {
                    getStockPriceResult.setStockPrice( stockPrice );
                    getStockPriceResult.setStockPriceResult( StockPriceFetchResult.SUCCESS );
                }
            }
            catch( Exception e )
            {
                logError( methodName, " Failed to get quote from IEXTrading" + e.getMessage() );
                if ( !triedYahoo )
                {
                    try
                    {
                        stockPrice = this.getPriceFromYahoo( myTickerSymbol );
                    }
                    catch( StockNotFoundException stockNotFoundException )
                    {
                        getStockPriceResult.setException( stockNotFoundException );
                    }
                }
                if ( stockPrice == null )
                {
                    logError( methodName, " Failed to get quote(null) from IEXTrading for " + myTickerSymbol );
                    getStockPriceResult.setStockPriceResult( StockPriceFetchResult.NOT_FOUND );
                }
                else
                {
                    getStockPriceResult.setStockPrice( stockPrice );
                    getStockPriceResult.setStockPriceResult( StockPriceFetchResult.SUCCESS );
                }
            }
        }
        logMethodEnd( methodName, getStockPriceResult );
        return getStockPriceResult;
    }


    /**
     * Get a quote from Yahoo
     * @param tickerSymbol
     * @return
     */
    private BigDecimal getPriceFromYahoo( final String tickerSymbol )
        throws StockNotFoundException
    {
        final String methodName = "getPriceFromYahoo";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Assert.isTrue( !tickerSymbol.equalsIgnoreCase( "null" ), "ticker symbol cannot be 'null'");

        BigDecimal stockPrice = null;
        if ( !this.discontinuedStocks.contains( tickerSymbol ))
        {
            try
            {
                stockPrice = this.yahooStockService.getStockPrice( tickerSymbol );
                if ( stockPrice == null )
                {
                    logError( methodName, " Failed to get quote(null) from Yahoo for " + tickerSymbol );
                }
                else
                {
                    logDebug( methodName, " Stock quote obtained from Yahoo for " + tickerSymbol );
                }
            }
            catch( StockNotFoundException e )
            {
                logWarn( methodName," Failed to get quote(StockNotFoundException) from Yahoo for " + tickerSymbol );
                this.handleStockNotFoundException( tickerSymbol );
                //logger.warn( methodName + " " );
                throw e;
            }
        }
        else
        {
            logInfo( methodName, " tickerSymbol " + tickerSymbol + " is no longer valid." );
        }
        return stockPrice;
    }

    /**
     * This method will mark a stock as invalid in the Stock table so that it will still be around for future
     * reference but also known that a current stock price cannot obtained for it.
     * @param tickerSymbol
     */
    private void handleStockNotFoundException( final String tickerSymbol )
    {
        final String methodName = "handleStockNotFoundException";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Assert.isTrue( !tickerSymbol.equalsIgnoreCase( "null" ), "ticker symbol cannot be 'null'");
        this.discontinuedStocks.add( tickerSymbol );
        this.stockCompanyEntityService
            .markStockAsDiscontinued( tickerSymbol );
        logMethodEnd( methodName, tickerSymbol );
    }

    /**
     * Get a quote from IEXTrading
     * @param tickerSymbol
     * @return
     * @throws StockNotFoundException for invalid ticker symbols.
     */
    private BigDecimal getPriceFromIEXTrading( final String tickerSymbol )
        throws StockNotFoundException
    {
        final String methodName = "getPriceFromIEXTrading";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Assert.isTrue( !tickerSymbol.equalsIgnoreCase( "null" ), "ticker symbol cannot be 'null'");
        BigDecimal stockPrice = null;
        if ( !this.discontinuedStocks.contains( tickerSymbol ))
        {
            stockPrice = this.iexTradingStockService.getPrice( tickerSymbol );
            if ( stockPrice == null )
            {
                logError( methodName," Failed to get quote(null) from IEXTrading for " + tickerSymbol );
            }
            else
            {
                logError( methodName, " Stock quote obtained from IEXTrading for " + tickerSymbol );
            }
        }
        else
        {
            logInfo( methodName, " tickerSymbol " + tickerSymbol + " is no longer valid." );
        }
        return stockPrice;
    }
    /*
     * Remove option target dates which follow spaces.
     */
    public static String checkTickerSymbol( final String tickerSymbol )
    {
        if ( tickerSymbol.contains( " " ))
        {
            return tickerSymbol.substring( 0, tickerSymbol.indexOf( ' ' ));
        }
        return tickerSymbol;
    }
}
