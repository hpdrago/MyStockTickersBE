package com.stocktracker.servicelayer.service;

import com.stocktracker.repositorylayer.entity.VersionedEntity;
import com.stocktracker.servicelayer.service.stocks.StockInformationService;
import com.stocktracker.servicelayer.service.stocks.StockPriceContainer;
import com.stocktracker.weblayer.dto.VersionedDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * This is the base class for services that service DTO's that contain stock information.
 * @param <E>
 * @param <D>
 */
public abstract class StockInformationEntityService<K extends Serializable,
                                                    E extends VersionedEntity<K>,
                                                    D extends StockPriceContainer &
                                                              VersionedDTO<K>,
                                                    R extends JpaRepository<E,K>>
    extends VersionedEntityService<K,E,D,R>
{
    private StockInformationService stockInformationService;
    protected enum StockPriceFetchAction
    {
        NONE,
        FETCH;
        protected boolean isFetch() { return this == FETCH; }
    }

    /**
     * Transforms {@code Page<ENTITY>} objects into {@code Page<DTO>} objects.
     *
     * @param pageRequest The information of the requested page.
     * @param entityPage The {@code Page<ENTITY>} object.
     * @return The created {@code Page<DTO>} object.
     */
    protected Page<D> entitiesToDTOs( @NotNull final Pageable pageRequest,
                                      @NotNull final Page<E> entityPage )
    {
        return this.entitiesToDTOs( pageRequest, entityPage, StockPriceFetchAction.FETCH );
    }

    /**
     * Converts a list of entities to to a list of DTO's.
     * @param entities
     * @return
     */
    protected List<D> entitiesToDTOs( @NotNull final List<E> entities,
                                      @NotNull final StockPriceFetchAction stockPriceFetchAction )
    {
        final List<D> dtos = super.entitiesToDTOs( entities );
        if ( stockPriceFetchAction.isFetch() )
        {
            this.getStockPrices( dtos );
        }
        return dtos;
    }

    /**
     * Transforms {@code Page<ENTITY>} objects into {@code Page<DTO>} objects.
     *
     * @param pageRequest The information of the requested page.
     * @param entityPage The {@code Page<ENTITY>} object.
     * @param stockPriceFetchAction Determines if the stock quotes should be fetched.
     * @return The created {@code Page<DTO>} object.
     */
    protected Page<D> entitiesToDTOs( @NotNull final Pageable pageRequest,
                                      @NotNull final Page<E> entityPage,
                                      @NotNull final StockPriceFetchAction stockPriceFetchAction )
    {
        final String methodName = "entitiesToDTOs";
        logMethodBegin( methodName, pageRequest );
        Objects.requireNonNull( pageRequest, "pageRequest cannot be null" );
        Objects.requireNonNull( entityPage, "source cannot be null" );
        List<D> dtos = this.entitiesToDTOs( entityPage.getContent(), stockPriceFetchAction );
        logMethodEnd( methodName );
        return new PageImpl<>( dtos, pageRequest, entityPage.getTotalElements() );
    }

    /**
     * Updates the stock quote information for all dtos
     * @param dtos
     */
    protected void getStockPrices( final List<? extends StockPriceContainer> dtos )
    {
        final String methodName = "getStockPrices";
        logMethodBegin( methodName );
        this.stockInformationService.setStockPrice( dtos );
        /*
        for ( D dto : dtos )
        {
            try
            {
                this.stockPriceService.setStockPrice( dto, StockPriceFetchMode.ASYNCHRONOUS );
            }
            catch( StockQuoteUnavailableException e )
            {
                logError( methodName, e );
            }
            catch( StockNotFoundException e )
            {
                logError( methodName, e );
            }
        }
        */
        logMethodEnd( methodName );
    }

    @Autowired
    public void setStockInformationService( final StockInformationService stockInformationService )
    {
        logInfo( "setStockQuoteService", "Dependency Injection of " + stockInformationService );
        this.stockInformationService = stockInformationService;
    }

    public StockInformationService getStockInformationService()
    {
        return stockInformationService;
    }

}
