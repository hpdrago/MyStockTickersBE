package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
 * This is the base class for all services that interface with the database to store and retrieve entities from a single
 * table in the database.
 *
 * Created by mike on 11/1/2016.
 */
public abstract class BaseEntityService<ID extends Serializable,
                                        E,
                                        D,
                                        R extends JpaRepository<E, ID>>
                                        implements MyLogger
{
    @Autowired
    protected ApplicationContext context;

    /**
     * Subclass must override this method to copy properties from the database entity to the DTO.
     * @param entity
     * @return
     */
    abstract protected D entityToDTO( final E entity );

    /**
     * Subclasses must override this method to copy properties from the DTO to the database entity.
     * @param dto
     * @return
     */
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

    /**
     * Converts a list of entities to to a list of DTO's.
     * @param entities
     * @return
     */
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

    /**
     * Converts a list of DTO's to a list of entities.
     * @param dtos
     * @return
     */
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

    /**
     * Get the repository instance that is injected into the subclass services.
     * @return
     */
    protected abstract R getRepository();
}
