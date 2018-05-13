package com.stocktracker.servicelayer.service;

import com.stocktracker.common.exceptions.DuplicateEntityException;
import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.entity.StockCompanyEntity;
import com.stocktracker.repositorylayer.repository.StockCompanyRepository;
import com.stocktracker.servicelayer.service.cache.stockcompany.StockCompanyEntityCacheDataReceiver;
import com.stocktracker.servicelayer.service.cache.stockcompany.StockCompanyEntityCache;
import com.stocktracker.servicelayer.service.cache.stockcompany.StockCompanyEntityCacheClient;
import com.stocktracker.servicelayer.service.cache.stockcompany.StockCompanyEntityContainer;
import com.stocktracker.servicelayer.service.cache.stockpricequote.IEXTradingStockService;
import com.stocktracker.weblayer.dto.StockCompanyDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.zankowski.iextrading4j.api.stocks.Company;

import java.util.Objects;

/**
 * This is the service class for the stock company table entity.
 * The stock company table contains all know stocks that are currently in use and those that have been discontinued.
 */
@Service
public class StockCompanyEntityService extends VersionedEntityService<String,
                                                                      StockCompanyEntity,
                                                                      String,
                                                                      StockCompanyDTO,
                                                                      StockCompanyRepository>
{
    private StockCompanyRepository stockCompanyRepository;
    private StockCompanyEntityCache stockCompanyEntityCache;
    private IEXTradingStockService iexTradingStockService;
    private StockCompanyEntityCacheClient stockCompanyEntityCacheClient;

    /**
     * Updates the stock company information in {@code container}. It works with the {@code StockCompanyCache} to
     * retrieve the stock company asynchronously if needed.
     * @param container The container to set the value.
     */
    public void setCompanyInformation( final StockCompanyEntityContainer container )
    {
        final String methodName = "getStockCompanyDTO";
        logMethodBegin( methodName, container.getTickerSymbol() );
        /*
         * Create the DTO.
         */
        final StockCompanyDTO stockCompanyDTO = this.context.getBean( StockCompanyDTO.class );
        /*
         * Create the cached data receiver.
         */
        final StockCompanyEntityCacheDataReceiver receiver = new StockCompanyEntityCacheDataReceiver( container.getTickerSymbol() );
        /*
         * Get the cached data or make an asynchronous request for the data.
         */
        this.stockCompanyEntityCacheClient.setCachedData( receiver );
        /*
         * Set the cache data and status results.
         */
        stockCompanyDTO.setStockCompanyEntity( receiver.getStockCompanyEntity() );
        stockCompanyDTO.setStockCompanyCacheEntryState( receiver.getCacheState() );
        stockCompanyDTO.setStockCompanyCacheError( receiver.getError() );
        logMethodEnd( methodName, stockCompanyDTO );
    }

    /**
     * Checks the stock company cache to see if the stock company is being fetched and if so, will block and wait for
     * the fetch to complete.  If the company is in the cache but not being fetch, the cached version will be returned.
     * Otherwise, database entity will be loaded.
     * @param tickerSymbol
     * @return Stock Company DTO
     */
    public StockCompanyDTO getStockCompanyDTO( final String tickerSymbol )
    {
        final String methodName = "getStockCompanyDTO";
        logMethodBegin( methodName, tickerSymbol );
        final StockCompanyEntity stockCompanyEntity = this.stockCompanyEntityCacheClient.getCachedData( tickerSymbol );
        final StockCompanyDTO stockCompanyDTO = this.entityToDTO( stockCompanyEntity );
        logMethodEnd( methodName, stockCompanyDTO );
        return stockCompanyDTO;
    }

    /**
     * Get a page of StockDomainEntities's
     * @param pageRequest
     * @return
     */
    public Page<StockCompanyDTO> getPage( final Pageable pageRequest, final boolean withStockPrices )
    {
        final String methodName = "getPage";
        logMethodBegin( methodName, pageRequest );
        Objects.requireNonNull( pageRequest, "pageRequest cannot be null" );
        /*
         * Get the page from the database
         */
        final Page<StockCompanyEntity> stockEntities = this.stockCompanyRepository
                                                           .findAll( pageRequest );
        /*
         * Map from Entity to DomainEntity
         */
        final Page<StockCompanyDTO> stockDTOPage = this.entitiesToDTOs( pageRequest, stockEntities );
        logMethodEnd( methodName );
        return stockDTOPage;
    }

    /**
     * Get companies matching either the company name or ticker symbol strings
     * @param pageRequest
     * @param companiesLike
     * @return
     */
    public Page<StockCompanyDTO> getCompaniesLike( final Pageable pageRequest, final String companiesLike )
    {
        final String methodName = "getCompaniesLike";
        logMethodBegin( methodName, pageRequest, companiesLike );
        Objects.requireNonNull( pageRequest, "pageRequest cannot be null" );
        Objects.requireNonNull( companiesLike, "companiesLike cannot be null" );
        /*
         * Get the page from the database
         */
        final Page<StockCompanyEntity> stockEntities = this.stockCompanyRepository
                                                           .findByCompanyNameIsLikeOrTickerSymbolIsLike(
            pageRequest, companiesLike + "%", companiesLike + "%" );
        /*
         * Map from Entity to DomainEntity
         */
        final Page<StockCompanyDTO> stockDTOPage = this.entitiesToDTOs( pageRequest, stockEntities );
        logMethodEnd( methodName );
        return stockDTOPage;
    }

    /**
     * This method will validate that the ticker symbol exists in the database.  If it doesn't, it will
     * attempt to get a stock quote and create the database entry.
     * @param tickerSymbol
     * @return
     */
    public StockCompanyEntity checkStockTableEntry( final String tickerSymbol )
    {
        final String methodName = "checkStock";
        logMethodBegin( methodName, tickerSymbol );
        StockCompanyEntity stockCompanyEntity = null;
        try
        {
            stockCompanyEntity = this.getEntity( tickerSymbol );
        }
        catch( StockNotFoundException e )
        {
            /*
             * Add a discontinued company
             */
            stockCompanyEntity = this.context.getBean( StockCompanyEntity.class );
            stockCompanyEntity.setTickerSymbol( tickerSymbol );
            stockCompanyEntity.setDiscontinuedInd( true );
            try
            {
                stockCompanyEntity = this.addEntity( stockCompanyEntity );
            }
            catch( EntityVersionMismatchException e1 )
            {
                // ignore, it means another thread already added the company.
            }
            catch( DuplicateEntityException e1 )
            {
                // ignore, another must have added it.
            }
        }
        catch( VersionedEntityNotFoundException e )
        {
            e.printStackTrace();
        }
        logMethodEnd( methodName, stockCompanyEntity );
        return stockCompanyEntity;
    }

    /**
     * Adds the IEXTrading stock company to the database. Converts the Company to a StockCompanyEntity and then saves it.
     * @param company
     * @return
     */
    private StockCompanyEntity addStockCompany( final Company company )
    {
        final String methodName = "saveStockCompany";
        logMethodBegin( methodName, company );
        Objects.requireNonNull( company, "company argument cannot be null" );
        Objects.requireNonNull( company.getSymbol(), "company.symbol argument cannot be null" );
        Objects.requireNonNull( company.getCompanyName(), "company.companyName argument cannot be null" );
        StockCompanyEntity stockCompanyEntity = null;
        try
        {
            stockCompanyEntity = this.context.getBean( StockCompanyEntity.class );
            stockCompanyEntity.setDiscontinuedInd( false );
            BeanUtils.copyProperties( company, stockCompanyEntity );
            stockCompanyEntity = this.addEntity( stockCompanyEntity );
        }
        catch( EntityVersionMismatchException e )
        {
            logError( methodName, "Failed version check while adding stock company " + company );
        }
        catch( DuplicateEntityException e )
        {
            // ignore if it's already there.
        }
        logMethodEnd( methodName, stockCompanyEntity );
        return stockCompanyEntity;
    }

    /**
     * Sets the discontinued flag on the stock table for {@code tickerSymbol}
     * @param tickerSymbol
     */
    public void markStockAsDiscontinued( final String tickerSymbol )
    {
        final String methodName = "markStockAsDiscontinued";
        logMethodBegin( methodName, tickerSymbol );
        StockCompanyEntity stockCompanyEntity;
        try
        {
            stockCompanyEntity = this.getEntity( StringUtils.truncate( tickerSymbol, StockCompanyEntity.TICKER_SYMBOL_LEN ) );
            stockCompanyEntity.setDiscontinuedInd( true );
            try
            {
                stockCompanyEntity = this.saveEntity( stockCompanyEntity );
            }
            catch( EntityVersionMismatchException e )
            {
                logError( methodName, "Failed version check but saving anyway " + stockCompanyEntity, e );
                stockCompanyEntity.setVersion( e.getCurrentVersion() );
                this.stockCompanyRepository
                    .save( stockCompanyEntity );
            }
        }
        catch( VersionedEntityNotFoundException e )
        {
            logError( methodName, "Failed to retrieve a stock company but it should exists or we wouldn't" +
                                  "be marking it as discontinued." );
        }
        logMethodEnd( methodName );
    }

    @Override
    protected StockCompanyDTO createDTO()
    {
        return this.context.getBean( StockCompanyDTO.class );
    }

    @Override
    protected StockCompanyEntity createEntity()
    {
        return this.context.getBean( StockCompanyEntity.class );
    }

    @Override
    protected StockCompanyRepository getRepository()
    {
        return this.stockCompanyRepository;
    }

    @Autowired
    public void setStockCompanyRepository( final StockCompanyRepository stockCompanyRepository )
    {
        this.stockCompanyRepository = stockCompanyRepository;
    }

    @Autowired
    public void setIexTradingStockService( final IEXTradingStockService iexTradingStockService )
    {
        this.iexTradingStockService = iexTradingStockService;
    }

    @Autowired
    public void setStockCompanyEntityCache( final StockCompanyEntityCache stockCompanyEntityCache )
    {
        this.stockCompanyEntityCache = stockCompanyEntityCache;
    }

    @Autowired
    public void setStockCompanyEntityCacheClient( final StockCompanyEntityCacheClient stockCompanyEntityCacheClient )
    {
        this.stockCompanyEntityCacheClient = stockCompanyEntityCacheClient;
    }

}
