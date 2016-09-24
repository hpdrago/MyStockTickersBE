package com.stocktracker.common;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a generic class that provides the ability to create a copy of the list containing a
 * different object type with the property values copied from the original
 *
 * Created by mike on 9/10/2016.
 */
public class ListCopyProperties<S, T>
{
    private Class<T> targetType;

    /**
     * Constructor
     * @param targetType Need to know the target type so a new instance can be created
     */
    public ListCopyProperties( Class<T> targetType )
    {
        this.targetType = targetType;
    }

    /**
     * Creates a copy of the source list into a new list containing new objects of type <T> containing
     * the properties copied from the original object
     * @param src
     * @return
     */
    public List<T> copy( List<S> src )
    {
        List<T> target = new ArrayList<>();
        for ( S s : src )
        {
            T t = BeanUtils.instantiateClass( targetType );
            BeanUtils.copyProperties( s, t );
            target.add( t );
        }
        return target;
    }
}
