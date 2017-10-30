package com.stocktracker.servicelayer.service;

import com.stocktracker.common.MyLogger;

import java.math.BigDecimal;
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

    /**
     * Determines the percent of change from the original price to the last price.
     * @param originalPrice
     * @param lastPrice
     * @return A percent of change.
     */
    public BigDecimal calculatePercentOfChange( final BigDecimal originalPrice, final BigDecimal lastPrice )
    {
        if ( lastPrice == null || lastPrice.floatValue() == 0 )
        {
            return new BigDecimal( 0 );
        }
        if ( originalPrice == null || originalPrice.floatValue() == 0 )
        {
            return new BigDecimal( 0 );
        }
        BigDecimal fraction = originalPrice.divide( lastPrice, 4, BigDecimal.ROUND_HALF_UP );
        BigDecimal percentOfChange = new BigDecimal( 1.0 );
        percentOfChange = percentOfChange.subtract( fraction );
        return percentOfChange;
    }

}
