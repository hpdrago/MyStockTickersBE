package com.stocktracker.servicelayer.service;

import com.stocktracker.common.UUIDUtil;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.PortfolioNotFoundException;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockQuoteUnavailableException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.entity.PortfolioEntity;
import com.stocktracker.repositorylayer.repository.PortfolioRepository;
import com.stocktracker.servicelayer.service.common.PortfolioCalculator;
import com.stocktracker.weblayer.dto.PortfolioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * This service communicates between the Web layer and Repositories using the Domain Model
 * It is a stateless instance that provides business level services
 *
 * Created by mike on 10/23/2016.
 */
@Service
public class PortfolioEntityService extends UuidEntityService<PortfolioEntity,
                                                              PortfolioDTO,
                                                              PortfolioRepository>
{
    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    private PortfolioCalculator portfolioCalculator;

    /**
     * Get the portfolio by id request
     * @param portfolioUuid
     * @return
     * @throws StockNotFoundException
     * @throws StockQuoteUnavailableException
     * @throws EntityVersionMismatchException
     */
    public PortfolioDTO getPortfolioByUuid( final UUID portfolioUuid )
        throws StockNotFoundException,
               StockQuoteUnavailableException,
               EntityVersionMismatchException
    {
        final String methodName = "getPortfolioByUuid";
        logMethodBegin( methodName, portfolioUuid );
        Objects.requireNonNull( portfolioUuid, "portfolioUuid cannot be null" );
        PortfolioEntity portfolioEntity = null;
        try
        {
            portfolioEntity = this.getEntity( portfolioUuid );
        }
        catch( VersionedEntityNotFoundException e )
        {
            throw new PortfolioNotFoundException( portfolioUuid );
        }
        PortfolioDTO portfolioDTO = this.entityToDTO( portfolioEntity );
        this.portfolioCalculator
            .calculate( portfolioDTO );
        logMethodEnd( methodName, portfolioDTO );
        return portfolioDTO;
    }

    /**
     * Get all of the portfolios for the customer
     * @param customerUuid
     * @return
     * @throws PortfolioNotFoundException
     * @throws StockNotFoundException
     * @throws StockQuoteUnavailableException
     * @throws EntityVersionMismatchException
     */
    public List<PortfolioDTO> getPortfoliosByCustomerUuid( final UUID customerUuid )
        throws PortfolioNotFoundException,
               StockNotFoundException,
               StockQuoteUnavailableException,
               EntityVersionMismatchException
    {
        final String methodName = "getPortfoliosByCustomerUuid";
        logMethodBegin( methodName, customerUuid );
        Objects.requireNonNull( customerUuid, "customerUuid cannot be null" );
        List<PortfolioEntity> portfolioEntities = this.portfolioRepository.findByCustomerUuid( customerUuid );
        List<PortfolioDTO> portfolioDTOs = new ArrayList<>();
        if ( portfolioEntities != null )
        {
            portfolioDTOs = this.entitiesToDTOs( portfolioEntities );
        }
        this.portfolioCalculator.calculate( portfolioDTOs );
        logMethodEnd( methodName, portfolioDTOs );
        return portfolioDTOs;
    }

    @Override
    protected PortfolioEntity dtoToEntity( final PortfolioDTO dto )
    {
        final PortfolioEntity portfolioEntity = super.dtoToEntity( dto );
        portfolioEntity.setCustomerUuid( UUIDUtil.uuid( dto.getCustomerId() ) );
        portfolioEntity.setLinkedAccountUuid( UUIDUtil.uuid( dto.getLinkedAccountId() ) );
        return portfolioEntity;
    }

    @Override
    protected PortfolioDTO createDTO()
    {
        return this.context.getBean( PortfolioDTO.class );
    }

    @Override
    protected PortfolioEntity createEntity()
    {
        return this.context.getBean( PortfolioEntity.class );
    }

    @Override
    protected PortfolioRepository getRepository()
    {
        return this.portfolioRepository;
    }
}