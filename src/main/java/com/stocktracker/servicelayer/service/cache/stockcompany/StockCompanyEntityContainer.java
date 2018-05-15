package com.stocktracker.servicelayer.service.cache.stockcompany;

import com.stocktracker.repositorylayer.entity.StockCompanyEntity;
import com.stocktracker.servicelayer.service.cache.common.AsyncCacheDataReceiver;
import com.stocktracker.servicelayer.service.stocks.TickerSymbolContainer;

/**
 * This interface defines the methods for classes that will get Stock Quote information from the StockQuoteEntityCache
 */
public interface StockCompanyEntityContainer extends TickerSymbolContainer,
                                                     AsyncCacheDataReceiver<String,StockCompanyEntity>
{

    /**
     * This method is called to set the {@code StockCompanyEntity}.  Classes implementing this interface can then
     * extract any or all of the Stock Company (IEXTrading Company) properties as needed.
     * @param stockCompanyEntity
     */
    //void setStockCompanyEntity( final StockCompanyEntity stockCompanyEntity );

    /**
     * Identifies the state of the {@code StockCompanyEntity} received from the stock company cache.
     * @param cacheEntryState
     */
    //void setStockCompanyCacheEntryState( final AsyncCacheEntryState cacheEntryState );

    /**
     * This method is called to pass the error encountered while try to retrieve stock company from IEXTrading.
     * @param error
     */
    //void setStockCompanyCacheError( final String error );
}
