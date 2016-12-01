package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.db.entity.PortfolioEntity;
import com.stocktracker.repositorylayer.db.entity.VPortfolioStockEntity;
import com.stocktracker.repositorylayer.exceptions.PortfolioNotFoundException;
import com.stocktracker.servicelayer.entity.CustomerStockDE;
import com.stocktracker.servicelayer.entity.PortfolioDE;
import com.stocktracker.weblayer.dto.PortfolioDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * This service communicates between the Web layer and Repositories using the Domain Model
 * It is a stateless instance that provides business level services
 *
 * Created by mike on 10/23/2016.
 */
@Service
public class PortfolioService extends BaseService implements MyLogger
{
    /**
     * Get the portfolio by id request
     * @param id
     * @return
     */
    public PortfolioDE getPortfolioById( final int id )
    {
        final String methodName = "getPortfolioById";
        logMethodBegin( methodName, id );
        PortfolioEntity portfolioEntity = portfolioRepository.findOne( id );
        if ( portfolioEntity == null )
        {
            throw new PortfolioNotFoundException( id );
        }
        PortfolioDE portfolioDE = PortfolioDE.newInstance( portfolioEntity );
        logMethodEnd( methodName, portfolioDE );
        return portfolioDE;
    }

    /**
     * Get all of the portfolios for the customer
     * @param customerId Customer id
     * @return List of Portfolio DTO's for the customer
     * @throws PortfolioNotFoundException
     */
    public List<PortfolioDE> getPortfoliosByCustomerId( final int customerId )
        throws PortfolioNotFoundException
    {
        final String methodName = "getPortfoliosByCustomerId";
        logMethodBegin( methodName, customerId );
        List<PortfolioEntity> portfolioEntities = portfolioRepository.findByCustomerId( customerId );
        List<PortfolioDE> portfolioDomainEntities = new ArrayList<>();
        if ( portfolioEntities != null )
        {
            portfolioDomainEntities = listCopyPortfolioEntityToPortfolioDomainEntity.copy( portfolioEntities );
        }
        logMethodEnd( methodName, portfolioDomainEntities );
        return portfolioDomainEntities;
    }

    /**
     * Get all of the stocks for a portfolio
     * @param portfolioId
     * @return
     */
    public List<CustomerStockDE> getPortfolioStocks( final int portfolioId )
    {
        final String methodName = "getPortfolioStocks";
        logMethodBegin( methodName, portfolioId );
        List<VPortfolioStockEntity> stocks = vPortfolioStockRepository.findByPortfolioIdOrderByTickerSymbol( portfolioId );
        List<CustomerStockDE> customerStockDEs = new ArrayList<>();
        if ( stocks != null )
        {
            customerStockDEs = listCopyVPortfolioStockEntityToCustomerStockDE.copy( stocks );
        }
        logMethodEnd( methodName, customerStockDEs );
        return customerStockDEs;
    }

    /**
     * Add a new portfolio for the customer
     * @param customerId
     * @param portfolioDTO
     * @return PortfolioEntity that was inserted
     */
    public PortfolioDE addPortfolio( final int customerId, final PortfolioDTO portfolioDTO )
    {
        final String methodName = "addPortfolio";
        logMethodBegin( methodName, customerId, portfolioDTO );
        PortfolioEntity portfolioEntity = PortfolioEntity.newInstance( portfolioDTO );
        portfolioEntity = portfolioRepository.save( portfolioEntity );
        PortfolioDE portfolioDE = PortfolioDE.newInstance( portfolioEntity );
        logMethodEnd( methodName, portfolioDE );
        return portfolioDE;
    }

    /**
     * Delete the portfolio from the database
     * @param portfolioId The portfolio id
     * @return The PortfolioDE for the deleted portfolio
     */
    public PortfolioDE deletePortfolio( final int portfolioId )
    {
        final String methodName = "deletePortfolio";
        logMethodBegin( methodName, portfolioId );
        PortfolioEntity portfolioEntity = portfolioRepository.getOne( portfolioId );
        portfolioRepository.delete( portfolioId );
        PortfolioDE portfolioDE = PortfolioDE.newInstance( portfolioEntity );
        logMethodEnd( methodName, portfolioDE );
        return portfolioDE;
    }
}
