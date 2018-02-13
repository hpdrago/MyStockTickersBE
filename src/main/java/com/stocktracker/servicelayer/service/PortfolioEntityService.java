package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.PortfolioNotFoundException;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockQuoteUnavailableException;
import com.stocktracker.repositorylayer.entity.PortfolioEntity;
import com.stocktracker.repositorylayer.repository.PortfolioRepository;
import com.stocktracker.weblayer.dto.PortfolioDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional
public class PortfolioEntityService extends DMLEntityService<Integer,
                                                             PortfolioEntity,
                                                             PortfolioDTO,
                                                             PortfolioRepository>
    implements MyLogger
{
    private PortfolioRepository portfolioRepository;
    private StockEntityService stockService;
    private PortfolioStockEntityService portfolioStockService;
    private PortfolioCalculator portfolioCalculator;

    /**
     * Get the portfolio by id request
     * @param portfolioId
     * @return
     * @throws StockNotFoundException
     * @throws StockQuoteUnavailableException
     * @throws EntityVersionMismatchException
     */
    public PortfolioDTO getPortfolioById( final int portfolioId )
        throws StockNotFoundException,
               StockQuoteUnavailableException,
               EntityVersionMismatchException
    {
        final String methodName = "getPortfolioById";
        logMethodBegin( methodName, portfolioId );
        Assert.isTrue( portfolioId > 0, "Portfolio ID must be > 0" );
        PortfolioEntity portfolioEntity = this.portfolioRepository.findOne( portfolioId );
        if ( portfolioEntity == null )
        {
            throw new PortfolioNotFoundException( portfolioId );
        }
        PortfolioDTO portfolioDTO = this.entityToDTO( portfolioEntity );
        this.portfolioCalculator
            .calculate( portfolioDTO );
        logMethodEnd( methodName, portfolioDTO );
        return portfolioDTO;
    }

    /**
     * Get all of the portfolios for the customer
     * @param customerId
     * @return
     * @throws PortfolioNotFoundException
     * @throws StockNotFoundException
     * @throws StockQuoteUnavailableException
     * @throws EntityVersionMismatchException
     */
    public List<PortfolioDTO> getPortfoliosByCustomerId( final int customerId )
        throws PortfolioNotFoundException,
               StockNotFoundException,
               StockQuoteUnavailableException,
               EntityVersionMismatchException
    {
        final String methodName = "getPortfoliosByCustomerId";
        logMethodBegin( methodName, customerId );
        Assert.isTrue( customerId > 0, "Customer ID must be > 0" );
        List<PortfolioEntity> portfolioEntities = this.portfolioRepository.findByCustomerId( customerId );
        List<PortfolioDTO> portfolioDTOs = new ArrayList<>();
        if ( portfolioEntities != null )
        {
            portfolioDTOs = this.entitiesToDTOs( portfolioEntities );
        }
        this.portfolioCalculator.calculate( portfolioDTOs );
        logMethodEnd( methodName, portfolioDTOs );
        return portfolioDTOs;
    }

    /**
     * Add a new portfolio for the customer
     * @param customerId
     * @param portfolioDTO
     * @return PortfolioEntity that was inserted
     */
    public PortfolioDTO addPortfolio( final int customerId, final PortfolioDTO portfolioDTO )
    {
        final String methodName = "addPortfolio";
        logMethodBegin( methodName, customerId, portfolioDTO );
        Objects.requireNonNull( portfolioDTO, "portfolioDTO cannot be null" );
        Assert.isTrue( customerId > 0, "Customer ID must be > 0" );
        PortfolioEntity portfolioEntity = this.dtoToEntity( portfolioDTO );
        portfolioEntity = this.portfolioRepository.save( portfolioEntity );
        PortfolioDTO returnPortfolioDTO = this.entityToDTO( portfolioEntity );
        logMethodEnd( methodName, returnPortfolioDTO );
        return returnPortfolioDTO;
    }

    /**
     * Delete the portfolio from the database
     * @param portfolioId The portfolio id
     * @return The PortfolioDTO for the deleted portfolio
     */
    public PortfolioDTO deletePortfolio( final int portfolioId )
    {
        final String methodName = "deletePortfolio";
        logMethodBegin( methodName, portfolioId );
        Assert.isTrue( portfolioId > 0, "Portfolio ID must be > 0" );
        PortfolioEntity portfolioEntity = this.portfolioRepository.getOne( portfolioId );
        this.portfolioRepository.delete( portfolioId );
        PortfolioDTO portfolioDTO = this.entityToDTO( portfolioEntity );
        logMethodEnd( methodName, portfolioDTO );
        return portfolioDTO;
    }

    @Override
    protected PortfolioDTO entityToDTO( final PortfolioEntity entity )
    {
        Objects.requireNonNull( entity );
        PortfolioDTO portfolioDTO = PortfolioDTO.newInstance();
        BeanUtils.copyProperties( entity, portfolioDTO );
        return portfolioDTO;
    }

    @Override
    protected PortfolioEntity dtoToEntity( final PortfolioDTO dto )
    {
        Objects.requireNonNull( dto );
        PortfolioEntity portfolioEntity = PortfolioEntity.newInstance();
        BeanUtils.copyProperties( dto, portfolioEntity );
        return portfolioEntity;
    }

    @Override
    protected PortfolioRepository getRepository()
    {
        return this.portfolioRepository;
    }

    @Autowired
    public void setPortfolioCalculator( final PortfolioCalculator portfolioCalculator )
    {
        this.portfolioCalculator = portfolioCalculator;
    }

    @Autowired
    public void setPortfolioRepository( final PortfolioRepository portfolioRepository )
    {
        this.portfolioRepository = portfolioRepository;
    }

    @Autowired
    public void setStockService( StockEntityService stockService )
    {
        this.stockService = stockService;
    }

    @Autowired
    public void setPortfolioStockService( PortfolioStockEntityService portfolioStockService )
    {
        this.portfolioStockService = portfolioStockService;
    }

}
