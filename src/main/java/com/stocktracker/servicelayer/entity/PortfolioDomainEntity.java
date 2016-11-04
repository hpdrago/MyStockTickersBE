package com.stocktracker.servicelayer.entity;

import com.stocktracker.repositorylayer.db.entity.PortfolioEntity;
import org.springframework.beans.BeanUtils;

/**
 * This class represents a Portfolio for a customer.  A portfolio is a set of stocks identified by the customer.
 *
 * Created by mike on 10/23/2016.
 */
public class PortfolioDomainEntity
{
    private int id;
    private int customerId;
    private String name;

    /**
     * Create a new empty instance
     * @return
     */
    public static PortfolioDomainEntity newInstance()
    {
        return new PortfolioDomainEntity();
    }

    /**
     * Convert the {@code portfolioEntity} into a {@code PortfolioDomainEntity}
     * @param portfolioEntity
     * @return
     */
    public static PortfolioDomainEntity newInstance( final PortfolioEntity portfolioEntity )
    {
        PortfolioDomainEntity portfolioDomainEntity = new PortfolioDomainEntity();
        BeanUtils.copyProperties( portfolioEntity, portfolioDomainEntity );
        return portfolioDomainEntity;
    }

    private PortfolioDomainEntity()
    {
    }

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public int getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId( int customerId )
    {
        this.customerId = customerId;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder( "PortfolioDomainEntity{" );
        sb.append( "id=" ).append( id );
        sb.append( ", customerId=" ).append( customerId );
        sb.append( ", name='" ).append( name ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }
}
