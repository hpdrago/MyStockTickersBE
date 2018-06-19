package com.stocktracker.weblayer.dto.common;

import com.stocktracker.servicelayer.service.cache.common.AsyncCacheEntryState;
import com.stocktracker.servicelayer.service.stocks.TickerSymbolContainer;
import com.stocktracker.weblayer.dto.StockCompanyDTO;

/**
 * This interface defines the methods for classes that will get Stock Company information from the StockCompanyDTOCache
 */
public interface StockCompanyDTOContainer extends TickerSymbolContainer

{
    /**
     * This method is called to set the {@code StockCompanyDTO}.  Classes implementing this interface can then
     * extract any or all of the Stock Company (IEXTrading Company) properties as needed.
     * @param stockCompanyEntity
     */
    void setStockCompanyDTO( final StockCompanyDTO stockCompanyEntity );

    /**
     * This method is called to set the state of the {@code StockCompanyDTO} received from the stock company cache.
     * @param stockCompanyEntityCacheState
     */
    void setStockCompanyCacheEntryState( final AsyncCacheEntryState stockCompanyEntityCacheState );

    /**
     * This method is called to set the error encoutered while retrieving the Company from IEXTrading.
     * @param stockCompanyCacheError
     */
    void setStockCompanyCacheError( final String stockCompanyCacheError );

}
