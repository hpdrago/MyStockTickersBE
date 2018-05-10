package com.stocktracker.servicelayer.service;

import com.stocktracker.common.exceptions.DuplicateEntityException;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockQuoteNotFoundException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.entity.StockQuoteEntity;
import com.stocktracker.repositorylayer.repository.StockQuoteRepository;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheFetchMode;
import com.stocktracker.servicelayer.service.cache.stockpricequote.IEXTradingStockService;
import com.stocktracker.servicelayer.service.cache.stockquote.StockQuoteEntityCache;
import com.stocktracker.servicelayer.service.cache.stockquote.StockQuoteEntityCacheClient;
import com.stocktracker.servicelayer.service.cache.stockquote.StockQuoteEntityCacheEntry;
import com.stocktracker.weblayer.dto.StockQuoteDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.zankowski.iextrading4j.api.stocks.Quote;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * This service contains the methods for the StockQuoteEntity.
 */
@Service
public class StockQuoteEntityService extends VersionedEntityService<String,
                                                                    StockQuoteEntity,
                                                                    String,
                                                                    StockQuoteDTO,
                                                                    StockQuoteRepository>
{
    private StockQuoteEntityCache stockQuoteEntityCache;
    private StockQuoteRepository stockQuoteRepository;
    private IEXTradingStockService iexTradingStockService;

    /**
     * Set the stock price quotes on the {@code containers}.
     * @param clientList
     * @param fetchMode Synchronous or asynchronous.
     */
    public void setStockQuote( final List<? extends StockQuoteEntityCacheClient> clientList, final AsyncCacheFetchMode fetchMode  )
    {
        final String methodName = "setStockQuote";
        logMethodBegin( methodName, clientList.size() );
        Objects.requireNonNull( clientList, "clientList cannot be null" );
        clientList.forEach( client -> this.setStockQuote( client, fetchMode ));
        logMethodEnd( methodName );
    }

    /**
     * Retrieves the stock quote.  If the quote is in the cache, then the container is updated.  If the quote is not
     * in the cache and {@code informationCacheFetchMode} == ASYNCHRONOUS, the container's stock cache state will be updated to
     * reflect that the quote is being fetched.  If the {@code informationCacheFetchMode} == SYNCHRONOUS, the quote will be
     * fetched immediately and the container will be  updated.
     * @param stockQuoteEntityCacheClient
     * @param fetchMode Synchronous or Asynchronous.
     */
    public void setStockQuote( final StockQuoteEntityCacheClient stockQuoteEntityCacheClient, final AsyncCacheFetchMode fetchMode )
    {
        final String methodName = "setStockQuote";
        logMethodBegin( methodName );
        //final StockQuoteEntity stockQuoteEntity = this.context.getBean( StockQuoteEntity.class );
        StockQuoteEntityCacheEntry stockQuoteEntityCacheEntry;
        if ( fetchMode.isASynchronous() )
        {
            stockQuoteEntityCacheEntry = this.stockQuoteEntityCache
                                             .asynchronousGet( stockQuoteEntityCacheClient.getTickerSymbol() );
        }
        else
        {
            stockQuoteEntityCacheEntry = this.stockQuoteEntityCache
                                             .synchronousGet( stockQuoteEntityCacheClient.getTickerSymbol() );
        }
        switch ( stockQuoteEntityCacheClient.getStockQuoteCacheState() )
        {
            case FAILURE:
                stockQuoteEntityCacheClient.setError( stockQuoteEntityCacheEntry.getFetchThrowable().getMessage() );
                break;

            case CURRENT:
            case STALE:
                BeanUtils.copyProperties( stockQuoteEntityCacheEntry.getInformation(), stockQuoteEntityCacheClient );
                break;

            case NOT_FOUND:
                stockQuoteEntityCacheClient.setError( "Not Found" );
                break;
        }
        logMethodEnd( methodName );
    }

    /**
     * Get a stock quote for the ticker symbol.
     * @param tickerSymbol
     * @return
     * @throws StockQuoteNotFoundException
     */
    public StockQuoteEntity getStockQuote( final String tickerSymbol )
        throws StockNotFoundException
    {
        final String methodName = "getStockPriceQuote";
        logMethodBegin( methodName, tickerSymbol );
        StockQuoteEntity returnStockQuoteEntity = null;
        try
        {
            final StockQuoteEntity existingStockQuoteEntity = this.getEntity( tickerSymbol );
            /*
             * If the quote is more than 6 hours old.
             * The information in the quote does not need to updated often due to contents and we cache
             * the stock price in the cache.
             */
            if ( TimeUnit.MILLISECONDS.toHours( existingStockQuoteEntity.getLastQuoteRequestDate().getTime() ) > 6 )
            {
                logDebug( methodName, "{0} stock quote is stale, refetching..." );
                final StockQuoteEntity newStockQuoteEntity = getQuoteFromIEXTrading( tickerSymbol );
                returnStockQuoteEntity = newStockQuoteEntity;
            }
            else
            {
                returnStockQuoteEntity = existingStockQuoteEntity;
            }
        }
        catch( VersionedEntityNotFoundException e )
        {
            returnStockQuoteEntity = getQuoteFromIEXTrading( tickerSymbol );
        }
        logMethodEnd( methodName, returnStockQuoteEntity );
        return returnStockQuoteEntity;
    }

    /**
     * Contact IEXTrading to get the Quote for the stock.
     * @param tickerSymbol
     * @return
     * @throws StockNotFoundException
     */
    public StockQuoteEntity getQuoteFromIEXTrading( final String tickerSymbol )
        throws StockNotFoundException
    {
        final String methodName = "getQuoteFromIEXTrading";
        logMethodBegin( methodName, tickerSymbol );
        final Quote quote = this.iexTradingStockService
                                .getQuote( tickerSymbol );
        StockQuoteEntity returnStockQuoteEntity = null;
        final StockQuoteEntity newStockQuoteEntity = quoteToStockQuoteEntity( quote );
        boolean mismatch = false;
        do
        {
            mismatch = false;
            try
            {
                /*
                 * We got a new stock quote entity above from the previous call, we need to get the vital database values
                 * and merge the two entities' properties
                 */
                final StockQuoteEntity existingStockQuoteEntity = this.getEntity( tickerSymbol );
                existingStockQuoteEntity.setLastQuoteRequestDate( new Timestamp( System.currentTimeMillis() ));
                returnStockQuoteEntity = this.saveEntity( newStockQuoteEntity );
            }
            catch( EntityVersionMismatchException e1 )
            {
                logDebug( methodName, "Entity version mismatch, trying again.  {0}", returnStockQuoteEntity );
                mismatch = true;
            }
            catch( VersionedEntityNotFoundException e )
            {
                /*
                 * If it doesn't exist then add it.
                 */
                try
                {
                    newStockQuoteEntity.setLastQuoteRequestDate( new Timestamp( System.currentTimeMillis() ));
                    returnStockQuoteEntity = this.addEntity( newStockQuoteEntity );
                }
                catch( EntityVersionMismatchException e1 )
                {
                    mismatch = true;
                    logDebug( methodName, "Entity version mismatch, trying again.  {0}", returnStockQuoteEntity );
                }
                catch( DuplicateEntityException e1 )
                {
                    logError( methodName, "Should not get a duplicate after checking for existence: " +
                                          newStockQuoteEntity, e );
                }
            }
        }
        while ( mismatch );
        logMethodEnd( methodName, returnStockQuoteEntity );
        return returnStockQuoteEntity;
    }

    /**
     * Converts the quote to the quote entity.
     * @param quote
     * @return
     */
    public StockQuoteEntity quoteToStockQuoteEntity( final Quote quote )
    {
        final StockQuoteEntity stockQuoteEntity = this.createEntity();
        stockQuoteEntity.setTickerSymbol( quote.getSymbol() );
        stockQuoteEntity.setCalculationPrice( quote.getCalculationPrice() );
        stockQuoteEntity.setOpenPrice( quote.getOpen() );
        stockQuoteEntity.setClosePrice( quote.getClose() );
        stockQuoteEntity.setHighPrice( quote.getHigh() );
        stockQuoteEntity.setLowPrice( quote.getLow() );
        stockQuoteEntity.setLatestPrice( quote.getLatestPrice() );
        stockQuoteEntity.setLatestPriceSource( quote.getLatestSource() );
        stockQuoteEntity.setLatestPriceTime( quote.getLatestTime() );
        stockQuoteEntity.setLatestUpdate( quote.getLatestUpdate() );
        stockQuoteEntity.setLatestVolume( quote.getLatestVolume().longValue() );
        stockQuoteEntity.setDelayedPrice( quote.getDelayedPrice() );
        stockQuoteEntity.setDelayedPriceTime( quote.getDelayedPriceTime() );
        stockQuoteEntity.setPreviousClose( quote.getPreviousClose() );
        stockQuoteEntity.setChangeAmount( quote.getChange() );
        stockQuoteEntity.setChangePercent( quote.getChangePercent() );
        stockQuoteEntity.setThirtyDayAvgVolume( quote.getAvgTotalVolume().longValue() );
        stockQuoteEntity.setMarketCap( quote.getMarketCap().longValue() );
        stockQuoteEntity.setPeRatio( quote.getPeRatio() );
        stockQuoteEntity.setWeek52High( quote.getWeek52High() );
        stockQuoteEntity.setWeek52Low( quote.getWeek52Low() );
        stockQuoteEntity.setYtdChangePercent( quote.getYtdChange() );
        stockQuoteEntity.setLastQuoteRequestDate( new Timestamp( System.currentTimeMillis() ));
        return stockQuoteEntity;
    }

    @Override
    protected StockQuoteDTO createDTO()
    {
        return this.context.getBean( StockQuoteDTO.class );
    }

    @Override
    protected StockQuoteEntity createEntity()
    {
        return this.context.getBean( StockQuoteEntity.class );
    }

    @Override
    protected StockQuoteRepository getRepository()
    {
        return this.stockQuoteRepository;
    }

    @Autowired
    public void setStockQuoteRepository( final StockQuoteRepository stockQuoteRepository )
    {
        this.stockQuoteRepository = stockQuoteRepository;
    }

    @Autowired
    public void setIexTradingStockService( final IEXTradingStockService iexTradingStockService )
    {
        this.iexTradingStockService = iexTradingStockService;
    }

    @Autowired
    public void setStockQuoteEntityCache( final StockQuoteEntityCache stockQuoteEntityCache )
    {
        this.stockQuoteEntityCache = stockQuoteEntityCache;
    }

}
