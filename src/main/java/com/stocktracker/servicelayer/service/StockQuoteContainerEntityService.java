package com.stocktracker.servicelayer.service;

import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockQuoteUnavailableException;
import com.stocktracker.repositorylayer.entity.VersionedEntity;
import com.stocktracker.servicelayer.stockinformationprovider.StockQuoteFetchMode;
import com.stocktracker.weblayer.dto.VersionedDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This is the base class for services that service DTO's that contain stock quote information.
 * @param <E>
 * @param <D>
 */
public abstract class StockQuoteContainerEntityService<K extends Serializable,
                                                       E extends VersionedEntity<K>,
                                                       D extends StockQuoteService.StockQuoteContainer &
                                                                 VersionedDTO<K>,
                                                       R extends JpaRepository<E,K>>
    extends VersionedEntityService<K,E,D,R>
{
    private StockQuoteService stockQuoteService;
    protected enum StockQuoteFetch
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
        return this.entitiesToDTOs( pageRequest, entityPage, StockQuoteFetch.FETCH );
    }

    /**
     * Converts the entities to DTO and gets the stock quote information.
     * @param entities
     * @return
     */
    protected List<D> entitiesToDTOs( final List<E> entities )
    {
        return this.entitiesToDTOs( entities, StockQuoteFetch.FETCH );
    }

    /**
     * Converts a list of entities to to a list of DTO's.
     * @param entities
     * @return
     */
    protected List<D> entitiesToDTOs( @NotNull final List<E> entities,
                                      @NotNull final StockQuoteFetch stockQuoteFetch )
    {
        List<D> dtos = super.entitiesToDTOs( entities );
        if ( stockQuoteFetch.isFetch() )
        {
            this.updateStockQuoteInformation( dtos );
        }
        return dtos;
    }

    /**
     * Transforms {@code Page<ENTITY>} objects into {@code Page<DTO>} objects.
     *
     * @param pageRequest The information of the requested page.
     * @param entityPage The {@code Page<ENTITY>} object.
     * @param stockQuoteFetch Determines if the stock quotes should be fetched.
     * @return The created {@code Page<DTO>} object.
     */
    protected Page<D> entitiesToDTOs( @NotNull final Pageable pageRequest,
                                      @NotNull final Page<E> entityPage,
                                      @NotNull final StockQuoteFetch stockQuoteFetch )
    {
        final String methodName = "entitiesToDTOs";
        logMethodBegin( methodName, pageRequest );
        Objects.requireNonNull( pageRequest, "pageRequest cannot be null" );
        Objects.requireNonNull( entityPage, "source cannot be null" );
        List<D> dtos = this.entitiesToDTOs( entityPage.getContent() );
        if ( stockQuoteFetch.isFetch() )
        {
            this.updateStockQuoteInformation( dtos );
        }
        logMethodEnd( methodName );
        return new PageImpl<>( dtos, pageRequest, entityPage.getTotalElements() );
    }

    /**
     * Updates the stock quote information for all dtos
     * @param dtos
     */
    protected void updateStockQuoteInformation( final List<D> dtos )
    {
        final String methodName = "updateStockQuoteInformation";
        logMethodBegin( methodName );
        for ( D dto : dtos )
        {
            try
            {
                this.stockQuoteService.setStockQuoteInformation( dto, StockQuoteFetchMode.ASYNCHRONOUS );
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
        logMethodEnd( methodName );
    }

    @Autowired
    public void setStockQuoteService( final StockQuoteService stockQuoteService )
    {
        logInfo( "setStockQuoteService", "Dependency Injection of " + stockQuoteService );
        this.stockQuoteService = stockQuoteService;
    }

    public StockQuoteService getStockQuoteService()
    {
        return stockQuoteService;
    }

}
