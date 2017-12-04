package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockNotFoundInDatabaseException;
import com.stocktracker.common.exceptions.StockQuoteUnavailableException;
import com.stocktracker.repositorylayer.entity.StockEntity;
import com.stocktracker.servicelayer.stockinformationprovider.StockQuote;
import com.stocktracker.servicelayer.stockinformationprovider.StockQuoteCache;
import com.stocktracker.servicelayer.stockinformationprovider.StockQuoteFetchMode;
import com.stocktracker.servicelayer.stockinformationprovider.StockQuoteState;
import com.stocktracker.servicelayer.stockinformationprovider.StockTickerQuote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

/**
 * This service manages all of the methods to obtain stock quote information.
 */
@Service
public class StockQuoteService implements MyLogger
{
    private StockQuoteCache stockQuoteCache;
    private StockContainerService stockService;

    public interface StockCompanyNameContainer
    {
        String getTickerSymbol();
        void setCompanyName( final String companyName );
        String getCompanyName();
    }

    /**
     * To be implemented by any class containing a ticker symbol and a stock price to
     * beu use with the {@code updateStockPrice} methods
     */
    public interface LastPriceContainer
    {
        String getTickerSymbol();
        void setLastPrice( final BigDecimal stockPrice );
        BigDecimal getLastPrice();
        void setLastPriceChange( final Timestamp lastPriceChange );
        Timestamp getLastPriceChange();
    }

    /**
     * Defines the necessary methods to contain a stock quote and work with the StockQuoteCache
     */
    public interface StockQuoteContainer extends StockCompanyNameContainer, LastPriceContainer
    {
        void setStockExchange( final String exchange );
        String getStockExchange();
        void setStockQuoteState( final StockQuoteState stockQuoteState );
        StockQuoteState getStockQuoteState();
    }

    /**
     * Gets a stock quote from the {@code StockQuoteCache}.
     * If the fetch mode is SYNCHRONOUS, this method will block and wait while the quote is retrieved.
     * If the fetch mode is ASYNCHRONOUS and the stock quote is not found or is stale, this block will return without
     * a complete quote.  The quote can be obtained by a subsequent request.  If the stock quote is found and is current,
     * the behaviour is that of the SYNCHRONOUS mode and this method will return with a complete stock quote.
     * @param tickerSymbol
     * @param stockQuoteFetchMode
     * @return
     * @throws StockNotFoundInDatabaseException
     */
    public StockQuote getStockQuote( final String tickerSymbol,
                                     final StockQuoteFetchMode stockQuoteFetchMode )
        throws StockNotFoundInDatabaseException,
               StockQuoteUnavailableException,
               StockNotFoundException
    {
        final String methodName = "getStockQuote";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        StockQuote stockQuote = this.stockQuoteCache.getStockQuote( tickerSymbol, stockQuoteFetchMode );
        logMethodEnd( methodName, stockQuote );
        return stockQuote;
    }

    /**
     * Obtains the stock information from the stock cache with the current stock information including the price,
     * last price change, and company name.
     * This information that was retrieved from the cache, is also saved back to the database stock table.
     * @param container The container to populate with the stock quote information.
     * @param stockQuoteFetchMode SYNCHRONOUS or ASYNCHRONOUS
     */
    public void setStockQuoteInformation( final StockQuoteContainer container,
                                          final StockQuoteFetchMode stockQuoteFetchMode )
        throws StockQuoteUnavailableException,
               StockNotFoundException
    {
        final String methodName = "setStockQuoteInformation";
        logMethodBegin( methodName, container.getTickerSymbol(), stockQuoteFetchMode );
        Objects.requireNonNull( container, "container cannot be null" );
        Objects.requireNonNull( container.getTickerSymbol(), "container.getTickerSymbol() returns null" );
        StockQuote stockQuote = this.stockQuoteCache.getStockQuote( container.getTickerSymbol(), stockQuoteFetchMode );
        container.setStockQuoteState( stockQuote.getStockQuoteState() );
        if ( stockQuote.getStockQuoteState().isCurrent() ||
             stockQuote.getStockQuoteState().isStale() )
        {
            /*
             * Update the stock table with the new information
             */

            /*
             * Need asynch call here to udpate DB.
             */
            container.setCompanyName( stockQuote.getCompanyName() );
            container.setLastPrice( stockQuote.getLastPrice() );
            container.setStockQuoteState( stockQuote.getStockQuoteState() );
            container.setLastPriceChange( stockQuote.getLastPriceChange() );
        }
        else
        {
            logWarn( methodName, "Could not get stock information from the stock cache for {0}",
                     container.getTickerSymbol() );
        }
        logMethodEnd( methodName, container );
    }

