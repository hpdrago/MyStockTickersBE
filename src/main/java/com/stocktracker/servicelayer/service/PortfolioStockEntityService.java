package com.stocktracker.servicelayer.service;

import com.fasterxml.uuid.impl.UUIDUtil;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * This is the service class for portfolio stock related methods.
 *
 * Created by mike on 11/26/2016.
 */
@Service
//@Transactional
public class PortfolioStockEntityService extends UuidEntityService<PortfolioStockEntity,
                                                                   PortfolioStockDTO,
                                                                   PortfolioStockRepository>
{
    private StockInformationService stockInformationService;
    private PortfolioStockRepository portfolioStockRepository;

    /**
     * Get the customer stock entry for the customer id and the ticker symbol
     * @param customerUuid
     * @param portfolioUuid
     * @param tickerSymbol
     * @return
     * @throws PortfolioStockNotFound
     * @throws StockNotFoundException
     * @throws StockQuoteUnavailableException
     * @throws EntityVersionMismatchException
     */
    public PortfolioStockDTO getPortfolioStock( final UUID customerUuid, final UUID portfolioUuid, final String tickerSymbol )
        throws PortfolioStockNotFound
    {
        final String methodName = "getPortfolioStock";
        logMethodBegin( methodName, customerUuid, portfolioUuid, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Objects.requireNonNull( customerUuid, "customerUuid cannot be null" );
        Objects.requireNonNull( portfolioUuid, "portfolioUuid cannot be null" );
        PortfolioStockEntity portfolioStockEntity = portfolioStockRepository.
            findFirstByCustomerUuidAndPortfolioUuidAndTickerSymbol( customerUuid, portfolioUuid, tickerSymbol );
        if ( portfolioStockEntity == null )
        {
            throw new PortfolioStockNotFound( customerUuid, portfolioUuid, tickerSymbol );
        }
        PortfolioStockDTO portfolioStockDTO = this.entityToDTO( portfolioStockEntity );
        this.stockInformationService
            .setStockPrice( portfolioStockDTO, StockPriceFetchMode.ASYNCHRONOUS );
        logMethodEnd( methodName, portfolioStockDTO );
        return portfolioStockDTO;
    }

    /**
     * Get all of the stocks for a portfolio
     * @param portfolioUuid
     * @return
     * @throws StockNotFoundException
     * @throws StockQuoteUnavailableException
     */
    public List<PortfolioStockDTO> getPortfolioStocks( final UUID portfolioUuid )
        throws StockNotFoundException,
               StockQuoteUnavailableException
    {
        final String methodName = "getPortfolioStocks";
        logMethodBegin( methodName, portfolioUuid );
        List<PortfolioStockEntity> stocks = this.portfolioStockRepository
                                                .findByPortfolioUuidOrderByTickerSymbol( portfolioUuid );
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
     * Determines if the portfolio stock exists.
     * @param customerUuid
     * @param portfolioUuid
     * @param tickerSymbol
     * @return
     */
    public boolean isStockExists( final String customerUuid,
                                  final String portfolioUuid,
                                  final String tickerSymbol )
    {
        Objects.requireNonNull( customerUuid, "customerUuid cannot be null" );
        Objects.requireNonNull( portfolioUuid, "portfolioUuid cannot be null" );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        return this.isStockExists( UUIDUtil.uuid( customerUuid ),
                                   UUIDUtil.uuid( portfolioUuid ),
                                   tickerSymbol );
    }

    /**
     * Determines if the customer stock entry is in the database
     * @param customerUuid
     * @param tickerSymbol
     * @return
     */
    public boolean isStockExists( final UUID customerUuid, final UUID portfolioUuid, final String tickerSymbol )
    {
        final String methodName = "isStockExistsInDatabase";
        logMethodBegin( methodName, customerUuid, tickerSymbol );
        Objects.requireNonNull( customerUuid, "customerUuid cannot be null" );
        Objects.requireNonNull( portfolioUuid, "portfolioUuid cannot be null" );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        PortfolioStockEntity portfolioStockEntity = PortfolioStockEntity.newInstance();
        portfolioStockEntity.setCustomerUuid( customerUuid );
        portfolioStockEntity.setPortfolioUuid( portfolioUuid );
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
     */
    public PortfolioStockEntity createPortfolioStockEntity( final PortfolioStockDTO portfolioStockDTO )
    {
        final String methodName = "createPortfolioStockEntity";
        logMethodBegin( methodName, portfolioStockDTO );
        Objects.requireNonNull( portfolioStockDTO );
        PortfolioStockEntity portfolioStockEntity = PortfolioStockEntity.newInstance();
        portfolioStockEntity.setPortfolioUuid( UUIDUtil.uuid( portfolioStockDTO.getPortfolioId() ));
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
    public void setPortfolioStockRepository( final PortfolioStockRepository portfolioStockRepository )
    {
        this.portfolioStockRepository = portfolioStockRepository;
    }
}
