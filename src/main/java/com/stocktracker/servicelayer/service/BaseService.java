package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.entity.AccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by mike on 11/1/2016.
 */
public abstract class BaseService<E,D> implements MyLogger
{
    abstract protected D entityToDTO( final E entity );
    abstract protected E dtoToEntity( final D dto );

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
        final String methodName = "mapStockEntityPageIntoStockDTOPage";
        logMethodBegin( methodName, pageRequest );
        Objects.requireNonNull( pageRequest, "pageRequest cannot be null" );
        Objects.requireNonNull( entityPage, "source cannot be null" );
        Page<D> dtoPage = dtosToPage( pageRequest, entityPage );
        logMethodEnd( methodName, "page size: " + dtoPage.getTotalElements() );
        return dtoPage;
    }

    /**
     * Converts a page of Entities of type <E> to a page of DTOs of type <D>
     * @param pageRequest
     * @param source
     * @return
     */
    protected Page<D> dtosToPage( final @NotNull Pageable pageRequest,
                                  final @NotNull Page<E> source )
    {
        List<D> dtos = this.entitiesToDTOs( source.getContent() );
        return new PageImpl<>( dtos, pageRequest, source.getTotalElements() );
    }

    protected List<D> entitiesToDTOs( final List<E> entities )
    {
        List<D> dtos = new ArrayList<>();
        for ( E entity : entities )
        {
            D dto = this.entityToDTO( entity );
            dtos.add( dto );
        }
        return dtos;
    }

    protected List<E> dtosToEntities( final List<D> dtos )
    {
        List<E> entities = new ArrayList<>();
        for ( D dto : dtos )
        {
            E entity = this.dtoToEntity( dto );
            entities.add( entity );
        }
        return entities;
    }
}
