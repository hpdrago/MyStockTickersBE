package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;
import com.stocktracker.repositorylayer.entity.StockTagEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike on 11/1/2016.
 */
public abstract class BaseService<E,D> implements MyLogger
{
    abstract protected D entityToDTO( final E entity );
    abstract protected E dtoToEntity( final D dto );

    public List<D> entitiesToDTOs( final List<E> entities )
    {
        List<D> dtos = new ArrayList<>();
        for ( E entity : entities )
        {
            D dto = this.entityToDTO( entity );
            dtos.add( dto );
        }
        return dtos;
    }

    public List<E> dtosToEntities( final List<D> dtos )
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
