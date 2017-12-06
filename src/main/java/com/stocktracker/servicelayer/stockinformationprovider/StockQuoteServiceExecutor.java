package com.stocktracker.servicelayer.stockinformationprovider;

import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockQuoteUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * This class provides synchronous and asynchronous methods to obtain stock quotes.  A call to {@code asynchronousGetStockQuote}
 * will result in the request being run on a separate thread.
 */
@Service
public class StockQuoteServiceExecutor
{
    private IEXTradingStockService iexTradingStockService;
    private YahooStockService yahooStockService;
    private Logger logger = LoggerFactory.getLogger( StockQuoteServiceExecutor.class );

    @PostConstruct
    public void init()
    {
    }

    /**
     * Gets a stock quote for {@code tickerSymbol} asynchronously by creating a new thread.
     * @param tickerSymbol
     * @param handleStockQuoteResult This interface method will be called when the stock quote has been retreived.
     * @return
     */
    @Async( "stockQuoteThreadPool")
    public void asynchronousGetStockQuote( final String tickerSymbol,
                                           final HandleStockQuoteReturn handleStockQuoteResult )
        throws StockQuoteUnavailableException, StockNotFoundException
    {
        final String methodName = "asynchronousGetStockQuote";
        logger.debug( methodName + " " + tickerSymbol );
        StockTickerQuote stockTickerQuote;
        try
        {
            stockTickerQuote = this.getQuoteFromIEXTrading( tickerSymbol );
            if ( stockTickerQuote == null )
            {
                stockTickerQuote = this.getQuoteFromYahoo( tickerSymbol );
            }
        }
        catch( Exception e )
        {
            logger.error( methodName + " Failed to get quote from IEXTrading: " + e.getMessage() );
            stockTickerQuote = this.getQuoteFromYahoo( tickerSymbol );
        }
        handleStockQuoteResult.handleStockQuoteReturn( stockTickerQuote );
    }

    /**
     * Fetch the stock quote synchronously.
     * @param tickerSymbol
     * @return
     */
    public StockTickerQuote synchronousGetStockQuote( final String tickerSymbol )
        throws StockQuoteUnavailableException
    {
        final String methodName = "synchronousGetStockQuote";
        logger.debug( methodName + " " + tickerSymbol );
        StockTickerQuote stockTickerQuote;
        try
        {
            stockTickerQuote = this.getQuoteFromIEXTrading( tickerSymbol );
            if ( stockTickerQuote == null )
            {
                stockTickerQuote = this.getQuoteFromYahoo( tickerSymbol );
            }
        }
        catch( Exception e )
        {
            logger.error( methodName + " Failed to get quote from IEXTrading: " + e.getMessage() );
            stockTickerQuote = this.getQuoteFromYahoo( tickerSymbol );
            if ( stockTickerQuote == null )
            {
                logger.error( methodName + " Failed to get quote(null) from IEXTrading for " + tickerSymbol );
            }
        }
        return stockTickerQuote;
    }

    /**
     * Get a quote from Yahoo
     * @param tickerSymbol
     * @return
     */
    private StockTickerQuote getQuoteFromYahoo( final String tickerSymbol )
    {
        final String methodName = "getQuoteFromYahoo";
        logger.debug( methodName + " " + tickerSymbol );
        StockTickerQuote stockTickerQuote = null;
        try
        {
            stockTickerQuote = this.yahooStockService.getStockTickerQuote( tickerSymbol );
            if ( stockTickerQuote == null )
            {
                logger.error( methodName + " Failed to get quote(null) from Yahoo for " + tickerSymbol );
            }
            else
            {
                logger.debug( methodName + " Stock quote obtained from Yahoo for " + tickerSymbol );
            }
        }
        catch( Exception e )
        {
            logger.error( methodName + " Failed to get quote(exception) from Yahoo for " + tickerSymbol, e );
        }
        return stockTickerQuote;
    }

    /**
     * Get a quote from IEXTrading
     * @param tickerSymbol
     * @return
     */
    private StockTickerQuote getQuoteFromIEXTrading( final String tickerSymbol )
    {
        final String methodName = "getQuoteFromIEXTrading";
        logger.debug( methodName + " " + tickerSymbol );
        StockTickerQuote stockTickerQuote = this.iexTradingStockService.getStockTickerQuote( tickerSymbol );
        try
        {
            stockTickerQuote = this.iexTradingStockService.getStockTickerQuote( tickerSymbol );
            if ( stockTickerQuote == null )
            {
                logger.error( methodName + " Failed to get quote(null) from IEXTrading for " + tickerSymbol );
            }
            else
            {
                logger.debug( methodName + " Stock quote obtained from IEXTrading for " + tickerSymbol );
            }
        }
        catch( Exception e )
        {
            logger.error( methodName + " Failed to get quote(exception) from IEXTrading for " + tickerSymbol, e );
        }
        return stockTickerQuote;
    }


    @Autowired
    public void setIexTradingStockService( final IEXTradingStockService iexTradingStockService )
    {
        this.iexTradingStockService = iexTradingStockService;
    }

    @Autowired
    public void setYahooStockService( final YahooStockService yahooStockService )
    {
        this.yahooStockService = yahooStockService;
    }
}
