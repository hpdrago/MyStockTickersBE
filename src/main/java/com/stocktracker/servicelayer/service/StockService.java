package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.common.exceptions.StockNotFoundInDatabaseException;
import com.stocktracker.repositorylayer.common.BooleanUtils;
import com.stocktracker.repositorylayer.entity.StockEntity;
import com.stocktracker.repositorylayer.repository.StockRepository;
import com.stocktracker.weblayer.dto.StockDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yahoofinance.Stock;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * Created by mike on 9/11/2016.
 */
@Service
@Transactional
public class StockService extends BaseService<StockEntity, StockDTO> implements MyLogger
{
    private YahooStockService yahooStockService;
    private StockService stockService;
    private StockRepository stockRepository;

    @Autowired
    public void setStockRepository( final StockRepository stockRepository )
    {
        this.stockRepository = stockRepository;
    }

    @Autowired
    public void setStockService( final StockService stockService )
    {
        this.stockService = stockService;
    }

    @Autowired
    public void setYahooStockService( final YahooStockService yahooStockService )
    {
        this.yahooStockService = yahooStockService;
    }

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
     * To be implemented by any class containing a ticker symbol and a stock price to
     * beu use with the {@code setStockPrice} methods
     */
    public interface StockPriceContainer
    {
        String getTickerSymbol();
        void setStockPrice( final BigDecimal stockPrice );
    }

    /**
     * Using the ticker symbol of {@code container}, obtains and sets the stock's company name on the {@code container}
     * @param container
     */
    public void setStockPrice( final StockPriceContainer container )
    {
        final String methodName = "setStockPrice";
        Objects.requireNonNull( container.getTickerSymbol(), "container.getTickerSymbol() returns null" );
        logMethodBegin( methodName, container.getTickerSymbol() );
        StockEntity stockEntity = getStockEntity( container.getTickerSymbol() );
        final BigDecimal stockPrice = stockEntity.getLastPrice();
        container.setStockPrice( stockPrice );
        logMethodEnd( methodName, stockPrice );
    }

    /**
     * Using the ticker symbol of {@code container}, obtains and sets the stock's company name on the {@code container}
     * @param list
     */
    public void setCompanyName( final List<StockCompanyNameContainer> list )
    {
        list.forEach( stockCompanyNameContainer -> setCompanyName( stockCompanyNameContainer ));
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
        Objects.requireNonNull( pageRequest, "pageRequest cannot be null" );
        Objects.requireNonNull( source, "source cannot be null" );
        List<StockDTO> stockDomainEntities = super.entitiesToDTOs( source.getContent() );
        if ( updateStockPrices )
        {
            updateStockPrices( stockDomainEntities );
        }
        return new PageImpl<>( stockDomainEntities, pageRequest, source.getTotalElements() );
    }

    /**
     * Adds the current stock prices for the stocks contained within {@code stockDomainEntities}
     * @param stockDomainEntities
     */
    private void updateStockPrices( final List<StockDTO> stockDomainEntities )
    {
        final String methodName = "updateStockPrices";
        logMethodBegin( methodName );
        Objects.requireNonNull( stockDomainEntities, "stockDomainEntities cannot be null" );
        for ( StockDTO stockDTO : stockDomainEntities )
        {
            updateStockPrice( stockDTO );
        }
        logMethodEnd( methodName );
    }

    /**
     * Updates the stock price to the last price
     * @param stockDTO
     */
    private void updateStockPrice( final StockDTO stockDTO )
    {
        Objects.requireNonNull( stockDTO, "stockDTO cannot be null" );
        Objects.requireNonNull( this.yahooStockService, "yahooStockService cannot be null" );
        StockTickerQuote stockTickerQuote = this.yahooStockService.getStockQuote( stockDTO.getTickerSymbol() );
        stockDTO.setLastPrice( stockTickerQuote.getLastPrice() );
        stockDTO.setLastPriceChange( stockTickerQuote.getLastPriceChange() );
        stockDTO.setLastPriceUpdate( stockTickerQuote.getLastPriceUpdate() );
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
        if ( stockEntity == null )
        {
            throw new StockNotFoundInDatabaseException( tickerSymbol );
        }
        StockDTO stockDTO = this.entityToDTO( stockEntity );
        updateStockPrice( stockDTO );
        setCompanyName( stockDTO );
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
        logMethodBegin( methodName, stockPrice );
        return stockPrice;
    }

    /**
     * Add a Yahoo stock to the database
     * @param yahooStock
     * @return {@code StockDTO} the stock that was added to the database
     */
    public StockDTO addStock( final Stock yahooStock )
    {
        final String methodName = "addStock";
        logMethodBegin( methodName, yahooStock );
        Objects.requireNonNull( yahooStock, "yahooStock cannot be null" );
        StockDTO stockDTO = StockDTO.newInstance();
        stockDTO.setTickerSymbol( yahooStock.getSymbol() );
        stockDTO.setCompanyName( yahooStock.getName() );
        stockDTO.setUserEntered( false );
        stockDTO.setExchange( yahooStock.getStockExchange() );
        StockTickerQuote stockTickerQuote = this.yahooStockService.getStockTickerQuote( yahooStock );
        stockDTO.setLastPriceChange( stockTickerQuote.getLastPriceChange() );
        stockDTO.setLastPrice( yahooStock.getQuote().getPrice() );
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
        return stockRepository.findOne( tickerSymbol );
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

}
