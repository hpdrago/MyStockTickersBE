package com.stocktracker.servicelayer.service;

import com.stocktracker.common.exceptions.EntityVersionMismatchException;
import com.stocktracker.common.exceptions.StockCompanyNotFoundException;
import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.VersionedEntityNotFoundException;
import com.stocktracker.repositorylayer.entity.StockCompanyEntity;
import com.stocktracker.repositorylayer.repository.StockCompanyRepository;
import com.stocktracker.servicelayer.stockinformationprovider.IEXTradingStockService;
import com.stocktracker.weblayer.dto.StockCompanyDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.zankowski.iextrading4j.api.stocks.Company;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * This is the service class for the stock company table entity.
 * The stock company table contains all know stocks that are currently in use and those that have been discontinued.
 */
@Service
public class StockCompanyEntityService extends VersionedEntityService<String,
                                                                      StockCompanyEntity,
                                                                      StockCompanyDTO,
                                                                      StockCompanyRepository>
{
    private StockCompanyRepository stockCompanyRepository;
    private IEXTradingStockService iexTradingStockService;

    /**
     * Gets the stock company from the database and if not found throws StockCompanyNotFoundException.
     * Call {@code getStockCompanyEntity} if you want to add the company to the database if the company is not found.
     * @param tickerSymbol
     * @return
     * @throws StockCompanyNotFoundException
     */
    public StockCompanyEntity getStockCompany( final String tickerSymbol )
        throws StockCompanyNotFoundException
    {
        final String methodName = "getStockCompany";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        final StockCompanyEntity stockCompanyEntity;
        try
        {
            stockCompanyEntity = this.getEntity( tickerSymbol );
        }
        catch( VersionedEntityNotFoundException e )
        {
            throw new StockCompanyNotFoundException( tickerSymbol, e );
        }
        logMethodEnd( methodName, stockCompanyEntity );
        return stockCompanyEntity;
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
        Page<StockCompanyEntity> stockEntities = this.stockCompanyRepository.findAll( pageRequest );
        /*
         * Map from Entity to DomainEntity
         */
        Page<StockCompanyDTO> stockDTOPage = this.entitiesToDTOs( pageRequest, stockEntities );
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
        Page<StockCompanyEntity> stockEntities = this.stockCompanyRepository.findByCompanyNameIsLikeOrTickerSymbolIsLike(
            pageRequest, companiesLike + "%", companiesLike + "%" );
        /*
         * Map from Entity to DomainEntity
         */
        Page<StockCompanyDTO> stockDTOPage = this.entitiesToDTOs( pageRequest, stockEntities );
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
        boolean exists = stockCompanyRepository.exists( tickerSymbol );
        logMethodEnd( methodName, exists );
        return exists;
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
        StockCompanyEntity stockEntity;
        try
        {
            stockEntity = this.getStockCompanyEntity( tickerSymbol );
        }
        catch( StockNotFoundException e )
        {
            /*
             * Add a discontinued company
             */
            stockEntity = this.context.getBean( StockCompanyEntity.class );
            stockEntity.setTickerSymbol( tickerSymbol );
            stockEntity.setDiscontinuedInd( true );
            try
            {
                this.addEntity( stockEntity );
            }
            catch( EntityVersionMismatchException e1 )
            {
                // ignore, it means another thread already added the company.
            }
        }
        logMethodEnd( methodName, stockEntity );
        return stockEntity;
    }

    /**
     * Retrieves a {@code StockCompanyEntity} from the database.
     * If the stock company is not found in the database, a get company request will be attempted to validated that
     * this is a valid ticker symbol. If a company cannot be found, StockNoteFoundException will be thrown
     * @param tickerSymbol
     * @return
     * @throws StockNotFoundException
     */
    public StockCompanyEntity getStockCompanyEntity( final String tickerSymbol )
        throws StockNotFoundException
    {
        final String methodName = "getStockCompanyEntity";
        logMethodBegin( methodName, tickerSymbol );
        Objects.requireNonNull( tickerSymbol, "tickerSymbol cannot be null" );
        StockCompanyEntity stockEntity = null;
        try
        {
            stockEntity = this.getEntity( StringUtils.truncate( tickerSymbol, StockCompanyEntity.TICKER_SYMBOL_LEN ) );
        }
        catch( VersionedEntityNotFoundException e )
        {
            logDebug( methodName, tickerSymbol + " does note exist in the database, getting quote..." );
            Company company = this.iexTradingStockService
                                  .getCompany( tickerSymbol );
            if ( company == null )
            {
                logDebug( methodName, "Cannot get a quote for stock " + tickerSymbol );
                throw new StockNotFoundException( tickerSymbol );
            }
            else
            {
                stockEntity = addStockCompany( company );
            }
        }
        logMethodEnd( methodName, stockEntity );
        return stockEntity;
    }

    /**
     * add the stock company to the database.
     * @param company
     * @return
     */
    public StockCompanyEntity addStockCompany( final Company company )
    {
        final String methodName = "saveStockCompany";
        logMethodBegin( methodName, company );
        StockCompanyEntity stockCompanyEntity = null;
        try
        {
            stockCompanyEntity = this.context.getBean( StockCompanyEntity.class );
            stockCompanyEntity.setTickerSymbol( company.getSymbol() );
            stockCompanyEntity.setCompanyName( company.getCompanyName() );
            stockCompanyEntity.setCreateDate( new Timestamp( System.currentTimeMillis() ) );
            stockCompanyEntity.setDiscontinuedInd( false );
            stockCompanyEntity.setIndustry( company.getIndustry() );
            stockCompanyEntity.setSector( company.getSector() );
            stockCompanyEntity.setCompanyURL( company.getWebsite() );
            this.addEntity( stockCompanyEntity );
        }
        catch( EntityVersionMismatchException e )
        {
            logError( methodName, "Failed version check while adding stock company " + company );
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
        final StockCompanyEntity stockCompanyEntity;
        try
        {
            stockCompanyEntity = this.getEntity( StringUtils.truncate( tickerSymbol, StockCompanyEntity.TICKER_SYMBOL_LEN ) );
            stockCompanyEntity.setDiscontinuedInd( true );
            try
            {
                this.saveEntity( stockCompanyEntity );
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
            logError( methodName, "Stock not found in stock {0} table to mark as discontinued.", tickerSymbol );
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

}
