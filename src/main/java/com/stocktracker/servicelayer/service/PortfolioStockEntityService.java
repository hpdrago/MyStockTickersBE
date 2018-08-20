package com.stocktracker.servicelayer.service;

import com.fasterxml.uuid.impl.UUIDUtil;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.PortfolioStockNotFound;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockQuoteUnavailableException;
import com.stocktracker.repositorylayer.entity.PortfolioStockEntity;
import com.stocktracker.repositorylayer.repository.PortfolioStockRepository;
import com.stocktracker.weblayer.dto.PortfolioStockDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * This is the service class for portfolio stock related methods.
 *
 * Created by mike on 11/26/2016.
 */
@Service
public class PortfolioStockEntityService extends StockInformationEntityService<PortfolioStockEntity,
                                                                               PortfolioStockDTO,
                                                                               PortfolioStockRepository>
{
    @Autowired
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
        final PortfolioStockEntity portfolioStockEntity = portfolioStockRepository.
            findFirstByCustomerUuidAndPortfolioUuidAndTickerSymbol( customerUuid, portfolioUuid, tickerSymbol );
        if ( portfolioStockEntity == null )
        {
            throw new PortfolioStockNotFound( customerUuid, portfolioUuid, tickerSymbol );
        }
        final PortfolioStockDTO portfolioStockDTO = this.entityToDTO( portfolioStockEntity );
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
    {
        final String methodName = "getPortfolioStocks";
        logMethodBegin( methodName, portfolioUuid );
        final List<PortfolioStockEntity> stocks = this.portfolioStockRepository
                                                      .findByPortfolioUuidOrderByTickerSymbol( portfolioUuid );
        final List<PortfolioStockDTO> portfolioStockDTOs = entitiesToDTOs( stocks );
        logMethodEnd( methodName, portfolioStockDTOs.size() );
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
        final PortfolioStockEntity portfolioStockEntity = this.context.getBean( PortfolioStockEntity.class );
        portfolioStockEntity.setCustomerUuid( customerUuid );
        portfolioStockEntity.setPortfolioUuid( portfolioUuid );
        portfolioStockEntity.setTickerSymbol( tickerSymbol );
        Example<PortfolioStockEntity> example = Example.of( portfolioStockEntity );
        boolean exists = portfolioStockRepository.exists( example );
        logMethodEnd( methodName, exists );
        return exists;
    }

    @Override
    public List<PortfolioStockDTO> entitiesToDTOs( final Iterable<PortfolioStockEntity> entities )
    {
        List<PortfolioStockDTO> dtos = super.entitiesToDTOs( entities );

        return dtos;
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
}
