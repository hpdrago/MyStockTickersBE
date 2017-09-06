package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.StockNotFoundInDatabaseException;
import com.stocktracker.repositorylayer.entity.StockEntity;
import com.stocktracker.repositorylayer.entity.StockSectorEntity;
import com.stocktracker.repositorylayer.entity.StockSubSectorEntity;
import com.stocktracker.servicelayer.entity.StockDE;
import com.stocktracker.servicelayer.entity.StockSectorDE;
import com.stocktracker.servicelayer.entity.StockSubSectorDE;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

/**
 * Created by mike on 9/11/2016.
 */
@Service
public class StockService extends BaseService implements MyLogger
{
    /**
     * Transforms {@code Page<ENTITY>} objects into {@code Page<DTO>} objects.
     *
     * @param pageRequest The information of the requested page.
     * @param source      The {@code Page<ENTITY>} object.
     * @param updateStockPrices If true, the last stock price will be updated
     * @return The created {@code Page<DTO>} object.
     */
    private Page<StockDE> mapStockEntityPageIntoStockDEPage( Pageable pageRequest, Page<StockEntity> source,
                                                             boolean updateStockPrices )
    {
        Objects.requireNonNull( pageRequest, "pageRequest cannot be null" );
        Objects.requireNonNull( source, "source cannot be null" );
        List<StockDE> stockDomainEntities = listCopyStockEntityToStockDE.copy( source.getContent() );
        if ( updateStockPrices )
        {
            updateStockPrices( stockDomainEntities );
        }
        return new PageImpl<>( stockDomainEntities, pageRequest, source.getTotalElements() );
    }

    /**
     * Adds the current stock prices for the stocks contained within {@code stockDomainEntities}
     * @param stockDomainEntities
     */
    private void updateStockPrices( final List<StockDE> stockDomainEntities )
    {
        final String methodName = "updateStockPrices";
        logMethodBegin( methodName );
        Objects.requireNonNull( stockDomainEntities, "stockDomainEntities cannot be null" );
        for ( StockDE stockDE : stockDomainEntities )
        {
            updateStockPrice( stockDE );
        }
        logMethodEnd( methodName );
    }

    /**
     * Updates the stock price to the last price
     * @param stockDE
     */
    private void updateStockPrice( final StockDE stockDE )
    {
        Objects.requireNonNull( stockDE, "stockDE cannot be null" );
        StockTickerQuote stockTickerQuote = this.getStockQuote( stockDE.getTickerSymbol() );
        stockDE.updateFromQuote( stockTickerQuote );
    }

    /**
     * Get a page of StockDomainEntities's
     * @param pageRequest
     * @return
     */
    public Page<StockDE> getPage( final Pageable pageRequest, final boolean withStockPrices )
    {
        final String methodName = "getPage";
        logMethodBegin( methodName, pageRequest );
        Objects.requireNonNull( pageRequest, "pageRequest cannot be null" );
        /*
         * Get the page from the database
         */
        Page<StockEntity> stockEntities = stockRepository.findAll( pageRequest );
        /*
         * Map from Entity to DomainEntity
         */
        Page<StockDE> stockDEPage = mapStockEntityPageIntoStockDEPage( pageRequest, stockEntities, withStockPrices );
        logMethodEnd( methodName );
        return stockDEPage;
    }

    /**
     * Get companies matching either the company name or ticker symbol strings
     * @param pageRequest
     * @param companiesLike
     * @param withStockPrices
     * @return
     */
    public Page<StockDE> getCompaniesLike( final Pageable pageRequest, final String companiesLike,
                                           final boolean withStockPrices )
    {
        final String methodName = "getCompaniesLike";
        logMethodBegin( methodName, pageRequest, companiesLike );
        Objects.requireNonNull( pageRequest, "pageRequest cannot be null" );
        Objects.requireNonNull( companiesLike, "companiesLike cannot be null" );
        /*
         * Get the page from the database
         */
        Page<StockEntity> stockEntities = stockRepository.findByCompanyNameIsLikeOrTickerSymbolIsLike(
            pageRequest, companiesLike + "%", companiesLike + "%" );
        /*
         * Map from Entity to DomainEntity
         */
        Page<StockDE> stockDEPage = mapStockEntityPageIntoStockDEPage( pageRequest, stockEntities, withStockPrices );
        logMethodEnd( methodName );
        return stockDEPage;
    }

    /**
     * Gets a single stock for the {@code tickerSymbol} from the database
     * @param tickerSymbol
     * @return
     * @throws StockNotFoundInDatabaseException if {@code tickerSymbol} is not found in the stock table
     */
    public StockDE getStock( final String tickerSymbol )
        throws StockNotFoundInDatabaseException
    {
        final String methodName = "getStock";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        /*
         * Get the stock from the database
         */
        StockEntity stockEntity = stockRepository.findOne( tickerSymbol );
        if ( stockEntity == null )
        {
            throw new StockNotFoundInDatabaseException( tickerSymbol );
        }
        StockDE stockDE = StockDE.newInstance( stockEntity );
        updateStockPrice( stockDE );
        logMethodEnd( methodName, stockDE );
        return stockDE;
    }

    /**
     * Determines if the {@code tickerSymbol exists}
     * @param tickerSymbol
     * @return
     */
    public boolean isStockExistsInDatabase( final String tickerSymbol )
    {
        final String methodName = "isStockExistsInDatabase";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        boolean exists = stockRepository.exists( tickerSymbol );
        logMethodEnd( methodName, exists );
        return exists;
    }

