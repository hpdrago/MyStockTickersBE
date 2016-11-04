package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.db.entity.PortfolioEntity;
import com.stocktracker.repositorylayer.db.entity.VPortfolioStockEntity;
import com.stocktracker.repositorylayer.exceptions.PortfolioNotFoundException;
import com.stocktracker.servicelayer.entity.CustomerStockDomainEntity;
import com.stocktracker.servicelayer.entity.PortfolioDomainEntity;
import com.stocktracker.weblayer.dto.CustomerStockDTO;
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
    public PortfolioDTO getPortfolioById( final int id )
    {
        final String methodName = "getPortfolioById";
        logMethodBegin( methodName, id );
        PortfolioEntity portfolioEntity = portfolioRepository.findOne( id );
        if ( portfolioEntity == null )
        {
            throw new PortfolioNotFoundException( id );
        }
        PortfolioDomainEntity portfolioDomainEntity = PortfolioDomainEntity.newInstance( portfolioEntity );
        PortfolioDTO portfolioDTO = PortfolioDTO.newInstance( portfolioDomainEntity );
        logMethodEnd( methodName, portfolioDTO );
        return portfolioDTO;
    }

    /**
     * Get all of the portfolios for the customer
     * @param customerId Customer id
     * @return List of Portfolio DTO's for the customer
     * @throws PortfolioNotFoundException
     */
    public List<PortfolioDTO> getPortfoliosByCustomerId( final int customerId )
        throws PortfolioNotFoundException
    {
        final String methodName = "getPortfolioByCustomerId";
        logMethodBegin( methodName, customerId );
        List<PortfolioEntity> portfolioEntities = portfolioRepository.findByCustomerId( customerId );
        List<PortfolioDTO> portfolioDTOs = new ArrayList<>();
        if ( portfolioEntities != null )
        {
            List<PortfolioDomainEntity> portfolioDomainEntities = listCopyPortfolioEntityToPortfolioDomainEntity.copy( portfolioEntities );
            portfolioDTOs = listCopyPortfolioDomainEntityToPortfolioDTO.copy( portfolioDomainEntities );
        }
        logMethodEnd( methodName, portfolioDTOs );
        return portfolioDTOs;
    }

    /**
     * Get all of the stocks for a portfolio
     * @param portfolioId
     * @return
     */
    public List<CustomerStockDTO> getPortfolioStocks( final int portfolioId )
    {
        final String methodName = "getPortfolioByCustomerId";
        logMethodBegin( methodName, portfolioId );
        List<CustomerStockDTO> customerStockDTOs = new ArrayList<>();
        List<VPortfolioStockEntity> stocks = vPortfolioStockRepository.findByPortfolioIdOrderByTickerSymbol( portfolioId );
        if ( stocks != null )
        {
            List<CustomerStockDomainEntity> customerStockDomainEntities = listCopyVPortfolioStockEntityToCustomerStockDomainEntity.copy( stocks );
            customerStockDTOs = listCopyCustomerStockDomainEntityToCustomerStockDTO.copy( customerStockDomainEntities );
        }
        logMethodEnd( methodName, customerStockDTOs );
        return customerStockDTOs;
    }

    /**
     * Add a new portfolio for the customer
     * @param customerId
     * @param portfolioDTO
     * @return PortfolioEntity that was inserted
     */
    public PortfolioEntity addPortfolio( final int customerId, final PortfolioDTO portfolioDTO )
    {
        final String methodName = "addPortfolio";
        logMethodBegin( methodName, customerId, portfolioDTO );
        PortfolioEntity portfolioEntity = PortfolioEntity.newInstance( portfolioDTO );
        portfolioEntity = portfolioRepository.save( portfolioEntity );
        logMethodEnd( methodName, portfolioEntity );
        return portfolioEntity;
    }
}
