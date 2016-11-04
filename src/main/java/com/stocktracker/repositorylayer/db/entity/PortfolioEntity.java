package com.stocktracker.repositorylayer.db.entity;

import com.stocktracker.weblayer.dto.PortfolioDTO;
import org.springframework.beans.BeanUtils;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
    private int customerId;

    /**
     * Create a new instance from a PortfolioDTO
     * @param portfolioDTO
     * @return
     */
    public static PortfolioEntity newInstance( final PortfolioDTO portfolioDTO )
    {
        PortfolioEntity portfolioEntity = new PortfolioEntity();
        BeanUtils.copyProperties( portfolioDTO, portfolioEntity );
        return portfolioEntity;
    }

    @Id
    @GeneratedValue
    @Column( name = "id", nullable = false )
    public int getId()
    {
        return id;
    }

    public void setId( final int id )
    {
        this.id = id;
    }

    @Basic
    @Column( name = "name", nullable = false, length = 20 )
    public String getName()
    {
        return name;
    }

    public void setName( final String name )
    {
        this.name = name;
    }

    @Basic
    @Column( name = "customer_id", nullable = false )
    public int getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( int customerId )
    {
        this.customerId = customerId;
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
        return getId() == that.getId() &&
            Objects.equals( name, that.name );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( getId(), name );
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "PortfolioEntity{" );
        sb.append( "id=" ).append( id );
        sb.append( ", name='" ).append( name ).append( '\'' );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( '}' );
        return sb.toString();
    }
}
