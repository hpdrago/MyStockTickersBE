package com.stocktracker.servicelayer.stockinformationprovider;

import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockQuoteUnavailableException;
import com.stocktracker.servicelayer.service.StockCompanyEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

/**
 * This class provides synchronous and asynchronous methods to obtain stock quotes.  A call to {@code asynchronousGetStockPrice}
 * will result in the request being run on a separate thread.
 */
@Service
public class StockPriceServiceExecutor
{
    private IEXTradingStockService iexTradingStockService;
    private YahooStockService yahooStockService;
    private Logger logger = LoggerFactory.getLogger( StockPriceServiceExecutor.class );
    private TreeSet<String> discontinuedStocks = new TreeSet();
    private StockCompanyEntityService stockCompanyEntityService;


    /**
     * Gets a stock quote for {@code tickerSymbol} asynchronously by creating a new thread.
     * @param tickerSymbol
     * @param handleStockQuoteResult
     * @throws StockQuoteUnavailableException
     * @throws StockNotFoundException
     */
    @Async( "stockQuoteThreadPool")
    public void asynchronousGetStockPrice( final String tickerSymbol,
                                           final HandleStockQuoteResult handleStockQuoteResult )
    {
        final String methodName = "asynchronousGetStockPrice";
        logger.debug( methodName + " " + tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Objects.requireNonNull( handleStockQuoteResult, "handleStockQuoteResult cannot be null" );
        final String myTickerSymbol = checkTickerSymbol( tickerSymbol );
        BigDecimal stockPrice = null;
        try
        {
            GetStockPriceResult getStockPriceResult = getStockPrice( myTickerSymbol );
            handleStockQuoteResult.handleGetStockPriceResult( getStockPriceResult );
        }
        catch( Exception e )
        {
            final GetStockPriceResult getStockPriceResult = new GetStockPriceResult();
            getStockPriceResult.setTickerSymbol( tickerSymbol );
            getStockPriceResult.setException( e );
            getStockPriceResult.setStockPriceResult( StockPriceFetchResult.EXCEPTION );
            handleStockQuoteResult.handleGetStockPriceResult( getStockPriceResult );
        }
    }

    /**
     * Get stock prices for a list of ticker symbols.
     * @param tickerSymbols
     * @param handleStockQuoteResult
     */
    @Async( "stockQuoteThreadPool")
    public void asynchronousGetStockPrices( final List<String> tickerSymbols,
                                            final HandleStockQuoteResult handleStockQuoteResult )
    {
        final String methodName = "asynchronousGetStockPrices";
        logger.debug( methodName + ".begin " + tickerSymbols.size() + " ticker symbols" );
        /*
        final List<GetStockPriceResult> stockPriceResults = this.iexTradingStockService
                                                                .getPrices( tickerSymbols );
                                                                */
        logger.debug( methodName + ".end" );
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
        logger.debug( methodName + ".begin " + tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        final GetStockPriceResult getStockPriceResult = this.getStockPrice( tickerSymbol );
        logger.debug( methodName + ".end " + tickerSymbol + " " + getStockPriceResult );
        return getStockPriceResult;
    }

    /**
     * Gets the stock price from IEXTrading or Yahoo if there is a failure with IEXTrading.
     * @param tickerSymbol
     * @return
     */
    private GetStockPriceResult getStockPrice( final String tickerSymbol )
    {
        final String methodName = "getStockPriceQuote";
        logger.debug( methodName + " " + tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        final GetStockPriceResult getStockPriceResult = new GetStockPriceResult();
        getStockPriceResult.setTickerSymbol( tickerSymbol );
        getStockPriceResult.setStockPriceResult( StockPriceFetchResult.NOT_FOUND );
        final String myTickerSymbol = checkTickerSymbol( tickerSymbol );
        BigDecimal stockPrice = null;
        if ( this.discontinuedStocks.contains( myTickerSymbol ))
        {
            logger.debug( methodName + " myTickerSymbol " + myTickerSymbol + " is no longer valid." );
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
                logger.error( methodName + " Failed to get quote from IEXTrading: " + e.getMessage(), e );
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
                    logger.error( methodName + " Failed to get quote(null) from IEXTrading for " + myTickerSymbol );
                    getStockPriceResult.setStockPriceResult( StockPriceFetchResult.NOT_FOUND );
                }
                else
                {
                    getStockPriceResult.setStockPrice( stockPrice );
                    getStockPriceResult.setStockPriceResult( StockPriceFetchResult.SUCCESS );
                }
            }
        }
        logger.debug( methodName + " " + getStockPriceResult );
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
        logger.debug( methodName + " " + tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );

        BigDecimal stockPrice = null;
        if ( !this.discontinuedStocks.contains( tickerSymbol ))
        {
            try
            {
                stockPrice = this.yahooStockService.getStockPrice( tickerSymbol );
                if ( stockPrice == null )
                {
                    logger.error( methodName + " Failed to get quote(null) from Yahoo for " + tickerSymbol );
                }
                else
                {
                    logger.debug( methodName + " Stock quote obtained from Yahoo for " + tickerSymbol );
                }
            }
            catch( StockNotFoundException e )
            {
                logger.warn( methodName + " Failed to get quote(StockNotFoundException) from Yahoo for " + tickerSymbol );
                this.handleStockNotFoundException( tickerSymbol );
                //logger.warn( methodName + " " );
                throw e;
            }
        }
        else
        {
            logger.info( methodName + " tickerSymbol " + tickerSymbol + " is no longer valid." );
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
        logger.info( methodName + " " + tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        this.discontinuedStocks.add( tickerSymbol );
        this.stockCompanyEntityService
            .markStockAsDiscontinued( tickerSymbol );
    }

    /**
     * Get a quote from IEXTrading
     * @param tickerSymbol
     * @return
     * @throws StockNotFoundException for invalid ticker symbols.
     */
    private BigDecimal getPriceFromIEXTrading( final String tickerSymbol )
    {
        final String methodName = "getPriceFromIEXTrading";
        logger.debug( methodName + " " + tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        BigDecimal stockPrice = null;
        if ( !this.discontinuedStocks.contains( tickerSymbol ))
        {
            stockPrice = this.iexTradingStockService.getPrice( tickerSymbol );
            if ( stockPrice == null )
            {
                logger.error( methodName + " Failed to get quote(null) from IEXTrading for " + tickerSymbol );
            }
            else
            {
                logger.debug( methodName + " Stock quote obtained from IEXTrading for " + tickerSymbol );
            }
        }
        else
        {
            logger.info( methodName + " tickerSymbol " + tickerSymbol + " is no longer valid." );
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
    public void setStockCompanyEntityService( final StockCompanyEntityService stockCompanyEntityService )
    {
        this.stockCompanyEntityService = stockCompanyEntityService;
    }
}
