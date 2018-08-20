package com.stocktracker.repositorylayer.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * Base list class for all entity lists.
 * @param <K> - Primary key type of the entity.
 * @param <E> - Entity type.
 */
public class BaseEntityList<K extends Serializable, E extends BaseEntity<K>> implements Iterable<E>
{
    @Autowired
    protected ApplicationContext context;

    private List<E> entityList = new ArrayList<>();

    /**
     * Add all of the entities from the {@code entityList} to the internal list.
     * @param entityList
     */
    protected void addEntities( final List<E> entityList )
    {
        this.entityList.addAll( entityList );
    }

    /**
     * Interator for the entity list.
     * @return
     */
    @Override
    public Iterator<E> iterator()
    {
        return entityList.iterator();
    }

    /**
     * Executes {@code action} over all entities.
     * @param action
     */
    @Override
    public void forEach( final Consumer<? super E> action )
    {
        this.entityList
            .forEach( action );
    }

    /**
     * Returns the number of entities in the list.
     * @return
     */
    public int size()
    {
        return this.entityList.size();
    }

    /**
     * Add a new entity to the list.
     * @param entity
     */
    public void add( final E entity )
    {
        this.entityList.add( entity );;
    }
}
