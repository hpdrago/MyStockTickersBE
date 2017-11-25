package com.stocktracker.servicelayer.service.stockinformationprovider;

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
        throws StockNotFoundException,
               StockQuoteUnavailableException
    {
        StockTickerQuote stockTickerQuote = this.iexTradingStockService.getStockTickerQuote( tickerSymbol );
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
        StockTickerQuote stockTickerQuote = null;
        try
        {
           stockTickerQuote = this.iexTradingStockService.getStockTickerQuote( tickerSymbol );
        }
        catch( Exception e )
        {
            logger.error( methodName + " Failed to get quote from IEXTrading: " + e.getMessage() );
            try
            {
                stockTickerQuote = this.yahooStockService.getStockTickerQuote( tickerSymbol );
                logger.debug( methodName + " Stock quote obtained from Yahoo." );
            }
            catch( Exception e2 )
            {
                logger.error( methodName + " Failed to get quote from Yahoo: " + e.getMessage() );
                throw new StockQuoteUnavailableException( e2 );
            }
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
