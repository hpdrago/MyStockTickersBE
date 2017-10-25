package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.StockNotFoundInDatabaseException;
import com.stocktracker.common.exceptions.StockNotFoundInExchangeException;
import com.stocktracker.repositorylayer.common.BooleanUtils;
import com.stocktracker.repositorylayer.entity.StockEntity;
import com.stocktracker.repositorylayer.repository.StockRepository;
import com.stocktracker.weblayer.dto.StockDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by mike on 9/11/2016.
 */
@Service
@Transactional
public class StockService extends BaseService<StockEntity, StockDTO> implements MyLogger
{
    private YahooStockService yahooStockService;
    private StockRepository stockRepository;
    private StockCache stockCache;

    /**
     * To be implemented by any class containing a ticker symbol and a stock company name to
     * be used with the {@code setCompanyName} methods
     */
    public interface StockCompanyNameContainer
    {
        String getTickerSymbol();
        void setCompanyName( final String companyName );
    }

    /**
     * To be implemented by any class containing a ticker symbol and a stock price to
     * beu use with the {@code updateStockPrice} methods
     */
    public interface LastPriceContainer
    {
        String getTickerSymbol();
        void setLastPrice( final BigDecimal stockPrice );
    }

    /**
     * Transforms {@code Page<ENTITY>} objects into {@code Page<DTO>} objects.
     *
     * @param pageRequest The information of the requested page.
     * @param source      The {@code Page<ENTITY>} object.
     * @param updateStockPrices If true, the last stock price will be updated
     * @return The created {@code Page<DTO>} object.
     */
    private Page<StockDTO> mapStockEntityPageIntoStockDTOPage( final Pageable pageRequest, Page<StockEntity> source,
                                                               final boolean updateStockPrices )
    {
        final String methodName = "mapStockEntityPageIntoStockDTOPage";
        Objects.requireNonNull( pageRequest, "pageRequest cannot be null" );
        Objects.requireNonNull( source, "source cannot be null" );
        logMethodBegin( methodName );
        List<StockDTO> stockDTOs = this.entitiesToDTOs( source.getContent() );
        if ( updateStockPrices )
        {
            for ( StockDTO stockDTO : stockDTOs )
            {
                setStockInformation( stockDTO );
            }
        }
        logMethodEnd( methodName );
        return new PageImpl<>( stockDTOs, pageRequest, source.getTotalElements() );
    }

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
        Page<StockDTO> stockDTOPage = mapStockEntityPageIntoStockDTOPage( pageRequest, stockEntities, withStockPrices );
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
        Page<StockDTO> stockDTOPage = mapStockEntityPageIntoStockDTOPage( pageRequest, stockEntities, withStockPrices );
        logMethodEnd( methodName );
        return stockDTOPage;
    }

    /**
     * Gets a single stock for the {@code tickerSymbol} from the database
     * @param tickerSymbol
     * @return
     * @throws StockNotFoundInDatabaseException if {@code tickerSymbol} is not found in the stock table
     */
    public StockDTO getStock( final String tickerSymbol )
        throws StockNotFoundInDatabaseException
    {
        final String methodName = "getStock";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        /*
         * Get the stock from the database
         */
        StockEntity stockEntity = getStockEntity( tickerSymbol );
        StockDTO stockDTO;
        if ( stockEntity == null )
        {
            logDebug( methodName, "{0} not in stock table, calling yahoo..." );
            Optional<StockCache.CachedStock> cachedStock = this.stockCache.getStock( tickerSymbol );
            logDebug( methodName, "yahooStock: {0}", cachedStock );
            if ( !cachedStock.isPresent() )
            {
                throw new StockNotFoundInExchangeException( tickerSymbol );
            }
            stockDTO = this.addStock( cachedStock.get() );
        }
        else
        {
            stockDTO = this.entityToDTO( stockEntity );
            setStockInformation( stockDTO );
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
     * Add a stock to the database
     * @param stockDTO
     * @return
     */
    public StockDTO addStock( final StockDTO stockDTO )
    {
        final String methodName = "addStock";
        logMethodBegin( methodName, stockDTO );
        Objects.requireNonNull( stockDTO, "stockDTO cannot be null" );
        StockEntity stockEntity = this.dtoToEntity( stockDTO );
        stockEntity = stockRepository.save( stockEntity );
        StockDTO returnStockDTO = this.entityToDTO( stockEntity );
        this.setStockInformation( returnStockDTO );
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
        stockRepository.delete( tickerSymbol );
        logMethodEnd( methodName );
    }

    /**
     * Get the last price for the stock
     * @param tickerSymbol
     * @return Null if there are any exceptions
     */
    public BigDecimal getStockPrice( final String tickerSymbol )
    {
        final String methodName = "getStockQuote";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        Objects.requireNonNull( this.yahooStockService, "yahooStockService cannot be null" );
        StockTickerQuote stockTickerQuote = this.yahooStockService.getStockQuote( tickerSymbol );
        BigDecimal stockPrice = null;
        if ( stockTickerQuote != null )
        {
            stockPrice = stockTickerQuote.getLastPrice();
        }
        /*
         * Update the stock table with the new price
         */
        updateStockPrice( tickerSymbol, stockTickerQuote );
        logMethodBegin( methodName, stockPrice );
        return stockPrice;
    }

    /**
     * Updates the database stock table with the new price information
     * @param tickerSymbol
     * @param stockTickerQuote
     */
    private void updateStockPrice( final String tickerSymbol, final StockTickerQuote stockTickerQuote )
    {
        StockEntity stockEntity = this.getStockEntity( tickerSymbol );
        stockEntity.setLastPrice( stockTickerQuote.getLastPrice() );
        stockEntity.setLastPriceChange( stockTickerQuote.getLastPriceChange() );
        this.stockRepository.save( stockEntity );
    }

    /**
     * Add a Yahoo stock to the database
     * @param cachedStock
     * @return {@code StockDTO} the stock that was added to the database
     */
    public StockDTO addStock( final StockCache.CachedStock cachedStock )
    {
        final String methodName = "addStock";
        logMethodBegin( methodName, cachedStock );
        Objects.requireNonNull( cachedStock, "yahooStock cannot be null" );
        StockDTO stockDTO = StockDTO.newInstance();
        stockDTO.setTickerSymbol( cachedStock.getTickerSymbol() );
        stockDTO.setCompanyName( cachedStock.getCompanyName() );
        stockDTO.setUserEntered( false );
        stockDTO.setExchange( cachedStock.getExchange() );
        stockDTO.setLastPriceChangeTimestamp( cachedStock.getLastPriceChange() );
        stockDTO.setLastPrice( cachedStock.getLastPrice() );
        stockDTO.setCreatedBy( 1 );
        this.addStock( stockDTO );
        logMethodEnd( methodName, stockDTO );
        return stockDTO;
    }

    /**
     * Retrieves a {@code StockEntity} from the database
     * @param tickerSymbol
     * @return
     */
    private StockEntity getStockEntity( final String tickerSymbol )
    {
        return stockRepository.findOne( StringUtils.truncate( tickerSymbol, StockEntity.TICKER_SYMBOL_LEN ) );
    }

    /**
     * Using the ticker symbol of {@code container}, obtains and sets the stock's company name on the {@code container}
     * @param container
     */
    public void setCompanyName( final StockCompanyNameContainer container )
    {
        final String methodName = "setCompanyName";
        Objects.requireNonNull( container.getTickerSymbol(), "container.getTickerSymbol() returns null" );
        logMethodBegin( methodName, container.getTickerSymbol() );
        StockEntity stockEntity = getStockEntity( container.getTickerSymbol() );
        final String companyName = stockEntity.getCompanyName();
        container.setCompanyName( companyName );
        logMethodEnd( methodName, companyName );
    }

    /**
     * Convenience method to set the stock price.  Simply calls {@code updateStockPrice}.
     * Provided for searches for setting a stock price.
     */
    public void setStockPrice( final LastPriceContainer container )
    {
        this.updateStockPrice( container );
    }

    /**
     * Using the ticker symbol of {@code container}, obtains and sets the stock's company name on the {@code container}
     * @param container
     */
    public void updateStockPrice( final LastPriceContainer container )
    {
        final String methodName = "updateStockPrice";
        Objects.requireNonNull( container.getTickerSymbol(), "container.getTickerSymbol() returns null" );
        logMethodBegin( methodName, container.getTickerSymbol() );
        StockEntity stockEntity = getStockEntity( container.getTickerSymbol() );
        final BigDecimal stockPrice = stockEntity.getLastPrice();
        container.setLastPrice( stockPrice );
        logMethodEnd( methodName, stockPrice );
    }

    /**
     * Makes a call to Yahoo to get the current stock information including the price, last price change, and company
     * name.  This information that was retrieved from Yahoo, is also saved back to the database stock table.
     * @param containers
     * @throws IOException
     */
    public void setStockInformation( final List<YahooStockService.YahooStockContainer> containers )
        throws IOException
    {
        final String methodName = "setStockInformation";
        logMethodBegin( methodName );
        Objects.requireNonNull( containers, "stockDomainEntities cannot be null" );
        for ( YahooStockService.YahooStockContainer container : containers )
        {
            this.setStockInformation( container );
        }
        logMethodEnd( methodName );
    }

    /**
     * Obtains the stock information from the stock cache with the current stock information including the price,
     * last price change, and company name.
     * This information that was retrieved from the cache, is also saved back to the database stock table.
     * @param container
     */
    public void setStockInformation( final YahooStockService.YahooStockContainer container )
    {
        final String methodName = "setStockInformation";
        logMethodBegin( methodName, container.getTickerSymbol() );
        Objects.requireNonNull( container, "container cannot be null" );
        Objects.requireNonNull( container.getTickerSymbol(), "container.getTickerSymbol() returns null" );
        Optional<StockCache.CachedStock> cachedStock = this.stockCache.getStock( container.getTickerSymbol() );
        if ( cachedStock.isPresent() )
        {
            /*
             * Update the stock table with the new information
             */
            container.setCompanyName( cachedStock.get().getCompanyName() );
            container.setLastPrice( cachedStock.get().getLastPrice() );
            container.setLastPriceChangeTimestamp( cachedStock.get().getLastPriceChange() );
            this.saveStockInformation( container );
        }
        else
        {
            logWarn( methodName, "Could not get stock information from the stock cache for {0}",
                     container.getTickerSymbol() );
        }
        logMethodEnd( methodName, container );
    }

    /**
     * Saves the stock information in {@code container} to the stock table.
     * @param container
     */
    private void saveStockInformation( final YahooStockService.YahooStockContainer container )
    {
        final String methodName = "saveStockInformation";
        Objects.requireNonNull( container, "container cannot be null" );
        Objects.requireNonNull( container.getTickerSymbol(), "container.getTickerSymbol() returns null" );
        StockEntity stockEntity = this.getStockEntity( container.getTickerSymbol() );
        if ( stockEntity == null )
        {
            stockEntity.setCompanyName( container.getCompanyName() );
        }
        stockEntity.setLastPrice( container.getLastPrice() );
        stockEntity.setLastPriceChange( container.getLastPriceChangeTimestamp() );
        this.stockRepository.save( stockEntity );
        logMethodEnd( methodName, container );
    }

    @Override
    protected StockDTO entityToDTO( final StockEntity stockEntity )
    {
        Objects.requireNonNull( stockEntity );
        StockDTO stockDTO = StockDTO.newInstance();
        BeanUtils.copyProperties( stockEntity, stockDTO );
        stockDTO.setUserEntered( BooleanUtils.fromCharToBoolean( stockEntity.getUserEntered() ));
        return stockDTO;
    }

    @Override
    protected StockEntity dtoToEntity( final StockDTO stockDTO )
    {
        Objects.requireNonNull( stockDTO );
        StockEntity stockEntity = StockEntity.newInstance();
        BeanUtils.copyProperties( stockDTO, stockEntity );
        stockEntity.setUserEntered( BooleanUtils.fromBooleanToChar( stockDTO.isUserEntered() ));
        return stockEntity;
    }

    @Autowired
    public void setStockRepository( final StockRepository stockRepository )
    {
        this.stockRepository = stockRepository;
    }

    @Autowired
    public void setYahooStockService( final YahooStockService yahooStockService )
    {
        this.yahooStockService = yahooStockService;
    }

    @Autowired
    public void setStockCache( final StockCache stockCache )
    {
        this.stockCache = stockCache;
    }

}
