package com.stocktracker.repositorylayer.db.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Created by mike on 9/4/2016.
 */
@Entity
@Table( name = "portfolio", schema = "stocktracker", catalog = "" )
public class PortfolioEntity
{
    private int id;
    private String name;

    @Id
    @Column( name = "id" )
    public int getId()
    {
        return id;
    }

    public void setId( final int id )
    {
        this.id = id;
    }

    @Basic
    @Column( name = "name" )
    public String getName()
    {
        return name;
    }

    public void setName( final String name )
    {
        this.name = name;
    }

    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }
        final PortfolioEntity that = (PortfolioEntity) o;
        return id == that.id &&
            Objects.equals( name, that.name );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( id, name );
    }
}
