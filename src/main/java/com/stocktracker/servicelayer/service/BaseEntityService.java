package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.servicelayer.service.stocks.StockCompanyContainer;
import com.stocktracker.servicelayer.service.stocks.StockInformationService;
import org.springframework.beans.BeanUtils;
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
 * <ID> The Primary Key
 *  <E> The Entity
 *  <D> The DTO
 *  <R> The JpaRepository
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
    protected StockInformationService stockInformationService;

    /**
     * Creates a new entity instance of type <E> and copies the properties with the same name and type
     * to the dto.
     * @param entity Contains the entity information.
     * @return new DTO instance of type <D> with properties copied from the entity.
     */
    protected D entityToDTO( final E entity )
    {
        final D dto = this.createDTO();
        BeanUtils.copyProperties( entity, dto );
        /*
         * I think this is a good use of instanceof...
         * If any stock DTO is a stock company container, it will be populated automatically with the stock company
         * information.  No need for any sub cvl
         */
        if ( dto instanceof StockCompanyContainer )
        {
            this.stockInformationService
                .setCompanyInformation( (StockCompanyContainer)dto );
        }
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

    @Autowired
    public void setStockInformationService( final StockInformationService stockInformationService )
    {
        this.stockInformationService = stockInformationService;
    }

}
