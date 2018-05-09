package com.stocktracker.servicelayer.service.stocks;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.StockCompanyNotFoundException;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.repositorylayer.entity.StockCompanyEntity;
import com.stocktracker.servicelayer.service.BaseService;
import com.stocktracker.servicelayer.service.StockCompanyEntityService;
import com.stocktracker.servicelayer.service.cache.common.InformationCacheFetchMode;
import com.stocktracker.servicelayer.service.cache.stockpricequote.StockPriceQuoteCache;
import com.stocktracker.servicelayer.service.cache.stockpricequote.StockPriceQuoteCacheEntry;
import com.stocktracker.servicelayer.service.cache.stockquote.StockQuoteEntityCache;
import com.stocktracker.servicelayer.service.cache.common.InformationCacheEntryState;
import com.stocktracker.servicelayer.service.cache.stockpricequote.StockPriceQuote;
import com.stocktracker.servicelayer.service.cache.stockpricequote.StockPriceQuoteContainer;
import com.stocktracker.weblayer.dto.StockPriceQuoteDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.stocktracker.servicelayer.service.cache.common.InformationCacheFetchMode.ASYNCHRONOUS;

/**
 * This service manages all of the methods to obtain stock quote information.
 */
@Service
public class StockInformationService extends BaseService
                                     implements MyLogger
{
    private StockPriceQuoteCache stockPriceQuoteCache;
    private StockCompanyEntityService stockCompanyEntityService;
    private StockQuoteEntityCache stockQuoteEntityCache;

    /**
     * Loads {@code container} with the company information.
     *
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
     *
     * @param tickerSymbol
     * @param lastPrice
     * @throws StockNotFoundException
     */
    public void updateStockPrice( final String tickerSymbol, final BigDecimal lastPrice )
        throws StockNotFoundException
    {
        final String methodName = "updateStockPrice";
        logMethodBegin( methodName, tickerSymbol, lastPrice );
        this.stockPriceQuoteCache
            .updateStockPrice( tickerSymbol, lastPrice );
        logMethodEnd( methodName );
    }

    /**
     * Get the last price for the stock synchronously.
     *
     * @param tickerSymbol
     * @return The last stock price for the {@code tickerSymbol}
     * @throws StockNotFoundException
     */
    public BigDecimal getLastPrice( final String tickerSymbol )
        throws StockNotFoundException
    {
        final String methodName = "getStockPriceQuote";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        final StockPriceQuoteCacheEntry stockPriceQuoteCacheEntry = this.stockPriceQuoteCache
                                                                        .synchronousGet( tickerSymbol );
        BigDecimal stockPrice = stockPriceQuoteCacheEntry.getInformation().getLastPrice();
        logMethodBegin( methodName, stockPrice );
        return stockPrice;
    }
    /**
     * Gets a stock quote from the {@code StockQuoteCache} asynchronously.  A valid result may be returned if the
     * cache entry is not STALE. Otherwise, a {@code StockPriceQuoteDTO} will be returned with the stale information
     * if there is any and appropriate cache states that can be sent back to the client so that it will make a
     * subsequent request to get the updated stock quote.
     *
     * @param tickerSymbol
     * @return
     * @throws StockNotFoundException
     * @throws StockCompanyNotFoundException
     */
    public StockPriceQuoteDTO getAsynchronousStockPriceQuote( final String tickerSymbol )
        throws StockNotFoundException
    {
        final String methodName = "getAynchronousStockPriceQuote";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Assert.isTrue( !tickerSymbol.equalsIgnoreCase( "null" ), "ticker symbol cannot be 'null'" );
        final StockPriceQuoteCacheEntry stockPriceQuoteCacheEntry = this.stockPriceQuoteCache
                                                                        .asynchronousGet( tickerSymbol );
        final StockPriceQuoteDTO stockPriceQuoteDTO = handleStockPriceQuoteCacheEntry( tickerSymbol, stockPriceQuoteCacheEntry );
        logMethodEnd( methodName, stockPriceQuoteDTO );
        return stockPriceQuoteDTO;

    }

    /**
     * Gets a stock quote from the {@code StockPriceQuoteCache}.
     * This method will block and wait while the quote is retrieved.
     *
     * @param tickerSymbol
     * @return
     * @throws StockNotFoundException
     */
    public StockPriceQuoteDTO getSynchronousStockPriceQuote( final String tickerSymbol )
        throws StockNotFoundException
    {
        final String methodName = "getSynchronousStockPriceQuote";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Assert.isTrue( !tickerSymbol.equalsIgnoreCase( "null" ), "ticker symbol cannot be 'null'" );
        final StockPriceQuoteCacheEntry stockPriceQuoteCacheEntry = this.stockPriceQuoteCache
                                                                        .synchronousGet( tickerSymbol );
        final StockPriceQuoteDTO stockPriceQuoteDTO = handleStockPriceQuoteCacheEntry( tickerSymbol, stockPriceQuoteCacheEntry );
        logMethodEnd( methodName, stockPriceQuoteDTO );
        return stockPriceQuoteDTO;
    }

    private StockPriceQuoteDTO handleStockPriceQuoteCacheEntry( final String tickerSymbol, final StockPriceQuoteCacheEntry stockPriceQuoteCacheEntry )
    {
        StockPriceQuote stockPriceQuote = null;
        switch ( stockPriceQuoteCacheEntry.getCacheState() )
        {
            case FAILURE:
                throw new StockNotFoundException( tickerSymbol, stockPriceQuoteCacheEntry.getFetchThrowable() );

            case NOT_FOUND:
                stockPriceQuote = stockPriceCacheEntryToStockPriceDTO( tickerSymbol, stockPriceQuoteCacheEntry );
                break;

            case CURRENT:
            case STALE:
                stockPriceQuote = stockPriceCacheEntryToStockPriceDTO( tickerSymbol, stockPriceQuoteCacheEntry );
                setCompanyProperties( tickerSymbol, stockPriceQuote );
                this.stockQuoteEntityCache
                    .asynchronousGet( tickerSymbol, stockPriceQuote );
        }
        return this.stockPriceQuoteToDTO( stockPriceQuote );
    }

    /**
     * Gets the Stock Company information and updates the properaties in {@code stockPriceQuote}
     *
     * @param tickerSymbol
     * @param stockPriceQuote
     */
    private void setCompanyProperties( final String tickerSymbol, final StockPriceQuote stockPriceQuote )
    {
        final StockCompanyEntity stockCompanyEntity = this.stockCompanyEntityService
            .getStockCompanyEntity( tickerSymbol );
        stockPriceQuote.setCompanyName( stockCompanyEntity.getCompanyName() );
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
            this.setStockPrice( container, ASYNCHRONOUS );
        }
        logMethodEnd( methodName );
    }

    /**
     * Obtains the stock information from the stock cache with the current stock information including the price,
     * last price change, and company name.
     * This information that was retrieved from the cache, is also saved back to the database stock table.
     * @param container
     * @param informationCacheFetchMode
     */
    public void setStockPrice( final StockPriceContainer container,
                               final InformationCacheFetchMode informationCacheFetchMode )
    {
        final String methodName = "setStockPrice";
        logMethodBegin( methodName, container.getTickerSymbol(), informationCacheFetchMode );
        Objects.requireNonNull( container, "container cannot be null" );
        Objects.requireNonNull( container.getTickerSymbol(), "container.getTickerSymbol() returns null" );
        StockPriceQuoteCacheEntry stockPriceQuoteCacheEntry = null;
        try
        {
            if ( informationCacheFetchMode.isSynchronous() )
            {
                stockPriceQuoteCacheEntry = this.stockPriceQuoteCache
                                                .synchronousGet( container.getTickerSymbol() );
            }
            else
            {
                stockPriceQuoteCacheEntry = this.stockPriceQuoteCache
                                                .asynchronousGet( container.getTickerSymbol() );
            }
            container.setStockPriceCacheState( stockPriceQuoteCacheEntry.getCacheState() );
            switch ( stockPriceQuoteCacheEntry.getCacheState() )
            {
                case CURRENT:
                    container.setLastPrice( stockPriceQuoteCacheEntry.getInformation().getLastPrice() );
                    break;

                case NOT_FOUND:
                    // the container is marked as not found.
                    break;

                case STALE:
                    if ( informationCacheFetchMode.isSynchronous() )
                    {
                        Optional<StockPriceQuote> optionalStockPriceQuote = stockPriceQuoteCacheEntry.getFetchSubject()
                                                                                                     .doOnError( throwable ->
                                                                                                                container
                                                                                                                    .setStockPriceCacheState( InformationCacheEntryState.FAILURE ) )
                                                                                                     .blockingFirst();
                        if ( optionalStockPriceQuote.isPresent() )
                        {
                            container.setLastPrice( optionalStockPriceQuote.get().getLastPrice() );
                        }
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
     * Set the stock price quotes on the {@code containers}
     * @param containers
     */
    public void setStockPriceQuote( final List<? extends StockPriceQuoteContainer> containers )
    {
        final String methodName = "setStockPriceQuote";
        logMethodBegin( methodName, containers.size() );
        Objects.requireNonNull( containers, "stockDomainEntities cannot be null" );
        for ( final StockPriceQuoteContainer container : containers )
        {
            this.setStockPriceQuote( container );
        }
        logMethodEnd( methodName );
    }

    /**
     * Retrieves the stock quote.  If the quote is in the cache, then the container is updated.  If the quote is not
     * in the cache and {@code informationCacheFetchMode} == ASYNCHRONOUS, the container's stock cache state will be updated to
     * reflect that the quote is being fetched.  If the {@code informationCacheFetchMode} == SYNCHRONOUS, the quote will be
     * fetched immediately and the container will be  updated.
     * @param container
     */
    public void setStockPriceQuote( final StockPriceQuoteContainer container )
    {
        final String methodName = "setStockPriceQuote";
        logMethodBegin( methodName );
        final StockPriceQuoteDTO stockPriceQuoteDTO = this.getAsynchronousStockPriceQuote( container.getTickerSymbol() );
        BeanUtils.copyProperties( stockPriceQuoteDTO, container );
        logMethodEnd( methodName );
    }

    /**
     * Create the StockDTO from the stock price cache entry.
     * @param tickerSymbol
     * @param stockPriceQuoteCacheEntry
     * @return
     */
    private StockPriceQuote stockPriceCacheEntryToStockPriceDTO( final String tickerSymbol,
                                                                 final StockPriceQuoteCacheEntry stockPriceQuoteCacheEntry )
    {
        final StockPriceQuote stockPriceQuoteDTO = new StockPriceQuote();
        stockPriceQuoteDTO.setTickerSymbol( tickerSymbol );
        stockPriceQuoteDTO.setLastPrice( stockPriceQuoteCacheEntry.getInformation().getLastPrice() );
        stockPriceQuoteDTO.setStockPriceCacheState( stockPriceQuoteCacheEntry.getCacheState() );
        stockPriceQuoteDTO.setExpirationTime( stockPriceQuoteCacheEntry.getExpirationTime() );
        return stockPriceQuoteDTO;
    }

    /**
     * Converts the StockPriceQuoteEntity into a StockPriceQuoteDTO.
     * @param stockPriceQuote
     * @return
     */
    private StockPriceQuoteDTO stockPriceQuoteToDTO( final StockPriceQuote stockPriceQuote )
    {
        final StockPriceQuoteDTO stockPriceQuoteDTO = this.context.getBean( StockPriceQuoteDTO.class );
        BeanUtils.copyProperties( stockPriceQuote, stockPriceQuoteDTO );
        return stockPriceQuoteDTO;
    }

    @Autowired
    public void setStockPriceQuoteCache( final StockPriceQuoteCache stockPriceQuoteCache )
    {
        this.stockPriceQuoteCache = stockPriceQuoteCache;
    }

    @Autowired
    public void setStockCompanyEntityService( final StockCompanyEntityService stockCompanyEntityService )
    {
        this.stockCompanyEntityService = stockCompanyEntityService;
    }

    @Autowired
    public void setStockQuoteEntityCache( final StockQuoteEntityCache stockQuoteEntityCache )
    {
        this.stockQuoteEntityCache = stockQuoteEntityCache;
    }

}
