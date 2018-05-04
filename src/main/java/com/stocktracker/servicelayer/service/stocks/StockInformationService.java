package com.stocktracker.servicelayer.service.stocks;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.StockCompanyNotFoundException;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.repositorylayer.entity.StockCompanyEntity;
import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import com.stocktracker.servicelayer.service.StockCompanyEntityService;
import com.stocktracker.servicelayer.service.StockQuoteEntityService;
import com.stocktracker.servicelayer.stockinformationprovider.IEXTradingStockService;
import com.stocktracker.servicelayer.stockinformationprovider.StockPriceCache;
import com.stocktracker.servicelayer.stockinformationprovider.StockPriceCacheEntry;
import com.stocktracker.servicelayer.stockinformationprovider.StockPriceCacheState;
import com.stocktracker.servicelayer.stockinformationprovider.StockPriceFetchMode;
import com.stocktracker.servicelayer.stockinformationprovider.StockPriceQuoteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * This service manages all of the methods to obtain stock quote information.
 */
@Service
public class StockInformationService implements MyLogger
{
    private StockPriceCache stockPriceCache;
    private StockQuoteEntityService stockQuoteEntityService;
    private StockCompanyEntityService stockCompanyEntityService;
    private IEXTradingStockService iexTradingStockService;

    /**
     * Loads {@code container} with the company information.
     * @param container
     */
    public void setCompanyInformation( final StockCompanyContainer container )
    {
        final String methodName = "setCompanyInformation";
        logMethodBegin( methodName, container );
        final StockCompanyEntity stockCompanyEntity;
        try
        {
            stockCompanyEntity = this.stockCompanyEntityService
                                     .getStockCompanyEntity( container.getTickerSymbol() );
            container.setCompanyName( stockCompanyEntity.getCompanyName() );
            container.setIndustry( stockCompanyEntity.getIndustry() );
            container.setSector( stockCompanyEntity.getSector() );
        }
        catch( StockNotFoundException e )
        {
            logError( methodName, "Company not found for " + container.getTickerSymbol() );
        }
    }

    /**
     * Updates the quote cache with the last price.
     * @param tickerSymbol
     * @param lastPrice
     * @throws StockNotFoundException
     */
    public void updateStockPrice( final String tickerSymbol, final BigDecimal lastPrice )
        throws StockNotFoundException
    {
        final String methodName = "updateStockPrice";
        logMethodBegin( methodName, tickerSymbol, lastPrice );
        this.stockPriceCache
            .updateStockPrice( tickerSymbol, lastPrice );
        logMethodEnd( methodName );
    }

    /**
     * Get the last price for the stock synchronously.
     * @param tickerSymbol
     * @return The last stock price for the {@code tickerSymbol}
     * @throws StockNotFoundException
     */
    public BigDecimal getLastPrice( final String tickerSymbol )
        throws StockNotFoundException
    {
        final String methodName = "getStockQuote";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        final StockPriceCacheEntry stockPriceCacheEntry = this.stockPriceCache
                                                              .getStockPrice( tickerSymbol, StockPriceFetchMode.SYNCHRONOUS );
        BigDecimal stockPrice = stockPriceCacheEntry.getStockPrice();
        logMethodBegin( methodName, stockPrice );
        return stockPrice;
    }

