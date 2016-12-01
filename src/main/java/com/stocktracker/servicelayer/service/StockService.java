package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.db.entity.StockEntity;
import com.stocktracker.repositorylayer.db.entity.StockSectorEntity;
import com.stocktracker.repositorylayer.db.entity.StockSubSectorEntity;
import com.stocktracker.repositorylayer.exceptions.StockNotFoundException;
import com.stocktracker.servicelayer.entity.StockDE;
import com.stocktracker.servicelayer.entity.StockSectorDE;
import com.stocktracker.servicelayer.entity.StockSubSectorDE;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.math.BigDecimal;
import java.util.List;

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
        stockDE.setLastPrice( this.getStockPrice( stockDE.getTickerSymbol() ) );
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
        /*
         * Get the page from the database
         */
        Page<StockEntity> stockEntities = stockRepository.findAll( pageRequest );
        /*
         * Map from Entity to DomainEntity
         */
        Page<StockDE> stockDEPage = mapStockEntityPageIntoStockDEPage( pageRequest, stockEntities, withStockPrices );
        logMethodEnd( methodName, stockDEPage );
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
        /*
         * Get the page from the database
         */
        Page<StockEntity> stockEntities = stockRepository.findByCompanyNameIsLikeOrTickerSymbolIsLike(
            pageRequest, companiesLike + "%", companiesLike + "%" );
        /*
         * Map from Entity to DomainEntity
         */
        Page<StockDE> stockDEPage = mapStockEntityPageIntoStockDEPage( pageRequest, stockEntities, withStockPrices );
        logMethodEnd( methodName, stockDEPage );
        return stockDEPage;
    }

    /**
     * Gets a single stock for the {@code tickerSymbol} from the database
     * @param tickerSymbol
     * @return
     * @throws StockNotFoundException if {@code tickerSymbol} is not found in the stock table
     */
    public StockDE getStock( final String tickerSymbol )
        throws StockNotFoundException
    {
        final String methodName = "getStock";
        logMethodBegin( methodName, tickerSymbol );
        /*
         * Get the stock from the database
         */
        StockEntity stockEntity = stockRepository.findOne( tickerSymbol );
        if ( stockEntity == null )
        {
            throw new StockNotFoundException( tickerSymbol );
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
    public boolean isStockExists( final String tickerSymbol )
    {
        final String methodName = "isStockExists";
        logMethodBegin( methodName, tickerSymbol );
        /*
         * Get the stock from the database
         */
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
        StockEntity stockEntity = StockEntity.newInstance( stockDE );
        stockEntity = stockRepository.save( stockEntity );
        StockDE returnStockDE = StockDE.newInstance( stockEntity );
        logMethodEnd( methodName, returnStockDE );
        return returnStockDE;
    }

    /**
     * Delete the stock
     * @param stockDE
     */
    public void deleteStock( final StockDE stockDE )
    {
        final String methodName = "getStock";
        logMethodBegin( methodName, stockDE );
        StockEntity stockEntity = StockEntity.newInstance( stockDE );
        stockRepository.delete( stockEntity );
        logMethodEnd( methodName, stockEntity );
    }

    /**
     * Get the stock price for the {@code tickerSymbol}
     * @param tickerSymbol
     * @return -1 if there is an error, otherwise the last stock price will be returned
     */
    public BigDecimal getStockPrice( final String tickerSymbol )
    {
        final String methodName = "getStockPrice";
        logMethodBegin( methodName, tickerSymbol );
        BigDecimal price = new BigDecimal( -1 );
        try
        {
            Stock stock = YahooFinance.get( tickerSymbol );
            price = stock.getQuote().getPrice();
            //BigDecimal change = stock.getQuote().getChangeInPercent();
            //BigDecimal peg = stock.getStats().getPeg();
            //BigDecimal dividend = stock.getDividend().getAnnualYieldPercent();
            logMethodEnd( methodName, String.format( "%s %s", tickerSymbol, price ));
        }
        catch( Exception e )
        {
            logError( methodName, e );
        }
        return price;
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
        logMethodEnd( methodName, stockSectorList );
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
        logMethodEnd( methodName, stockSubSectorList );
        return stockSubSectorList;
    }
}