    /**
     * Saves the stock information in {@code container} to the stock table.
     * @param stockQuote
     * @throws StockNotFoundException
     * @throws StockQuoteUnavailableException
     */
    public void persistStockQuote( final StockQuote stockQuote )
        throws StockQuoteUnavailableException,
               StockNotFoundException
    {
        final String methodName = "persistStockQuote";
        Objects.requireNonNull( stockQuote, "container cannot be null" );
        Objects.requireNonNull( stockQuote.getTickerSymbol(), "container.getTickerSymbol() returns null" );
        StockEntity stockEntity = this.stockService.getStockEntity( stockQuote.getTickerSymbol() );
        if ( stockEntity == null )
        {
            stockEntity.setCompanyName( stockQuote.getCompanyName() );
        }
        stockEntity.setLastPrice( stockQuote.getLastPrice() );
        stockEntity.setLastPriceChange( stockQuote.getLastPriceChange() );
        this.stockService.saveStock( stockEntity );
        logMethodEnd( methodName, stockQuote );
    }

    /**
     * Updates the database stock table with the new price information
     * @param tickerSymbol
     * @param stockTickerQuote
     */
    private void updateStockPrice( final String tickerSymbol, final StockTickerQuote stockTickerQuote )
        throws StockNotFoundException,
               StockQuoteUnavailableException
    {
        StockEntity stockEntity = this.stockService.getStockEntity( tickerSymbol );
        stockEntity.setLastPrice( stockTickerQuote.getLastPrice() );
        stockEntity.setLastPriceChange( stockTickerQuote.getLastPriceChange() );
        this.stockService.saveStock( stockEntity );
    }

    /**
     * Using the ticker symbol of {@code container}, obtains and sets the stock's company name on the {@code container}
     * @param container
     */
    public void setCompanyName( final StockCompanyNameContainer container )
        throws StockNotFoundException,
               StockQuoteUnavailableException
    {
        final String methodName = "setCompanyName";
        Objects.requireNonNull( container.getTickerSymbol(), "container.getTickerSymbol() returns null" );
        logMethodBegin( methodName, container.getTickerSymbol() );
        StockEntity stockEntity = this.stockService.getStockEntity( container.getTickerSymbol() );
        final String companyName = stockEntity.getCompanyName();
        container.setCompanyName( companyName );
        logMethodEnd( methodName, companyName );
    }

    /**
     * Get the last price for the stock
     * @param tickerSymbol
     * @return Null if there are any exceptions
     */
    public BigDecimal getStockPrice( final String tickerSymbol )
        throws StockQuoteUnavailableException,
               StockNotFoundException
    {
        final String methodName = "getStockPrice";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        BigDecimal stockPrice = null;
        StockQuote stockQuote = this.getStockQuote( tickerSymbol, StockQuoteFetchMode.SYNCHRONOUS );
        if ( !stockQuote.getStockQuoteState().isNotFound() )
        {
            stockPrice = this.getStockQuote( tickerSymbol, StockQuoteFetchMode.SYNCHRONOUS ).getLastPrice();
        }
        logMethodBegin( methodName, stockPrice );
        return stockPrice;
    }

    /**
     * Makes a call to Yahoo to get the current stock information including the price, last price change, and company
     * name.  This information that was retrieved from Yahoo, is also saved back to the database stock table.
     * @param containers
     * @param stockQuoteFetchMode SYNCHRONOUS or ASYNCHRONOUS
     * @throws IOException
     */
    public void setStockQuoteInformation( final List<StockQuoteContainer> containers,
                                          final StockQuoteFetchMode stockQuoteFetchMode )
        throws StockQuoteUnavailableException,
               StockNotFoundException
    {
        final String methodName = "setStockQuoteInformation";
        logMethodBegin( methodName, stockQuoteFetchMode );
        Objects.requireNonNull( containers, "stockDomainEntities cannot be null" );
        for ( StockQuoteContainer container : containers )
        {
            this.setStockQuoteInformation( container, stockQuoteFetchMode );
        }
        logMethodEnd( methodName );
    }

    /**
     * Using the ticker symbol of {@code container}, obtains and sets the stock's company name on the {@code container}
     * @param container
     */
    public void updateStockPrice( final LastPriceContainer container )
    {
        final String methodName = "updateStockPrice";
        Objects.requireNonNull( container.getTickerSymbol(), "container.getTickerSymbol() returns null" );
        logMethodBegin( methodName, container.getTickerSymbol() );
        /*
        StockEntity stockEntity = this.stockQuoteCache
                                      .getStockEntity( container.getTickerSymbol() );
        final BigDecimal stockPrice = stockEntity.getLastPrice();
        container.setLastPrice( stockPrice );
        container.setLastPriceChange(  );
        logMethodEnd( methodName, stockPrice );
                                      */
    }

    @Autowired
    public void setStockQuoteCache( final StockQuoteCache stockQuoteCache )
    {
        this.stockQuoteCache = stockQuoteCache;
    }

    @Autowired
    public void setStockService( final StockContainerService stockService )
    {
        this.stockService = stockService;
    }

}