    /**
     * Gets a stock quote from the {@code StockQuoteCache}.
     * If the fetch mode is SYNCHRONOUS, this method will block and wait while the quote is retrieved.
     * If the fetch mode is ASYNCHRONOUS and the stock quote is not found or is stale, this block will return without
     * a complete quote.  The quote can be obtained by a subsequent request.  If the stock quote is found and is current,
     * the behaviour is that of the SYNCHRONOUS mode and this method will return with a complete stock quote.
     * @param tickerSymbol
     * @param stockPriceFetchMode
     * @return
     * @throws StockNotFoundException
     * @throws StockCompanyNotFoundException
     */
    public StockPriceQuoteDTO getStockQuote( final String tickerSymbol,
                                             final StockPriceFetchMode stockPriceFetchMode )
        throws StockNotFoundException,
               StockCompanyNotFoundException
    {
        final String methodName = "getStockQuote";
        logMethodBegin( methodName, tickerSymbol, stockPriceFetchMode );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Objects.requireNonNull( stockPriceFetchMode, "stockPriceFetchMode cannot be null" );
        Assert.isTrue( !tickerSymbol.equalsIgnoreCase( "null" ), "ticker symbol cannot be 'null'");
        final StockPriceCacheEntry stockPriceCacheEntry = this.stockPriceCache
                                                              .getStockPrice( tickerSymbol, stockPriceFetchMode );
        StockPriceQuoteDTO stockPriceQuoteDTO = null;
        if ( stockPriceFetchMode.isSynchronous() )
        {
            switch ( stockPriceCacheEntry.getCacheState() )
            {
                case FAILURE:
                    throw new StockNotFoundException( tickerSymbol, stockPriceCacheEntry.getFetchException() );
                case NOT_FOUND:
                    stockPriceQuoteDTO = stockPriceCacheEntryToStockPriceDTO( tickerSymbol, stockPriceCacheEntry );
                    break;
                case CURRENT:
                case STALE:
                    stockPriceQuoteDTO = stockPriceCacheEntryToStockPriceDTO( tickerSymbol, stockPriceCacheEntry );
                    final StockCompanyEntity stockCompanyEntity = this.stockCompanyEntityService
                                                                      .getStockCompany( tickerSymbol );
                    final StockQuoteEntity stockQuoteEntity = this.stockQuoteEntityService
                                                                  .getStockQuote( tickerSymbol );
                    stockPriceQuoteDTO.setCompanyName( stockCompanyEntity.getCompanyName() );
                    stockPriceQuoteDTO.setOpenPrice( stockQuoteEntity.getOpenPrice() );
            }
        }
        logMethodEnd( methodName, stockPriceQuoteDTO );
        return stockPriceQuoteDTO;
    }
    /**
     * Obtains the stock information from the stock cache with the current stock information including the price,
     * last price change, and company name.
     * This information that was retrieved from the cache, is also saved back to the database stock table.
     * @param container
     * @param stockPriceFetchMode
     */
    public void setStockPrice( final StockPriceContainer container,
                               final StockPriceFetchMode stockPriceFetchMode )
    {
        final String methodName = "setStockPrice";
        logMethodBegin( methodName, container.getTickerSymbol(), stockPriceFetchMode );
        Objects.requireNonNull( container, "container cannot be null" );
        Objects.requireNonNull( container.getTickerSymbol(), "container.getTickerSymbol() returns null" );
        StockPriceCacheEntry stockPriceCacheEntry = null;
        try
        {
            stockPriceCacheEntry = this.stockPriceCache
                                       .getStockPrice( container.getTickerSymbol(), stockPriceFetchMode );
            container.setStockPriceCacheState( stockPriceCacheEntry.getCacheState() );
            switch ( stockPriceCacheEntry.getCacheState() )
            {
                case CURRENT:
                    container.setLastPrice( stockPriceCacheEntry.getStockPrice() );
                    break;

                case NOT_FOUND:
                    // the container is marked as not found.
                    break;

                case STALE:
                    if ( stockPriceFetchMode.isSynchronous() )
                    {
                        container.setLastPrice( stockPriceCacheEntry.getFetchSubject()
                                                                    .asObservable()
                                                                    .doOnError( throwable ->
                                                                                    container
                                                                                        .setStockPriceCacheState( StockPriceCacheState.FAILURE ) )
                                                                    .toBlocking()
                                                                    .first() );
                    }
                    break;
            }
        }
        catch( StockNotFoundException e )
        {
            // ignore, just don't update the stock price.
        }
        logMethodEnd( methodName, container );
    }

    /**
     * Create the StockDTO from the stock price cache entry.
     * @param tickerSymbol
     * @param stockPriceCacheEntry
     * @return
     */
    private StockPriceQuoteDTO stockPriceCacheEntryToStockPriceDTO( final String tickerSymbol,
                                                                    final StockPriceCacheEntry stockPriceCacheEntry )
    {
        final StockPriceQuoteDTO stockPriceQuoteDTO = new StockPriceQuoteDTO();
        stockPriceQuoteDTO.setTickerSymbol( tickerSymbol );
        stockPriceQuoteDTO.setLastPrice( stockPriceCacheEntry.getStockPrice() );
        stockPriceQuoteDTO.setStockPriceCacheState( stockPriceCacheEntry.getCacheState() );
        stockPriceQuoteDTO.setExpirationTime( stockPriceCacheEntry.getExpiration() );
        return stockPriceQuoteDTO;
    }

    /**
     * Makes a call to IEXTrading to get the current stock prices for the list of stock price containers.
     * Stock prices are fetched asynchronously.
     * @param containers
     */
    public void setStockPrice( final List<? extends StockPriceContainer> containers )
    {
        final String methodName = "setStockPrice";
        logMethodBegin( methodName );
        Objects.requireNonNull( containers, "stockDomainEntities cannot be null" );
        for ( final StockPriceContainer container : containers )
        {
            this.setStockPrice( container, StockPriceFetchMode.ASYNCHRONOUS );
        }
        logMethodEnd( methodName );
    }

    @Autowired
    public void setStockPriceCache( final StockPriceCache stockPriceCache )
    {
        this.stockPriceCache = stockPriceCache;
    }

    @Autowired
    public void setStockCompanyEntityService( final StockCompanyEntityService stockCompanyEntityService )
    {
        this.stockCompanyEntityService = stockCompanyEntityService;
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

}
