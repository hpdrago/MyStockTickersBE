package com.stocktracker.servicelayer.stockinformationprovider;

import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockQuoteUnavailableException;
import com.stocktracker.servicelayer.service.StockEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.util.TreeSet;

/**
 * This class provides synchronous and asynchronous methods to obtain stock quotes.  A call to {@code asynchronousGetStockQuote}
 * will result in the request being run on a separate thread.
 */
@Service
public class StockQuoteServiceExecutor
{
    private IEXTradingStockService iexTradingStockService;
    private YahooStockService yahooStockService;
    private StockEntityService stockService;
    private Logger logger = LoggerFactory.getLogger( StockQuoteServiceExecutor.class );
    private TreeSet<String> discontinuedStocks = new TreeSet();

    @PostConstruct
    public void init()
    {
    }

    /**
     * Gets a stock quote for {@code tickerSymbol} asynchronously by creating a new thread.
     * @param tickerSymbol
     * @param handleStockQuoteResult
     * @throws StockQuoteUnavailableException
     * @throws StockNotFoundException
     */
    @Async( "stockQuoteThreadPool")
    public void asynchronousGetStockQuote( final String tickerSymbol,
                                           final HandleStockQuoteReturn handleStockQuoteResult )
        throws StockQuoteUnavailableException,
               StockNotFoundException
    {
        final String methodName = "asynchronousGetStockQuote";
        logger.debug( methodName + " " + tickerSymbol );
        StockTickerQuote stockTickerQuote = null;
        try
        {
            stockTickerQuote = this.getQuoteFromIEXTrading( tickerSymbol );
            if ( stockTickerQuote == null )
            {
                stockTickerQuote = this.getQuoteFromYahoo( tickerSymbol );
            }
        }
        catch( StockNotFoundException e )
        {
            this.handleStockNotFoundException( tickerSymbol );
        }
        catch( Exception e )
        {
            logger.error( methodName + " Failed to get quote from IEXTrading: " + e.getMessage(), e );
            stockTickerQuote = this.getQuoteFromYahoo( tickerSymbol );
        }
        handleStockQuoteResult.handleStockQuoteReturn( stockTickerQuote );
    }

    /**
     * Fetch the stock quote synchronously.
     * @param tickerSymbol
     * @return
     * @throws StockNotFoundException
     */
    public StockTickerQuote synchronousGetStockQuote( final String tickerSymbol )
    {
        final String methodName = "synchronousGetStockQuote";
        logger.debug( methodName + " " + tickerSymbol );
        StockTickerQuote stockTickerQuote = null;
        if ( !this.discontinuedStocks.contains( tickerSymbol ))
        {
            boolean triedYahoo = false;
            try
            {
                stockTickerQuote = this.getQuoteFromIEXTrading( tickerSymbol );
                if ( stockTickerQuote == null )
                {
                    stockTickerQuote = this.getQuoteFromYahoo( tickerSymbol );
                    triedYahoo = true;
                }
            }
            catch( StockNotFoundException e )
            {
                this.discontinuedStocks.add( tickerSymbol );
            }
            catch( Exception e )
            {
                logger.error( methodName + " Failed to get quote from IEXTrading: " + e.getMessage(), e );
                if ( !triedYahoo )
                {
                    stockTickerQuote = this.getQuoteFromYahoo( tickerSymbol );
                }
                if ( stockTickerQuote == null )
                {
                    logger.error( methodName + " Failed to get quote(null) from IEXTrading for " + tickerSymbol );
                }
            }
        }
        else
        {
            logger.info( methodName + " tickerSymbol " + tickerSymbol + " is no longer valid." );
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
        if ( !this.discontinuedStocks.contains( tickerSymbol ))
        {
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
                if ( e instanceof FileNotFoundException )
                {
                    this.handleStockNotFoundException( tickerSymbol );
                }
                else
                {
                    logger.error( methodName + " Failed to get quote(exception) from Yahoo for " + tickerSymbol, e );
                }
            }
        }
        else
        {
            logger.info( methodName + " tickerSymbol " + tickerSymbol + " is no longer valid." );
        }
        return stockTickerQuote;
    }

    /**
     * This method will mark a stock as invalid in the Stock table so that it will still be around for future
     * reference but also known that a current stock price cannot obtained for it.
     * @param tickerSymbol
     */
    private void handleStockNotFoundException( final String tickerSymbol )
    {
        final String methodName = "handleStockNotFoundException";
        logger.info( methodName + " " + tickerSymbol );
        this.discontinuedStocks.add( tickerSymbol );
        this.stockService.markStockAsDiscontinued( tickerSymbol );
    }

    /**
     * Get a quote from IEXTrading
     * @param tickerSymbol
     * @return
     * @throws StockNotFoundException for invalid ticker symbols.
     */
    private StockTickerQuote getQuoteFromIEXTrading( final String tickerSymbol )
        throws StockNotFoundException, EntityVersionMismatchException
    {
        final String methodName = "getQuoteFromIEXTrading";
        logger.debug( methodName + " " + tickerSymbol );
        StockTickerQuote stockTickerQuote = null;
        if ( !this.discontinuedStocks.contains( tickerSymbol ))
        {
            stockTickerQuote = this.iexTradingStockService.getStockTickerQuote( tickerSymbol );
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
            catch( StockNotFoundException e )
            {
                this.stockService.markStockAsDiscontinued( tickerSymbol );
            }
            catch( Exception e )
            {
                logger.error( methodName + " Failed to get quote(exception) from IEXTrading for " + tickerSymbol, e );
            }
        }
        else
        {
            logger.info( methodName + " tickerSymbol " + tickerSymbol + " is no longer valid." );
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

    @Autowired
    public void setStockService( final StockEntityService stockService )
    {
        this.stockService = stockService;
    }

}
