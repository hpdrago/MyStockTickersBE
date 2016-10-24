package com.stocktracker.servicelayer.entity;

import com.stocktracker.repositorylayer.db.entity.PortfolioEntity;
import org.springframework.beans.BeanUtils;

/**
 * Created by mike on 10/23/2016.
 */
public class PortfolioDomainEntity
{
    private int id;
    private String name;
    private int customerId;

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
}
