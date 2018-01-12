package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockNotFoundInDatabaseException;
import com.stocktracker.common.exceptions.StockQuoteUnavailableException;
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
public class StockService extends BaseStockQuoteContainerService<StockEntity, StockDTO> implements MyLogger
{
    private StockRepository stockRepository;

    /**
     * Get a page of StockDomainEntities's
     * @param pageRequest
     * @return
     */
    public Page<StockDTO> getPage( final Pageable pageRequest, final boolean withStockPrices )
        throws StockQuoteUnavailableException,
               StockNotFoundException
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
     * @param withStockPrices
     * @return
     */
    public Page<StockDTO> getCompaniesLike( final Pageable pageRequest, final String companiesLike,
                                            final boolean withStockPrices )
        throws StockQuoteUnavailableException,
               StockNotFoundException
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
        Page<StockDTO> stockDTOPage = this.entitiesToDTOs( pageRequest, stockEntities );
        logMethodEnd( methodName );
        return stockDTOPage;
    }

    /**
     * Gets a single stock for the {@code tickerSymbol} from the stock cache or the database if necessary.
     * If the stock price is not current, a request will be made to update the price asynchronously.  In this case,
     * the {@code cachedStock.stockQuoteState = StockQuoteState.STALE or StockQuoteState.NOT_FOUND}.  In either case,
     * a subsequent request will need to be made to retrieve the updated stock price while the background tasks obtains
     * the update from the stock quote service.
     *
     * @param tickerSymbol
     * @return
     * @throws StockNotFoundInDatabaseException if {@code tickerSymbol} is not found in the stock table
     */
    public StockDTO getStock( final String tickerSymbol )
    {
        final String methodName = "getStock";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        StockEntity stockEntity = this.stockRepository.findOne( tickerSymbol );
        StockDTO stockDTO = null;
        if ( stockEntity != null )
        {
            stockDTO = this.entityToDTO( stockEntity );
        }
        logMethodEnd( methodName, stockDTO );
        return stockDTO;
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
     * Add a Yahoo stock to the database
     * @param stockQuote
     * @return {@code StockDTO} the stock that was added to the database
     */
    public StockDTO saveStock( final StockQuote stockQuote )
    {
        final String methodName = "saveStock";
        logMethodBegin( methodName, stockQuote );
        Objects.requireNonNull( stockQuote, "yahooStock cannot be null" );
        StockDTO stockDTO = stockQuoteToStockDTO( stockQuote );
        this.saveStock( stockDTO );
        logMethodEnd( methodName, stockDTO );
        return stockDTO;
    }

    /**
     * Saves the {@code stockEntity} to the database.
     * @param stockEntity
     */
    public void saveStock( final StockEntity stockEntity )
    {
        final String methodName = "saveStock";
        logMethodBegin( methodName, stockEntity );
        Objects.requireNonNull( stockEntity, "stockEntity cannot be null" );
        this.stockRepository.save( stockEntity );
        logMethodEnd( methodName );
    }

    /**
     * Converts a {@code CachedStockQuote} into a {@code StockDTO}
     * @param stockQuote
     * @return
     */
    private StockDTO stockQuoteToStockDTO( final StockQuote stockQuote )
    {
        StockDTO stockDTO = StockDTO.newInstance();
        stockDTO.setTickerSymbol( stockQuote.getTickerSymbol() );
        stockDTO.setCompanyName( stockQuote.getCompanyName() );
        stockDTO.setStockExchange( stockQuote.getStockExchange() );
        stockDTO.setLastPriceChange( stockQuote.getLastPriceChange() );
        stockDTO.setLastPrice( stockQuote.getLastPrice() );
        stockDTO.setStockQuoteState( stockQuote.getStockQuoteState() );
        return stockDTO;
    }

    /**
     * Add a stock to the database
     * @param stockDTO
     * @return
     */
    public StockDTO saveStock( final StockDTO stockDTO )
    {
        final String methodName = "saveStock";
        logMethodBegin( methodName, stockDTO );
        Objects.requireNonNull( stockDTO, "stockDTO cannot be null" );
        StockEntity stockEntity = this.dtoToEntity( stockDTO );
        stockEntity = this.stockRepository.save( stockEntity );
        StockDTO returnStockDTO = this.entityToDTO( stockEntity );
        //this.setStockQuoteInformation( returnStockDTO );
        logMethodEnd( methodName, returnStockDTO );
        return returnStockDTO;
    }

    /**
     * Delete the stock
     * @param tickerSymbol
     */
    public void deleteStock( final String tickerSymbol )
    {
        final String methodName = "deleteStock";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        this.stockRepository.delete( tickerSymbol );
        logMethodEnd( methodName );
    }

    /**
     * This method will validate that the ticker symbol exists in the database.  If it doesn't, it will
     * attempt to get a stock quote and create the database entry.  This method simply calls {@code getStockEntity}
     * but is provided for readability purposes.
     * @param tickerSymbol
     * @throws StockNotFoundException If the {@code tickerSymbol} cannot be found.
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
     * @throws StockNotFoundException
     */
    public StockEntity getStockEntity( final String tickerSymbol )
        throws StockQuoteUnavailableException,
               StockNotFoundException
    {
        final String methodName = "getStockEntity";
        logMethodBegin( methodName, tickerSymbol );
        StockEntity stockEntity = stockRepository.findOne( StringUtils.truncate( tickerSymbol, StockEntity.TICKER_SYMBOL_LEN ) );
        if ( stockEntity == null )
        {
            logDebug( methodName, tickerSymbol + " does note exist in the database, getting quote..." );
            StockQuote stockQuote = this.getStockQuoteService().getStockQuote( tickerSymbol, StockQuoteFetchMode.SYNCHRONOUS );
            if ( stockQuote == null )
            {
                logDebug( methodName, "Cannot get a quote for stock " + tickerSymbol );
                throw new StockNotFoundException( tickerSymbol );
            }
            else
            {
                logDebug( methodName, "Creating new stock table entry" );
                stockEntity = stockQuoteToStockEntity( stockQuote );
                this.stockRepository.save( stockEntity );
            }
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
        final StockEntity stockEntity = stockRepository.findOne( StringUtils.truncate( tickerSymbol, StockEntity.TICKER_SYMBOL_LEN ) );
        if ( stockEntity == null )
        {
            logError( methodName, "Stock not found in stock {0} table to mark as discontinued.", tickerSymbol );
        }
        else
        {
            stockEntity.setDiscontinuedInd( true );
            this.saveStock( stockEntity );
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
        stockEntity.setTickerSymbol( stockQuote.getTickerSymbol() );
        stockEntity.setCompanyName( stockQuote.getCompanyName() );
        stockEntity.setStockExchange( stockQuote.getStockExchange() );
        stockEntity.setLastPrice( stockQuote.getLastPrice() );
        stockEntity.setLastPriceChange( stockQuote.getLastPriceChange() );
        stockEntity.setLastPriceChange( stockQuote.getLastPriceChange() );
        return stockEntity;
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

    @Autowired
    public void setStockRepository( final StockRepository stockRepository )
    {
        logInfo( "setStockRepository", "Dependency Injection of " + stockRepository );
        this.stockRepository = stockRepository;
    }
}
