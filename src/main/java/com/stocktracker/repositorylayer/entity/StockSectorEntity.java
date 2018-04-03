package com.stocktracker.repositorylayer.entity;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Created by mike on 11/19/2016.
 */
@Component
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
@Entity
@Table( name = "stock_sector", schema = "stocktracker", catalog = "" )
public class StockSectorEntity
{
    private Integer id;
    private String sector;

    @Id
    @Column( name = "id", nullable = false )
    public Integer getId()
    {
        return id;
    }

    public void setId( final Integer id )
    {
        this.id = id;
    }

    @Basic
    @Column( name = "sector", nullable = true, length = 30 )
    public String getSector()
    {
        return sector;
    }

    public void setSector( final String primarySectorName )
    {
        this.sector = primarySectorName;
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
        final StockSectorEntity that = (StockSectorEntity) o;
        return Objects.equals( id, that.id ) &&
            Objects.equals( sector, that.sector );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( id, sector );
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "StockSectorEntity{" );
        sb.append( "id=" ).append( id );
        sb.append( ", sector='" ).append( sector ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }

}
