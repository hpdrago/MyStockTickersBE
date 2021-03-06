package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import org.springframework.beans.BeanUtils;
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
 * <EK> The Entity Key
 *  <E> The Entity
 * <DK> The DTO Key
 *  <D> The DTO
 *  <R> The JpaRepository
 * Created by mike on 11/1/2016.
 */
public abstract class BaseEntityService<EK extends Serializable,
                                        E,
                                        DK extends Serializable,
                                        D,
                                        R extends JpaRepository<E, EK>>
    extends BaseService
    implements MyLogger
{
    /**
     * Creates a new entity instance of type <E> and copies the properties with the same name and type
     * to the dto.
     * @param entity Contains the entity information.
     * @return new DTO instance of type <D> with properties copied from the entity.
     */
    public D entityToDTO( final E entity )
    {
        final D dto = this.createDTO();
        BeanUtils.copyProperties( entity, dto );
        return dto;
    }

    /**
     * Subclasses must override this method to create their version of a DTO.
     * @return
     */
    protected abstract D createDTO();

    /**
     * Creates a new entity of type <E> and copies the properties with the same name and data type from the dto.
     * @param dto of type <D>
     * @return new DTO instance with values copied form the entity.
     */
    protected E dtoToEntity( final D dto )
    {
        final E entity = this.createEntity();
        BeanUtils.copyProperties( dto, entity );
        return entity;
    }

    /**
     * Subclasses must override this method to create their instance of an entity.
     * @return
     */
    protected abstract E createEntity();


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
        final Page<D> dtoPage = dtosToPage( pageRequest, entityPage );
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
        final List<D> dtos = this.entitiesToDTOs( source.getContent() );
        return new PageImpl<>( dtos, pageRequest, source.getTotalElements() );
    }

    /**
     * Converts a list of entities to to a list of DTO's.
     * @param entities
     * @return
     */
    public List<D> entitiesToDTOs( final Iterable<E> entities )
    {
        final List<D> dtos = new ArrayList<>();
        for ( final E entity : entities )
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
    protected List<E> dtosToEntities( final Iterable<D> dtos )
    {
        final List<E> entities = new ArrayList<>();
        for ( final D dto : dtos )
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
