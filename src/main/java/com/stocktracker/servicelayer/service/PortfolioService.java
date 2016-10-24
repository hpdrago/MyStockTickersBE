package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.PortfolioRepository;
import com.stocktracker.repositorylayer.db.entity.PortfolioEntity;
import com.stocktracker.repositorylayer.exceptions.PortfolioNotFoundException;
import com.stocktracker.servicelayer.entity.PortfolioDomainEntity;
import com.stocktracker.servicelayer.service.listcopy.ListCopyPortfolioDomainEntityToPortfolioDTO;
import com.stocktracker.servicelayer.service.listcopy.ListCopyPortfolioEntityToPortfolioDomainEntity;
import com.stocktracker.weblayer.dto.PortfolioDTO;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PortfolioService implements MyLogger
{
    private PortfolioRepository portfolioRepository;
    private ListCopyPortfolioEntityToPortfolioDomainEntity listCopyPortfolioEntityToPortfolioDomainEntity;
    private ListCopyPortfolioDomainEntityToPortfolioDTO listCopyPortfolioDomainEntityToPortfolioDTO;

    /**
     * Dependency injection of the PortfolioRepository
     * @param portfolioRepository
     */
    @Autowired
    public void setPortfolioRepository( final PortfolioRepository portfolioRepository )
    {
        final String methodName = "setPortfolioRepository";
        logDebug( methodName, "Dependency Injection of: " + portfolioRepository );
        this.portfolioRepository = portfolioRepository;
    }

    /**
     * Dependency injection of the ListCopyPortfolioEntityToPortfolioDo
     * @param listCopyPortfolioEntityToPortfolioDomainEntity
     */
    @Autowired
    public void setListCopyPortfolioEntityToPortfolioDomainEntity( final ListCopyPortfolioEntityToPortfolioDomainEntity listCopyPortfolioEntityToPortfolioDomainEntity )
    {
        final String methodName = "setListCopyPortfolioEntityToPortfolioDo";
        logDebug( methodName, "Dependency Injection of: " + listCopyPortfolioEntityToPortfolioDomainEntity );
        this.listCopyPortfolioEntityToPortfolioDomainEntity = listCopyPortfolioEntityToPortfolioDomainEntity;
    }

    /**
     * Dependency injection of the ListCopyPortfolioDoToPortfolioDo
     * @param
     */
    @Autowired
    public void setListCopyPortfolioDoToPortfolioDo(
        final ListCopyPortfolioDomainEntityToPortfolioDTO listCopyPortfolioDomainEntityToPortfolioDTO )
    {
        final String methodName = "setListCopyPortfolioDoToPortfolioDo";
        logDebug( methodName, "Dependency Injection of: " + listCopyPortfolioDomainEntityToPortfolioDTO );
        this.listCopyPortfolioDomainEntityToPortfolioDTO = listCopyPortfolioDomainEntityToPortfolioDTO;
    }

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

}
