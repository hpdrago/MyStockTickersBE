package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockQuoteUnavailableException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.entity.StockEntity;
import com.stocktracker.repositorylayer.repository.StockRepository;
import com.stocktracker.servicelayer.stockinformationprovider.StockQuote;
import com.stocktracker.servicelayer.stockinformationprovider.StockQuoteFetchMode;
import com.stocktracker.weblayer.dto.StockDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Created by mike on 9/11/2016.
 */
@Service
@Transactional
public class StockEntityService extends StockQuoteContainerEntityService<String,
                                                                         StockEntity,
                                                                         StockDTO,
                                                                         StockRepository> implements MyLogger
{
    private StockRepository stockRepository;

    /**
     * Get a page of StockDomainEntities's
     * @param pageRequest
     * @return
     */
    public Page<StockDTO> getPage( final Pageable pageRequest, final boolean withStockPrices )
    {
        final String methodName = "getPage";
        logMethodBegin( methodName, pageRequest );
        Objects.requireNonNull( pageRequest, "pageRequest cannot be null" );
        /*
         * Get the page from the database
         */
        Page<StockEntity> stockEntities = stockRepository.findAll( pageRequest );
        /*
         * Map from Entity to DomainEntity
         */
        Page<StockDTO> stockDTOPage = this.entitiesToDTOs( pageRequest, stockEntities );
        logMethodEnd( methodName );
        return stockDTOPage;
    }

    /**
     * Get companies matching either the company name or ticker symbol strings
     * @param pageRequest
     * @param companiesLike
     * @return
     */
    public Page<StockDTO> getCompaniesLike( final Pageable pageRequest, final String companiesLike )
    {
        final String methodName = "getCompaniesLike";
        logMethodBegin( methodName, pageRequest, companiesLike );
        Objects.requireNonNull( pageRequest, "pageRequest cannot be null" );
        Objects.requireNonNull( companiesLike, "companiesLike cannot be null" );
        /*
         * Get the page from the database
         */
        Page<StockEntity> stockEntities = stockRepository.findByCompanyNameIsLikeOrTickerSymbolIsLike(
            pageRequest, companiesLike + "%", companiesLike + "%" );
        /*
         * Map from Entity to DomainEntity
         */
        Page<StockDTO> stockDTOPage = this.entitiesToDTOs( pageRequest, stockEntities, StockQuoteFetch.NONE );
        logMethodEnd( methodName );
        return stockDTOPage;
    }

    /**
     * Determines if the {@code tickerSymbol exists}
     * @param tickerSymbol
     * @return
     */
    public boolean isStockExistsInDatabase( final String tickerSymbol )
    {
        final String methodName = "isStockExistsInDatabase";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        boolean exists = stockRepository.exists( tickerSymbol );
        logMethodEnd( methodName, exists );
        return exists;
    }

    /**
     * This method will validate that the ticker symbol exists in the database.  If it doesn't, it will
     * attempt to get a stock quote and create the database entry.  This method simply calls {@code getStockEntity}
     * but is provided for readability purposes.
     * @param tickerSymbol
     * @return
     * @throws StockQuoteUnavailableException
     * @throws StockNotFoundException
     */
    public StockEntity checkStockTableEntry( final String tickerSymbol )
        throws StockQuoteUnavailableException,
               StockNotFoundException
    {
        final String methodName = "checkStock";
        logMethodBegin( methodName, tickerSymbol );
        StockEntity stockEntity = this.getStockEntity( tickerSymbol );
        logMethodEnd( methodName, stockEntity );
        return stockEntity;
    }

    /**
     * Retrieves a {@code StockEntity} from the database.
     * If the stock is not found in the database, a stock quote will be attempted to validated that this is a valid
     * ticker symbol. If a quote cannot be found, StockNoteFoundException will be thrown
     * @param tickerSymbol
     * @return
     * @throws StockQuoteUnavailableException
     * @throws StockNotFoundException
     */
    public StockEntity getStockEntity( final String tickerSymbol )
        throws StockQuoteUnavailableException,
               StockNotFoundException
    {
        final String methodName = "getStockEntity";
        logMethodBegin( methodName, tickerSymbol );
        StockEntity stockEntity = null;
        try
        {
            stockEntity = this.getEntity( StringUtils.truncate( tickerSymbol, StockEntity.TICKER_SYMBOL_LEN ) );
        }
        catch( VersionedEntityNotFoundException e )
        {
            logDebug( methodName, tickerSymbol + " does note exist in the database, getting quote..." );
            StockQuote stockQuote = this.getStockQuoteService()
                                        .getStockQuote( tickerSymbol, StockQuoteFetchMode.SYNCHRONOUS );
            if ( stockQuote == null )
            {
                logDebug( methodName, "Cannot get a quote for stock " + tickerSymbol );
                throw new StockNotFoundException( tickerSymbol );
            }
            else
            {
                stockEntity = saveStockQuote( stockQuote );
            }
        }
        logMethodEnd( methodName, stockEntity );
        return stockEntity;
    }

    /**
     * Saves the stock quote to the database.
     * @param stockQuote
     * @return
     */
    public StockEntity saveStockQuote( final StockQuote stockQuote )
    {
        final String methodName = "saveStockQuote";
        logMethodBegin( methodName, stockQuote );
        StockEntity stockEntity = null;
        try
        {
            try
            {
                /*
                 * Update the existing entity
                 */
                stockEntity = this.getEntity( stockQuote.getTickerSymbol() );
                this.setStockQuoteProperties( stockEntity, stockQuote );
                this.saveEntity( stockEntity );
            }
            catch( VersionedEntityNotFoundException e )
            {
                /*
                 * Create a new entity.
                 */
                stockEntity = this.stockQuoteToStockEntity( stockQuote );
                this.saveEntity( stockEntity );
            }
        }
        catch( EntityVersionMismatchException e2 )
        {
            logError( methodName, "Failed version check but saving stock anyway", e2 );
            stockEntity.setVersion( e2.getCurrentVersion() );
            this.getRepository()
                .save( stockEntity );
        }
        logMethodEnd( methodName, stockEntity );
        return stockEntity;
    }

    /**
     * Sets the discontinued flag on the stock table for {@code tickerSymbol}
     * @param tickerSymbol
     */
    public void markStockAsDiscontinued( final String tickerSymbol )
    {
        final String methodName = "markStockAsDiscontinued";
        logMethodBegin( methodName, tickerSymbol );
        final StockEntity stockEntity;
        try
        {
            stockEntity = this.getEntity( StringUtils.truncate( tickerSymbol, StockEntity.TICKER_SYMBOL_LEN ) );
            stockEntity.setDiscontinuedInd( true );
            try
            {
                this.saveEntity( stockEntity );
            }
            catch( EntityVersionMismatchException e )
            {
                logError( methodName, "Failed version check but saving anyway " + stockEntity, e );
                stockEntity.setVersion( e.getCurrentVersion() );
                this.stockRepository
                    .save( stockEntity );
            }
        }
        catch( VersionedEntityNotFoundException e )
        {
            logError( methodName, "Stock not found in stock {0} table to mark as discontinued.", tickerSymbol );
        }
        logMethodEnd( methodName );
    }

    /**
     * Converts the {@code StockQuote} into a {@code StockEntity}
     * @param stockQuote
     * @return
     */
    private StockEntity stockQuoteToStockEntity( final StockQuote stockQuote )
    {
        final StockEntity stockEntity;
        stockEntity = new StockEntity();
        setStockQuoteProperties( stockEntity, stockQuote );
        return stockEntity;
    }

    /**
     * Sets the stock quote propertess on the stock entity.
     * @param stockEntity
     * @param stockQuote
     */
    private void setStockQuoteProperties( final StockEntity stockEntity, final StockQuote stockQuote )
    {
        stockEntity.setTickerSymbol( stockQuote.getTickerSymbol() );
        stockEntity.setCompanyName( stockQuote.getCompanyName() );
        stockEntity.setStockExchange( stockQuote.getStockExchange() );
        stockEntity.setLastPrice( stockQuote.getLastPrice() );
        stockEntity.setLastPriceChange( stockQuote.getLastPriceChange() );
        stockEntity.setDiscontinuedInd( false );
    }


    @Override
    protected StockDTO entityToDTO( final StockEntity stockEntity )
    {
        Objects.requireNonNull( stockEntity );
        StockDTO stockDTO = StockDTO.newInstance();
        BeanUtils.copyProperties( stockEntity, stockDTO );
        return stockDTO;
    }

    @Override
    protected StockEntity dtoToEntity( final StockDTO stockDTO )
    {
        Objects.requireNonNull( stockDTO );
        StockEntity stockEntity = StockEntity.newInstance();
        BeanUtils.copyProperties( stockDTO, stockEntity );
        return stockEntity;
    }

    @Override
    protected StockRepository getRepository()
    {
        return this.stockRepository;
    }

    @Autowired
    public void setStockRepository( final StockRepository stockRepository )
    {
        logInfo( "setStockRepository", "Dependency Injection of " + stockRepository );
        this.stockRepository = stockRepository;
    }
}