    /**
     * Add a stock to the database
     * @param stockDE
     * @return
     */
    public StockDE addStock( final StockDE stockDE )
    {
        final String methodName = "addStock";
        logMethodBegin( methodName, stockDE );
        Objects.requireNonNull( stockDE, "stockDE cannot be null" );
        StockEntity stockEntity = StockEntity.newInstance( stockDE );
        stockEntity = stockRepository.save( stockEntity );
        StockDE returnStockDE = StockDE.newInstance( stockEntity );
        logMethodEnd( methodName, returnStockDE );
        return returnStockDE;
    }

    /**
     * Delete the stock
     * @param tickerSymbol
     */
    public void deleteStock( final String tickerSymbol )
    {
        final String methodName = "deleteStock";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        stockRepository.delete( tickerSymbol );
        logMethodEnd( methodName );
    }

    /**
     * Get the stock price for the {@code tickerSymbol}
     * @param tickerSymbol
     * @return -1 if there is an error, otherwise the last stock price will be returned
     */
    public StockTickerQuote getStockQuote( final String tickerSymbol )
    {
        final String methodName = "getStockQuote";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        StockTickerQuote stockTickerQuote = null;
        try
        {
            Stock stock = getStockFromYahoo( tickerSymbol );
            stockTickerQuote = createStockTickerQuote( stock );
            logMethodEnd( methodName, String.format( "{0} {1}", tickerSymbol, stockTickerQuote.getLastPrice() ));
        }
        catch( Exception e )
        {
            logError( methodName, e );
        }
        return stockTickerQuote;
    }

    /**
     * Get the last price for the stock
     * @param tickerSymbol
     * @return Null if there are any exceptions
     */
    public BigDecimal getStockPrice( final String tickerSymbol )
    {
        final String methodName = "getStockQuote";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        StockTickerQuote stockTickerQuote = getStockQuote( tickerSymbol );
        BigDecimal stockPrice = null;
        if ( stockTickerQuote != null )
        {
            stockPrice = stockTickerQuote.getLastPrice();
        }
        logMethodBegin( methodName, stockPrice );
        return stockPrice;
    }

    /**
     * Create a {@code StockTickerQuote} from a {@code Stock} Yahoo stock instance
     * @param stock
     * @return
     */
    private StockTickerQuote createStockTickerQuote( final Stock stock )
    {
        StockTickerQuote stockTickerQuote = new StockTickerQuote();
        stockTickerQuote.setTickerSymbol( stock.getSymbol() );
        stockTickerQuote.setLastPrice( stock.getQuote().getPrice() );
        if ( stock.getQuote().getLastTradeTime() != null )
        {
            stockTickerQuote.setLastPriceUpdate( new Timestamp( stock.getQuote().getLastTradeTime().getTimeInMillis() ) );
        }
        return stockTickerQuote;
    }

    /**
     * Add a Yahoo stock to the database
     * @param yahooStock
     * @return {@code StockDE} the stock that was added to the database
     */
    public StockDE addStock( final Stock yahooStock )
    {
        final String methodName = "addStock";
        logMethodBegin( methodName, yahooStock );
        Objects.requireNonNull( yahooStock, "yahooStock cannot be null" );
        StockDE stockDE = StockDE.newInstance();
        stockDE.setTickerSymbol( yahooStock.getSymbol() );
        stockDE.setCompanyName( yahooStock.getName() );
        stockDE.setUserEntered( false );
        stockDE.setExchange( yahooStock.getStockExchange() );
        StockTickerQuote stockTickerQuote = createStockTickerQuote( yahooStock );
        stockDE.setLastPriceChange( stockTickerQuote.getLastPriceChange() );
        stockDE.setLastPrice( yahooStock.getQuote().getPrice() );
        stockDE.setCreatedBy( 1 );
        this.addStock( stockDE );
        logMethodEnd( methodName, stockDE );
        return stockDE;
    }

    /**
     * Get the stock information from Yahoo
     * @param tickerSymbol
     * @return
     * @throws IOException
     */
    public Stock getStockFromYahoo( final String tickerSymbol )
        throws IOException
    {
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        return YahooFinance.get( tickerSymbol );
    }

    /**
     * Get all of the stock sector information
     * @return
     */
    public List<StockSectorDE> getStockSectors()
    {
        final String methodName = "getStockSectors";
        logMethodBegin( methodName );
        List<StockSectorEntity> stockSectorEntities = stockSectorRepository.findAll();
        List<StockSectorDE> stockSectorList = listCopyStockSectorEntityToStockSectorDE.copy( stockSectorEntities );
        logMethodEnd( methodName );
        return stockSectorList;
    }

    /**
     * Get all of the stock sub sectors information
     * @return
     */
    public List<StockSubSectorDE> getStockSubSectors()
    {
        final String methodName = "getStockSubSectors";
        logMethodBegin( methodName );
        List<StockSubSectorEntity> stockSubSectorEntities = stockSubSectorRepository.findAll();
        List<StockSubSectorDE> stockSubSectorList = listCopyStockSubSectorEntityToStockSubSectorDE.copy( stockSubSectorEntities );
        logMethodEnd( methodName );
        return stockSubSectorList;
    }

}
