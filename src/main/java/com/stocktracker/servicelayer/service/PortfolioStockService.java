package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.db.entity.PortfolioStockEntity;
import com.stocktracker.common.exceptions.PortfolioStockNotFound;
import com.stocktracker.servicelayer.entity.PortfolioStockDE;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This is the service class for portfolio stock related methods.
 *
 * Created by mike on 11/26/2016.
 */
@Service
public class PortfolioStockService extends BaseService implements MyLogger
{
    /**
     * Get the customer stock entry for the customer id and the ticker symbol
     * @param customerId
     * @param tickerSymbol
     * @return
     */
    public PortfolioStockDE getPortfolioStock( final int customerId, final int portfolioId, final String tickerSymbol )
        throws PortfolioStockNotFound
    {
        final String methodName = "getPortfolioStock";
        logMethodBegin( methodName, customerId, tickerSymbol );
        PortfolioStockEntity portfolioStockEntity = portfolioStockRepository.
            findFirstByCustomerIdAndPortfolioIdAndTickerSymbol( customerId, portfolioId, tickerSymbol );
        if ( portfolioStockEntity == null )
        {
            throw new PortfolioStockNotFound( customerId, portfolioId, tickerSymbol );
        }
        PortfolioStockDE portfolioStockDE = PortfolioStockDE.newInstance( portfolioStockEntity );
        logMethodEnd( methodName, portfolioStockDE );
        return portfolioStockDE;
    }

    /**
     * Get the stocks for a portfolio
     * @param customerId
     * @param portfolioId
     * @return
     */
    public List<PortfolioStockDE> getPortfolioStocks( final int customerId, final int portfolioId )
    {
        final String methodName = "getPortfolioStocks";
        logMethodBegin( methodName, customerId, portfolioId );
        List<PortfolioStockEntity> portfolioStockEntities = portfolioStockRepository.
            findByCustomerIdAndPortfolioIdOrderByTickerSymbol( customerId, portfolioId );
        List<PortfolioStockDE> portfolioStockDEList = listCopyPortfolioStockEntityToPortfolioStockDE.copy( portfolioStockEntities );
        logMethodEnd( methodName, String.format( "Found %d stocks", portfolioStockDEList.size() ));
        return portfolioStockDEList;
    }

    /**
     * Determines if the customer stock entry is in the database
     * @param customerId
     * @param tickerSymbol
     * @return
     */
    public boolean isStockExists( final int customerId, final int portfolioId, final String tickerSymbol )
    {
        final String methodName = "isStockExists";
        logMethodBegin( methodName, customerId, tickerSymbol );
        PortfolioStockEntity portfolioStockEntity = new PortfolioStockEntity();
        portfolioStockEntity.setCustomerId( customerId );
        portfolioStockEntity.setPortfolioId( portfolioId );
        portfolioStockEntity.setTickerSymbol( tickerSymbol );
        Example<PortfolioStockEntity> example = Example.of( portfolioStockEntity );
        boolean exists = portfolioStockRepository.exists( example );
        logMethodEnd( methodName, exists );
        return exists;
    }

    /**
     * Add a new customer stock to the database
     * @param portfolioStockDE
     * @return
     */
    public PortfolioStockDE addPortfolioStock( final PortfolioStockDE portfolioStockDE )
    {
        final String methodName = "addPortfolioStock";
        logMethodBegin( methodName, portfolioStockDE );
        PortfolioStockEntity portfolioStockEntity = PortfolioStockEntity.newInstance( portfolioStockDE );
        PortfolioStockEntity returnCustomerStockEntity = portfolioStockRepository.save( portfolioStockEntity );
        PortfolioStockDE returnPortfolioStockDE = PortfolioStockDE.newInstance( returnCustomerStockEntity );
        logMethodEnd( methodName, returnPortfolioStockDE );
        return returnPortfolioStockDE;
    }
}
