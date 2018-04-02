package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.PortfolioStockNotFound;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockQuoteUnavailableException;
import com.stocktracker.repositorylayer.entity.PortfolioStockEntity;
import com.stocktracker.repositorylayer.repository.PortfolioStockRepository;
import com.stocktracker.servicelayer.service.stocks.StockInformationService;
import com.stocktracker.servicelayer.stockinformationprovider.StockPriceFetchMode;
import com.stocktracker.weblayer.dto.PortfolioStockDTO;
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
public class PortfolioStockEntityService extends VersionedEntityService<Integer,
                                                                        PortfolioStockEntity,
                                                                        PortfolioStockDTO,
                                                                        PortfolioStockRepository>
    implements MyLogger
{
    private StockInformationService stockInformationService;
    private PortfolioStockRepository portfolioStockRepository;

    /**
     * Get the customer stock entry for the customer id and the ticker symbol
     * @param customerId
     * @param portfolioId
     * @param tickerSymbol
     * @return
     * @throws PortfolioStockNotFound
     * @throws StockNotFoundException
     * @throws StockQuoteUnavailableException
     * @throws EntityVersionMismatchException
     */
    public PortfolioStockDTO getPortfolioStock( final int customerId, final int portfolioId, final String tickerSymbol )
        throws PortfolioStockNotFound,
               StockNotFoundException,
               StockQuoteUnavailableException
    {
        final String methodName = "getPortfolioStock";
        logMethodBegin( methodName, customerId, portfolioId, tickerSymbol );
        Assert.isTrue( customerId > 0, "customerId must be > 0" );
        Assert.isTrue( portfolioId > 0, "portfolioId must be > 0" );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        PortfolioStockEntity portfolioStockEntity = portfolioStockRepository.
            findFirstByCustomerIdAndPortfolioIdAndTickerSymbol( customerId, portfolioId, tickerSymbol );
        if ( portfolioStockEntity == null )
        {
            throw new PortfolioStockNotFound( customerId, portfolioId, tickerSymbol );
        }
        PortfolioStockDTO portfolioStockDTO = this.entityToDTO( portfolioStockEntity );
        this.stockInformationService
            .setStockPrice( portfolioStockDTO, StockPriceFetchMode.ASYNCHRONOUS );
        logMethodEnd( methodName, portfolioStockDTO );
        return portfolioStockDTO;
    }

    /**
     * Get all of the stocks for a portfolio
     * @param portfolioId
     * @return
     * @throws StockNotFoundException
     * @throws StockQuoteUnavailableException
     */
    public List<PortfolioStockDTO> getPortfolioStocks( final int portfolioId )
        throws StockNotFoundException,
               StockQuoteUnavailableException
    {
        final String methodName = "getPortfolioStocks";
        logMethodBegin( methodName, portfolioId );
        List<PortfolioStockEntity> stocks = portfolioStockRepository.findByPortfolioIdOrderByTickerSymbol( portfolioId );
        List<PortfolioStockDTO> portfolioStockDTOs = new ArrayList<>();
        if ( stocks != null )
        {
            portfolioStockDTOs = entitiesToDTOs( stocks );
            this.setStockInformation( portfolioStockDTOs );
        }
        logMethodEnd( methodName, portfolioStockDTOs );
        return portfolioStockDTOs;
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
     * @throws StockNotFoundException
     * @throws StockQuoteUnavailableException
     * @throws EntityVersionMismatchException
     */
    public PortfolioStockDTO addPortfolioStock( final PortfolioStockDTO portfolioStockDE )
        throws StockNotFoundException,
               StockQuoteUnavailableException, EntityVersionMismatchException
    {
        final String methodName = "addPortfolioStock";
        logMethodBegin( methodName, portfolioStockDE );
        Objects.requireNonNull( portfolioStockDE, "portfolioStockDE cannot be null" );
        final PortfolioStockEntity portfolioStockEntity = this.createPortfolioStockEntity( portfolioStockDE );
        logDebug( methodName, "inserting: {0}", portfolioStockEntity );
        final PortfolioStockEntity returnCustomerStockEntity = this.addEntity( portfolioStockEntity );
        final PortfolioStockDTO returnPortfolioStockDTO = this.entityToDTO( returnCustomerStockEntity );
        this.stockInformationService.setStockPrice( returnPortfolioStockDTO, StockPriceFetchMode.ASYNCHRONOUS );
        logMethodEnd( methodName, returnPortfolioStockDTO );
        return returnPortfolioStockDTO;
    }

    /**
     * Creates a new {@code CustomerStockEntity} instance from {@code PortfolioLastStockDTO} instance
     * @param portfolioStockDTO
     * @return
     * @throws StockNotFoundException
     * @throws StockQuoteUnavailableException
     */
    public PortfolioStockEntity createPortfolioStockEntity( final PortfolioStockDTO portfolioStockDTO )
        throws StockNotFoundException,
               StockQuoteUnavailableException
    {
        final String methodName = "createPortfolioStockEntity";
        logMethodBegin( methodName, portfolioStockDTO );
        Objects.requireNonNull( portfolioStockDTO );
        PortfolioStockEntity portfolioStockEntity = PortfolioStockEntity.newInstance();
        portfolioStockEntity.setPortfolioId( portfolioStockDTO.getPortfolioId() );
        BeanUtils.copyProperties( portfolioStockDTO, portfolioStockEntity );
        this.stockInformationService.setStockPrice( portfolioStockDTO, StockPriceFetchMode.ASYNCHRONOUS );
        logMethodEnd( methodName, portfolioStockEntity );
        return portfolioStockEntity;
    }

    /**
     * Sets the company name, last price, and last price change
     * @param portfolioStockDTOList
     * @throws StockNotFoundException
     * @throws StockQuoteUnavailableException
     * @throws EntityVersionMismatchException
     */
    private void setStockInformation( final List<PortfolioStockDTO> portfolioStockDTOList )
        throws StockNotFoundException,
               StockQuoteUnavailableException
    {
        final String methodName = "setStockInformation";
        for ( PortfolioStockDTO portfolioStockDTO : portfolioStockDTOList )
        {
            this.stockInformationService.setStockPrice( portfolioStockDTO, StockPriceFetchMode.ASYNCHRONOUS );
        }
    }

    @Override
    protected PortfolioStockDTO createDTO()
    {
        return this.context.getBean( PortfolioStockDTO.class );
    }

    @Override
    protected PortfolioStockEntity createEntity()
    {
        return this.context.getBean( PortfolioStockEntity.class );
    }

    @Override
    protected PortfolioStockRepository getRepository()
    {
        return this.portfolioStockRepository;
    }

    @Autowired
    public void setStockInformationService( final StockInformationService stockInformationService )
    {
        this.stockInformationService = stockInformationService;
    }

    @Autowired
    public void setPortfolioStockRepository( final PortfolioStockRepository portfolioStockRepository )
    {
        this.portfolioStockRepository = portfolioStockRepository;
    }
}
