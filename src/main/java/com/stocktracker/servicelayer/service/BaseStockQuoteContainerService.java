package com.stocktracker.servicelayer.service;

import com.stocktracker.common.exceptions.StockNotFoundException;
import com.stocktracker.common.exceptions.StockQuoteUnavailableException;
import com.stocktracker.servicelayer.service.stockinformationprovider.StockQuoteFetchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * This is the base class for services that service DTO's that contain stock quote information.
 * @param <E>
 * @param <D>
 */
public abstract class BaseStockQuoteContainerService<E,D extends StockQuoteService.StockQuoteContainer> extends BaseService<E,D>
{
    private StockQuoteService stockQuoteService;

    /**
     * Transforms {@code Page<ENTITY>} objects into {@code Page<DTO>} objects.
     *
     * @param pageRequest The information of the requested page.
     * @param entityPage      The {@code Page<ENTITY>} object.
     * @return The created {@code Page<DTO>} object.
     */
    protected Page<D> entitiesToDTOs( @NotNull final Pageable pageRequest,
                                      @NotNull final Page<E> entityPage )
    {
        final String methodName = "entitiesToDTOs";
        logMethodBegin( methodName, pageRequest );
        Objects.requireNonNull( pageRequest, "pageRequest cannot be null" );
        Objects.requireNonNull( entityPage, "source cannot be null" );
        List<D> dtos = this.entitiesToDTOs( entityPage.getContent() );
        for ( D dto : dtos )
        {
            try
            {
                this.stockQuoteService.setStockQuoteInformation( dto, StockQuoteFetchMode.ASYNCHRONOUS );
            }
            catch ( StockQuoteUnavailableException e )
            {
                logError( methodName, e );
            }
            catch ( StockNotFoundException e )
            {
                logError( methodName, e );
            }
        }
        logMethodEnd( methodName );
        return new PageImpl<>( dtos, pageRequest, entityPage.getTotalElements() );
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
