package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.db.entity.StockEntity;
import com.stocktracker.repositorylayer.exceptions.StockNotFoundException;
import com.stocktracker.servicelayer.entity.StockDE;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
     * @return The created {@code Page<DTO>} object.
     */
    private Page<StockDE> mapStockEntityPageIntoStockDEPage( Pageable pageRequest, Page<StockEntity> source )
    {
        List<StockDE> stockDomainEntities = listCopyStockEntityToStockDE.copy( source.getContent() );
        return new PageImpl<>( stockDomainEntities, pageRequest, source.getTotalElements() );
    }

    /**
     * Get a page of StockDomainEntities's
     * @param pageRequest
     * @return
     */
    public Page<StockDE> getPage( final Pageable pageRequest )
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
        Page<StockDE> stockDEPage = mapStockEntityPageIntoStockDEPage( pageRequest, stockEntities );
        logMethodEnd( methodName, stockDEPage );
        return stockDEPage;
    }

    public Page<StockDE> getCompaniesLike( final Pageable pageRequest, final String companiesLike )
    {
        final String methodName = "getCompaniesLike";
        logMethodBegin( methodName, pageRequest, companiesLike );
        /*
         * Get the page from the database
         */
        Page<StockEntity> stockEntities = stockRepository.findByCompanyNameIsLikeOrTickerSymbolIsLike(
            pageRequest, companiesLike, companiesLike );
        /*
         * Map from Entity to DomainEntity
         */
        Page<StockDE> stockDEPage = mapStockEntityPageIntoStockDEPage( pageRequest, stockEntities );
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
}
