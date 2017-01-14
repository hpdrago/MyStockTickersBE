package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.db.entity.PortfolioEntity;
import com.stocktracker.repositorylayer.db.entity.VPortfolioStockEntity;
import com.stocktracker.common.exceptions.PortfolioNotFoundException;
import com.stocktracker.servicelayer.entity.PortfolioStockDE;
import com.stocktracker.servicelayer.entity.PortfolioDE;
import com.stocktracker.weblayer.dto.PortfolioDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
     * @param portfolioId
     * @return
     */
    public PortfolioDE getPortfolioById( final int portfolioId )
    {
        final String methodName = "getPortfolioById";
        logMethodBegin( methodName, portfolioId );
        Assert.isTrue( portfolioId > 0, "Portfolio ID must be > 0" );
        PortfolioEntity portfolioEntity = portfolioRepository.findOne( portfolioId );
        if ( portfolioEntity == null )
        {
            throw new PortfolioNotFoundException( portfolioId );
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
        Assert.isTrue( customerId > 0, "Customer ID must be > 0" );
        List<PortfolioEntity> portfolioEntities = portfolioRepository.findByCustomerId( customerId );
        List<PortfolioDE> portfolioDomainEntities = new ArrayList<>();
        if ( portfolioEntities != null )
        {
            portfolioDomainEntities = listCopyPortfolioEntityToPortfolioDE.copy( portfolioEntities );
        }
        logMethodEnd( methodName, portfolioDomainEntities );
        return portfolioDomainEntities;
    }

    /**
     * Get all of the stocks for a portfolio
     * @param portfolioId
     * @return
     */
    public List<PortfolioStockDE> getPortfolioStocks( final int portfolioId )
    {
        final String methodName = "getPortfolioStocks";
        logMethodBegin( methodName, portfolioId );
        Assert.isTrue( portfolioId > 0, "Portfolio ID must be > 0" );
        List<VPortfolioStockEntity> stocks = vPortfolioStockRepository.findByPortfolioIdOrderByTickerSymbol( portfolioId );
        List<PortfolioStockDE> portfolioStockDES = new ArrayList<>();
        if ( stocks != null )
        {
            portfolioStockDES = listCopyVPortfolioStockEntityToCustomerStockDE.copy( stocks );
        }
        logMethodEnd( methodName, portfolioStockDES );
        return portfolioStockDES;
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
        Objects.requireNonNull( portfolioDTO, "portfolioDTO cannot be null" );
        Assert.isTrue( customerId > 0, "Customer ID must be > 0" );
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
        Assert.isTrue( portfolioId > 0, "Portfolio ID must be > 0" );
        PortfolioEntity portfolioEntity = portfolioRepository.getOne( portfolioId );
        portfolioRepository.delete( portfolioId );
        PortfolioDE portfolioDE = PortfolioDE.newInstance( portfolioEntity );
        logMethodEnd( methodName, portfolioDE );
        return portfolioDE;
    }
}
