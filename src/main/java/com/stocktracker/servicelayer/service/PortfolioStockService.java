package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.PortfolioStockNotFound;
import com.stocktracker.repositorylayer.entity.PortfolioStockEntity;
import com.stocktracker.repositorylayer.repository.PortfolioStockRepository;
import com.stocktracker.weblayer.dto.PortfolioLastStockDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This is the service class for portfolio stock related methods.
 *
 * Created by mike on 11/26/2016.
 */
@Service
@Transactional
public class PortfolioStockService extends BaseService<PortfolioStockEntity, PortfolioLastStockDTO> implements MyLogger
{
    private StockService stockService;
    private PortfolioStockRepository portfolioStockRepository;

    @Autowired
    public void setPortfolioStockRepository( final PortfolioStockRepository portfolioStockRepository )
    {
        this.portfolioStockRepository = portfolioStockRepository;
    }

    @Autowired
    public void setStockService( final StockService stockService )
    {
        this.stockService = stockService;
    }
    /**
     * Get the customer stock entry for the customer id and the ticker symbol
     * @param customerId
     * @param tickerSymbol
     * @return
     */
    public PortfolioLastStockDTO getPortfolioStock( final int customerId, final int portfolioId, final String tickerSymbol )
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
        PortfolioLastStockDTO portfolioStockDTO = this.entityToDTO( portfolioStockEntity );
        this.setStockInformation( portfolioStockDTO );
        logMethodEnd( methodName, portfolioStockDTO );
        return portfolioStockDTO;
    }

    /**
     * Get the stocks for a portfolio
     * @param customerId
     * @param portfolioId
     * @return
     */
    public List<PortfolioLastStockDTO> getPortfolioStocks( final int customerId, final int portfolioId )
    {
        final String methodName = "getPortfolioStocks";
        logMethodBegin( methodName, customerId, portfolioId );
        Assert.isTrue( customerId > 0, "customerId must be > 0" );
        Assert.isTrue( portfolioId > 0, "portfolioId must be > 0" );
        List<PortfolioStockEntity> portfolioStockEntities = portfolioStockRepository.
            findByCustomerIdAndPortfolioIdOrderByTickerSymbol( customerId, portfolioId );
        List<PortfolioLastStockDTO> portfolioStockDTOList = this.entitiesToDTOs( portfolioStockEntities );
        this.setStockInformation( portfolioStockDTOList );
        logMethodEnd( methodName, String.format( "Found %d stocks", portfolioStockDTOList.size() ));
        return portfolioStockDTOList;
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
        PortfolioStockEntity portfolioStockEntity = PortfolioStockEntity.newInstance();
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
    public PortfolioLastStockDTO addPortfolioStock( final PortfolioLastStockDTO portfolioStockDE )
    {
        final String methodName = "addPortfolioStock";
        logMethodBegin( methodName, portfolioStockDE );
        Objects.requireNonNull( portfolioStockDE, "portfolioStockDE cannot be null" );
        PortfolioStockEntity portfolioStockEntity = this.createPortfolioStockEntity( portfolioStockDE );
        logDebug( methodName, "inserting: {0}", portfolioStockEntity );
        PortfolioStockEntity returnCustomerStockEntity = this.portfolioStockRepository.save( portfolioStockEntity );
        PortfolioLastStockDTO returnPortfolioStockDTO = this.entityToDTO( returnCustomerStockEntity );
        this.setStockInformation( returnPortfolioStockDTO );
        logMethodEnd( methodName, returnPortfolioStockDTO );
        return returnPortfolioStockDTO;
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
    public void deletePortfolioStock( final PortfolioLastStockDTO portfolioStockDE )
    {
        final String methodName = "deletePortfolioStock";
        logMethodBegin( methodName, portfolioStockDE );
        Objects.requireNonNull( portfolioStockDE, "portfolioStockDE cannot be null" );
        PortfolioStockEntity portfolioStockEntity = createPortfolioStockEntity( portfolioStockDE );
        this.portfolioStockRepository.delete( portfolioStockEntity );
        logMethodBegin( methodName );
    }

    /**
     * Creates a new {@code CustomerStockEntity} instance from {@code PortfolioLastStockDTO} instance
     * @param portfolioStockDTO
     * @return
     */
    public PortfolioStockEntity createPortfolioStockEntity( final PortfolioLastStockDTO portfolioStockDTO )
    {
        final String methodName = "createPortfolioStockEntity";
        logMethodBegin( methodName, portfolioStockDTO );
        Objects.requireNonNull( portfolioStockDTO );
        PortfolioStockEntity portfolioStockEntity = PortfolioStockEntity.newInstance();
        portfolioStockEntity.setPortfolioId( portfolioStockDTO.getPortfolioId() );
        BeanUtils.copyProperties( portfolioStockDTO, portfolioStockEntity );
        this.setStockInformation( portfolioStockDTO );
        logMethodEnd( methodName, portfolioStockEntity );
        return portfolioStockEntity;
    }

    /**
     * Get all of the stocks for a portfolio
     * @param portfolioId
     * @return
     */
    public List<PortfolioLastStockDTO> getPortfolioStocks( final int portfolioId )
    {
        final String methodName = "getPortfolioStocks";
        logMethodBegin( methodName, portfolioId );
        Assert.isTrue( portfolioId > 0, "Portfolio ID must be > 0" );
        List<PortfolioStockEntity> stocks = portfolioStockRepository.findByPortfolioIdOrderByTickerSymbol( portfolioId );
        List<PortfolioLastStockDTO> portfolioStockDTOs = new ArrayList<>();
        if ( stocks != null )
        {
            portfolioStockDTOs = entitiesToDTOs( stocks );
            this.setStockInformation( portfolioStockDTOs );
        }
        logMethodEnd( methodName, portfolioStockDTOs );
        return portfolioStockDTOs;
    }

    /**
     * Sets the company name, last price, and last price change
     * @param portfolioStockDTOList
     */
    private void setStockInformation( final List<PortfolioLastStockDTO> portfolioStockDTOList )
    {
        final String methodName = "setStockInformation";
        portfolioStockDTOList.forEach( portfolioStockDTO ->
        {
            setStockInformation( portfolioStockDTO );
        } );
    }

    /**
     * Sets the company name, last price, and last price change
     * @param portfolioStockDTO
     */
    private void setStockInformation( final PortfolioLastStockDTO portfolioStockDTO )
    {
        final String methodName = "setStockInformation";
        logDebug( methodName, "portfolioStockDTO: {0}", portfolioStockDTO );
        this.stockService.setStockInformation( portfolioStockDTO );
    }

    @Override
    protected PortfolioLastStockDTO entityToDTO( final PortfolioStockEntity entity )
    {
        PortfolioLastStockDTO dto = PortfolioLastStockDTO.newInstance();
        BeanUtils.copyProperties( entity, dto );
        return dto;
    }

    @Override
    protected PortfolioStockEntity dtoToEntity( final PortfolioLastStockDTO dto )
    {
        PortfolioStockEntity entity = PortfolioStockEntity.newInstance();
        BeanUtils.copyProperties( entity, dto );
        return entity;
    }
}
