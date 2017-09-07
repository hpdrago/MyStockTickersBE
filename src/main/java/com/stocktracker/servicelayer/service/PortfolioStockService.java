package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.entity.PortfolioStockEntity;
import com.stocktracker.common.exceptions.PortfolioStockNotFound;
import com.stocktracker.servicelayer.entity.PortfolioStockDE;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

/**
 * This is the service class for portfolio stock related methods.
 *
 * Created by mike on 11/26/2016.
 */
@Service
@Transactional
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
        Assert.isTrue( customerId > 0, "customerId must be > 0" );
        Assert.isTrue( portfolioId > 0, "portfolioId must be > 0" );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
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
        Assert.isTrue( customerId > 0, "customerId must be > 0" );
        Assert.isTrue( portfolioId > 0, "portfolioId must be > 0" );
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
        final String methodName = "isStockExistsInDatabase";
        logMethodBegin( methodName, customerId, tickerSymbol );
        Assert.isTrue( customerId > 0, "customerId must be > 0" );
        Assert.isTrue( portfolioId > 0, "portfolioId must be > 0" );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
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
        Objects.requireNonNull( portfolioStockDE, "portfolioStockDE cannot be null" );
        PortfolioStockEntity portfolioStockEntity = PortfolioStockEntity.newInstance( portfolioStockDE );
        PortfolioStockEntity returnCustomerStockEntity = portfolioStockRepository.save( portfolioStockEntity );
        PortfolioStockDE returnPortfolioStockDE = PortfolioStockDE.newInstance( returnCustomerStockEntity );
        logMethodEnd( methodName, returnPortfolioStockDE );
        return returnPortfolioStockDE;
    }

    /**
     * Delete the portfolio stock.
     * @param portfolioStockId The {@code portfolioId} is the primary key -- generated int
     */
    public void deletePortfolioStock( final int portfolioStockId )
    {
        final String methodName = "deletePortfolioStock";
        logMethodBegin( methodName, portfolioStockId );
        Assert.isTrue( portfolioStockId > 0, "portfolioStockId must be > 0" );
        portfolioStockRepository.delete( portfolioStockId );
        logMethodBegin( methodName );
    }

    /**
     * Delete a portfolio stock as defined by the {@code portfolioStockDE}
     * @param portfolioStockDE
     */
    public void deletePortfolioStock( final PortfolioStockDE portfolioStockDE )
    {
        final String methodName = "deletePortfolioStock";
        logMethodBegin( methodName, portfolioStockDE );
        Objects.requireNonNull( portfolioStockDE, "portfolioStockDE cannot be null" );
        PortfolioStockEntity portfolioStockEntity = PortfolioStockEntity.newInstance( portfolioStockDE );
        this.portfolioStockRepository.delete( portfolioStockEntity );
        logMethodBegin( methodName );
    }
}
